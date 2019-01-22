package de.hsos.bachelorarbeit.nh.endpoint.test;

import de.hsos.bachelorarbeit.nh.endpoint.test.entities.DebugInfos;
import de.hsos.bachelorarbeit.nh.endpoint.test.entities.TestRunnerResult;
import de.hsos.bachelorarbeit.nh.endpoint.test.frameworks.JUnit.TestRunner.JUnitTestRunner;
import de.hsos.bachelorarbeit.nh.endpoint.test.frameworks.jacoco.JacocoCodeCoverageCollector;
import de.hsos.bachelorarbeit.nh.endpoint.test.frameworks.jmeter.JMeterTestRunner;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.HealthCheck;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner.CollectCodeCoverage;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner.EndpointTestRunner;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner.UnitTest.UnitTestRunner;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.Watch;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage.CodeCoverageResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestGroupedResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.frameworks.GsonJsonUtil;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@Mojo(name = "test", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class TestMojo extends AbstractMojo{
    @Parameter(required = true, readonly =  true)
    private String destination;

    @Parameter(required = true, readonly =  true)
    private String jMeterFullPath;

    @Parameter(required = true, readonly =  true)
    private String jMeterCMDPluginFullPath;

    @Parameter(readonly =  true, defaultValue = "127.0.0.1")
    String defaultHost;
    @Parameter(readonly =  true, defaultValue = "8080")
    int defaultPort;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(required = false, readonly =  true)
    private String mvnFullPath = System.getenv("mvn");

    @Parameter(readonly =  true)
    boolean isLinux = false;

    //Debug-Optional

    @Parameter(readonly =  true)
    boolean skipValidationTests = false;

    @Parameter(readonly =  true)
    boolean skipCapacityTests = false;

    @Parameter(readonly =  true)
    boolean skipWatchtests = false;

    @Parameter(readonly =  true)
    boolean skipUnitTests = false;

    @Override
    public void execute() throws MojoFailureException {
        Log log = getLog();

        String baseDir = project.getBasedir().getAbsolutePath();
        if(!destination.startsWith(baseDir)) destination = Paths.get(baseDir, destination).toAbsolutePath().toString();
        if(destination.endsWith(".jmx")) destination = Paths.get(destination).getParent().toAbsolutePath().toString();
        if(!isLinux && !mvnFullPath.endsWith(".cmd")) mvnFullPath += ".cmd";

        JsonUtil jsonUtil = new GsonJsonUtil();

        try {
            this.runTests(baseDir, jsonUtil, log);
        } catch (Exception e) {
            throw new MojoFailureException(e.toString());
        }
    }

    private void runTests(String baseDir, JsonUtil jsonUtil, Log log) throws Exception{
        if(!skipUnitTests){
            log.info("Run Unit-/Coverage Tests");
            this.runUnitTests(baseDir, jsonUtil);
        }else{
            log.info("Unit-Tests skipped!");
        }
        log.info("Run Endpoint-Tests");
        this.runEndpointTests(log, jsonUtil);
    }

    private void runEndpointTests(Log log, JsonUtil jsonUtil) throws Exception{
        String healthPath = "/actuator/info";
        String exeInfiURL = "http://" + defaultHost + ":"+defaultPort+"/actuator/executeinfo";
        HealthCheck hc = new HealthCheck(defaultHost, defaultPort, healthPath);
        Watch watch = new Watch(defaultHost,defaultPort+"", jsonUtil);

        DebugInfos debugInfos = new DebugInfos();
        debugInfos.setSkipCapacityTests(skipCapacityTests);
        debugInfos.setSkipValidationTests(skipValidationTests);
        debugInfos.setSkipWatchtests(skipWatchtests);

        EndpointTestRunner testRunner = new JMeterTestRunner(log, jMeterFullPath, jMeterCMDPluginFullPath, destination, hc, watch, debugInfos);
        TestRunnerResult tr = testRunner.runTests();

        try {
            testRunner.collectActuratorInfos(exeInfiURL);
        } catch (Exception e) {
            throw new MojoFailureException(e.toString());
        }
    }

    private void runUnitTests(String baseDir, JsonUtil jsonUtil) throws Exception{
        UnitTestRunner unitTestRunner = new JUnitTestRunner(baseDir, mvnFullPath);
        TestRunnerResult tr = unitTestRunner.runTests(baseDir);
        if(!tr.isSuccess()){
            throw new Exception(tr.getErrorMessage());
        }
        UnitTestGroupedResult unitTestGroupedResult =  unitTestRunner.getResults();
        CollectCodeCoverage cc = new JacocoCodeCoverageCollector(baseDir);
        List<CodeCoverageResult> ccR = cc.collectCodeCoverage();
        unitTestGroupedResult = cc.combine(unitTestGroupedResult, ccR);

        String baseP =Paths.get(destination, "reports", "/coverage/").toAbsolutePath().toString();
        System.out.println(baseP);
        new File(baseP).mkdirs();

        String aggrresultPath = Paths.get(baseP ,"unittestandcoverage-aggregated.json").toAbsolutePath().toString();
        String resultPath = Paths.get(baseP ,"unittestandcoverage.json").toAbsolutePath().toString();

        jsonUtil.writeJson(unitTestGroupedResult.getSummedResult(), aggrresultPath, true);
        jsonUtil.writeJson(unitTestGroupedResult, resultPath, true);
    }
}







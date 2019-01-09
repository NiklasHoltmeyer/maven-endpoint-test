package de.hsos.bachelorarbeit.nh.endpoint.test;

import de.hsos.bachelorarbeit.nh.endpoint.test.entities.DebugInfos;
import de.hsos.bachelorarbeit.nh.endpoint.test.entities.TestRunnerResult;
import de.hsos.bachelorarbeit.nh.endpoint.test.frameworks.jmeter.JMeterTestRunner;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.HealthCheck;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.Watch;
import de.hsos.bachelorarbeit.nh.endpoint.util.frameworks.GsonJsonUtil;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Paths;

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

    //Debug-Optional

    @Parameter(readonly =  true)
    boolean skipValidationTests = false;

    @Parameter(readonly =  true)
    boolean skipCapacityTests = false;

    @Parameter(readonly =  true)
    boolean skipWatchtests = false;

    @Override
    public void execute() throws MojoFailureException {
        Log log = getLog();

        String baseDir = project.getBasedir().getAbsolutePath();
        if(!destination.startsWith(baseDir)) destination = Paths.get(baseDir, destination).toAbsolutePath().toString();
        if(destination.endsWith(".jmx")) destination = Paths.get(destination).getParent().toAbsolutePath().toString();

        String healthPath = "/actuator/info";
        String exeInfiURL = "http://" + defaultHost + ":"+defaultPort+"/actuator/executeinfo";
        HealthCheck hc = new HealthCheck(defaultHost, defaultPort, healthPath);
        JsonUtil jsonUtil = new GsonJsonUtil();
        Watch watch = new Watch(defaultHost,defaultPort+"", jsonUtil);

        DebugInfos debugInfos = new DebugInfos();
        debugInfos.setSkipCapacityTests(skipCapacityTests);
        debugInfos.setSkipValidationTests(skipValidationTests);
        debugInfos.setSkipWatchtests(skipWatchtests);
        System.out.println(debugInfos.toString());

        TestRunner testRunner = new JMeterTestRunner(log, jMeterFullPath, jMeterCMDPluginFullPath, destination, hc, watch, debugInfos);
        TestRunnerResult tr = testRunner.runTests();

        try {
            testRunner.collectActuratorInfos(exeInfiURL);
        } catch (Exception e) {
            throw new MojoFailureException(e.toString());
        }
    }
}





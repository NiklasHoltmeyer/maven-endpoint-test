package de.hsos.bachelorarbeit.nh.endpoint.test;

import de.hsos.bachelorarbeit.nh.endpoint.test.entities.TestRunnerResult;
import de.hsos.bachelorarbeit.nh.endpoint.test.frameworks.jmeter.JMeterTestRunner;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.HealthCheck;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.Watch;
import de.hsos.bachelorarbeit.nh.endpoint.util.frameworks.GsonJsonUtil;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

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

    @Override
    public void execute() throws MojoFailureException {
        Log log = getLog();
        if(destination.endsWith(".jmx")) destination = Paths.get(destination).getParent().toAbsolutePath().toString();
        String healthPath = "/actuator/info";
        String exeInfiURL = "http://" + defaultHost + ":"+defaultPort+"/actuator/executeinfo";
        HealthCheck hc = new HealthCheck(defaultHost, defaultPort, healthPath);
        JsonUtil jsonUtil = new GsonJsonUtil();
        Watch watch = new Watch(defaultHost,defaultPort+"", jsonUtil);
        TestRunner testRunner = new JMeterTestRunner(log, jMeterFullPath, jMeterCMDPluginFullPath, destination, hc, watch);
        TestRunnerResult tr = testRunner.runTests();

        try {
            testRunner.collectActuratorInfos(exeInfiURL);
        } catch (Exception e) {
            throw new MojoFailureException(e.toString());
        }
    }
}



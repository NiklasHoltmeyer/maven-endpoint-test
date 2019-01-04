package de.hsos.bachelorarbeit.nh.endpoint.test;

import de.hsos.bachelorarbeit.nh.endpoint.test.entities.TestRunnerResult;
import de.hsos.bachelorarbeit.nh.endpoint.test.jmeter.JMeterTestRunner;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.HealthCheck;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.nio.file.Paths;

@Mojo(name = "test", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class TestMojo extends AbstractMojo {
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
    public void execute() throws MojoExecutionException {
        if(destination.endsWith(".jmx")) destination = Paths.get(destination).getParent().toAbsolutePath().toString();
        String healthPath = "/actuator/info";

        HealthCheck hc = new HealthCheck(defaultHost, defaultPort, healthPath);
        TestRunner testRunner = new JMeterTestRunner(jMeterFullPath, jMeterCMDPluginFullPath, destination, hc);
        TestRunnerResult tr = testRunner.runTests();
        testRunner.collectActuratorInfos("http://" + defaultHost + ":"+defaultPort+"/actuator/executeinfo");
        getLog().info(tr.toString());
    }
}

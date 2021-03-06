package de.hsos.bachelorarbeit.nh.endpoint.generate;

import de.hsos.bachelorarbeit.nh.endpoint.generate.usecases.getRequests.EndpointUTIL;
import de.hsos.bachelorarbeit.nh.endpoint.generate.frameworks.spring.SpringEndpointUTIL;
import de.hsos.bachelorarbeit.nh.endpoint.generate.frameworks.jmeter.JMeterUtil;
import de.hsos.bachelorarbeit.nh.endpoint.generate.entities.Request;
import de.hsos.bachelorarbeit.nh.endpoint.util.ClassLoaderLoader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Paths;
import java.util.List;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.TEST, threadSafe = true)
public class JMeterGenerateTestMojo extends AbstractMojo {
    @Parameter(required = true, readonly =  true)
    private String destination;

    @Parameter(required = true, readonly =  true)
    private List<String> basePackages;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(readonly =  true)
    String testName;
    @Parameter(readonly =  true, defaultValue = "127.0.0.1")
    String defaultHost;
    @Parameter(readonly =  true, defaultValue = "8080")
    int defaultPort;
    @Parameter(readonly =  true, defaultValue = "5000")
    int defaultMaxLatency = 50000;

    @Override
    public void execute() throws MojoFailureException {
        String baseDir = project.getBasedir().getAbsolutePath();
        String jmxAbsolutePath = (destination.startsWith(baseDir)) ? Paths.get(destination).toString() :  Paths.get(baseDir, destination).toString(); // (jmx=absolute)? jmx : absolutePath(jmx)

        getLog().info("Path: " + jmxAbsolutePath);

        Thread.currentThread().setContextClassLoader(new ClassLoaderLoader().getClassLoader(project));

        EndpointUTIL endpointUTIL = new SpringEndpointUTIL();
        List<Request> requests = endpointUTIL.getRequests(basePackages);
        String _testName = (testName!=null) ? testName :  project.getName();
        JMeterUtil jMeterUtil = new JMeterUtil(getLog(), requests, _testName, defaultHost, defaultPort, defaultMaxLatency);

        try {
            jMeterUtil.createTests(jmxAbsolutePath);
            getLog().info("***********************************");
            getLog().info("***** J-Meter Test Generated! ******");
            getLog().info("***********************************");
            getLog().info("Test(s) generated: " + requests.size());
            getLog().info("***********************************");

            if(requests.size()<=0){
                getLog().info("Basepackages: " + basePackages.toString());
                getLog().info("_testName: " + _testName);
            }
        } catch (Exception e) {
            getLog().error("******************************************");
            getLog().error("***** J-Meter Test Generated failed! ******");
            getLog().error("******************************************");
            throw new MojoFailureException(e.toString());
        }

    }
}

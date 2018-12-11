package de.hsos.bachelorarbeit.endpoint.generate;

import de.hsos.bachelorarbeit.endpoint.generate.usecases.getRequests.EndpointUTIL;
import de.hsos.bachelorarbeit.endpoint.generate.frameworks.spring.SpringEndpointUTIL;
import de.hsos.bachelorarbeit.endpoint.generate.frameworks.jmeter.JMeterUtil;
import de.hsos.bachelorarbeit.endpoint.generate.entities.Request;
import de.hsos.bachelorarbeit.endpoint.util.ClassLoaderLoader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Paths;
import java.util.List;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.PROCESS_TEST_CLASSES, threadSafe = true)
public class JMeterGenerateTestMojo extends AbstractMojo {
    @Parameter(required = true, readonly =  true)
    private String jmxPath;

    @Parameter(required = true, readonly =  true)
    private List<String> basePackages;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(readonly =  true)
    String testName;
    @Parameter(readonly =  true, defaultValue = "localhost")
    String defaultHost;
    @Parameter(readonly =  true, defaultValue = "8080")
    int defaultPort;
    @Parameter(readonly =  true, defaultValue = "5000")
    int defaultMaxLatency = 50000;

    @Override
    public void execute() throws MojoExecutionException{
        String baseDir = project.getBasedir().getAbsolutePath();
        String jmxAbsolutePath = (jmxPath.startsWith(baseDir)) ? Paths.get(jmxPath).toString() :  Paths.get(baseDir, jmxPath).toString(); // (jmx=absolute)? jmx : absolutePath(jmx)

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
            throw new MojoExecutionException(e.toString());
        }

    }
}

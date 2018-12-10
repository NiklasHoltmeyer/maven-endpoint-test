package de.hsos.bachelorarbeit.nh;

import de.hsos.bachelorarbeit.nh.Endpoint.EndpointUTIL;
import de.hsos.bachelorarbeit.nh.Endpoint.SpringEndpointUTIL;
import de.hsos.bachelorarbeit.nh.Endpoint.Request;
import de.hsos.bachelorarbeit.nh.JMeter.JMeterUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
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

        Thread.currentThread().setContextClassLoader(getClassLoader());

        EndpointUTIL endpointUTIL = new SpringEndpointUTIL();
        List<Request> requests = endpointUTIL.getRequests(basePackages);
        String _testName = (testName!=null) ? testName :  project.getName();
        JMeterUtil jMeterUtil = new JMeterUtil(requests, _testName, defaultHost, defaultPort, defaultMaxLatency);

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

    private ClassLoader getClassLoader() throws MojoExecutionException
    {
        //Quelle: https://stackoverflow.com/a/13220011/5026265
        try
        {
            List<String> classpathElements = project.getCompileClasspathElements();
            classpathElements.add(project.getBuild().getOutputDirectory() );
            classpathElements.add(project.getBuild().getTestOutputDirectory() );
            URL urls[] = new URL[classpathElements.size()];

            for ( int i = 0; i < classpathElements.size(); ++i )
            {
                urls[i] = new File( (String) classpathElements.get( i ) ).toURI().toURL();
            }
            return new URLClassLoader(urls, getClass().getClassLoader() );
        }
        catch (Exception e)//gotta catch em all
        {
            throw new MojoExecutionException("Couldn't create a classloader.", e);
        }
    }

}

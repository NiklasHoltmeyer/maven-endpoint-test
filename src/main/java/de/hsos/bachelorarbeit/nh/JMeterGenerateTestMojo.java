package de.hsos.bachelorarbeit.nh;

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

@Mojo(name = "test", defaultPhase = LifecyclePhase.TEST, threadSafe = true)
public class JMeterGenerateTestMojo extends AbstractMojo {
    @Parameter(required = true, readonly =  true)
    private String jmxPath;

    @Parameter(required = true, readonly =  true)
    private List<String> basePackages;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "0", required = true)
    private int minimumCoverage;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {       
        String baseDir = project.getBasedir().getAbsolutePath();
        String jmxAbsolutePath = (jmxPath.startsWith(baseDir)) ? Paths.get(jmxPath).toString() :  Paths.get(baseDir, jmxPath).toString(); // (jmx=absolute)? jmx : absolutePath(jmx)

        Thread.currentThread().setContextClassLoader(getClassLoader());
        /*
        try {
            Coverage coverage = new Coverage(getLog(), basePackages, jmxAbsolutePath);
            CoverageResult coverageResult = coverage.loadResult();
            int threshHold = minimumCoverage < 1? minimumCoverage*100 : minimumCoverage; //range 0 - 100
            boolean result = coverageResult.buildSuccessful(threshHold);
            if(result){
                getLog().info("***********************************");
                getLog().info("***** J-Meter Test Coverage! ******");
                getLog().info("***********************************");
                getLog().info(coverageResult.toString());            
                getLog().info("minimumCoverage: " + threshHold);
                getLog().info("Actual: " + coverageResult.getCoverage());            
                getLog().info("***********************************");
                getLog().info("**** J-Meter Coverage-Success! ****");
                getLog().info("***********************************");
            }else{
                getLog().error("***********************************");
                getLog().error("***** J-Meter Test Coverage! ******");
                getLog().error("***********************************");
                getLog().error("***********************************");
                getLog().error("basePackages:    " + basePackages);
                getLog().error("jmxAbsolutePath: " + jmxAbsolutePath);
                getLog().error("***********************************");                
                getLog().error(coverageResult.toString());            
                getLog().error("minimumCoverage: " + threshHold);
                getLog().error("Actual: " + coverageResult.getCoverage());            
                getLog().error("***********************************");
                getLog().error("**** J-Meter Coverage-Failed! *****");
                getLog().error("***********************************");
            }

            if(!result){
                throw new Exception("Target Coverage: " + threshHold + "%, Actual: " + coverageResult.getCoverage());
            }
        } catch (Exception e) {
            throw new MojoExecutionException(e.toString());
        }
        */
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

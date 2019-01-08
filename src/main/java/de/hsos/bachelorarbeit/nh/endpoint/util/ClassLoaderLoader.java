package de.hsos.bachelorarbeit.nh.endpoint.util;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class ClassLoaderLoader {
    public ClassLoader getClassLoader(MavenProject project) throws MojoFailureException
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
            throw new MojoFailureException("Couldn't create a classloader.", e);
        }
    }
}

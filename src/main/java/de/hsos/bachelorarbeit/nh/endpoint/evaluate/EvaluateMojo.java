package de.hsos.bachelorarbeit.nh.endpoint.evaluate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.frameworks.gson.ReadEndPointGroupInfos;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.frameworks.jmeter.JMeterReadTestResults;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.EvaluateTestResults;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.ReadExecutionInfo;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.ReadTestsResults;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;

@Mojo(name = "evaluate", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class EvaluateMojo  extends AbstractMojo {
    @Parameter(required = true, readonly =  true)
    private String destination;

    @Override
    public void execute() throws MojoExecutionException/*, MojoFailureException*/ {
        String allEndpointResultPath = Paths.get(destination, "reports", "all-endpoints.jmx", "results.xml").toAbsolutePath().toString();
        ReadTestsResults readTestsResults;
        try {
            readTestsResults = new JMeterReadTestResults(allEndpointResultPath);
        } catch (FileNotFoundException e) {
            throw new MojoExecutionException(e.toString());
        }

        ReadExecutionInfo readExecutionInfo = new ReadEndPointGroupInfos(getLog(), allEndpointResultPath);

        //EvaluateTestResults evaluateTestResults = new EvaluateTestResults(readTestsResults, readExecutionInfo);
        //String jsonPath = Paths.get(reportPath, "evaluate.json").toAbsolutePath().toString();

        //try (Writer writer = new FileWriter(jsonPath)) {
            //Gson gson = new GsonBuilder().create();
            //gson.toJson(evaluateTestResults, writer);
        //} catch (IOException e) {
            //getLog().error(e.toString());
        //}
    }
}

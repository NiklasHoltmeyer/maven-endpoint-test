package de.hsos.bachelorarbeit.nh.endpoint.evaluate;

import com.google.gson.Gson;
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
import java.nio.file.Paths;

@Mojo(name = "evaluate", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class EvaluateMojo  extends AbstractMojo {
    @Parameter(required = true, readonly =  true)
    private String reportPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ReadTestsResults readTestsResults = null;
        try {
            readTestsResults = new JMeterReadTestResults(reportPath);
        } catch (FileNotFoundException e) {
            throw new MojoExecutionException(e.toString());
        }

        ReadExecutionInfo readExecutionInfo = new ReadEndPointGroupInfos(getLog(), reportPath);

        EvaluateTestResults evaluateTestResults = new EvaluateTestResults(readTestsResults, readExecutionInfo);
        String json =new Gson().toJson(evaluateTestResults);
        getLog().info(json);
    }
}

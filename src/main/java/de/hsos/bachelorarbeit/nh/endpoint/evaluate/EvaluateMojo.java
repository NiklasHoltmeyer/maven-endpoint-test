package de.hsos.bachelorarbeit.nh.endpoint.evaluate;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.frameworks.jmeter.JMeterReadTestResults;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.CombineResults;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.ReadExecutionInfo;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.ReadTestsResults;
import de.hsos.bachelorarbeit.nh.endpoint.util.frameworks.GsonJsonUtil;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

@Mojo(name = "evaluate", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class EvaluateMojo  extends AbstractMojo{
    @Parameter(required = true, readonly =  true)
    private String destination;

    @Override
    public void execute() throws MojoFailureException {
        String reportPath = Paths.get(destination, "reports").toAbsolutePath().toString();
        //String allEndpointResultPath = Paths.get(reportPath, "all-endpoints.jmx", "results.xml").toAbsolutePath().toString();
        ReadTestsResults readTestsResults;
        JsonUtil jsonUtil = new GsonJsonUtil();
        try {
            readTestsResults = new JMeterReadTestResults(reportPath);
        } catch (FileNotFoundException e) {
            throw new MojoFailureException(e.toString());
        }
        ReadExecutionInfo readExecutionInfo;
        try {
            readExecutionInfo = new ReadExecutionInfo(reportPath, jsonUtil);
        } catch (FileNotFoundException e) {
            throw new MojoFailureException(e.toString());
        }

        CombineResults cR = new CombineResults(readTestsResults, readExecutionInfo);
        TestResultGroup result = cR.getTestResultGroup();

        //EvaluateTestResults evaluateTestResults = new EvaluateTestResults(readTestsResults, readExecutionInfo);
        //getLog().info(evaluateTestResults.toString());
        //getLog().info(evaluateTestResults.getTestGroupOlds().toString());
        //String jsonPath = Paths.get(reportPath, "evaluate.json").toAbsolutePath().toString();

        try {
            jsonUtil.writeJson(result, "c:/Users/nikla/Desktop/hitler.json");
        } catch (IOException e) {
            throw new MojoFailureException(e.toString());
        }
    }
}


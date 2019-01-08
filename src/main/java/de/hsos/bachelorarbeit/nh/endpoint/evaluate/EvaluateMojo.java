package de.hsos.bachelorarbeit.nh.endpoint.evaluate;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.frameworks.jmeter.JMeterReadTestResults;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.frameworks.jmeter.PublishResultsHttpClient;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.*;
import de.hsos.bachelorarbeit.nh.endpoint.util.frameworks.GsonJsonUtil;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;
import org.apache.maven.plugin.AbstractMojo;
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

    @Parameter(required = false, readonly =  true)
    private String historyServer = "http://localhost:5089";

    @Override
    public void execute() throws MojoFailureException {
        String reportPath = Paths.get(destination, "reports").toAbsolutePath().toString();
        //String allEndpointResultPath = Paths.get(reportPath, "all-endpoints.jmx", "results.xml").toAbsolutePath().toString();
        ReadTestsResults readTestsResults;
        ReadWatchResults readWatchResults;
        JsonUtil jsonUtil = new GsonJsonUtil();
        PublishResults publishResults = new PublishResultsHttpClient(historyServer);
        try{
            readWatchResults = new ReadWatchResults(jsonUtil, reportPath);
        }catch (FileNotFoundException e) {
            throw new MojoFailureException(e.toString());
        }
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

        CombineResults cR = null;
        try {
            cR = new CombineResults(readTestsResults, readExecutionInfo, readWatchResults);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoFailureException(e.toString());

        }
        TestResultGroup result = cR.getTestResultGroup();
        boolean success = false;
        try {
            success = publishResults.publish(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MojoFailureException(e.toString());
        }
        String text = (success)? "Result Successfully pushed" : "Result could not be pushed!";
        System.out.println(text);
    }
}


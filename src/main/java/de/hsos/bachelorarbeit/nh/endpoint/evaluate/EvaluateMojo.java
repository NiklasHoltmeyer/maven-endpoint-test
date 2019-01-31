package de.hsos.bachelorarbeit.nh.endpoint.evaluate;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.AVGResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.EvaluateResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.frameworks.jmeter.JMeterReadTestResults;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.*;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestGroupedResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.coverage.CoverageResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.coverage.CoverageResultAggregated;
import de.hsos.bachelorarbeit.nh.endpoint.util.frameworks.GsonJsonUtil;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.HttpClientHelper;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.nio.file.Paths;
import java.util.List;

@Mojo(name = "evaluate", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class EvaluateMojo  extends AbstractMojo{
    @Parameter(required = true, readonly =  true)
    private String destination;

    @Parameter(required = false, readonly =  true)
    private String historyServer = "http://localhost:5089";

    @Parameter(required = true, readonly =  true)
    private String uniqueProjectID;

    @Override
    public void execute() throws MojoFailureException {
        String reportPath = Paths.get(destination, "reports").toAbsolutePath().toString();

        //String allEndpointResultPath = Paths.get(reportPath, "all-endpoints.jmx", "results.xml").toAbsolutePath().toString();
        JsonUtil jsonUtil = new GsonJsonUtil();

        try{
            String historyURL = historyServer+"/testResult/"+uniqueProjectID;
            TestResultGroup result = collectResults(jsonUtil, reportPath, historyURL);

            String reportP =Paths.get(destination, "/reports/").toAbsolutePath().toString();
            String detailedPath = Paths.get(reportP ,"result-detailed.json").toAbsolutePath().toString();
            String resultPath = Paths.get(reportP ,"result.json").toAbsolutePath().toString();

            Evaluate evaluate = new Evaluate();
            EvaluateResult evaluateResult = evaluate.evaluate(result);

            if(result.isFirstTestBuild() || result.didBuildPass()){
                List<TestResult> results = evaluateResult.getTestResultGroup().getResultGroups();
                String jsonString = jsonUtil.toJson(results);
                HttpClientHelper http = new HttpClientHelper(jsonUtil);
                boolean response = http.putRequest(jsonString, historyURL);
                System.out.println("Publish-Result: " + (response? "done" : "error" ));
            }

            jsonUtil.writeJson(evaluateResult, detailedPath, true);
            evaluateResult.nullDetails();
            jsonUtil.writeJson(evaluateResult, resultPath, true);
        }catch(Exception e){
            e.printStackTrace();
            throw new MojoFailureException(e.toString());
        }

        /*
        try {
            success = publishResults.publish(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MojoFailureException(e.toString());
        }
        */
        //String text = (success)? "Result Successfully pushed" : "Result could not be pushed!";
        //System.out.println(text);
    }

    private TestResultGroup collectResults(JsonUtil jsonUtil, String reportPath, String historyServerFullPath) throws Exception{
        ReadTestsResults readTestsResults;
        ReadWatchResults readWatchResults;
        readWatchResults = new ReadWatchResults(jsonUtil, reportPath);
        readTestsResults = new JMeterReadTestResults(reportPath);

        ReadExecutionInfo readExecutionInfo;
        readExecutionInfo = new ReadExecutionInfo(reportPath, jsonUtil);


        CombineResults cR = new CombineResults(readTestsResults, readExecutionInfo, readWatchResults);

        String reportP =Paths.get(destination, "/reports/").toAbsolutePath().toString();
        String baseP =Paths.get(reportP, "/coverage/").toAbsolutePath().toString();
        String unitTestAndCoveragePath = Paths.get(baseP ,"unittestandcoverage.json").toAbsolutePath().toString();
        //String aggregatedUnitTestAndCoveragePath = Paths.get(baseP ,"unittestandcoverage-aggregated.json").toAbsolutePath().toString();

        String endpointCoveragePath = Paths.get(baseP, "endpointcoverage.json").toAbsolutePath().toString();
        String aggregatedEndpointCoveragePath = Paths.get(baseP ,"endpointcoverage-aggregated.json").toAbsolutePath().toString();

        //UnitTestResult aggregatedUnitTestResults = jsonUtil.fromJsonFile(aggregatedUnitTestAndCoveragePath, UnitTestResult.class);
        CoverageResultAggregated aggregatedEndpointCoverage = jsonUtil.fromJsonFile(aggregatedEndpointCoveragePath, CoverageResultAggregated.class);

        UnitTestGroupedResult unitTestGroupedResult = jsonUtil.fromJsonFile(unitTestAndCoveragePath, UnitTestGroupedResult.class);

        TestResultGroup result = cR.getTestResultGroup();
        result.setAggregatedEndpointCoverage(aggregatedEndpointCoverage);
        //result.setAggregatedUnitTestResults(aggregatedUnitTestResults);
        result.setUnitTestResults(unitTestGroupedResult);

        CoverageResult coverageResult = jsonUtil.fromJsonFile(endpointCoveragePath, CoverageResult.class);

        cR.addEndpointCoverageResults(coverageResult);

        try{
            AVGResult[] meanTurnAroundTime = jsonUtil.fromJsonURL(historyServerFullPath + "/meanTurnAroundTime/", AVGResult[].class);
            result.setMeanTurnAroundTime(meanTurnAroundTime);
        }catch(Exception e){}
        try{
            AVGResult[] meanResponseTime = jsonUtil.fromJsonURL(historyServerFullPath + "/meanResponseTime/", AVGResult[].class);
            result.setMeanResponseTime(meanResponseTime);
        }catch(Exception e){}
        try{
            AVGResult[] meanThroughPut = jsonUtil.fromJsonURL(historyServerFullPath + "/meanThroughPut/", AVGResult[].class);
            result.setMeanThroughPut(meanThroughPut);
        }catch(Exception e){}
        try{
            AVGResult[] meanCPUUtil = jsonUtil.fromJsonURL(historyServerFullPath + "/meanCPUUtil/", AVGResult[].class);
            result.setMeanCPUUtil(meanCPUUtil);
        }catch(Exception e){}
        try{
            AVGResult[] meanMemUtil = jsonUtil.fromJsonURL(historyServerFullPath + "/meanMemUtil/", AVGResult[].class);
            result.setMeanMemUtil(meanMemUtil);
        }catch(Exception e){}
        try{
            AVGResult[] meanMaxTransactions = jsonUtil.fromJsonURL(historyServerFullPath + "/meanMaxTransactions/", AVGResult[].class);
            result.setMeanMaxTransactions(meanMaxTransactions);
        }catch(Exception e){}

        return result;
    }
}


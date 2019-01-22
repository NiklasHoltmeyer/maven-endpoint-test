package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestGroupedResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.WatchResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.coverage.CoverageResultAggregated;

import java.util.List;
import java.util.Optional;

public class TestResultGroup {
    List<TestResult> resultGroups;

    UnitTestGroupedResult unitTestResults;

    CoverageResultAggregated aggregatedEndpointCoverage;

    public List<TestResult> getResultGroups() {
        return resultGroups;
    }

    public void setResultGroups(List<TestResult> resultGroups) {
        this.resultGroups = resultGroups;
    }

    public void addResultGroup(TestResult result){
        this.resultGroups.add(result);
    }

    public CoverageResultAggregated getAggregatedEndpointCoverage() {
        return aggregatedEndpointCoverage;
    }

    public void setAggregatedEndpointCoverage(CoverageResultAggregated aggregatedEndpointCoverage) {
        this.aggregatedEndpointCoverage = aggregatedEndpointCoverage;
    }

    public UnitTestGroupedResult getUnitTestResults() {
        return unitTestResults;
    }

    public void setUnitTestResults(UnitTestGroupedResult unitTestResults) {
        this.unitTestResults = unitTestResults;
    }

    public boolean didFail(){
        return resultGroups.stream().filter(TestResult::isFailed).findFirst().orElse(null) != null;
    }

    /**
     *
     * @param path (/bla/{id})
     * @param method
     * @return
     */
    public Optional<TestResult> getTestResult(String path, String method){
        return this.resultGroups.stream()
                .filter(x-> x.compare(path, method))
                .findFirst();
    }

    /**
     *
     * @param path (/bla/1)
     * @param method
     * @return
     */
    public Optional<TestResult> getTestResultServletURL(String path, String method){
        return this.resultGroups.stream()
                .filter(x->x.compareServletURL(path, method))
                .findFirst();
    }

    private void nullErrors(){
        this.resultGroups.forEach(x->{
            x.setTestRequestResults(null);
            if(x.getAverageLatency() != null && x.getAverageLatency().isNaN()) x.setAverageLatency(null);
            if(x.getMeanTurnAroundTime() != null && x.getMeanTurnAroundTime().isNaN()) x.setMeanTurnAroundTime(null);
            if(x.getMeanResponseSize()!= null && x.getMeanResponseSize().getValue().isEmpty()) x.setMeanResponseSize(null);
            if(x.getMaxCapacity()!= null  && x.getMaxCapacity().getValue() != null && x.getMaxCapacity().getValue().isEmpty()) x.setMaxCapacity(null);
            if(x.getAverageSize() != null && x.getAverageSize().getValue() != null  && x.getAverageSize().getValue().isNaN()) x.setAverageSize(null);
            if(x.getThroughPut() != null  && x.getThroughPut().getValue() != null && x.getThroughPut().getValue().isNaN()) x.setThroughPut(null);
            //getMeanResponseTime
        });
    }

    public void nullRedundant(){
        this.nullErrors();
        this.unitTestResults.setResults(null);
        this.resultGroups.forEach(x->{
            WatchResultGroup wrg = x.getWatches();
            if(wrg!=null){
                if(wrg.getCpu() != null && wrg.getCpu().isSuccess()) wrg.getCpu().setSuccess(null);
                if(wrg.getMemory() != null && wrg.getMemory().isSuccess()) wrg.getMemory().setSuccess(null);
            }
        });
    }
}

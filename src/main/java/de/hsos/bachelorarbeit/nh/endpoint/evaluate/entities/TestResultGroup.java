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

    AVGResult[] meanTurnAroundTime;
    AVGResult[] meanResponseTime;
    AVGResult[] meanThroughPut;
    AVGResult[] meanCPUUtil;
    AVGResult[] meanMemUtil;
    AVGResult[] meanMaxTransactions;


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

    public void nullErrors(){
        this.resultGroups.forEach(TestResult::nullErrors);
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

        this.meanTurnAroundTime = null;
        this.meanResponseTime = null;
        this.meanThroughPut = null;
        this.meanCPUUtil = null;
        this.meanMemUtil = null;
        this.meanMaxTransactions = null;
    }

    public AVGResult[] getMeanTurnAroundTime() {
        return meanTurnAroundTime;
    }

    public void setMeanTurnAroundTime(AVGResult[] meanTurnAroundTime) {
        this.meanTurnAroundTime = meanTurnAroundTime;
    }

    public AVGResult[] getMeanResponseTime() {
        return meanResponseTime;
    }

    public void setMeanResponseTime(AVGResult[] meanResponseTime) {
        this.meanResponseTime = meanResponseTime;
    }

    public AVGResult[] getMeanThroughPut() {
        return meanThroughPut;
    }

    public void setMeanThroughPut(AVGResult[] meanThroughPut) {
        this.meanThroughPut = meanThroughPut;
    }

    public AVGResult[] getMeanCPUUtil() {
        return meanCPUUtil;
    }

    public void setMeanCPUUtil(AVGResult[] meanCPUUtil) {
        this.meanCPUUtil = meanCPUUtil;
    }

    public AVGResult[] getMeanMemUtil() {
        return meanMemUtil;
    }

    public void setMeanMemUtil(AVGResult[] meanMemUtil) {
        this.meanMemUtil = meanMemUtil;
    }

    public AVGResult[] getMeanMaxTransactions() {
        return meanMaxTransactions;
    }

    public void setMeanMaxTransactions(AVGResult[] meanMaxTransactions) {
        this.meanMaxTransactions = meanMaxTransactions;
    }


    public void initAdequacy(boolean isFirstBuild){
        if(meanTurnAroundTime != null){
            for(AVGResult x : meanTurnAroundTime){
                TestResult tr = this.getTestResult(x.getPath(), x.getMethod()).orElse(null);
                if(tr!=null && tr.getMeanTurnAroundTime() != null && x.getValue() > 0){
                    tr.setMeanTurnAroundTimeAdequacy(tr.getMeanTurnAroundTime()/x.getValue());
                }else if(tr != null && tr.getMeanTurnAroundTime() != null){
                    tr.setMeanTurnAroundTimeAdequacy(1.0);
                }
            }
        }
        if(meanResponseTime != null){
            for(AVGResult x : meanResponseTime){
                TestResult tr = this.getTestResult(x.getPath(), x.getMethod()).orElse(null);
                if(tr!=null && tr.getMeanResponseTime() != null && tr.getMeanResponseTime().getValue() != null && x.getValue() != null && x.getValue() > 0){
                    tr.setMeanResponseTimeAdequacy(tr.getMeanResponseTime().getValue()/x.getValue());
                }else if(tr != null && tr.getMeanResponseTime() != null && tr.getMeanResponseTime().getValue() != null){
                    tr.setMeanResponseTimeAdequacy(1.0);
                }
            }
        }
        if(meanThroughPut != null){
            for(AVGResult x : meanThroughPut){
                TestResult tr = this.getTestResult(x.getPath(), x.getMethod()).orElse(null);
                if(tr!=null && tr.getThroughPut() != null && tr.getThroughPut().getValue() != null && x.getValue() != null && x.getValue() > 0){
                    tr.setThroughPutAdequacy(tr.getThroughPut().getValue()/x.getValue());
                }else if(tr!=null && tr.getThroughPut() != null && tr.getThroughPut().getValue() != null){
                    tr.setThroughPutAdequacy(1.0);
                }
            }
        }
        if(meanCPUUtil != null){
            for(AVGResult x : meanCPUUtil){
                TestResult tr = this.getTestResult(x.getPath(), x.getMethod()).orElse(null);
                if(tr!=null && x.getValue() != null && x.getValue() > 0 && tr.getWatches() != null && tr.getWatches().getCpu() != null && tr.getWatches().getCpu().getAverage() != null && tr.getWatches().getCpu().getAverage().getValue() != null){
                    tr.setCpuUtilAdequacy(tr.getWatches().getCpu().getAverage().getValue()/x.getValue());
                }else if(tr != null){
                    tr.setCpuUtilAdequacy(1.0);
                }
            }
        }
        if(meanMemUtil != null){
            for(AVGResult x : meanMemUtil){
                TestResult tr = this.getTestResult(x.getPath(), x.getMethod()).orElse(null);
                if(tr!=null && x.getValue() != null && x.getValue() > 0 && tr.getWatches() != null && tr.getWatches().getMemory() != null && tr.getWatches().getMemory().getAverage() != null && tr.getWatches().getMemory().getAverage().getValue() != null){
                    tr.setMemUtilAdequacy(tr.getWatches().getMemory().getAverage().getValue()/x.getValue());
                }else if(tr != null){
                    tr.setMemUtilAdequacy(1.0);
                }
            }
        }
        if(meanMaxTransactions != null){
            for(AVGResult x : meanMaxTransactions){
                TestResult tr = this.getTestResult(x.getPath(), x.getMethod()).orElse(null);
                if(tr!=null && x.getValue() != null && x.getValue() > 0 && tr.getMaxCapacity() != null && tr.getMaxCapacity().getValue() != null){
                    try{
                        Double d = Double.valueOf(tr.getMaxCapacity().getValue());
                        if(d!=null) tr.setMaxCapacityAdequacy(d / x.getValue());
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }else if(tr != null){
                    tr.setMaxCapacityAdequacy(1.0);
                }
            }
        }
    }

    public boolean isFirstTestBuild(){
        return meanTurnAroundTime == null && meanResponseTime == null && meanThroughPut == null && meanCPUUtil == null && meanMemUtil == null && meanMaxTransactions == null;
    }

    public boolean didBuildPass(){
        return true; //todo spaeter adequecy undso angucken
    }
}

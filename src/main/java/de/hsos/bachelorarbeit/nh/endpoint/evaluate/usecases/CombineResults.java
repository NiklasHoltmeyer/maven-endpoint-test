package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndpointGroupInfo;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestRequestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.WatchResultGroup;

import java.util.ArrayList;
import java.util.List;

public class CombineResults {
    TestResultGroup testResultGroup;
    public CombineResults(ReadTestsResults readTestsResults, ReadExecutionInfo readExecutionInfo, ReadWatchResults readWatchResults) throws Exception {
        testResultGroup = readTestsResults.getTestResult();
        prePare();
        List<EndpointGroupInfo> endpointGroupInfos = readExecutionInfo.getEndPointGroupInfo();
        List<WatchResultGroup> watchResultGroups = readWatchResults.getWatchResultGroup();
        this.addEndpointInfo(endpointGroupInfos);
        this.addWatchResultGroups(watchResultGroups);
        nullRedundantFields();
    }

    private void prePare(){
        testResultGroup.getResultGroups().stream()
                .forEach(x->{
                    TestRequestResult tRR =
                            x.getTestRequestResults().stream()
                            .filter(y-> methodAndPathNotNull(y.getMethod(), y.getUrlParameterLess()))
                            .findFirst().orElse(null);
                    if(tRR!=null){
                        x.setMethod(tRR.getMethod());
                        x.setPath(tRR.getUrlParameterLess());
                    }
                });
    }

    private void nullRedundantFields(){
        testResultGroup.getResultGroups().stream()
                .forEach(this::nullTestResult);
    }

    private void nullTestResult(TestResult tr){
        //tr.getTestRequestResults().forEach(this::nullTestRequestResult);
        //^^ eh transient
        WatchResultGroup wrg = tr.getWatches();
        wrg.setMethod(null);
        wrg.setPath(null);
        //TR haellt Method&Path
    }

    //private void nullTestRequestResult(TestRequestResult testRequestResult){
        //testRequestResult.setUrlParameterLess(null);
        //testRequestResult.setMethod(null);
        //testRequestResult.setPath(null);
        //
    //}

    private boolean methodOrPathNull(String method, String path){
        return method==null||method.isEmpty() || path==null || path.isEmpty();
    }
    private boolean methodAndPathNotNull(String method, String path){
        return method!=null && !method.isEmpty() &&  path!=null && !path.isEmpty();
    }

    public void addEndpointInfo(List<EndpointGroupInfo> endpointGroupInfos){
        endpointGroupInfos.stream()
                .forEach(this::addEndpointInfo);
    }

    private void addEndpointInfo(EndpointGroupInfo endpointInfo) {
        String url = endpointInfo.getPath();
        String method = endpointInfo.getMethod();
        String p2 = endpointInfo.getEndpointExecutionInfoAveraged().getPath();
        //System.out.println("endpointInfo: " + endpointInfo);
        //System.out.println("p2: " + p2);
        //System.out.println("url: " + url);

        TestResult tr = this.testResultGroup.getTestResultServletURL(url, method).orElse(null);
        if(tr == null){
            tr = new TestResult(url, method, new ArrayList<>());
            tr.setPath(url);
            tr.setMethod(method);
            this.testResultGroup.addResultGroup(tr);
        }
        tr.setEndpointExecutionInfosAverage(endpointInfo.getEndpointExecutionInfoAveraged());
        //System.out.println("Path: " + tr.getPath());
        //System.out.println("");
    }

    private void addWatchResultGroups(List<WatchResultGroup> watchResultGroups){
        watchResultGroups.stream()
                .forEach(this::watchResultGroup);
    }

    private void watchResultGroup(WatchResultGroup watchResultGroup) {
        String url = watchResultGroup.getPath();
        String method = watchResultGroup.getMethod();
        TestResult tr = this.testResultGroup.getTestResult(url, method).orElse(null);
        if(tr==null && methodAndPathNotNull(method,url)){
            tr = new TestResult(url, method, new ArrayList<>());
            this.testResultGroup.addResultGroup(tr);
        }
        tr.setPath(url);
        tr.setMethod(method);
        tr.setWatches(watchResultGroup);
    }

//    getTestResult

    public TestResultGroup getTestResultGroup() {
        return testResultGroup;
    }
}


package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndpointGroupInfo;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;

import java.util.ArrayList;
import java.util.List;

public class CombineResults {
    TestResultGroup testResultGroup;
    public CombineResults(ReadTestsResults readTestsResults, ReadExecutionInfo readExecutionInfo) {
        testResultGroup = readTestsResults.getTestResult();
        this.addEndpointInfo(readExecutionInfo.getEndPointGroupInfo());
    }

    public void addEndpointInfo(List<EndpointGroupInfo> endpointGroupInfos){
        endpointGroupInfos.stream().forEach(this::addEndpointInfo);
    }

    private void addEndpointInfo(EndpointGroupInfo endpointInfo) {
        String url = endpointInfo.getPath();
        String method = endpointInfo.getMethod();
        TestResult tr = this.testResultGroup.getTestResult(url, method).orElse(null);
        if(tr == null){
            tr = new TestResult(url, method, new ArrayList<>());
            this.testResultGroup.addResultGroup(tr);
        }
        tr.setEndpointExecutionInfos(endpointInfo.getEndpointExecutionInfos());
        tr.setPath(url);
        tr.setMethod(method);
    }

    public TestResultGroup getTestResultGroup() {
        return testResultGroup;
    }
}

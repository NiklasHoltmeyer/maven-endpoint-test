package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndpointExecutionInfo;

import java.util.List;
import java.util.Optional;

public class TestGroup {
    String path;
    String method;
    List<TestResult> testResults;
    List<EndpointExecutionInfo> executionInfos;

    Double meanResponseTime;
    Double meanTurnaroundTime;
    Double throughput;

    public TestGroup(String path, String method, List<TestResult> testResults) {
        this.path = path;
        this.method = method;
        this.testResults = testResults;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResult> testResults) {
        this.testResults = testResults;
    }

    public Double getMeanResponseTime() {
        return meanResponseTime;
    }

    public void setMeanResponseTime(Double meanResponseTime) {
        this.meanResponseTime = meanResponseTime;
    }

    public Double getMeanTurnaroundTime() {
        return meanTurnaroundTime;
    }

    public void setMeanTurnaroundTime(Double meanTurnaroundTime) {
        this.meanTurnaroundTime = meanTurnaroundTime;
    }

    public Double getThroughput() {
        return throughput;
    }

    public void setThroughput(Double throughput) {
        this.throughput = throughput;
    }

    public boolean isSuccess(){
        Optional<TestResult> tR = this.getTestResults().stream().filter(tr->!tr.isSuccess()).findFirst();
        return !tR.isPresent();
    }

    public List<EndpointExecutionInfo> getExecutionInfos() {
        return executionInfos;
    }

    @Override
    public String toString() {
        return "TestGroup{" +
                "path='" + path + '\'' +
                ", method='" + method + '\'' +
                ", testResults=" + testResults +
                ", executionInfos=" + executionInfos +
                ", meanResponseTime=" + meanResponseTime +
                ", meanTurnaroundTime=" + meanTurnaroundTime +
                ", throughput=" + throughput +
                '}';
    }

    public void setExecutionInfos(List<EndpointExecutionInfo> executionInfos) {
        this.executionInfos = executionInfos;
    }

}

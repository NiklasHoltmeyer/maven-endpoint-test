package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndPointExecutionInfo.EndpointExecutionInfo;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.WatchResultGroup;

import java.util.List;

public class TestResult extends Endpoint{
    transient List<TestRequestResult> testRequestResults;
    WatchResultGroup watches;

    Double averageLatency;
    Double averageElapsedTime;
    Unit<String> maxCapacity;

    Unit<Double> averageSize;
    Unit<Double> throughPut;

    EndpointExecutionInfo endpointExecutionInfosAverage;

    int testCount = 0;

    public TestResult(String path, String method, List<TestRequestResult> testRequestResults) {
        super(path, method);
        this.testRequestResults = testRequestResults;
        init();
    }

    private void init(){
        Double summedLatency = 0.0;
        Double summedElapsedTime = 0.0;
        long summedSize = 0L;

        for(TestRequestResult result : testRequestResults){
            summedLatency += result.getLatency();
            summedElapsedTime += result.getElapsedTime();
            summedSize += result.getSize();
        }

        int count = testRequestResults.size();
        if(count > 0){
            averageLatency = summedLatency / count;
            averageElapsedTime = summedElapsedTime / count;
            averageSize = new Unit<>((double)(summedSize / count), "b");
        }

        calculateThroughPut();
    }

    public List<TestRequestResult> getTestRequestResults() {
        return testRequestResults;
    }

    public void setTestRequestResults(List<TestRequestResult> testRequestResults) {
        this.testRequestResults = testRequestResults;
    }

    public Double getAverageLatency() {
        return averageLatency;
    }

    public void setAverageLatency(Double averageLatency) {
        this.averageLatency = averageLatency;
    }

    public Double getAverageElapsedTime() {
        return averageElapsedTime;
    }

    public void setAverageElapsedTime(Double averageElapsedTime) {
        this.averageElapsedTime = averageElapsedTime;
    }

    public int getTestCount() {
        return testCount;
    }

    public void setTestCount(int testCount) {
        this.testCount = testCount;
    }

    public boolean isFailed(){
        return this.testRequestResults.stream().filter(tr -> !tr.isSuccess()).findFirst().orElse(null) != null;
    }

    /**
     *
     * @param url /bla/1
     * @param method
     * @return
     */
    public boolean compareServletURL(String url, String method){
        return this.testRequestResults.stream()
                .filter(tr->tr.compareServletURL(url, method))
                .findFirst()
                .orElse(null) != null;
    }

    private void calculateThroughPut(){
        double totalTime = this.testRequestResults.stream().mapToDouble(TestRequestResult::getElapsedTime).sum();
        double totalRequests = this.testRequestResults.stream().mapToDouble(TestRequestResult::getRequestCount).sum();
        double throughPutMS = totalRequests / totalTime;
        this.throughPut = new Unit<>(throughPutMS / 1000, "s");
    }

    public Unit<Double> getThroughPut() {
        return throughPut;
    }

    public void setThroughPut(Unit<Double> throughPut) {
        this.throughPut = throughPut;
    }

    public Unit<String> getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Unit<String> maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setAverageSize(Unit<Double> averageSize) {
        this.averageSize = averageSize;
    }

    public Unit<Double> getAverageSize() {
        return averageSize;
    }

    public EndpointExecutionInfo getEndpointExecutionInfosAverage() {
        return endpointExecutionInfosAverage;
    }

    public void setEndpointExecutionInfosAverage(EndpointExecutionInfo endpointExecutionInfosAverage) {
        this.endpointExecutionInfosAverage = endpointExecutionInfosAverage;
    }

    public WatchResultGroup getWatches() {
        return watches;
    }

    public void setWatches(WatchResultGroup watches) {
        this.watches = watches;
    }


    @Override
    public String toString() {
        return "TestResult{" +
                "\ttestRequestResults=" + testRequestResults +
                "\t, watches=" + watches +
                "\t, averageLatency=" + averageLatency +
                "\t, averageElapsedTime=" + averageElapsedTime +
                "\t, maxCapacity=" + maxCapacity +
                "\t, averageSize=" + averageSize +
                "\t, throughPut=" + throughPut +
                "\t, endpointExecutionInfosAverage=" + endpointExecutionInfosAverage +
                "\t, path='" + path + '\'' +
                "\t, method='" + method + '\'' +
                '}';
    }
}

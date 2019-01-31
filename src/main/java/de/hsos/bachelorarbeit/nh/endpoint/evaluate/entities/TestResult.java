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
    Double averageLatencyAdequacy;

    Double meanTurnAroundTime;
    Double meanTurnAroundTimeAdequacy;

    Unit<String> maxCapacity;
    Double maxCapacityAdequacy;

    Unit<Double> averageSize;
    Double averageSizeAdequacy;

    Unit<Double> throughPut;
    Double throughPutAdequacy;

    Unit<Long> meanResponseTime;
    Double meanResponseTimeAdequacy;

    Unit<String> meanResponseSize;
    Double meanResponseSizeAdequacy;

    Double cpuUtilAdequacy;
    Double memUtilAdequacy;

    Integer testCount = 0;

    public TestResult(String path, String method, List<TestRequestResult> testRequestResults) {
        super(path, method);
        this.testRequestResults = testRequestResults;
        init();
    }

    private void init(){
        Double summedLatency = 0.0;
        Double summedTurnAroundTime = 0.0;
        long summedSize = 0L;

        for(TestRequestResult result : testRequestResults){
            summedLatency += result.getLatency();
            summedTurnAroundTime += result.getTurnAroundTime();
            summedSize += result.getSize();
        }

        int count = testRequestResults.size();
        if(count > 0){
            averageLatency = summedLatency / count;
            meanTurnAroundTime = summedTurnAroundTime / count;
            averageSize = new Unit<>((double)(summedSize / count), "b");
        }

        calculateThroughPut();
        if(this.throughPut.getValue().isNaN()) this.throughPut.setValue(null);
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

    public Double getMeanTurnAroundTime() {
        return meanTurnAroundTime;
    }

    public void setMeanTurnAroundTime(Double meanTurnAroundTime) {
        this.meanTurnAroundTime = meanTurnAroundTime;
    }

    public int getTestCount() {
        return testCount;
    }

    public void setTestCount(Integer testCount) {
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
        double totalTime = this.testRequestResults.stream().mapToDouble(TestRequestResult::getTurnAroundTime).sum();
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

    public Unit<Long> getMeanResponseTime() {
        return meanResponseTime;
    }

    public Unit<String> getMeanResponseSize() {
        return meanResponseSize;
    }

    public void setMeanResponseTime(Unit<Long> meanResponseTime) {
        this.meanResponseTime = meanResponseTime;
    }

    public void setMeanResponseSize(Unit<String> meanResponseSize) {
        this.meanResponseSize = meanResponseSize;
    }

    public void setEndpointExecutionInfosAverage(EndpointExecutionInfo endpointExecutionInfosAverage) {
        this.meanResponseTime = endpointExecutionInfosAverage.getResponseTime();
        this.meanResponseSize = endpointExecutionInfosAverage.getResponseSize();
    }

    public WatchResultGroup getWatches() {
        return watches;
    }

    public void setWatches(WatchResultGroup watches) {
        this.watches = watches;
    }

    public void setMeanTurnAroundTimeAdequacy(Double meanTurnAroundTimeAdequacy) {
        this.meanTurnAroundTimeAdequacy = meanTurnAroundTimeAdequacy;
    }

    public void setMeanResponseTimeAdequacy(Double meanResponseTimeAdequacy) {
        this.meanResponseTimeAdequacy = meanResponseTimeAdequacy;
    }

    public void nullErrors(){
        this.testRequestResults=null;

        if(watches != null) watches.nullErrors();
        if(testCount<0) testCount = null;

        if(averageLatency == null || averageLatency < 0 || averageLatency.isNaN() || averageLatency.isInfinite()) averageLatency = null;
        if(meanTurnAroundTime == null || meanTurnAroundTime < 0 || meanTurnAroundTime.isNaN() || meanTurnAroundTime.isInfinite()) meanTurnAroundTime = null;

        if(averageSize != null && (averageSize.getValue() == null || averageSize.getValue().isNaN() || averageSize.getValue().isInfinite())) averageSize = null;
        if(throughPut != null && (throughPut.getValue() == null || throughPut.getValue().isNaN() || throughPut.getValue().isInfinite())) throughPut = null;
        if(meanResponseTime != null && (meanResponseTime.getValue()==null || meanResponseTime.getValue() < 0 )) meanResponseTime = null;
        if(maxCapacity != null && (maxCapacity.getValue() == null || maxCapacity.getValue().isEmpty())) maxCapacity = null;
        if(meanResponseSize != null && (meanResponseSize.getValue() == null || meanResponseSize.getValue().isEmpty())) meanResponseSize = null;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "\ttestRequestResults=" + testRequestResults +
                "\t, watches=" + watches +
                "\t, averageLatency=" + averageLatency +
                "\t, meanTurnAroundTime=" + meanTurnAroundTime +
                "\t, maxCapacity=" + maxCapacity +
                "\t, averageSize=" + averageSize +
                "\t, throughPut=" + throughPut +
                "\t, meanResponseTime=" + meanResponseTime +
                "\t, meanResponseSize=" + meanResponseSize +
                "\t, testCount=" + testCount +
                "\t, path='" + path + '\'' +
                "\t, method='" + method + '\'' +
                '}';
    }

    public Double getAverageLatencyAdequacy() {
        return averageLatencyAdequacy;
    }

    public void setAverageLatencyAdequacy(Double averageLatencyAdequacy) {
        this.averageLatencyAdequacy = averageLatencyAdequacy;
    }

    public Double getMeanTurnAroundTimeAdequacy() {
        return meanTurnAroundTimeAdequacy;
    }

    public Double getMaxCapacityAdequacy() {
        return maxCapacityAdequacy;
    }

    public void setMaxCapacityAdequacy(Double maxCapacityAdequacy) {
        this.maxCapacityAdequacy = maxCapacityAdequacy;
    }

    public Double getAverageSizeAdequacy() {
        return averageSizeAdequacy;
    }

    public void setAverageSizeAdequacy(Double averageSizeAdequacy) {
        this.averageSizeAdequacy = averageSizeAdequacy;
    }

    public Double getThroughPutAdequacy() {
        return throughPutAdequacy;
    }

    public void setThroughPutAdequacy(Double throughPutAdequacy) {
        this.throughPutAdequacy = throughPutAdequacy;
    }

    public Double getMeanResponseTimeAdequacy() {
        return meanResponseTimeAdequacy;
    }

    public Double getMeanResponseSizeAdequacy() {
        return meanResponseSizeAdequacy;
    }

    public void setMeanResponseSizeAdequacy(Double meanResponseSizeAdequacy) {
        this.meanResponseSizeAdequacy = meanResponseSizeAdequacy;
    }

    public Double getCpuUtilAdequacy() {
        return cpuUtilAdequacy;
    }

    public void setCpuUtilAdequacy(Double cpuUtilAdequacy) {
        this.cpuUtilAdequacy = cpuUtilAdequacy;
    }

    public Double getMemUtilAdequacy() {
        return memUtilAdequacy;
    }

    public void setMemUtilAdequacy(Double memUtilAdequacy) {
        this.memUtilAdequacy = memUtilAdequacy;
    }
}

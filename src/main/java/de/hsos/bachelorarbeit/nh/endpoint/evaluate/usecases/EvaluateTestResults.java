package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndpointGroupInfo;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestGroup;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class EvaluateTestResults {
    List<TestGroup> testGroups;
    List<TestResult> testResults;

    public EvaluateTestResults(ReadTestsResults readTestsResults, ReadExecutionInfo readExecutionInfo) {
        this.testResults = readTestsResults.getTestResults();
        this.testGroups = this.getTestGroups(this.testResults);
        initValues();
        addEndPointInfo(readExecutionInfo.getEndPointGroupInfo());
    }

    private void addEndPointInfo(List<EndpointGroupInfo> endPointGroupInfos) {
        for(EndpointGroupInfo endpointGroupInfo : endPointGroupInfos){
            String path = endpointGroupInfo.getUrl().toLowerCase().trim();
            String method = endpointGroupInfo.getMethod().toLowerCase().trim();

            TestGroup entry = this.testGroups.stream()
                    .filter(x -> x.getPath().toLowerCase().trim().equals(path) && x.getMethod().toLowerCase().trim().equals(method))
                    .findFirst()
                    .orElse(null);

            if(entry==null){
                entry = new TestGroup(path, method, new ArrayList<>());
                this.testGroups.add(entry);
            }
            entry.setExecutionInfos(endpointGroupInfo.getEndpointExecutionInfos());
        }
    }

    private void initValues(){
        setMeanTurnAroundTime();
        setMeanResponseTime();
        setThroughPut();
    }

    private List<TestGroup> getTestGroups(List<TestResult> testRestults){
        List<TestGroup> result = new ArrayList<>();
        Map<String, Map<String, List<TestResult>>> stringMapMap = testRestults.stream()
                .collect(
                        Collectors.groupingBy(TestResult::getUrl,
                                Collectors.groupingBy(TestResult::getMethod))
                );

        for(String path : stringMapMap.keySet()){
            Map<String, List<TestResult>> stringListMap = stringMapMap.get(path);
            for(String method : stringListMap.keySet()){
                List<TestResult> list = stringListMap.get(method);
                result.add(new TestGroup(path, method, list));
            }
        }
        return result;
    }

    private Optional<TestGroup> getGroupByKey(String key){
        return this.testGroups.stream().filter(g -> g.getPath().equals(key)).findFirst();
    }

    public void setMeanTurnaroundTime(TestGroup testGroup){
        double mtt = testGroup.getTestResults().stream().mapToDouble(TestResult::getElapsedTime).average().orElse(Double.NaN);
        testGroup.setMeanTurnaroundTime(mtt);
    }

    public void setMeanResponseTime(TestGroup testGroup){
        double mrt = testGroup.getTestResults().stream().mapToDouble(TestResult::getLatency).average().orElse(Double.NaN);
        testGroup.setMeanResponseTime(mrt);
    }

    public void setMeanResponseTime(){
        this.testGroups
                .stream()
                .forEach(tG -> {
                    if(tG.getMeanResponseTime() == null){
                        this.setMeanResponseTime(tG);
                    }
                });
    }

    public void setMeanTurnAroundTime(){
        this.testGroups
                .stream()
                .forEach(tG -> {
                    if(tG.getMeanTurnaroundTime() == null){
                        this.setMeanTurnaroundTime(tG);
                    }
                });
    }

    private Double getSystemMean(ToDoubleFunction<? super TestResult> mapper){
        return this.testResults.stream().mapToDouble(mapper).average().orElse(Double.NaN);
    }

    public Double getSystemMeanResponseTime(){
        return this.getSystemMean(TestResult::getLatency);
    }

    public Double getSystemMeanTurnaroundTime(){
        return this.getSystemMean(TestResult::getElapsedTime);
    }

    public Double getSystemMeanResponseTimeAdequacy(double target){
        return this.getSystemMeanResponseTime() / target;
    }

    public Double getSystemMeanTurnaroundTimeAdequacy(double target){
        return this.getSystemMeanTurnaroundTime() / target;
    }

    public void setThroughPut(){
        for(TestGroup testGroup : this.testGroups){
            List<TestResult> testResults = testGroup.getTestResults();
            double summedElapsedTime = testResults.stream().map(TestResult::getElapsedTime).collect(Collectors.summingDouble(Double::doubleValue));
            double avgElapsedTime = summedElapsedTime / testResults.size();
            double throughPut = 1000 / avgElapsedTime;
            testGroup.setThroughput(throughPut);
        }
    }

    public boolean testPassed(){
        Optional<TestGroup> testGroup = this.testGroups.stream().filter(tG -> !tG.isSuccess()).findFirst();
        return !testGroup.isPresent();
    }

    @Override
    public String toString() {
        return "EvaluateTestResults{" +
                "testGroups=" + testGroups +
                '}';
    }
}


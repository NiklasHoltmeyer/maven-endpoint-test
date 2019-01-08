package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;
//todo: xyz
public class EvaluateTestResults {
    //List<TestGroupOld> testGroupOlds;
    //List<TestRequestResult> testRequestResults;

    //public EvaluateTestResults(ReadTestsResults readTestsResults, ReadExecutionInfo readExecutionInfo) {
        //this.testRequestResults = readTestsResults.getTestResults();
        //this.testGroupOlds = this.getTestGroups(this.testRequestResults);
        //initValues();
        //addEndPointInfo(readExecutionInfo.getEndPointGroupInfo());
    //}
/*
    private void addEndPointInfo(List<EndpointGroupInfo> endPointGroupInfos) {
        for(EndpointGroupInfo endpointGroupInfo : endPointGroupInfos){
            String path = endpointGroupInfo.getUrlParameterLess().toLowerCase().trim();
            String method = endpointGroupInfo.getMethod().toLowerCase().trim();

            TestGroupOld entry = this.testGroupOlds.stream()
                    .filter(x -> x.getPath().toLowerCase().trim().equals(path) && x.getMethod().toLowerCase().trim().equals(method))
                    .findFirst()
                    .orElse(null);

            if(entry==null){
                entry = new TestGroupOld(path, method, new ArrayList<>());
                this.testGroupOlds.add(entry);
            }
            entry.setExecutionInfos(endpointGroupInfo.getEndpointExecutionInfos());
        }
    }

    private void initValues(){
        setMeanTurnAroundTime();
        setMeanResponseTime();
        setThroughPut();
    }

    private List<TestGroupOld> getTestGroups(List<TestRequestResult> testRestults){
        List<TestGroupOld> result = new ArrayList<>();
        Map<String, Map<String, List<TestRequestResult>>> stringMapMap = testRestults.stream()
                .collect(
                        Collectors.groupingBy(TestRequestResult::getUrlParameterLess,
                                Collectors.groupingBy(TestRequestResult::getMethod))
                );

        for(String path : stringMapMap.keySet()){
            Map<String, List<TestRequestResult>> stringListMap = stringMapMap.get(path);
            for(String method : stringListMap.keySet()){
                List<TestRequestResult> list = stringListMap.get(method);
                result.add(new TestGroupOld(path, method, list));
            }
        }
        return result;
    }

    public List<TestGroupOld> getTestGroupOlds(){
        return this.testGroupOlds;
    }

    private Optional<TestGroupOld> getGroupByKey(String key){
        return this.testGroupOlds.stream().filter(g -> g.getPath().equals(key)).findFirst();
    }

    public void setMeanTurnaroundTime(TestGroupOld testGroupOld){
        double mtt = testGroupOld.getTestRequestResults().stream().mapToDouble(TestRequestResult::getElapsedTime).average().orElse(Double.NaN);
        testGroupOld.setMeanTurnaroundTime(mtt);
    }

    public void setMeanResponseTime(TestGroupOld testGroupOld){
        double mrt = testGroupOld.getTestRequestResults().stream().mapToDouble(TestRequestResult::getLatency).average().orElse(Double.NaN);
        testGroupOld.setMeanResponseTime(mrt);
    }

    public void setMeanResponseTime(){
        this.testGroupOlds
                .stream()
                .forEach(tG -> {
                    if(tG.getMeanResponseTime() == null){
                        this.setMeanResponseTime(tG);
                    }
                });
    }

    public void setMeanTurnAroundTime(){
        this.testGroupOlds
                .stream()
                .forEach(tG -> {
                    if(tG.getMeanTurnaroundTime() == null){
                        this.setMeanTurnaroundTime(tG);
                    }
                });
    }

    private Double getSystemMean(ToDoubleFunction<? super TestRequestResult> mapper){
        return this.testRequestResults.stream().mapToDouble(mapper).average().orElse(Double.NaN);
    }

    public Double getSystemMeanResponseTime(){
        return this.getSystemMean(TestRequestResult::getLatency);
    }

    public Double getSystemMeanTurnaroundTime(){
        return this.getSystemMean(TestRequestResult::getElapsedTime);
    }

    public Double getSystemMeanResponseTimeAdequacy(double target){
        return this.getSystemMeanResponseTime() / target;
    }

    public Double getSystemMeanTurnaroundTimeAdequacy(double target){
        return this.getSystemMeanTurnaroundTime() / target;
    }

    public void setThroughPut(){
        for(TestGroupOld testGroupOld : this.testGroupOlds){
            List<TestRequestResult> testRequestResults = testGroupOld.getTestRequestResults();
            double summedElapsedTime = testRequestResults.stream().map(TestRequestResult::getElapsedTime).collect(Collectors.summingDouble(Double::doubleValue));
            double avgElapsedTime = summedElapsedTime / testRequestResults.size();
            double throughPut = 1000 / avgElapsedTime;
            testGroupOld.setThroughput(throughPut);
        }
    }

    public boolean testPassed(){
        Optional<TestGroupOld> testGroup = this.testGroupOlds.stream().filter(tG -> !tG.isSuccess()).findFirst();
        return !testGroup.isPresent();
    }

    @Override
    public String toString() {
        return "EvaluateTestResults{" +
                "testGroupOlds=" + testGroupOlds +
                ", testRequestResults=" + testRequestResults +
                '}';
    }
    */
}


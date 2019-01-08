package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities;

import java.util.List;
import java.util.Optional;

public class TestResultGroup {
    List<TestResult> resultGroups;
    String method;
    String path;

    public List<TestResult> getResultGroups() {
        return resultGroups;
    }

    public void setResultGroups(List<TestResult> resultGroups) {
        this.resultGroups = resultGroups;
    }

    public void addResultGroup(TestResult result){
        this.resultGroups.add(result);
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

    public boolean didFail(){
        return resultGroups.stream().filter(TestResult::isFailed).findFirst().orElse(null) != null;
    }

    public Optional<TestResult> getTestResult(String path, String method){
        return this.resultGroups.stream()
                .filter(x-> x.compare(path, method))
                .findFirst();
    }

}

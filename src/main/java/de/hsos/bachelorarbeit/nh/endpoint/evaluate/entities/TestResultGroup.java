package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities;

import java.util.List;
import java.util.Optional;

public class TestResultGroup {
    List<TestResult> resultGroups;

    public List<TestResult> getResultGroups() {
        return resultGroups;
    }

    public void setResultGroups(List<TestResult> resultGroups) {
        this.resultGroups = resultGroups;
    }

    public void addResultGroup(TestResult result){
        this.resultGroups.add(result);
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
}
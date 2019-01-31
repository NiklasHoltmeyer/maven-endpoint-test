package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance.PerformanceResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Reliability.ReliabilityResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;

public class EvaluateResult{
    ReliabilityResult reliability;
    PerformanceResult performance;
    TestResultGroup testResultGroup;

    Double value;

    public EvaluateResult() {
        this.reliability = new ReliabilityResult();
        this.performance = new PerformanceResult();
    }

    public ReliabilityResult getReliability() {
        return reliability;
    }

    public PerformanceResult getPerformance() {
        return performance;
    }

    public void setTestResultGroup(TestResultGroup testResultGroup) {
        this.testResultGroup = testResultGroup;
    }

    public TestResultGroup getTestResultGroup() {
        return testResultGroup;
    }

    public void nullDetails() {
        this.testResultGroup = null;
        this.reliability.nullDetails();
        this.nullErrors();
        //todo performance adeq. + nullen
    }

    public void nullErrors(){
        if(reliability != null) reliability.nullErrors();
        if(performance != null) performance.nullErrors();
        if(testResultGroup != null){
            testResultGroup.nullErrors();
        }
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}

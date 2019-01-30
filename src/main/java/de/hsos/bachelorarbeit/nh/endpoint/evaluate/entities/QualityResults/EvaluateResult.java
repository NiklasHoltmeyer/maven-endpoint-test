package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance.PerformanceResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Reliability.ReliabilityResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;

public class EvaluateResult{
    ReliabilityResult reliability;
    PerformanceResult performance;
    TestResultGroup testResultGroup;

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
        //todo performance adeq. + nullen
    }
}

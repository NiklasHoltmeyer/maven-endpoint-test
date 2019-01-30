package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Reliability;

public class ReliabilityResult {
    TestingStatusResult testingStatus;
    Double value;

    public ReliabilityResult() {
        this.testingStatus = new TestingStatusResult();
    }

    public TestingStatusResult getTestingStatus() {
        return testingStatus;
    }

    public Double getValue() {
        return this.value;
    }

    public void updateValue(){
        this.value = this.testingStatus.getValue() /*/ 1*/; //avg (testingstatus, software-stability (todo))
    }

    public void nullDetails(){
        this.testingStatus = null;
    }
}

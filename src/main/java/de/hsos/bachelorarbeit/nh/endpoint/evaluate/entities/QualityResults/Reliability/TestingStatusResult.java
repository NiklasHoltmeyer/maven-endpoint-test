package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Reliability;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;

public class TestingStatusResult {
    Unit<Double> passedTest;
    Unit<Double> fastTestBuilds;
    Unit<Double> testCoverage;
    Double value;

    public TestingStatusResult(){
        this.passedTest = new Unit<>(Double.NaN, "AVG(Passed-Tests)");
        this.fastTestBuilds = new Unit<>(Double.NaN, "AVG(Fast-Test-Builds)");
        this.testCoverage = new Unit<>(Double.NaN, "AVG(Test-Coverage)");
    }

    public Unit<Double> getPassedTest() {
        return passedTest;
    }

    public Unit<Double> getFastTestBuilds() {
        return fastTestBuilds;
    }

    public Unit<Double> getTestCoverage() {
        return testCoverage;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    public void nullErrors(){
        if(passedTest!=null && (passedTest.getValue() == null||passedTest.getValue().isNaN() || passedTest.getValue().isInfinite()))  passedTest = null;
        if(fastTestBuilds!=null && (fastTestBuilds.getValue() == null||fastTestBuilds.getValue().isNaN() || fastTestBuilds.getValue().isInfinite()))  fastTestBuilds = null;
        if(testCoverage!=null && (testCoverage.getValue() == null||testCoverage.getValue().isNaN() || testCoverage.getValue().isInfinite()))  testCoverage = null;
        if(value != null && value.isNaN() || value.isInfinite()) value = null;
    }
}

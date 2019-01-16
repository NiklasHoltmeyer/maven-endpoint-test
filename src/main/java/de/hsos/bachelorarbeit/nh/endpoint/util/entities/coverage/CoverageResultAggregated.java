package de.hsos.bachelorarbeit.nh.endpoint.util.entities.coverage;

public class CoverageResultAggregated {
    int totalEndpoints;
    int testedEndpoints;
    int totalTests;

    public CoverageResultAggregated(int totalEndpoints, int testedEndpoints, int totalTests) {
        this.totalEndpoints = totalEndpoints;
        this.testedEndpoints = testedEndpoints;
        this.totalTests = totalTests;
    }

    public CoverageResultAggregated(CoverageResult coverageResult){
        this.totalEndpoints   = coverageResult.getTotalEndpoints();
        this.testedEndpoints  = coverageResult.getTestedEndpoints();
        this.totalTests       = coverageResult.getTotalTests();
    }

    public int getTotalEndpoints() {
        return totalEndpoints;
    }

    public void setTotalEndpoints(int totalEndpoints) {
        this.totalEndpoints = totalEndpoints;
    }

    public int getTestedEndpoints() {
        return testedEndpoints;
    }

    public void setTestedEndpoints(int testedEndpoints) {
        this.testedEndpoints = testedEndpoints;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }
}

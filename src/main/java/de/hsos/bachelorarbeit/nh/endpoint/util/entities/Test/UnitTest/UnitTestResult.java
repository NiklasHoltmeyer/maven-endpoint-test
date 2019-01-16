package de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Result;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage.CodeCoverageResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;

public class UnitTestResult extends Result {
    String className;

    int errors;
    int skipped;
    int tests;
    int failures;
    CodeCoverageResult codeCoverage;
    Unit<Double> time = new Unit<Double>(0.0,"s");

    public UnitTestResult(){
        super();
    }

    public UnitTestResult(boolean success, String errorMessage) {
        super(success, errorMessage);
    }

    public UnitTestResult(boolean success, String errorMessage, String className) {
        super(success, errorMessage);
        this.className = className;
    }
    public UnitTestResult(Result result) {
        super(result);
    }

    public UnitTestResult(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getTests() {
        return tests;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }

    public void setTime(double time) {
        this.time.setValue(time);
    }

    public Unit<Double> getTime() {
        return time;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }

    public void setTime(Unit<Double> time) {
        this.time = time;
    }

    public CodeCoverageResult getCodeCoverage() {
        return codeCoverage;
    }

    public void setCodeCoverage(CodeCoverageResult codeCoverage) {
        this.codeCoverage = codeCoverage;
    }

    @Override
    public String toString() {
        return "UnitTestResult{" +
                "\tclassName='" + className + '\'' +
                "\t, errors=" + errors +
                "\t, skipped=" + skipped +
                "\t, tests=" + tests +
                "\t, failures=" + failures +
                "\t, time=" + time + "\n" +
                "\t, codeCoverage=" + codeCoverage + "\n" +
                "}";
    }
}

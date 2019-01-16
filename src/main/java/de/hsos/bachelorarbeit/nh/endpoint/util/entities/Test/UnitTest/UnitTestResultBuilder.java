package de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage.CodeCoverageResult;

public final class UnitTestResultBuilder {
    String className;
    int errors;
    int skipped;
    int tests;
    int failures;
    double time;
    private boolean success = true;
    private String errorMessage;
    CodeCoverageResult codeCoverageResult;

    private UnitTestResultBuilder() {
    }

    public static UnitTestResultBuilder anUnitTestResult() {
        return new UnitTestResultBuilder();
    }

    public UnitTestResultBuilder withSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public UnitTestResultBuilder withClassName(String className) {
        this.className = className;
        return this;
    }

    public UnitTestResultBuilder withErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public UnitTestResultBuilder withErrors(int errors) {
        this.errors = errors;
        return this;
    }

    public UnitTestResultBuilder withSkipped(int skipped) {
        this.skipped = skipped;
        return this;
    }

    public UnitTestResultBuilder withTests(int tests) {
        this.tests = tests;
        return this;
    }

    public UnitTestResultBuilder withTime(double time) {
        this.time = time;
        return this;
    }

    public UnitTestResultBuilder withFailures(int failures){
        this.failures = failures;
        return this;
    }

    public UnitTestResultBuilder withCodeCoverageResult(CodeCoverageResult codeCoverageResult){
        this.codeCoverageResult = codeCoverageResult;
        return this;
    }

    public UnitTestResult build() {
        UnitTestResult unitTestResult = new UnitTestResult();
        unitTestResult.setSuccess(success);
        unitTestResult.setClassName(className);
        unitTestResult.setErrorMessage(errorMessage);
        unitTestResult.setErrors(errors);
        unitTestResult.setSkipped(skipped);
        unitTestResult.setTests(tests);
        unitTestResult.setTime(time);
        unitTestResult.setFailures(failures);
        unitTestResult.setCodeCoverage(codeCoverageResult);
        return unitTestResult;
    }
}

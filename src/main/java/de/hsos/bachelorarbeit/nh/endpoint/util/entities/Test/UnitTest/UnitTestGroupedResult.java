package de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage.CodeCoverageResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage.Coverage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UnitTestGroupedResult {
    List<UnitTestResult> results;
    UnitTestResult summedResult;
    CodeCoverageResult summedCodeCoverage = new CodeCoverageResult();

    public UnitTestGroupedResult(List<UnitTestResult> results) {
        this.results = results;
        this.summedResult = new UnitTestResult("*");
        this.init();
    }

    private void init(){
        this.summedCodeCoverage.initSumEntity();
        this.results.forEach(this::sumUpValues);
        summedCodeCoverage.setClassName("*");
    }

    private void sumUpValues(UnitTestResult unitTestResult){
        int errors     =  summedResult.getErrors() + unitTestResult.getErrors();
        int skipped    =  summedResult.getSkipped() + unitTestResult.getSkipped();
        int tests      =  summedResult.getTests() + unitTestResult.getTests();
        int failures   =  summedResult.getFailures() + unitTestResult.getFailures();
        double time    =  summedResult.getTime().getValue() + unitTestResult.getTime().getValue();

        summedResult.setErrors(errors);
        summedResult.setSkipped(skipped);
        summedResult.setTests(tests);
        summedResult.setFailures(failures);
        summedResult.setTime(time);
    }


    public List<UnitTestResult> getResults() {
        return results;
    }

    public UnitTestResult getSummedResult() {
        return summedResult;
    }

    public Optional<UnitTestResult> getUnitTestResult(String className){
        return this.getResults()
                .stream()
                .filter(x->x.getClassName().equals(className))
                .findFirst();
    }

    public void addCoverage(CodeCoverageResult codeCoverageResult){
        String className = codeCoverageResult.getClassName();
        UnitTestResult unitTestResult = this.getUnitTestResult(className).orElse(null);
        if(unitTestResult!=null){
            unitTestResult.setCodeCoverage(codeCoverageResult);
        }else{
            UnitTestResult ur = UnitTestResultBuilder.anUnitTestResult()
                    .withClassName(className)
                    .withCodeCoverageResult(codeCoverageResult)
                    .build();
            this.results.add(ur);
        }
        addCoverageSum(codeCoverageResult);
        this.summedResult.setCodeCoverage(summedCodeCoverage);
    }

    public void addCoverageSum(CodeCoverageResult cc){
        Coverage instruction =  Coverage.add(this.summedCodeCoverage.getInstruction(), cc.getInstruction());
        Coverage branch =  Coverage.add(this.summedCodeCoverage.getBranch(), cc.getBranch());
        Coverage line =  Coverage.add(this.summedCodeCoverage.getLine(), cc.getLine());
        Coverage complexity =  Coverage.add(this.summedCodeCoverage.getComplexity(), cc.getComplexity());
        Coverage method =  Coverage.add(this.summedCodeCoverage.getMethod(), cc.getMethod());
        Coverage clazz =  Coverage.add(this.summedCodeCoverage.getClazz(), cc.getClazz());

        if(instruction!=null) this.summedCodeCoverage.setInstruction(instruction);
        if(branch!=null) this.summedCodeCoverage.setBranch(branch);
        if(line!=null) this.summedCodeCoverage.setLine(line);
        if(complexity!=null) this.summedCodeCoverage.setComplexity(complexity);
        if(method!=null) this.summedCodeCoverage.setMethod(method);
        if(clazz!=null) this.summedCodeCoverage.setClazz(clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(results, summedResult, summedCodeCoverage);
    }

    @Override
    public String toString() {
        return "UnitTestGroupedResult{" +
                "results=" + results + "\n" +
                ", summedResult=" + summedResult + "\n" +
                //", summedCodeCoverage=" + summedCodeCoverage + "\n" +
                '}';
    }
}

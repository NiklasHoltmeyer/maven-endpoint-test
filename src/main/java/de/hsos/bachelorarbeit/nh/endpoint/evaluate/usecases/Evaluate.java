package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.EvaluateResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance.CapacityResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance.PerformanceResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance.ResourceUtilizationResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance.TimeBehaviorResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Reliability.ReliabilityResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Reliability.TestingStatusResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.test.entities.WatchResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage.Coverage;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestGroupedResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.WatchResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.coverage.CoverageResultAggregated;

import java.util.List;
import java.util.stream.Collectors;

public class Evaluate {
    public EvaluateResult evalute(TestResultGroup testResultGroup){
        EvaluateResult evaluateResult = new EvaluateResult();
        evaluateResult.setTestResultGroup(testResultGroup);

        this.calculateReliability(evaluateResult, testResultGroup);
        this.calculatePerformance(evaluateResult, testResultGroup);
        return evaluateResult;
    }

    @SuppressWarnings("Duplicates")
    //todo ^^ spaeter
    private void calculatePerformance(EvaluateResult evaluateResult, TestResultGroup testResultGroup) {
        PerformanceResult performanceResult = evaluateResult.getPerformance();
        CapacityResult capacityResult = performanceResult.getCapacity();

        this.calculateTimeBehavior(performanceResult, testResultGroup);
        this.calcuateResourceUtilization(performanceResult, testResultGroup);
        this.calcuateCapacity(capacityResult, testResultGroup);
    }

    private void calcuateCapacity(CapacityResult capacityResult, TestResultGroup testResultGroup) {
        List<TestResult> testResults = testResultGroup.getResultGroups();
        double sum = 0.0;
        int count = 0;
        for(TestResult testResult : testResults){
            if(testResult!=null){
                Unit<String> v = testResult.getMaxCapacity();
                if(v!=null && v.getValue()!=null && !v.getValue().isEmpty()){
                    try{
                        double doubleV = Double.valueOf(v.getValue());
                        sum += doubleV;
                        ++count;
                    }catch(NumberFormatException e){
                        System.err.println("Couldnt parse (" + v.getValue() + ") to double");
                    }
                }
            }
        }
        if(count != 0) capacityResult.getCapacity().setValue(sum/count);
    }

    @SuppressWarnings("Duplicates")
    private void calcuateResourceUtilization(PerformanceResult performanceResult, TestResultGroup testResultGroup){
        ResourceUtilizationResult resourceUtilizationResult = performanceResult.getResourceUtilization();
        List<TestResult> testResults = testResultGroup.getResultGroups();
        double sumCPU = 0.0;
        int countCPU = 0;
        double sumMEM = 0.0;
        int countMEM = 0;

        for(TestResult testResult : testResults){
            if(testResult!=null){
                WatchResultGroup watchResultGroup = testResult.getWatches();
                if(watchResultGroup!=null){
                    WatchResult<Double> cpuW = watchResultGroup.getCpu();
                    WatchResult<Double> memW = watchResultGroup.getMemory();
                    if(cpuW != null && cpuW.getAverage() != null && cpuW.getAverage().getValue() != null){
                        Double avg = cpuW.getAverage().getValue();
                        sumCPU += avg;
                        ++countCPU;
                    }
                    if(memW != null && memW.getAverage() != null && memW.getAverage().getValue() != null){
                        Double avg = memW.getAverage().getValue();
                        sumMEM += avg;
                        ++countMEM;
                    }
                }
            }
        }

        if(countMEM != 0) resourceUtilizationResult.getMeanMemoryUtilization().setValue(sumMEM/countMEM);
        if(countCPU != 0) resourceUtilizationResult.getMeanProcessorUtilization().setValue(sumCPU/countCPU);
    }

    private void calculateTimeBehavior(PerformanceResult performanceResult, TestResultGroup testResultGroup){
        List<TestResult> testResults = testResultGroup.getResultGroups();
        TimeBehaviorResult timeBehaviorResult = performanceResult.getTimeBehavior();

        Long sumMRT = 0L;
        int mRTCount = 0;

        Double sumMTT = 0.0;
        int mTTCount = 0;

        Double sumThroughPut = 0.0;
        int throughPutCount = 0;

        for(TestResult tr : testResults){
            if(tr!=null){
                Unit<Long> mrt = tr.getMeanResponseTime();
                Double mtt = tr.getMeanTurnAroundTime();
                Unit<Double> meanThroughPut = tr.getThroughPut();

                if(mrt != null && mrt.getValue() != null){
                    Long mrtValue = mrt.getValue();
                    if(mrtValue!=null){
                        sumMRT += mrtValue;
                        ++mRTCount;
                    }
                }
                if(mtt != null && !mtt.isNaN()){
                    sumMTT += mtt;
                    ++mTTCount;
                }
                if(meanThroughPut != null && meanThroughPut.getValue() != null){
                    Double meanThroughPutValue = meanThroughPut.getValue();
                    if(meanThroughPutValue!=null && !meanThroughPutValue.isNaN()){
                        sumThroughPut += meanThroughPutValue;
                        ++throughPutCount;
                    }
                }
            }
        }
        if(mRTCount != 0)timeBehaviorResult.getMeanResponseTime().setValue(sumMRT/(double)mRTCount);
        if(mTTCount != 0)timeBehaviorResult.getMeanTurnAroundTime().setValue(sumMTT/mTTCount);
        if(throughPutCount != 0)timeBehaviorResult.getMeanThroughput().setValue(sumThroughPut/throughPutCount);
    }

    private void calculateReliability(EvaluateResult evaluateResult, TestResultGroup testResultGroup) {
        ReliabilityResult reliabilityResult = evaluateResult.getReliability();
        TestingStatusResult testingStatusResult = reliabilityResult.getTestingStatus();
        //Q-Rapids
        double passedTests = this.passedTestsCalculate(testResultGroup);
        double fastTestBuilds = this.fastTestBuildsCalculate(testResultGroup);
        double testCoverage = this.testCoverageCalculate(testResultGroup);
        testingStatusResult.getPassedTest().setValue(passedTests);
        testingStatusResult.getFastTestBuilds().setValue(fastTestBuilds);
        testingStatusResult.getTestCoverage().setValue(testCoverage);

        double testingStatusValue = (passedTests+fastTestBuilds+testCoverage)/3;
        testingStatusResult.setValue(testingStatusValue);
        reliabilityResult.updateValue();
    }


    private double testCoverageCalculate(TestResultGroup testResultGroup) {
        CoverageResultAggregated coverageResultAggregated = testResultGroup.getAggregatedEndpointCoverage();
        Coverage coverageResult = testResultGroup.getUnitTestResults().getSummedResult().getCodeCoverage().getLine();

        int lines =  coverageResult.getCoverd() + coverageResult.getMissed();
        double unitTestCoverage = (double) coverageResult.getCoverd() / lines;

        double endpointTestCoverage = (double) coverageResultAggregated.getTestedEndpoints() / coverageResultAggregated.getTotalEndpoints();

        double testCoverage = (unitTestCoverage + endpointTestCoverage) / 2;
        return testCoverage;
    }

    double passedTestsCalculate(TestResultGroup testResultGroup){
        UnitTestGroupedResult unitTestGroupedResult = testResultGroup.getUnitTestResults();
        UnitTestResult unitTestResult = unitTestGroupedResult.getSummedResult();
        List<TestResult> resultGroups = testResultGroup.getResultGroups()
                .stream()
                .filter(x->x.getTestCount() > 0)
                .collect(Collectors.toList());

        int endpointTests = resultGroups.stream().mapToInt(TestResult::getTestCount).sum();
        int endpointFailedTests = resultGroups.stream().mapToInt(tr->tr.isFailed()? 1 : 0).sum();

        int unitTests = unitTestResult.getTests();
        int unitTestErrors = unitTestResult.getErrors();
        int unitTestFailures = unitTestResult.getFailures();
        int unitTestSkipped = unitTestResult.getSkipped();

        int totalTests = unitTests + endpointTests;
        int totalNonSuccess = unitTestErrors+unitTestFailures+unitTestSkipped + endpointFailedTests;

        String t = "endpointTests= " + endpointTests + ", endpointFailedTests=" + endpointFailedTests + ", totalTests=" + totalTests + ", totalNonSuccess=" + totalNonSuccess;
        System.out.println(t);

        if(totalTests==0) return 0.0;
        return (totalTests - totalNonSuccess) / (double) totalTests;
    }

    double fastTestBuildsCalculate(TestResultGroup testResultGroup){
        UnitTestGroupedResult unitTestGroupedResult = testResultGroup.getUnitTestResults();

        List<UnitTestResult> testsWithTests =  unitTestGroupedResult.getResults().stream().filter(x->(x.getErrors() + x.getFailures() + x.getTests())>0).collect(Collectors.toList());

        List<TestResult> resultGroups = testResultGroup.getResultGroups()
                .stream()
                .filter(x->x.getTestCount() > 0)
                .collect(Collectors.toList());

        int endpointTests = resultGroups.stream().mapToInt(TestResult::getTestCount).sum();
        int fastEndpointTests = resultGroups.stream().mapToInt(tr->{
            boolean isFast = tr.getTestRequestResults().stream().filter(x->(x.getTurnAroundTime() * 0.001)>300).findFirst().orElse(null) == null;
            return (isFast) ? 1 : 0;
        }).sum();

        int unitTests = testsWithTests.size();
        int fastUnitTests = testsWithTests
                                .stream()
                                .mapToInt(x->{
                                      if(x.getTime()!=null){
                                          Double d = x.getTime().getValue();
                                          if(d!=null) {
                                              d /= (x.getErrors() + x.getFailures() + x.getTests()); // mean d per test
                                              d /= 300; //
                                              if(d <= 1) return 1;
                                          }

                                      }
                                      System.out.println(x.getClassName());
                                      return 0;
                                })
                                .sum();

        int fastTests = fastUnitTests + fastEndpointTests;
        int totalTests = unitTests + endpointTests;

        String t = "endpointTests= " + endpointTests + ", fastEndpointTests=" + fastEndpointTests + ", fastTests=" + fastTests + ", totalTests=" + totalTests;
        System.out.println(t);

        if(totalTests == 0) return 0.0;
        return fastTests / (double) totalTests;
    }
}


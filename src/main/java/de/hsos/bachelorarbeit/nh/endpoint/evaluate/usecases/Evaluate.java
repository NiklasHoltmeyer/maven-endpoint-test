package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestGroupedResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestResult;

import java.util.List;
import java.util.stream.Collectors;

public class Evaluate {
    /*
            List<TestResult> resultGroups;
                WatchResultGroup watches;
                    WatchResult<Double> cpu;
                    WatchResult<Double> memory;
                Double averageLatency;
                Double meanTurnAroundTime;
                Unit<String> maxCapacity;
                Unit<Double> averageSize;
                Unit<Double> throughPut;
                Unit<Long> meanResponseTime;
                Unit<String> meanResponseSize;
            UnitTestGroupedResult unitTestResults;
                CodeCoverageResult summedCodeCoverage = new CodeCoverageResult();
                    transient String className;
                    Coverage instruction;
                    Coverage branch;
                    Coverage line;
                    Coverage complexity;
                    Coverage method;
                    Coverage clazz;
            CoverageResultAggregated aggregatedEndpointCoverage;
                int totalEndpoints;
                int testedEndpoints;
                int totalTests;

    ---nicht genutzt---
        TestResultGroup
            => UnitTestGroupedResult
                => CodeCoverageResult
                => List<UnitTestResult> results; => codecoverage
     */

    public void evalute(TestResultGroup testResultGroup){
        //Q-Rapids
        double passedTests = this.passedTestsCalculate(testResultGroup);
        System.out.println("Passed = " + passedTests + "\n");
        double fastTestBuilds = this.fastTestBuildsCalculate(testResultGroup);
        System.out.println("fastTestBuilds = " + fastTestBuilds + "\n");
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

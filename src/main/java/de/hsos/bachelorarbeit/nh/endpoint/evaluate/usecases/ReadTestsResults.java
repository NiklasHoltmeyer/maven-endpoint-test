package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestRequestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ReadTestsResults {
    public abstract TestResultGroup getTestResult();

    protected List<TestResult> parseTestResults(List<TestRequestResult> testRequestResult){
        List<TestResult> result = new ArrayList<>();

        Map<String, Map<String, List<TestRequestResult>>> stringMapMap = testRequestResult.stream()
                .collect( Collectors.groupingBy(TestRequestResult::getUrlParameterLess,
                        Collectors.groupingBy(TestRequestResult::getMethod)));

        for(String path: stringMapMap.keySet()){
            Map<String, List<TestRequestResult>> stringListMap = stringMapMap.get(path);
            for(String method : stringListMap.keySet()){
                List<TestRequestResult> list = stringListMap.get(method);
                result.add(new TestResult(path, method, list));
            }
        }
        return result;
    }

    protected TestResultGroup parseTestResultGroup(List<TestResult> testResults){
        TestResultGroup  tr = new TestResultGroup();
        tr.setResultGroups(testResults);

        return tr;
    }
}

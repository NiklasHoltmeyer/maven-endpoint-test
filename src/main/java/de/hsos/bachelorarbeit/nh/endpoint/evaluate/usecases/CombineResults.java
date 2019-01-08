package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndpointGroupInfo;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestRequestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.util.frameworks.GsonJsonUtil;

import java.util.ArrayList;
import java.util.List;

public class CombineResults {
    TestResultGroup testResultGroup;
    public CombineResults(ReadTestsResults readTestsResults, ReadExecutionInfo readExecutionInfo) throws Exception {
        List<EndpointGroupInfo> endpointGroupInfos = readExecutionInfo.getEndPointGroupInfo();
        testResultGroup = readTestsResults.getTestResult();
        GsonJsonUtil gsonJsonUtil = new GsonJsonUtil();
        gsonJsonUtil.writeJson(endpointGroupInfos, "c:/Users/nikla/Desktop/endpointGroupInfos.json", true);
        gsonJsonUtil.writeJson(testResultGroup, "c:/Users/nikla/Desktop/testResultGroup.json", true);
        this.addEndpointInfo(endpointGroupInfos);
        cleanUp();
    }

    private void cleanUp(){
        testResultGroup.getResultGroups().stream()
                .filter(x->methodOrPathNull(x.getMethod(), x.getPath()))
                .forEach(x->{
                    TestRequestResult tRR =
                            x.getTestRequestResults().stream()
                            .filter(y->methodOrPathNotNull(y.getMethod(), y.getUrlParameterLess()))
                            .findFirst().orElse(null);
                    if(tRR!=null){
                        x.setMethod(tRR.getMethod());
                        x.setPath(tRR.getUrlParameterLess());
                    }
                });
    }

    private boolean methodOrPathNull(String method, String path){
        return method==null||method.isEmpty() || path==null || path.isEmpty();
    }
    private boolean methodOrPathNotNull(String method, String path){
        return method!=null && !method.isEmpty() &&  path!=null && !path.isEmpty();
    }

    public void addEndpointInfo(List<EndpointGroupInfo> endpointGroupInfos){
        endpointGroupInfos.stream()
                .filter(x->this.methodOrPathNotNull(x.getMethod(),x.getPath()))
                .forEach(this::addEndpointInfo);
    }

    private void addEndpointInfo(EndpointGroupInfo endpointInfo) {
        String url = endpointInfo.getPath();
        String method = endpointInfo.getMethod();
        System.out.println("XYZ: " + url);
        System.out.println("SERVLET URL?");
        TestResult tr = this.testResultGroup.getTestResultServletURL(url, method).orElse(null);
        if(tr == null && methodOrPathNotNull(method,url)){
            tr = new TestResult(url, method, new ArrayList<>());
            this.testResultGroup.addResultGroup(tr);
        }
        tr.setEndpointExecutionInfosAverage(endpointInfo.getEndpointExecutionInfoAveraged());
        tr.setPath(url);
        tr.setMethod(method);
    }

    public TestResultGroup getTestResultGroup() {
        return testResultGroup;
    }
}

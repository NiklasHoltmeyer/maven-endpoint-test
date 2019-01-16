package de.hsos.bachelorarbeit.nh.endpoint.util.entities.coverage;

import de.hsos.bachelorarbeit.nh.endpoint.coverage.entities.Request;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CoverageResult {
    List<CoverageEndpointResult> endpointCoverages;
    int totalEndpoints;
    int testedEndpoints;
    int totalTests;


    public CoverageResult(HashMap<Endpoint, Integer> resultList, HashMap<Request, Integer> requestIntegerHashMap) {
        this.totalEndpoints = resultList.size();
        this.testedEndpoints = resultList.entrySet()
                .stream()
                .map(springEndpointIntegerEntry -> (springEndpointIntegerEntry==null||springEndpointIntegerEntry.getValue().equals(0))? 0 : 1)
                .collect(Collectors.summingInt(Integer::intValue));

        this.totalTests = requestIntegerHashMap
                .entrySet()
                .stream()
                .map(x -> x.getValue())
                .collect(Collectors.summingInt(Integer::intValue));

        this.endpointCoverages = resultList.keySet().stream()
                .map(k -> new CoverageEndpointResult(k,resultList.get(k)))
                .collect(Collectors.toList());

    }

    public int getTotalEndpoints() {
        return totalEndpoints;
    }

    public int getTestedEndpoints() {
        return testedEndpoints;
    }

    public int getCoverage(){
        return (this.getTotalEndpoints() != 0)? (int)(100.0 * this.getTestedEndpoints() / this.getTotalEndpoints()) : 0;
    }

    public void setTotalEndpoints(int totalEndpoints) {
        this.totalEndpoints = totalEndpoints;
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

    public List<CoverageEndpointResult> getEndpointCoverages() {
        return endpointCoverages;
    }

    public void setEndpointCoverages(List<CoverageEndpointResult> endpointCoverages) {
        this.endpointCoverages = endpointCoverages;
    }

    public boolean buildSuccessful(int threshHold){
        return this.getCoverage() >= threshHold;
    }

}

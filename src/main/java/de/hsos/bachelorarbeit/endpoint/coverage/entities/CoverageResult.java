package de.hsos.bachelorarbeit.endpoint.coverage.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CoverageResult {
    HashMap<Endpoint, Integer> endPointCoverage;
    int totalEndpoints;
    int testedEndpoints;
    int totalTests;


    public CoverageResult(HashMap<Endpoint, Integer> resultList, HashMap<Request, Integer> requestIntegerHashMap) {
        this.endPointCoverage = resultList;
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
    }
    public HashMap<Endpoint, Integer> getEndPointCoverage() {
        return endPointCoverage;
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

    public boolean buildSuccessful(int threshHold){
        return this.getCoverage() >= threshHold;
    }

    @Override
    public String toString() {
        StringBuilder sB = new StringBuilder();
        sB.append("\tCoverageResult{")
                .append(System.getProperty("line.separator"))
                .append("\tendPointCoverage=[");

        /*
        this.endPointCoverage.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .forEach(
                x -> sB.append(System.getProperty("line.separator"))
                        .append("\t")
                        .append("(Key: ")
                        .append(x.getKey().toString())
                        .append("-> Value: ")
                        .append(x.getValue().toString())
                        .append(")")
        );*/
        sB.append(System.getProperty("line.separator")).append("\t\tEndpoints with Test-GenerateCoverage:");
        this.endPointCoverage.entrySet()
                .stream()
                .filter(x->x.getValue()>0)
                .sorted(Map.Entry.comparingByValue())
                .forEach(
                        x -> sB.append(System.getProperty("line.separator"))
                                .append("\t\t\t")
                                .append("(Key: ")
                                .append(x.getKey().toString())
                                .append("-> Tests: ")
                                .append(x.getValue().toString())
                                .append(")")
                );

        sB.append(System.getProperty("line.separator")).append("\t\tEndpoints without Test-GenerateCoverage:");
        this.endPointCoverage.entrySet()
                .stream()
                .filter(x->x.getValue()==0)
                .sorted(Map.Entry.comparingByValue())
                .forEach(
                        x -> sB.append(System.getProperty("line.separator"))
                                .append("\t\t\t")
                                .append("(: ")
                                .append(x.getKey().toString())
                                .append("-> Tests: ")
                                .append(x.getValue().toString())
                                .append(")")
        );



        return sB.append(System.getProperty("line.separator"))
                .append("\t], totalEndpoints=")
                .append(totalEndpoints)
                .append(", testedEndpoints=")
                .append(testedEndpoints)
                .append(", totalTests=")
                .append(totalTests)
                .append(System.getProperty("line.separator"))
                .append('}')
                .toString();
    }
}

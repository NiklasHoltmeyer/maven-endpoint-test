package de.hsos.bachelorarbeit.nh.endpoint.coverage.usecases.generateCoverage;

import de.hsos.bachelorarbeit.nh.endpoint.coverage.entities.CoverageResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;
import de.hsos.bachelorarbeit.nh.endpoint.coverage.entities.Request;
import org.apache.maven.plugin.logging.Log;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateCoverage {
    private List<Endpoint> endpoints;
    private HashMap<Request, Integer> requestIntegerHashMap;
    Log log;

    public GenerateCoverage(Log log, List<Endpoint> endpoints, HashMap<Request, Integer> requestIntegerHashMap) {
        this.endpoints = endpoints;
        this.requestIntegerHashMap = requestIntegerHashMap;
        this.log = log;
    }

    public CoverageResult loadResult(){
        HashMap<Endpoint, Integer> resultList = new HashMap<>();

        this.endpoints
                .stream()
                .map(this::getOccurrences)
                .forEach(x -> resultList.put(x.getKey(), x.getValue()));

        return new CoverageResult(resultList, requestIntegerHashMap);
    }

    private  AbstractMap.SimpleEntry<Endpoint, Integer> getOccurrences(Endpoint endpoint){
        Map.Entry<Request, Integer> mE = this.requestIntegerHashMap.entrySet()
                        .stream()
                        .filter(x->{
                            Request r = x.getKey();
                            return endpoint.compare(r.getPath(), r.getMethod());
                        })
                .findFirst().orElse(null);

        return new AbstractMap.SimpleEntry<>(endpoint, (mE == null)? 0 : mE.getValue());
    }
}

































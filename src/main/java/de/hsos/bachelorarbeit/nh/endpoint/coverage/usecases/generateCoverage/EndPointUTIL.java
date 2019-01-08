package de.hsos.bachelorarbeit.nh.endpoint.coverage.usecases.generateCoverage;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

public abstract class EndPointUTIL {
    public abstract List<Endpoint> getEndpoints(String basePackage);

    public List<Endpoint> getEndpoints(List<String> basePackages){
        return basePackages
                .stream()
                .flatMap(x -> this.getEndpoints(x).stream())
                .collect(Collectors.toList());

    }
}

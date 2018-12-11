package de.hsos.bachelorarbeit.nh.endpoint.generate.usecases.getRequests;

import de.hsos.bachelorarbeit.nh.endpoint.generate.entities.RESTEndpoint;
import de.hsos.bachelorarbeit.nh.endpoint.generate.entities.Request;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class EndpointUTIL {
    protected abstract Set<Class<?>> getRESTController(String basePackage);
    protected abstract Stream<RESTEndpoint> getEndpoints(Class<?> clazz);
    protected abstract Request parseRequest(RESTEndpoint restEndpoint);

    public List<Request> getRequests(List<String> basePackages){
        return basePackages
                .stream()
                .flatMap(x -> this.getRequests(x).stream())
                .collect(Collectors.toList());
    }

    public List<Request> getRequests(String basePackage){
        return this.getRESTController(basePackage)
                .stream()
                .flatMap(this::getEndpoints)
                .map(this::parseRequest)
                .collect(Collectors.toList());
    }
}

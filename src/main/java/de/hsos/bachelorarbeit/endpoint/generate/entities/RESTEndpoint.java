package de.hsos.bachelorarbeit.endpoint.generate.entities;

import de.hsos.bachelorarbeit.nh.jmeter.annotation.EndpointTest;

import java.util.Objects;

public class RESTEndpoint {
    String path;
    String method;
    EndpointTest endpointTest;

    public RESTEndpoint(String path, String method, EndpointTest endpointTest) {
        this.path = path;
        this.method = method;
        this.endpointTest = endpointTest;
    }

    public boolean compare(String path, String method){
        return Objects.equals(path, this.path) && Objects.equals(method, this.method);
    }

    @Override
    public String toString() {
        return "RESTEndpoint{" +
                "path='" + path + '\'' +
                ", method='" + method + '\'' +
                ", endpointTest=" + endpointTest +
                '}';
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public EndpointTest getEndpointTest() {
        return endpointTest;
    }
}
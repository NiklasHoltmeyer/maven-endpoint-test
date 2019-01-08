package de.hsos.bachelorarbeit.nh.endpoint.generate.entities;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;
import de.hsos.bachelorarbeit.nh.jmeter.annotation.EndpointTest;

import java.util.Objects;

public class RESTEndpoint extends Endpoint {
    EndpointTest endpointTest;

    public RESTEndpoint(String path, String method, EndpointTest endpointTest) {
        super(path,method);
        this.endpointTest = endpointTest;
    }

    public boolean compare(String path, String method){
        return super.compare(path,method);
    }

    @Override
    public String toString() {
        return "RESTEndpoint{" +
                "\tpath='" + this.getPath() + '\'' +
                "\t, method='" + this.getMethod() + '\'' +
                "\t, endpointTest=" + endpointTest +
                '}';
    }

    public String getPath() {
        return this.path;
    }
    public String getMethod() {
        return this.method;
    }

    public EndpointTest getEndpointTest() {
        return endpointTest;
    }
}
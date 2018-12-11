package de.hsos.bachelorarbeit.endpoint.coverage.entities;

import java.util.Objects;

public class Endpoint {
    String path;
    String method;

    public Endpoint(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public boolean compare(String path, String method){
        return Objects.equals(path, this.path) && Objects.equals(method, this.method);
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "path='" + path + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}

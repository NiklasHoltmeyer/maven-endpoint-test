package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;

public class AVGResult{
    String method;
    String path;
    Double value;

    public AVGResult(String method, String path, Double value) {
        this.method = method;
        this.path = path;
        this.value = value;
    }

    public AVGResult(Endpoint endpoint, Double value) {
        this.value = value;
        this.path = endpoint.getPath();
        this.method = endpoint.getMethod();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AVGResult{" +
                "method='" + ((method!=null) ? method : "nullpointer") + '\'' +
                ", path='" + ((path!=null) ? path : "nullpointer") + '\'' +
                ", value=" + ((value!=null) ? value : "nullpointer") +
                '}';
    }
}

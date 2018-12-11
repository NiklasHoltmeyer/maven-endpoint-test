package de.hsos.bachelorarbeit.endpoint.coverage.entities;

import de.hsos.bachelorarbeit.endpoint.coverage.util.CountedHashMapEntity;
import java.util.Objects;

public class Request implements CountedHashMapEntity {
    String path;
    String httpMethod;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Override
    public String toString() {
        return "Request{" +
                "path='" + path + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(path, request.path) &&
                Objects.equals(httpMethod, request.httpMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, httpMethod);
    }

    @Override
    public Object getKey() {
        return this;
    }
}

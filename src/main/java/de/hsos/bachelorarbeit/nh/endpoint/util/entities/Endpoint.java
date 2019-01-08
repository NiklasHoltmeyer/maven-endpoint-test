package de.hsos.bachelorarbeit.nh.endpoint.util.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.hsos.bachelorarbeit.nh.endpoint.coverage.entities.Request;

import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Endpoint {
    protected String path;
    protected String method;

    public Endpoint(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public Endpoint(){
        this.path = "";
        this.method = "";
    }

    public boolean compare(String path, String method){
        return Objects.equals(path, this.path) && Objects.equals(method, this.method);
    }

    @JsonIgnore
    public String getPath() {
        return path;
    }
    @JsonIgnore
    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "\tpath='" + path + '\'' +
                "\t, method='" + method + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(this.getPath(), request.getPath()) &&
                Objects.equals(this.getMethod(), request.getMethod());
    }
    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}

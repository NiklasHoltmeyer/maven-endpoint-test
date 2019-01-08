package de.hsos.bachelorarbeit.nh.endpoint.coverage.entities;

import de.hsos.bachelorarbeit.nh.endpoint.coverage.util.CountedHashMapEntity;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;

import java.util.Objects;

public class Request extends Endpoint implements CountedHashMapEntity {
    public Request(String path, String method) {
        super(path, method);
    }

    public Request(){
    }

    @Override
    public String toString() {
        return "Request{\n" +
                super.toString()+
                "}";
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(0);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public Object getKey() {
        return this;
    }
}

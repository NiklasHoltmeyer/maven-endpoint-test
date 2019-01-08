package de.hsos.bachelorarbeit.nh.endpoint.test.entities;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;

public class CapacityResult extends Endpoint {
    int maxCapacity;

    public CapacityResult(String path, String method, int maxCapacity) {
        super(path,method);
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}

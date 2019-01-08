package de.hsos.bachelorarbeit.nh.endpoint.util.entities;

import de.hsos.bachelorarbeit.nh.endpoint.test.entities.WatchResult;

public class WatchResultGroup extends Endpoint {
    WatchResult<Double> cpu;
    WatchResult<Double> memory;

    public WatchResultGroup(WatchResult<Double> cpu, WatchResult<Double> memory) {
        this.cpu = cpu;
        this.memory = memory;
    }

    public WatchResultGroup(WatchResult<Double> cpu, WatchResult<Double> memory, String path, String method) {
        super(path,method);
        this.cpu = cpu;
        this.memory = memory;
    }

    public WatchResult<Double> getCpu() {
        return cpu;
    }

    public void setCpu(WatchResult<Double> cpu) {
        this.cpu = cpu;
    }

    public WatchResult<Double> getMemory() {
        return memory;
    }

    public void setMemory(WatchResult<Double> memory) {
        this.memory = memory;
    }

    public String getPath() {
        return path;
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

    @Override
    public String toString() {
        return "WatchResultGroup{" +
                "\tcpu=" + cpu +
                "\t, memory=" + memory +
                "\t, path='" + path + '\'' +
                "\t, method='" + method + '\'' +
                '}';
    }
}

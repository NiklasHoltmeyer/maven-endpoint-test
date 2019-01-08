package de.hsos.bachelorarbeit.nh.endpoint.test.entities;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;

public class WatchResultGroup extends Endpoint {
    WatchResult<Double> cpuWatchResult;
    WatchResult<Double> memoryWatchResult;

    public WatchResultGroup(WatchResult<Double> cpuWatchResult, WatchResult<Double> memoryWatchResult) {
        this.cpuWatchResult = cpuWatchResult;
        this.memoryWatchResult = memoryWatchResult;
    }

    public WatchResultGroup(WatchResult<Double> cpuWatchResult, WatchResult<Double> memoryWatchResult, String path, String method) {
        super(path,method);
        this.cpuWatchResult = cpuWatchResult;
        this.memoryWatchResult = memoryWatchResult;
    }

    public WatchResult<Double> getCpuWatchResult() {
        return cpuWatchResult;
    }

    public void setCpuWatchResult(WatchResult<Double> cpuWatchResult) {
        this.cpuWatchResult = cpuWatchResult;
    }

    public WatchResult<Double> getMemoryWatchResult() {
        return memoryWatchResult;
    }

    public void setMemoryWatchResult(WatchResult<Double> memoryWatchResult) {
        this.memoryWatchResult = memoryWatchResult;
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
}

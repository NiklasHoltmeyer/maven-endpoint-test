package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo;

public class EndpointExecutionInfo {
    ExecutionTime executionTime;
    Usage cpuUsage;
    Usage cpuUsageSystem;
    Usage memoryUsage;
    Size responseSize;

    public EndpointExecutionInfo(ExecutionTime executionTime, Usage cpuUsage, Usage cpuUsageSystem, Usage memoryUsage, Size responseSize) {
        this.executionTime = executionTime;
        this.cpuUsage = cpuUsage;
        this.cpuUsageSystem = cpuUsageSystem;
        this.memoryUsage = memoryUsage;
        this.responseSize = responseSize;
    }

    public Usage getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Usage memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public ExecutionTime getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(ExecutionTime executionTime) {
        this.executionTime = executionTime;
    }

    public Usage getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Usage cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Usage getCpuUsageSystem() {
        return cpuUsageSystem;
    }

    public void setCpuUsageSystem(Usage cpuUsageSystem) {
        this.cpuUsageSystem = cpuUsageSystem;
    }

    public Size getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(Size responseSize) {
        this.responseSize = responseSize;
    }

    @Override
    public String toString() {
        return "EndpointExecutionInfo{" +
                "executionTime=" + executionTime +
                ", cpuUsage=" + cpuUsage +
                ", cpuUsageSystem=" + cpuUsageSystem +
                ", memoryUsage=" + memoryUsage +
                ", responseSize=" + responseSize +
                '}';
    }
}



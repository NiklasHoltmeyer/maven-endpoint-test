package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities;

public class TestResult {
    /**
     * Time between sending
     */
    Double latency;
    /**
     * Time between sending the request & last response
     */
    Double elapsedTime;
    String url;
    String method;
    Integer requestCount;
    boolean success;
    long size;


    public Double getLatency() {
        return latency;
    }

    public void setLatency(Double latency) {
        this.latency = latency;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "latency=" + latency +
                ", elapsedTime=" + elapsedTime +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", requestCount=" + requestCount +
                ", success=" + success +
                ", size=" + size +
                '}';
    }
}

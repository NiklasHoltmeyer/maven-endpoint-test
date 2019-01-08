package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;

public class TestRequestResult extends Endpoint{
    /**
     * Time between sending
     */
    Double latency;
    /**
     * Time between sending the request & last response
     */
    Double elapsedTime;
    String urlParameterLess;
    Integer requestCount;
    boolean success;
    long size;

    public TestRequestResult(String path, String method) {
        super(path, method);
    }

    public Double getLatency() {
        return latency;
    }

    public void setLatency(Double latency) {
        this.latency = latency;
    }

    public String getUrlParameterLess() {
        return urlParameterLess;
    }

    public void setUrlParameterLess(String urlParameterLess) {
        this.urlParameterLess = urlParameterLess;
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

    public boolean compareServletURL(String url, String method){
        return method.equals(this.method) &&
                (this.getPath() + "/").replace("//", "/").equals((url + "/").replace("//", "/"));
    }

    @Override
    public String toString() {
        return "\tTestRequestResult{" + "\n" +
                "\t\tlatency=" + latency + "\n" +
                "\t\t, elapsedTime=" + elapsedTime + "\n" +
                "\t\t, urlParameterLess='" + urlParameterLess + '\'' + "\n" +
                "\t\t, path='" + this.getPath() + '\'' + "\n" +
                "\t\t, method='" + this.getMethod() + '\'' + "\n" +
                "\t\t, requestCount=" + requestCount + "\n" +
                "\t\t, success=" + success + "\n" +
                "\t\t, size=" + size + "\n" +
                "\t}" + "\n";
    }
}

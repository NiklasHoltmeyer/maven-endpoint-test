package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo;

import java.util.List;

public class EndpointGroupInfo {
    String url;
    String method;
    List<EndpointExecutionInfo> endpointExecutionInfos;

    public EndpointGroupInfo(String url, String method, List<EndpointExecutionInfo> endpointExecutionInfos) {
        this.url = url;
        this.method = method;
        this.endpointExecutionInfos = endpointExecutionInfos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<EndpointExecutionInfo> getEndpointExecutionInfos() {
        return endpointExecutionInfos;
    }

    public void setEndpointExecutionInfos(List<EndpointExecutionInfo> endpointExecutionInfos) {
        this.endpointExecutionInfos = endpointExecutionInfos;
    }

    @Override
    public String toString() {
        return "EndpointGroupInfo{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", endpointExecutionInfos=" + endpointExecutionInfos +
                '}';
    }
}

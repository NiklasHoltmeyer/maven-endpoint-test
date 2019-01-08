package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndPointExecutionInfo.EndpointExecutionInfo;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EndpointGroupInfo extends Endpoint {
    List<EndpointExecutionInfo> endpointExecutionInfos;

    public EndpointGroupInfo(String url, String method, List<EndpointExecutionInfo> endpointExecutionInfos) {
        super(url, method);
        this.endpointExecutionInfos = endpointExecutionInfos;
    }

    public List<EndpointExecutionInfo> getEndpointExecutionInfos() {
        return endpointExecutionInfos;
    }

    public void setEndpointExecutionInfos(List<EndpointExecutionInfo> endpointExecutionInfos) {
        this.endpointExecutionInfos = endpointExecutionInfos;
    }
}

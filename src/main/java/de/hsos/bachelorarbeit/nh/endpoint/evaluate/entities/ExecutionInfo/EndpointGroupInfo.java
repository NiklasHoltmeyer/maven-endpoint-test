package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndPointExecutionInfo.EndpointExecutionInfo;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EndpointGroupInfo extends Endpoint {
    EndpointExecutionInfo endpointExecutionInfoAveraged;

    public EndpointGroupInfo(String url, String method, EndpointExecutionInfo endpointExecutionInfos) {
        super(url, method);
        this.endpointExecutionInfoAveraged = endpointExecutionInfos;
    }

    public EndpointExecutionInfo getEndpointExecutionInfoAveraged() {
        return endpointExecutionInfoAveraged;
    }

    public void setEndpointExecutionInfoAveraged(EndpointExecutionInfo endpointExecutionInfoAveraged) {
        this.endpointExecutionInfoAveraged = endpointExecutionInfoAveraged;
    }

    @Override
    public String toString() {
        return "EndpointGroupInfo{" +
                "\tendpointExecutionInfoAveraged=" + endpointExecutionInfoAveraged +
                "\t, path='" + path + '\'' +
                "\t, method='" + method + '\'' +
                '}';
    }
}

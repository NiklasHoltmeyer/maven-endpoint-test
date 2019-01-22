package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndPointExecutionInfo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.gson.annotations.SerializedName;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EndpointExecutionInfo extends Endpoint {
    @SerializedName(value="responseTime", alternate={"executionTime"})
    Unit<Long> responseTime;
    Unit<String> responseSize;

    public EndpointExecutionInfo(String url, String method, Unit<Long> responseTime, Unit<String> responseSize) {
        super(url, method);
        this.responseTime = responseTime;
        this.responseSize = responseSize;
    }

    public Unit<Long> getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Unit<Long> responseTime) {
        this.responseTime = responseTime;
    }

    public Unit<String> getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(Unit<String> responseSize) {
        this.responseSize = responseSize;
    }

    @Override
    public String toString() {
        return "EndpointExecutionInfo{" +
                "\tresponseTime=" + responseTime +
                "\t, responseSize=" + responseSize +
                "\t, path='" + path + '\'' +
                "\t, method='" + method + '\'' +
                '}';
    }
}



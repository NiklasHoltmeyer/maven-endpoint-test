package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndpointGroupInfo;

import java.util.List;

public interface ReadExecutionInfo {
    List<EndpointGroupInfo> getEndPointGroupInfo();
}

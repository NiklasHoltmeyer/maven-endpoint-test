package de.hsos.bachelorarbeit.nh.endpoint.coverage.usecases.generateCoverage;

import de.hsos.bachelorarbeit.nh.endpoint.coverage.entities.Request;

import java.io.IOException;
import java.util.HashMap;

public interface EndpointTests {
    HashMap<Request, Integer> getAllRequests() throws IOException;
}

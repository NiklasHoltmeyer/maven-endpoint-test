package de.hsos.bachelorarbeit.endpoint.coverage.usecases.generateCoverage;

import de.hsos.bachelorarbeit.endpoint.coverage.entities.Request;

import java.io.IOException;
import java.util.HashMap;

public interface EndpointTests {
    HashMap<Request, Integer> getAllRequests() throws IOException;
}

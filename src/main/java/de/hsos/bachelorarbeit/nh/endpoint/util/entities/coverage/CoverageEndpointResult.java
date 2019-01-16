package de.hsos.bachelorarbeit.nh.endpoint.util.entities.coverage;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;

public class CoverageEndpointResult {
    Endpoint endpoint;
    int counter;

    public CoverageEndpointResult(Endpoint endpoint, int counter) {
        this.endpoint = endpoint;
        this.counter = counter;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}

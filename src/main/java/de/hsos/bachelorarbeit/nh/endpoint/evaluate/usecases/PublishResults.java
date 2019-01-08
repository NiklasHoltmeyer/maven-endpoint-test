package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface PublishResults {
    boolean publish(TestResultGroup result) throws IOException;
}

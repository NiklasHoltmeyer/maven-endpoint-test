package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResult;

import java.util.List;

public interface ReadTestsResults {
    List<TestResult> getTestResults();
}

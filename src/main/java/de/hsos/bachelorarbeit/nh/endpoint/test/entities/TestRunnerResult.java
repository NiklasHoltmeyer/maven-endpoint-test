package de.hsos.bachelorarbeit.nh.endpoint.test.entities;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Result;

public class TestRunnerResult extends Result {
    @Override
    public String toString() {
        return "TestRunnerResult{" +
                "success=" + this.isSuccess() +
                ", errorMessage='" + this.getErrorMessage() + '\'' +
                '}';
    }
}

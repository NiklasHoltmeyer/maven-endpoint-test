package de.hsos.bachelorarbeit.nh.endpoint.test.entities;

public class TestRunnerResult{
    boolean success = true;
    String errorMessage;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.success = false;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "TestRunnerResult{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}

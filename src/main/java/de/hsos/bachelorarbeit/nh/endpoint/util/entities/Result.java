package de.hsos.bachelorarbeit.nh.endpoint.util.entities;

public class Result {
    private boolean success = true;
    private String errorMessage;

    public Result(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public Result(Result result){
        this.success = result.isSuccess();
        this.errorMessage = result.getErrorMessage();
    }

    public Result() {}

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
        this.errorMessage = errorMessage;
    }
}


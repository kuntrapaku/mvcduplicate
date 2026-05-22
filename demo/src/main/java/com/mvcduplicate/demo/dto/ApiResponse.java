package com.mvcduplicate.demo.dto;

public class ApiResponse {

    private boolean success;
    private String message;
    private String data;

    public ApiResponse(boolean success, String message, String data)
    {
        this.success=success;
        this.message=message;
        this.data=data;
    }

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

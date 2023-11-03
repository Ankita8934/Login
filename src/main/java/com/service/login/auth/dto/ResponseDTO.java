package com.service.login.auth.dto;

public class ResponseDTO<T> {

    public boolean status = true;
    public String msg = null;
    public T data = null;

    public void setSuccessResponse(T data, String msg) {
        this.data = data;
        this.msg = msg;
    }

    public void setFailureResponse(String msg) {
        this.data = null;
        this.status = false;
        this.msg = msg;
    }

    public void setErrorResponse(Exception e, String msg) {
        this.msg = msg != null ? msg : e.getMessage();
        this.data = null;
        this.status = false;
        e.printStackTrace();
    }

}

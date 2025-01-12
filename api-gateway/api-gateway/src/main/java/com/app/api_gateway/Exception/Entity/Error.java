package com.app.api_gateway.Exception.Entity;

public class Error {
    public int code;
    public String message;

    public Error(int code, String message){
        this.code = code;
        this.message= message;
    }
}

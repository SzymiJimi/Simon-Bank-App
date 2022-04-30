package com.simon.bank.services.exception;

public class RequestDataNullException extends RuntimeException {

    public RequestDataNullException(String message){
        super(message);
    }
}

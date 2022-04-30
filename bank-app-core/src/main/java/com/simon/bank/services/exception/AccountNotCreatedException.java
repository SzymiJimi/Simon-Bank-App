package com.simon.bank.services.exception;

public class AccountNotCreatedException extends RuntimeException{

    public AccountNotCreatedException(String message){
        super(message);
    }
}

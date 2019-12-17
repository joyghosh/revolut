package com.revolut.exception;

public class AccountExistsException extends BankingException {

    public AccountExistsException(String message) {
        super(message);
    }

    public String getMessage(){
        return super.getMessage();
    }
}

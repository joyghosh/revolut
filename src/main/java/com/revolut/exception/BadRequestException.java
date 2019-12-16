package com.revolut.exception;

/**
 * Bad formed HTTP request.
 */
public class BadRequestException extends BankingException {

    public BadRequestException(String message) {
        super(message);
    }

    public String getMessage(){
        return super.getMessage();
    }
}

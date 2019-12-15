package com.revolut.exception;

/**
 * @author joyghosh
 * @description Generic Banking exception.
 */
public class BankingException extends Exception {

    public BankingException(String message){
        super(message);
    }

    public String getMessage(){
        return super.getMessage();
    }
}

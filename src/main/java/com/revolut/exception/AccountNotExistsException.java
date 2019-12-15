package com.revolut.exception;

/**
 * @author joyghosh
 *
 */
public class AccountNotExistsException extends BankingException {

    public AccountNotExistsException(Long accountNumber) {
        super("No such account exists "+accountNumber);
    }

    public String getMessage(){
        return super.getMessage();
    }
}

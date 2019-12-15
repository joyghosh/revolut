package com.revolut.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends BankingException {

    public InsufficientBalanceException(BigDecimal amount) {
        super("Insufficient funds. Balance found is "+amount);
    }

    public String getMessage(){
        return super.getMessage();
    }
}

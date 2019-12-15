package com.revolut.exception;

import java.math.BigDecimal;

public class NegativeCreditException extends BankingException {

    public NegativeCreditException(BigDecimal amount) {
        super("Negative credit not allowed. Credit amount is "+amount);
    }

    public String getMessage(){
        return super.getMessage();
    }
}

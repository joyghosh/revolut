package com.revolut.model;

import com.revolut.exception.InsufficientBalanceException;
import com.revolut.exception.NegativeCreditException;

import java.math.BigDecimal;

/**
 * @author joyghosh
 * @description Account model
 */
public class Account {

    Long accountNumber;
    BigDecimal balance;

    public Account(Long accountNumber){
        this.accountNumber = accountNumber;
    }

    public Account(Long accountNumber, BigDecimal balance){
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public BigDecimal credit(BigDecimal amount) throws NegativeCreditException {
        if(amount.signum() == -1){
            throw new NegativeCreditException(amount);
        }

        synchronized (this){
            balance = balance.add(amount);
        }

        return getBalance();
    }

    public BigDecimal debit(BigDecimal amount) throws InsufficientBalanceException {

        if(amount.compareTo(balance) > 0){
            throw new InsufficientBalanceException(amount);
        }

        synchronized (this){
            balance = balance.subtract(amount);
        }

        return getBalance();
    }

    public BigDecimal getBalance(){
        return balance;
    }

    public Long getAccountNumber(){
        return accountNumber;
    }
}

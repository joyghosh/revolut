package com.revolut.dao;

import com.revolut.exception.AccountExistsException;
import com.revolut.exception.AccountNotExistsException;
import com.revolut.exception.InsufficientBalanceException;
import com.revolut.exception.NegativeCreditException;
import com.revolut.model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface AccountDao {

    Account create(Account account) throws SQLException, AccountExistsException;
    Account get(Long accountAccount) throws SQLException;
    boolean tranferFunds(BigDecimal amount, Long from, Long to) throws AccountNotExistsException, InsufficientBalanceException, NegativeCreditException;
}

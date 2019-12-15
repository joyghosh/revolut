package com.revolut.dao;

import com.revolut.exception.AccountNotExistsException;
import com.revolut.exception.InsufficientBalanceException;
import com.revolut.exception.NegativeCreditException;
import com.revolut.helper.RevolutServer;
import com.revolut.model.Account;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class AccountDaoTest {

    private static RevolutServer server;
    private static AccountDao dao;

    @BeforeClass
    public static void setUp() throws Throwable {
        server = new RevolutServer();
        server.start();
        dao = new AccountDaoImpl();
    }

    @AfterClass
    public static void tearDown(){
        server.stop();
    }

    @Test
    public void testCreateAccount() throws SQLException {
        Account a = new Account(1L, new BigDecimal("1000"));
        dao.create(a);
        Account result = dao.get(1L);
        assertEquals(Long.valueOf(1l), result.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), result.getBalance());
    }

    @Test
    public void testFundTransfer() throws SQLException, AccountNotExistsException, NegativeCreditException, InsufficientBalanceException {
        Account a1 = new Account(2L, new BigDecimal("1000"));
        dao.create(a1);

        Account a2 = new Account(3L, new BigDecimal("2000"));
        dao.create(a2);

        dao.tranferFunds(new BigDecimal(100), a1.getAccountNumber(), a2.getAccountNumber());

        Account r1 = dao.get(2L);
        Account r2 = dao.get(3L);

        assertEquals(BigDecimal.valueOf(900L), r1.getBalance());
        assertEquals(BigDecimal.valueOf(2100L), r2.getBalance());
    }

    @Test(expected = InsufficientBalanceException.class)
    public void testInsufficientFunds() throws SQLException, AccountNotExistsException, NegativeCreditException, InsufficientBalanceException {
        Account a1 = new Account(4L, new BigDecimal("1000"));
        dao.create(a1);

        Account a2 = new Account(5L, new BigDecimal("2000"));
        dao.create(a2);

        dao.tranferFunds(new BigDecimal(10000L), a1.getAccountNumber(), a2.getAccountNumber());
    }

    @Test(expected = NegativeCreditException.class)
    public void testNegativeCredit() throws SQLException, AccountNotExistsException, NegativeCreditException, InsufficientBalanceException {
        Account a1 = new Account(6L, new BigDecimal("1000"));
        dao.create(a1);

        Account a2 = new Account(7L, new BigDecimal("2000"));
        dao.create(a2);

        dao.tranferFunds(new BigDecimal(-1000L), a1.getAccountNumber(), a2.getAccountNumber());
    }

    @Test(expected = AccountNotExistsException.class)
    public void testAccountNotExists() throws AccountNotExistsException, NegativeCreditException, InsufficientBalanceException {
        dao.tranferFunds(new BigDecimal(10L), 1001L, 7L);
    }
}

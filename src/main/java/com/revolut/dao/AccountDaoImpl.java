package com.revolut.dao;

import com.revolut.exception.AccountNotExistsException;
import com.revolut.exception.InsufficientBalanceException;
import com.revolut.exception.NegativeCreditException;
import com.revolut.helper.H2ConnectionManager;
import com.revolut.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author joyghosh
 */
public class AccountDaoImpl implements AccountDao{

    private static Connection conn;

    public AccountDaoImpl(){
        conn = H2ConnectionManager.getInstance().getDbConnection();
    }

    public Account create(Account account) throws SQLException {
        try{
            Long accountNumber = account.getAccountNumber();
            String balance = account.getBalance().toPlainString();

            Statement stmt = conn.createStatement();
            String SQL = "INSERT INTO ACCOUNTS VALUES("+accountNumber+","+balance+")";
            stmt.executeUpdate(SQL);

            stmt.close();
        }catch (SQLException e) {
            throw e;
        }

        return account;
    }

    public Account get(Long accountNumber) throws SQLException {
        Account account = null;
        try{

            Statement stmt = conn.createStatement();
            String SQL = "SELECT * FROM ACCOUNTS WHERE accountNumber = "+accountNumber;
            ResultSet rs = stmt.executeQuery(SQL);

            while(rs.next()){
                Long accountNum = rs.getLong("accountNumber");
                String balance = rs.getString("balance");
                account = new Account(accountNum, new BigDecimal(balance));
            }

            rs.close();
            stmt.close();
        }catch (SQLException e) {
            throw e;
        }
        return account;
    }

    public boolean tranferFunds(BigDecimal amount,
                                Long fromAccount,
                                Long toAccount)
            throws AccountNotExistsException,
            InsufficientBalanceException,
            NegativeCreditException {
        try{
            Account fromAcc = get(fromAccount);
            Account toAcc = get(toAccount);

            if(fromAcc == null){
                throw new AccountNotExistsException(fromAccount);
            }else if(toAcc == null){
                throw new AccountNotExistsException(toAccount);
            }else{

                //Update the object state.
                fromAcc.debit(amount);
                toAcc.credit(amount);

                Statement stmt = conn.createStatement();
                String fromSQL = "UPDATE ACCOUNTS SET balance="+fromAcc.getBalance()
                                    .toPlainString()+" WHERE accountNumber="+fromAcc.getAccountNumber();
                String toSQL = "UPDATE ACCOUNTS SET balance="+toAcc.getBalance()
                        .toPlainString()+" WHERE accountNumber="+toAcc.getAccountNumber();
                stmt.addBatch(fromSQL);
                stmt.addBatch(toSQL);

                int[] affectedRows = stmt.executeBatch();
                stmt.close();

                if(affectedRows.length == 2){
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}

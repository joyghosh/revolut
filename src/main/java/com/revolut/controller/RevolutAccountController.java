package com.revolut.controller;

import com.revolut.dao.AccountDao;
import com.revolut.dao.AccountDaoImpl;
import com.revolut.exception.AccountNotExistsException;
import com.revolut.exception.BadRequestException;
import com.revolut.exception.InsufficientBalanceException;
import com.revolut.exception.NegativeCreditException;
import com.revolut.model.Account;
import com.revolut.request.AccountCreateRequest;
import com.revolut.request.FundTransferRequest;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public class RevolutAccountController {

    private AccountDao accountDao;

    private RevolutAccountController(){
        accountDao = new AccountDaoImpl();
    }

    public static RevolutAccountController create(){
        return new RevolutAccountController();
    }

    public void createAccount(HttpServerExchange exchange) throws ParseException,
            BadRequestException, IOException, SQLException {
        System.out.println("creating new account");
        AccountCreateRequest req = new AccountCreateRequest().create(exchange);
        if(accountDao.create(req.getAccount()) != null){
            exchange.getResponseHeaders().put(Headers.STATUS, "200");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Revolut account created");
        }else{
            exchange.getResponseHeaders().put(Headers.STATUS, "400");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Failed to create account");
        }
    }

    public void transfer(HttpServerExchange exchange) throws BadRequestException,
            ParseException, AccountNotExistsException, NegativeCreditException, InsufficientBalanceException, IOException {
        FundTransferRequest req = new FundTransferRequest().create(exchange);
        if(accountDao.tranferFunds(req.getAmount(), req.from(), req.to())){
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Funds transferred sucessfully!");
        }else{
            exchange.getResponseHeaders().put(Headers.STATUS, "400");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Failed to transfer funds!");
        }
    }
}

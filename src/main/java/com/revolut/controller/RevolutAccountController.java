package com.revolut.controller;

import com.revolut.dao.AccountDao;
import com.revolut.dao.AccountDaoImpl;
import com.revolut.model.Account;
import com.revolut.request.AccountCreateRequest;
import com.revolut.request.FundTransferRequest;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class RevolutAccountController {

    private AccountDao accountDao;

    private RevolutAccountController(){
        accountDao = new AccountDaoImpl();
    }

    public static RevolutAccountController create(){
        return new RevolutAccountController();
    }

    public void createAccount(HttpServerExchange exchange) throws Exception {
        AccountCreateRequest req = new AccountCreateRequest().create(exchange);
        if(accountDao.create(req.getAccount()) != null){
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Revolut account created");
        }else{
            exchange.getResponseHeaders().put(Headers.STATUS, "400");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Failed to create account");
        }
    }

    public void transfer(HttpServerExchange exchange) throws Exception{
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

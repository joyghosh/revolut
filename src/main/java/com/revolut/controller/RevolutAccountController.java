package com.revolut.controller;

import com.revolut.dao.AccountDao;
import com.revolut.dao.AccountDaoImpl;
import com.revolut.exception.*;
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

    public void createAccount(HttpServerExchange exchange) throws IOException, SQLException {
        System.out.println("creating new account");
        try {
            AccountCreateRequest req = new AccountCreateRequest().create(exchange);
            if (accountDao.create(req.getAccount()) != null) {
                exchange.getResponseHeaders().put(Headers.STATUS, "200");
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                exchange.getResponseSender().send("Revolut account created");
            } else {

            }
        }catch (ParseException | BadRequestException e){
            exchange.getResponseHeaders().put(Headers.STATUS, "400");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Bad request.");
        } catch (AccountExistsException e) {
            exchange.getResponseHeaders().put(Headers.STATUS, "400");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Account already exists.");
        }
    }

    public void transfer(HttpServerExchange exchange) throws IOException {

        try{
            FundTransferRequest req = new FundTransferRequest().create(exchange);
            accountDao.tranferFunds(req.getAmount(), req.from(), req.to());
            exchange.getResponseHeaders().put(Headers.STATUS, "200");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Funds transferred successfully.");
        }catch (BadRequestException | ParseException e){
            exchange.getResponseHeaders().put(Headers.STATUS, "400");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Bad request.");
        }catch (AccountNotExistsException e){
            exchange.getResponseHeaders().put(Headers.STATUS, "404");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("No such account exists.");
        }catch(NegativeCreditException e){
            exchange.getResponseHeaders().put(Headers.STATUS, "403");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Negative credit not allowed.");
        }catch(InsufficientBalanceException e){
            exchange.getResponseHeaders().put(Headers.STATUS, "403");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Insufficient funds.");
        }
    }
}

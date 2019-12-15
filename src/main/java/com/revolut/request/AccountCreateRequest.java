package com.revolut.request;

import com.revolut.model.Account;
import io.undertow.server.HttpServerExchange;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;

public class AccountCreateRequest {

    private JSONParser parser = new JSONParser();

    private Account account;

    public AccountCreateRequest create(HttpServerExchange exchange) throws Exception {

        JSONObject object = (JSONObject) parser.parse(exchange.getInputStream().toString());
        if(object.containsKey("account_num") &&
                object.containsKey("balance")){
            account = new Account((Long)object.get("account_num"),
                    (BigDecimal)object.get("balance"));
            return new AccountCreateRequest();
        }

        throw new Exception("Invalid Request Exception");
    }

    public Account getAccount(){
        return account;
    }
}

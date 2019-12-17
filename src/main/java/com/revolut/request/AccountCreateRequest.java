package com.revolut.request;

import com.revolut.exception.BadRequestException;
import com.revolut.model.Account;
import io.undertow.server.HttpServerExchange;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class AccountCreateRequest {

    private JSONParser parser = new JSONParser();

    private Account account;

    public AccountCreateRequest create(HttpServerExchange exchange) throws
            IOException, ParseException, BadRequestException {

        System.out.println("Getting request");
        JSONObject object = (JSONObject) parser.parse(toString(exchange));
        if(object.containsKey("account_num") &&
                object.containsKey("balance")){
            this.account = new Account(
                    new Long(object.get("account_num").toString()),
                    new BigDecimal(object.get("balance").toString()));
            return this;
        }

        throw new BadRequestException("Invalid Request Exception");
    }

    public Account getAccount(){
        return account;
    }

    private String toString(HttpServerExchange exchange) throws IOException {

        StringBuilder builder = new StringBuilder();
        exchange.getRequestReceiver().receiveFullString((ex, data) -> builder.append(data));

        return builder.toString();
    }
}

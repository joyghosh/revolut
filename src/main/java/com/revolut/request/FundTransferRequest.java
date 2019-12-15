package com.revolut.request;

import com.revolut.model.Account;
import io.undertow.server.HttpServerExchange;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.math.BigDecimal;

public class FundTransferRequest {

    private JSONParser parser = new JSONParser();

    private BigDecimal amount;
    private Long from;
    private Long to;

    public FundTransferRequest create(HttpServerExchange exchange) throws Exception {

        JSONObject object = (JSONObject) parser.parse(exchange.getInputStream().toString());
        if(object.containsKey("amount") &&
                object.containsKey("from") && object.containsKey("to")){
            amount = (BigDecimal) object.get("amount");
            from = (Long) object.get("from");
            to = (Long) object.get("to");
        }

        throw new Exception("Invalid Request Exception");
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public Long from(){
        return from;
    }

    public Long to(){
        return to;
    }
}

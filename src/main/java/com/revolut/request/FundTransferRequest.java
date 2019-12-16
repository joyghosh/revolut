package com.revolut.request;

import com.revolut.exception.BadRequestException;
import com.revolut.model.Account;
import io.undertow.server.HttpServerExchange;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.math.BigDecimal;

public class FundTransferRequest {

    private JSONParser parser = new JSONParser();

    private BigDecimal amount;
    private Long from;
    private Long to;

    public FundTransferRequest create(HttpServerExchange exchange) throws ParseException, BadRequestException, IOException {

        JSONObject object = (JSONObject) parser.parse(toString(exchange));
        if(object.containsKey("amount") &&
                object.containsKey("from") && object.containsKey("to")){
            amount = new BigDecimal(object.get("amount").toString());
            from = new Long(object.get("from").toString());
            to = new Long(object.get("to").toString());

            return this;
        }

        throw new BadRequestException("Invalid Request Exception");
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

    private String toString(HttpServerExchange exchange) throws IOException {

        StringBuilder builder = new StringBuilder();
        exchange.getRequestReceiver().receiveFullString((ex, data) -> builder.append(data));
        return builder.toString();
    }
}

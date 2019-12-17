package com.revolut.api;

import com.revolut.dao.AccountDao;
import com.revolut.dao.AccountDaoImpl;
import com.revolut.helper.RevolutServer;
import com.revolut.model.Account;
import io.undertow.util.Headers;
import okhttp3.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class APITest {

    private static RevolutServer server;
    private static AccountDao dao;
    private static String BASE_URL = "http://localhost:8080";

    @BeforeClass
    public static void setUp() throws Throwable {
        server = new RevolutServer();
        server.start();
        dao = new AccountDaoImpl();

        Account a1 = new Account(8L, new BigDecimal("1000"));
        dao.create(a1);

        Account a2 = new Account(9L, new BigDecimal("2000"));
        dao.create(a2);
    }

    @AfterClass
    public static void tearDown(){
        server.stop();
    }

    @Test
    public void shouldCreateAccount() throws IOException {

        String payload = "{\"account_num\":12,\"balance\":1000}";

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), payload);

        Request request = new Request.Builder()
                .url(BASE_URL+"/v1/revolut/account")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        Response response = call.execute();
        assertEquals("text/plain", response.body().contentType().toString());
        assertEquals("Revolut account created", response.body().string());
        assertEquals("200", response.headers().get(Headers.STATUS_STRING));
    }

    @Test
    public void testAmountTransfer() throws SQLException, IOException {

        String payload = "{\"amount\":10,\"from\":8,\"to\":9}";

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), payload);

        Request request = new Request.Builder()
                .url(BASE_URL+"/v1/revolut/account/transfer")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        Response response = call.execute();
        assertEquals("Funds transferred successfully.", response.body().string());
        assertEquals("200", response.headers().get(Headers.STATUS_STRING));
    }

    @Test
    public void testNegativeCreditTransfer() throws IOException {
        String payload = "{\"amount\":-5000,\"from\":8,\"to\":9}";

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), payload);

        Request request = new Request.Builder()
                .url(BASE_URL+"/v1/revolut/account/transfer")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        Response response = call.execute();
        assertEquals("Negative credit not allowed.", response.body().string());
        assertEquals("403", response.headers().get(Headers.STATUS_STRING));
    }

    @Test
    public void testInsufficientFundTransfer() throws IOException {
        String payload = "{\"amount\":10000,\"from\":8,\"to\":9}";

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), payload);

        Request request = new Request.Builder()
                .url(BASE_URL+"/v1/revolut/account/transfer")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        Response response = call.execute();
        assertEquals("Insufficient funds.", response.body().string());
        assertEquals("403", response.headers().get(Headers.STATUS_STRING));
    }
}

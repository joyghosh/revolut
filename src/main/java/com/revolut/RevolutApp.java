package com.revolut;

import com.revolut.helper.RevolutServer;

public class RevolutApp {

    public static void main(String[] args){
        RevolutServer server = new RevolutServer();
        try {
            server.start();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

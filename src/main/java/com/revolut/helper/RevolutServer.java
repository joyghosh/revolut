package com.revolut.helper;


import com.revolut.controller.RevolutAccountController;
import io.undertow.Handlers;
import io.undertow.Undertow;

public class RevolutServer {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static Undertow server;

    public void start() throws Throwable {
        try{
            RevolutAccountController controller = RevolutAccountController.create();
            server = Undertow.builder()
                    .addHttpListener(PORT,HOST)
                    .setHandler(Handlers.exceptionHandler(
                            Handlers.path()
                            .addPrefixPath("/v1/revolut/account",
                                    Handlers.routing()
                                    .post("", controller::createAccount))
                            .addPrefixPath("/v1/revolut/account/transfer",
                                    Handlers.routing()
                                    .post("", controller::transfer))))
                    .build();

            server.start();
        }catch(Throwable t){
            throw t;
        }
    }

    public void stop(){
        server.stop();
    }
}

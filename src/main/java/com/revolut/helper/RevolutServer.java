package com.revolut.helper;


import com.revolut.controller.RevolutAccountController;
import com.revolut.exception.BankingException;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public class RevolutServer {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static RevolutAccountController controller = RevolutAccountController.create();

    private static final HttpHandler ROUTES = new RoutingHandler()
            .post("/v1/revolut/account", controller::createAccount)
            .post("/v1/revolut/account/transfer", controller::transfer);

    private static SimpleServer server = SimpleServer.simpleServer(ROUTES);

    public void start() throws Throwable {
        try{
            System.out.println("Revolut server started.");
            Undertow.Builder undertow = server.getUndertow();
//            server = Undertow.builder()
//                    .addHttpListener(PORT,HOST)
//                    .setHandler(Handlers.exceptionHandler(
//                            Handlers.path()
//                            .addPrefixPath("/v1/revolut/account",
//                                    Handlers.routing()
//                                    .post("", controller::createAccount))
//                            .addPrefixPath("/v1/revolut/account/transfer",
//                                    Handlers.routing()
//                                    .post("", controller::transfer))
//                    ).addExceptionHandler(BankingException.class))
//                    .build();

            server.start();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void stop(){
        server.getUndertow().build().stop();
    }
}

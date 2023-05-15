package org.example;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/external-service", (exchange -> {

            if ("GET".equals(exchange.getRequestMethod())) {
                String responseText = "Response from external service 1";
                try {
                    System.out.println("called");
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println("called after 3 seconds");

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                exchange.sendResponseHeaders(200, responseText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(responseText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));


        server.setExecutor(null); // creates a default executor
        server.start();

    }

}
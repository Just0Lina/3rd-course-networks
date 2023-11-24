package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java SocksProxyServer <port>");
            return;
        }
        try {
            ProxyServer proxy = new ProxyServer(Integer.parseInt(args[0].substring("--port=".length())));
            proxy.run();
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class EscravoLetras {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/letras", new LetraHandler());
        server.createContext("/status", new StatusHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Escravo 1 (letras) rodando");
    }

    static class LetraHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String texto = new String(exchange.getRequestBody().readAllBytes());
            long count = texto.chars().filter(Character::isLetter).count();
            String response = String.valueOf(count);
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        }
    }

    static class StatusHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = "OK";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        }
    }
}

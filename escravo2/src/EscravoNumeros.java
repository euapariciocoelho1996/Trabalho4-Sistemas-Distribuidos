import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class EscravoNumeros {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
        server.createContext("/numeros", new NumeroHandler());
        server.createContext("/status", new StatusHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Escravo 2 (números) rodando");
    }

    static class NumeroHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String texto = new String(exchange.getRequestBody().readAllBytes());
            long count = texto.chars().filter(Character::isDigit).count();
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

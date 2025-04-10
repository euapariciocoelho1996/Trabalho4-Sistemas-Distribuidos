import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class EscravoLetras {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/letras", new LetraHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Escravo 1 (letras) rodando");
    }

    static class LetraHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            InputStream is = exchange.getRequestBody();
            String texto = new String(is.readAllBytes());
            long count = texto.chars().filter(Character::isLetter).count();
            String response = String.valueOf(count);
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}

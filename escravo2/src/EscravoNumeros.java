import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class EscravoNumeros {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
        server.createContext("/numeros", new NumeroHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Escravo 2 (números) rodando");
    }

    static class NumeroHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        if ("HEAD".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(200, -1); // Sem corpo
            return;
        }

        InputStream is = exchange.getRequestBody();
        String texto = new String(is.readAllBytes());
        long count = texto.chars().filter(Character::isDigit).count();
        String response = String.valueOf(count);
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

}

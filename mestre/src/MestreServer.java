import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MestreServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/processar", new MestreHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("🧠 Mestre rodando na porta 8080");
    }
}

class MestreHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            System.out.println("📥 Requisição recebida!");

            InputStream is = exchange.getRequestBody();
            String texto = new String(is.readAllBytes());
            System.out.println("📄 Texto recebido do cliente: " + texto);

            final int[] letras = new int[1];
            final int[] numeros = new int[1];

            Thread t1 = new Thread(() -> {
                letras[0] = postToEscravo("http://escravo1:8081/letras", texto);
            });

            Thread t2 = new Thread(() -> {
                numeros[0] = postToEscravo("http://escravo2:8082/numeros", texto);
            });

            t1.start();
            t2.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("✅ Resultado do escravo1 (letras): " + letras[0]);
            System.out.println("✅ Resultado do escravo2 (números): " + numeros[0]);

            String resposta = "Letras: " + letras[0] + ", Números: " + numeros[0];
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, resposta.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();
        }
    }

    private int postToEscravo(String url, String texto) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.getOutputStream().write(texto.getBytes());

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String linha = in.readLine();
            in.close();
            return Integer.parseInt(linha.trim());
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao conectar com escravo: " + url);
            e.printStackTrace();
            return -1;
        }
    }
}

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
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
            String texto = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("📄 Texto recebido do cliente: " + texto);

            final int[] letras = new int[1];
            final int[] numeros = new int[1];

            Thread t1 = new Thread(() -> {
                String url = "http://escravo1:8081/letras";
                if (escravoDisponivel(url)) {
                    letras[0] = postToEscravo(url, texto);
                } else {
                    System.out.println("❌ Escravo 1 (letras) indisponível.");
                    letras[0] = -1;
                }
            });

            Thread t2 = new Thread(() -> {
                String url = "http://escravo2:8082/numeros";
                if (escravoDisponivel(url)) {
                    numeros[0] = postToEscravo(url, texto);
                } else {
                    System.out.println("❌ Escravo 2 (números) indisponível.");
                    numeros[0] = -1;
                }
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

            StringBuilder resposta = new StringBuilder();
            if (letras[0] >= 0) {
                resposta.append("Letras: ").append(letras[0]);
            } else {
                resposta.append("Erro ao processar letras");
            }

            resposta.append(", ");

            if (numeros[0] >= 0) {
                resposta.append("Números: ").append(numeros[0]);
            } else {
                resposta.append("Erro ao processar números");
            }

            byte[] respostaBytes = resposta.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, respostaBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(respostaBytes);
            os.close();
        }
    }

    private int postToEscravo(String url, String texto) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.getOutputStream().write(texto.getBytes(StandardCharsets.UTF_8));

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String linha = in.readLine();
            in.close();
            return Integer.parseInt(linha.trim());
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao conectar com escravo: " + url);
            e.printStackTrace();
            return -1;
        }
    }

    private boolean escravoDisponivel(String url) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            con.setConnectTimeout(1000);
            con.connect();
            int responseCode = con.getResponseCode();
            return responseCode >= 200 && responseCode < 400;
        } catch (Exception e) {
            return false;
        }
    }
}

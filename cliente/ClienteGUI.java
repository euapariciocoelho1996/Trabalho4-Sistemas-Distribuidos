import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;  // Import necessário
import java.nio.file.Paths;

public class ClienteGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cliente Mestre-Escravo");
        JTextArea resultado = new JTextArea(10, 30);
        resultado.setEditable(false);
        JButton enviar = new JButton("Enviar Texto");

        enviar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int retorno = fc.showOpenDialog(null);
            if (retorno == JFileChooser.APPROVE_OPTION) {
                File arquivo = fc.getSelectedFile();
                try {
                    String texto = new String(Files.readAllBytes(arquivo.toPath()));
                    String resposta = enviarTexto(texto);
                    resultado.setText(resposta);
                } catch (Exception ex) {
                    resultado.setText("Erro ao enviar: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        frame.setLayout(new FlowLayout());
        frame.add(enviar);
        frame.add(new JScrollPane(resultado));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static String enviarTexto(String texto) throws IOException {
        // ⛔ Altere o IP abaixo para o IP do Notebook 2 onde está o mestre
        URL url = new URL("http://192.168.0.100:8080/processar");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.getOutputStream().write(texto.getBytes());

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder resposta = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            resposta.append(line).append("\n");
        }
        return resposta.toString().trim();
    }
}

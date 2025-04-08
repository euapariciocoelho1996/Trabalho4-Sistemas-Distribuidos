import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ClienteGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cliente Mestre-Escravo");
        JTextArea resultado = new JTextArea(10, 30);
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
        URL url = new URL("http://<IP_MESTRE>:8080/processar"); // Coloque o IP do mestre aqui
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.getOutputStream().write(texto.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return in.readLine();
    }
}

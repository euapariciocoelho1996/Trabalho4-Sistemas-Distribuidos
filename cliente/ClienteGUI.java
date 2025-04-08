import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;

public class ClienteGUI {
    public static void main(String[] args) {
        // Configurações do tema moderno
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        JFrame frame = new JFrame("Cliente Mestre-Escravo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        // Componentes
        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        resultado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultado.setMargin(new Insets(10, 10, 10, 10));
        resultado.setLineWrap(true);
        resultado.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultado);

        JButton enviar = new JButton("Selecionar Arquivo");
        enviar.setFocusPainted(false);
        enviar.setBackground(new Color(0x1976D2));
        enviar.setForeground(Color.WHITE);
        enviar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        enviar.setPreferredSize(new Dimension(250, 40));

        JLabel titulo = new JLabel("Trabalho 4 - Sistemas Distribuidos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Ação do botão
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

        // Layout moderno com BoxLayout
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.setBackground(Color.WHITE);

        painel.add(titulo);
        painel.add(Box.createVerticalStrut(20));
        painel.add(enviar);
        painel.add(Box.createVerticalStrut(20));
        painel.add(scrollPane);

        frame.setContentPane(painel);
        frame.setVisible(true);
    }

    public static String enviarTexto(String texto) throws IOException {
        URL url = new URL("http://192.168.1.13:8080/processar");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.getOutputStream().write(texto.getBytes());

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        StringBuilder resposta = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            resposta.append(line).append("\n");
        }
        return resposta.toString().trim();
    }
}
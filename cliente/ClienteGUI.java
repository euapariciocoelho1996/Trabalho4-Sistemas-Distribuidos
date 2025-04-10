import javax.swing.*; // interface gráfica
import java.awt.*; // componentes de layout (cor, textos..)
import java.io.*; // leitura e escrita dos arquivos e conexões
import java.net.*; // comunicação via http
import java.nio.file.Files; // Utilizado para ler arquivos inteiros em uma linha

public class ClienteGUI {
    public static void main(String[] args) {
        // tenta aplicar o tem, se não der certo ignora o erro
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        // configura a janela principal
        JFrame frame = new JFrame("Cliente Mestre-Escravo"); // cria a janela
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // operação padrão ao fechar janela, encerrando a aplicação
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null); // centraliza na tela, null faz que a posição fique relativa ao centro da tela

        // Componentes
        JTextArea resultado = new JTextArea(); // area de texto
        resultado.setEditable(false);
        resultado.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // config de texto (fonte, tamanho...)
        resultado.setMargin(new Insets(10, 10, 10, 10)); // espaçamento
        resultado.setLineWrap(true); // quebra de linha automática
        resultado.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultado); // barra de rolagem caso necessário

        JButton enviar = new JButton("Selecionar Arquivo");
        enviar.setFocusPainted(false);
        enviar.setBackground(new Color(0x1976D2)); // cor do botão
        enviar.setForeground(Color.WHITE);
        enviar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        enviar.setPreferredSize(new Dimension(250, 40));

        JLabel titulo = new JLabel("Trabalho 4 - Sistemas Distribuidos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Ação do botão
        enviar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(); // escolher um arquivo
            int retorno = fc.showOpenDialog(null); // se o usuário escolheu e clicou em "Abrir". Outros valores: se ele cancelou.
            if (retorno == JFileChooser.APPROVE_OPTION) { // entra se o usuário clicou em "Abrir"
                File arquivo = fc.getSelectedFile();
                try {
                    // transforma em bytes e depois string para ter certeza que está enviando um texto
                    String texto = new String(Files.readAllBytes(arquivo.toPath())); // le o arquivo inteiro
                    String resposta = enviarTexto(texto);
                    resultado.setText(resposta);
                } catch (Exception ex) {
                    resultado.setText("Erro ao enviar: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        
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
        // Cria um objeto URL apontando para o servidor mestre na rede local.
        URL url = new URL("http://192.168.1.13:8080/processar");

        // Abre a conexão HTTP com o servidor.
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Define que o método da requisição será POST
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        // Transforma a String texto em bytes e escreve no corpo da requisição HTTP.
        con.getOutputStream().write(texto.getBytes());

        // Lê a resposta que o servidor retorna.
        // con.getInputStream() abre o canal de leitura.
        // Envolve com BufferedReader para facilitar a leitura linha por linha.
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));


        StringBuilder resposta = new StringBuilder();
        String line;
        
        // Converte o StringBuilder em uma única string.
        // Usa trim() para remover espaços ou quebras de linha extras no final.
        while ((line = in.readLine()) != null) {
            resposta.append(line).append("\n");
        }
        return resposta.toString().trim();
    }
}
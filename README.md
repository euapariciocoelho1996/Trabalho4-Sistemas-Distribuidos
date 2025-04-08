# SISTEMAS DISTRIBUÍDOS - THREADS, MIDDLEWARE, PARALELISMO, DOCKER
Este projeto implementa um sistema distribuído com arquitetura mestre-escravo, utilizando Java puro com Threads e containers Docker. O cliente envia um arquivo .txt contendo letras e números para o servidor mestre, que o processa paralelamente, delegando a contagem de letras e números a dois escravos distintos. A comunicação entre os componentes ocorre via requisições HTTP REST, e o processamento é realizado em containers separados com Docker.

```
📁 Trabalho4-Sistemas-Distribuidos
│
├── 📁 cliente               # Aplicação Java com GUI (Swing) que envia o texto
│   └── ClienteGUI.java     
│
├── 📁 mestre                # Servidor principal que recebe e delega o processamento
│   ├── Dockerfile
│   └── src/
│       └── MestreServer.java
│
├── 📁 escravo1              # Container responsável por contar letras
│   ├── Dockerfile
│   └── src/
│       └── EscravoLetras.java
│
├── 📁 escravo2              # Container responsável por contar números
│   ├── Dockerfile
│   └── src/
│       └── EscravoNumeros.java
│
├── 📁 Telas               # Prints da tela do projeto
│  
├── docker-compose.yml      # Orquestração dos containers Docker
│
└── README.md               # Documentação do projeto

```
## Requisitos
☕ Java 17 ou superior

🐳 Docker e Docker Compose

🖥️ GUI Java (Swing ou JavaFX)

🌐 HTTP para comunicação entre mestre e escravos

## 🚀 **Como Executar o Projeto**
🔹 1. Clone o repositório
```git clone https://github.com/seu-usuario/Sistema-Mestre-Escravo-Distribuido.git
cd Sistema-Mestre-Escravo-Distribuido
```
### 🔹 2. Inicie os containers no Notebook 2 (Servidor)
```
docker-compose up --build
```
Esse comando irá criar e subir os seguintes serviços:

Mestre: Porta 8080

Escravo 1 (letras): Porta 8081

Escravo 2 (números): Porta 8082

### 🔹 3. Compile e execute o cliente no Notebook 1
Certifique-se de que o Java está instalado no cliente.
```
javac ClienteGUI.java
java ClienteGUI
```

### 🛑 Importante: no arquivo ClienteGUI.java, altere a linha da URL para apontar para o IP do Notebook 2:
```
URL url = new URL("http://<IP_MESTRE>:8080/processar");
```

### 🧠 Funcionamento Interno
O cliente envia um texto via HTTP para o mestre.

O mestre usa duas threads para se comunicar com os dois escravos:

Escravo 1: conta letras

Escravo 2: conta números

O mestre espera as duas respostas e combina o resultado para retornar ao cliente.

📸 Telas do Sistema
📤 Interface do cliente Swing onde o usuário seleciona o arquivo .txt:

![Tela Inicial](https://github.com/euapariciocoelho1996/Trabalho4-Sistemas-Distribuidos/blob/main/Telas/telaInicial.png?raw=true)

⚙️ Saída no terminal do mestre ao receber e processar as requisições:

![Terminal Mestre](https://github.com/euapariciocoelho1996/Trabalho4-Sistemas-Distribuidos/blob/main/Telas/terminalMestre.png?raw=true)


📊 Retorno de contagem exibido no cliente:

![Tela Resultado](https://github.com/euapariciocoelho1996/Trabalho4-Sistemas-Distribuidos/blob/main/Telas/tela%20resultado.jpeg?raw=true)


## Contribuidores
Luis Eduardo

Francisco Aparício

Victor Macêdo

package br.org.edu.ifrn.lojacarro.mensageria;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        // Executa a leitura da fila em uma thread separada para não travar o Spring Boot
        new Thread(() -> {
            try {
                Receber.lerMensagem();
                System.out.println(">>> Ouvinte RabbitMQ iniciado com sucesso no ms-usuarios!");
            } catch (Exception e) {
                System.err.println(">>> Erro ao iniciar ouvinte RabbitMQ: " + e.getMessage());
            }
        }).start();
    }
}
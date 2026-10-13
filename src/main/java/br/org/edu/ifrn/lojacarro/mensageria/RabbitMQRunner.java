package br.org.edu.ifrn.lojacarro.mensageria;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        new Thread(() -> {
            try {
                // Escuta a fila de resposta onde o ms-usuarios envia o "true"
                Receber.lerMensagem();
                System.out.println(">>> [ms-carros] Ouvinte de respostas RabbitMQ iniciado com sucesso!");
            } catch (Exception e) {
                System.err.println(">>> [ms-carros] Erro ao iniciar ouvinte RabbitMQ: " + e.getMessage());
            }
        }).start();
    }
}
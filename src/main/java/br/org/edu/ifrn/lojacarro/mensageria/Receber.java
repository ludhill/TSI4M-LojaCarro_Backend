package br.org.edu.ifrn.lojacarro.mensageria;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Receber {

    public static void main(String[] args) {
        try {
            lerMensagem();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void lerMensagem() throws IOException {
        ConnectionFactory factory;
        Connection connection;
        Channel channel;

        String QUEUE_NAME1 = "minha_fila_resposta";
        String EXCHANGE_NAME1 = "minha_fila_resposta";
        try {
            factory = new ConnectionFactory();
            factory.setUri("amqp://localhost:5672");
            factory.setUsername("admin");
            factory.setPassword("admin");

            connection = factory.newConnection();
            channel = connection.createChannel();

            // Declaração do exchange e fila de resposta
            channel.exchangeDeclare(EXCHANGE_NAME1, "fanout", true, false, null);
            channel.queueDeclare(QUEUE_NAME1, true, false, false, null);
            channel.queueBind(QUEUE_NAME1, EXCHANGE_NAME1, "");

            // Callback para processar a resposta vinda do ms-usuarios
            DeliverCallback deliverCallbackQuestion = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(">>> [ms-carros] RESPOSTA RECEBIDA DO MS-USUARIOS = '" + message + "'");
            };

            channel.basicConsume(QUEUE_NAME1, true, deliverCallbackQuestion, consumerTag -> {
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
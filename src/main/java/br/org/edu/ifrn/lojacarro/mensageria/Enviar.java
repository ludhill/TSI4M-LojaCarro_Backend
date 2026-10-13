package br.org.edu.ifrn.lojacarro.mensageria;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class Enviar {


    public static void main(String[] args) {
        //enviarMensagem("precos de produtos teste v2...", "preco");
    }

    public static void enviarMensagem(String message, String queue) {
        ConnectionFactory factory = new ConnectionFactory();

        try {
            factory.setUri("amqp://localhost:5672");
            factory.setUsername("admin");
            factory.setPassword("admin");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(queue, true, false, false, null);
            channel.basicPublish("", queue, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("MENSAGEM ENVIADA = '" + message + "'");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


}
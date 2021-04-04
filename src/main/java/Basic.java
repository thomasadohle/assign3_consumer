import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import models.Purchase;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Basic {

    private static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection cnxn = factory.newConnection();
        Channel channel = cnxn.createChannel();
        channel.exchangeDeclare("purchase", "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "purchase", "");
        System.out.println("Waiting for messages");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            ObjectMapper mapper = new ObjectMapper();
            Purchase purchase = mapper.readValue(delivery.getBody(), Purchase.class);
            System.out.println("Received new message: " + purchase.getItems());
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

}

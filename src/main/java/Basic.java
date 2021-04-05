import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import models.Purchase;
import worker.DbWriterRunnable;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class Basic {
    private static final ConnectionFactory factory = new ConnectionFactory();
    private static final int numThreads = 16;
    private static ExecutorService executorService = Executors.newFixedThreadPool(numThreads);


    public static void main(String[] args) throws IOException, TimeoutException {
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare("purchase", "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "purchase", "");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                executorService.execute(new DbWriterRunnable(delivery.getBody()));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        };
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
    }

}

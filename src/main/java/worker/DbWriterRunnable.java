package worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import models.Purchase;
import persistance.PurchasePersistor;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DbWriterRunnable implements Runnable{
    private ConnectionFactory factory = new ConnectionFactory();
    private final Connection connection = factory.newConnection();
    private ObjectMapper mapper = new ObjectMapper();

    public DbWriterRunnable() throws IOException, TimeoutException {
    }

    @Override
    public void run(){
        try{
            System.out.println("Initializing thread " + Thread.currentThread());
            final Channel channel = connection.createChannel();
            channel.exchangeDeclare("purchase", "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, "purchase", "");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Purchase purchase = mapper.readValue(delivery.getBody(), Purchase.class);
                System.out.println("Thread " + Thread.currentThread() + " received " + purchase);
                persistData(purchase);
            };
            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void persistData(Purchase p){
        PurchasePersistor persistor = new PurchasePersistor(p);
        persistor.persistPurchase();
        persistor.persistPurchaseItems();
    }
}

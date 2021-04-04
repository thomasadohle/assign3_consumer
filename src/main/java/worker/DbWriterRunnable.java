package worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Purchase;
import persistance.PurchasePersistor;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DbWriterRunnable implements Runnable{
    private final ObjectMapper mapper = new ObjectMapper();
    private Purchase purchase;
    private PurchasePersistor persistor;

    public DbWriterRunnable(byte[] message) throws IOException, TimeoutException {
        this.purchase = mapper.readValue(message, Purchase.class);
    }

    @Override
    public void run(){
        persistData();
    }

    private synchronized void persistData(){
        this.persistor = new PurchasePersistor(this.purchase);
        persistor.persistPurchase();
        persistor.persistPurchaseItems();
    }
}

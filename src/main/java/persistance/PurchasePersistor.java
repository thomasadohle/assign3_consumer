package persistance;

import models.PurchaseItem;
import models.Purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PurchasePersistor {
    private Purchase purchase;
    private AtomicBoolean purchasePersisted = new AtomicBoolean(false);
    private AtomicBoolean purchaseItemsPersisted = new AtomicBoolean(false);
    private AtomicInteger persistPurchaseAttempts = new AtomicInteger();
    private AtomicInteger PersistPurchaseItemsAttempts = new AtomicInteger();

    public PurchasePersistor(Purchase p){
        this.purchase = p;
    }

    public synchronized boolean persistPurchase(){
        while (! this.purchasePersisted.get() && this.persistPurchaseAttempts.get()<5){
            this.executePersistPurchase();
        }
        return true;
    }

    public boolean persistPurchaseItems(){
        while (! this.purchaseItemsPersisted.get() && this.PersistPurchaseItemsAttempts.get()<5){
            this.executePersistPurchaseItems();
        }
        return true;
    }




    private synchronized void executePersistPurchase(){
        this.persistPurchaseAttempts.incrementAndGet();
        int storeId = this.purchase.getStoreId();
        int customerId = purchase.getCustomerId();
        String purchaseQuery = "INSERT INTO purchase (store_id, customer_id, purchase_date) VALUES(?,?,?)";
        try (Connection cnxn = DbConnection.getConnection(); PreparedStatement purchaseStatement = cnxn.prepareStatement(purchaseQuery)) {
            purchaseStatement.setInt(1, storeId);
            purchaseStatement.setInt(2, customerId);
            purchaseStatement.setString(3, purchase.getDate());
            boolean purchaseStored = purchaseStatement.execute();
            this.purchasePersisted.set(true);
        } catch (SQLException e) {
            String msg = "Failed to persist on thread " + Thread.currentThread();
            msg += "\n" + e.toString();
            System.out.println(msg);
        }

    }

    private void executePersistPurchaseItems() {
        this.PersistPurchaseItemsAttempts.incrementAndGet();
        int storeId = this.purchase.getStoreId();
        int customerId = purchase.getCustomerId();
        String purchaseItemsQuery = "INSERT INTO purchase_item (item_id, num_items, store_id, customer_id) VALUES (?,?,?,?)";
        try (Connection cnxn = DbConnection.getConnection(); PreparedStatement purchaseItemsStatement = cnxn.prepareStatement(purchaseItemsQuery)) {
            for (PurchaseItem item : purchase.getItems()) {
                purchaseItemsStatement.setInt(1, item.getItemID());
                purchaseItemsStatement.setInt(2, item.getNumberOfItems());
                purchaseItemsStatement.setInt(3, storeId);
                purchaseItemsStatement.setInt(4, customerId);
                purchaseItemsStatement.addBatch();
            }
            purchaseItemsStatement.executeBatch();
            this.purchaseItemsPersisted.set(true);
        } catch (Exception e){
            System.out.println("Failed to persist PurchaseItems for Purchase " + this.purchase);
        }
    }
}

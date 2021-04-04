package models;

import java.util.List;

public class Purchase {
    private List<PurchaseItem> items;
    private int storeId;
    private int customerId;
    private String date;

    public void setItems(List<PurchaseItem> items){
        this.items = items;
    }

    public void setStoreId(int storeId){
        this.storeId = storeId;
    }

    public void setCustomerId(int customerId){this.customerId=customerId;}

    public void setDate(String date){this.date=date;}


    public List<PurchaseItem> getItems(){return this.items;}

    public int getStoreId(){return this.storeId;}

    public int getCustomerId(){return this.customerId;}

    public String getDate() {return this.date;}
}

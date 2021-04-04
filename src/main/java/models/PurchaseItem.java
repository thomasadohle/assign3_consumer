package models;

public class PurchaseItem {
    private int itemID;
    private int numberOfItems;
    private Purchase purchase;

    public void setItemID(int itemID){
        this.itemID = itemID;
    }


    public void setPurchase(Purchase p){this.purchase = p;}
    public void setNumberOfItems(int numberOfItems){
        this.numberOfItems = numberOfItems;
    }

    public int getItemID(){return this.itemID;}
    public int getNumberOfItems(){return this.numberOfItems;}
    public Purchase getPurchase(){return this.purchase;}
}

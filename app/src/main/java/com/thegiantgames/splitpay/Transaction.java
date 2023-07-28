package com.thegiantgames.splitpay;

public class Transaction {

    private  String name;
    private  int imageId;
    private String settle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getSettle() {
        return settle;
    }

    public void setSettle(String settle) {
        this.settle = settle;
    }

    public Transaction(String name, int imageId, String settle) {
        this.name = name;
        this.imageId = imageId;
        this.settle = settle;
    }
}

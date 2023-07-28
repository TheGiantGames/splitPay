package com.thegiantgames.splitpay;

public class Expense {

    private  int ImageView;
    private  String expenseName , paidBy;

    public int getImageView() {
        return ImageView;
    }

    public void setImageView(int imageView) {
        ImageView = imageView;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public Expense(int imageView, String expenseName, String paidBy) {
        ImageView = imageView;
        this.expenseName = expenseName;
        this.paidBy = paidBy;
    }
}

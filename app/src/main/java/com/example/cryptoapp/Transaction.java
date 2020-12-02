package com.example.cryptoapp;

public class Transaction {
    private int image;
    private String type_of_transaction;
    private String time_done;
    private String amount;
    private String paid;

    public Transaction(int image, String type, String time, String total, String paid){ // constructor for each transaction created by user
        this.image = image;
        this.type_of_transaction = type;
        this.time_done = time;
        this.amount = total;
        this.paid = paid;
    }

    // getters methods
    public int getImage(){
        return this.image;
    }

    public String getType(){
        return this.type_of_transaction;
    }

    public String getTime(){
        return this.time_done;
    }

    public String getAmount(){
        return this.amount;
    }

    public String getPaid(){
        return this.paid;
    }





}

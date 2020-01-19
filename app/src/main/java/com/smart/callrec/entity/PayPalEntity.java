package com.smart.callrec.entity;

/**
 * Created by Harry_Hai on 2/22/2018.
 */

public class PayPalEntity {
    String price;
    String typePrice;
    String content;

    public PayPalEntity(String price, String typePrice, String content) {
        this.price = price;
        this.typePrice = typePrice;
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public String getTypePrice() {
        return typePrice;
    }

    public String getContent() {
        return content;
    }
}

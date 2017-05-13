package com.example.csc.helloworld2;

/**
 * Created by jisoo on 2017-05-09.
 */

public class List_item {

    private String name;
    private int recentPrice;
    private int stopPrice;

    public List_item(String name, int recentPrice, int stopPrice) {
        this.name = name;
        this.recentPrice = recentPrice;
        this.stopPrice = stopPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRecentPrice() {
        return recentPrice;
    }

    public void setRecentPrice(int recentPrice) {
        this.recentPrice = recentPrice;
    }

    public int getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(int stopPrice) {
        this.stopPrice = stopPrice;
    }

}

package com.example.csc.helloworld2;

/**
 * Created by jisoo on 2017-05-09.
 */
//안드로이드에서 열어보실 경우 초록색 박스를 눌러야 주석이 보여요!!
public class List_item {

    private String name;
    private int recentPrice;
    private int stopPrice;
    private String id;

    public List_item(String id, String name, int recentPrice, int stopPrice) {//constructor
        this.id = id;
        this.name = name;
        this.recentPrice = recentPrice;
        this.stopPrice = stopPrice;
    }

    public String getId() {//id 가져오기
        return id;
    }

    public void setId(String id) {//id 세팅하기
        this.id = id;
    }


    public String getName() {//name 가져오기
        return name;
    }

    public void setName(String name){//name 세팅하기
        this.name = name;
    }

    public int getRecentPrice() {//최근 가격(갱신된 가격) 가저오기
        return recentPrice;
    }

    public void setRecentPrice(int recentPrice) {//최근 가격 세팅하기
        this.recentPrice = recentPrice;
    }

    public int getStopPrice() {//경매 종료가격 가져오기
        return stopPrice;
    }

    public void setStopPrice(int stopPrice) {//경매 종료 가격 설정
        this.stopPrice = stopPrice;
    }

}

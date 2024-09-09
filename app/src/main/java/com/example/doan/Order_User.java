package com.example.doan;

public class Order_User {
    private String orderid;
    private String nameC;
    private String nameD;
    private int quantity;
    private String orderdate;
    private int total_price;
    private String imageD;

    public Order_User(){

    }

    public String getImageD() {
        return imageD;
    }

    public void setImageD(String imageD) {
        this.imageD = imageD;
    }

    public Order_User(String orderid, String nameC, String nameD, int quantity, String orderdate, int total_price, String imageD) {
        this.orderid = orderid;
        this.nameC = nameC;
        this.nameD = nameD;
        this.quantity = quantity;
        this.orderdate = orderdate;
        this.total_price = total_price;
        this.imageD = imageD;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }


    public String getNameC() {
        return nameC;
    }

    public void setNameC(String nameC) {
        this.nameC = nameC;
    }

    public String getNameD() {
        return nameD;
    }

    public void setNameD(String nameD) {
        this.nameD = nameD;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
}

package com.example.doan;

public class Order {
    private String orderid;
    private int customerid;
    private String dishid;
    private int quantity;
    private String orderdate;

    private Order(){

    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public String getDishid() {
        return dishid;
    }

    public void setDishid(String dishid) {
        this.dishid = dishid;
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

    public Order(String orderid, int customerid, String dishid, int quantity, String orderdate, int total_price) {
        this.orderid = orderid;
        this.customerid = customerid;
        this.dishid = dishid;
        this.quantity = quantity;
        this.orderdate = orderdate;
        this.total_price = total_price;
    }

    private int total_price;
}

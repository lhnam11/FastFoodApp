package com.example.doan;



public class Dish {
    private String dishId;
    private String name;
    private String description;
    private int price;
    private int quantityD;
    private String image;

    public Dish(String dishId, String name, String description, int price, int quantityD, String image) {
        this.dishId = dishId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityD = quantityD;
        this.image = image;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantityD() {
        return quantityD;
    }

    public void setQuantityD(int quantityD) {
        this.quantityD = quantityD;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

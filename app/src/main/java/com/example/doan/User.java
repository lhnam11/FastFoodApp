package com.example.doan;

public class User {
    private int customerId;
    private String nameC;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String imgUser;
    private int isAdmin;

    public User() {
        // Default constructor
    }

    public User(int customerId, String nameC, String email, String phone, String username, String password, String imgUser, int isAdmin) {
        this.customerId = customerId;
        this.nameC = nameC;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.imgUser = imgUser;
        this.isAdmin = isAdmin;
    }

    // Getter and Setter methods

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getNameC() {
        return nameC;
    }

    public void setNameC(String nameC) {
        this.nameC = nameC;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}

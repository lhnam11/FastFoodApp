package com.example.doan;

public class Discount {
    private String magiam;
    private String thongtinmagiam;
    private int phantramgiam;
    private int soluongma;
    private String hinhanh;
    private boolean isSelected; // Thêm thuộc tính isSelected

    // Constructor mặc định (cần thiết cho Firebase)
    public Discount() {
    }

    public Discount(String magiam, String thongtinmagiam, int phantramgiam, int soluongma, String hinhanh) {
        this.magiam = magiam;
        this.thongtinmagiam = thongtinmagiam;
        this.phantramgiam = phantramgiam;
        this.soluongma = soluongma;
        this.hinhanh = hinhanh;
        this.isSelected = false; // Mặc định không được chọn
    }

    // Getter và Setter
    public String getMagiam() {
        return magiam;
    }

    public void setMagiam(String magiam) {
        this.magiam = magiam;
    }

    public String getThongtinmagiam() {
        return thongtinmagiam;
    }

    public void setThongtinmagiam(String thongtinmagiam) {
        this.thongtinmagiam = thongtinmagiam;
    }

    public int getPhantramgiam() {
        return phantramgiam;
    }

    public void setPhantramgiam(int phantramgiam) {
        this.phantramgiam = phantramgiam;
    }

    public int getSoluongma() {
        return soluongma;
    }

    public void setSoluongma(int soluongma) {
        this.soluongma = soluongma;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

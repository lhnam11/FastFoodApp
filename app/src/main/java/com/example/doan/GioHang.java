package com.example.doan;

public class GioHang {
    String idgh;
    String tengh;
    long giagh; // Change from int to long
    String hinhgh;
    int soluonggh;

    public GioHang(String idgh, String tengh, long giagh, String hinhgh, int soluonggh) {
        this.idgh = idgh;
        this.tengh = tengh;
        this.giagh = giagh;
        this.hinhgh = hinhgh;
        this.soluonggh = soluonggh;
    }

    public String getIdgh() {
        return idgh;
    }

    public void setIdgh(String idgh) {
        this.idgh = idgh;
    }

    public String getTengh() {
        return tengh;
    }

    public void setTengh(String tengh) {
        this.tengh = tengh;
    }

    public long getGiagh() {
        return giagh;
    }

    public void setGiagh(long giagh) { // Change from int to long
        this.giagh = giagh;
    }

    public String getHinhgh() {
        return hinhgh;
    }

    public void setHinhgh(String hinhgh) {
        this.hinhgh = hinhgh;
    }

    public int getSoluonggh() {
        return soluonggh;
    }

    public void setSoluonggh(int soluonggh) {
        this.soluonggh = soluonggh;
    }

    public GioHang() {

    }
}

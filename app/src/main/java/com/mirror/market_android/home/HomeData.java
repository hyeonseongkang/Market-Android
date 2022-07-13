package com.mirror.market_android.home;

import android.graphics.Bitmap;

public class HomeData {

    private String title;
    private String content;
    private String location;
    private String totalPrice;
    private String price;
    private String time;
    private Bitmap image;

    public HomeData() {}

    public HomeData(String title, String content, String location, String totalPrice, String price, String time, Bitmap image) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.totalPrice = totalPrice;
        this.price = price;
        this.time = time;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

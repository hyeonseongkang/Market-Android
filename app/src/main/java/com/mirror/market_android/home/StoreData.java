package com.mirror.market_android.home;

import android.net.Uri;

import java.util.ArrayList;

public class StoreData {

    private String id;
    private String title;
    private String price;
    private String content;
    private ArrayList<String> photoKeys;
    private String key;
    private String firstUri;

    public StoreData() {

    }

    public StoreData(String id, String title, String price, String content, ArrayList<String> photoKeys, String key, String firstUri) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.content = content;
        this.photoKeys = photoKeys;
        this.key = key;
        this.firstUri = firstUri;
    }

    public StoreData(String id, String title, String price, String content, String key) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.content = content;
        this.photoKeys = null;
        this.key = key;
    }

    public String getFirstUri() {
        return firstUri;
    }

    public void setFirstUri(String firstUri) {
        this.firstUri = firstUri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public ArrayList<String> getPhotoKeys() {
        return photoKeys;
    }

    public void setPhotoKeys(ArrayList<String> photoKeys) {
        this.photoKeys = photoKeys;
    }

    public String getId() { return id;}

    public void setId(String id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

/*\\

https://firebase.google.com/docs/database/android/read-and-write?hl=ko

public void writeNewUser(String userId, String name, String email) {
    User user = new User(name, email);

    mDatabase.child("users").child(userId).setValue(user);
}

mDatabase.child("users").child(userId).child("username").setValue(name);

 */
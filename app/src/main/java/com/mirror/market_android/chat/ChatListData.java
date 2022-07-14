package com.mirror.market_android.chat;

import com.mirror.market_android.home.StoreData;

class ChatListData {
    private String key;
    private String user;
    private String lastMessage;
    private String firstUri;
    private String title;

    public ChatListData(String key, String user, String lastMessage, String firstUri, String title) {
        this.key = key;
        this.user = user;
        this.lastMessage = lastMessage;
        this.firstUri = firstUri;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getFirstUri() {
        return firstUri;
    }

    public void setFirstUri(String firstUri) {
        this.firstUri = firstUri;
    }
}

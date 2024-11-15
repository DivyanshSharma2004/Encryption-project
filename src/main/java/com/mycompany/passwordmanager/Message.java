package com.mycompany.passwordmanager;

import com.google.gson.Gson;

public class Message {
    private String sender;
    private String content;
    private long timestamp;
    private String type;

    // Constructor 
    public Message(String sender, String recipient, String content,String type) {
        this.sender = sender;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
        this.type = type;
    }

    // Getters and setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String getType(){
        return type;
    }

    // Serialize Message object to JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // Deserialize JSON to Message object
    public static Message fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Message.class);
    }
    //Overriden ToString()
    @Override
    public String toString(){
        return "Sender: " + sender + "\nContent: " + content + "\nType: " + type;
    }
}


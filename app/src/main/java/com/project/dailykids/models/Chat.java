package com.project.dailykids.models;

public class Chat {
    private String uid;
    private String name;
    private String message;
    private String timestamp;

    public Chat() {
    }

    public Chat(String uid, String name, String message, String timestamp) {
        this.uid = uid;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
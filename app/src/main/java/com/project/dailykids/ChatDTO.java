package com.project.dailykids;

public class ChatDTO {
    private String uid;
    private String name;
    private String message;
    private String timestamp;

    public ChatDTO() {
    }

    public ChatDTO(String uid, String name, String message, String timestamp) {
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
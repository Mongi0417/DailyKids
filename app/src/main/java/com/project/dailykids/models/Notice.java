package com.project.dailykids.models;

public class Notice {
    private String uid;
    private String name;
    private String title;
    private String content;
    private int isNotice;
    private long timestamp;
    private long timestampForSorting;

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getIsNotice() {
        return isNotice;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getTimestampForSorting() {
        return timestampForSorting;
    }

    public Notice() {
    }

    public Notice(String uid, String name, String title, String content, int isNotice, long timestamp, long timestampForSorting) {
        this.uid = uid;
        this.name = name;
        this.title = title;
        this.content = content;
        this.isNotice = isNotice;
        this.timestamp = timestamp;
        this.timestampForSorting = timestampForSorting;
    }
}
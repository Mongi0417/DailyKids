package com.project.dailykids.models;

import java.text.SimpleDateFormat;

public class Notice {
    private String uid;
    private String name;
    private String title;
    private String content;
    private int isNotice;
    private long postedTimestamp;
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

    public long getPostedTimestamp() {
        return postedTimestamp;
    }

    public long getTimestampForSorting() {
        return timestampForSorting;
    }

    public String postedDateForHomeNotice() {
        return new SimpleDateFormat("yyyy.MM.dd").format(postedTimestamp);
    }

    public String postedDateForNotice() {
        return new SimpleDateFormat("yyyy년 MM월 dd일").format(postedTimestamp);
    }

    /*public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDate() {
        return date;
    }*/


    /*public String postedDate() {
        return year + " " + month + " " + date;
    }*/

    public Notice() {
    }

    public Notice(String uid, String name, String title, String content, int isNotice, long postedTimestamp, long timestampForSorting) {
        this.uid = uid;
        this.name = name;
        this.title = title;
        this.content = content;
        this.isNotice = isNotice;
        this.postedTimestamp = postedTimestamp;
        this.timestampForSorting = timestampForSorting;
    }
}
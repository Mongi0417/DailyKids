package com.project.dailykids.models;

public class Notice {
    private String uid;
    private String name;
    private String title;
    private String content;
    private String year;
    private String month;
    private String date;
    private String noticeKey;
    private int notice;
    private long timestamp;

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

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDate() {
        return date;
    }

    public String getNoticeKey() {
        return noticeKey;
    }

    public int getNotice() {
        return notice;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String postedDate() {
        return year + " " + month + " " + date;
    }

    public Notice() {
    }

    public Notice(String uid, String name, String title, String content, String year, String month, String date, String noticeKey, int notice, long timestamp) {
        this.uid = uid;
        this.name = name;
        this.title = title;
        this.content = content;
        this.year = year;
        this.month = month;
        this.date = date;
        this.noticeKey = noticeKey;
        this.notice = notice;
        this.timestamp = timestamp;
    }
}

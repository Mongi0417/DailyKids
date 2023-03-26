package com.project.dailykids;

public class NoticeDTO {
    private String uid;
    private String name;
    private String title;
    private String content;
    private String year;
    private String month;
    private String date;
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

    public int getNotice() {
        return notice;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String postedDate() {
        return year + " " + month + " " + date;
    }

    public NoticeDTO() {
    }

    public NoticeDTO(String uid, String name, String title, String content, String year, String month, String date, int notice, long timestamp) {
        this.uid = uid;
        this.name = name;
        this.title = title;
        this.content = content;
        this.year = year;
        this.month = month;
        this.date = date;
        this.notice = notice;
        this.timestamp = timestamp;
    }
}

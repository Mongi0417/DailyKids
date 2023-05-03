package com.project.dailykids.models;

public class Post {
    private String uid;
    private String name;
    private String title;
    private String content;
    private String comment;
    private String date;
    private String time;
    private String dateTime;
    private long timestamp;
    private String postKey;
    private String commentKey;

    public Post() {
    }

    public Post(String uid, String name, String comment, String dateTime, String postKey, String commentKey) { // 댓글 작성
        this.uid = uid;
        this.name = name;
        this.comment = comment;
        this.dateTime = dateTime;
        this.postKey = postKey;
        this.commentKey = commentKey;
    }

    public Post(String uid, String name, String title, String content, String date, String time, long timestamp, String postKey) { // 게시글 작성
        this.uid = uid;
        this.name = name;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.timestamp = timestamp;
        this.postKey = postKey;
    }

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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDateTime() {
        return dateTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getCommentKey() {
        return commentKey;
    }

    public String getComment() {
        return comment;
    }
}

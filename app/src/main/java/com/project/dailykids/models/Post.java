package com.project.dailykids.models;

public class Post {
    private String uid;
    private String name;
    private String title;
    private String content;
    private String comment;
    private long timestamp;
    private long timestampForSorting;
    private String postKey;
    private String commentKey;

    public Post() {
    }

    public Post(String uid, String name, String title, String content, long timestamp, long timestampForSorting, String postKey) {  // 게시글 작성
        this.uid = uid;
        this.name = name;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.timestampForSorting = timestampForSorting;
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

    public String getComment() {
        return comment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getTimestampForSorting() {
        return timestampForSorting;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getCommentKey() {
        return commentKey;
    }
}
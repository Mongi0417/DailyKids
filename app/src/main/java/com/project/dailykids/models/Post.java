package com.project.dailykids.models;

import java.text.SimpleDateFormat;

public class Post {
    private String uid;
    private String name;
    private String title;
    private String content;
    private String comment;
    private long postedTimestamp;
    private long timestampForSorting;
    private String postKey;
    private String commentKey;

    public Post() {
    }

    public Post(String uid, String name, String title, String content, long postedTimestamp, long timestampForSorting, String postKey) {  // 게시글 작성
        this.uid = uid;
        this.name = name;
        this.title = title;
        this.content = content;
        this.postedTimestamp = postedTimestamp;
        this.timestampForSorting = timestampForSorting;
        this.postKey = postKey;
    }

    public Post(String uid, String name, String comment, long postedTimestamp, String postKey, String commentKey) {  // 댓글 작성
        this.uid = uid;
        this.name = name;
        this.comment = comment;
        this.postedTimestamp = postedTimestamp;
        this.postKey = postKey;
        this.commentKey = commentKey;
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

    public long getPostedTimestamp() {
        return postedTimestamp;
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

    public String postedDateForPost() { return new SimpleDateFormat("yyyy년 MM월 dd일").format(postedTimestamp); }

    public String postedDateAndTimeForComment() { return new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(postedTimestamp); }
}

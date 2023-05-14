package com.project.dailykids.models;

public class Comment {
    private String uid;
    private String nickname;
    private String comment;
    private long timestamp;
    private String postKey;
    private String commentKey;

    public Comment(String uid, String nickname, String comment, long timestamp, String postKey, String commentKey) {
        this.uid = uid;
        this.nickname = nickname;
        this.comment = comment;
        this.timestamp = timestamp;
        this.postKey = postKey;
        this.commentKey = commentKey;
    }

    public String getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getComment() {
        return comment;
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
}
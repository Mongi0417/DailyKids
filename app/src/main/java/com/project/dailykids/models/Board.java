package com.project.dailykids.models;

public class Board {
    private String uid;
    private String name;
    private String title;
    private String content;
    private String comment;
    private String date;
    private String time;
    private long timestamp;
    private String boardKey;
    private String commentKey;

    public Board() {
    }

    public Board(String uid, String name, String comment, String date, String time, String boardKey, String commentKey) { // 댓글 작성
        this.uid = uid;
        this.name = name;
        this.comment = comment;
        this.date = date;
        this.time = time;
        this.boardKey = boardKey;
        this.commentKey = commentKey;
    }

    public Board(String uid, String name, String title, String content, String date, String time, long timestamp, String boardKey) { // 게시글 작성
        this.uid = uid;
        this.name = name;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.timestamp = timestamp;
        this.boardKey = boardKey;
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

    public long getTimestamp() {
        return timestamp;
    }

    public String getBoardKey() {
        return boardKey;
    }

    public String getCommentKey() {
        return commentKey;
    }

    public String getComment() {
        return comment;
    }
}

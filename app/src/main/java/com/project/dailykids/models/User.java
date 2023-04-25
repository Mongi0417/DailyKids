package com.project.dailykids.models;

public class User {
    private String uid;
    private String email;
    private String nickname;
    private String who;
    private String kinderName;
    //private String phone;
    private String simpleDTOKey;
    //private String[] profileKey;


    public User() {
    }

    public User(String uid, String email, String nickname, String who, String kinderName, String simpleDTOKey) {
        this.uid = uid;
        this.email = email;
        this.nickname = nickname;
        this.who = who;
        this.kinderName = kinderName;
        this.simpleDTOKey = simpleDTOKey;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getWho() {
        return who;
    }

    public String getKinderName() {
        return kinderName;
    }

    public String getSimpleDTOKey() {
        return simpleDTOKey;
    }
}

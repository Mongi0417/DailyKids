package com.project.dailykids.models;

public class UserSimple {
    private String email;
    private String nickname;

    public UserSimple(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public UserSimple() {
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }
}
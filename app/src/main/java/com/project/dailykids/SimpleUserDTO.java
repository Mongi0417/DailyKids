package com.project.dailykids;

public class SimpleUserDTO {
    private String email;
    private String nickname;

    public SimpleUserDTO(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public SimpleUserDTO() {
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }
}
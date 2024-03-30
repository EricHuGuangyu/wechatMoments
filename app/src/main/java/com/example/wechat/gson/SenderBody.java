package com.example.wechat.gson;

public class SenderBody {
    private String username;
    private String nick = "";
    private String avatar;

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setNickname(String nick) {
        this.nick = nick;
    }
    public String getNickname() {
        return nick;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAvatar() {
        return avatar;
    }
}

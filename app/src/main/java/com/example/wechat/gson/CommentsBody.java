package com.example.wechat.gson;

public class CommentsBody {
    private String content;
    private SenderBody sender;

    public void setCotent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setSender(SenderBody sender) {
        this.sender = sender;
    }
    public SenderBody getSenderBody() {
        return sender;
    }
}

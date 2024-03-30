package com.example.wechat.gson;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TweetBean {
    private String content;
    private String error;

    @SerializedName("unknown error")
    private String unknown_error;

    private SenderBody sender;
    private List<ImageBody> images = new ArrayList<>();
    private List<CommentsBody> comments = new ArrayList<>();
    private List<String> imagesString = new ArrayList<>();
    private List<String> commentsString = new ArrayList<>();

    public void setCotent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setUnknown_error(String unknown_error) {
        this.unknown_error = unknown_error;
    }
    public String getUnknown_error() {
        return unknown_error;
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }

    public void setImages(List<ImageBody> images) {
        this.images = images;
    }
    public List<ImageBody> getImagesBody() {
        return images;
    }

    public List<String> getImagesBodyString() {
        imagesString.clear();
        for(ImageBody s :images) {
            imagesString.add(s.getUrl());
        }
        return imagesString;
    }

    public List<String> getCommentsBodyString() {
        commentsString.clear();
        for(CommentsBody s :comments) {
            commentsString.add(s.getSenderBody().getNickname() + ": "+ s.getContent());
        }
        return commentsString;
    }

    public void setSender(SenderBody sender) {
        this.sender = sender;
    }
    public SenderBody getSenderBody() {
        return sender;
    }

    public void setCommentsBody(List<CommentsBody> comments) {
        this.comments = comments;
    }
    public List<CommentsBody> getCommentsBody() {
        return comments;
    }

}

package com.example.cookwhat.models;

public class followData {

    private String followName;
    private String followId;
    private Integer followImg;

    public followData(String followName, String followId) {
        this.followName = followName;
        this.followId = followId;
        this.followImg = followImg;
    }

    public String getFollowName() {
        return followName;
    }

    public void setFollowName(String followName) {
        this.followName = followName;
    }

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public Integer getFollowImg() {
        return followImg;
    }

    public void setFollowImg(Integer followImg) {
        this.followImg = followImg;
    }
}

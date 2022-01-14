package com.example.cookwhat.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class UserModelDB implements Serializable {
    private String userId;
    private int profilePic;
    private String userName;
    private String emailAddr;
    private int level;
    private String country;
    private String description;
    private ArrayList<String> preference;
    private Map<String, ArrayList<String>> favouriteCategory;
    private ArrayList<String> followers;
    private ArrayList<String> followings;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPreference() {
        return preference;
    }

    public void setPreference(ArrayList<String> preference) {
        this.preference = preference;
    }

    public Map<String, ArrayList<String>> getFavouriteCategory() {
        return favouriteCategory;
    }

    public void setFavouriteCategory(Map<String, ArrayList<String>> favouriteCategory) {
        this.favouriteCategory = favouriteCategory;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public ArrayList<String> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<String> followings) {
        this.followings = followings;
    }
}

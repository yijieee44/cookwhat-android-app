package com.example.cookwhat.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UserModelDB implements Serializable {
    private String userId;
    private int profilePic;
    private String userName;
    private String emailAddr;
    private String level;
    private String country;
    private String description;
    private ArrayList<String> preference = new ArrayList<>();
    private Map<String, ArrayList<String>> favouriteCategory = new HashMap<>();
    private ArrayList<String> followersName = new ArrayList<>();
    private ArrayList<String> followingsName = new ArrayList<>();
    private ArrayList<String> followingsId = new ArrayList<>();
    private ArrayList<String> followersId = new ArrayList<>();
    private boolean showPreference;
    private boolean showEmail;

    public UserModelDB(){
        String[] favCategory = {"Arabic", "Chinese", "European", "Indian", "Mediterranean", "Korean", "Japanese", "South-East Asia", "Others" } ;
        for(int i =0; i<favCategory.length; i++){
            this.favouriteCategory.put(favCategory[i],new ArrayList<String>());
        }
        this.showEmail = false;
        this.showPreference = false;
    }


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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
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

    public ArrayList<String> getFollowersName() {
        return followersName;
    }

    public ArrayList<String> getFollowingsName() {
        return followingsName;
    }

    public void setFollowingsName(ArrayList<String> followingsName) {
        this.followingsName = followingsName;
    }

    public ArrayList<String> getFollowingsId() {
        return followingsId;
    }

    public void setFollowingsId(ArrayList<String> followingsId) {
        this.followingsId = followingsId;
    }

    public ArrayList<String> getFollowersId() {
        return followersId;
    }

    public void setFollowersId(ArrayList<String> followersId) {
        this.followersId = followersId;
    }

    public void setFollowersName(ArrayList<String> followersName) {
        this.followersName = followersName;
    }

    public boolean isShowPreference() {
        return showPreference;
    }

    public void setShowPreference(boolean showPreference) {
        this.showPreference = showPreference;
    }

    public boolean isShowEmail() {
        return showEmail;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }
}

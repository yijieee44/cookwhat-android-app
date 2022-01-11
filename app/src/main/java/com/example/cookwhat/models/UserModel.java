package com.example.cookwhat.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;

public class UserModel   {

    private String userId;
    private int profilePic;
    private String userName;
    private String emailAddr;
    private ArrayList<String> favouriteFood;
    @JsonIgnore
    private ArrayList<String> prefer;
    @JsonIgnore
    private ArrayList<String> favouriteCategory;
    @JsonIgnore
    private ArrayList<UserModel> followers;
    @JsonIgnore
    private ArrayList<UserModel> followings;
    private String password;
    @JsonIgnore
    private ArrayList<String> followersNameList;
    @JsonIgnore
    private ArrayList<String> followingsNameList;



    public UserModel(String userName, String emailAddr, String password){
        this.userName = userName;
        this.emailAddr = emailAddr;
        this.password = password;
    }

    public UserModel() {
    }

    public String getUserId() { return userId; }

    public void setFavouriteCategory(String addCategory) {
        this.favouriteCategory.add(1,addCategory);
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public void setFavouriteFood(ArrayList<String> recipeId) {

        this.favouriteFood = recipeId;
    }

    public void addFollowers(UserModel followers) {
        this.followers.add(followers) ;
    }

    public void addFollowings(UserModel followings) {
        this.followings.add(followings);
    }

    public void setFollowers(ArrayList<UserModel> followers) {
        this.followers = followers;
    }

    public void setFollowings(ArrayList<UserModel> followings) {
        this.followings = followings;
    }

    public void setPrefer(String prefer) {
        this.prefer.add(prefer);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProfilePic(int profilePic){
        this.profilePic = profilePic;
    }

    public void setUserId(String userId) { this.userId = userId; }

    public ArrayList <String> getFollowersNameList(){
        ArrayList<String>namelist = new ArrayList<>();
        if (followers != null){
            for (int i =0; i<this.followers.size();i++){
                namelist.add(this.followers.get(i).getUserName());
            }
            Collections.sort(namelist);
        }
        return namelist;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public UserModel getSpecificFollower(String followername){
        ArrayList<String> namelist = this.getFollowersNameList();
        if (namelist.contains(followername)){
            int p = this.followers.indexOf(followername);
            return this.followers.get(p);
        }
        return null;
    }

    public ArrayList <String> getFollowingsNameList(){
        ArrayList<String>namelist = new ArrayList<>();
        if (followings != null) {
            for (int i =0; i<this.followings.size();i++){
                namelist.add(this.followings.get(i).getUserName());
            }
            Collections.sort(namelist);
        }
        return namelist;
    }

    public UserModel getSpecificFollowing(String followingname){
        ArrayList<String> namelist = this.getFollowingsNameList();
        if (namelist.contains(followingname)){
            int p = this.followings.indexOf(followingname);
            return this.followings.get(p);
        }
        return null;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public String getPassword() {
        return password;
    }

    private ArrayList<String> getPrefer() {
        return prefer;
    }

    public ArrayList<String> getFavouriteCategory() {
        return favouriteCategory;
    }

    public ArrayList<String> getFavouriteFood() {
        return favouriteFood;
    }

    public ArrayList<UserModel> getFollowers() {
        return followers;
    }

    public ArrayList<UserModel> getFollowings() {
        return followings;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public String getUserName() {
        return userName;
    }


}



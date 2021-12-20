package com.example.cookwhat.models;

import java.util.ArrayList;
import java.util.Collections;

public class UserModel   {


    String userName;
    String emailAddr;
    private ArrayList<String> prefer;
    private ArrayList<String> favouriteCategory;
    private ArrayList<String> favouriteFood;
    private ArrayList<UserModel> followers;
    private ArrayList<UserModel> followings;


    public UserModel(String userName, String emailAddr){
        this.userName = userName;
        this.emailAddr = emailAddr;
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

    public void setFavouriteCategory(String addCategory) {
        this.favouriteCategory.add(1,addCategory);
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public void setFavouriteFood(String addFood) {

        this.favouriteFood.add(1,addFood);
    }

    public void setFollowers(UserModel followers) {
        this.followers.add(followers) ;
    }

    public void setFollowings(UserModel followings) {
        this.followings.add(followings);
    }

    public void setPrefer(String prefer) {
        this.prefer.add(prefer);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList <String> getFollowersNameList(){
        ArrayList<String>namelist = new ArrayList<>();
        for (int i =0; i<this.followers.size();i++){
            namelist.add(this.followers.get(i).getUserName());
        }
        Collections.sort(namelist);
        return namelist;
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
        for (int i =0; i<this.followings.size();i++){
            namelist.add(this.followings.get(i).getUserName());
        }
        Collections.sort(namelist);
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



}



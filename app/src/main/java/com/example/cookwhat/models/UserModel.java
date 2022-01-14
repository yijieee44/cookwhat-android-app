package com.example.cookwhat.models;

import java.util.ArrayList;
import java.util.Map;

public class UserModel   {

    private String userId;
    private int profilePic;
    private String userName;
    private String emailAddr;


    private ArrayList<String> preferences = new ArrayList<>();

    private ArrayList<String> favouriteCategory =  new ArrayList<>();


    private ArrayList<String> followers = new ArrayList<>();

    private ArrayList<String> followings = new ArrayList<>();

    private ArrayList<String> followingsID = new ArrayList<>();

    private ArrayList<String> followersID = new ArrayList<>();

    private ArrayList<String> favouriteFood;



    //private Map<String, ArrayList<String>> favouriteCategory;



    private String password;

    private ArrayList<String> followersNameList;

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




    public void setFollowers(String follower) {
        this.followers.add(follower) ;
    }

    public void setFollowings(String following) {
        this.followings.add(following);
    }

    public void setFollowersID(String followerID) {
        this.followersID.add(followerID) ;
    }

    public void setFollowingsID(String followingID) {
        this.followingsID.add(followingID);
    }

    public void setPreferences(String prefer) {
        this.preferences.add(prefer);
    }




    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProfilePic(int profilePic){
        this.profilePic = profilePic;
    }

    public void setUserId(String userId) { this.userId = userId; }

    /*public ArrayList <String> getFollowersNameList(){
        ArrayList<String>namelist = new ArrayList<>();
        if (followers != null){
            for (int i =0; i<this.followers.size();i++){
                namelist.add(this.followers.get(i).getUserName());
            }
            Collections.sort(namelist);
        }
        return namelist;
    }*/


    public void setPassword(String password) {
        this.password = password;
    }

   /* public UserModel getSpecificFollower(String followername){
        ArrayList<String> namelist = this.getFollowersNameList();
        if (namelist.contains(followername)){
            int p = this.followers.indexOf(followername);
            return this.followers.get(p);
        }
        return null;
    }*/

   /* public ArrayList <String> getFollowingsNameList(){
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
    }*/

    public int getProfilePic() {
        return profilePic;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getPreferences() {
        return preferences;
    }

    public ArrayList<String> getFavouriteCategory() {
        return favouriteCategory;
    }

    public ArrayList<String> getFavouriteFood() {
        return favouriteFood;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public ArrayList<String> getFollowings() {
        return followings;
    }

    public ArrayList<String> getFollowersID() {
        return followersID;
    }

    public ArrayList<String> getFollowingsID() {
        return followingsID;
    }

    public Integer getFollowingsNumber(){ return this.followings.size(); }

    public Integer getFollowersNumber(){ return this.followers.size(); }

    public String getEmailAddr() {
        return emailAddr;
    }

    public String getUserName() {
        return userName;
    }


}



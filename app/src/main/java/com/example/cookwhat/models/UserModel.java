package com.example.cookwhat.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel implements Parcelable {

    private String userId;
    private int profilePic;
    private String userName;
    private String emailAddr;
    private String description;
    private Boolean isShowPreferences;
    private Boolean isShowEmail;
    private String country;
    private String level;
    private List<String> preference = new ArrayList<>();
    private ArrayList<Integer> preferencesResId = new ArrayList<>();
    private ArrayList<String> followersName = new ArrayList<>();
    private ArrayList<String> followingsName = new ArrayList<>();
    private ArrayList<String> followingsId = new ArrayList<>();
    private ArrayList<String> followersId = new ArrayList<>();
    private Map<String, ArrayList<String>> favouriteCategory = new HashMap<>();


    private String password;

    public UserModel(String userName, String emailAddr, String password){
        this.userName = userName;
        this.emailAddr = emailAddr;
        this.password = password;
    }

    public UserModel() {
        String[] favCategory = {"Arabic", "Chinese", "European", "Indian", "Mediterranean", "Korean", "Japanese", "South-East Asia" } ;
        for(int i =0; i<favCategory.length; i++){
            this.favouriteCategory.put(favCategory[i],new ArrayList<String>());
        }
        this.isShowEmail = false;
        this.isShowPreferences = false;
    }


    protected UserModel(Parcel in) {
        userId = in.readString();
        profilePic = in.readInt();
        userName = in.readString();
        emailAddr = in.readString();
        description = in.readString();
        country = in.readString();
        level = in.readString();
        byte tmpIsShowPreferences = in.readByte();
        isShowPreferences = tmpIsShowPreferences == 0 ? null : tmpIsShowPreferences == 1;
        byte tmpIsShowEmail = in.readByte();
        isShowEmail = tmpIsShowEmail == 0 ? null : tmpIsShowEmail == 1;
        preference = in.createStringArrayList();
        followersName = in.createStringArrayList();
        followingsName = in.createStringArrayList();
        followingsId = in.createStringArrayList();
        followersId = in.createStringArrayList();
        password = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUserId() { return userId; }

    public void setFavouriteCategory(Map <String, ArrayList<String>> favouriteCategory) {
        this.favouriteCategory = favouriteCategory;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }


    public void setFollowersName(ArrayList<String> followersName) {
        this.followersName = followersName;
    }

    public void setFollowingsName(ArrayList<String> followingsName) {
        this.followingsName = followingsName;
    }

    public void setFollowersId(ArrayList<String> followersId) {
        this.followersId = followersId;
    }

    public void setFollowingsId(ArrayList<String> followingsId) {
        this.followingsId = followingsId;
    }

    public void setPreferences(List<String> preference) {
        this.preference = preference;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProfilePic(int profilePic){
        this.profilePic = profilePic;
    }

    public void setUserId(String userId) { this.userId = userId; }

    public void setPassword(String password) {
        this.password = password;
    }


    public int getProfilePic() {
        return profilePic;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getPreferences() {
        return preference;
    }

    public Map<String, ArrayList<String>> getFavouriteCategory() {
        return favouriteCategory;
    }


    public ArrayList<String> getFollowersName() {
        return followersName;
    }

    public ArrayList<String> getFollowingsName() {
        return followingsName;
    }

    public ArrayList<String> getFollowersId() {
        return followersId; }

    public ArrayList<String> getFollowingsId() {
        return followingsId;
    }

    public Integer getFollowingsNumber(){ return this.followingsName.size(); }

    public Integer getFollowersNumber(){ return this.followersName.size(); }

    public String getEmailAddr() {
        return emailAddr;
    }

    public String getUserName() {
        return userName;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getShowPreferences() {
        return isShowPreferences;
    }

    public void setShowPreferences(Boolean showPreferences) {
        isShowPreferences = showPreferences;
    }

    public Boolean getShowEmail() {
        return isShowEmail;
    }

    public void setShowEmail(Boolean showEmail) {
        isShowEmail = showEmail;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static Creator<UserModel> getCREATOR() {
        return CREATOR;
    }

    public ArrayList<Integer> getPreferencesResId() {
        return preferencesResId;
    }

    public void setPreferencesResId(ArrayList<Integer> preferencesResId) {
        this.preferencesResId = preferencesResId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeInt(profilePic);
        parcel.writeString(userName);
        parcel.writeString(emailAddr);
        parcel.writeString(description);
        parcel.writeString(country);
        parcel.writeString(level);
        parcel.writeByte((byte) (isShowPreferences == null ? 0 : isShowPreferences ? 1 : 2));
        parcel.writeByte((byte) (isShowEmail == null ? 0 : isShowEmail ? 1 : 2));
        parcel.writeStringList(preference);
        parcel.writeStringList(followersName);
        parcel.writeStringList(followingsName);
        parcel.writeStringList(followingsId);
        parcel.writeStringList(followersId);
        parcel.writeString(password);
    }
}



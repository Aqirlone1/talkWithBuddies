package com.aqirlone.talkwithbuddies;

public class UserProfileModel {

    public String userName,userUID;

    public UserProfileModel(String userName, String userUID) {
        this.userName = userName;
        this.userUID = userUID;
    }

    public UserProfileModel() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}

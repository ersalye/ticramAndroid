package com.turnpoint.ticram.modules;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User_Information {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("android_version")
    @Expose
    private String android_version;


    @SerializedName("time_to_cancel_in_sec")
    @Expose
    private int time_to_cancel_in_sec;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }


    public Integer getTime_to_cancel_in_sec() {
        return time_to_cancel_in_sec;
    }

    public void setTime_to_cancel_in_sec(Integer time_to_cancel_in_sec) {
        this.time_to_cancel_in_sec = time_to_cancel_in_sec;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }
}
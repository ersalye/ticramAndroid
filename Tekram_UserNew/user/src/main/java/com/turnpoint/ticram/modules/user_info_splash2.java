package com.turnpoint.ticram.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by marina on 18/05/2018.
 */

public class user_info_splash2 {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("rate")
    @Expose
    private String rate;

    @SerializedName("mob_active")
    @Expose
    private String mobActive;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("noti")
    @Expose
    private Integer noti;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;

    @SerializedName("order")
    @Expose
    private user_info_splash3 order;


    @SerializedName("types")
    @Expose
    private ArrayList<tabs_catogary> types=null;


    @SerializedName("android_version")
    @Expose
    private String android_version;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMobActive() {
        return mobActive;
    }

    public void setMobActive(String mobActive) {
        this.mobActive = mobActive;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getNoti() {
        return noti;
    }

    public void setNoti(Integer noti) {
        this.noti = noti;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public user_info_splash3 getOrder() {
        return order;
    }

    public void setOrder(user_info_splash3 order) {
        this.order = order;
    }



    public ArrayList<tabs_catogary> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<tabs_catogary> types) {
        this.types = types;
    }


    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }
}
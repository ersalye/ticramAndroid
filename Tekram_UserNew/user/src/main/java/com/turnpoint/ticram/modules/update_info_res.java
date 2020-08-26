package com.turnpoint.ticram.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class update_info_res {

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
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("mob_active")
    @Expose
    private String mobActive;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("noti")
    @Expose
    private Integer noti;

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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getNoti() {
        return noti;
    }

    public void setNoti(Integer noti) {
        this.noti = noti;
    }

}
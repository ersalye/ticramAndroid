package com.turnpoint.ticram.tekram_driver.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class check_order {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("account_type")
    @Expose
    private String accountType;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("time_to_cancel_trip_in_sec")
    @Expose
    private String timeToCancelTripInSec;
    @SerializedName("time_to_reload_current_orders_in_sec")
    @Expose
    private String timeToReloadCurrentOrdersInSec;

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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
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

    public String getTimeToCancelTripInSec() {
        return timeToCancelTripInSec;
    }

    public void setTimeToCancelTripInSec(String timeToCancelTripInSec) {
        this.timeToCancelTripInSec = timeToCancelTripInSec;
    }

    public String getTimeToReloadCurrentOrdersInSec() {
        return timeToReloadCurrentOrdersInSec;
    }

    public void setTimeToReloadCurrentOrdersInSec(String timeToReloadCurrentOrdersInSec) {
        this.timeToReloadCurrentOrdersInSec = timeToReloadCurrentOrdersInSec;
    }

}
package com.turnpoint.ticram.tekram_driver.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current {

    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_rate")
    @Expose
    private String userRate;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("user_photo")
    @Expose
    private String userPhoto;
    @SerializedName("user_location")
    @Expose
    private String userLocation;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("time_to_user")
    @Expose
    private String timeToUser;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("expect_fee")
    @Expose
    private String expectFee;

    @SerializedName("date_trip")
    @Expose
    private String dateTrip;


    @SerializedName("time_val")
    @Expose
    private Integer time_val;


    @SerializedName("time_to_hide")
    @Expose
    private Integer time_to_hide;

    @SerializedName("origin_address")
    @Expose
    private Integer origin_address;

    @SerializedName("destination_address")
    @Expose
    private Integer destination_address;


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRate() {
        return userRate;
    }

    public void setUserRate(String userRate) {
        this.userRate = userRate;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeToUser() {
        return timeToUser;
    }

    public void setTimeToUser(String timeToUser) {
        this.timeToUser = timeToUser;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getExpectFee() {
        return expectFee;
    }

    public void setExpectFee(String expectFee) {
        this.expectFee = expectFee;
    }

    public String getDateTrip() {
        return dateTrip;
    }

    public void setDateTrip(String dateTrip) {
        this.dateTrip = dateTrip;
    }


    public Integer getTime_val() {
        return time_val;
    }

    public void setTime_val(Integer time_val) {
        this.time_val= time_val;
    }


    public Integer getTime_to_hide() {
        return time_to_hide;
    }

    public void setTime_to_hide(Integer time_to_hide) {
        this.time_to_hide= time_to_hide;
    }

    public Integer getOrigin_address() {
        return origin_address;
    }

    public void setOrigin_address(Integer origin_address) {
        this.origin_address = origin_address;
    }

    public Integer getDestination_address() {
        return destination_address;
    }

    public void setDestination_address(Integer destination_address) {
        this.destination_address = destination_address;
    }
}
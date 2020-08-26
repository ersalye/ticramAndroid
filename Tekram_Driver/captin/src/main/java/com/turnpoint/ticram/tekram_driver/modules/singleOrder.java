package com.turnpoint.ticram.tekram_driver.modules;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class singleOrder {

    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("location_txt")
    @Expose
    private String locationTxt;
    @SerializedName("to_location")
    @Expose
    private String toLocation;
    @SerializedName("to_location_txt")
    @Expose
    private String toLocationTxt;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("date_trip")
    @Expose
    private String dateTrip;


    @SerializedName("status")
    @Expose
    private String status;


    @SerializedName("status_txt")
    @Expose
    private String status_txt;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationTxt() {
        return locationTxt;
    }

    public void setLocationTxt(String locationTxt) {
        this.locationTxt = locationTxt;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getToLocationTxt() {
        return toLocationTxt;
    }

    public void setToLocationTxt(String toLocationTxt) {
        this.toLocationTxt = toLocationTxt;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDateTrip() {
        return dateTrip;
    }

    public void setDateTrip(String dateTrip) {
        this.dateTrip = dateTrip;
    }

    public String getStatus_txt() {
        return status_txt;
    }

    public void setStatus_txt(String status_txt) {
        this.status_txt = status_txt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
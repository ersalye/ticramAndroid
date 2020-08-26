package com.turnpoint.ticram.modules;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class singleOrder {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("type_txt")
    @Expose
    private String typeTxt;
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
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("paid")
    @Expose
    private String paid;
    @SerializedName("transport_id")
    @Expose
    private String transportId;
    @SerializedName("transport_name")
    @Expose
    private String transportName;
    @SerializedName("transport_photo")
    @Expose
    private String transportPhoto;
    @SerializedName("post_date")
    @Expose
    private String postDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeTxt() {
        return typeTxt;
    }

    public void setTypeTxt(String typeTxt) {
        this.typeTxt = typeTxt;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getTransportPhoto() {
        return transportPhoto;
    }

    public void setTransportPhoto(String transportPhoto) {
        this.transportPhoto = transportPhoto;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

}
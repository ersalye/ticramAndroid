package com.turnpoint.ticram.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class user_info_splash3 {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("transport_id")
    @Expose
    private String transportId;
    @SerializedName("transport_name")
    @Expose
    private String transportName;
    @SerializedName("transport_coords")
    @Expose
    private String transportCoords;
    @SerializedName("transport_phone")
    @Expose
    private String transportPhone;
    @SerializedName("transport_rate")
    @Expose
    private String transportRate;
    @SerializedName("transport_photo")
    @Expose
    private String transportPhoto;
    @SerializedName("transport_car")
    @Expose
    private String transportCar;
    @SerializedName("plate_no")
    @Expose
    private String plateNo;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
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
    @SerializedName("subtype")
    @Expose
    private String subtype;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_txt")
    @Expose
    private String statusTxt;
    @SerializedName("canceled_by")
    @Expose
    private String canceledBy;
    @SerializedName("expect_fee")
    @Expose
    private String expectFee;
    @SerializedName("final_fee")
    @Expose
    private String finalFee;
    @SerializedName("main_fee")
    @Expose
    private String mainFee;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("old_payment")
    @Expose
    private String oldPayment;
    @SerializedName("base_fair")
    @Expose
    private String baseFair;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("order_info")
    @Expose
    private user_info_splash4 orderInfo;

    @SerializedName("rated")
    @Expose
    private String rated;

    @SerializedName("time_to_cancel_in_sec")
    @Expose
    private int time_to_cancel_in_sec;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTransportCoords() {
        return transportCoords;
    }

    public void setTransportCoords(String transportCoords) {
        this.transportCoords = transportCoords;
    }

    public String getTransportPhone() {
        return transportPhone;
    }

    public void setTransportPhone(String transportPhone) {
        this.transportPhone = transportPhone;
    }

    public String getTransportRate() {
        return transportRate;
    }

    public void setTransportRate(String transportRate) {
        this.transportRate = transportRate;
    }

    public String getTransportPhoto() {
        return transportPhoto;
    }

    public void setTransportPhoto(String transportPhoto) {
        this.transportPhoto = transportPhoto;
    }

    public String getTransportCar() {
        return transportCar;
    }

    public void setTransportCar(String transportCar) {
        this.transportCar = transportCar;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusTxt() {
        return statusTxt;
    }

    public void setStatusTxt(String statusTxt) {
        this.statusTxt = statusTxt;
    }

    public String getCanceledBy() {
        return canceledBy;
    }

    public void setCanceledBy(String canceledBy) {
        this.canceledBy = canceledBy;
    }

    public String getExpectFee() {
        return expectFee;
    }

    public void setExpectFee(String expectFee) {
        this.expectFee = expectFee;
    }

    public String getFinalFee() {
        return finalFee;
    }

    public void setFinalFee(String finalFee) {
        this.finalFee = finalFee;
    }

    public String getMainFee() {
        return mainFee;
    }

    public void setMainFee(String mainFee) {
        this.mainFee = mainFee;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOldPayment() {
        return oldPayment;
    }

    public void setOldPayment(String oldPayment) {
        this.oldPayment = oldPayment;
    }

    public String getBaseFair() {
        return baseFair;
    }

    public void setBaseFair(String baseFair) {
        this.baseFair = baseFair;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public user_info_splash4 getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(user_info_splash4 orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Integer getTime_to_cancel_in_sec() {
        return time_to_cancel_in_sec;
    }

    public void setTime_to_cancel_in_sec(Integer time_to_cancel_in_sec) {
        this.time_to_cancel_in_sec = time_to_cancel_in_sec;
    }




    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }
}

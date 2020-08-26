package com.turnpoint.ticram.modules;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class viewDetails {

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

    @SerializedName("type")
    @Expose
    private String type;
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
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_photo")
    @Expose
    private String userPhoto;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("expect_fee")
    @Expose
    private String expectFee;
    @SerializedName("final_fee")
    @Expose
    private String finalFee;


    @SerializedName("fee")
    @Expose
    private String fee;


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
    @SerializedName("time_to_user")
    @Expose
    private String timeToUser;
    @SerializedName("order_info")
    @Expose
    private viewDetails2 orderInfo;


    @SerializedName("subtype_txt")
    @Expose
    private String subtype_txt;



    @SerializedName("staticmap")
    @Expose
    private String staticmap;


    public String getStaticmap() {
        return staticmap;
    }

    public void setStaticmap(String staticmap) {
        this.staticmap = staticmap;
    }




    public String getSubtype_txt() {
        return subtype_txt;
    }

    public void setSubtype_txt(String subtype_txt) {
        this.subtype_txt = subtype_txt;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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

    public String getTimeToUser() {
        return timeToUser;
    }

    public void setTimeToUser(String timeToUser) {
        this.timeToUser = timeToUser;
    }

    public viewDetails2 getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(viewDetails2 orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee =fee;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type =type;
    }
}
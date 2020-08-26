package com.turnpoint.ticram.tekram_driver.modules;




import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class viewDetails {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_id")
    @Expose
    private String user_id;
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

    @SerializedName("fee")
    @Expose
    private String fee;



    @SerializedName("time_to_user")
    @Expose
    private String timeToUser;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;




    @SerializedName("staticmap")
    @Expose
    private String staticmap;


    public String getStaticmap() {
        return staticmap;
    }

    public void setStaticmap(String staticmap) {
        this.staticmap = staticmap;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getTimeToUser() {
        return timeToUser;
    }

    public void setTimeToUser(String timeToUser) {
        this.timeToUser = timeToUser;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }



    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee =fee;
    }

}

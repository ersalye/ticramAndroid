package com.turnpoint.ticram.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by marina on 18/05/2018.
 */

public class user_info_splash4 {
    @SerializedName("subtype")
    @Expose
    private String subtype;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("car_type")
    @Expose
    private String carType;
    @SerializedName("plate_no")
    @Expose
    private String plateNo;
    @SerializedName("transport_name")
    @Expose
    private String transportName;

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

}


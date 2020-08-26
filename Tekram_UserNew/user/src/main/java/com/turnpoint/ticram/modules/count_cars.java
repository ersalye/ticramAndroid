package com.turnpoint.ticram.modules;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class count_cars {

    @SerializedName("subtype")
    @Expose
    private String subtype;
    @SerializedName("taxi")
    @Expose
    private String taxi;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("subtype_txt")
    @Expose
    private String subtypeTxt;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("fee_text")
    @Expose
    private String fee_text;
    @SerializedName("choose_text")
    @Expose
    private String choose_text;

    @SerializedName("captain_arrival_time")
    @Expose
    private String captain_arrival_time;



    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getTaxi() {
        return taxi;
    }

    public void setTaxi(String taxi) {
        this.taxi = taxi;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSubtypeTxt() {
        return subtypeTxt;
    }

    public void setSubtypeTxt(String subtypeTxt) {
        this.subtypeTxt = subtypeTxt;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.count = fee;
    }

    public String getCaptain_arrival_time() {
        return captain_arrival_time;
    }

    public void setCaptain_arrival_time(String captain_arrival_time) {
        this.captain_arrival_time = captain_arrival_time;
    }


    public String getFee_text() {
        return fee_text;
    }

    public void setFee_text(String fee_text) {
        this.fee_text = fee_text;
    }

    public String getChoose_text() {
        return choose_text;
    }

    public void setChoose_text(String choose_text) {
        this.choose_text = choose_text;
    }
}
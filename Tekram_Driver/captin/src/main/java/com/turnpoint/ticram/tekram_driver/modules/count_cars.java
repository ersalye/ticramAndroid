package com.turnpoint.ticram.tekram_driver.modules;


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

}
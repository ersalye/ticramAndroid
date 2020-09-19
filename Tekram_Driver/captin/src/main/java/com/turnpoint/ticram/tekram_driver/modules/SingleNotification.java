package com.turnpoint.ticram.tekram_driver.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleNotification {


    @SerializedName("text")
    @Expose
    private String textNotification;

    @SerializedName("created_at")
    @Expose
    private String dateNotification;

    public String getTextNotification() {
        return textNotification;
    }

    public void setTextNotification(String textNotification) {
        this.textNotification = textNotification;
    }

    public String getDateNotification() {
        return dateNotification;
    }

    public void setDateNotification(String dateNotification) {
        this.dateNotification = dateNotification;
    }
}

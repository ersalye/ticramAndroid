package com.turnpoint.ticram.tekram_driver.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryNotifications {
    @SerializedName("notification")
    @Expose
    private List<SingleNotification> notifications = null;

    public List<SingleNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<SingleNotification> notifications) {
        this.notifications = notifications;
    }
}

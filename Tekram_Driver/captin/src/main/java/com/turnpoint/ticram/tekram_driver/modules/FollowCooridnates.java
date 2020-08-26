package com.turnpoint.ticram.tekram_driver.modules;

public class FollowCooridnates {
    double latitude;
    double longitude;
    String dateTime;
    double secondsDiff;

    public FollowCooridnates(double latitude, double longitude, String dateTime, double secondsDiff) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
        this.secondsDiff = secondsDiff;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getSecondsDiff() {
        return secondsDiff;
    }

    public void setSecondsDiff(double secondsDiff) {
        this.secondsDiff = secondsDiff;
    }
}

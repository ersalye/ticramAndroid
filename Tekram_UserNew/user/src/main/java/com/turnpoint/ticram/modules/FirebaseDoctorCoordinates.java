package com.turnpoint.ticram.modules;

/**
 * Created by marina on 15/08/2018.
 */

public class FirebaseDoctorCoordinates {


    public FirebaseDoctorInfo info;

    public FirebaseDoctorCoordinates() {

    }
    public FirebaseDoctorCoordinates(FirebaseDoctorInfo info) {
        this.info = info;
    }

    public FirebaseDoctorInfo getInfo() {
        return info;
    }

    public void setInfo(FirebaseDoctorInfo info) {
        this.info = info;
    }
}

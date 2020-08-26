package com.turnpoint.ticram.modules;

/**
 * Created by marina on 14/08/2018.
 */

public class addcoordsfirebase {
    public String coords;

    public addcoordsfirebase() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public addcoordsfirebase(String coords) {
        this.coords = coords;
        //this.order_id = order_id;
    }
}

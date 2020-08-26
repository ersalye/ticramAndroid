package com.turnpoint.ticram.modules;

/**
 * Created by marina on 13/08/2018.
 */

public class firebaseAddCoords {
    public String coords;
    public String order_id;

    public firebaseAddCoords() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public firebaseAddCoords(String coords, String order_id) {
        this.coords = coords;
        this.order_id = order_id;
    }
}
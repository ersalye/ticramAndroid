package com.turnpoint.ticram.tekram_driver.modules;

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
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }


    @Override
    public String toString() {
        return coords;
    }
}

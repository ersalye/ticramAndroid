package com.turnpoint.ticram.tekram_driver.modules;

public class availabilityFirebase {
    public String available;

    public availabilityFirebase() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public availabilityFirebase(String available) {
        this.available = available;
    }
}

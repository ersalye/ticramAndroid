package com.turnpoint.ticram.modules;

/**
 * Created by marina on 15/08/2018.
 */

public class addOrderFirebase {

    public long id;
    public String status;

    public addOrderFirebase() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public addOrderFirebase(long id, String status) {
        this.id = id;
        this.status = status;

    }
}

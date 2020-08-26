package com.turnpoint.ticram.tekram_driver.modules;

/**
 * Created by marina on 15/08/2018.
 */

public class addOrderFirebase {

    public long id;
    public String status;

    public String destination_text="";
    public String distance_to_user="";
    public String location_text="";
    public String order_timestamp="";
    public String time_to_user="";
    public String user_name="";
    public long user_rate = 0;
    public String order_info="";
    public long taxi = 1;
    public String order_count_down = "20";

    public addOrderFirebase() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public addOrderFirebase(long id, String status) {
        this.id = id;
        this.status = status;

    }
}

package com.turnpoint.ticram.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by marina on 28/03/2018.
 */

public class register_user {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("handle")
    @Expose
    private String handle;

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("User")
    @Expose
    public User user;



    public String getmsg() {
        return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }


    public String gethandle() {
        return handle;
    }

    public void sethandle(String handle) {
        this.handle = handle;
    }

    public String getsuccess() {
        return success;
    }

    public void setsuccess(String success) {
        this.success = success;
    }



}

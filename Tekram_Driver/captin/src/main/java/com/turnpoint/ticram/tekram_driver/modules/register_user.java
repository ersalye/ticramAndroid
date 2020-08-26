package com.turnpoint.ticram.tekram_driver.modules;

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
    private Boolean success;
    @SerializedName("Transport")
    @Expose
    private User transport;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public User getTransport() {
        return transport;
    }

    public void setTransport(User transport) {
        this.transport = transport;
    }

}
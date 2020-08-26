package com.turnpoint.ticram.tekram_driver.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Check {

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
    private check_order transport;

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

    public check_order getTransport() {
        return transport;
    }

    public void setTransport(check_order transport) {
        this.transport = transport;
    }

}
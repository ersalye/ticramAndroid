package com.turnpoint.ticram.modules;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class current_support {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("handle")
    @Expose
    private String handle;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("support")
    @Expose
    private support support;

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

    public support getSupport() {
        return support;
    }

    public void setSupport(support support) {
        this.support = support;
    }


}

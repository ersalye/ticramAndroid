package com.turnpoint.ticram.tekram_driver.modules;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class viewDetailsOrder {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("handle")
    @Expose
    private String handle;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("order")
    @Expose
    private viewDetails order;

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

    public viewDetails getOrder() {
        return order;
    }

    public void setOrder(viewDetails order) {
        this.order = order;
    }

}

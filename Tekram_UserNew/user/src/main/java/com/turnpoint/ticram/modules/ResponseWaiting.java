package com.turnpoint.ticram.modules;

/**
 * Created by marina on 16/04/2018.
 */



import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class ResponseWaiting {

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
    private OrderWaiting order;

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

    public OrderWaiting getOrder() {
        return order;
    }

    public void setOrder(OrderWaiting order) {
        this.order = order;
    }

}
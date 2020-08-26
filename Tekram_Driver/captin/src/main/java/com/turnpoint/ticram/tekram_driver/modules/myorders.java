package com.turnpoint.ticram.tekram_driver.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by marina on 02/04/2018.
 */

public class myorders {
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
    private User Transport;

    @SerializedName("current")
    @Expose
    private List<Current> current = null;
    @SerializedName("order")
    @Expose
    private Order order;



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

    public List<Current> getCurrent() {
        return current;
    }

    public void setCurrent(List<Current> current) {
        this.current = current;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public User getTransport() {
        return Transport;
    }

    public void setTransport(User Transport) {
        this.Transport = Transport;
    }


}

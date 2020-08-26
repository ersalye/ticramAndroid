package com.turnpoint.ticram.tekram_driver.modules;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryOrders {

    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("handle")
    @Expose
    private String handle;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("transports")
    @Expose
    private Integer transports;
    @SerializedName("payments")
    @Expose
    private String payments;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("orders")
    @Expose
    private List<singleOrder> orders = null;

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

    public Integer getTransports() {
        return transports;
    }

    public void setTransports(Integer transports) {
        this.transports = transports;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<singleOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<singleOrder> orders) {
        this.orders = orders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


}

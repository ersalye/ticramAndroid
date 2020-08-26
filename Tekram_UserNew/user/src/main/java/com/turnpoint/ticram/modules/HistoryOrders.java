package com.turnpoint.ticram.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryOrders {

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

}

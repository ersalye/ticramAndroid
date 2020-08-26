package com.turnpoint.ticram.tekram_driver.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment_end_trip {

    @SerializedName("fee")
    @Expose
    private String fee;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("user_balance")
    @Expose
    private String userBalance;

    @SerializedName("final_fee")
    @Expose
    private String finalFee;

    @SerializedName("base_fare")
    @Expose
    private String baseFare;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(String userBalance) {
        this.userBalance = userBalance;
    }

    public String getFinalFee() {
        return finalFee;
    }

    public void setFinalFee(String finalFee) {
        this.finalFee = finalFee;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

}

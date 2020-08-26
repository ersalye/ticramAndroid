package com.turnpoint.ticram.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by marina on 10/05/2018.
 */

public class current_coupoun {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("end")
    @Expose
    private Integer end;
    @SerializedName("expired_date")
    @Expose
    private String expiredDate;
    @SerializedName("text")
    @Expose
    private String text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
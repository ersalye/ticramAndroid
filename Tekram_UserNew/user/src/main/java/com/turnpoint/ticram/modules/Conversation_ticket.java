package com.turnpoint.ticram.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Conversation_ticket {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("msg")
    @Expose
    private String msg;



    public Conversation_ticket(String date, String userType, String msg){
        this.date=date;
        this.userType=userType;
        this.msg=msg;

    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
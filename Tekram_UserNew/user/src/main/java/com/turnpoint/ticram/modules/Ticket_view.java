package com.turnpoint.ticram.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Ticket_view  {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ref_no")
    @Expose
    private String refNo;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_text")
    @Expose
    private String statusText;
    @SerializedName("dept")
    @Expose
    private String dept;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("conversation")
    @Expose
    private ArrayList<Conversation_ticket> conversation = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Conversation_ticket> getConversation() {
        return conversation;
    }

    public void setConversation(ArrayList<Conversation_ticket> conversation) {
        this.conversation = conversation;
    }

}
package com.turnpoint.ticram.modules;




import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class code_mobile {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("handle")
    @Expose
    private String handle;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("User")
    @Expose
    private code_mobile_res user;

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

    public code_mobile_res getUser() {
        return user;
    }

    public void setUser(code_mobile_res user) {
        this.user = user;
    }

}
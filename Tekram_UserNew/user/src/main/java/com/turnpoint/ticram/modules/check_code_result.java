package com.turnpoint.ticram.modules;

/**
 * Created by marina on 28/03/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


    public class check_code_result {

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
        private User_Information user;

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

        public User_Information getUser() {
            return user;
        }

        public void setUser(User_Information user) {
            this.user = user;
        }

    }
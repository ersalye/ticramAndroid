package com.turnpoint.ticram.modules;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cancel_list_response {

        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("handle")
        @Expose
        private String handle;
        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("cancels")
        @Expose
        private ArrayList<Cancels> cancels = null;

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

        public ArrayList<Cancels> getCancels() {
            return cancels;
        }

        public void setCancels(ArrayList<Cancels> cancels) {
            this.cancels = cancels;
        }

    }
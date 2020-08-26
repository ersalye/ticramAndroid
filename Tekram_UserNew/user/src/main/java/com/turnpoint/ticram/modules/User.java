package com.turnpoint.ticram.modules;

/**
 * Created by marina on 28/03/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class User {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("new")
        @Expose
        public String _new;

    }



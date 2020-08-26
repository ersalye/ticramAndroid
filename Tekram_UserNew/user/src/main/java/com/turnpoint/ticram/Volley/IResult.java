package com.turnpoint.ticram.Volley;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface IResult {
    void notifySuccessPost(String response);
    void notifyError(VolleyError error);
}

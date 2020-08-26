package com.turnpoint.ticram.tekram_driver.Volley;

import com.android.volley.VolleyError;

public interface IResult {
    void notifySuccessPost(String response);
    void notifyError(VolleyError error);
}

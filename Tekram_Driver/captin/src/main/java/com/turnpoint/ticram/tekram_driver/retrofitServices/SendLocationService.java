package com.turnpoint.ticram.tekram_driver.retrofitServices;


import com.turnpoint.ticram.tekram_driver.modules.usual_result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by marina on 28/03/2018.
 */

public interface SendLocationService {

    @POST("send_location")
    @FormUrlEncoded
    Call<usual_result> send_location(@Field("access_token") String access_token,
                                     @Field("local") String local,
                                @Field("driver_id") String driver_id,
                                @Field("lat") String lat,
                                @Field("lon") String lon,
                                @Field("type") String type);
}

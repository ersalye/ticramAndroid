package com.turnpoint.ticram.retrofitServices;

import com.turnpoint.ticram.modules.register_user;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by marina on 28/03/2018.
 */

public interface registerService {

    @POST("send_code.php")
    @FormUrlEncoded
    Call<register_user> reg_user(@Field("local") String local,
                                 @Field("mob") String mobile,
                                 @Field("reg_id") String reg_id,
                                 @Field("type") String type);
}

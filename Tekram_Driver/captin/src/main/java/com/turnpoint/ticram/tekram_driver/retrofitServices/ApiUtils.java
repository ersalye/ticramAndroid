package com.turnpoint.ticram.tekram_driver.retrofitServices;

/**
 * Created by marina on 28/03/2018.
 */


public class ApiUtils {

    public static final String BASE_URL = "https://test.faistec.com/api/api/";
   // public static String SEND_LOCATION = "https://test.faistec.com/api/api/send_location";


    public static SendLocationService sendLoc() {
        return RetrofitClient.getClient(BASE_URL).create(SendLocationService.class);
    }




}

package com.turnpoint.ticram.retrofitServices;

/**
 * Created by marina on 28/03/2018.
 */


public class ApiUtils {

    public static final String BASE_URL = "http://test3.ticram.com/api/api/";

    public static registerService getregisterService() {
        return RetrofitClient.getClient(BASE_URL).create(registerService.class);
    }

  /*  public static registerService getregisterService() {
        return RetrofitClient.getClient(BASE_URL_MINE).create(registerService.class);
    }

    public static loginService getloginService(){
        return RetrofitClient.getClient(BASE_URL_MINE).create(loginService.class);

    }*/



}

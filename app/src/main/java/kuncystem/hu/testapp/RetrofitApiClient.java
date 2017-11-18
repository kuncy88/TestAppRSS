package kuncystem.hu.testapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kuncy on 2017. 11. 18..
 */

public class RetrofitApiClient {

    public static final String BASE_URL = "http://www.json-generator.com/api/json/get/";

    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}

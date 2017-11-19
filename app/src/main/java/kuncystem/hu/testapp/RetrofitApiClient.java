package kuncystem.hu.testapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kuncy on 2017. 11. 18..
 */
/**
 * Create retrofit object. We can connect to the server through the object.
 * */
public class RetrofitApiClient {

    //default url
    public static final String BASE_URL = "http://www.json-generator.com/api/json/get/";

    public static Retrofit retrofit = null;

    /**
     * Get current retrofit instance. This method use the singleton pattern design, so we have same instance every time.
     * */
    public static Retrofit getApiClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}

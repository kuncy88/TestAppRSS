package kuncystem.hu.testapp.interfaces;

import java.util.List;

import kuncystem.hu.testapp.models.ApiLevel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kuncy on 2017. 11. 18..
 */

public interface ApiInterface {
    @GET("claNBwNqOG")
    Call<ResponseBody> getJsonString(@Query("indent") int indent);
}

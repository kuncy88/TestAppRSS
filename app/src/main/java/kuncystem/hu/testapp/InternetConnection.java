package kuncystem.hu.testapp;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by kuncy on 2017. 11. 18..
 */

public class InternetConnection {

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}

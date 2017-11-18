package kuncystem.hu.testapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import kuncystem.hu.testapp.DefaultDividerItemDecoration;
import kuncystem.hu.testapp.InternetConnection;
import kuncystem.hu.testapp.R;
import kuncystem.hu.testapp.RetrofitApiClient;
import kuncystem.hu.testapp.StorageHandler;
import kuncystem.hu.testapp.adapters.RecyclerAdapter;
import kuncystem.hu.testapp.interfaces.ApiInterface;
import kuncystem.hu.testapp.models.ApiLevel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kuncy on 2017. 11. 17..
 */

public class FragmentApiTest extends Fragment {
    //We will write this file when response arrive from the server
    public final static String responseFile = "serverResponse.json";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private List<ApiLevel> apiLevel;
    private ApiInterface apiInterface;

    //this object handle the files operations
    private StorageHandler storageHandler;

    public FragmentApiTest() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        storageHandler = new StorageHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apitest, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DefaultDividerItemDecoration(getActivity()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //pull to refresh
                FragmentApiTest.this.startSync();
            }
        });

        //start
        this.startSync();
        return view;
    }

    /**
     * This method send request to the server and waiting for the response.
     * When this method gets an json response then it will create new RecyclerAdapter and it will save the response into the file.
     * */
    private void startSync(){
        //create new retrofit aoi interface
        apiInterface = RetrofitApiClient.getApiClient().create(ApiInterface.class);

        //check the internet connection
        if(InternetConnection.isNetworkConnected(getActivity())) {
            //send the request to the server
            Call<ResponseBody> call = apiInterface.getJsonString(2);
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {                      //we get the response, the request was success
                        try {
                            String json = response.body().string();

                            //write the response data into the file
                            if(!storageHandler.write(responseFile, json)){
                                Toast.makeText(getActivity(), getString(R.string.toast_no_write_server_response), Toast.LENGTH_LONG).show();
                            }

                            //create ApiLevel objects from json string
                            Gson gson = new Gson();
                            apiLevel = gson.fromJson(json, new TypeToken<List<ApiLevel>>() {
                            }.getType());

                            //create and set new adapter that the recycler view use
                            adapter = new RecyclerAdapter(getActivity(), apiLevel);
                            recyclerView.setAdapter(adapter);

                            //marker, we reach the end of operations
                            endSync(null);
                        } catch (IOException e) {
                            e.printStackTrace();
                            endSync(getString(R.string.toast_response_error));
                        }
                    } else {                                        //the request was unsuccessful
                        endSync(getString(R.string.toast_load_error_network_error));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //it happened error, so we display message on ui
                    recyclerView.setAdapter(null);
                    endSync(getString(R.string.toast_load_error_network_error));
                }
            });
        }else{                                      //no internet connection
            this.endSync(null);
            createErrorDialog();
        }
    }

    /**
     * End of the operations we can run other process.
     *
     * @param toastText If we want to display any message with Toast object we can add this parameter.
     *                  If this value of parameter is null we won't display nothing
     * */
    public void endSync(String toastText){
        swipeRefreshLayout.setRefreshing(false);
        if(toastText != null){
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create new dialog if we haven't internet.
     * */
    private void createErrorDialog(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }

        builder.setTitle(getString(R.string.dialog_title_error))
            .setMessage(getString(R.string.dialog_error_network))
            .setPositiveButton(getString(R.string.dialog_reload), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startSync();
                }
            })
            .setNegativeButton(getString(R.string.dialog_exit), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                   getActivity().finish();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setCancelable(false)
            .show();
    }
}

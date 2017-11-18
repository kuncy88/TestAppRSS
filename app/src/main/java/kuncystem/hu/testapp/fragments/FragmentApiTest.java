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
    public final static String responseFile = "serverResponse.json";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private List<ApiLevel> apiLevel;
    private ApiInterface apiInterface;

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
                FragmentApiTest.this.startSync();
            }
        });


        this.startSync();
        return view;
    }

    private void startSync(){
        apiInterface = RetrofitApiClient.getApiClient().create(ApiInterface.class);

        //checking internet connection
        if(InternetConnection.isNetworkConnected(getActivity())) {
            Call<ResponseBody> call = apiInterface.getJsonString(2);
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String json = response.body().string();

                            if(!storageHandler.write(responseFile, json)){
                                Toast.makeText(getActivity(), getString(R.string.toast_no_write_server_response), Toast.LENGTH_LONG).show();
                            }

                            Gson gson = new Gson();

                            apiLevel = gson.fromJson(json, new TypeToken<List<ApiLevel>>() {
                            }.getType());
                            adapter = new RecyclerAdapter(getActivity(), apiLevel);
                            recyclerView.setAdapter(adapter);
                            endSync(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), getString(R.string.toast_response_error), Toast.LENGTH_SHORT).show();
                            endSync(false);
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.toast_load_error_network_error), Toast.LENGTH_SHORT).show();
                        endSync(false);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    recyclerView.setAdapter(null);
                    Toast.makeText(getActivity(), getString(R.string.toast_load_error_network_error), Toast.LENGTH_SHORT).show();
                    endSync(false);
                }
            });
        }else{
            this.endSync(false);
            createErrorDialog();
        }
    }

    public void endSync(boolean success){
        swipeRefreshLayout.setRefreshing(false);
        if(success){
            Toast.makeText(getActivity(), getString(R.string.toast_update_success), Toast.LENGTH_SHORT).show();
        }
    }

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

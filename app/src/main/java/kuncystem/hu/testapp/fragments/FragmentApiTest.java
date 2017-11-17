package kuncystem.hu.testapp.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kuncystem.hu.testapp.R;

/**
 * Created by kuncy on 2017. 11. 17..
 */

public class FragmentApiTest extends Fragment {
    public FragmentApiTest() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apitest, container, false);
    }
}

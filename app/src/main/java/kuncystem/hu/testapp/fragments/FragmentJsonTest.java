package kuncystem.hu.testapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import kuncystem.hu.testapp.R;
import kuncystem.hu.testapp.StorageHandler;

/**
 * Created by kuncy on 2017. 11. 17..
 */

public class FragmentJsonTest extends Fragment {
    //this object handle the files operations
    private StorageHandler storageHandler;

    private TextView textView;

    public FragmentJsonTest() {
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
        View view = inflater.inflate(R.layout.fragment_json, container, false);
        textView = (TextView) view.findViewById(R.id.textView);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //if we choose this fragment then we read the response file and we will show the content of this file.
        if (isVisibleToUser) {
            //read content
           String content = storageHandler.read(FragmentApiTest.responseFile);
           if(content != null){              //this read was success
               textView.setText(content);
           }else{                           //we can't read the file
               Toast.makeText(
                       getActivity(),
                       String.format(getString(R.string.file_content_read_error), storageHandler.getDirectory() + File.separator + FragmentApiTest.responseFile),
                       Toast.LENGTH_LONG).show();
           }
        }
    }
}

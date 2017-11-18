package kuncystem.hu.testapp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kuncy on 2017. 11. 18..
 */

public class StorageHandler {
    public enum STATE {NONE, READ, READ_WRITE};

    private final static String directory = "/download";

    private final Context context;

    public STATE currentState = STATE.NONE;

    public StorageHandler(Context context){
        this.context = context;

        currentState = this.check();
    }

    private STATE check(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            return STATE.READ_WRITE;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return STATE.READ;
        } else {
            return STATE.NONE;
        }
    }

    public boolean write(String fileName, String content){
        switch(currentState){
            case NONE:{
                Toast.makeText(context, context.getString(R.string.storage_ext_error_no_read_write), Toast.LENGTH_LONG).show();
                return false;
            }
            case READ:{
                Toast.makeText(context, context.getString(R.string.storage_ext_error_just_read), Toast.LENGTH_LONG).show();
                return false;
            }
            case READ_WRITE:{
                break;
            }
        }

        File root = Environment.getExternalStorageDirectory();
        if(root != null){
            File dir = new File (root.getAbsolutePath() + directory);
            dir.mkdirs();

            try {
                FileOutputStream f = new FileOutputStream(new File(dir, fileName));
                f.write(content.getBytes());
                f.flush();
                f.close();

                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String read(String fileName) {
        if(currentState == STATE.NONE){
            Toast.makeText(context, context.getString(R.string.storage_ext_error_no_read_write), Toast.LENGTH_LONG).show();
            return null;
        }else{
            File root = Environment.getExternalStorageDirectory();
            if(root != null){
                try {
                    FileInputStream fin = new FileInputStream(new File (root.getAbsolutePath() + directory, fileName));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fin));

                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    reader.close();
                    fin.close();

                    return sb.toString();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public String getDirectory(){
        File root = Environment.getExternalStorageDirectory();
        if(root == null){
            return directory;
        }
        return root.getAbsolutePath() + directory;
    }
}

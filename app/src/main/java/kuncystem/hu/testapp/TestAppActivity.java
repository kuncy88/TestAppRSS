package kuncystem.hu.testapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import kuncystem.hu.testapp.adapters.ViewPagerAdapter;
import kuncystem.hu.testapp.fragments.FragmentApiTest;
import kuncystem.hu.testapp.fragments.FragmentJsonTest;

public class TestAppActivity extends AppCompatActivity {
    private static final int NOTIF_ID = 2222;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            createNotification(level);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_app);

        //check, the loading of view elements are successful or not
        boolean error = true;

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        if(viewPager != null) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

            //add fragments to the adapter
            adapter.addFragment(new FragmentApiTest(), getString(R.string.tab_text_apitest));
            adapter.addFragment(new FragmentJsonTest(), getString(R.string.tab_text_jsontest));
            viewPager.setAdapter(adapter);

            //set the tablelayout
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            if(tabLayout != null) {
                tabLayout.setupWithViewPager(viewPager);
                error = false;
            }
        }

        //loading is unsuccessful
        if(error){
            Toast.makeText(this, getString(R.string.toast_app_load_error), Toast.LENGTH_LONG).show();
        }

        //start battery monitoring, if it will refresh the charge level, we will refresh the notification
        final Intent batteryIntent = this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        //we will refresh the notification every one minute.
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                createNotification(batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
            }
        }, 60000, 60000);
    }

    /**
     * Create or update notification message with battery info.
     *
     * @param level battery charge level
     * */
    private void createNotification(int level){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_android_white_36dp);
        mBuilder.setContentTitle(String.valueOf(level) + " %");
        mBuilder.setContentText(getString(R.string.notif_battery));

        Intent notificationIntent = new Intent(this, TestAppActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIF_ID, mBuilder.build());
    }
}

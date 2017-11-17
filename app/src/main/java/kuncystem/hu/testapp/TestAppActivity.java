package kuncystem.hu.testapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kuncystem.hu.testapp.adapters.ViewPagerAdapter;
import kuncystem.hu.testapp.fragments.FragmentApiTest;
import kuncystem.hu.testapp.fragments.FragmentJsonTest;

public class TestAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_app);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //add fragments to the adapter
        adapter.addFragment(new FragmentApiTest(), getString(R.string.tab_text_apitest));
        adapter.addFragment(new FragmentJsonTest(), getString(R.string.tab_text_jsontest));
        viewPager.setAdapter(adapter);

        //set the tablayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}

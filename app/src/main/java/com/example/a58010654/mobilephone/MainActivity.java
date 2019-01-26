package com.example.a58010654.mobilephone;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private MobileListFragment mobileListFragment;
    private FavoriteFragment favoriteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getSupportFragmentManager());

        mobileListFragment = new MobileListFragment();
        favoriteFragment = new FavoriteFragment();

        adapter.addFragment(mobileListFragment, "MOBILE LIST");
        adapter.addFragment(favoriteFragment, "FAVORITE LIST");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url("https://scb-test-mobile.herokuapp.com/api/mobiles/")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Cache-Control", "no-cache")
                        .build();

                Response response = null;

                try{
                    response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        return response.body().string();
                    }else
                        return response;

                }catch (IOException e){
                    Log.v("Error in okHTTP:",e.toString());
                    e.printStackTrace();
                }

                return response;
            }

            @Override
            protected void onPostExecute(Object o) {

                Log.v("String",o.toString());
                mobileListFragment.setMobileCardList(o.toString());

            }

        }.execute();

    }

    public MobileListFragment getMobileListFragment() {
        return mobileListFragment;
    }

    public FavoriteFragment getFavoriteFragment() {
        return favoriteFragment;
    }

}

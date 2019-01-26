package com.example.a58010654.mobilephone;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {

    private TextView textView_mobile;
    private TextView textView_brand;
    private TextView textView_price;
    private TextView textView_rating;
    private TextView textView_description;
    private String id;
    private String name;
    private String brand;
    private String description;
    private String price;
    private String rating;
    private ViewFlipper viewFlipper_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        brand = getIntent().getStringExtra("brand");
        description = getIntent().getStringExtra("description");
        price = "Price: $"+getIntent().getStringExtra("price");
        rating = "Rating: "+getIntent().getStringExtra("rating");

        final String url = "https://scb-test-mobile.herokuapp.com/api/mobiles/"+id+"/images/";

        textView_brand = (TextView) findViewById(R.id.textView_brand);
        textView_mobile = (TextView) findViewById(R.id.textView_mobile);
        textView_price = (TextView) findViewById(R.id.textView_price);
        textView_rating = (TextView) findViewById(R.id.textView_rating);
        textView_description = (TextView) findViewById(R.id.textView_description);
        viewFlipper_image = (ViewFlipper) findViewById(R.id.viewFlipper_image);

        textView_description.setText(description);
        textView_rating.setText(rating);
        textView_mobile.setText(name);
        textView_brand.setText(brand);
        textView_price.setText(price);

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(url)
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
                    Log.e("Error in okHTTP:",e.toString());
                    e.printStackTrace();
                }

                return response;
            }

            @Override
            protected void onPostExecute(Object o) {
                List<String> urls = new ArrayList<>();
                JSONArray arr;
                try {
                    arr = new JSONArray(o.toString());

                    for (int i = 0; i < arr.length(); i++) {
                        String image_url = arr.getJSONObject(i).getString("url");
                        urls.add(image_url);
                    }

                    flipperImages(urls);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }

        }.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void flipperImages(List<String> urls) {

        for(String url: urls){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(this).load(url).into(imageView);
            viewFlipper_image.addView(imageView);
        }

        viewFlipper_image.setFlipInterval(4000);
        viewFlipper_image.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper_image.setOutAnimation(this,android.R.anim.slide_out_right);
        viewFlipper_image.startFlipping();
    }

}

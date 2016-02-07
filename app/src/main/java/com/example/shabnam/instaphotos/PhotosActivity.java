package com.example.shabnam.instaphotos;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID ="e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    private IntagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        photos =new ArrayList<>();
        aPhotos=new IntagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();
    }

    //Triggers API Request
    public void fetchPopularPhotos(){

        AsyncHttpClient client=new AsyncHttpClient();

        String url="https://api.instagram.com/v1/media/popular?client_id="+CLIENT_ID;

        client.get(url,null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                aPhotos.clear();
                JSONArray photosJSON= null;

                try {
                    photosJSON=response.getJSONArray("data");
                    for(int i=0 ; i<photosJSON.length();i++){

                        JSONObject photoJSON=photosJSON.getJSONObject(i);
                        InstagramPhoto photo= new InstagramPhoto();
                        photo.username=photoJSON.getJSONObject("user").getString("username");
                        photo.caption=photoJSON.getJSONObject("caption").getString("text");
                        photo.imageUrl=photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.userImageUrl=photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.imageHeight=photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount=photoJSON.getJSONObject("likes").getInt("count");
                        photo.longCreation=photoJSON.getLong("created_time");
                        photos.add(photo);
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
                aPhotos.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "FAILED");
            }
        });

    }
}

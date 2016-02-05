package com.example.shabnam.instaphotos;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        //android.os.Debug.waitForDebugger();
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

                JSONArray photosJSON= null;

                try {
                    photosJSON=response.getJSONArray("data");
                    for(int i=0 ; i<photosJSON.length();i++){

                        JSONObject photoJSON=photosJSON.getJSONObject(i);
                        InstagramPhoto photo= new InstagramPhoto();
                        photo.username=photoJSON.getJSONObject("user").getString("username");
                        photo.caption=photoJSON.getJSONObject("caption").getString("text");
                        photo.imageUrl=photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight=photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount=photoJSON.getJSONObject("likes").getInt("count");
                        photos.add(photo);
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
                aPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "FAILED");
            }
        });

    }
}

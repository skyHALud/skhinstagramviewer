package com.codepath.skyhalud.skhinstagramviewer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class PhotosActivity extends Activity {

	protected static final String CLIENT_ID="8a2f29f159d84f06af4dfedfb6fc9a7d";
	
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotosAdapter aPhotos;

	private SwipeRefreshLayout swipeContainer;

	private AsyncHttpClient ahc;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
            	fetchPopularPhotos();
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            } 
        });
        // Configure the refreshing colors
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright, 
                android.R.color.holo_green_light, 
                android.R.color.holo_orange_light, 
                android.R.color.holo_red_light);
        
        fetchPopularPhotos();
    }

    public void fetchTimelineAsync(int page) {
    	 swipeContainer.setRefreshing(false);
    }
    

    private void fetchPopularPhotos() {
    	// Hit: https://api.instagram.com/v1/media/popular?client_id=8a2f29f159d84f06af4dfedfb6fc9a7d
    	String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
    	
    	photos = new ArrayList<InstagramPhoto>();
    	aPhotos = new InstagramPhotosAdapter(this, photos);
    	
    	ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
    	lvPhotos.setAdapter(aPhotos);
    	
    	Toast.makeText(PhotosActivity.this.getBaseContext(), "Loading photos...", Toast.LENGTH_LONG).show();
    	
    	ahc = new AsyncHttpClient();
    	ahc.setTimeout((int) TimeUnit.SECONDS.toMillis(5));
    	ahc.get(popularUrl, new JsonHttpResponseHandler() {

			@Override public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.i(getClass().getName(), "Response with status code " + statusCode);
				
				JSONArray photosJSON = null;
				
				try {
					photosJSON = response.getJSONArray("data");
					
					Log.i(getClass().getName(), "Loaded " + photosJSON.length() + " photos");
					
					for(int i=0;i<photosJSON.length();i++) {
						JSONObject photoJSON = photosJSON.getJSONObject(i);
						JSONObject caption = photoJSON.get("caption") != JSONObject.NULL ? photoJSON.getJSONObject("caption") : null;
						JSONObject userObject = photoJSON.getJSONObject("user");
						InstagramPhoto photo = new InstagramPhoto(
								userObject.getString("username"),
								caption != null ? caption.getString("text") : null,
								photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"),
								photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"),
								photoJSON.getJSONObject("likes").getInt("count")
								);
						
						photo.profilePicture = userObject.getString("profile_picture");
						
						if(photoJSON.get("location") != JSONObject.NULL) {
							JSONObject locationObj = photoJSON.getJSONObject("location");
							photo.locationName = locationObj.has("name") ? locationObj.getString("name") : null;
							photo.locationLatitude = locationObj.has("latitude") ? locationObj.getDouble("latitude") : 0;
							photo.locationLongitude = locationObj.has("longitude") ? locationObj.getDouble("longitude") : 0;
						}
						
						photos.add(photo);
					}
					
					aPhotos.notifyDataSetChanged();
				} catch(JSONException ex) {
					Log.e(getClass().getName(), ex.getMessage(), ex);
				}
			}
    		
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, final Throwable throwable) {
				PhotosActivity.this.runOnUiThread(new Runnable(){
					public void run() {
						Log.e(getClass().getName(), throwable.getMessage(), throwable);
						Toast.makeText(PhotosActivity.this.getBaseContext(), "Could not load data. Please check your network connectivity.", Toast.LENGTH_LONG).show();
					}
				});
			}
			
			
    	});
    	
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}

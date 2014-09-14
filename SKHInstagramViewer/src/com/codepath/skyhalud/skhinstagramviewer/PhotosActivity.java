package com.codepath.skyhalud.skhinstagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class PhotosActivity extends Activity {

	protected static final String CLIENT_ID="8a2f29f159d84f06af4dfedfb6fc9a7d";
	
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotosAdapter aPhotos;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        
        fetchPopularPhotos();
    }


    private void fetchPopularPhotos() {
    	// Hit: https://api.instagram.com/v1/media/popular?client_id=8a2f29f159d84f06af4dfedfb6fc9a7d
    	String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
    	
    	photos = new ArrayList<InstagramPhoto>();
    	aPhotos = new InstagramPhotosAdapter(this, photos);
    	
    	ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
    	lvPhotos.setAdapter(aPhotos);
    	
    	AsyncHttpClient ahc = new AsyncHttpClient();
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
						InstagramPhoto photo = new InstagramPhoto(
								photoJSON.getJSONObject("user").getString("username"),
								caption != null ? caption.getString("text") : null,
								photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"),
								photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"),
								photoJSON.getJSONObject("likes").getInt("count")
								);
						
						photos.add(photo);
					}
					
					aPhotos.notifyDataSetChanged();
				} catch(JSONException ex) {
					Log.e(getClass().getName(), ex.getMessage(), ex);
				}
			}
    		
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
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

package com.codepath.skyhalud.skhinstagramviewer;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ViewImageFullscreenActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_view_image_fullscreen);

		final LinearLayout contentView = (LinearLayout) findViewById(R.id.fullscreen_content);

		InstagramPhoto photo = (InstagramPhoto) getIntent().getSerializableExtra("photo");
		
		View photoView = InstagramPhotosAdapter.createPhotoView(null, (ViewGroup) contentView.getParent(), photo, this, false);
		ImageView imgPhotoView = (ImageView) photoView.findViewById(R.id.imgPhoto);
		
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		imgPhotoView.getLayoutParams().height = displayMetrics.heightPixels; // TODO This is ugly. Reimplement better.
		contentView.addView(photoView);
		
		contentView.requestLayout();
	}

}

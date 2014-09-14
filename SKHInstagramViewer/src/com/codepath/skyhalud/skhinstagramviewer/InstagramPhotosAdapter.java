package com.codepath.skyhalud.skhinstagramviewer;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
	public InstagramPhotosAdapter(Context context, ArrayList<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InstagramPhoto photo = getItem(position);
		
		if(convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
		}
		
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
		
		tvCaption.setText(Html.fromHtml("&hearts;" + photo.likesCount + " likes<br/><br/>" + "<b>" + photo.username + "</b> - " + photo.caption));
		imgPhoto.getLayoutParams().height = photo.imageHeight;
		imgPhoto.setImageResource(0);// clear any previous image
		
		// Load, decode, resize the photo in async call
		Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
		
		return convertView;
	}
	
	
}

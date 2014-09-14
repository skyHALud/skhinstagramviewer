package com.codepath.skyhalud.skhinstagramviewer;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
	private static final String MAP_SYMBOL_PREFIX = "&raquo; ";

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
		
		tvCaption.setText(Html.fromHtml("&#x2661; " + photo.likesCount + " likes<br/><br/>" + StringUtils.trimToEmpty(photo.caption)));
		imgPhoto.getLayoutParams().height = photo.imageHeight;
		imgPhoto.setImageResource(0);// clear any previous image
		
		// Load, decode, resize the photo in async call
		Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
		
		final ImageView imgProfilePicture = (ImageView) convertView.findViewById(R.id.imgProfilePicture);
		TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
		TextView tvPhotoLocation = (TextView) convertView.findViewById(R.id.tvPhotoLocation);
		
		tvPhotoLocation.setText(buildMapLinkText(photo));
		
		Transformation transformation = new Transformation() {

	        @Override public Bitmap transform(Bitmap source) {
	            int targetWidth = 30; //imgProfilePicture.getWidth();

	            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
	            int targetHeight = (int) (targetWidth * aspectRatio);
	            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
	            if (result != source) {
	                // Same bitmap is returned if sizes are the same
	                source.recycle();
	            }
	            return result;
	        }

	        @Override public String key() {
	            return "transformation" + " desiredWidth";
	        }
	    };
		
		Picasso.with(getContext()).load(photo.profilePicture).transform(transformation).into(imgProfilePicture);
		
		tvUserName.setText(Html.fromHtml("<b>" + photo.username + "</b>"));
		
		return convertView;
	}

	private CharSequence buildMapLinkText(InstagramPhoto photo) {
		if(photo.locationName != null) {
			return Html.fromHtml(MAP_SYMBOL_PREFIX + photo.locationName);
		} else
		if(photo.locationLatitude != 0 && photo.locationLatitude != 0) {
			return Html.fromHtml(MAP_SYMBOL_PREFIX + "map"); // TODO Add link
		}
		else return "";
	}
	
}

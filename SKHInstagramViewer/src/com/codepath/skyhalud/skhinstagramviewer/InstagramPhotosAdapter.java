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
	

	private static final class ViewHolder {
		private TextView tvCaption;
		private ImageView imgPhoto;
		private ImageView imgProfilePicture;
		private TextView tvUserName;
		private TextView tvPhotoLocation;
	}
	
	public InstagramPhotosAdapter(Context context, ArrayList<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InstagramPhoto photo = getItem(position);
		
		ViewHolder vh;
		
		if(convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
			vh = new ViewHolder();
			
			vh.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
			vh.imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
			vh.imgProfilePicture = (ImageView) convertView.findViewById(R.id.imgProfilePicture);
			vh.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
			vh.tvPhotoLocation = (TextView) convertView.findViewById(R.id.tvPhotoLocation);
			
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.tvCaption.setText(Html.fromHtml("&#x2661; " + photo.likesCount + " likes<br/><br/>" + StringUtils.trimToEmpty(photo.caption)));
		vh.imgPhoto.getLayoutParams().height = photo.imageHeight;
		vh.imgPhoto.setImageResource(0);// clear any previous image
		
		// Load, decode, resize the photo in async call
		Picasso.with(getContext()).load(photo.imageUrl).into(vh.imgPhoto);
		
		vh.tvPhotoLocation.setText(buildMapLinkText(photo));
		
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
		
		Picasso.with(getContext()).load(photo.profilePicture).transform(transformation).into(vh.imgProfilePicture);
		
		vh.tvUserName.setText(Html.fromHtml("<b>" + photo.username + "</b>"));
		
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

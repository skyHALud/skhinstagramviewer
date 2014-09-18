package com.codepath.skyhalud.skhinstagramviewer;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
	private static final String MAP_SYMBOL_PREFIX = "&raquo; ";
	protected static final int REQUEST_CODE = 100;
	

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

	private View convertView;
	
	@Override
	public View getView(int position, final View recycledView, ViewGroup parent) {
		InstagramPhoto photo = getItem(position);
		
		convertView = createPhotoView(recycledView, parent, photo, getContext(), false);
		
		return convertView;
	}

	public static View createPhotoView(final View recycledView, ViewGroup parent, final InstagramPhoto photo, Context context, boolean fullscreenView) {
		ViewHolder vh;
		
		View tmpConvertView;
		
		if(recycledView == null) {
			tmpConvertView = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
			vh = new ViewHolder();
			
			vh.tvCaption = (TextView) tmpConvertView.findViewById(R.id.tvCaption);
			vh.imgPhoto = (ImageView) tmpConvertView.findViewById(R.id.imgPhoto);
			vh.imgProfilePicture = (ImageView) tmpConvertView.findViewById(R.id.imgProfilePicture);
			vh.tvUserName = (TextView) tmpConvertView.findViewById(R.id.tvUserName);
			vh.tvPhotoLocation = (TextView) tmpConvertView.findViewById(R.id.tvPhotoLocation);

			if(!fullscreenView) {
				final Activity photosActivity = (Activity) tmpConvertView.getContext();
				
				vh.imgPhoto.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.i("debug", "on CLICK>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						
						Intent i = new Intent(photosActivity, ViewImageFullscreenActivity.class);
						i.putExtra("photo", photo);
						photosActivity.startActivityForResult(i, REQUEST_CODE);
					}
				});
				
				tmpConvertView.setTag(vh);
			}
		} else {
			tmpConvertView = recycledView;
			vh = (ViewHolder) recycledView.getTag();
		}
		
		vh.tvCaption.setText(Html.fromHtml("&#x2661; " + photo.likesCount + " likes<br/><br/>" + StringUtils.trimToEmpty(photo.caption)));
		vh.imgPhoto.getLayoutParams().height = photo.imageHeight;
		vh.imgPhoto.setImageResource(0);// clear any previous image
		
		// Load, decode, resize the photo in async call
		Picasso.with(context).load(photo.imageUrl).fit().centerCrop().into(vh.imgPhoto);
		
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
		
		Picasso.with(context).load(photo.profilePicture).transform(transformation).into(vh.imgProfilePicture);
		
		vh.tvUserName.setText(Html.fromHtml("<b>" + photo.username + "</b>"));
		
		return tmpConvertView;
	}

	private static CharSequence buildMapLinkText(InstagramPhoto photo) {
		if(photo.locationName != null) {
			return Html.fromHtml(MAP_SYMBOL_PREFIX + photo.locationName);
		} else
		if(photo.locationLatitude != 0 && photo.locationLatitude != 0) {
			return Html.fromHtml(MAP_SYMBOL_PREFIX + "map"); // TODO Add link
		}
		else return "";
	}
	
}

package com.codepath.skyhalud.skhinstagramviewer;

public class InstagramPhoto {
	public String username;
	public String profilePicture;
	public String caption;
	public String imageUrl;
	public int imageHeight;
	public int likesCount;
	public double locationLatitude;
	public double locationLongitude;
	public String locationName;
	
	public InstagramPhoto(String username, String caption, String imageUrl,int imageHeight, int likesCount) {
		this.username = username;
		this.caption = caption;
		this.imageUrl = imageUrl;
		this.imageHeight = imageHeight;
		this.likesCount = likesCount;
	}
}

package com.codepath.skyhalud.skhinstagramviewer;

public class InstagramPhoto {
	public String username;
	public String caption;
	public String imageUrl;
	public int imageHeight;
	public int likesCount;
	
	public InstagramPhoto(String username, String caption, String imageUrl,int imageHeight, int likesCount) {
		this.username = username;
		this.caption = caption;
		this.imageUrl = imageUrl;
		this.imageHeight = imageHeight;
		this.likesCount = likesCount;
	}
}

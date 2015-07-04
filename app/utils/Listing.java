package utils;

import models.Listings;

public class Listing {
	public Listings listing;
	public String path;
	public Listing(Listings list,String img){
		this.listing = list;
		this.path = img;
	}
}

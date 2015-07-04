package utils;

import models.Listings;

public class Listing {
	public Listings listing;
	public String path;
	public boolean isOwner;
	public boolean isSponsored;
	public String categoryName;
	public boolean hasResponded;
	
	public Listing(Listings list, String img) {
		this.listing = list;
		this.path = img;
		this.categoryName = models.Category.findById(list.CategoryId).CategoryName;
	}

	public Listing(Listings list, String img, boolean canEdit) {
		this.listing = list;
		this.path = img;
		this.isOwner = canEdit;
		this.categoryName = models.Category.findById(list.CategoryId).CategoryName;
	}

}

package utils;

import static controllers.login.getUserId;
import static models.Messages.findAll;
import static models.Users.findById;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Listings;
import models.Messages;

public class Listing {
	public Listings listing;
	public String path;
	public boolean isOwner;
	public boolean isSponsored;
	public String categoryName;
	public boolean hasResponded;
	public List<MessageList> messages;

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
		messages = new ArrayList<MessageList>();
		if (!canEdit) {
			for (Messages msg : findAll(getUserId(), list.ListingId)) {
				MessageList item = new MessageList();
				if (msg.FromUserId == getUserId()) {
					item.usrname = "You";
				} else {
					int usrId = list.UserId;
					item.usrname = findById(usrId).FirstName;
				}
				SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
				item.date = df.format(msg.CreationDate);
				item.Message = msg.Message;
				messages.add(item);
			}
		}
	}
}

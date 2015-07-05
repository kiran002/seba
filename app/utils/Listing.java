package utils;

import static controllers.login.getUserId;
import static models.Messages.findAll;
import static models.Users.findById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public Map<String, List<MessageList>> msg_map;

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
		} else {
			// he is the owner, first need to get all the distinct users and then all the conversations
			msg_map = new HashMap<String, List<MessageList>>();
			ArrayList<MessageList> messages_n ;
			for (int usrId : models.Messages.distinctUsers(list.ListingId)) {
				messages_n = new ArrayList<MessageList>();
				for (Messages msg : findAll(usrId, list.ListingId)) {
					MessageList item = new MessageList();
					if (msg.FromUserId == getUserId()) {
						item.usrname = "You";
					} else {
						int tmp = msg.FromUserId;
						item.usrname = findById(tmp).FirstName;
					}
					SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
					item.date = df.format(msg.CreationDate);
					item.Message = msg.Message;
					messages_n.add(item);
				}
				msg_map.put(usrId+"", messages_n);
			}
		}
	}
}

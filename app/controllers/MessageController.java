package controllers;

import static controllers.login.isLoggedIn;
import static controllers.login.getUserId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import utils.Listing;
import utils.MessageList;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

import play.mvc.*;
import views.html.*;

public class MessageController extends Controller {

	@play.db.jpa.Transactional
	public Result sendMessage() {
		if (isLoggedIn()) {
			DynamicForm form = Form.form().bindFromRequest();
			Messages msg = Form
					.form(Messages.class, Messages.sendMessage.class)
					.bindFromRequest().get();
			msg.FromUserId = getUserId();
			msg.CreationDate = new Date();
			msg.save();
			List<Listing> allLists = ListingController.getNewListings();
			List<Category> categoryList = utilController.getCategories();
			if (isLoggedIn()) {
				Users user = Users.findById(getUserId());
				return ok(views.html.Home.render(true, allLists, categoryList,
						202, "message sent succesfully!", user));
			}
		}
		return ok(); // unsuccessful need to validate and other stuff
	}

	@play.db.jpa.Transactional
	public Result showMessages() {
		List<utils.MessageList> sent = new ArrayList<MessageList>();
		List<utils.MessageList> recieved = new ArrayList<MessageList>();
		if (isLoggedIn()) {
			List<models.Messages> msgs = Messages.findAll(getUserId(), true);
			Users user = Users.findById(getUserId());
			SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
			
			for (Messages msg : msgs) {
				MessageList msglistItem = new MessageList();
				msglistItem.ListingId = msg.ListingId;
				msglistItem.Listing_name = Listings.findById(msg.ListingId).Name;
				msglistItem.Message = msg.Message;
				msglistItem.usrname = ""
						+ Users.findById(msg.ToUserId).FirstName;
				msglistItem.date = df.format(msg.CreationDate);
				sent.add(msglistItem);
			}

			msgs = Messages.findAll(getUserId(), false);
			for (Messages msg : msgs) {
				MessageList msglistItem = new MessageList();
				msglistItem.ListingId = msg.ListingId;
				msglistItem.Listing_name = Listings.findById(msg.ListingId).Name;
				msglistItem.Message = msg.Message;
				msglistItem.ToUserId = msg.FromUserId;
				msglistItem.usrname = ""
						+ Users.findById(msg.FromUserId).FirstName;
				msglistItem.date = df.format(msg.CreationDate);
				recieved.add(msglistItem);
			}
			return ok(views.html.messages.render(true, sent, recieved, user));
		}
		return ok(views.html.messages.render(false, sent, recieved, null));
	}

	@play.db.jpa.Transactional
	public static List<Messages> getAllMessages() {
		List<Messages> msgs = null;
		if (isLoggedIn()) {
			msgs = Messages.findAll(getUserId(), false);
		}
		return msgs;
	}
}

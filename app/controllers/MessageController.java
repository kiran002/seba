package controllers;

import static controllers.login.isLoggedIn;
import static controllers.login.getUserId;

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
			Messages msg = Form.form(Messages.class,Messages.sendMessage.class).bindFromRequest().get();
			msg.FromUserId = getUserId();
			msg.CreationDate = new Date();
			msg.save();
			List<Listing> allLists = ListingController.getNewListings();
			List<Category> categoryList = utilController.getCategories();
			if (isLoggedIn()) {
				return ok(views.html.Home.render(true, allLists, categoryList,
						202, "message sent succesfully!"));
			} 
		}
		return ok(); // unsuccessful need to validate and other stuff
	}

	@play.db.jpa.Transactional
	public Result showMessages() {
		List<utils.MessageList> msglist = new ArrayList<MessageList>();
		if (isLoggedIn()) {
			List<models.Messages> msgs = Messages.findAll(getUserId());
			for (Messages msg : msgs) {
				MessageList msglistItem = new MessageList();
				msglistItem.ListingId = msg.ListingId;
				msglistItem.Listing_name = Listings.findById(msg.ListingId).Name;
				msglistItem.Message = msg.Message;
				if (msg.FromUserId == getUserId()) {
					msglistItem.msgtype = "Sent";
					msglistItem.respond_disabled = false;
					msglistItem.usrname = ""
							+ Users.findById(msg.ToUserId).FirstName;
				} else {
					msglistItem.respond_disabled = true;
					msglistItem.ToUserId = msg.FromUserId;
					msglistItem.msgtype = "Recieved";
					msglistItem.usrname = ""
							+ Users.findById(msg.FromUserId).FirstName;
				}
				msglist.add(msglistItem);
			}
			return ok(views.html.messages.render(true, msglist));
		}
		return ok(views.html.messages.render(false, msglist));
	}

	@play.db.jpa.Transactional
	public static List<Messages> getAllMessages() {
		List<Messages> msgs = null;
		if (isLoggedIn()) {
			msgs = Messages.findAll(getUserId());
		}
		return msgs;
	}
}

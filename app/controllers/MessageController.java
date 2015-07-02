package controllers;

import java.util.ArrayList;
import java.util.List;
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
	// send message
	// get messages

	@play.db.jpa.Transactional
	public Result sendMessage() {
		if (session("usrid") != null && session("usrid").length() > 0) {			
			DynamicForm form = Form.form().bindFromRequest();
			Messages msg = new Messages();
			msg.FromUserId = Integer.parseInt(session("usrid"));
			msg.ToUserId = Integer.parseInt(form.get("sendTo"));
			msg.ListingId = Integer.parseInt(form.get("listingId"));
			msg.Message = form.get("msg");
			msg.save();
			Map<Listings, String> allLists = ListingController.getNewListings();
			List<Category> categoryList = utilController.getCategories();
			if (session("usrid") != null && session("usrid").length() > 0) {
				return ok(views.html.Home.render(true, allLists, categoryList,
						null, null));
			} // successful have to forward
		}
		return ok(); // unsuccessful need to validate and other stuff
	}
	
	
	@play.db.jpa.Transactional
	public Result showMessages() {
		List<utils.MessageList> msglist = new ArrayList<MessageList>();
		if (session("usrid") != null && session("usrid").length() > 0) {
			List<models.Messages> msgs = Messages.findAll(Integer
					.parseInt(session("usrid")));
			
			for (Messages msg : msgs) {
				MessageList msglistItem = new MessageList();
				msglistItem.ListingId = msg.ListingId;
				msglistItem.Listing_name = Listings.findById(msg.ListingId).Name;
				msglistItem.Message = msg.Message;
				if (msg.FromUserId == Integer.parseInt(session("usrid"))) {
					msglistItem.msgtype = "Sent";
					msglistItem.respond_disabled = false;
					msglistItem.usrname = ""+ Users.findById(msg.ToUserId).FirstName; 
				} else {
					msglistItem.respond_disabled = true;
					msglistItem.ToUserId = msg.FromUserId;
					msglistItem.msgtype = "Recieved";					
					msglistItem.usrname = ""+ Users.findById(msg.FromUserId).FirstName; 
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
		if (session("usrId") != null && session("usrId").length() > 0) {			
			msgs = Messages.findAll(Integer.parseInt(session("usrId")));
		}
		return msgs;
	}
}

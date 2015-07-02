package controllers;

import java.util.ArrayList;
import java.util.List;

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
	public static List<Messages> getAllMessages() {
		List<Messages> msgs = null;
		if (session("usrId") != null && session("usrId").length() > 0) {			
			msgs = Messages.findAll(Integer.parseInt(session("usrId")));
		}
		return msgs;
	}
}

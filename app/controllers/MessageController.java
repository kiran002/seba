package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Messages;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

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
			return ok(); // successful have to forward
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

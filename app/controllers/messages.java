package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class messages extends Controller {
	
	public Result index() {
		return ok(views.html.messages.render());	
	}

}

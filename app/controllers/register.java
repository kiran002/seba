package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class register extends Controller {
	
	public Result index() {
		return ok(views.html.register.render());	
	}

}
package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class login extends Controller {
	
	public Result index() {
		return ok(views.html.login.render());	
	}

}
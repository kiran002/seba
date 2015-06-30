package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class addListing extends Controller {
	
	public Result index() {
		return ok(views.html.addListing.render());	
	}

}
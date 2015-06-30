package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Home extends Controller {
	
	public Result index() {
		return ok(views.html.Home.render());	
	}

}

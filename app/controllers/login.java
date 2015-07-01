package controllers;

import play.*;
import java.util.*;
import models.*;
import play.api.mvc.Session;
import play.mvc.*;
import views.html.*;

public class login extends Controller {

	@play.db.jpa.Transactional
	public Result index() {
		Map<Listings, String> allLists= ListingController.getNewListings();
		List<Category> categoryList= utilController.getCategories();
		if (session("usrid") != null && session("usrid").length() > 0) {
			//Logger.info(session("usrid") + " something is wrong");
			return ok(views.html.Home.render(true, allLists, categoryList, null,null));
		}
		return ok(views.html.login.render(false, null));
	}

	public Result logout() {
		try {
			if (session("usrid") != null) {
				session().clear();
				return ok(views.html.login.render(false, null));
			}
		} catch (Exception e) {
			return ok(views.html.login.render(false, null));
		}
		return ok(views.html.login.render(false, null));

	}

}
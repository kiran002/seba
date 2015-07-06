package controllers;

import play.*;

import java.util.*;

import models.*;
import play.api.mvc.Session;
import play.mvc.*;
import utils.Listing;
import views.html.*;

public class login extends Controller {

	@play.db.jpa.Transactional
	public Result index() {
		List<Listing> allLists = ListingController.getNewListings();
		List<Category> categoryList = utilController.getCategories();
		if (isLoggedIn()) {	
			Users user = Users.findById(getUserId());
			return ok(views.html.Home.render(true, allLists, categoryList,
					null, null, user));
		}
		return ok(views.html.login.render(false, null,200,"Please login to continue", null));
	}

	public Result logout() {
		try {
			if (isLoggedIn()) {
				session().clear();
				return ok(views.html.login.render(false, null,202,"Logout successful! See you soon!!", null));
			}
		} catch (Exception e) {
			return ok(views.html.login.render(false, null,201,"Please login to continue", null));
		}
		return ok(views.html.login.render(false, null,201,"Please login to continue", null));

	}
	
	public static int getUserId() {
		if (session("usrid") != null && session("usrid").length() > 0) {	
			return Integer.parseInt(session("usrid"));
		}
		return 0;
	}
	
	public static boolean isLoggedIn(){
		return getUserId() > 0;
	}

}
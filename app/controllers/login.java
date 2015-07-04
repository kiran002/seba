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
		Map<Listings, String> allLists = ListingController.getNewListings();
		List<Category> categoryList = utilController.getCategories();
		if (isLoggedIn()) {			
			return ok(views.html.Home.render(true, allLists, categoryList,
					null, null));
		}
		return ok(views.html.login.render(false, null,200,"Please login to continue"));
	}

	public Result logout() {
		try {
			if (isLoggedIn()) {
				session().clear();
				return ok(views.html.login.render(false, null,202,"Logout successful! See you soon!!"));
			}
		} catch (Exception e) {
			return ok(views.html.login.render(false, null,201,"Please login to continue"));
		}
		return ok(views.html.login.render(false, null,201,"Please login to continue"));

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
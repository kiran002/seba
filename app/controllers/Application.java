package controllers;

import static controllers.login.getUserId;
import static controllers.login.isLoggedIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Category;
import models.Listings;
import models.Users;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Listing;

import com.fasterxml.jackson.databind.node.ObjectNode;

;

public class Application extends Controller {

	@play.db.jpa.Transactional
	public Result index() {
		List<Listing> allLists = ListingController.getNewListings();
		List<Category> categoryList = utilController.getCategories();		
		if (isLoggedIn()) {
			Users user = Users.findById(getUserId());
			return ok(views.html.Home.render(true, allLists, categoryList,
					null, null, user));
		}
		return ok(views.html.Home.render(false, allLists, categoryList, null,
				null, null));
	}

	public Result activate() {
		return ok(views.html.activate.render("", 200, ""));
	}

	private static boolean isNull(String str) {
		return str == null || str.equals("");
	}

	@play.db.jpa.Transactional
	public Result searchListing() {
		String query = "SELECT l FROM Listings l where ";
		String scnd_query = "";
		DynamicForm form = Form.form().bindFromRequest();
		//if (isLoggedIn()) {
		if (!isNull(form.get("keywords"))) {
			query += " description like '%" + form.get("keywords")
					+ "%' and";
		}
		if (Integer.parseInt(form.get("categoryId")) > 0) {
			query += " categoryId = " + form.get("categoryId") + " and";
		}
		if (form.get("ltype").equals("O") || form.get("ltype").equals("R")) {
			query += " listingtype = '" + form.get("ltype") + "' and";
		}

		if (!isNull(form.get("price"))) {
			query += " Price <= " + form.get("price") + " and";
		}

		if (!isNull(form.get("pnegotiable"))) {
			query += " PriceNegotiable = True ";
		}

		if (!isNull(form.get("price"))) {
			query += " Price <=" + form.get("price") + " and";
		}
		if (!isNull(form.get("afrom"))) {
			query += "  TransactionStart >= " + form.get("afrom");
		}
		if (query.endsWith("and")) {
			query = query.substring(0, query.length() - 4);
		}

		//}
		List<Listing> allLists = ListingController.searchListings(query);
		List<Category> categoryList = utilController.getCategories();

		if (isLoggedIn()) {
			Users user = Users.findById(getUserId());
			return ok(views.html.Home.render(true, allLists, categoryList,
					null, null, user));
		}
		return ok(views.html.Home.render(false, allLists, categoryList, null,
				null, null));
	}

	@play.db.jpa.Transactional
	public Result showOffers() {
		if (isLoggedIn()) {
			List<Listing> offersLists = ListingController
					.getTopOffers(getUserId());
			Users user = Users.findById(getUserId());
			String myOffers = "MyOffers";
			return ok(views.html.offers.render(true, offersLists, 200, "",
					utilController.getCategories(), user, myOffers));
		}
		return ok(views.html.login.render(false, null,200,"Please login to continue", null));
	}

	@play.db.jpa.Transactional
	public Result showRequests() {
		if (isLoggedIn()) {
			List<utils.Listing> requestsLists = ListingController
					.getTopRequests(getUserId());
			Users user = Users.findById(getUserId());
			String myRequests = "MyRequests";
			return ok(views.html.requests.render(true, requestsLists, 200, "",
					utilController.getCategories(), user, myRequests));
		}
		return ok(views.html.login.render(false, null,200,"Please login to continue", null));
	}

	@play.db.jpa.Transactional
	public Result catchAll(String path) {
		List<Listing> allLists = ListingController.getNewListings();
		List<Category> categoryList = utilController.getCategories();
		Users user = Users.findById(getUserId());
		return ok(views.html.Home.render(false, allLists, categoryList, null,
				null, user));
	}

	public static ObjectNode defaultError(String message) {

		ObjectNode result = Json.newObject();
		Logger.info(message);
		result.put("status", "ERR");
		result.put("data", message);
		return result;
	}

	@play.db.jpa.Transactional
	public Result showProfile() {
		List<Listing> allLists = ListingController.getNewListings();
		List<Category> categoryList = utilController.getCategories();
		if (isLoggedIn()) {
			Users usr = userController.getUser(getUserId());
			return ok(views.html.profile.render(true, usr));
		}
		return ok(views.html.login.render(false, null,200,"Please login to continue", null));
	}

}

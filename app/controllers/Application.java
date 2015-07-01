package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

import models.*;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

	@play.db.jpa.Transactional
	public Result index() {
		Map<Listings, String> allLists = ListingController.getNewListings();
		List<Category> categoryList = utilController.getCategories();
		if (session("usrid") != null && session("usrid").length() > 0) {
			return ok(views.html.Home.render(true, allLists, categoryList,
					null, null));
		}
		return ok(views.html.Home.render(false, allLists, categoryList, null,
				null));
	}

	private static boolean isNull(String str) {
		return str == null || str.equals("");
	}

	@play.db.jpa.Transactional
	public Result searchListing() {
		Logger.info("search request");
		String query = "SELECT l FROM Listings l where ";
		String scnd_query = "";
		DynamicForm form = Form.form().bindFromRequest();

		if (session("usrid") != null && session("usrid").length() > 0) {
			Logger.info("HELLLOOOO");
			// Users usr = Users.findById(Integer.parseInt(session("usrId")));
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

		}
		Logger.info(query);

		Map<Listings, String> allLists = ListingController
				.searchListings(query);
		List<Category> categoryList = utilController.getCategories();

		if (session("usrid") != null && session("usrid").length() > 0) {
			return ok(views.html.Home.render(true, allLists, categoryList,
					null, null));
		}
		return ok(views.html.Home.render(false, allLists, categoryList, null,
				null));
	}

	@play.db.jpa.Transactional
	public Result showOffers() {
		Map<Listings, String> offersLists = ListingController.getTopOffers();
		return ok(views.html.offers.render(true, offersLists));
	}

	@play.db.jpa.Transactional
	public Result showRequests() {
		List<Listings> requestsLists = ListingController.getTopRequests();
		return ok(views.html.requests.render(true, requestsLists));
	}

	@play.db.jpa.Transactional
	public Result showMessages() {
		Map<Listings, String> messagesLists = ListingController
				.getNewListings();
		return ok(views.html.messages.render(true, messagesLists));
	}

	@play.db.jpa.Transactional
	public Result catchAll(String path) {
		Map<Listings, String> allLists = ListingController.getNewListings();
		List<Category> categoryList = utilController.getCategories();
		return ok(views.html.Home.render(false, allLists, categoryList, null,
				null));
	}

	public static ObjectNode defaultError(String message) {

		ObjectNode result = Json.newObject();
		Logger.info(message);
		result.put("status", "ERR");
		result.put("data", message);
		return result;
	}

}

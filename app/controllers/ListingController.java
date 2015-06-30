package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Listings;
import models.Pictures;
import models.Users;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ListingController extends Controller {

	ObjectNode result = Json.newObject();

	public Result index() {
		return ok(views.html.addListing.render());
	}

	@play.db.jpa.Transactional
	public static Result createListing() {

	}	
	
	
	@play.db.jpa.Transactional
	public Result createListing() {

		Logger.info("CreateListing request");
		result.removeAll();

		try {

			DynamicForm form = Form.form().bindFromRequest();
			// Validate Login
			Users currentUser = userController.getUser(Integer
					.parseInt(session("usrid")));

			if (currentUser != null) {

				Logger.info("Listing info " + form.toString());
				String name = (!form.get("name").equals("") ? form.get("name")
						: "");
				String description = (!form.get("description").equals("") ? form
						.get("description") : "");
				String pricePeriod = (!form.get("pricePeriod").equals("") ? form
						.get("pricePeriod") : "");
				int categoryId = (!form.get("categoryId").equals("") ? Integer
						.parseInt(form.get("categoryId")) : 0);
				char listingType = (!form.get("listingType").equals("") ? form
						.get("listingType").charAt(0) : ' ');
				double price = (!form.get("price").equals("") ? Double
						.parseDouble(form.get("price")) : 0.0);
				char transactionType = (!form.get("transactionType").equals("") ? form
						.get("transactionType").charAt(0) : ' ');
				SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");
				// If no StartDate by default CurrentDate
				Date transactionStart = (!form.get("transactionStart").equals(
						"") ? format.parse(form.get("transactionStart"))
						: new Date());
				// If no EndDate by default CurrentDate + 60 days
				Date transactionEnd = (!form.get("transactionEnd").equals("") ? format
						.parse(form.get("transactionEnd")) : addDays(
						new Date(), 60));
				// If no ExpireDate by default CurrentDate + 15 days
				Date transactionExpire = (!form.get("transactionExpire")
						.equals("") ? format.parse(form
						.get("transactionExpire")) : addDays(new Date(), 15));

				if (!name.equals("") && !description.equals("")
						&& !pricePeriod.equals("") && categoryId > 0
						&& listingType != ' ' && price > 0.0
						&& transactionType != ' ') {
					Logger.info("Create instance of Listing");
					Listings listing = new Listings(currentUser.UserId, name,
							categoryId, description, listingType, price,
							pricePeriod, transactionType);
					listing.TransactionStart = transactionStart;
					listing.TransactionEnd = transactionEnd;
					listing.ExpiryDate = transactionExpire;
					listing.save();
					result.put("listingId", listing.ListingId);
					result.put("status", "OK");
					result.put("data", "Your listing: " + name
							+ " was saved. Now you can upload images.");
					return ok(result);
				} else
					return badRequest(Application
							.defaultError("Fields are required please complete the form."));

			} else
				return badRequest(Application
						.defaultError("Please login to enjoy Stumark."));

		} catch (Exception e) {
			return badRequest("Exception: "
					+ Application.defaultError(e.getMessage()));
		}
	}

	private Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}
}

package controllers;

import static controllers.login.getUserId;
import static controllers.login.isLoggedIn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Listings;
import models.Pictures;
import models.Users;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Library;
import utils.Listing;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ListingController extends Controller {

	ObjectNode result = Json.newObject();

	@play.db.jpa.Transactional
	public Result index() {
		if (isLoggedIn()) {
			Users user = Users.findById(getUserId());
			return ok(views.html.addListing.render(true,
					utilController.getCategories(), 200, "",
					new models.Listings(), user));
		}
		return ok(views.html.addListing.render(false,
				utilController.getCategories(), 200, "", new models.Listings(),
				null));
	}

	@play.db.jpa.Transactional
	public Result update() {
		Users user = Users.findById(getUserId());
		if (isLoggedIn()) {
			DynamicForm form = Form.form().bindFromRequest();
			int usrId = getUserId();
			int listingId = Library.getInt(form.get("lid"));
			Listings listing = Listings.findById(listingId);
			if (listing.UserId == usrId) {
				return ok(views.html.addListing.render(true,
						utilController.getCategories(), 200, "", listing, user));
			}
		}
		return ok(views.html.addListing.render(false,
				utilController.getCategories(), 200, "", new models.Listings(),
				user));
	}

	@play.db.jpa.Transactional
	public static List<utils.Listing> getTopOffers(int userId) {
		List<utils.Listing> list = new ArrayList<Listing>();
		for (Listings listing : models.Listings.findAllUserListings("O",
				userId, 0)) {
			Pictures picture = Pictures.findByListingId(listing.ListingId);
			Listing item;
			if (picture != null) {
				item = new Listing(listing,
						Pictures.findByListingId(listing.ListingId).path,
						listing.UserId == getUserId());

			} else {
				item = new Listing(listing, "", listing.UserId == getUserId());
			}
			list.add(item);
		}
		return list;
	}

	@play.db.jpa.Transactional
	public Result showAllOffers() {
		List<utils.Listing> list = new ArrayList<Listing>();
		Users user = Users.findById(getUserId());
		DynamicForm form = Form.form().bindFromRequest();
		int index = Library.getInt(form.get("index"));
		for (Listings listing : models.Listings.findAll("O", index)) {
			Listing item;
			Pictures picture = Pictures.findByListingId(listing.ListingId);
			if (picture != null) {
				item = new Listing(listing,
						Pictures.findByListingId(listing.ListingId).path,
						getUserId() == listing.UserId);
			} else {
				item = new Listing(listing, "", getUserId() == listing.UserId);
			}
			list.add(item);
		}
		return ok(views.html.offers.render(true, list, 200, "",
				utilController.getCategories(), user, null));
	}

	@play.db.jpa.Transactional
	public Result showAllRequests() {
		List<utils.Listing> list = new ArrayList<Listing>();
		Users user = Users.findById(getUserId());
		DynamicForm form = Form.form().bindFromRequest();
		int index = Library.getInt(form.get("index"));
		for (Listings listing : models.Listings.findAll("R", index)) {
			Listing item = new Listing(listing, "",
					listing.UserId == getUserId());
			list.add(item);
		}
		return ok(views.html.requests.render(true, list, 200, "",
				utilController.getCategories(), user, null));
	}

	@play.db.jpa.Transactional
	public Result show_listing() {

		DynamicForm form = Form.form().bindFromRequest();
		int listing_id = utils.Library.getInt(form.get("lid"));
		Listings listing = Listings.findById(listing_id);
		String path = "";
		Pictures pic = Pictures.findByListingId(listing_id);
		if (pic != null) {
			path = pic.path;
		}
		Listing list;
		if (isLoggedIn()) {
			list = new Listing(listing, path, listing.UserId == getUserId());
			Users user = Users.findById(getUserId());
			return ok(views.html.showlisting.render(true, list, user));
		} else {
			list = new Listing(listing, path, false);
		}
		return ok(views.html.showlisting.render(false, list, null));

	}

	@play.db.jpa.Transactional
	public Result updateListing() {
		if (isLoggedIn()) {
			List<Listing> list = new ArrayList<Listing>();
			DynamicForm form = Form.form().bindFromRequest();
			int usrId = getUserId();
			int listingId = Library.getInt(form.get("ListingId"));
			Listings listing = Listings.findById(listingId);
			if (listing.UserId == usrId) {
				// the listing was posted by this user and can update whatever
				// he wants to, except for
				// Listing id and listing type
				listing.Name = form.get("Name");
				listing.Description = form.get("Description");
				listing.CategoryId = Integer.parseInt(form.get("CategoryId"));
				listing.TransactionType = form.get("TransactionType").charAt(0);
				listing.Price = Double.parseDouble(form.get("Price"));
				listing.PricePeriod = form.get("PricePeriod");
				if (form.get("PriceNegotiable") != null) {
					listing.PriceNegotiable = true;
				}
				listing.save();
				list = ListingController.getTopRequests(getUserId());
				Users user = Users.findById(usrId);
				String myOffers = "MyOffers";
				String myRequests = "MyRequests";
				if (listing.ListingType == 'O') {
					list = ListingController.getTopOffers((getUserId()));
					return ok(views.html.offers.render(true, list, 202,
							"Update successful!", utilController.getCategories(),
							user, myOffers));
				} else {
					list = ListingController.getTopRequests((getUserId()));
					return ok(views.html.requests.render(true, list, 202,
							"Update successful!", utilController.getCategories(),
							user, myRequests));
				}
			}
		}
		return ok(views.html.login.render(false, null, 200,
				"Please login to continue", null));
	}

	@play.db.jpa.Transactional
	public static List<utils.Listing> getTopRequests(int userId) {
		List<utils.Listing> list = new ArrayList<Listing>();
		for (Listings listing : Listings.findAllUserListings("R", userId, 0)) {
			Listing item = new Listing(listing, "",
					listing.UserId == getUserId());
			list.add(item);
		}
		return list;
	}

	@play.db.jpa.Transactional
	public static List<Listing> getNewListings() {
		List<Listing> pairs = new ArrayList<Listing>();
		for (Listings listing : models.Listings.findAll()) {
			Logger.info(listing.Name);
			if (listing.ListingType == 'R') {
				Listing item = new Listing(listing, "",
						listing.UserId == getUserId());
				pairs.add(item);
			} else {
				Listing item = new Listing(listing,
						Pictures.findByListingId(listing.ListingId).path,
						listing.UserId == getUserId());
				pairs.add(item);
			}
		}
		return pairs;
	}

	@play.db.jpa.Transactional
	public Result deleteListing() {
		List<Listing> list = new ArrayList<Listing>();
		if (isLoggedIn()) {
			DynamicForm form = Form.form().bindFromRequest();
			int usrId = getUserId();
			int listingId = Library.getInt(form.get("lid"));
			Listings listing = Listings.findById(listingId);
			Users user = Users.findById(usrId);
			char type = listing.ListingType;
			String myOffers = "MyOffers";
			String myRequests = "MyRequests";
			if (listing.UserId == usrId) {
				// the listing was posted by this user and has permission to
				// delete it
				Pictures pic = Pictures.findByListingId(listingId);
				if (pic != null) {
					pic.delete();
				}
				listing.delete();
				list = ListingController.getTopRequests(getUserId());
				if (type == 'O') {
					list = ListingController.getTopOffers((getUserId()));
					return ok(views.html.offers.render(true, list, 202,
							"Delete successful!", utilController.getCategories(),
							user, myOffers));
				} else {
					list = ListingController.getTopRequests((getUserId()));
					return ok(views.html.requests.render(true, list, 202,
							"Delete successful!", utilController.getCategories(),
							user, myRequests));
				}
			} else {
				if (type == 'O') {
					return ok(views.html.offers.render(true, list, 201,
							"This listing does not belong to you",
							utilController.getCategories(), user, null));
				} else {
					return ok(views.html.requests.render(true, list, 201,
							"This listing does not belong to you",
							utilController.getCategories(), user, null));
				}
			}
		}
		return ok(views.html.login.render(false, null, 200,
				"Please login to continue", null));
	}

	@play.db.jpa.Transactional
	public Listings getListing() {
		DynamicForm form = Form.form().bindFromRequest();
		int id = Integer.parseInt(form.get("lid"));
		return Listings.findById(id);
	}

	@play.db.jpa.Transactional
	public Result createListing() {

		try {
			Listings listing = Form
					.form(Listings.class, Listings.createListing.class)
					.bindFromRequest().get();
			play.mvc.Http.MultipartFormData body = request().body()
					.asMultipartFormData();
			play.mvc.Http.MultipartFormData.FilePart picture = body
					.getFile("picture");
			DynamicForm form = Form.form().bindFromRequest();
			Users currentUser = userController.getUser(getUserId());
			if (currentUser != null) {

				listing.CreationDate = new Date();
				listing.UserId = currentUser.UserId;
				Users user = Users.findById(currentUser.UserId);
				listing.save();
				String myOffers = "MyOffers";
				String myRequests = "MyRequests";
				if (listing.ListingType == 'O' && picture != null
						&& picture.getFile().length() > 0) {
					this.uploadImage(picture, listing.ListingId);
					List<utils.Listing> offersLists = ListingController
							.getTopOffers(getUserId());					
					return ok(views.html.offers.render(true, offersLists, 202,
							"Listing successfully created",
							utilController.getCategories(), user, myOffers));
				}
				List<utils.Listing> requestsLists = ListingController
						.getTopRequests(getUserId());
				return ok(views.html.requests.render(true, requestsLists, 202,
						"Listing successfully created",
						utilController.getCategories(), user, myRequests));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Users user = Users.findById(getUserId());
			return ok(views.html.addListing.render(true,
					utilController.getCategories(), 201,
					"Please enter all the required fields", new Listings(),
					user));
		}
		return ok();
	}

	@play.db.jpa.Transactional
	public Result uploadImage(play.mvc.Http.MultipartFormData.FilePart picture,
			int ListingId) throws IOException {

		Logger.info("UploadImage request");
		try {
			DynamicForm form = Form.form().bindFromRequest();
			if (picture != null) {
				Logger.info("Picture is not null");
				String contentType = picture.getContentType();
				java.io.File fileUpload = picture.getFile();
				String fileUploadExtension = contentType.split("/")[1];
				if (fileUploadExtension.equals("jpg")
						|| fileUploadExtension.equals("png")) {
					String fileName = "L"
							+ ListingId
							+ new SimpleDateFormat("yyyyMMddkkmmssS")
									.format(new Date()) + "."
							+ fileUploadExtension;
					File file = new File("public/uploaded/" + fileName);
					Pictures pictureToSave = new models.Pictures(ListingId,
							fileName);
					pictureToSave.save();
					Files.copy(fileUpload.toPath(), file.toPath());
					return ok(""); // TODO: fix this
				} else
					return badRequest(Application
							.defaultError("File is not an available image format. Use jpg or png files."));

			} else
				return badRequest(Application
						.defaultError("Please select a picture to upload"));

		} catch (Exception e) {
			return badRequest(Application.defaultError("Exception: "
					+ e.getMessage()));
		}
	}

	@play.db.jpa.Transactional
	public static List<Listing> searchListings(String query) {
		List<Listing> pairs = new ArrayList<Listing>();
		for (Listings listing : Listings.search(query)) {
			if (listing.ListingType == 'R') {
				Listing item = new Listing(listing, "");
				pairs.add(item);
			} else {
				Listing item = new Listing(listing,
						Pictures.findByListingId(listing.ListingId).path);
				pairs.add(item);
			}
		}
		return pairs;
	}
}

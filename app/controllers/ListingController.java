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
			return ok(views.html.addListing.render(true,
					utilController.getCategories(), 200, "",
					new models.Listings()));
		}
		return ok(views.html.addListing.render(false,
				utilController.getCategories(), 200, "", new models.Listings()));
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
						Pictures.findByListingId(listing.ListingId).path);

			} else {
				item = new Listing(listing, "");
			}
			list.add(item);
		}
		return list;
	}

	@play.db.jpa.Transactional
	public Result showAllOffers() {
		List<utils.Listing> list = new ArrayList<Listing>();
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
				utilController.getCategories()));
	}

	@play.db.jpa.Transactional
	public Result showAllRequests() {
		List<utils.Listing> list = new ArrayList<Listing>();
		DynamicForm form = Form.form().bindFromRequest();
		int index = Library.getInt(form.get("index"));
		for (Listings listing : models.Listings.findAll("R", index)) {
			Listing item = new Listing(listing, "");
			list.add(item);
		}
		return ok(views.html.offers.render(true, list, 200, "",
				utilController.getCategories()));
	}

	@play.db.jpa.Transactional
	public Result show_listing() {
		if (isLoggedIn()) {
			DynamicForm form = Form.form().bindFromRequest();
			int listing_id = utils.Library.getInt(form.get("lid"));
			Listings listing = Listings.findById(listing_id);
			String path ="";
			Pictures pic = Pictures.findByListingId(listing_id);
			if(pic!=null) {
				path = pic.path;
			}	
			Listing list = new Listing(listing,path,listing.UserId==getUserId());
			return ok(views.html.showlisting.render(true, list));
		}
		return ok(views.html.showlisting.render(false, null));
	}

	@play.db.jpa.Transactional
	public Result updateListing() {
		if (isLoggedIn()) {
			DynamicForm form = Form.form().bindFromRequest();
			int usrId = getUserId();
			int listingId = Library.getInt(form.get("lid"));
			Listings listing = Listings.findById(listingId);
			if (listing.UserId == usrId) {
				// the listing was posted by this user and can update whatever
				// he wants to, except for
				// Listing id and listing type
				SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");
				listing.Name = form.get("name");
				listing.Description = form.get("description");
				listing.CategoryId = Integer.parseInt(form.get("cid"));
				listing.TransactionType = form.get("ttype").charAt(0);
				listing.Price = Double.parseDouble(form.get("price"));
				listing.PricePeriod = form.get("pperiod");
				if (form.get("pnegotiable") != null) {
					listing.PriceNegotiable = true;
				}
				try {
					if (form.get("afrom") != null) {
						listing.TransactionStart = format.parse(form
								.get("afrom"));
					}
					if (form.get("ato") != null) {
						listing.TransactionEnd = format
								.parse(form.get("afrom"));
					}
					if (form.get("edate") != null) {
						listing.ExpiryDate = format.parse(form.get("afrom"));
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listing.save();
				return ok();
			}
		}
		return ok();
	}

	@play.db.jpa.Transactional
	public static List<utils.Listing> getTopRequests(int userId) {
		List<utils.Listing> list = new ArrayList<Listing>();
		for (Listings listing : Listings.findAllUserListings("R", userId, 0)) {
			Listing item = new Listing(listing, "");
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

	@play.db.jpa.Transactional
	public Result deleteListing() {
		List<Listing> list = new ArrayList<Listing>();
		if (isLoggedIn()) {
			DynamicForm form = Form.form().bindFromRequest();
			int usrId = getUserId();
			int listingId = Library.getInt(form.get("lid"));
			Listings listing = Listings.findById(listingId);
			
			char type = listing.ListingType;
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
				} 
				
				return ok(views.html.offers.render(true, list, 202,
						"Delete successful!",
						utilController.getCategories()));
			} else {
				return ok(views.html.offers.render(true, list, 201,
						"This listing does not belong to you",
						utilController.getCategories()));
			}
		}
		return ok(views.html.offers.render(false, list, 201,
				"Please login to continue", utilController.getCategories()));
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
				listing.save();
				if (listing.ListingType == 'O' && picture != null
						&& picture.getFile().length() > 0) {
					this.uploadImage(picture, listing.ListingId);
					List<utils.Listing> offersLists = ListingController
							.getTopOffers(getUserId());
					return ok(views.html.offers.render(true, offersLists, 202,
							"Listing successfully created",
							utilController.getCategories()));
				}
				List<utils.Listing> requestsLists = ListingController
						.getTopRequests(getUserId());
				return ok(views.html.offers.render(true, requestsLists, 202,
						"Listing successfully created",
						utilController.getCategories()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return ok(views.html.addListing.render(true,
					utilController.getCategories(), 201,
					"Please enter all the required fields", new Listings()));
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

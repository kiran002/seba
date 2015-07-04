package controllers;

import static controllers.login.isLoggedIn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			return ok(views.html.addListing.render(true));
		}
		return ok(views.html.addListing.render(false));
	}

	@play.db.jpa.Transactional
	public static List<utils.Listing> getTopOffers(int userId) {
		List<utils.Listing> list = new ArrayList<Listing>();
		for (Listings listing : models.Listings.findAllUserListings("O",
				userId, 0)) {
			Listing item = new Listing(listing,
					Pictures.findByListingId(listing.ListingId).path);
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
			Listing item = new Listing(listing,
					Pictures.findByListingId(listing.ListingId).path);
			list.add(item);
		}
		return ok(views.html.offers.render(true, list, 200, "",utilController.getCategories()));
	}
	
	@play.db.jpa.Transactional
	public Result showAllRequests() {
		List<utils.Listing> list = new ArrayList<Listing>();
		DynamicForm form = Form.form().bindFromRequest();
		int index = Library.getInt(form.get("index"));
		for (Listings listing : models.Listings.findAll("R", index)) {
			Listing item = new Listing(listing,"");
			list.add(item);
		}
		return ok(views.html.offers.render(true, list, 200, "",utilController.getCategories()));
	}

	@play.db.jpa.Transactional
	public Result show_listing() {
		if (isLoggedIn()) {
			DynamicForm form = Form.form().bindFromRequest();
			int listing_id = utils.Library.getInt(form.get("lid"));
			Listings listing = Listings.findById(listing_id);
			return ok(views.html.showlisting.render(true, listing));
		}
		return ok(views.html.showlisting.render(false, null));
	}

	@play.db.jpa.Transactional
	public Result updateListing() {
		if (isLoggedIn()) {
			DynamicForm form = Form.form().bindFromRequest();
			int usrId = Integer.parseInt(session("usrid"));
			int listingId = Integer.parseInt(form.get("lid"));
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
	public static List<Listings> getTopRequests(int userId) {
		return Listings.findAllUserListings("R", userId, 0);
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
	public Listings getListing() {
		DynamicForm form = Form.form().bindFromRequest();
		int id = Integer.parseInt(form.get("lid"));
		return Listings.findById(id);
	}

	@play.db.jpa.Transactional
	public Result createListing() {
		try {
			play.mvc.Http.MultipartFormData body = request().body()
					.asMultipartFormData();
			play.mvc.Http.MultipartFormData.FilePart picture = body
					.getFile("picture");
			DynamicForm form = Form.form().bindFromRequest();
			// Validate Login
			Users currentUser = userController.getUser(Integer
					.parseInt(session("usrid")));
			if (currentUser != null) {
				String name = form.get("name");
				String description = form.get("description");
				String pricePeriod = form.get("pricePeriod");
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

				if (!name.equals("") && !description.equals("")
						&& !pricePeriod.equals("") && categoryId > 0
						&& listingType != ' ' && price > 0.0
						&& transactionType != ' ') {
					Logger.info("Create instance of Listing");
					Listings listing = new Listings(currentUser.UserId, name,
							categoryId, description, listingType, price,
							pricePeriod, transactionType);
					if (form.get("transactionStart") != null) {
						Date transactionStart = (!form.get("transactionStart")
								.equals("") ? format.parse(form
								.get("transactionStart")) : new Date());
						// If no EndDate by default CurrentDate + 60 days
						Date transactionEnd = (!form.get("transactionEnd")
								.equals("") ? format.parse(form
								.get("transactionEnd")) : addDays(new Date(),
								60));
						// If no ExpireDate by default CurrentDate + 15 days
						Date transactionExpire = (!form
								.get("transactionExpire").equals("") ? format
								.parse(form.get("transactionExpire"))
								: addDays(new Date(), 15));
						listing.TransactionStart = transactionStart;
						listing.TransactionEnd = transactionEnd;
						listing.ExpiryDate = transactionExpire;

					}
					listing.save();
					if (listing.ListingType == 'O' && picture != null
							&& picture.getFile().length() > 0) {
						this.uploadImage(picture, listing.ListingId);
						List<Listing> offersLists = ListingController
								.getTopOffers(Integer
										.parseInt(session("usrid")));
						return ok(views.html.offers.render(true, offersLists,
								202, "Listing successfully created",
								utilController.getCategories()));
					}
					List<Listings> requestsLists = ListingController
							.getTopRequests(Integer.parseInt(session("usrid")));
					return ok(views.html.requests.render(true, requestsLists,
							202, "Listing successfully created"));

				} else {
					return badRequest(Application
							.defaultError("Fields are required please complete the form."));
				}

			} else {
				return badRequest(Application
						.defaultError("Please login to enjoy Stumark."));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return badRequest("Exception: "
					+ Application.defaultError(e.getMessage()));
		}
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

	private Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
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

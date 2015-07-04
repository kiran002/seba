package controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import models.Category;
import models.Listings;
import models.Users;

import org.hibernate.exception.ConstraintViolationException;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Listing;
import static utils.Library.getActivationCode;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class userController extends Controller {

	public Result index() {
		return ok(views.html.register.render(200, ""));
	}

	ObjectNode result = Json.newObject();

	@play.db.jpa.Transactional
	public Result createUser() {
		DynamicForm form = Form.form().bindFromRequest();
		Users user = Form.form(Users.class, Users.createUser.class)
				.bindFromRequest().get();
		String pass2 = form.get("password2");
		Logger.info(pass2 + " in: Register User request");
		try {
			if (user.Email.contains("tum.de")) {
				if (user.password.equals(pass2)) {
					user.CreationDate = new Date();
					user.isActivated = false;
					user.ActivationCode = getActivationCode();
					user.save();
					return ok(views.html.activate.render(user.Email, 202,
							"Your account has been created, "
									+ "please activate your account "
									+ user.ActivationCode));
				} else {
					return ok(views.html.register.render(201,
							"Please make sure that the passwords match"));
				}
			} else {
				return ok(views.html.register
						.render(201,
								"Sorry! You need to have a valid university mail address to register"));
			}
		} catch (ConstraintViolationException ex) {
			return ok(views.html.register.render(201,
					"Please enter all required fields"));
		}
	}

	@play.db.jpa.Transactional
	public Result activate() {
		DynamicForm form = Form.form().bindFromRequest();
		String email = form.get("email");
		String activationCode = form.get("acode");
		try {
			models.Users user = models.Users.findUser(email);
			if (!(user == null)) {
				if (user.ActivationCode.equals(activationCode)) {
					user.isActivated = true;
					user.update();
					return ok(views.html.login
							.render(false, user, 202,
									"Activation successfull please login to start using stumark"));
				} else
					return ok(views.html.activate.render(user.Email, 201,
							"Invalid activation code. Please check your entry and try again."
									+ user.ActivationCode));
			} else
				return ok(views.html.activate.render(user.Email, 201,
						"Sorry, we cannot find the user."));

		} catch (Exception e) {
			return ok(views.html.activate.render(email, 201,
					"Sorry, we cannot find the user."));
		}

	}

	@play.db.jpa.Transactional
	public Result login() {
		Logger.info(this.toString() + " in: Login User request");
		try {
			DynamicForm form = Form.form().bindFromRequest();
			String email = form.get("email");
			String password = form.get("password");
			models.Users user = models.Users.findUser(email);
			if (!user.equals(null)) {
				if (user.password.equals(password)) {
					session("userId", "" + user.UserId);
					user.AuthCode = generateAuthCode();
					if (user.isActivated == true) {
						session("usrid", "" + user.UserId);
						List<Listing> allLists = ListingController
								.getNewListings();
						List<Category> categoryList = utilController
								.getCategories();
						return ok(views.html.Home.render(true, allLists,
								categoryList, null, null));
					} else {
						return ok(views.html.activate.render(user.Email, 202,
								"please activate your account using "
										+ user.ActivationCode));
					}

				} else {
					return ok(views.html.login
							.render(false, null, 201,
									"Invalid user please enter a valid user name and password"));
				}
			} else {
				return ok(views.html.login
						.render(false, null, 201,
								"Invalid user please enter a valid user name and password"));
			}
		} catch (Exception e) {
			return ok(views.html.login.render(false, null, 201,
					"Invalid user please enter a valid user name and password"));
		}

	}

	private String generateAuthCode() {
		Random r = new Random();
		String pass = "";
		for (int i = 0; i < 10; i++) {
			pass = pass + r.nextInt(10);
		}
		return pass;
	}

	@play.db.jpa.Transactional
	public static Users getUser(int id) {
		return Users.findById(id);
	}

	@play.db.jpa.Transactional
	public Result updateUser() {
		List<Listing> allLists = ListingController.getNewListings();
		List<Category> categoryList = utilController.getCategories();
		
		DynamicForm form = Form.form().bindFromRequest();
		String firstname = form.get("fname");
		Logger.info(this.toString() + firstname);
		String lastname = form.get("lname");
		String pass = form.get("password");
		String pass2 = form.get("password2");
		if (pass == null || pass.equals(pass2)) {
			if (session("usrid") != null && session("usrid").length() > 0) {
				Users usr = Users.findById(Integer.parseInt(session("usrid")));
				usr.FirstName = firstname;
				usr.LastName = lastname;
				if (pass != null && pass.length() >= 1) {
					usr.password = pass;
				}
				usr.update();
				return ok(views.html.Home.render(true, allLists, categoryList,
						null, null));
			}
		}
		// something has gone wrong make the validations
		return ok(views.html.Home.render(true, allLists, categoryList, null,
				null));

	}

}
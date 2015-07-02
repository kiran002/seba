package controllers;

import static play.libs.Json.toJson;

import java.util.Random;

import models.Users;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;
import models.*;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class userController extends Controller {

	public Result index() {
		return ok(views.html.register.render());
	}

	ObjectNode result = Json.newObject();

	@play.db.jpa.Transactional
	public Result createUser() {

		Logger.info(this.toString() + " in: Register User request");
		result.removeAll();

		try {
			DynamicForm form = Form.form().bindFromRequest();
			String firstname = form.get("fname");
			Logger.info(this.toString() + firstname);
			String lastname = form.get("lname");
			String pass = form.get("password");
			String pass2 = form.get("password2");
			String email = form.get("email");

			if (!firstname.equals("") && !lastname.equals("")
					&& !email.equals("") && !pass.equals("")
					&& !pass2.equals("")) {
				Logger.info(this.toString() + " in: Required fields Ok");
				if (email.contains("tum.de")) {
					Logger.info(this.toString() + " in: TUM Email account");
					if (pass.equals(pass2)) {
						Logger.info(this.toString() + " in: Passwords Match");
						Users user = new Users(firstname, lastname, email, pass);
						user.save(); // TODO @Kiran AuthCode can be null either
										// change in db or models
						Logger.info(this.toString() + " in: User saved");

						return ok(views.html.login.render(true, user));
						// return ok(result);
					} else
						return badRequest(Application
								.defaultError("Password does not match with confirmation password"));
				} else
					return badRequest(Application
							.defaultError("You must provide your TUM email. This is a Student Markt."));
			} else
				return badRequest(Application
						.defaultError("First name, last name, email and password are required fields"));

		} catch (Exception e) {
			Logger.info(this.toString() + " in: Caught exception");
			Logger.error(e.getMessage());
			return badRequest("Exception: "
					+ Application.defaultError(e.getMessage()));
		}
	}

	@play.db.jpa.Transactional
	public Result activate() {
		Logger.info(this.toString() + " in: Activate User request");
		try {

			DynamicForm form = Form.form().bindFromRequest();
			String email = form.get("email");
			String activationCode = form.get("activationCode");
			models.Users user = models.Users.findUser(email);
			if (!user.equals(null)) {
				if (user.ActivationCode.equals(activationCode)) {
					user.isActivated = true;
					user.update();
					Logger.info(this.toString()
							+ " in: User activated and updated");
					return ok(); // return to activated page
				} else
					return badRequest(Application
							.defaultError("Invalid activation code. Please check your entry and try again."));
			} else
				return badRequest(Application
						.defaultError("Sorry, we cannot find the user : "
								+ email
								+ ". Please check spelling and try again."));

		} catch (Exception e) {
			Logger.info(this.toString() + " in: Caught exception");
			Logger.error(e.getMessage());
			return badRequest("Exception: "
					+ Application.defaultError(e.getMessage()));
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
			Logger.info(this.toString() + " in: User found");
			if (!user.equals(null)) {
				if (user.password.equals(password)) {
					session("userId", "" + user.UserId); // TODO @All check and
															// try through the
															// session
					user.AuthCode = generateAuthCode();
					if (user.isActivated = true) { // Note: When user has
													// already activate his
													// account
						session("usrid", "" + user.UserId);
						Map<Listings, String> allLists = ListingController
								.getNewListings();
						List<Category> categoryList = utilController
								.getCategories();
						return ok(views.html.Home.render(true, allLists,
								categoryList, null, null));
					} else {
						return ok(views.html.login.render(false, user));
					}

				} else
					return badRequest(Application
							.defaultError("Login information is not valid."));

			} else
				return badRequest(Application
						.defaultError("Login information is not valid."));

		} catch (Exception e) {
			Logger.info(this.toString() + " in: Caught exception");
			Logger.error(e.getMessage());
			return badRequest("Exception: "
					+ Application.defaultError(e.getMessage()));
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
		Map<Listings, String> allLists = ListingController.getNewListings();
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
				if(pass != null && pass.length() >= 1) {
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

	@play.db.jpa.Transactional
	public Result deleteUser() {
		if (session("usrId") != null && session("usrId").length() > 0) {
			Users usr = Users.findById(Integer.parseInt(session("usrId")));
			DynamicForm form = Form.form().bindFromRequest();
			String pass = form.get("password");
			if (usr.password.equals(pass)) {
				usr.delete();
				return ok(); // was succesful return to home page
			}
		}
		return ok(); // something has gone wrong TODO: add validations
	}

}
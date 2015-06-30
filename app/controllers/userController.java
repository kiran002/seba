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
						
						return ok(views.html.login.render(user, 200));
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
		result.removeAll();

		try {

			DynamicForm form = Form.form().bindFromRequest();
			String email = form.get("email");
			String activationCode = form.get("activationCode");
			models.Users user = models.Users.findUser(email);

			if (!user.equals(null)) {

				Logger.info(this.toString() + " in: User found");
				if (user.ActivationCode.equals(activationCode)) {
					user.isActivated = true;
					user.update();
					Logger.info(this.toString()
							+ " in: User activated and updated");
					result.put("user", user.Email);
					result.put("status", "OK");
					result.put(
							"data",
							"Welcome "
									+ user.Email
									+ ".You are now part of Stumark. You can now post offers or requests.");
					return ok(result);
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
		result.removeAll();

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
						return ok(views.html.login.render(user, 200));
					} else {
						result.put("user", toJson(user)); // TODO @Efe analize
															// how to manage
															// this state when
															// account is not
															// yet activated
						result.put("status", "OK");
						result.put(
								"data",
								"Welcome to Stumark. Please activate your account so you can use our services. ");
						return ok(result);
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

	
}
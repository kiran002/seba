package controllers;

import play.*;
import play.api.mvc.Session;
import play.mvc.*;
import views.html.*;

public class login extends Controller {

	@play.db.jpa.Transactional
	public Result index() {
		if (session("usrid") != null) {
			return ok(views.html.login.render(
					userController.getUser(Integer.parseInt(session("usrid"))),
					200));
		}
		return ok(views.html.login.render(null, 201));
	}

	public Result logout() {
		try {
			if (session("usrid") != null) {
				
				return ok(views.html.login.render(null, 201));
			}
		} catch (Exception e) {
			return ok(views.html.login.render(null, 201));
		}
		return ok(views.html.login.render(null, 201));

	}

}
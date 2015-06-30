package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;
import models.*;

import play.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

	@play.db.jpa.Transactional
    public Result index() {
		Map<Listings, String> allLists= ListingController.getNewListings();
    	if(session("usrid")!=null && session("usrid").length() > 0) {
    		return ok(views.html.Home.render(true, allLists));
    	}
        return ok(views.html.Home.render(false, allLists));
    }
    
	@play.db.jpa.Transactional
    public Result catchAll(String path) {
		Map<Listings, String> allLists= ListingController.getNewListings();
    	return ok(views.html.Home.render(false, allLists));
    }
    
 
    public static ObjectNode defaultError(String message){

        ObjectNode result = Json.newObject();
        Logger.info(message);
        result.put("status", "ERR");
        result.put("data", message);
        return result;
    }

}

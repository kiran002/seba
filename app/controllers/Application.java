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
		List<Category> categoryList= utilController.getCategories();
    	if(session("usrid")!=null && session("usrid").length() > 0) {
    		return ok(views.html.Home.render(true, allLists, categoryList, null, null));
    	}
        return ok(views.html.Home.render(false, allLists, categoryList, null, null));
    }
	
	@play.db.jpa.Transactional
    public Result showOffers() {
		Map<Listings, String> offersLists= ListingController.getTopOffers();
		return ok(views.html.offers.render(true, offersLists));
    }
	
	@play.db.jpa.Transactional
    public Result showRequests() {
		Map<Listings, String> requestsLists= ListingController.getTopRequests();
		return ok(views.html.requests.render(true, requestsLists));
    }
	
	@play.db.jpa.Transactional
    public Result showMessages() {
		Map<Listings, String> messagesLists= ListingController.getNewListings();
		return ok(views.html.messages.render(true, messagesLists));
    }
	
    
	@play.db.jpa.Transactional
    public Result catchAll(String path) {
		Map<Listings, String> allLists= ListingController.getNewListings();
		List<Category> categoryList= utilController.getCategories();
    	return ok(views.html.Home.render(false, allLists, categoryList, null, null));
    }
    
 
    public static ObjectNode defaultError(String message){

        ObjectNode result = Json.newObject();
        Logger.info(message);
        result.put("status", "ERR");
        result.put("data", message);
        return result;
    }

}

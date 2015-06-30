package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

    public Result index() {
    	if(session("usrid")!=null && session("usrid").length() > 0) {
    		return ok(views.html.Home.render(true));
    	}
        return ok(views.html.Home.render(false));
    }

 
    public static ObjectNode defaultError(String message){

        ObjectNode result = Json.newObject();
        Logger.info(message);
        result.put("status", "ERR");
        result.put("data", message);
        return result;
    }

}

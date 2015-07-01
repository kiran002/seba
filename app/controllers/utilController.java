package controllers;

import java.util.List;

import models.Category;

public class utilController {

	@play.db.jpa.Transactional
	public static List<Category> getCategories(){
		return Category.findAll();
	}
	
}

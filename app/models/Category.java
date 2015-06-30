package models;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Category {

	@Id
	@GeneratedValue
	public int CategoryId;

	@Constraints.Required
	public String CategoryName;

	public void save() {
		JPA.em().persist(this);
	}

	public Category update() {
		return JPA.em().merge(this);

	}

	public void delete() {
		JPA.em().remove(this);
	}

}

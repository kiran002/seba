package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import play.db.jpa.JPA;

@Entity
public class Pictures {

	@Id
	@GeneratedValue
	public int PictureId;

	@Constraints.Required
	public int ListingId;

	@Constraints.Required
	public String path;

	public Pictures() {

	}

	public Pictures(int ListingId, String path) {
		this.ListingId = ListingId;
		this.path = path;
	}

	public void save() {
		JPA.em().persist(this);
	}

	public Pictures update() {
		return JPA.em().merge(this);
	}

	public static Pictures findById(Integer id) {
		return JPA.em().find(Pictures.class, id);
	}

	public void delete() {
		JPA.em().remove(this);
	}

}

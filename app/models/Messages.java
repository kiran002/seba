package models;

import java.util.Date;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Messages {

	@Id
	@GeneratedValue
	public int MessageId;

	@Constraints.Required
	public int ListingId;

	@Constraints.Required
	public int FromUserId;

	@Constraints.Required
	public int ToUserId;

	@Constraints.Required
	public String Message;

	@Constraints.Required
	public Date CreationDate;

	public void save() {
		JPA.em().persist(this);
	}

	public Messages update() {
		return JPA.em().merge(this);

	}

	public void delete() {
		JPA.em().remove(this);
	}
}

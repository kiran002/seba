package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

@Entity
public class SponsoredListings {
	@Id
	@GeneratedValue
	public int SponsoredListingId;
	@Constraints.Required
	public int PaymentId;
	@Constraints.Required
	public int CategoryId;
	@Constraints.Required
	public Date ValidFrom;
	@Constraints.Required
	public Date ValidTo;

	public SponsoredListings() {

	}

	public SponsoredListings(int SponsoredListingId, int PaymentId,
			int CategoryId, Date ValidFrom, Date ValidTo) {
		this.SponsoredListingId = SponsoredListingId;
		this.PaymentId = PaymentId;
		this.CategoryId = CategoryId;
		this.ValidFrom = ValidFrom;
		this.ValidTo = ValidTo;
	}

	public void save() {
		JPA.em().persist(this);
	}

	public SponsoredListings update() {
		return JPA.em().merge(this);
	}

	public void delete() {
		JPA.em().remove(this);
	}
}

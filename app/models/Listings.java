package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

@Entity
public class Listings {

	@Id
	@GeneratedValue
	public int ListingId;

	public int SponsoredId;
	
	@Constraints.Required
	public int UserId;

	@Constraints.Required 
	public String Name; // this is the listing title

	@Constraints.Required
	public int CategoryId;

	@Constraints.Required
	public String Description;

	@Constraints.Required
	public char ListingType;

	@Constraints.Required
	public double Price;

	public boolean PriceNegotiable;

	@Constraints.Required
	public String PricePeriod;

	@Constraints.Required
	public char TransactionType;

	public Date TransactionStart;

	public Date TransactionEnd;

	@Constraints.Required
	public Date CreationDate;

	public Date ExpiryDate;

	public boolean isExpired;

	public void save() {
		JPA.em().persist(this);
	}

	public Listings update() {
		return JPA.em().merge(this);

	}

	public Listings() {
	}

	public Listings(int UserId, String name, int CategoryId,
			String Description, char ListingType, double Price,
			String PricePeriod, char TransactionType) {
		this.UserId = UserId;
		this.Name = name;
		this.CategoryId = CategoryId;
		this.Description = Description;
		this.ListingType = ListingType;
		this.Price = Price;
		this.PricePeriod = PricePeriod;
		this.TransactionType = TransactionType;
		this.CreationDate = new Date();
	}

	public static List<Listings> findAll() {
		TypedQuery<Listings> query = JPA.em().createQuery(
				"SELECT l FROM listings l", Listings.class);
		return query.getResultList();
	}

	public static List<Listings> findAll(String type) {
		TypedQuery<Listings> query = JPA.em().createQuery(
				"SELECT l FROM listings l where l.ListingType = :ListingType",
				Listings.class);
		return query.setParameter("ListingType", type).getResultList();
	}

	public static Listings findById(Integer id) {
		return JPA.em().find(Listings.class, id);
	}

	public void delete() {
		JPA.em().remove(this);
	}
}

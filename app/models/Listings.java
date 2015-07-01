package models;

import java.io.Console;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;

import com.sun.media.jfxmedia.logging.Logger;

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
				"SELECT l FROM Listings l order by CreationDate",
				Listings.class);
		query.setMaxResults(10);
		return query.getResultList();
	}

	public static List<Listings> findAll(String type) {
		TypedQuery<Listings> query = JPA
				.em()
				.createQuery(
						"SELECT l FROM Listings l where l.ListingType = :ListingType order by CreationDate",
						Listings.class);
		query.setMaxResults(10);
		return query.setParameter("ListingType", type.charAt(0))
				.getResultList();
	}

	public static List<Listings> search(String query_string) {
		try {
			TypedQuery<Listings> query = JPA.em().createQuery(query_string,
					Listings.class);
			query.setMaxResults(10);
			return query.getResultList();
		} catch (Exception ex) {			
			return findAll();
		}
	}

	public static Listings findById(Integer id) {
		return JPA.em().find(Listings.class, id);
	}

	public void delete() {
		JPA.em().remove(this);
	}
}

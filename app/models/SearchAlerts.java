package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

@Entity
public class SearchAlerts {
	@Id
	@GeneratedValue
	public int SearchAlertId;

	@Constraints.Required
	public int PaymentId;
	@Constraints.Required
	public int CategoryId;
	@Constraints.Required
	public Date ValidFrom;
	@Constraints.Required
	public Date ValidTo;

	@Constraints.Required
	public String ListingTitle;

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

	public SearchAlerts() {

	}

	public SearchAlerts(int paymentId, int categoryId, Date validFrom,
			Date validTo, String listingTitle, String description,
			char listingType, double price, boolean priceNegotiable,
			String pricePeriod, char transactionType) {

		this.PaymentId = paymentId;
		this.CategoryId = categoryId;
		this.ValidFrom = validFrom;
		this.ValidTo = validTo;
		this.ListingTitle = listingTitle;
		this.Description = description;
		this.ListingType = listingType;
		this.Price = price;
		this.PriceNegotiable = priceNegotiable;
		this.PricePeriod = pricePeriod;
		this.TransactionType = transactionType;
	}

	public void save() {
		JPA.em().persist(this);
	}

	public SearchAlerts update() {
		return JPA.em().merge(this);
	}

	public void delete() {
		JPA.em().remove(this);
	}
}

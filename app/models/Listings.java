package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

@Entity
public class Listings {	
		
	public interface createListing{};
	
	@Id
	@GeneratedValue
	public int ListingId;
	public int SponsoredId;
	@Constraints.Required()
	public int UserId;
	@Constraints.Required(groups=createListing.class)
	public String Name;
	@Constraints.Required(groups=createListing.class)
	public int CategoryId;
	@Constraints.Required(groups=createListing.class)
	public String Description;
	@Constraints.Required(groups=createListing.class)
	public char ListingType;
	@Constraints.Required(groups=createListing.class)
	public double Price;
	public boolean PriceNegotiable;
	@Constraints.Required(groups=createListing.class)
	public String PricePeriod;
	@Constraints.Required(groups=createListing.class)
	public char TransactionType;
	@Formats.DateTime(pattern="yyyy/MM/dd")
	public Date TransactionStart;
	@Formats.DateTime(pattern="yyyy/MM/dd")
	public Date TransactionEnd;
	@Constraints.Required()
	public Date CreationDate;
	@Formats.DateTime(pattern="yyyy/MM/dd")
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

	
	public static List<Listings> findAllUserListings(String type,int userId,int index) {
		TypedQuery<Listings> query = JPA
				.em()
				.createQuery(
						"SELECT l FROM Listings l where l.ListingType = :ListingType and l.UserId = :UserId order by CreationDate",
						Listings.class);
		query.setFirstResult(index);
		query.setMaxResults(10);
		return query.setParameter("ListingType", type.charAt(0)).setParameter("UserId", userId)
				.getResultList();
	}

	
	public static List<Listings> findAll(String type,int index) {
		TypedQuery<Listings> query = JPA
				.em()
				.createQuery(
						"SELECT l FROM Listings l where l.ListingType = :ListingType order by CreationDate",
						Listings.class);
		query.setFirstResult(index);
		query.setMaxResults(10);
		return query.setParameter("ListingType", type.charAt(0)).getResultList();
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

	public int getSponsoredId() {
		return SponsoredId;
	}

	public void setSponsoredId(int sponsoredId) {
		SponsoredId = sponsoredId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(int categoryId) {
		CategoryId = categoryId;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public char getListingType() {
		return ListingType;
	}

	public void setListingType(char listingType) {
		ListingType = listingType;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		Price = price;
	}

	public boolean isPriceNegotiable() {
		return PriceNegotiable;
	}

	public void setPriceNegotiable(boolean priceNegotiable) {
		PriceNegotiable = priceNegotiable;
	}

	public String getPricePeriod() {
		return PricePeriod;
	}

	public void setPricePeriod(String pricePeriod) {
		PricePeriod = pricePeriod;
	}

	public char getTransactionType() {
		return TransactionType;
	}

	public void setTransactionType(char transactionType) {
		TransactionType = transactionType;
	}

	public Date getTransactionStart() {
		return TransactionStart;
	}

	public void setTransactionStart(Date transactionStart) {
		TransactionStart = transactionStart;
	}

	public Date getTransactionEnd() {
		return TransactionEnd;
	}

	public void setTransactionEnd(Date transactionEnd) {
		TransactionEnd = transactionEnd;
	}

	public Date getCreationDate() {
		return CreationDate;
	}

	public void setCreationDate(Date creationDate) {
		CreationDate = creationDate;
	}

	public Date getExpiryDate() {
		return ExpiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		ExpiryDate = expiryDate;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}
}

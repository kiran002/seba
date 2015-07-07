package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.Logger;
import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;

@Entity
public class Messages {

	public interface sendMessage {
	}

	@Id
	@GeneratedValue
	public int MessageId;

	@Constraints.Required(groups = sendMessage.class)
	public int ListingId;

	@Constraints.Required
	public int FromUserId;

	@Constraints.Required(groups = sendMessage.class)
	public int ToUserId;

	@Constraints.Required(groups = sendMessage.class)
	public String Message;

	@Constraints.Required
	public Date CreationDate;

	public void save() {
		JPA.em().persist(this);
	}

	public Messages() {

	}

	public Messages update() {
		return JPA.em().merge(this);

	}

	public void delete() {
		JPA.em().remove(this);
	}

	public static List<Integer> distinctUsers(int listingId) {
		String query_str = "Select distinct l.FromUserId from Messages l where l.ListingId=:lid";
		TypedQuery<Integer> query = JPA.em().createQuery(query_str,
				Integer.class);
		return query.setParameter("lid", listingId).getResultList();
	}

	public static List<Messages> findAll(int userId, boolean received) {
		String tmp = "SELECT l FROM Messages l where l.FromUserId =:frm_uid order by MessageId DESC";
		if (received) {
			tmp = "SELECT l FROM Messages l where l.ToUserId =:frm_uid order by MessageId DESC";
		}
		Logger.info(tmp);
		TypedQuery<Messages> query = JPA.em().createQuery(tmp, Messages.class);
		return query.setParameter("frm_uid", userId).getResultList();
	}

	public static List<Messages> findAll(int userId, int listingId) {
		TypedQuery<Messages> query = JPA
				.em()
				.createQuery(
						"SELECT l FROM Messages l where (l.FromUserId =:frm_uid or l.ToUserId = :to_uids) and l.ListingId = :lid  order by CreationDate ASC",
						Messages.class);
		return query.setParameter("frm_uid", userId)
				.setParameter("to_uids", userId).setParameter("lid", listingId)
				.getResultList();
	}

	public int getListingId() {
		return ListingId;
	}

	public void setListingId(int listingId) {
		ListingId = listingId;
	}

	public int getFromUserId() {
		return FromUserId;
	}

	public void setFromUserId(int fromUserId) {
		FromUserId = fromUserId;
	}

	public int getToUserId() {
		return ToUserId;
	}

	public void setToUserId(int toUserId) {
		ToUserId = toUserId;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public Date getCreationDate() {
		return CreationDate;
	}

	public void setCreationDate(Date creationDate) {
		CreationDate = creationDate;
	}
}

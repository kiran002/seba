package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

@Entity
public class Payments {

	@Id
	@GeneratedValue
	public int PaymentId;
	
	@Constraints.Required
	public int UserId;
	
	@Constraints.Required
	public Date CreationDate;
	
	@Constraints.Required
	public double amount;

	public Payments() {

	}

	public Payments(int PaymentId, int UserId, double amount) {
		this.PaymentId = PaymentId;
		this.UserId = UserId;
		this.amount = amount;
		this.CreationDate = new Date();
	}

	public void save() {
		JPA.em().persist(this);
	}

	public Payments update() {
		return JPA.em().merge(this);
	}

	public void delete() {
		JPA.em().remove(this);
	}
}

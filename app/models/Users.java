package models;

import play.data.validation.Constraints;
import play.db.jpa.JPA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TypedQuery;

import java.util.Date;
import java.util.Random;

@Entity
public class Users {

	@Id
	@GeneratedValue
	public int UserId;

	@Constraints.MaxLength(value = 40)
	@Constraints.Required
	public String FirstName;

	
	@Constraints.MaxLength(value = 40)
	@Constraints.Required
	public String LastName;

	@Constraints.Required
	public String ActivationCode;

	@Constraints.Required
	public boolean isActivated;

	@Constraints.Required
	public Date CreationDate;

	@Constraints.Required
	public String Email;

	@Constraints.Required
	public String password;

	
	public String AuthCode;
	
	
	public Users() {
	}

	public Users(String firstName2, String lastName2, String email2,
			String password2) {
		Random r = new Random();
		String pass="";
		for(int i=0;i<5;i++){
			pass = pass + r.nextInt(10);
		}
		this.ActivationCode = pass;
		this.FirstName = firstName2;
		this.CreationDate = new Date();
		this.isActivated = false;
		this.LastName = lastName2;
		this.Email = email2;
		this.password = password2;
	}

	
	
	public void save() {
		JPA.em().persist(this);
	}

	public Users update() {
		return JPA.em().merge(this);
	}

	public void delete() {
		JPA.em().remove(this);
	}

	public static Users findUser(String emailId) {
		TypedQuery<Users> query = JPA.em().createQuery(
				"SELECT u FROM Users u where u.Email = :email", Users.class);
		return query.setParameter("email", emailId).getSingleResult();
	}

	public static Users findById(Integer id) {
		return JPA.em().find(Users.class, id);
	}

}

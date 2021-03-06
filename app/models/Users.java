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

	public interface createUser {	}

	@Id
	@GeneratedValue
	public int UserId;

	@Constraints.MaxLength(value = 40)
	@Constraints.Required(groups=createUser.class)
	public String FirstName;

	@Constraints.MaxLength(value = 40)
	@Constraints.Required(groups=createUser.class)
	public String LastName;

	@Constraints.Required
	public String ActivationCode;

	@Constraints.Required
	public boolean isActivated;

	@Constraints.Required
	public Date CreationDate;

	@Constraints.Required(groups=createUser.class)
	public String Email;

	@Constraints.Required(groups=createUser.class)
	public String password;

	public String AuthCode;

	public Users() {
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

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getActivationCode() {
		return ActivationCode;
	}

	public void setActivationCode(String activationCode) {
		ActivationCode = activationCode;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	public Date getCreationDate() {
		return CreationDate;
	}

	public void setCreationDate(Date creationDate) {
		CreationDate = creationDate;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthCode() {
		return AuthCode;
	}

	public void setAuthCode(String authCode) {
		AuthCode = authCode;
	}

}

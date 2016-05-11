package hr.fer.zemris.java.tecaj_14.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Class containing informations about specific blog user.
 * 
 * @author Marin Maršić
 *
 */
@Entity
@Table(name = "blog_users")
@Cacheable(true)
public class BlogUser {

	/**
	 * ID of the user.
	 */
	private Long id;
	/**
	 * First name.
	 */
	private String firstName;
	/**
	 * Last name.
	 */
	private String lastName;
	/**
	 * User's nickname.
	 */
	private String nick;
	/**
	 * User's e-mail address.
	 */
	private String email;
	/**
	 * SHA-1 value of the password.
	 */
	private String passwordHash;
	/**
	 * Blog entries written by this user.
	 */
	private List<BlogEntry> blogEntries = new ArrayList<>();

	/**
	 * @return Returns User's ID.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets the User's ID.
	 * 
	 * @param id
	 *            User's ID.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns User's first name.
	 */
	@Column(length = 50, nullable = false)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the User's first name.
	 * @param firstName User's first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Returns User's last name.
	 */
	@Column(length = 50, nullable = false)
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the User's last name.
	 * @param lastName User's last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return Returns User's nickname.
	 */
	@Column(nullable = true, length = 20, unique = true)
	public String getNick() {
		return nick;
	}

	/**
	 * Sets the User's nickname.
	 * @param nick User's nickname.
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return Returns User's e-mail.
	 */
	@Column(length = 50, nullable = false)
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the User's e-mail.
	 * @param email User's e-mail.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Returns User's password hash.
	 */
	@Column(length = 100, nullable = false)
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Sets user's hash value of password.
	 * @param password User's password.
	 */
	public void setPasswordHash(String password) {
		if (passwordHash == null || password.length()!=40){
			try {
				MessageDigest aes = MessageDigest.getInstance("SHA-1");
				aes.update(password.getBytes());

				// CREATE HEXADECIMAL STRING KEY
				byte[] digestedBytes = aes.digest();
				StringBuffer hexString = new StringBuffer();
				for (byte b : digestedBytes) {
					hexString.append(String.format("%02x", b));
				}

				this.passwordHash = hexString.toString();
			} catch (NoSuchAlgorithmException e) {
			}
		}
		passwordHash = password;
	}

	/**
	 * @return Returns blog entries written by the user.
	 */
	@OneToMany(mappedBy = "creator")
	public List<BlogEntry> getBlogEntries() {
		return blogEntries;
	}

	/**
	 * Sets the blog entries written by user.
	 * @param blogEntries Blog entries written by user.
	 */
	public void setBlogEntries(List<BlogEntry> blogEntries) {
		this.blogEntries = blogEntries;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

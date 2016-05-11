package hr.fer.zemris.java.tecaj_14.model;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class representing specific blog comment.
 * 
 * @author Marin Maršić
 *
 */
@Entity
@Table(name = "blog_comments")
@Cacheable(true)
public class BlogComment {

	/**
	 * Comment ID.
	 */
	private Long id;
	/**
	 * Blog entry associated with this comment.
	 */
	private BlogEntry blogEntry;
	/**
	 * E-mail of user who posted this comment.
	 */
	private String usersEMail;
	/**
	 * Text of a comment.
	 */
	private String message;
	/**
	 * Date of the comment's creation.
	 */
	private Date postedOn;

	/**
	 * @return Returns id of the comment.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets the comment's ID.
	 * 
	 * @param id
	 *            Comment's ID.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns Blog entry associated with this comment.
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * Sets the blog entry associated with this comment.
	 * @param blogEntry Blog entry associated with this comment.
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * @return Returns E-mail of user who posted this comment.
	 */
	@Column(length = 100, nullable = false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Sets the E-mail of user who posted this comment.
	 * @param usersEMail E-mail of user who posted this comment.
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * @return Returns text of a comment.
	 */
	@Column(length = 4096, nullable = false)
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the text of a comment.
	 * @param message Text of a comment.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Returns date of the comment's creation.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Sets the date of the comment's creation.
	 * @param postedOn Date of the comment's creation.
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
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
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

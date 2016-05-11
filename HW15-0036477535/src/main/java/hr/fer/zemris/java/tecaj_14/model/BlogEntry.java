package hr.fer.zemris.java.tecaj_14.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class containing informations about specific blog entry.
 * 
 * @author Marin Maršić
 *
 */
@NamedQueries({ @NamedQuery(name = "BlogEntry.upit1", query = "select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }) })
@Entity
@Table(name = "blog_entries")
@Cacheable(true)
public class BlogEntry {

	/**
	 * ID of the entry.
	 */
	private Long id;
	/**
	 * List of comments.
	 */
	private List<BlogComment> comments = new ArrayList<>();
	/**
	 * Date of creation.
	 */
	private Date createdAt;
	/**
	 * Date of last modification.
	 */
	private Date lastModifiedAt;
	/**
	 * Title of the entry.
	 */
	private String title;
	/**
	 * Text of the entry.
	 */
	private String text;
	/**
	 * User who created the blog entry.
	 */
	private BlogUser creator;

	/**
	 * @return Returns entry's ID.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets the entry's ID.
	 * 
	 * @param id
	 *            Entry's ID.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns list of entry's comments.
	 */
	@OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public List<BlogComment> getComments() {
		return comments;
	}

	/**
	 * Sets the list of comments.
	 * 
	 * @param comments
	 *            List of comments.
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * @return Returns entry's date of creation.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the date of creation.
	 * 
	 * @param createdAt
	 *            Date of creation.
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return Returns entry's date of last modification.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Sets the date of last modification.
	 * 
	 * @param lastModifiedAt
	 *            Date of last modification.
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * @return Returns entry's title.
	 */
	@Column(nullable = false, length = 200)
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the entry's title.
	 * 
	 * @param title
	 *            Entry's title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns entry's text.
	 */
	@Column(nullable = false, length = 4096)
	public String getText() {
		return text;
	}

	/**
	 * Sets the entry's text.
	 * 
	 * @param text
	 *            entry's text.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return Returns entry's creator.
	 */
	@ManyToOne
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * Sets the creator of the entry.
	 * @param creator Creator of the entry.
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

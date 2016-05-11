package hr.fer.zemris.java.tecaj_14.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.tecaj_14.dao.DAO;
import hr.fer.zemris.java.tecaj_14.dao.DAOException;
import hr.fer.zemris.java.tecaj_14.formular.FormularComment;
import hr.fer.zemris.java.tecaj_14.formular.FormularEntry;
import hr.fer.zemris.java.tecaj_14.formular.FormularReg;
import hr.fer.zemris.java.tecaj_14.model.BlogComment;
import hr.fer.zemris.java.tecaj_14.model.BlogEntry;
import hr.fer.zemris.java.tecaj_14.model.BlogUser;

/**
 * Implementation of the DAO system using the JSP technology.
 * 
 * @author Marin Maršić
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(
				BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public BlogUser getUserByNick(String nick) {
		EntityManager em = JPAEMProvider.getEntityManager();

		@SuppressWarnings("unchecked")
		List<BlogUser> users = (List<BlogUser>) em
				.createQuery("select b from BlogUser as b where b.nick=:nick")
				.setParameter("nick", nick)
				.setHint("org.hibernate.cacheable", Boolean.TRUE)
				.getResultList();

		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}

	@Override
	public List<BlogUser> getAllUsers() {
		EntityManager em = JPAEMProvider.getEntityManager();

		@SuppressWarnings("unchecked")
		List<BlogUser> users = (List<BlogUser>) em
				.createQuery("select b from BlogUser as b")
				.setHint("org.hibernate.cacheable", Boolean.TRUE)
				.getResultList();

		return users;
	}

	@Override
	public BlogEntry getUsersEntry(String nick, Long id) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();

		BlogEntry entry = (BlogEntry) em
				.createQuery(
						"select b from BlogEntry as b where b.creator.nick=:nick and b.id=:id")
				.setParameter("nick", nick.trim()).setParameter("id", id)
				.setHint("org.hibernate.cacheable", Boolean.TRUE)
				.getSingleResult();

		return entry;
	}

	@Override
	public List<BlogComment> getBlogComments(BlogEntry entry) {
		EntityManager em = JPAEMProvider.getEntityManager();

		@SuppressWarnings("unchecked")
		List<BlogComment> comments = (List<BlogComment>) em
				.createQuery(
						"select b from BlogComment as b where b.blogEntry=:be")
				.setParameter("be", entry)
				.setHint("org.hibernate.cacheable", Boolean.TRUE)
				.getResultList();

		return comments;
	}

	@Override
	public List<BlogEntry> getUsersEntries(String nick) {
		EntityManager em = JPAEMProvider.getEntityManager();

		@SuppressWarnings("unchecked")
		List<BlogEntry> entries = (List<BlogEntry>) em
				.createQuery(
						"select b from BlogEntry as b where b.creator.nick=:nick")
				.setParameter("nick", nick)
				.setHint("org.hibernate.cacheable", Boolean.TRUE)
				.getResultList();

		return entries;
	}

	@Override
	public void createNewUser(FormularReg f) {
		EntityManager em = JPAEMProvider.getEntityManager();
		BlogUser blogUser = new BlogUser();
		f.popuniURecord(blogUser);

		em.persist(blogUser);
	}

	@Override
	public void createNewComment(FormularComment f, String email,
			BlogEntry entry) {
		EntityManager em = JPAEMProvider.getEntityManager();

		BlogComment comment = new BlogComment();
		f.popuniURecord(comment);

		comment.setPostedOn(new Date());
		comment.setUsersEMail(email);
		comment.setBlogEntry(entry);

		em.persist(comment);
	}

	@Override
	public void createNewEntry(FormularEntry f, BlogUser user) {
		EntityManager em = JPAEMProvider.getEntityManager();

		BlogEntry entry = new BlogEntry();
		f.popuniURecord(entry);

		entry.setCreatedAt(new Date());
		entry.setLastModifiedAt(new Date());
		entry.setCreator(user);

		em.persist(entry);
	}

	@Override
	public void editEntry(FormularEntry f, Long id) {
		EntityManager em = JPAEMProvider.getEntityManager();

		BlogEntry entry = em.find(BlogEntry.class, id);
		f.popuniURecord(entry);
		entry.setLastModifiedAt(new Date());
	}

}

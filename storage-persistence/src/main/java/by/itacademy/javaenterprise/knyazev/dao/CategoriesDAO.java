package by.itacademy.javaenterprise.knyazev.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import by.itacademy.javaenterprise.knyazev.dao.exceptions.DAOException;
import by.itacademy.javaenterprise.knyazev.entities.Category;

@Repository
public class CategoriesDAO implements DAO<Category>{
	private static final Logger logger = LoggerFactory.getLogger(CategoriesDAO.class);
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Long save(Category category) throws DAOException {
		if (category != null) {
			entityManager.persist(category);
			return category.getId();
		} else {
			throw new DAOException(
					"Expected Category object. Null was given in method Long save(Category category)");
		}
	}

	@Override
	public Category find(Long id) throws DAOException {

		if (id == null || id < 0L) {
			throw new DAOException(
					"Expected Category object. Null or bad ID was given in method Category find(Long id)");
		}

		try {
			return entityManager.find(Category.class, id);
		} catch (IllegalArgumentException e) {
			logger.error("Can't get Category object on id=" + id + " on method Category find(Long id): "
					+ e.getMessage() + " with exception class name: " + e.getClass().getCanonicalName(), e);
		}

		return null;
	}

	@Override
	public List<Category> findAll() {

		try {
			return entityManager.createNamedQuery("allCategories", Category.class).getResultList();
		} catch (IllegalStateException | IllegalArgumentException | PersistenceException e) {
			logger.error("Error in method List<Category> findAll(): " + e.getMessage() + " from class exception name: "
					+ e.getClass().getCanonicalName(), e);
		}

		return Collections.emptyList();
	}

	@Override
	public void update(Category category) throws DAOException {	
		
		if (category != null && category.getId() > 0) {
			entityManager.merge(category);
		} else {
			throw new DAOException("Error can't merge object on method void update(Category category)");
		}
	}

	@Override
	public void delete(Category category) throws DAOException {
		
		if (category != null && category.getId() > 0) {
			entityManager.remove(category);
		} else {
			throw new DAOException("Error can't remove object on method void delete(Category category)");
		}
	}

}

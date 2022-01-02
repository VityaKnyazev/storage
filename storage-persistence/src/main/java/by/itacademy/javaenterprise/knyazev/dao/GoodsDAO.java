package by.itacademy.javaenterprise.knyazev.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import by.itacademy.javaenterprise.knyazev.dao.exceptions.DAOException;
import by.itacademy.javaenterprise.knyazev.entities.Good;

@Repository
public class GoodsDAO implements DAO<Good>{
	private static final Logger logger = LoggerFactory.getLogger(GoodsDAO.class);
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Long save(Good good) throws DAOException {
		if (good != null) {
			entityManager.persist(good);
			return good.getId();
		} else {
			throw new DAOException(
					"Expected good object. Null was given in method Long save(Good good)");
		}
	}

	@Override
	public Good find(Long id) throws DAOException {

		if (id == null || id < 0L) {
			throw new DAOException(
					"Expected good object. Null or bad ID was given in method Good find(Long id)");
		}
		try {
			return entityManager.find(Good.class, id);
		} catch (IllegalArgumentException e) {
			logger.error("Can't get good object on id=" + id + " on method good find(Integer id): "
					+ e.getMessage() + " with exception class name: " + e.getClass().getCanonicalName(), e);
		}

		return null;
	}
	
	
	public Long findCountByCategoryId(Long id) {
		try {
			return (Long) entityManager.createNamedQuery("countGoodsByCategoryId").setParameter("category_id", id).getResultList().get(0);
		} catch (IllegalStateException | IllegalArgumentException | PersistenceException e) {
			logger.error("Error in method List<Good> findAll(): " + e.getMessage() + " from class exception name: "
					+ e.getClass().getCanonicalName(), e);
		}

		return 0L;
	}


	@Override
	public List<Good> findAll() {

		try {
			return entityManager.createNamedQuery("allGoods", Good.class).getResultList();
		} catch (IllegalStateException | IllegalArgumentException | PersistenceException e) {
			logger.error("Error in method List<Good> findAll(): " + e.getMessage() + " from class exception name: "
					+ e.getClass().getCanonicalName(), e);
		}

		return Collections.emptyList();
	}

	@Override
	@Transactional
	public void update(Good good) throws DAOException {
		if (good != null && good.getId() > 0) {
			entityManager.merge(good);
		} else {
			throw new DAOException("Error can't merge object on method void update(Good good)");
		}
	}

	@Override
	@Transactional
	public void delete(Good good) throws DAOException {
		if (good != null && good.getId() > 0) {
			entityManager.remove(good);
		} else {
			throw new DAOException("Error can't remove object on method void delete(Good good)");
		}
	}

}

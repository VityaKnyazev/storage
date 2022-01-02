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
import by.itacademy.javaenterprise.knyazev.entities.Producer;

@Repository
public class ProducersDAO implements DAO<Producer>{
	private static final Logger logger = LoggerFactory.getLogger(ProducersDAO.class);
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Long save(Producer producer) throws DAOException {
		
		if (producer != null) {
			entityManager.persist(producer);
			return producer.getId();
		} else {
			throw new DAOException(
					"Expected Producer object. Null was given in method Long save(Producer producer)");
		}
	}

	@Override
	public Producer find(Long id) throws DAOException {

		if (id == null || id < 0L) {
			throw new DAOException(
					"Expected Producer object. Null or bad ID was given in method Producer find(Long id)");
		}

		try {
			return entityManager.find(Producer.class, id);
		} catch (IllegalArgumentException e) {
			logger.error("Can't get Producer object on id=" + id + " on method Producer find(Integer id): "
					+ e.getMessage() + " with exception class name: " + e.getClass().getCanonicalName(), e);
		}

		return null;
	}

	@Override
	public List<Producer> findAll() {

		try {
			return entityManager.createNamedQuery("allProducers", Producer.class).getResultList();
		} catch (IllegalStateException | IllegalArgumentException | PersistenceException e) {
			logger.error("Error in method List<Producer> findAll(): " + e.getMessage() + " from class exception name: "
					+ e.getClass().getCanonicalName(), e);
		}

		return Collections.emptyList();
	}

	@Override
	@Transactional
	public void update(Producer producer) throws DAOException {
		
		if (producer != null && producer.getId() > 0) {
			entityManager.merge(producer);
		} else {
			throw new DAOException("Error can't merge object on method void update(Producer producer)");
		}
	}

	@Override
	@Transactional
	public void delete(Producer producer) throws DAOException {
		
		if (producer != null && producer.getId() > 0) {
			entityManager.remove(producer);
		} else {
			throw new DAOException("Error can't remove object on method void delete(Producer producer)");
		}
	}

}

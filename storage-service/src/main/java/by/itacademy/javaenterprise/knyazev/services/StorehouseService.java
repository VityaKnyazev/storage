package by.itacademy.javaenterprise.knyazev.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import by.itacademy.javaenterprise.knyazev.dao.StorehouseDAO;
import by.itacademy.javaenterprise.knyazev.entities.Storehouse;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;

@Service
public class StorehouseService {
	private static final Logger logger = LoggerFactory.getLogger(StorehouseService.class);

	private StorehouseDAO storehouseDAO;

	@Autowired
	public StorehouseService(StorehouseDAO storehouseDAO) {
		this.storehouseDAO = storehouseDAO;
	}

	@Transactional
	public List<Storehouse> showAll() {
		return storehouseDAO.findAll();
	}

	@Transactional
	public List<Storehouse> showAll(Integer page, Integer size) throws ServiceException {
		if (page == null || size == null || page < 1L || size < 1L) {
			throw new ServiceException(
					"Page must be equals or greater then 1 and size must be equals or greater than 1 for showing all storehouses");
		}

		int realPage = page.intValue() - 1;
		List<Storehouse> storehouses = storehouseDAO.findAll(PageRequest.of(realPage, size)).getContent();

		if (!storehouses.isEmpty()) {
			return storehouses;
		}
		logger.error("Error on loading storehouses");
		throw new ServiceException("Error on loading storehouses");
	}

	@Transactional
	public Storehouse showStorehouseUnitById(Long id) throws ServiceException {
		if (id != null) {
			Optional<Storehouse> storehouse = storehouseDAO.findById(id);
			if (storehouse.isPresent()) {
				return storehouse.get();
			}
		}
		logger.error("Error loading storehouse on id={}", id);
		throw new ServiceException("Error loading storehouse on id=" + id + "!");
	}

	@Transactional
	public Storehouse saveStorehouse(Storehouse storehouse) throws ServiceException {
		if (storehouse == null || storehouse.getId() != null) {
			logger.error("Storehouse have to exists and storehouse id must be equals to null for saving!");
			throw new ServiceException(
					"Storehouse have to exists and storehouse id must be equals to null for saving!");
		}

		Long goodId = storehouse.getGood() != null ? storehouse.getGood().getId() : null;
		Storehouse existingStorehouse = storehouseDAO.findByGoodId(goodId);

		if (existingStorehouse != null) {
			logger.error("Saving storehouse already exists with unique good!");
			throw new ServiceException("Saving storehouse already exists with unique good!");
		}

		BigDecimal price = storehouse.getPrice() != null ? storehouse.getPrice() : new BigDecimal(0);
		if (price.compareTo(new BigDecimal(0)) != 1) {
			logger.error("Error on saving storehouse. Storehouse price must be above zero!");
			throw new ServiceException("Error on saving storehouse. Storehouse price must be above zero!");
		}

		BigDecimal quantity = storehouse.getQuantity() != null ? storehouse.getQuantity() : new BigDecimal(0);
		if (quantity.compareTo(new BigDecimal(0)) != 1) {
			logger.error("Error on saving storehouse. Storehouse quantity must be above zero!");
			throw new ServiceException("Error on saving storehouse. Storehouse quantity must be above zero!");
		}

		Storehouse savedStorehouse = storehouseDAO.save(storehouse);

		if (savedStorehouse != null) {
			if ((savedStorehouse.getId() != null) && (savedStorehouse.getId() > 0L))
				return savedStorehouse;
		}
		logger.error("Error on saving storehouse!");
		throw new ServiceException("Error on saving storehouse!");
	}

	@Transactional
	public Storehouse updateStorehouse(Storehouse storehouse) throws ServiceException {
		Long id = (storehouse != null) ? storehouse.getId() : null;

		if (id == null || id <= 0L) {
			logger.error("Storehouse must have a valid id for updating!");
			throw new ServiceException("Storehouse must have a valid id for updating!");
		}

		if (!storehouseDAO.existsById(id)) {
			logger.error("Error on updating storehouse by non existing id = {}!", id);
			throw new ServiceException("Error on updating storehouse by non existing id = " + id);
		}
		
		BigDecimal price = storehouse.getPrice() != null ? storehouse.getPrice() : new BigDecimal(0);
		if (price.compareTo(new BigDecimal(0)) != 1) {
			logger.error("Error on updating storehouse. Storehouse price must be above zero!");
			throw new ServiceException("Error on updating storehouse. Storehouse price must be above zero!");
		}
		
		BigDecimal quantity = storehouse.getQuantity() != null ? storehouse.getQuantity() : new BigDecimal(0);
		if (quantity.compareTo(new BigDecimal(0)) != 1) {
			logger.error("Error on updating storehouse. Storehouse quantity must be above zero!");
			throw new ServiceException("Error on updating storehouse. Storehouse quantity must be above zero!");
		}

		Storehouse updatedStorehouse = storehouseDAO.save(storehouse);

		if (updatedStorehouse != null) {
			if ((updatedStorehouse.getId() != null) && (updatedStorehouse.getId() > 0L)) {
				return updatedStorehouse;
			}
		}

		logger.error("Error on updating storehouse!");
		throw new ServiceException("Error on updating storehouse!");
	}

	@Transactional
	public void deleteStorehouse(Long id) throws ServiceException {
		if ((id != null) && (storehouseDAO.existsById(id))) {
			storehouseDAO.deleteById(id);
		} else {
			logger.error("Error on deleting storehouse on id={}!", id);
			throw new ServiceException("Error on deleting storehouse on id=" + id + "!");
		}
	}
}
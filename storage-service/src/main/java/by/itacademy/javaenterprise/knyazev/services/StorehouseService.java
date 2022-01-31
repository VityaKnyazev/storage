package by.itacademy.javaenterprise.knyazev.services;

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
	@Autowired
	private StorehouseDAO storehouseDAO;
	private static final int COUNT_ON_PAGE = 3;
	private static final Logger logger = LoggerFactory.getLogger(StorehouseService.class);

	@Transactional
	public List<Storehouse> showAll() {
		return storehouseDAO.findAll();
	}

	@Transactional
	public List<Storehouse> showAll(Integer page) throws ServiceException {
		int realPage = page.intValue() - 1;
		List<Storehouse> storehouses = storehouseDAO.findAll(PageRequest.of(realPage, COUNT_ON_PAGE)).getContent();

		if (!storehouses.isEmpty()) {
			return storehouses;
		}
		logger.error("Error in page mumber");
		throw new ServiceException("Error in page mumber");
	}

	@Transactional
	public Storehouse showStorehouseUnitById(Long id) throws ServiceException {
		Optional<Storehouse> storehouse = storehouseDAO.findById(id);
		if (storehouse.isPresent()) {
			return storehouse.get();
		}
		logger.error("Error loading storehouse on id={}", id);
		throw new ServiceException("Error loading storehouse on id=" + id + "!");
	}

	@Transactional
	public Storehouse saveStorehouse(Storehouse storehouse) throws ServiceException {
		if (storehouse.getId() != null) {
			logger.error("Storehouse id must be equals to null for saving!");
			throw new ServiceException("Storehouse id must be equals to null for saving!");
		}

		Storehouse existingStorehouse = storehouseDAO.findByGoodId(storehouse.getGood().getId());

		if (existingStorehouse != null) {
			logger.error("Saving storehouse already exists with unique good!");
			throw new ServiceException("Saving storehouse already exists with unique good!");
		}

		Storehouse savedStorehouse = storehouseDAO.save(storehouse);

		if (savedStorehouse != null) {
			if (savedStorehouse.getId() != null && (savedStorehouse.getId() > 0L))
				return savedStorehouse;
		}
		logger.error("Error on saving storehose!");
		throw new ServiceException("Error on saving storehose!");
	}

	@Transactional
	public Storehouse updateStorehouse(Storehouse storehouse) throws ServiceException {
		if (storehouse.getId() == null || storehouse.getId() <= 0L) {
			logger.error("Storehouse must have a valid id for updating!");
			throw new ServiceException("Storehouse must have a valid id for updating!");
		}

		if (storehouseDAO.existsById(storehouse.getId())) {
			return storehouseDAO.save(storehouse);
		}
		
		logger.error("Error on updating storehouse!");
		throw new ServiceException("Error on updating storehouse!");
	}

	@Transactional
	public void deleteStorehouse(Long id) throws ServiceException {
		if (storehouseDAO.existsById(id)) {
			storehouseDAO.deleteById(id);
		} else {
			logger.error("Error on deleting storehouse on id={}!", id);
			throw new ServiceException("Error on deleting storehouse on id=" + id + "!");
		}
	}
}
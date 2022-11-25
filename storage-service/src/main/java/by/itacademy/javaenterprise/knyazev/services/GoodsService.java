package by.itacademy.javaenterprise.knyazev.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import by.itacademy.javaenterprise.knyazev.dao.GoodsDAO;
import by.itacademy.javaenterprise.knyazev.entities.Good;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;

@Service
public class GoodsService {
	private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);
	private GoodsDAO goodsDAO;

	@Autowired
	public GoodsService(GoodsDAO goodsDAO) {
		this.goodsDAO = goodsDAO;
	}

	@Transactional
	public List<Good> showAll() {
		return goodsDAO.findAll();
	}

	@Transactional
	public List<Good> showAll(Integer page, Integer size) throws ServiceException {
		if (page == null || size == null || page < 1L || size < 1L) {
			throw new ServiceException(
					"Page must be equals or greater then 1 and size must be equals or greater than 1 for showing all goods");
		}

		int realPage = page - 1;
		List<Good> goods = goodsDAO.findAll(PageRequest.of(realPage, size)).getContent();

		if (!goods.isEmpty()) {
			return goods;
		}

		logger.error("Error on loading goods");
		throw new ServiceException("Error on loading goods");
	}

	@Transactional
	public Good showGoodById(Long id) throws ServiceException {
		if (id != null) {
			Optional<Good> good = goodsDAO.findById(id);
			if (good.isPresent()) {
				return good.get();
			}
		}
		logger.error("Error loading good on id={}", id);
		throw new ServiceException("Error loading good on id=" + id + "!");
	}

	@Transactional
	public Good saveGood(Good good) throws ServiceException {

		if (good == null || good.getId() != null) {
			logger.error("Good have to exists and good id must be equals to null for saving!");
			throw new ServiceException("Good have to exists and good id must be equals to null for saving!");
		}

		String name = good.getName();
		String sort = good.getSort();
		Long categoryId = (good.getCategory() != null) ? good.getCategory().getId() : null;
		Long producerId = (good.getProducer() != null) ? good.getProducer().getId() : null;

		Good existingGood = goodsDAO.findByNameAndSortAndCategoryIdAndProducerId(name, sort, categoryId, producerId);

		if (existingGood != null) {
			logger.error("Saving good already exists with unique name, sort, category and producer!");
			throw new ServiceException("Saving good already exists with unique name, sort, category and producer!");
		}

		Good savedGood = goodsDAO.save(good);

		if (savedGood != null) {
			if ((savedGood.getId() != null) && (savedGood.getId() > 0L)) {
				return savedGood;
			}
		}
		logger.error("Error on saving good!");
		throw new ServiceException("Error on saving good!");
	}

	@Transactional
	public Good updateGood(Good good) throws ServiceException {
		Long id = (good != null) ? good.getId() : null;
		if (id == null || id <= 0L) {
			logger.error("Good must have a valid id for updating!");
			throw new ServiceException("Good must have a valid id for updating!");
		}
		
		
		if (!goodsDAO.existsById(id)) {
			logger.error("Error on updating good by non existing id = {}!", id);
			throw new ServiceException("Error on updating good by non existing id = " + id);
		}
		
		Good updatedGood = goodsDAO.save(good);
		
		if (updatedGood != null) {
			if ((updatedGood.getId() != null) && (updatedGood.getId() > 0L)) {
				return updatedGood;
			}
		}
		
		logger.error("Error on updating good!");
		throw new ServiceException("Error on updating good!");
	}

	@Transactional
	public void deleteGood(Long id) throws ServiceException {
		
		if ((id != null) && (goodsDAO.existsById(id))) {
			goodsDAO.deleteById(id);
		} else {
			logger.error("Error on deleting good on id={}!", id);
			throw new ServiceException("Error on deleting good on id=" + id + "!");
		}
	}

}
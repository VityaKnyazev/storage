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
	@Autowired
	private GoodsDAO goodsDAO;
	

	@Transactional
	public List<Good> showAll() {
		return goodsDAO.findAll();
	}

	@Transactional
	public List<Good> showAll(Integer page, Integer size) throws ServiceException {
		int realPage = page.intValue() - 1;
		List<Good> goods = goodsDAO.findAll(PageRequest.of(realPage, size)).getContent();

		if (!goods.isEmpty()) {
			return goods;
		}

		logger.error("Error in page mumber");
		throw new ServiceException("Error in page mumber");
	}

	@Transactional
	public Good showGoodById(Long id) throws ServiceException {
		Optional<Good> good = goodsDAO.findById(id);
		if (good.isPresent()) {
			return good.get();
		}
		logger.error("Error loading good on id={}", id);
		throw new ServiceException("Error loading good on id=" + id + "!");
	}

	@Transactional
	public Good saveGood(Good good) throws ServiceException {

		if (good.getId() != null) {
			logger.error("Good id must be equals to null for saving!");
			throw new ServiceException("Good id must be equals to null for saving!");
		}

		List<Good> existingGoods = goodsDAO.findByNameAndSort(good.getName(), good.getSort());
		Long categoryId = good.getCategory().getId();
		Long producerId = good.getProducer().getId();

		if (!existingGoods.isEmpty()) {
			for (Good existingGood : existingGoods) {

				if ((categoryId == existingGood.getCategory().getId())
						&& (producerId == existingGood.getProducer().getId())) {
					logger.error("Saving good already exists with unique name, sort, category and producer!");
					throw new ServiceException(
							"Saving good already exists with unique name, sort, category and producer!");
				}
			}
		}

		Good savedGood = goodsDAO.save(good);

		if (savedGood != null) {
			if (savedGood.getId() != null && (savedGood.getId() > 0L))
				return savedGood;
		}
		logger.error("Error on saving good!");
		throw new ServiceException("Error on saving good!");
	}

	@Transactional
	public Good updateGood(Good good) throws ServiceException {
		if (good.getId() == null || good.getId() <= 0L) {
			logger.error("Good must have a valid id for updating!");
			throw new ServiceException("Good must have a valid id for updating!");
		}

		if (goodsDAO.existsById(good.getId())) {
			return goodsDAO.save(good);
		}
		logger.error("Error on updating category!");
		throw new ServiceException("Error on updating category!");
	}

	@Transactional
	public void deleteGood(Long id) throws ServiceException {
		if (goodsDAO.existsById(id)) {
			goodsDAO.deleteById(id);
		} else {
			logger.error("Error on deleting category on id={}!", id);
			throw new ServiceException("Error on deleting category on id=" + id + "!");
		}
	}

}
package by.itacademy.javaenterprise.knyazev.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.itacademy.javaenterprise.knyazev.dao.CategoriesDAO;
import by.itacademy.javaenterprise.knyazev.dao.GoodsDAO;
import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;

@Service
public class CategoriesService {
	private static final Logger logger = LoggerFactory.getLogger(CategoriesService.class);
	private CategoriesDAO categoriesDAO;
	private GoodsDAO goodsDAO;

	@Autowired
	public CategoriesService(CategoriesDAO categoriesDAO, GoodsDAO goodsDAO) {
		this.categoriesDAO = categoriesDAO;
		this.goodsDAO = goodsDAO;
	}

	@Transactional
	public Map<Category, Long> showAll() throws ServiceException {
		Map<Category, Long> categoriesGoodsQuantity = new HashMap<>();

		List<Category> categories = categoriesDAO.findAll();

		if (categories == null || categories.isEmpty()) {
			throw new ServiceException("Categories not found!");
		}
		
		categories.stream().forEach(c -> {
			Long quantity = goodsDAO.findCountByCategoryId(c.getId());
			quantity = (quantity != null) ? quantity : 0L;
			categoriesGoodsQuantity.put(c, quantity);
		});

		return categoriesGoodsQuantity;
	}

	@Transactional
	public Category showCategoryById(Long id) throws ServiceException {
		if (id == null || id <= 0l) {
			logger.error("Error loading category, id must be not {}", id);
			throw new ServiceException("Error loading category, id must be not " + id + "!");
		}

		Optional<Category> optionalCategory = categoriesDAO.findById(id);
		if (optionalCategory.isPresent()) {
			Category savedCategory = optionalCategory.get();
			savedCategory.getGoods();
			return savedCategory;
		}

		logger.error("Error loading category on id={}!", id);
		throw new ServiceException("Error loading category on id=" + id + "!");
	}

	@Transactional
	public Category saveCategory(Category category) throws ServiceException {

		Category existing = categoriesDAO.findByName(category.getName());

		if (existing != null) {
			logger.error("Can't save category. Category with name={} already exists!", category.getName());
			throw new ServiceException(
					"Can't save category. Category with name=" + category.getName() + " already exists!");
		}

		Category savedCategory = null;
		if (category.getId() == null) {
			savedCategory = categoriesDAO.save(category);
		}

		if (savedCategory != null) {
			if ((savedCategory.getId() != null) && (savedCategory.getId() > 0L)) {
				return savedCategory;
			}
		}

		logger.error("Error on saving category!");
		throw new ServiceException("Error on saving category!");
	}

	@Transactional
	public Category updateCategory(Category category) throws ServiceException {
		Long id = (category != null) ? category.getId() : null;

		if (id == null || id <= 0L) {
			logger.error("Category must have a valid id for updating!");
			throw new ServiceException("Category must have a valid id for updating!");
		}

		if (!categoriesDAO.existsById(category.getId())) {
			logger.error("Error on updating category by non existing id = {}!", id);
			throw new ServiceException("Error on updating category by non existing id = " + id);
		}

		Category updatedCategory = categoriesDAO.save(category);

		if (updatedCategory != null) {
			if ((updatedCategory.getId() != null) && (updatedCategory.getId() > 0L)) {
				return updatedCategory;
			}
		}

		logger.error("Error on updating category!");
		throw new ServiceException("Error on updating category!");
	}

	@Transactional
	public void deleteCategory(Long id) throws ServiceException {

		if ((id != null) && (categoriesDAO.existsById(id))) {
			categoriesDAO.deleteById(id);
		} else {
			logger.error("Error on deleting category on id={}!", id);
			throw new ServiceException("Error on deleting category on id=" + id + "!");
		}
	}

}

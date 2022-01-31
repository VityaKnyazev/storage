package by.itacademy.javaenterprise.knyazev.services;

import java.util.HashMap;
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
	@Autowired
	private CategoriesDAO categoriesDAO;
	@Autowired
	private GoodsDAO goodsDAO;
	private static final Logger logger = LoggerFactory.getLogger(CategoriesService.class);

	@Transactional
	public Map<Category, Long> showAll() {
		Map<Category, Long> categoriesGoodsQuantity = new HashMap<>();

		categoriesDAO.findAll().stream().forEach(c -> {
			categoriesGoodsQuantity.put(c, goodsDAO.findCountByCategoryId(c.getId()));
		});

		return categoriesGoodsQuantity;
	}

	@Transactional
	public Category showCategoryById(Long id) throws ServiceException {

		Optional<Category> category = categoriesDAO.findById(id);
		if (category.isPresent()) {
			category.get().getGoods().size();
			return category.get();
		}
		
		logger.error("Error loading category on id={}!", id);
		throw new ServiceException("Error loading category on id=" + id + "!");
	}

	@Transactional
	public Category saveCategory(Category category) throws ServiceException {

		Category existing = categoriesDAO.findByName(category.getName());

		if (existing != null) {
			logger.error("Category with name={} already exists!", category.getName());
			throw new ServiceException("Category with name=" + category.getName() + " already exists!");
		}

		Category savedCategory = null;
		if (category.getId() == null) {
			savedCategory = categoriesDAO.save(category);
		}

		if (savedCategory != null) {
			if (savedCategory.getId() != null && (savedCategory.getId() > 0L))
				return savedCategory;
		}
		
		logger.error("Error on saving category!");
		throw new ServiceException("Error on saving category!");
	}

	@Transactional
	public Category updateCategory(Category category) throws ServiceException {
		if (category.getId() == null || category.getId() <= 0L) {
			logger.error("Category must have a valid id for updating!");
			throw new ServiceException("Category must have a valid id for updating!");
		}
		
		if (categoriesDAO.existsById(category.getId())) {
			return categoriesDAO.save(category);
		}
		
		logger.error("Error on updating category!");
		throw new ServiceException("Error on updating category!");
	}
	
	@Transactional
	public void deleteCategory(Long id) throws ServiceException {
				
		if (categoriesDAO.existsById(id)) {
			categoriesDAO.deleteById(id);
		} else {	
			logger.error("Error on deleting category on id={}!", id);
			throw new ServiceException("Error on deleting category on id=" + id + "!");
		}
	}

}

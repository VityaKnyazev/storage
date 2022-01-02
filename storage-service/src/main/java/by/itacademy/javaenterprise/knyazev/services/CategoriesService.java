package by.itacademy.javaenterprise.knyazev.services;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.itacademy.javaenterprise.knyazev.dao.CategoriesDAO;
import by.itacademy.javaenterprise.knyazev.dao.GoodsDAO;
import by.itacademy.javaenterprise.knyazev.dao.exceptions.DAOException;
import by.itacademy.javaenterprise.knyazev.entities.Category;

@Service
public class CategoriesService {
	@Autowired
	private CategoriesDAO categoriesDAO;
	@Autowired
	private GoodsDAO goodsDAO;

	@Transactional
	public Map<Category, Long> showAll() {
		Map<Category, Long> categoriesGoodsQuantity = new HashMap<>();

		categoriesDAO.findAll().stream().forEach(c -> {
			categoriesGoodsQuantity.put(c, goodsDAO.findCountByCategoryId(c.getId()));
		});

		return categoriesGoodsQuantity;
	}

	// TODO show Category with goods and to do validation
	@Transactional
	public Category showCategoryById(String id) {
		Long categoryId;
		try {
			categoryId = Long.valueOf(id);
		} catch (NumberFormatException e) {
			categoryId = null;
		}

		try {
			Category category = categoriesDAO.find(categoryId);
			if (category != null) {
				category.getGoods().size();
				return category;
			}
		} catch (DAOException e) {
			// TODO throw Service Exception
		}
		return null;
	}
}

package by.itacademy.javaenterprise.knyazev.services.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.services.CategoriesService;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;
import by.itacademy.javaenterprise.knyazev.spring.SpringPersistenceConfig;
import by.itacademy.javaenterprise.knyazev.spring.config.TestContainerConfig;
import by.itacademy.javaenterprise.knyazev.spring.config.TestServiceConfig;
import by.itacademy.javaenterprise.knyazev.spring.initializers.PropertyTestContextInitializer;

@ExtendWith(SpringExtension.class)
@ContextHierarchy({
		@ContextConfiguration(classes = { TestContainerConfig.class }), 
		@ContextConfiguration(classes = {SpringPersistenceConfig.class }, 
		                      initializers = { PropertyTestContextInitializer.class }),
		@ContextConfiguration(classes = { TestServiceConfig.class }) 
})
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class CategoriesServiceIntegrationTests {
	@Autowired
	CategoriesService categoriesService;

	@Test
	@Order(1)
	public void whenShowCategoryById() throws ServiceException {
		final Long categoryId = 1L;
		final String categoryName ="фрукты";
		
		Category category = categoriesService.showCategoryById(categoryId);
		assertEquals(categoryName, category.getName());
	}
	
	@Test
	@Order(2)
	public void whenShowAllCategories() throws ServiceException {
		final int countInDatabase = 4;
		
		Map<Category, Long> categories = categoriesService.showAll();
		assertEquals(countInDatabase, categories.keySet().size());
	}
	
	@Test
	@Order(3)
	public void whenSaveCategory() throws ServiceException {
		final String categoryName = "азиатские овощи";
		final String categoryDescription = "Овощи, привезенные из азиатских стран";
		final Category category = new Category(null, categoryName, categoryDescription);
		
		final Long savedCategoryId = 5L;
		
		Category savedCategory = categoriesService.saveCategory(category);
		assertNotNull(savedCategory);
		assertNotNull(savedCategory.getId());
		assertEquals(savedCategoryId, savedCategory.getId());
		
		assertEquals(categoryName, categoriesService.showCategoryById(savedCategory.getId()).getName());
		assertEquals(savedCategoryId.intValue(), categoriesService.showAll().keySet().size());
	}
	
	@Test
	@Order(4)
	public void whenUpdateCategory() throws ServiceException {
		final Long savedCategoryid = 5L;
		final String updatedName = "Среднеазиатские овощи";
		
		Category savedCategory = categoriesService.showCategoryById(savedCategoryid);
		savedCategory.setName(updatedName);
		
		Category updatedCategory = categoriesService.updateCategory(savedCategory);
		
		assertNotNull(updatedCategory);
		assertNotNull(updatedCategory.getId());
		assertEquals(savedCategoryid, updatedCategory.getId());
		
		assertEquals(updatedName, categoriesService.showCategoryById(savedCategoryid).getName());
	}
	
	@Test
	@Order(5)
	public void whenDeleteCategory() throws ServiceException {
		final Long deleteCategoryId = 5L;
		final int sizeAfterDeleting = deleteCategoryId.intValue() - 1;
		
		categoriesService.deleteCategory(deleteCategoryId);
		assertEquals(sizeAfterDeleting, categoriesService.showAll().keySet().size());
	}

}

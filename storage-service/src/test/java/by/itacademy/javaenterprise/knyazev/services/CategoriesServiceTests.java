package by.itacademy.javaenterprise.knyazev.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import by.itacademy.javaenterprise.knyazev.dao.CategoriesDAO;
import by.itacademy.javaenterprise.knyazev.entities.Category;

public class CategoriesServiceTests {
	private CategoriesDAO categoriesDAOMock;
	
	@BeforeEach
	public void setUpBeforeEachTest() {
		categoriesDAOMock = Mockito.mock(CategoriesDAO.class);
	}
	
	@Test
	public void whenSaveCategory() {
		Category category = new Category();
		category.setName("Овощи");
		
		Mockito.when(categoriesDAOMock.findByName(category.getName())).thenReturn(null);
		Mockito.when(categoriesDAOMock.save(category)).thenReturn(new Category(12L, "Овощи", "Овощи это хорошо"));
		
		Category savedCategory = categoriesDAOMock.save(category);
		
		assertNull(categoriesDAOMock.findByName(category.getName()));
		assertNotNull(savedCategory.getId());
		assertTrue(savedCategory.getId() > 0L);
		assertEquals(savedCategory.getName(), category.getName());		
	}
	
	@Test
	public void whenUpdatingCategory() {
		Category category = new Category(2, "Овощи", "Овощи - это");
	
		String name = "Овощные пельмени";
		category.setName(name);
		
		Mockito.when(categoriesDAOMock.existsById(category.getId())).thenReturn(true);
		Mockito.when(categoriesDAOMock.save(category)).thenReturn(category);
		
		boolean result = categoriesDAOMock.existsById(category.getId());
		Category updatedCategory = categoriesDAOMock.save(category);
		
		assertTrue(result);
		assertEquals(name, updatedCategory.getName());		
	}
	
	@Test
	public void whenDeletingCategory() {
		Category category = new Category(2, "Овощи", "Овощи - это");
		
		Mockito.when(categoriesDAOMock.existsById(category.getId())).thenReturn(true);
		boolean result = categoriesDAOMock.existsById(category.getId());
		
		assertTrue(result);
		assertDoesNotThrow(() -> categoriesDAOMock.deleteById(category.getId()), "IllegalArgumentException does not thrown");
	}
}

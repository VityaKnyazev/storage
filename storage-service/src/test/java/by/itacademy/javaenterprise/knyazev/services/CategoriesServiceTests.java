package by.itacademy.javaenterprise.knyazev.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import by.itacademy.javaenterprise.knyazev.dao.CategoriesDAO;
import by.itacademy.javaenterprise.knyazev.dao.GoodsDAO;
import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.entities.Good;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;

public class CategoriesServiceTests {
	private CategoriesService categoriesService;

	private CategoriesDAO categoriesDAOMock;
	private GoodsDAO goodsDAOMock;

	@BeforeEach
	public void setUpBeforeEachTest() {
		categoriesDAOMock = Mockito.mock(CategoriesDAO.class);
		goodsDAOMock = Mockito.mock(GoodsDAO.class);
		categoriesService = new CategoriesService(categoriesDAOMock, goodsDAOMock);
	}

	@Test
	public void whenShowAllCategories() throws ServiceException {
		List<Category> categories = new ArrayList<>();
		categories.add(new Category(1l, "Овощи", "Свекла, морковь и т.д."));
		categories.add(new Category(2l, "Фрукты", "Апельсины, бананы и т.д."));
		categories.add(new Category(3l, "Зелень", "Лук, петрушка, укроп и т.д."));

		final long count = 13l;

		Mockito.when(categoriesDAOMock.findAll()).thenReturn(categories);
		Mockito.when(goodsDAOMock.findCountByCategoryId(Mockito.longThat(arg -> arg >= 1L))).thenReturn(count);

		Map<Category, Long> categoriesGoodsQuantity = categoriesService.showAll();

		Collection<Long> resultValues = categoriesGoodsQuantity.values();
		resultValues.stream().forEach(c -> assertEquals(count, c));

		Set<Category> resultKeys = categoriesGoodsQuantity.keySet();
		List<Long> resultIds = resultKeys.stream().map(c -> c.getId()).sorted().collect(Collectors.toList());

		assertEquals(1l, resultIds.get(0));
		assertEquals(2l, resultIds.get(1));
		assertEquals(3l, resultIds.get(2));
	}

	@Test
	public void whenShowAllCategoriesOnNull() {
		Mockito.when(categoriesDAOMock.findAll()).thenReturn(null);

		assertThrows(ServiceException.class, () -> categoriesService.showAll(), "Service exception was expected");
	}

	@Test
	public void whenShowAllCategoriesOnEmpty() {
		Mockito.when(categoriesDAOMock.findAll()).thenReturn(new ArrayList<Category>());

		assertThrows(ServiceException.class, () -> categoriesService.showAll(), "Service exception was expected");
	}

	@Test
	public void whenShowCategoryById() throws ServiceException {
		List<Good> goods = new ArrayList<>();
		goods.add(new Good());
		goods.add(new Good());

		final Long id = 1l;
		final Optional<Category> optionalCategory = Optional.of(new Category(id, "Зелень", "Лук, укроп, перец", goods));

		Mockito.when(categoriesDAOMock.findById(Mockito.longThat(i -> i >= 1l))).thenReturn(optionalCategory);

		Category categoryResult = categoriesService.showCategoryById(id);

		assertEquals(id, categoryResult.getId());
	}

	@Test
	public void whenShowCategoryByNullId() {
		final Long id = null;

		assertThrows(ServiceException.class, () -> categoriesService.showCategoryById(id),
				"Service exception expected");
	}

	@Test
	public void whenShowCategoryByIdBelowOne() {
		final Long id = 0L;

		assertThrows(ServiceException.class, () -> categoriesService.showCategoryById(id),
				"Service exception expected");
	}

	@Test
	public void whenShowCategoryByWrongId() {
		final Long id = 358l;

		Mockito.when(categoriesDAOMock.findById(Mockito.eq(id))).thenReturn(Optional.empty());

		assertThrows(ServiceException.class, () -> categoriesService.showCategoryById(id),
				"Service exception expected");
	}

	@Test
	public void whenSaveCategory() throws ServiceException {
		final Long savedId = 25L;
		Category category = new Category(null, "Овощи", "Картофель, лук и т.д.");

		Mockito.when(categoriesDAOMock.findByName(Mockito.anyString())).thenReturn(null);
		Mockito.when(categoriesDAOMock.save(Mockito.any(Category.class))).thenAnswer(invocation -> {
			Category categoryIn = invocation.getArgument(0, Category.class);
			categoryIn.setId(savedId);
			return categoryIn;
		});

		Category savedCategory = categoriesService.saveCategory(category);

		assertNotNull(savedCategory.getId());
		assertTrue(savedCategory.getId() > 0L);
		assertEquals(savedId, savedCategory.getId());
	}

	@Test
	public void whenSaveCategoryExistsByName() {
		final String existingName = "Зелень";
		Category category = new Category(null, existingName, "Картофель, лук и т.д.");

		Mockito.when(categoriesDAOMock.findByName(Mockito.eq(existingName))).thenAnswer(invocation -> {
			return new Category(58L, existingName, "Лук и т.д.");
		});

		assertThrows(ServiceException.class, () -> categoriesService.saveCategory(category),
				"Service exception expected on existin category name");
	}

	@Test
	public void whenSaveCategoryContainsId() {
		Category category = new Category(12L, "Овощи", "Картофель, лук и т.д.");

		Mockito.when(categoriesDAOMock.findByName(Mockito.anyString())).thenReturn(null);

		assertThrows(ServiceException.class, () -> categoriesService.saveCategory(category),
				"Service exception expected on existin category id");
	}

	@Test
	public void whenSaveCategoryReturnsNull() {
		Category category = new Category(null, "Овощи", "Картофель, лук и т.д.");

		Mockito.when(categoriesDAOMock.findByName(Mockito.anyString())).thenReturn(null);
		Mockito.when(categoriesDAOMock.save(Mockito.any(Category.class))).thenReturn(null);

		assertThrows(ServiceException.class, () -> categoriesService.saveCategory(category),
				"Service exception expected when categoriesDAO.save(Category c) returns null");
	}

	@Test
	public void whenSaveCategoryReturnsCategoryWithNullId() {
		Category category = new Category(null, "Овощи", "Картофель, лук и т.д.");

		Mockito.when(categoriesDAOMock.findByName(Mockito.anyString())).thenReturn(null);
		Mockito.when(categoriesDAOMock.save(Mockito.any(Category.class)))
				.thenAnswer(invocation -> invocation.getArguments()[0]);

		assertThrows(ServiceException.class, () -> categoriesService.saveCategory(category),
				"Service exception expected when saved category has null id");
	}

	@Test
	public void whenSaveCategoryReturnsCategoryWithBadId() {
		Long badId = 0l;
		Category category = new Category(null, "Овощи", "Картофель, лук и т.д.");

		Mockito.when(categoriesDAOMock.findByName(Mockito.anyString())).thenReturn(null);
		Mockito.when(categoriesDAOMock.save(Mockito.any(Category.class))).thenAnswer(invocation -> {
			Category savedCategory = invocation.getArgument(0, Category.class);
			savedCategory.setId(badId);
			return savedCategory;
		});

		assertThrows(ServiceException.class, () -> categoriesService.saveCategory(category),
				"Service exception expected when saved category has bad id");
	}

	@Test
	public void whenUpdatingCategory() throws ServiceException {
		Category category = new Category(2L, "Овощи", "Овощи - это");
		String updatedName = "Овощные пельмени";

		Mockito.when(categoriesDAOMock.existsById(category.getId())).thenReturn(true);
		Mockito.when(categoriesDAOMock.save(category)).thenAnswer(invocation -> {
			Category categoryIn = invocation.getArgument(0, Category.class);
			categoryIn.setName(updatedName);
			return category;
		});

		Category updatedCategory = categoriesService.updateCategory(category);
		
		assertNotNull(updatedCategory.getId());
		assertTrue(updatedCategory.getId() > 0L);
		assertEquals(updatedName, updatedCategory.getName());
	}

	@Test
	public void whenUpdatingOnNullCategory() {
		Category category = null;

		assertThrows(ServiceException.class, () -> categoriesService.updateCategory(category),
				"Service exception expected when updating on null category");
	}

	@Test
	public void whenUpdatingOnNullId() {
		Category category = new Category(null, "Овощи", "Овощи - это");

		assertThrows(ServiceException.class, () -> categoriesService.updateCategory(category),
				"Service exception expected when updating on null id");
	}

	@Test
	public void whenUpdatingOnBadId() {
		Category category = new Category(-25L, "Овощи", "Овощи - это");

		assertThrows(ServiceException.class, () -> categoriesService.updateCategory(category),
				"Service exception expected when updating on bad id");
	}

	@Test
	public void whenUpdatingOnNonExistingId() {
		Category category = new Category(20L, "Овощи", "Овощи - это");

		Mockito.when(categoriesDAOMock.existsById(Mockito.longThat(l -> (l != null) && (l > 0l)))).thenReturn(false);

		assertThrows(ServiceException.class, () -> categoriesService.updateCategory(category),
				"Service exception expected when updating on non existing id");
	}

	@Test
	public void whenUpdatingCategoryReturnsNull() {
		Category category = new Category(20L, "Овощи", "Овощи - это");

		Mockito.when(categoriesDAOMock.existsById(Mockito.longThat(l -> (l != null) && (l > 0l)))).thenReturn(true);
		Mockito.when(categoriesDAOMock.save(category)).thenReturn(null);

		assertThrows(ServiceException.class, () -> categoriesService.updateCategory(category),
				"Service exception expected when updating category returns null");
	}
	
	@Test
	public void whenUpdatingCategoryReturnsNullCategoryId() {
		Category category = new Category(20L, "Овощи", "Овощи - это");

		Mockito.when(categoriesDAOMock.existsById(Mockito.longThat(l -> (l != null) && (l > 0l)))).thenReturn(true);
		Mockito.when(categoriesDAOMock.save(category)).thenReturn(new Category());

		assertThrows(ServiceException.class, () -> categoriesService.updateCategory(category),
				"Service exception expected when updating category returns null category id");
	}
	
	@Test
	public void whenUpdatingCategoryReturnsBadCategoryId() {
		Long badId = 0L;
		Category category = new Category(20L, "Овощи", "Овощи - это");

		Mockito.when(categoriesDAOMock.existsById(Mockito.longThat(l -> (l != null) && (l > 0l)))).thenReturn(true);
		Mockito.when(categoriesDAOMock.save(category)).thenAnswer(invocation-> {
			Category updatedCategory = invocation.getArgument(0, Category.class);
			updatedCategory.setId(badId);
			return updatedCategory;
		});

		assertThrows(ServiceException.class, () -> categoriesService.updateCategory(category),
				"Service exception expected when updating category returns bad category id");
	}

	@Test
	public void whenDeletingCategory() {
		Long id  = 5L;

		Mockito.when(categoriesDAOMock.existsById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)))).thenReturn(true);
		Mockito.doNothing().when(categoriesDAOMock).deleteById(id);

		assertDoesNotThrow(() -> categoriesService.deleteCategory(id),
				"ServiceException does not thrown");
	}

	@Test
	public void whenDeletingCategoryOnNoNExistingId() {
		Long id = 8L;

		Mockito.when(categoriesDAOMock.existsById(Mockito.anyLong())).thenReturn(false);

		assertThrows(ServiceException.class, () -> categoriesService.deleteCategory(id),
				"Service exception expected when deleting on non existing id");
	}
	
	@Test
	public void whenDeletingCategoryOnNullId() {
		Long id = null;

		Mockito.when(categoriesDAOMock.existsById(id)).thenReturn(false);

		assertThrows(ServiceException.class, () -> categoriesService.deleteCategory(id),
				"Service exception expected when deleting on null id");
	}
}

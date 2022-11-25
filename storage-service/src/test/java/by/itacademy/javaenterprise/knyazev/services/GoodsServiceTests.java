package by.itacademy.javaenterprise.knyazev.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import by.itacademy.javaenterprise.knyazev.dao.GoodsDAO;
import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.entities.Good;
import by.itacademy.javaenterprise.knyazev.entities.Producer;
import by.itacademy.javaenterprise.knyazev.entities.Storehouse;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;

public class GoodsServiceTests {
	private GoodsService goodsService;

	private GoodsDAO goodsDAOMock;

	@BeforeEach
	public void setUpBeforeEachTest() {
		goodsDAOMock = Mockito.mock(GoodsDAO.class);
		goodsService = new GoodsService(goodsDAOMock);

	}

	@Test
	public void whenShowAllGoods() {
		List<Good> goods = new ArrayList<>();
		goods.add(new Good(1L, "Яблоко", "Черныш", "Спелое", new Category(), new Producer(), new Storehouse()));

		Mockito.when(goodsDAOMock.findAll()).thenReturn(goods);

		assertEquals(1, goodsService.showAll().size());
	}

	@Test
	public void whenShowAllGoodsWithPageAndSize() throws ServiceException {
		Integer page = 1;
		Integer size = 2;

		List<Good> goods = new ArrayList<>();
		goods.add(new Good(1L, "Яблоко", "Черныш", "Спелое", new Category(), new Producer(), new Storehouse()));
		goods.add(new Good(2L, "Яблоко", "Коневод", "Сочное", new Category(), new Producer(), new Storehouse()));

		Mockito.when(goodsDAOMock.findAll(Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(goods));

		assertEquals(goodsService.showAll(page, size).get(1).getId(), 2L);
	}

	@Test
	public void whenShowAllGoodsWithNullPage() {
		Integer page = null;
		Integer size = 1;

		assertThrows(ServiceException.class, () -> goodsService.showAll(page, size),
				"ServiceException expected when null page passed");
	}

	@Test
	public void whenShowAllGoodsWithNullSize() {
		Integer page = 2;
		Integer size = null;

		assertThrows(ServiceException.class, () -> goodsService.showAll(page, size),
				"ServiceException expected when null size passed");
	}

	@Test
	public void whenShowAllGoodsWithBadPage() {
		Integer page = 0;
		Integer size = 5;

		assertThrows(ServiceException.class, () -> goodsService.showAll(page, size),
				"ServiceException expected when bad page passed");
	}

	@Test
	public void whenShowAllGoodsWithBadSize() {
		Integer page = 1;
		Integer size = 0;

		assertThrows(ServiceException.class, () -> goodsService.showAll(page, size),
				"ServiceException expected when bad size passed");
	}

	@Test
	public void whenShowAllGoodsIsEmpty() {
		int page = 10;
		int size = 5;

		List<Good> goods = new ArrayList<>();

		Mockito.when(goodsDAOMock.findAll(Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(goods));

		assertThrows(ServiceException.class, () -> goodsService.showAll(page, size),
				"ServiceException expected when empty list of goods returned");
	}

	@Test
	public void whenShowGoodById() throws ServiceException {
		Long id = 1L;
		Good good = new Good(id, "Яблоко", "Черныш", "Спелое", new Category(), new Producer(), new Storehouse());

		Optional<Good> optionalGood = Optional.of(good);

		Mockito.when(goodsDAOMock.findById(Mockito.argThat(l -> l > 0L))).thenReturn(optionalGood);

		assertEquals(id, goodsService.showGoodById(id).getId());
	}

	@Test
	public void whenShowGoodByIdAndListOfGoodsIsEmpty() {
		Long id = 18L;

		Mockito.when(goodsDAOMock.findById(Mockito.argThat(l -> l > 0L))).thenReturn(Optional.empty());

		assertThrows(ServiceException.class, () -> goodsService.showGoodById(id),
				"ServiceException expected when Optional empty good returned");
	}

	@Test
	public void whenShowGoodByNullId() {
		Long id = null;

		assertThrows(ServiceException.class, () -> goodsService.showGoodById(id),
				"ServiceException expected when null id passed");
	}

	@Test
	public void whenShowGoodByBadId() {
		Long id = -1L;

		assertThrows(ServiceException.class, () -> goodsService.showGoodById(id),
				"ServiceException expected when bad id passed");
	}

	@Test
	public void whenSaveGood() throws ServiceException {

		Mockito.when(goodsDAOMock.findByNameAndSortAndCategoryIdAndProducerId(
				Mockito.argThat(name -> (name.length() >= 3) && (name.length() <= 30)),
				Mockito.argThat(sort -> (sort.length() >= 3) && (sort.length() <= 30)),
				Mockito.argThat(cId -> cId > 0L), Mockito.argThat(pId -> pId > 0L))).thenReturn(null);

		final Long savedGoodId = 1l;
		final Long categoryId = 1L;
		final Long producerId = 1L;
		Good good = new Good(null, "Яблоко", "Черныш", "Спелое", new Category(), new Producer(), new Storehouse());
		good.getCategory().setId(categoryId);
		good.getProducer().setId(producerId);

		Mockito.when(goodsDAOMock.save(good)).thenAnswer(invocation -> {
			Good savedGood = invocation.getArgument(0, Good.class);
			savedGood.setId(savedGoodId);
			return savedGood;
		});

		Good savedGood = goodsService.saveGood(good);

		assertNotNull(savedGood.getId());
		assertTrue(savedGood.getId() > 0L);
		assertEquals(savedGoodId, savedGood.getId());
	}

	@Test
	public void whenSaveGoodEqualsNull() {
		Good good = null;

		assertThrows(ServiceException.class, () -> goodsService.saveGood(good),
				"ServiceException expected when null good passed to saveGood(good)");
	}

	@Test
	public void whenSaveGoodHasNullId() {
		Good good = new Good();
		good.setId(1L);

		assertThrows(ServiceException.class, () -> goodsService.saveGood(good),
				"ServiceException expected when null good id passed to saveGood(good)");
	}

	@Test
	public void whenSaveGoodAlreadyExists() {
		Long id = 1L;
		Long categoryId = 1L;
		Long producerId = 1L;

		Good existingGood = new Good(id, "Яблоко", "Черныш", "Спелое", new Category(), new Producer(),
				new Storehouse());
		existingGood.getCategory().setId(categoryId);
		existingGood.getProducer().setId(producerId);

		Mockito.when(goodsDAOMock.findByNameAndSortAndCategoryIdAndProducerId(
				Mockito.argThat(name -> (name.length() >= 3) && (name.length() <= 30)),
				Mockito.argThat(sort -> (sort.length() >= 3) && (sort.length() <= 30)),
				Mockito.argThat(cId -> cId > 0L), Mockito.argThat(pId -> pId > 0L))).thenReturn(existingGood);

		Good good = new Good(null, "Яблоко", "Черныш", "Спелое", new Category(), new Producer(), new Storehouse());
		good.getCategory().setId(categoryId);
		good.getProducer().setId(producerId);

		assertThrows(ServiceException.class, () -> goodsService.saveGood(good),
				"ServiceException expected when saving good already exists");
	}

	@Test
	public void whenSaveGoodNotSavedAndNullReturned() {
		Mockito.when(goodsDAOMock.findByNameAndSortAndCategoryIdAndProducerId(
				Mockito.argThat(name -> (name.length() >= 3) && (name.length() <= 30)),
				Mockito.argThat(sort -> (sort.length() >= 3) && (sort.length() <= 30)),
				Mockito.argThat(cId -> cId > 0L), Mockito.argThat(pId -> pId > 0L))).thenReturn(null);

		final Long categoryId = 1L;
		final Long producerId = 1L;
		Good good = new Good(null, "Яблоко", "Черныш", "Спелое", new Category(), new Producer(), new Storehouse());
		good.getCategory().setId(categoryId);
		good.getProducer().setId(producerId);

		Mockito.when(goodsDAOMock.save(good)).thenReturn(null);

		assertThrows(ServiceException.class, () -> goodsService.saveGood(good),
				"ServiceException expected when good was not saved and null was returned");
	}

	@Test
	public void whenSaveGoodNotSavedAndNullGoodIdReturned() {
		Mockito.when(goodsDAOMock.findByNameAndSortAndCategoryIdAndProducerId(
				Mockito.argThat(name -> (name.length() >= 3) && (name.length() <= 30)),
				Mockito.argThat(sort -> (sort.length() >= 3) && (sort.length() <= 30)),
				Mockito.argThat(cId -> cId > 0L), Mockito.argThat(pId -> pId > 0L))).thenReturn(null);

		final Long nullId = null;
		final Long categoryId = 1L;
		final Long producerId = 1L;
		Good good = new Good(nullId, "Яблоко", "Черныш", "Спелое", new Category(), new Producer(), new Storehouse());
		good.getCategory().setId(categoryId);
		good.getProducer().setId(producerId);

		Mockito.when(goodsDAOMock.save(good)).thenAnswer(invocation -> invocation.getArguments()[0]);

		assertThrows(ServiceException.class, () -> goodsService.saveGood(good),
				"ServiceException expected when good was not saved and null good id was returned");
	}

	@Test
	public void whenSaveGoodNotSavedAndBadGoodIdReturned() {
		Mockito.when(goodsDAOMock.findByNameAndSortAndCategoryIdAndProducerId(
				Mockito.argThat(name -> (name.length() >= 3) && (name.length() <= 30)),
				Mockito.argThat(sort -> (sort.length() >= 3) && (sort.length() <= 30)),
				Mockito.argThat(cId -> cId > 0L), Mockito.argThat(pId -> pId > 0L))).thenReturn(null);

		final Long badId = 0L;
		final Long categoryId = 1L;
		final Long producerId = 1L;
		Good good = new Good(null, "Яблоко", "Черныш", "Спелое", new Category(), new Producer(), new Storehouse());
		good.getCategory().setId(categoryId);
		good.getProducer().setId(producerId);

		Mockito.when(goodsDAOMock.save(good)).thenAnswer(invocation -> {
			Good savedGood = invocation.getArgument(0, Good.class);
			savedGood.setId(badId);
			return savedGood;
		});

		assertThrows(ServiceException.class, () -> goodsService.saveGood(good),
				"ServiceException expected when good was not saved and bad good id was returned");
	}

	@Test
	public void whenUpdatingGood() throws ServiceException {
		Long id = 2L;
		Long categoryId = 1L;
		Long producerId = 1L;

		String updatedGoodSort = "Черныш";

		Good goood = new Good(id, "Яблоко", "Ставр", "Спелое", new Category(), new Producer(), new Storehouse());
		goood.getCategory().setId(categoryId);
		goood.getProducer().setId(producerId);

		Mockito.when(goodsDAOMock.existsById(id)).thenReturn(true);
		Mockito.when(goodsDAOMock.save(goood)).thenAnswer(invocation -> {
			Good updatedGood = invocation.getArgument(0, Good.class);
			updatedGood.setSort(updatedGoodSort);
			return updatedGood;
		});

		Good updatedGood = goodsService.updateGood(goood);

		assertNotNull(updatedGood.getId());
		assertTrue(updatedGood.getId() > 0L);
		assertEquals(updatedGoodSort, updatedGood.getSort());
	}
	
	@Test
	public void whenUpdatingGoodOnNullId() {
		Long nullId = null;
		Good goood = new Good(nullId, "Яблоко", "Ставр", "Спелое", new Category(), new Producer(), new Storehouse());
		
		assertThrows(ServiceException.class, () -> goodsService.updateGood(goood), "ServiceException expected when trying to update good with null id");
	}
	
	@Test
	public void whenUpdatingGoodOnBadId() {
		Long badId = 0L;
		Good goood = new Good(badId, "Яблоко", "Ставр", "Спелое", new Category(), new Producer(), new Storehouse());
		
		assertThrows(ServiceException.class, () -> goodsService.updateGood(goood), "ServiceException expected when trying to update good with bad id");
	}
	
	@Test
	public void whenUpdatingGoodNotExistst() {
		Long id = 1200L;
		Good good = new Good(id, "Яблоко", "Ставр", "Спелое", new Category(), new Producer(), new Storehouse());
		
		Mockito.when(goodsDAOMock.existsById(id)).thenReturn(false);
		
		assertThrows(ServiceException.class, () -> goodsService.updateGood(good), "ServiceException expected when trying to update good that not exists");		
	}
	
	@Test
	public void whenUpdatingGoodReturnsNull() {
		Long id = 12L;
		Good good = new Good(id, "Яблоко", "Ставр", "Спелое", new Category(), new Producer(), new Storehouse());
		
		Mockito.when(goodsDAOMock.existsById(id)).thenReturn(true);
		Mockito.when(goodsDAOMock.save(good)).thenReturn(null);
		
		assertThrows(ServiceException.class, () -> goodsService.updateGood(good), "ServiceException expected when updating good returns null");		
	}
	
	@Test
	public void whenUpdatingGoodReturnsGoodWithNullId() {
		Long id = 12L;
		Good good = new Good(id, "Яблоко", "Ставр", "Спелое", new Category(), new Producer(), new Storehouse());
		
		Mockito.when(goodsDAOMock.existsById(id)).thenReturn(true);
		Mockito.when(goodsDAOMock.save(good)).thenReturn(new Good());
		
		assertThrows(ServiceException.class, () -> goodsService.updateGood(good), "ServiceException expected when updating good returns good with null id");		
	}
	
	@Test
	public void whenUpdatingGoodReturnsGoodWithBadId() {
		Long id = 12L;
		Good good = new Good(id, "Яблоко", "Ставр", "Спелое", new Category(), new Producer(), new Storehouse());
		
		Mockito.when(goodsDAOMock.existsById(id)).thenReturn(true);
		
		Long badReturningId = -1L;
		Mockito.when(goodsDAOMock.save(good)).thenReturn(new Good(badReturningId, "Яблоко", "Ставр", "Спелое", new Category(), new Producer(), new Storehouse()));
		
		assertThrows(ServiceException.class, () -> goodsService.updateGood(good), "ServiceException expected when updating good returns good with bad id");		
	}
	
	@Test
	public void whenDeletingGood() {
		Long id = 18L;
		
		Mockito.when(goodsDAOMock.existsById(id)).thenReturn(true);
		Mockito.doNothing().when(goodsDAOMock).deleteById(id);
		
		assertDoesNotThrow(() -> goodsService.deleteGood(id), "ServiceException not expected when deleting good");
	}
	
	@Test
	public void whenDeletingGoodOnNullId() {
		Long id = null;
			
		assertThrows(ServiceException.class, () -> goodsService.deleteGood(id), "ServiceException expected when deleting good on null id");
	}
	
	@Test
	public void whenDeletingGoodOnNonExistingId() {
		Long id = -1L;
		
		Mockito.when(goodsDAOMock.existsById(Mockito.anyLong())).thenReturn(false);
			
		assertThrows(ServiceException.class, () -> goodsService.deleteGood(id), "ServiceException expected when deleting good on non existing id");
	}
}

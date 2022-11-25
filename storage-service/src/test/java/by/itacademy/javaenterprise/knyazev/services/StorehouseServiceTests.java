package by.itacademy.javaenterprise.knyazev.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import by.itacademy.javaenterprise.knyazev.dao.StorehouseDAO;
import by.itacademy.javaenterprise.knyazev.entities.Good;
import by.itacademy.javaenterprise.knyazev.entities.Storehouse;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;
import by.itacademy.javaenterprise.knyazev.utils.Unit;

public class StorehouseServiceTests {
	private StorehouseDAO storehouseDAOMock;
	private StorehouseService storehouseService;

	@BeforeEach
	public void setUpBeforeEachTest() {
		storehouseDAOMock = Mockito.mock(StorehouseDAO.class);
		storehouseService = new StorehouseService(storehouseDAOMock);

	}

	@Test
	public void whenShowAllStorehouses() {
		List<Storehouse> storehouses = new ArrayList<>();
		storehouses.add(new Storehouse(1L, new Good(), "1522555", LocalDateTime.now(), Unit.т, new BigDecimal(12.300),
				new BigDecimal(5.5)));

		Mockito.when(storehouseDAOMock.findAll()).thenReturn(storehouses);

		assertEquals(1, storehouseService.showAll().size());
	}

	@Test
	public void whenShowAllStorehousesWithPageAndSize() throws ServiceException {
		Integer page = 1;
		Integer size = 2;

		List<Storehouse> storehouses = new ArrayList<>();
		storehouses.add(new Storehouse(1L, new Good(), "1522555", LocalDateTime.now(), Unit.т, new BigDecimal(12.300),
				new BigDecimal(5.5)));
		storehouses.add(new Storehouse(2L, new Good(), "1522556", LocalDateTime.now(), Unit.т, new BigDecimal(10.800),
				new BigDecimal(6.3)));

		Mockito.when(storehouseDAOMock.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(storehouses));

		assertEquals(2, storehouseService.showAll(page, size).size());
	}

	@Test
	public void whenShowAllStorehousesWithNullPageAndSize() throws ServiceException {
		Integer page = null;
		Integer size = 2;

		assertThrows(ServiceException.class, () -> storehouseService.showAll(page, size),
				"Service exception expected when null page was given");
	}

	@Test
	public void whenShowAllStorehousesWithBadPageAndSize() throws ServiceException {
		Integer page = 0;
		Integer size = 2;

		assertThrows(ServiceException.class, () -> storehouseService.showAll(page, size),
				"Service exception expected when bad page was given");
	}

	@Test
	public void whenShowAllStorehousesWithPageAndNullSize() throws ServiceException {
		Integer page = 1;
		Integer size = null;

		assertThrows(ServiceException.class, () -> storehouseService.showAll(page, size),
				"Service exception expected when null size was given");
	}

	@Test
	public void whenShowAllStorehousesWithPageAndBadSize() throws ServiceException {
		Integer page = 1;
		Integer size = 0;

		assertThrows(ServiceException.class, () -> storehouseService.showAll(page, size),
				"Service exception expected when bad size was given");
	}

	@Test
	public void whenShowAllStorehousesWhenEmpty() throws ServiceException {
		Integer page = 1;
		Integer size = 2;

		List<Storehouse> storehouses = new ArrayList<>();

		Mockito.when(storehouseDAOMock.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(storehouses));
		assertThrows(ServiceException.class, () -> storehouseService.showAll(page, size),
				"Service exception expected when empty list of storehouses returned");
	}

	@Test
	public void whenShowStorehouseUnitById() throws ServiceException {
		Long id = 5L;

		Storehouse storehouse = new Storehouse(id, new Good(), "1522555", LocalDateTime.now(), Unit.т,
				new BigDecimal(12.300), new BigDecimal(5.5));

		Mockito.when(storehouseDAOMock.findById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(Optional.of(storehouse));

		assertEquals(id, storehouseService.showStorehouseUnitById(id).getId());
	}

	@Test
	public void whenShowStorehouseUnitByNullId() throws ServiceException {
		Long id = null;

		assertThrows(ServiceException.class, () -> storehouseService.showStorehouseUnitById(id),
				"Service exception expected when showing storehouse on null id");
	}

	@Test
	public void whenShowStorehouseUnitByIdIsEmpty() throws ServiceException {
		Long id = 5L;

		Mockito.when(storehouseDAOMock.findById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(Optional.empty());

		assertThrows(ServiceException.class, () -> storehouseService.showStorehouseUnitById(id),
				"Service exception expected when showing storehouse returns empty storehouse");
	}

	@Test
	public void whenSaveStorehouse() throws ServiceException {
		Long storehouseId = null;
		Storehouse storehouse = new Storehouse(storehouseId, new Good(), "1522555", LocalDateTime.now(), Unit.т,
				new BigDecimal(12.300), new BigDecimal(5.5));
		Long goodId = 2L;
		storehouse.getGood().setId(goodId);

		Mockito.when(storehouseDAOMock.findByGoodId(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(null);

		final Long savedId = 18L;
		Mockito.when(storehouseDAOMock.save(storehouse)).thenAnswer(invocation -> {			
			Storehouse savedStorehouse = invocation.getArgument(0, Storehouse.class);
			savedStorehouse.setId(savedId);
			return storehouse;
		});

		Storehouse savedStorehouse = storehouseService.saveStorehouse(storehouse);

		assertNotNull(savedStorehouse.getId());
		assertTrue(savedStorehouse.getId() > 0L);
		assertEquals(savedId, savedStorehouse.getId());
	}

	@Test
	public void whenSaveOnNullStorehouse() throws ServiceException {
		Storehouse storehouse = null;

		assertThrows(ServiceException.class, () -> storehouseService.saveStorehouse(storehouse),
				"Service exception expected when saving on null storehouse");
	}

	@Test
	public void whenSaveOnNonNullStorehouseId() throws ServiceException {
		Long id = 2L;
		Storehouse storehouse = new Storehouse(id, new Good(), "1522555", LocalDateTime.now(), Unit.т,
				new BigDecimal(12.300), new BigDecimal(5.5));

		assertThrows(ServiceException.class, () -> storehouseService.saveStorehouse(storehouse),
				"Service exception expected when saving storehouse on null id");
	}

	@Test
	public void whenSavingStorehouseAlreadyExistsWithUniqueGood() {

		Mockito.when(storehouseDAOMock.findByGoodId(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(new Storehouse());

		Long storehouseId = null;
		Storehouse storehouse = new Storehouse(storehouseId, new Good(), "1522555", LocalDateTime.now(), Unit.т,
				new BigDecimal(12.300), new BigDecimal(5.5));
		Long goodId = 2L;
		storehouse.getGood().setId(goodId);
		
		assertThrows(ServiceException.class, () -> storehouseService.saveStorehouse(storehouse), "Service exception expected when saving storehouse already exists with unique good");
	}
	
	@Test
	public void whenSaveStorehouseReturnsNull() {
		Mockito.when(storehouseDAOMock.findByGoodId(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L))))
		.thenReturn(null);
		Mockito.when(storehouseDAOMock.save(Mockito.any(Storehouse.class))).thenReturn(null);
		
		Long storehouseId = null;
		Storehouse storehouse = new Storehouse(storehouseId, new Good(), "1522555", LocalDateTime.now(), Unit.т,
				new BigDecimal(12.300), new BigDecimal(5.5));
		Long goodId = 2L;
		storehouse.getGood().setId(goodId);
		
		assertThrows(ServiceException.class, () -> storehouseService.saveStorehouse(storehouse), "Service exception expected when save storehouse returns null");
	}
	
	@Test
	public void whenSaveStorehouseReturnsStorehouseWithNullId() {
		Mockito.when(storehouseDAOMock.findByGoodId(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L))))
		.thenReturn(null);
		Mockito.when(storehouseDAOMock.save(Mockito.any(Storehouse.class))).thenReturn(new Storehouse());
		
		Long storehouseId = null;
		Storehouse storehouse = new Storehouse(storehouseId, new Good(), "1522555", LocalDateTime.now(), Unit.т,
				new BigDecimal(12.300), new BigDecimal(5.5));
		Long goodId = 2L;
		storehouse.getGood().setId(goodId);
		
		assertThrows(ServiceException.class, () -> storehouseService.saveStorehouse(storehouse), "Service exception expected when save storehouse returns storehouse with null id");
	}
	
	@Test
	public void whenSaveStorehouseReturnsStorehouseWithBadId() {
		Mockito.when(storehouseDAOMock.findByGoodId(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L))))
		.thenReturn(null);
		Mockito.when(storehouseDAOMock.save(Mockito.any(Storehouse.class))).thenAnswer(invocation -> {
			Long savedId = 0L;			
			Storehouse savedStorehouse = invocation.getArgument(0, Storehouse.class);
			savedStorehouse.setId(savedId);		
			return savedStorehouse;
		});
		
		Long storehouseId = null;
		Storehouse storehouse = new Storehouse(storehouseId, new Good(), "1522555", LocalDateTime.now(), Unit.т,
				new BigDecimal(12.300), new BigDecimal(5.5));
		Long goodId = 2L;
		storehouse.getGood().setId(goodId);
		
		assertThrows(ServiceException.class, () -> storehouseService.saveStorehouse(storehouse), "Service exception expected when save storehouse returns storehouse with bad id");
	}
	
	@Test
	public void whenUpdateStorehouse() throws ServiceException {
		final BigDecimal updatedQuantity = new BigDecimal(128.300);
		
		Mockito.when(storehouseDAOMock.existsById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)))).thenReturn(true);
		Mockito.when(storehouseDAOMock.save(Mockito.any(Storehouse.class))).thenAnswer(invocation -> {
			Storehouse updatedStorhouse = invocation.getArgument(0, Storehouse.class);
			updatedStorhouse.setQuantity(updatedQuantity);
			return updatedStorhouse;
		});
		
		Long id = 5L;
		Storehouse storehouse = new Storehouse(id, new Good(), "256987", LocalDateTime.now(), Unit.т, new BigDecimal(120.250), new BigDecimal(6.35));
		
		assertEquals(updatedQuantity, storehouseService.updateStorehouse(storehouse).getQuantity());
	}
	
	@Test
	public void whenUpdateOnNullStorehouse() {
		Storehouse storehouse = null;
		
		assertThrows(ServiceException.class, () -> storehouseService.updateStorehouse(storehouse), "Service exception expected when updating on null storehouse");
	}
	
	@Test
	public void whenUpdateOnNullStorehouseId() {
		Long id = null;
		
		Storehouse storehouse = new Storehouse();
		storehouse.setId(id);
		
		assertThrows(ServiceException.class, () -> storehouseService.updateStorehouse(storehouse), "Service exception expected when updating on null storehouse id");
	}
	
	@Test
	public void whenUpdateOnBadStorehouseId() {
		Long id = 0L;
		
		Storehouse storehouse = new Storehouse();
		storehouse.setId(id);
		
		assertThrows(ServiceException.class, () -> storehouseService.updateStorehouse(storehouse), "Service exception expected when updating on null storehouse id");
	}

	
	@Test
	public void whenUpdateNonExistingStorehouse() {
		final Long maxStorehouseIdInDB = 250L;
		
		Mockito.when(storehouseDAOMock.existsById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L) && (idVal <= maxStorehouseIdInDB)))).thenReturn(true);
		
		final Long nonExistingIdInDB = maxStorehouseIdInDB + 1l;		
		Storehouse storehouse = new Storehouse(nonExistingIdInDB, new Good(), "1522555", LocalDateTime.now(), Unit.т,
				new BigDecimal(12.300), new BigDecimal(5.5));
		
		assertThrows(ServiceException.class, () -> storehouseService.updateStorehouse(storehouse), "Service exception expected when updating storehouse not exists in database");
	}
	
	@Test
	public void whenUpdateStorehouseReturnsNull() {
		Storehouse updatedStorehouse = null;
		
		Mockito.when(storehouseDAOMock.existsById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)))).thenReturn(true);
		Mockito.when(storehouseDAOMock.save(Mockito.any(Storehouse.class))).thenReturn(updatedStorehouse);
		
		Long id = 5L;
		Storehouse storehouse = new Storehouse(id, new Good(), "256987", LocalDateTime.now(), Unit.т, new BigDecimal(120.250), new BigDecimal(6.35));
	
		assertThrows(ServiceException.class, () -> storehouseService.updateStorehouse(storehouse), "Service exception expected when updating storehouse and returns null storehouse");
	}
	
	@Test
	public void whenUpdateStorehouseAndReturnsStorehouseWithNullId() {
		final Long updatedStoreHouseId = null;
		
		Mockito.when(storehouseDAOMock.existsById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)))).thenReturn(true);
		Mockito.when(storehouseDAOMock.save(Mockito.any(Storehouse.class))).thenAnswer(invocation -> {
			Storehouse updatStorehouse = invocation.getArgument(0, Storehouse.class);
			updatStorehouse.setId(updatedStoreHouseId);
			return updatStorehouse;
		});
		
		Long id = 5L;
		Storehouse storehouse = new Storehouse(id, new Good(), "256987", LocalDateTime.now(), Unit.т, new BigDecimal(120.250), new BigDecimal(6.35));
	
		assertThrows(ServiceException.class, () -> storehouseService.updateStorehouse(storehouse), "Service exception expected when updating storehouse and returns storehouse with null id");
	}
	
	@Test
	public void whenUpdateStorehouseAndReturnsStorehouseWithBadId() {
		final Long updatedStoreHouseId = 0L;
		
		Mockito.when(storehouseDAOMock.existsById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)))).thenReturn(true);
		Mockito.when(storehouseDAOMock.save(Mockito.any(Storehouse.class))).thenAnswer(invocation -> {
			Storehouse updatStorehouse = invocation.getArgument(0, Storehouse.class);
			updatStorehouse.setId(updatedStoreHouseId);
			return updatStorehouse;
		});
		
		Long id = 5L;
		Storehouse storehouse = new Storehouse(id, new Good(), "256987", LocalDateTime.now(), Unit.т, new BigDecimal(120.250), new BigDecimal(6.35));
	
		assertThrows(ServiceException.class, () -> storehouseService.updateStorehouse(storehouse), "Service exception expected when updating storehouse and returns storehouse with bad id");
	}
	
	@Test
	public void whenDeleteStorehouse() {
		Mockito.when(storehouseDAOMock.existsById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)))).thenReturn(true);
		Mockito.doNothing().when(storehouseDAOMock).delete(Mockito.any(Storehouse.class));
		
		Long storehouseId = 25L;
		assertDoesNotThrow(() -> storehouseService.deleteStorehouse(storehouseId), "Exception not expected, storehouse must be successfully deleted");
	}
	
	@Test
	public void whenDeleteStorehouseOnNullId() {
		final Long deletedId = null;
		
		assertThrows(ServiceException.class, () -> storehouseService.deleteStorehouse(deletedId), "Service exception expected when deleting storehouse on null id");
	}
	
	@Test
	public void whenDeleteStorehouseOnBadId() {
		final Long deletedId = 0L;
		
		assertThrows(ServiceException.class, () -> storehouseService.deleteStorehouse(deletedId), "Service exception expected when deleting storehouse on bad id");
	}
	
}
package by.itacademy.javaenterprise.knyazev.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import by.itacademy.javaenterprise.knyazev.dao.PurchasesDAO;
import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.entities.Good;
import by.itacademy.javaenterprise.knyazev.entities.Producer;
import by.itacademy.javaenterprise.knyazev.entities.Purchase;
import by.itacademy.javaenterprise.knyazev.entities.Role;
import by.itacademy.javaenterprise.knyazev.entities.Storehouse;
import by.itacademy.javaenterprise.knyazev.entities.User;
import by.itacademy.javaenterprise.knyazev.security.SecurityUser;
import by.itacademy.javaenterprise.knyazev.security.converters.UsersConverter;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;
import by.itacademy.javaenterprise.knyazev.utils.RoleType;
import by.itacademy.javaenterprise.knyazev.utils.Status;
import by.itacademy.javaenterprise.knyazev.utils.Unit;

public class PurchasesServiceTests {
	private SecurityUsersService securityUsersServiceMock;
	private StorehouseService storehouseServiceMock;
	private PurchasesDAO purchasesDAOMock;

	private PurchasesService purchasesService;

	@BeforeEach
	public void setUpBeforeEachTest() {
		securityUsersServiceMock = Mockito.mock(SecurityUsersService.class);
		storehouseServiceMock = Mockito.mock(StorehouseService.class);
		purchasesDAOMock = Mockito.mock(PurchasesDAO.class);

		purchasesService = new PurchasesService(securityUsersServiceMock, storehouseServiceMock, purchasesDAOMock);
	}

	@Test
	public void whenShowReserved() throws ServiceException {
		Long userId = 2L;
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(RoleType.ROLE_USER.name()));
		SecurityUser securityUser = new SecurityUser(userId, "Vano", "HASHED_VANO_PASSWORD", "Vano@mail.ru",
				Set.of(new Role(1L, RoleType.ROLE_USER)), true, grantedAuthorities);

		Long purchaseId = 3L;
		List<Purchase> purchases = new ArrayList<>();
		purchases.add(new Purchase(purchaseId, new User(), new Storehouse(), LocalDateTime.now(), Unit.кг,
				new BigDecimal(125.128), new BigDecimal(6.8), Status.reserved));

		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(purchasesDAOMock.findByUserIdAndStatus(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)),
				Mockito.any(Status.class))).thenReturn(purchases);

		assertEquals(purchaseId, purchasesService.showReserved().get(0).getId());
	}

	@Test
	public void whenShowReservedReturnsNull() throws ServiceException {
		Long userId = 2L;
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(RoleType.ROLE_USER.name()));
		SecurityUser securityUser = new SecurityUser(userId, "Vano", "HASHED_VANO_PASSWORD", "Vano@mail.ru",
				Set.of(new Role(1L, RoleType.ROLE_USER)), true, grantedAuthorities);

		List<Purchase> purchases = null;

		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(purchasesDAOMock.findByUserIdAndStatus(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)),
				Mockito.any(Status.class))).thenReturn(purchases);

		assertThrows(ServiceException.class, () -> purchasesService.showReserved(),
				"Service exception expected when show reserved returns null purchases");
	}

	@Test
	public void whenShowReservedReturnsEmpty() throws ServiceException {
		Long userId = 2L;
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(RoleType.ROLE_USER.name()));
		SecurityUser securityUser = new SecurityUser(userId, "Vano", "HASHED_VANO_PASSWORD", "Vano@mail.ru",
				Set.of(new Role(1L, RoleType.ROLE_USER)), true, grantedAuthorities);

		List<Purchase> purchases = new ArrayList<>();

		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(purchasesDAOMock.findByUserIdAndStatus(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)),
				Mockito.any(Status.class))).thenReturn(purchases);

		assertThrows(ServiceException.class, () -> purchasesService.showReserved(),
				"Service exception expected when show reserved returns empty list of purchases");
	}

	@Test
	public void whenShowAllBoughtForReport() throws ServiceException {
		Long purchaseId = 3L;
		List<Purchase> purchases = new ArrayList<>();
		purchases.add(new Purchase(purchaseId, new User(), new Storehouse(), LocalDateTime.now(), Unit.кг,
				new BigDecimal(125.128), new BigDecimal(6.8), Status.reserved));

		Mockito.when(purchasesDAOMock.findByStatus(Mockito.any(Status.class))).thenReturn(purchases);

		assertEquals(purchaseId, purchasesService.showAllBoughtForReport().get(0).getId());
	}

	@Test
	public void whenShowAllBoughtForReportReturnsNull() throws ServiceException {
		List<Purchase> purchases = null;

		Mockito.when(purchasesDAOMock.findByStatus(Mockito.any(Status.class))).thenReturn(purchases);

		assertThrows(ServiceException.class, () -> purchasesService.showAllBoughtForReport(),
				"Service exception expected when show bought for report returns null purchases");
	}

	@Test
	public void whenShowAllBoughtForReportReturnsEmptyList() throws ServiceException {
		List<Purchase> purchases = new ArrayList<>();

		Mockito.when(purchasesDAOMock.findByStatus(Mockito.any(Status.class))).thenReturn(purchases);

		assertThrows(ServiceException.class, () -> purchasesService.showAllBoughtForReport(),
				"Service exception expected when show bought for report returns empty list of purchases");
	}

	@Test
	public void whenReservePurchase() throws ServiceException {
		BigDecimal goodQuantityTest = new BigDecimal(25.326);
		BigDecimal goodPriceTest = new BigDecimal(7.54);
		Unit goodUnitTest = Unit.кг;
		Long purchaseIdTest = 8L;

		Good goodInStorehouse = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseDB = new Storehouse(1L, goodInStorehouse, "298855", LocalDateTime.now(), goodUnitTest,
				goodQuantityTest, goodPriceTest);
		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru", Set.of(new Role()),
				true, Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(storehouseServiceMock
				.showStorehouseUnitById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(storehouseDB);
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(purchasesDAOMock.save(Mockito.any(Purchase.class))).thenAnswer((invocation) -> {
			Purchase savedPurchase = invocation.getArgument(0, Purchase.class);
			savedPurchase.setId(purchaseIdTest);
			savedPurchase.setStatus(Status.reserved);
			return savedPurchase;
		});

		Good goodInStorehouseTest = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseTest = new Storehouse(1L, goodInStorehouseTest, "298855", LocalDateTime.now(),
				goodUnitTest, goodQuantityTest, goodPriceTest);
		Purchase purchase = new Purchase(null, new UsersConverter().convertFromTwoToOne(securityUser), storehouseTest,
				LocalDateTime.now(), goodUnitTest, goodQuantityTest, goodPriceTest, null);

		assertEquals(purchaseIdTest, purchasesService.reservePurchase(purchase).getId());
	}

	@Test
	public void whenReservePurchaseNull() {
		Purchase purchase = null;

		assertThrows(ServiceException.class, () -> purchasesService.reservePurchase(purchase),
				"Service exception expected when null purchase was given");
	}

	@Test
	public void whenReservePurchaseWithBadId() {
		Long purchaseBadId = 8L;
		Purchase purchase = new Purchase();
		purchase.setId(purchaseBadId);

		assertThrows(ServiceException.class, () -> purchasesService.reservePurchase(purchase),
				"Service exception expected when purchase was given with bad id");
	}

	@Test
	public void whenReservePurchaseWithNullStorehouse() {
		Long purchaseId = null;
		Purchase purchase = new Purchase();
		purchase.setId(purchaseId);
		Storehouse storehouse = null;
		purchase.setStorehouse(storehouse);

		assertThrows(ServiceException.class, () -> purchasesService.reservePurchase(purchase),
				"Service exception expected when purchase was given with null storehouse");
	}

	@Test
	public void whenReservePurchaseWithUnexistingGood() throws ServiceException {
		BigDecimal goodQuantityTest = new BigDecimal(25.326);
		BigDecimal goodPriceTest = new BigDecimal(7.54);
		Unit goodUnitTest = Unit.кг;

		Good goodInStorehouse = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseDB = new Storehouse(1L, goodInStorehouse, "298855", LocalDateTime.now(), goodUnitTest,
				goodQuantityTest, goodPriceTest);
		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru", Set.of(new Role()),
				true, Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(storehouseServiceMock
				.showStorehouseUnitById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(storehouseDB);

		Long anotherGoodId = 2L;
		Good goodInStorehouseTest = new Good(anotherGoodId, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseTest = new Storehouse(1L, goodInStorehouseTest, "298855", LocalDateTime.now(),
				goodUnitTest, goodQuantityTest, goodPriceTest);
		Purchase purchase = new Purchase(null, new UsersConverter().convertFromTwoToOne(securityUser), storehouseTest,
				LocalDateTime.now(), goodUnitTest, goodQuantityTest, goodPriceTest, null);

		assertThrows(ServiceException.class, () -> purchasesService.reservePurchase(purchase),
				"Service exception expected when good in purchase is not exists storehouse");
	}

	@Test
	public void whenReservePurchaseWithNonEqualsUnits() throws ServiceException {
		BigDecimal goodQuantityTest = new BigDecimal(25.326);
		BigDecimal goodPriceTest = new BigDecimal(7.54);
		Unit goodUnitTest = Unit.кг;

		Good goodInStorehouse = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseDB = new Storehouse(1L, goodInStorehouse, "298855", LocalDateTime.now(), goodUnitTest,
				goodQuantityTest, goodPriceTest);
		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru", Set.of(new Role()),
				true, Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(storehouseServiceMock
				.showStorehouseUnitById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(storehouseDB);

		Good goodInStorehouseTest = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Unit goodUnit = Unit.шт;
		Storehouse storehouseTest = new Storehouse(1L, goodInStorehouseTest, "298855", LocalDateTime.now(),
				goodUnitTest, goodQuantityTest, goodPriceTest);
		Purchase purchase = new Purchase(null, new UsersConverter().convertFromTwoToOne(securityUser), storehouseTest,
				LocalDateTime.now(), goodUnit, goodQuantityTest, goodPriceTest, null);

		assertThrows(ServiceException.class, () -> purchasesService.reservePurchase(purchase),
				"Service exception expected when unit in purchase is not exists storehouse");
	}

	@Test
	public void whenReservePurchaseWithNonEqualsPrice() throws ServiceException {
		BigDecimal goodQuantityTest = new BigDecimal(25.326);
		BigDecimal goodPriceTest = new BigDecimal(7.54);

		Good goodInStorehouse = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseDB = new Storehouse(1L, goodInStorehouse, "298855", LocalDateTime.now(), Unit.кг,
				goodQuantityTest, goodPriceTest);

		Mockito.when(storehouseServiceMock
				.showStorehouseUnitById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(storehouseDB);

		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru", Set.of(new Role()),
				true, Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));
		Good good = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(), new Producer(), null);
		Storehouse storehouseTest = new Storehouse(1L, good, "298855", LocalDateTime.now(), Unit.кг, goodQuantityTest,
				goodPriceTest);
		BigDecimal purchasePrice = new BigDecimal(6.55);
		Purchase purchase = new Purchase(null, new UsersConverter().convertFromTwoToOne(securityUser), storehouseTest,
				LocalDateTime.now(), Unit.кг, goodQuantityTest, purchasePrice, null);

		assertThrows(ServiceException.class, () -> purchasesService.reservePurchase(purchase),
				"Service exception expected when price in purchase is not exists storehouse");
	}

	@Test
	public void whenReservePurchaseWithBadQuantity() throws ServiceException {
		BigDecimal goodQuantityTest = new BigDecimal(25.326);
		BigDecimal goodPriceTest = new BigDecimal(7.54);

		Good goodInStorehouse = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseDB = new Storehouse(1L, goodInStorehouse, "298855", LocalDateTime.now(), Unit.кг,
				goodQuantityTest, goodPriceTest);

		Mockito.when(storehouseServiceMock
				.showStorehouseUnitById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(storehouseDB);

		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru", Set.of(new Role()),
				true, Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));
		Good good = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(), new Producer(), null);
		Storehouse storehouseTest = new Storehouse(1L, good, "298855", LocalDateTime.now(), Unit.кг, goodQuantityTest,
				goodPriceTest);
		BigDecimal purchaseQuantity = new BigDecimal(26.55);
		Purchase purchase = new Purchase(null, new UsersConverter().convertFromTwoToOne(securityUser), storehouseTest,
				LocalDateTime.now(), Unit.кг, purchaseQuantity, goodPriceTest, null);

		assertThrows(ServiceException.class, () -> purchasesService.reservePurchase(purchase),
				"Service exception expected when good quantity in purchase is exceeded that exists in storehouse");
	}

	@Test
	public void whenReservePurchaseReturnsNull() throws ServiceException {
		BigDecimal goodQuantityTest = new BigDecimal(25.326);
		BigDecimal goodPriceTest = new BigDecimal(7.54);
		Unit goodUnitTest = Unit.кг;

		Good goodInStorehouse = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseDB = new Storehouse(1L, goodInStorehouse, "298855", LocalDateTime.now(), goodUnitTest,
				goodQuantityTest, goodPriceTest);
		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru", Set.of(new Role()),
				true, Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));
		Purchase purchaseTest = null;

		Mockito.when(storehouseServiceMock
				.showStorehouseUnitById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(storehouseDB);
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(purchasesDAOMock.save(Mockito.any(Purchase.class))).thenReturn(purchaseTest);

		Good goodInStorehouseTest = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseTest = new Storehouse(1L, goodInStorehouseTest, "298855", LocalDateTime.now(),
				goodUnitTest, goodQuantityTest, goodPriceTest);
		Purchase purchase = new Purchase(null, new UsersConverter().convertFromTwoToOne(securityUser), storehouseTest,
				LocalDateTime.now(), goodUnitTest, goodQuantityTest, goodPriceTest, null);

		assertThrows(ServiceException.class, () -> purchasesService.reservePurchase(purchase),
				"Service exception expected when reserve purchase returns null");
	}

	@Test
	public void whenReservePurchaseReturnsPurchaseWithNullId() throws ServiceException {
		BigDecimal goodQuantityTest = new BigDecimal(25.326);
		BigDecimal goodPriceTest = new BigDecimal(7.54);
		Unit goodUnitTest = Unit.кг;
		Long nullPurchaseId = null;

		Good goodInStorehouse = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseDB = new Storehouse(1L, goodInStorehouse, "298855", LocalDateTime.now(), goodUnitTest,
				goodQuantityTest, goodPriceTest);
		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru", Set.of(new Role()),
				true, Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(storehouseServiceMock
				.showStorehouseUnitById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(storehouseDB);
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(purchasesDAOMock.save(Mockito.any(Purchase.class))).thenAnswer((invocation) -> {
			Purchase savedPurchase = invocation.getArgument(0, Purchase.class);
			savedPurchase.setId(nullPurchaseId);
			savedPurchase.setStatus(Status.reserved);
			return savedPurchase;
		});

		Good goodInStorehouseTest = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseTest = new Storehouse(1L, goodInStorehouseTest, "298855", LocalDateTime.now(),
				goodUnitTest, goodQuantityTest, goodPriceTest);
		Purchase purchase = new Purchase(null, new UsersConverter().convertFromTwoToOne(securityUser), storehouseTest,
				LocalDateTime.now(), goodUnitTest, goodQuantityTest, goodPriceTest, null);

		assertThrows(ServiceException.class, () -> purchasesService.reservePurchase(purchase),
				"Service exception expected when reserve purchase returns purchase with null id");
	}

	@Test
	public void whenReservePurchaseReturnsPurchaseWithBadId() throws ServiceException {
		BigDecimal goodQuantityTest = new BigDecimal(25.326);
		BigDecimal goodPriceTest = new BigDecimal(7.54);
		Unit goodUnitTest = Unit.кг;
		Long badPurchaseId = 0L;

		Good goodInStorehouse = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseDB = new Storehouse(1L, goodInStorehouse, "298855", LocalDateTime.now(), goodUnitTest,
				goodQuantityTest, goodPriceTest);
		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru", Set.of(new Role()),
				true, Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(storehouseServiceMock
				.showStorehouseUnitById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(storehouseDB);
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(purchasesDAOMock.save(Mockito.any(Purchase.class))).thenAnswer((invocation) -> {
			Purchase savedPurchase = invocation.getArgument(0, Purchase.class);
			savedPurchase.setId(badPurchaseId);
			savedPurchase.setStatus(Status.reserved);
			return savedPurchase;
		});

		Good goodInStorehouseTest = new Good(1L, "Груша", "Красный октябрь", "СладкиЙ груша", new Category(),
				new Producer(), null);
		Storehouse storehouseTest = new Storehouse(1L, goodInStorehouseTest, "298855", LocalDateTime.now(),
				goodUnitTest, goodQuantityTest, goodPriceTest);
		Purchase purchase = new Purchase(null, new UsersConverter().convertFromTwoToOne(securityUser), storehouseTest,
				LocalDateTime.now(), goodUnitTest, goodQuantityTest, goodPriceTest, null);

		assertThrows(ServiceException.class, () -> purchasesService.reservePurchase(purchase),
				"Service exception expected when reserve purchase returns purchase with bad id");
	}

	@Test
	public void whenBuyPurchase() throws ServiceException {
		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru",
				Set.of(new Role(1L, RoleType.ROLE_USER)), true,
				Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(purchasesDAOMock.findById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenAnswer(invocation -> {
					Good good = new Good(1L, "Яблоко", "Криворог", "СочныЙ", new Category(), new Producer(), null);
					Storehouse storehouse = new Storehouse(1L, good, "125689", LocalDateTime.now(), Unit.кг,
							new BigDecimal(55.289), new BigDecimal(5.89));

					Purchase purchase = new Purchase();

					purchase.setId(invocation.getArgument(0, Long.class));
					purchase.setUser(new UsersConverter().convertFromTwoToOne(securityUser));
					purchase.setStorehouse(storehouse);
					purchase.setDateTime(LocalDateTime.now());
					purchase.setUnit(Unit.кг);
					purchase.setQuantity(new BigDecimal(21.59));
					purchase.setPrice(new BigDecimal(5.89));
					purchase.setStatus(Status.reserved);

					Optional<Purchase> optionalPurchase = Optional.of(purchase);

					return optionalPurchase;
				});
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(purchasesDAOMock.save(Mockito.any(Purchase.class)))
				.thenAnswer(invocation -> invocation.getArgument(0, Purchase.class));

		Long purchaseId = 5L;
		Purchase purchase = new Purchase(purchaseId, new User(), new Storehouse(), LocalDateTime.now(), Unit.кг,
				new BigDecimal(5.89), new BigDecimal(122.59), Status.reserved);

		assertEquals(purchaseId, purchasesService.buyPurchase(purchase).getId());
		assertEquals(Status.bought, purchasesService.buyPurchase(purchase).getStatus());
	}

	@Test
	public void whenBuyPurchaseIsNull() {
		Purchase purchase = null;

		assertThrows(ServiceException.class, () -> purchasesService.buyPurchase(purchase),
				"Service exception expected when null purchase was given");
	}

	@Test
	public void whenBuyPurchaseHasNullId() {
		Purchase purchase = new Purchase();

		assertThrows(ServiceException.class, () -> purchasesService.buyPurchase(purchase),
				"Service exception expected when purchase with null id was given");
	}

	@Test
	public void whenBuyPurchaseHasBasId() {
		Long badId = 0L;
		Purchase purchase = new Purchase();
		purchase.setId(badId);

		assertThrows(ServiceException.class, () -> purchasesService.buyPurchase(purchase),
				"Service exception expected when purchase with bad id was given");
	}

	@Test
	public void whenBuyPurchaseDoesntContainPurchaseOnId() {
		Mockito.when(purchasesDAOMock.findById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.then(invocation -> {
					return Optional.empty();
				});

		Long purchaseId = 2L;
		Purchase purchase = new Purchase();
		purchase.setId(purchaseId);

		assertThrows(ServiceException.class, () -> purchasesService.buyPurchase(purchase),
				"Service exception expected when purchase with given id not found");
	}

	@Test
	public void whenBuyPurchaseAndSavedPurchaseHasNotReservedStatus() {
		Mockito.when(purchasesDAOMock.findById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.then(invocation -> {
					Status status = Status.bought;
					return Optional.of(new Purchase(1L, new User(), new Storehouse(), LocalDateTime.now(), Unit.кг,
							new BigDecimal(5.89), new BigDecimal(122.59), status));
				});

		Status status = Status.reserved;
		Purchase purchase = new Purchase(1L, new User(), new Storehouse(), LocalDateTime.now(), Unit.кг,
				new BigDecimal(5.89), new BigDecimal(122.59), status);

		assertThrows(ServiceException.class, () -> purchasesService.buyPurchase(purchase),
				"Service exception expected when purchase was given with not reserved status");
	}

	@Test
	public void whenBuyPurchaseThatNotPosssessedToCurrentUser() throws ServiceException {
		Mockito.when(purchasesDAOMock.findById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.then(invocation -> {
					User user = new User();
					user.setId(2L);
					return Optional.of(new Purchase(1L, user, new Storehouse(), LocalDateTime.now(), Unit.кг,
							new BigDecimal(5.89), new BigDecimal(122.59), Status.reserved));
				});

		Long securityUserId = 1L;
		SecurityUser securityUser = new SecurityUser(securityUserId, "Vano", "HASHED_PASSWORD", "Vano@mail.ru",
				Set.of(new Role(1L, RoleType.ROLE_USER)), true,
				Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);

		Long purchaseId = 3L;
		Purchase purchase = new Purchase();
		purchase.setId(purchaseId);

		assertThrows(ServiceException.class, () -> purchasesService.buyPurchase(purchase),
				"Service exception expected when given purchase not possessed to current user");
	}

	@Test
	public void WhenBuyPurchaseAndUpdatedPurchaseIsNull() throws ServiceException {
		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru",
				Set.of(new Role(1L, RoleType.ROLE_USER)), true,
				Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(purchasesDAOMock.findById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenAnswer(invocation -> {
					Good good = new Good(1L, "Яблоко", "Криворог", "СочныЙ", new Category(), new Producer(), null);
					Storehouse storehouse = new Storehouse(1L, good, "125689", LocalDateTime.now(), Unit.кг,
							new BigDecimal(55.289), new BigDecimal(5.89));

					Purchase purchase = new Purchase();

					purchase.setId(invocation.getArgument(0, Long.class));
					purchase.setUser(new UsersConverter().convertFromTwoToOne(securityUser));
					purchase.setStorehouse(storehouse);
					purchase.setDateTime(LocalDateTime.now());
					purchase.setUnit(Unit.кг);
					purchase.setQuantity(new BigDecimal(21.59));
					purchase.setPrice(new BigDecimal(5.89));
					purchase.setStatus(Status.reserved);

					Optional<Purchase> optionalPurchase = Optional.of(purchase);

					return optionalPurchase;
				});
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(purchasesDAOMock.save(Mockito.any(Purchase.class))).thenReturn(null);

		Long purchaseId = 5L;
		Purchase purchase = new Purchase(purchaseId, new User(), new Storehouse(), LocalDateTime.now(), Unit.кг,
				new BigDecimal(5.89), new BigDecimal(122.59), Status.reserved);

		assertThrows(ServiceException.class, () -> purchasesService.buyPurchase(purchase),
				"Service exception expected when buy (update) purchase returns null");
	}

	@Test
	public void WhenBuyPurchaseReturnPurchaseWithNullId() throws ServiceException {
		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru",
				Set.of(new Role(1L, RoleType.ROLE_USER)), true,
				Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(purchasesDAOMock.findById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenAnswer(invocation -> {
					Good good = new Good(1L, "Яблоко", "Криворог", "СочныЙ", new Category(), new Producer(), null);
					Storehouse storehouse = new Storehouse(1L, good, "125689", LocalDateTime.now(), Unit.кг,
							new BigDecimal(55.289), new BigDecimal(5.89));

					Purchase purchase = new Purchase();

					purchase.setId(invocation.getArgument(0, Long.class));
					purchase.setUser(new UsersConverter().convertFromTwoToOne(securityUser));
					purchase.setStorehouse(storehouse);
					purchase.setDateTime(LocalDateTime.now());
					purchase.setUnit(Unit.кг);
					purchase.setQuantity(new BigDecimal(21.59));
					purchase.setPrice(new BigDecimal(5.89));
					purchase.setStatus(Status.reserved);

					Optional<Purchase> optionalPurchase = Optional.of(purchase);

					return optionalPurchase;
				});
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(purchasesDAOMock.save(Mockito.any(Purchase.class))).thenAnswer(invocation -> {
			Purchase updatedPurchase = invocation.getArgument(0, Purchase.class);
			updatedPurchase.setId(null);
			return updatedPurchase;
		});

		Long purchaseId = 5L;
		Purchase purchase = new Purchase(purchaseId, new User(), new Storehouse(), LocalDateTime.now(), Unit.кг,
				new BigDecimal(5.89), new BigDecimal(122.59), Status.reserved);

		assertThrows(ServiceException.class, () -> purchasesService.buyPurchase(purchase),
				"Service exception expected when buy (update) purchase returns null");
	}

	@Test
	public void WhenBuyPurchaseReturnPurchaseWithBadId() throws ServiceException {
		SecurityUser securityUser = new SecurityUser(1L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru",
				Set.of(new Role(1L, RoleType.ROLE_USER)), true,
				Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(purchasesDAOMock.findById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenAnswer(invocation -> {
					Good good = new Good(1L, "Яблоко", "Криворог", "СочныЙ", new Category(), new Producer(), null);
					Storehouse storehouse = new Storehouse(1L, good, "125689", LocalDateTime.now(), Unit.кг,
							new BigDecimal(55.289), new BigDecimal(5.89));

					Purchase purchase = new Purchase();

					purchase.setId(invocation.getArgument(0, Long.class));
					purchase.setUser(new UsersConverter().convertFromTwoToOne(securityUser));
					purchase.setStorehouse(storehouse);
					purchase.setDateTime(LocalDateTime.now());
					purchase.setUnit(Unit.кг);
					purchase.setQuantity(new BigDecimal(21.59));
					purchase.setPrice(new BigDecimal(5.89));
					purchase.setStatus(Status.reserved);

					Optional<Purchase> optionalPurchase = Optional.of(purchase);

					return optionalPurchase;
				});
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(purchasesDAOMock.save(Mockito.any(Purchase.class))).thenAnswer(invocation -> {
			Purchase updatedPurchase = invocation.getArgument(0, Purchase.class);
			updatedPurchase.setId(0L);
			return updatedPurchase;
		});

		Long purchaseId = 5L;
		Purchase purchase = new Purchase(purchaseId, new User(), new Storehouse(), LocalDateTime.now(), Unit.кг,
				new BigDecimal(5.89), new BigDecimal(122.59), Status.reserved);

		assertThrows(ServiceException.class, () -> purchasesService.buyPurchase(purchase),
				"Service exception expected when buy (update) purchase returns null");
	}

	@Test
	public void WhenDeletePurchase() throws ServiceException {
		SecurityUser securityUser = new SecurityUser(3L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru",
				Set.of(new Role(1L, RoleType.ROLE_USER)), true,
				Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(purchasesDAOMock.findById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenAnswer(invocation -> {
					User user = new User();
					user.setId(3L);

					Storehouse storehouseDB = new Storehouse(1L, new Good(), "298855", LocalDateTime.now(), Unit.кг,
							new BigDecimal(1777.856), new BigDecimal(6.56));

					Purchase savedPurchase = new Purchase(invocation.getArgument(0, Long.class), user, storehouseDB,
							LocalDateTime.now(), Unit.кг, new BigDecimal(12.56), new BigDecimal(6.56), Status.reserved);

					return Optional.of(savedPurchase);
				});
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(storehouseServiceMock.updateStorehouse(Mockito.any(Storehouse.class)))
				.then(invocation -> invocation.getArgument(0, Storehouse.class));
		Mockito.doNothing().when(purchasesDAOMock)
				.deleteById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L)));

		assertDoesNotThrow(() -> purchasesService.deletePurchase(3L),
				"Service exception not expected when deleting on id");
	}

	@Test
	public void WhenDeletePurchaseOnNullId() {
		Long id = null;

		assertThrows(ServiceException.class, () -> purchasesService.deletePurchase(id),
				"Service exception expected when deleting on null id");
	}

	@Test
	public void WhenDeletePurchaseOnBadId() {
		Long id = 0L;

		assertThrows(ServiceException.class, () -> purchasesService.deletePurchase(id),
				"Service exception expected when deleting on bad id");
	}

	@Test
	public void WhenDeletePurchaseReturnsEmptyPurchase() {
		Long id = 3L;
		Mockito.when(purchasesDAOMock.findById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(Optional.empty());

		assertThrows(ServiceException.class, () -> purchasesService.deletePurchase(id),
				"Service exception expected when deleting and returns empty purchase");
	}

	@Test
	public void WhenDeletePurchaseWithBadUserId() throws ServiceException {
		Long id = 3L;

		Long securityUserId = 8L;
		SecurityUser securityUser = new SecurityUser(securityUserId, "Vano", "HASHED_PASSWORD", "Vano@mail.ru",
				Set.of(new Role(1L, RoleType.ROLE_USER)), true,
				Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(purchasesDAOMock.findById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenAnswer(invocation -> {
					User user = new User();
					user.setId(5L);

					Storehouse storehouseDB = new Storehouse(1L, new Good(), "298855", LocalDateTime.now(), Unit.кг,
							new BigDecimal(1777.856), new BigDecimal(6.56));

					Purchase savedPurchase = new Purchase(invocation.getArgument(0, Long.class), user, storehouseDB,
							LocalDateTime.now(), Unit.кг, new BigDecimal(12.56), new BigDecimal(6.56), Status.reserved);

					return Optional.of(savedPurchase);
				});
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);

		assertThrows(ServiceException.class, () -> purchasesService.deletePurchase(id),
				"Service exception expected when deleting with bad user");
	}

	@Test
	public void WhenDeletePurchaseWithBadStatus() throws ServiceException {
		SecurityUser securityUser = new SecurityUser(3L, "Vano", "HASHED_PASSWORD", "Vano@mail.ru",
				Set.of(new Role(1L, RoleType.ROLE_USER)), true,
				Collections.singletonList(new SimpleGrantedAuthority(RoleType.ROLE_USER.name())));

		Mockito.when(purchasesDAOMock.findById(Mockito.longThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenAnswer(invocation -> {
					User user = new User();
					user.setId(3L);

					Storehouse storehouseDB = new Storehouse(1L, new Good(), "298855", LocalDateTime.now(), Unit.кг,
							new BigDecimal(1777.856), new BigDecimal(6.56));
					
					Status status = Status.bought;
					Purchase savedPurchase = new Purchase(invocation.getArgument(0, Long.class), user, storehouseDB,
							LocalDateTime.now(), Unit.кг, new BigDecimal(12.56), new BigDecimal(6.56), status);

					return Optional.of(savedPurchase);
				});
		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);

		assertThrows(ServiceException.class, () -> purchasesService.deletePurchase(8L),
				"Service exception expected when deleting and returns empty purchase");
	}
}
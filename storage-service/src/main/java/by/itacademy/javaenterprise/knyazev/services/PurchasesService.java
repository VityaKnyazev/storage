package by.itacademy.javaenterprise.knyazev.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.itacademy.javaenterprise.knyazev.dao.PurchasesDAO;
import by.itacademy.javaenterprise.knyazev.entities.Purchase;
import by.itacademy.javaenterprise.knyazev.entities.Storehouse;
import by.itacademy.javaenterprise.knyazev.security.SecurityUser;
import by.itacademy.javaenterprise.knyazev.security.converters.UsersConverter;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;
import by.itacademy.javaenterprise.knyazev.utils.Status;

@Service
public class PurchasesService {
	private static final Logger logger = LoggerFactory.getLogger(PurchasesService.class.getName());

	private SecurityUsersService securityUsersService;
	private StorehouseService storehouseService;
	private PurchasesDAO purchasesDAO;

	@Autowired
	public PurchasesService(SecurityUsersService securityUsersService, StorehouseService storehouseService,
			PurchasesDAO purchasesDAO) {
		this.securityUsersService = securityUsersService;
		this.storehouseService = storehouseService;
		this.purchasesDAO = purchasesDAO;
	}

	@Transactional
	public List<Purchase> showReserved() throws ServiceException {
		SecurityUser securuityUser = securityUsersService.getSecurityUserFromSecurityContext();

		List<Purchase> purchases = purchasesDAO.findByUserIdAndStatus(securuityUser.getId(), Status.reserved);

		if (purchases == null || purchases.isEmpty()) {
			throw new ServiceException("Current user hasn't any reserved purchases");
		}

		return purchases;
	}

	@Transactional
	public List<Purchase> showAllBoughtForReport() throws ServiceException {
		List<Purchase> purchases = purchasesDAO.findByStatus(Status.bought);

		if (purchases == null || purchases.isEmpty()) {
			throw new ServiceException("Users purchases are empty. Nothing was bought.");
		}

		return purchases;
	}

	@Transactional
	public Purchase reservePurchase(Purchase purchase) throws ServiceException {
		if (purchase == null || purchase.getId() != null) {
			logger.error("Purchase must be not null for saving and purchase id must be equals to null for saving!");
			throw new ServiceException(
					"Purchase must be not null for saving and purchase id must be equals to null for saving!");
		}

		if (purchase.getStorehouse() == null) {
			logger.error("Purchase must contains storehouse with good");
			throw new ServiceException("Purchase must contains storehouse with good!");
		}

		Storehouse savedStorehouse = storehouseService.showStorehouseUnitById(purchase.getStorehouse().getId());

		Long purchaseGoodId = (purchase.getStorehouse().getGood() != null) ? purchase.getStorehouse().getGood().getId()
				: null;
		if (savedStorehouse.getGood().getId() != purchaseGoodId) {
			logger.error("Purchase can't be saved because of error, gaven good not exists in storehouse!");
			throw new ServiceException(
					"Purchase can't be saved because of error, gaven good not exists in storehouse!");
		}

		if (!savedStorehouse.getUnit().equals(purchase.getUnit())) {
			logger.error("Purchase can't be saved because of error, gaven unit not exists in storehouse!");
			throw new ServiceException(
					"Purchase can't be saved because of error, gaven unit not exists in storehouse!");
		}

		BigDecimal purchasePrice = purchase.getPrice() != null ? purchase.getPrice() : new BigDecimal(0);

		if (savedStorehouse.getPrice().compareTo(purchasePrice) != 0) {
			logger.error("Purchase can't be saved because of error, gaven good price not exists in storehouse!");
			throw new ServiceException(
					"Purchase can't be saved because of error, gaven good price not exists in storehouse!");
		}

		if (savedStorehouse.getQuantity().compareTo(purchase.getQuantity()) == -1) {
			logger.error(
					"Purchase can't be saved because of error, gaven good quantity is exceeded that exists in storehouse!");
			throw new ServiceException(
					"Purchase can't be saved because of error, gaven good quantity is exceeded that exists in storehouse!");
		}

		SecurityUser securityUser = securityUsersService.getSecurityUserFromSecurityContext();

		savedStorehouse.setQuantity(savedStorehouse.getQuantity().subtract(purchase.getQuantity()));
		purchase.setStorehouse(savedStorehouse);
		purchase.setUser(new UsersConverter().convertFromTwoToOne(securityUser));
		purchase.setStatus(Status.reserved);

		Purchase savedPurchase = purchasesDAO.save(purchase);

		if (savedPurchase != null) {
			if ((savedPurchase.getId() != null) && (savedPurchase.getId() > 0L))
				return savedPurchase;
		}
		logger.error("Error on saving (reserving) purchase!");
		throw new ServiceException("Error on reserving purchase!");
	}

	@Transactional
	public Purchase buyPurchase(Purchase purchase) throws ServiceException {
		if (purchase == null || purchase.getId() == null || purchase.getId() <= 0L) {
			logger.error("Purchase id must be equals to null or be above zero for saving!");
			throw new ServiceException("Purchase id must be equals to null or be above zero for saving!");
		}

		Optional<Purchase> optionalPurchase = purchasesDAO.findById(purchase.getId());

		if (optionalPurchase.isEmpty()) {
			logger.error("Error gaven purchase not present");
			throw new ServiceException("Error gaven purchase not present");
		}

		Purchase savedPurchase = optionalPurchase.get();
		
		if (!savedPurchase.getStatus().equals(Status.reserved)) {
			logger.error("Error. Purchase must have status reserved");
			throw new ServiceException("Error. Purchase must have status reserved");
		}
		
		SecurityUser securityUser = securityUsersService.getSecurityUserFromSecurityContext();;

		if (savedPurchase.getUser().getId() != securityUser.getId()) {
			logger.error("Error current user doesn't possessed of gaven purchase. Error on boughting purchase");
			throw new ServiceException(
					"Error current user doesn't possessed of gaven purchase. Error on boughting purchase");
		}

		savedPurchase.setStatus(Status.bought);
		savedPurchase.setDateTime(LocalDateTime.now());
		
		Purchase updatedPurchase = purchasesDAO.save(savedPurchase);
		
		if (updatedPurchase != null) {
			if ((updatedPurchase.getId() != null) && (updatedPurchase.getId() > 0L))
				return updatedPurchase;
		}
		logger.error("Error on updating (buying) purchase!");
		throw new ServiceException("Error on updating (buying) purchase!");
	}

	@Transactional
	public void deletePurchase(Long id) throws ServiceException {
		if (id == null || id <= 0L) {
			throw new ServiceException("Error. Purchase id must exists and be above 0");
		}
		
		Optional<Purchase> isPresent = purchasesDAO.findById(id);

		if (isPresent.isEmpty()) {
			logger.error("Error gaven purchase not present");
			throw new ServiceException("Error gaven purchase not present");
		}

		Purchase savedPurchase = isPresent.get();

		SecurityUser securityUser = securityUsersService.getSecurityUserFromSecurityContext();

		if (savedPurchase.getUser().getId() != securityUser.getId()) {
			logger.error("Error current user doesn't possessed of gaven purchase. Error on deleting purchase");
			throw new ServiceException(
					"Error current user doesn't possessed of gaven purchase. Error on deleting purchase");
		}

		if (!savedPurchase.getStatus().equals(Status.reserved)) {
			logger.error("Error. Current purchase has not reserved status");
			throw new ServiceException("Error. Current purchase has not reserved status");
		}
		
		Storehouse savedStorehouse = savedPurchase.getStorehouse();
		savedStorehouse.setQuantity(savedStorehouse.getQuantity().add(savedPurchase.getQuantity()));
		storehouseService.updateStorehouse(savedStorehouse);

		purchasesDAO.deleteById(id);
	}
	
}
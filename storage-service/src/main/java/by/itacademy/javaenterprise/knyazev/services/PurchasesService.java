package by.itacademy.javaenterprise.knyazev.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import by.itacademy.javaenterprise.knyazev.dao.PurchasesDAO;
import by.itacademy.javaenterprise.knyazev.entities.Purchase;
import by.itacademy.javaenterprise.knyazev.entities.Storehouse;
import by.itacademy.javaenterprise.knyazev.entities.User;
import by.itacademy.javaenterprise.knyazev.security.SecurityUser;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;
import by.itacademy.javaenterprise.knyazev.utils.Status;

@Service
public class PurchasesService {
	private static final Logger logger = LoggerFactory.getLogger(PurchasesService.class.getName());
	@Autowired
	private PurchasesDAO purchasesDAO;
	@Autowired
	private UsersService usersService;
	@Autowired
	private StorehouseService storehouseService;
	
	@Transactional
	public List<Purchase> showReserved() throws ServiceException {
		SecurityUser securuityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return purchasesDAO.findByUserIdAndStatus(securuityUser.getId(), Status.reserved);
	}
	
	@Transactional
	public List<Purchase> showAllBought() throws ServiceException {
		return purchasesDAO.findByStatus(Status.bought);
	}

	@Transactional
	public Purchase savePurchase(Purchase purchase) throws ServiceException {

		if (purchase.getId() != null) {
			logger.error("Purchase id must be equals to null for saving!");
			throw new ServiceException("Purchase id must be equals to null for saving!");
		}

		Long id = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		User user = usersService.showUserById(id);
		
		Storehouse savedStorehouse = storehouseService.showStorehouseUnitById(purchase.getStorehouse().getId());
		if (savedStorehouse.getGood().getId().longValue() != purchase.getStorehouse().getGood().getId().longValue()) {
			logger.error("Purchase can't be saved because of error, gaven good not exists in storehouse!");
			throw new ServiceException(
					"Purchase can't be saved because of error, gaven good not exists in storehouse!");
		}

		if (!savedStorehouse.getUnit().equals(purchase.getUnit())) {
			logger.error("Purchase can't be saved because of error, gaven unit not exists in storehouse!");
			throw new ServiceException(
					"Purchase can't be saved because of error, gaven unit not exists in storehouse!");
		}

		if (savedStorehouse.getPrice().compareTo(purchase.getPrice()) != 0) {
			logger.error("Purchase can't be saved because of error, gaven good price not exists in storehouse!");
			throw new ServiceException(
					"Purchase can't be saved because of error, gaven good price not exists in storehouse!");
		}

		if (savedStorehouse.getQuantity().compareTo(purchase.getQuantity()) == -1) {
			logger.error("Purchase can't be saved because of error, gaven good quantity is exceeded that exists in storehouse!");
			throw new ServiceException(
					"Purchase can't be saved because of error, gaven good quantity is exceeded that exists in storehouse!");
		}

		savedStorehouse.setQuantity(savedStorehouse.getQuantity().subtract(purchase.getQuantity()));
		purchase.setStorehouse(savedStorehouse);
		purchase.setUser(user);
		purchase.setStatus(Status.reserved);

		Purchase savedPurchase = purchasesDAO.save(purchase);

		if (savedPurchase != null) {
			if (savedPurchase.getId() != null && (savedPurchase.getId() > 0L))
				return savedPurchase;
		}
		logger.error("Error on saving purchase!");
		throw new ServiceException("Error on saving purchase!");
	}
	
	@Transactional
	public Purchase boughtPurchase(Purchase purchase) throws ServiceException {
		if (purchase.getId() == null || purchase.getId() <= 0L) {
			logger.error("Purchase id must be equals to null or be above zero for saving!");
			throw new ServiceException("Purchase id must be equals to null or be above zero for saving!");
		}		
		
		SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Optional<Purchase> isPresent = purchasesDAO.findById(purchase.getId());
		
		
		if (isPresent.isEmpty()) {
			logger.error("Error gaven purchase not present");
			throw new ServiceException("Error gaven purchase not present");		
		}
		
		Purchase savedPurchase = isPresent.get();
		
		if (savedPurchase.getUser().getId().longValue() != securityUser.getId().longValue()) {
			logger.error("Error current user doesn't possessed of gaven purchase. Error on boughting purchase");
			throw new ServiceException("Error current user doesn't possessed of gaven purchase. Error on boughting purchase");
		}
		
		savedPurchase.setStatus(Status.bought);
		savedPurchase.setDateTime(LocalDateTime.now());
		return purchasesDAO.save(savedPurchase);		
	}
	
	@Transactional
	public void deletePurchase(Long id) throws ServiceException {
		SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Optional<Purchase> isPresent = purchasesDAO.findById(id);
				
		if (isPresent.isEmpty()) {
			logger.error("Error gaven purchase not present");
			throw new ServiceException("Error gaven purchase not present");		
		}
		
		Purchase savedPurchase = isPresent.get();
		
		if (savedPurchase.getUser().getId().longValue() != securityUser.getId().longValue()) {
			logger.error("Error current user doesn't possessed of gaven purchase. Error on deleting purchase");
			throw new ServiceException("Error current user doesn't possessed of gaven purchase. Error on deleting purchase");
		}
		
		if (savedPurchase.getStatus().name().equals(Status.bought.name())) {
			logger.error("Error current purchase have been bought already");
			throw new ServiceException("Error current purchase have been bought already");
		}
		
		Storehouse savedStorehouse = storehouseService.showStorehouseUnitById(savedPurchase.getId());
		savedStorehouse.setQuantity(savedStorehouse.getQuantity().add(savedPurchase.getQuantity()));
		savedPurchase.setStorehouse(savedStorehouse);
		
		purchasesDAO.deleteById(id);		
	}

}
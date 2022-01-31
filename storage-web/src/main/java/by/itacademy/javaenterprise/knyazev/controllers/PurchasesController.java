package by.itacademy.javaenterprise.knyazev.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import by.itacademy.javaenterprise.knyazev.dto.PurchaseDTO;
import by.itacademy.javaenterprise.knyazev.entities.Purchase;
import by.itacademy.javaenterprise.knyazev.mappers.PurchaseMapper;
import by.itacademy.javaenterprise.knyazev.services.PurchasesService;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;
import exceptions.ControllerException;

@RestController
@Validated
public class PurchasesController {
	@Autowired
	PurchasesService purchasesService;
	@Autowired
	PurchaseMapper purchaseMapperImpl;
	private static final Logger logger = LoggerFactory.getLogger(PurchasesController.class);

	@GetMapping("/purchases")
	public List<PurchaseDTO> getAll() throws ControllerException {
		try {
			return purchaseMapperImpl.toListDTO(purchasesService.showReserved());
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException("Error on showing purchases", e);
		}
	}

	@PostMapping("/purchases")
	public ResponseEntity<PurchaseDTO> addPurchase(@Valid @RequestBody PurchaseDTO purchaseDTO)
			throws ControllerException {
		try {
			Purchase purchase = purchasesService.savePurchase(purchaseMapperImpl.toPurchase(purchaseDTO));
			return new ResponseEntity<PurchaseDTO>(purchaseMapperImpl.toDTO(purchase), HttpStatus.CREATED);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}
	
	@PutMapping("/purchases")
	public ResponseEntity<PurchaseDTO> changePurchase(@Valid @RequestBody PurchaseDTO purchaseDTO)
			throws ControllerException {
		try {
			Purchase purchase = purchasesService.boughtPurchase(purchaseMapperImpl.toPurchase(purchaseDTO));
			return new ResponseEntity<PurchaseDTO>(purchaseMapperImpl.toDTO(purchase), HttpStatus.OK);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}
	
	@DeleteMapping("/purchases/{id}")
	public ResponseEntity<String> removePurchase(@PathVariable @Min(value = 1L, message = "id must be greater than or equals to 1") Long id)
			throws ControllerException {
		String message = "purchase with id=" + id + " successfully deleted";
		
		try {
			purchasesService.deletePurchase(id);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}
}
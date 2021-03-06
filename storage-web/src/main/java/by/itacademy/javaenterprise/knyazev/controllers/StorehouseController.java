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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import by.itacademy.javaenterprise.knyazev.dto.StorehouseDTO;
import by.itacademy.javaenterprise.knyazev.entities.Storehouse;
import by.itacademy.javaenterprise.knyazev.exceptions.ControllerException;
import by.itacademy.javaenterprise.knyazev.mappers.StorehouseMapper;
import by.itacademy.javaenterprise.knyazev.services.StorehouseService;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;

@RestController
@Validated
public class StorehouseController {
	private static final Logger logger = LoggerFactory.getLogger(StorehouseController.class);
	@Autowired
	StorehouseService storehouseService;
	@Autowired
	StorehouseMapper storehouseMapperImpl;
	

	@GetMapping("/storehouses")
	public List<StorehouseDTO> getAll(
			@RequestParam(required = false) @Min(value = 1, message = "page id must be greater than or equals to 1") Integer page,
			@RequestParam(required = false) @Min(value = 1, message = "size must be greater than or equals to 1") Integer size)
			throws ControllerException {
		if (page == null || size == null) {
			return storehouseMapperImpl.toListDTO(storehouseService.showAll());
		} else {
			try {
				return storehouseMapperImpl.toListDTO(storehouseService.showAll(page, size));
			} catch (ServiceException e) {
				logger.error(e.getMessage(), e);
				throw new ControllerException("Bad request in page request param. " + e.getMessage(), e);
			}
		}
	}

	@GetMapping("/storehouses/{id}")
	public StorehouseDTO getStorehouse(
			@PathVariable @Min(value = 1L, message = "id must be greater than or equals to 1") Long id)
			throws ControllerException {
		try {
			return storehouseMapperImpl.toDTO(storehouseService.showStorehouseUnitById(id));
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

	@PostMapping("/storehouses")
	public ResponseEntity<StorehouseDTO> addStorehouse(@Valid @RequestBody StorehouseDTO storehouseDTO)
			throws ControllerException {
		try {
			Storehouse storehouse = storehouseService.saveStorehouse(storehouseMapperImpl.toStorehouse(storehouseDTO));
			return new ResponseEntity<StorehouseDTO>(storehouseMapperImpl.toDTO(storehouse), HttpStatus.CREATED);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

	@PutMapping("/storehouses")
	public ResponseEntity<StorehouseDTO> changeStorehouse(@Valid @RequestBody StorehouseDTO storehouseDTO)
			throws ControllerException {
		try {
			Storehouse storehouse = storehouseService
					.updateStorehouse(storehouseMapperImpl.toStorehouse(storehouseDTO));
			return new ResponseEntity<StorehouseDTO>(storehouseMapperImpl.toDTO(storehouse), HttpStatus.OK);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

	@DeleteMapping("/storehouses/{id}")
	public ResponseEntity<String> removeStorehouse(
			@PathVariable @Min(value = 1L, message = "id must be greater than or equals to 1") Long id)
			throws ControllerException {
		String message = "storehouse with id=" + id + " successfully deleted";

		try {
			storehouseService.deleteStorehouse(id);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}
}
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

import by.itacademy.javaenterprise.knyazev.dto.GoodDTO;
import by.itacademy.javaenterprise.knyazev.entities.Good;
import by.itacademy.javaenterprise.knyazev.exceptions.ControllerException;
import by.itacademy.javaenterprise.knyazev.mappers.GoodMapper;
import by.itacademy.javaenterprise.knyazev.services.GoodsService;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;

@RestController
@Validated
public class GoodsController {
	private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodMapper goodMapperImpl;
	
	@GetMapping("/goods")
	public List<GoodDTO> getAll(
			@RequestParam(required = false) @Min(value = 1, message = "page id must be greater than or equals to 1") Integer page,
			@RequestParam(required = false) @Min(value = 1, message = "size must be greater than or equals to 1") Integer size)
			throws ControllerException {
		if (page == null || size == null) {
			return goodMapperImpl.toListDTO(goodsService.showAll());
		} else {
			try {
				return goodMapperImpl.toListDTO(goodsService.showAll(page, size));
			} catch (ServiceException e) {
				logger.error(e.getMessage(), e);
				throw new ControllerException("Bad request in page request param. " + e.getMessage(), e);
			}
		}
	}

	@GetMapping("/goods/{id}")
	public GoodDTO getGood(@PathVariable @Min(value = 1L, message = "id must be greater than or equals to 1") Long id)
			throws ControllerException {

		try {
			return goodMapperImpl.toDTO(goodsService.showGoodById(id));
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}

	}

	@PostMapping("/goods")
	public ResponseEntity<GoodDTO> addGood(@Valid @RequestBody GoodDTO goodDTO) throws ControllerException {
		try {
			Good good = goodsService.saveGood(goodMapperImpl.toGood(goodDTO));
			return new ResponseEntity<GoodDTO>(goodMapperImpl.toDTO(good), HttpStatus.CREATED);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

	@PutMapping("/goods")
	public ResponseEntity<GoodDTO> changeGood(@Valid @RequestBody GoodDTO goodDTO) throws ControllerException {
		try {
			Good good = goodsService.updateGood(goodMapperImpl.toGood(goodDTO));
			return new ResponseEntity<GoodDTO>(goodMapperImpl.toDTO(good), HttpStatus.OK);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

	@DeleteMapping("/goods/{id}")
	public ResponseEntity<String> removeGood(
			@PathVariable @Min(value = 1L, message = "id must be greater than or equals to 1") Long id)
			throws ControllerException {
		String message = "good with id=" + id + " successfully deleted";

		try {
			goodsService.deleteGood(id);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}
}
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

import by.itacademy.javaenterprise.knyazev.dto.CategoryDTO;
import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.mappers.CategoryMapper;
import by.itacademy.javaenterprise.knyazev.services.CategoriesService;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;
import exceptions.ControllerException;

@RestController
@Validated
public class CategoriesController {
	@Autowired
	private CategoriesService categoriesService;
	@Autowired
	CategoryMapper categoryMapperImpl;
	private static final Logger logger = LoggerFactory.getLogger(CategoriesController.class);

	@GetMapping("/categories")
	public List<CategoryDTO> getAll() {
		return categoryMapperImpl.toDTOList(categoriesService.showAll());
	}

	@GetMapping("/categories/{id}")
	public CategoryDTO getCategory(
			@PathVariable @Min(value = 1L, message = "id must be greater than or equals to 1") Long id)
			throws ControllerException {

		try {
			return categoryMapperImpl.toDTO(categoriesService.showCategoryById(id));
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

	@PostMapping("/categories")
	public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO)
			throws ControllerException {
		try {
			Category category = categoriesService.saveCategory(categoryMapperImpl.toCategory(categoryDTO));
			return new ResponseEntity<CategoryDTO>(categoryMapperImpl.toDTO(category), HttpStatus.CREATED);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

	@PutMapping("/categories")
	public ResponseEntity<CategoryDTO> changeCategory(@Valid @RequestBody CategoryDTO categoryDTO)
			throws ControllerException {
		try {
			Category category = categoriesService.updateCategory(categoryMapperImpl.toCategory(categoryDTO));
			return new ResponseEntity<CategoryDTO>(categoryMapperImpl.toDTO(category), HttpStatus.OK);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

	@DeleteMapping("/categories/{id}")
	public ResponseEntity<String> removeCategory(
			@PathVariable @Min(value = 1L, message = "id must be greater than or equals to 1") Long id)
			throws ControllerException {
		String message = "category with id=" + id + " successfully deleted";

		try {
			categoriesService.deleteCategory(id);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

}
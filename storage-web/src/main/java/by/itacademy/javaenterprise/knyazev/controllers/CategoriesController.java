package by.itacademy.javaenterprise.knyazev.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import by.itacademy.javaenterprise.knyazev.dto.CategoryDTO;
import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.mappers.CategoryMapper;
//import by.itacademy.javaenterprise.knyazev.mappers.CategoryMapper;
import by.itacademy.javaenterprise.knyazev.services.CategoriesService;

@RestController
public class CategoriesController {
	@Autowired
	private CategoriesService categoriesService;
	@Autowired
	CategoryMapper categoryMapperImpl;
	
	@GetMapping("/categories")
	public List<CategoryDTO> getAll(Model model) {
		return categoryMapperImpl.toDTOList(categoriesService.showAll());
	}
	
	@GetMapping("/category/{id}")
	public CategoryDTO getCategory(@PathVariable String id) {
		Category category = categoriesService.showCategoryById(id);
		return categoryMapperImpl.toDTO(category);	
	}
	
}
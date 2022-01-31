package by.itacademy.javaenterprise.knyazev.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import by.itacademy.javaenterprise.knyazev.entities.Good;
import lombok.Data;

@Data
public class CategoryDTO {
	private Long goodsCount;
	private Long id;
	
	@NotBlank(message = "CategoryDTO name have to be not null or whitespace!")
	@Size(min = 3, max = 25, message = "CategoryDTO name must have at least 3 symbols and must be less than or equals to 25 characters!")
	private String name;
	
	@NotBlank(message = "CategoryDTO description have to be not null or whitespace!")
	@Size(min = 3, max = 70, message = "CategoryDTO description must have at least 3 symbols and  must be less than or equals to 70 characters!")
	private String description;
	
	private List<Good> goods;
}
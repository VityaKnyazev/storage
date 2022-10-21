package by.itacademy.javaenterprise.knyazev.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.entities.Producer;
import lombok.Data;

@Data
public class GoodDTO {
	
	@Min(value = 1L, message = "ID must be above 1")
	private Long id;
	
	@NotBlank(message = "GoodDTO name have to be not null or whitespace!")
	@Size(min = 3, max = 30, message = "GoodDTO name must have at least 3 symbols and must be less than or equals to 30 characters!")
	private String name;
	
	@NotBlank(message = "GoodDTO sort have to be not null or whitespace!")
	@Size(min = 3, max = 30, message = "GoodDTO sort must have at least 3 symbols and must be less than or equals to 30 characters!")
	private String sort;
		
	@NotNull
	@Size(max = 900, message = "GoodDTO description must be less than or equals to 900 characters!")
	private String description;
	
	@NotNull(message = "Category in GoodDTO must not be null")
	private Category category;
	
	@NotNull(message = "Producer in GoodDTO must not be null")
	private Producer producer;
	
	private Long storehouse;
}
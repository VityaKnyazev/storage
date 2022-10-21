package by.itacademy.javaenterprise.knyazev.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import by.itacademy.javaenterprise.knyazev.entities.Good;
import lombok.Data;

@Data
public class StorehouseDTO {
	
	@Min(value = 1L, message = "ID must be above 1")
	private Long id;
	
	@NotNull(message = "Good in StorehouseDTO must not be null")
	private Good good;
	
	@NotBlank(message = "StorehouseDTO ttn number have to be not null or whitespace!")
	@Size(min = 1, max = 15, message = "StorehouseDTO ttn number must have length from 1 to 15 symbols")
	private String ttnNum;
	
	@NotBlank(message = "StorehouseDTO date and time have to be not null or whitespace!")
	@Pattern(regexp = "^[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}\\s[0-9]{2}\\:[0-9]{2}$", message = "StorehouseDTO dateTime must have 16 symbols dd.MM.yyyy HH:mm")
	private String dateTime;
	
	@NotBlank(message = "StorehouseDTO unit have to be not null or whitespace!")
	@Pattern(regexp = "^(кг|г|т|шт|ед)$", message = "Unit must have only values: кг, г, т, шт, ед")
	private String unit;
		
	@Pattern(regexp = "^[0-9]{1,6},[0-9]{3}$", message = "StorehouseDTO qantity must have value like ######,###")
	private String quantity;

	@Pattern(regexp = "^[0-9]{1,9},[0-9]{2}$", message = "StorehouseDTO price must have value like #########,##")
	private String price;
}

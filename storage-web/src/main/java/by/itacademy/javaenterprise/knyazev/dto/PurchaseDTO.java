package by.itacademy.javaenterprise.knyazev.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PurchaseDTO {
	private Long id;
	
	@NotNull(message = "storehouse id in PurchaseDTO must not be null")
	@Min(value = 1L, message = "storehouse id must be above 1")
	private Long storehouseId;
	
	@NotNull(message = "good id in PurchaseDTO must not be null")
	@Min(value = 1L, message = "good id must be above 1")
	private Long goodId;
	
	@NotBlank(message = "PurchaseDTO goodName have to be not null or whitespace!")
	@Size(min = 3, max = 30, message = "GoodDTO name must have at least 3 symbols and must be less than or equals to 30 characters!")
	private String goodName;
	
	@NotBlank(message = "PurchaseDTO goodSort have to be not null or whitespace!")
	@Size(min = 3, max = 30, message = "GoodDTO sort must have at least 3 symbols and must be less than or equals to 30 characters!")
	private String goodSort;
	
	@NotBlank(message = "PurchaseDTO categoryName have to be not null or whitespace!")
	@Size(min = 3, max = 25, message = "PurchaseDTO categoryName must have at least 3 symbols and must be less than or equals to 25 characters!")
	private String categoryName;
	
	@NotBlank(message = "PurchaseDTO date and time have to be not null or whitespace!")
	@Pattern(regexp = "^[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}\\s[0-9]{2}\\:[0-9]{2}$", message = "StorehouseDTO dateTime must have 16 symbols dd.MM.yyyy HH:mm")
	private String dateTime;
	
	@NotBlank(message = "PurchaseDTO unit have to be not null or whitespace!")
	@Pattern(regexp = "^(кг|г|т|шт|ед)$", message = "Unit must have only values: кг, г, т, шт, ед")
	private String unit;
		
	@Pattern(regexp = "^[0-9]{1,6},[0-9]{3}$", message = "PurchaseDTO qantity must have value like ######,###")
	private String quantity;

	@Pattern(regexp = "^[0-9]{1,9},[0-9]{2}$", message = "PurchaseDTO price must have value like #########,##")
	private String price;
}

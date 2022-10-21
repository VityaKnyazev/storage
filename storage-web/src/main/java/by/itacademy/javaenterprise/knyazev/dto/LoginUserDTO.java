package by.itacademy.javaenterprise.knyazev.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginUserDTO {
	
	@NotBlank(message = "Username have to be not null or whitespace!")
	@Size(min = 3, max = 30, message = "Username must have at least 3 symbols and must be less than or equals to 40 characters!")
	private String username;
	
	@NotBlank(message = "Password have to be not null or whitespace!")
	@Size(min = 3, max = 68, message = "Password must have at least 3 symbols and  must be less than or equals to 70 characters!")
	private String password;
	
}
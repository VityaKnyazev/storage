package by.itacademy.javaenterprise.knyazev.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import by.itacademy.javaenterprise.knyazev.entities.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserDTO  extends LoginUserDTO{
	
	@Min(value = 1L, message = "ID must be above 1")
	private Long id;
	
	@Email(message = "Incorrect email!")
	@NotBlank
	private String email;	
	
	private Set<Role> roles;
	
	public String getEmail() {
		return email.toLowerCase().strip();
	}
}
package by.itacademy.javaenterprise.knyazev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor	
public class TokenDTO {
	private String access_token;
	private final String token_type = "Bearer";
	private String expires_in;
}
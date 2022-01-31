package exceptions.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
	private static final String UNKNOWN_ERROR = "UNKNOWN ERROR";
	
	private int statusCode;
	private Date timestamp;
	private String message;
	
	public static final String defaultError() {
		return UNKNOWN_ERROR;
	}
	
}
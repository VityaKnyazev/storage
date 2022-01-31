package by.itacademy.javaenterprise.knyazev.services.exceptions;

public class ServiceException extends Exception {
	
	private static final long serialVersionUID = 6528834898727173442L;
	
	private static final String DEFAULT = "Unknown error was occured id Service class.";
	
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ServiceException() {
		super(DEFAULT);
	}
	
}

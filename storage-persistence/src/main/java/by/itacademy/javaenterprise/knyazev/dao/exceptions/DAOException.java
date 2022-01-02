package by.itacademy.javaenterprise.knyazev.dao.exceptions;

public class DAOException extends Exception {
	
	private static final long serialVersionUID = 6528834898727173442L;
	
	private static final String DEFAULT = "Unknown error was occured id DAO class.";
	
	
	public DAOException(String message) {
		super(message);
	}
	
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DAOException() {
		super(DEFAULT);
	}
	
}

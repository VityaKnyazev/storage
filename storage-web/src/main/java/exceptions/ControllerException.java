package exceptions;

public class ControllerException extends Exception {
	
	private static final long serialVersionUID = 6528834898727173442L;
	
	private static final String DEFAULT = "Unknown error was occured id Service class.";
	
	
	public ControllerException(String message) {
		super(message);
	}
	
	public ControllerException(Throwable cause) {
		super(cause);
	}
	
	public ControllerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ControllerException() {
		super(DEFAULT);
	}
	
}

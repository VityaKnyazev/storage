package by.itacademy.javaenterprise.knyazev.exceptions.handlers;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import by.itacademy.javaenterprise.knyazev.exceptions.ControllerException;
import by.itacademy.javaenterprise.knyazev.exceptions.responses.ErrorMessage;

@ControllerAdvice
public class ControllersExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<ErrorMessage> handleUnknowException(Exception e) {
		ErrorMessage message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
				ErrorMessage.defaultError() + ": " + e.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { DataAccessException.class })
	public ResponseEntity<ErrorMessage> handleDataAccessException(DataAccessException e) {

		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), e.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { BadCredentialsException.class })
	public ResponseEntity<ErrorMessage> handleBadCredentialsException(BadCredentialsException e) {
		ErrorMessage message = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), new Date(), e.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ResponseEntity<ErrorMessage> handleRequestParameters(ConstraintViolationException e) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), e.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { ControllerException.class })
	public ResponseEntity<ErrorMessage> handleRequestParameters(ControllerException e) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), e.getMessage());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage());
		return handleExceptionInternal(ex, message, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage());
		return handleExceptionInternal(ex, message, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		pageNotFoundLogger.warn(ex.getMessage());

		Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
		if (!CollectionUtils.isEmpty(supportedMethods)) {
			headers.setAllow(supportedMethods);
		}
		
		ErrorMessage message = new ErrorMessage(HttpStatus.METHOD_NOT_ALLOWED.value(), new Date(), ex.getMessage());
		
		return handleExceptionInternal(ex, message, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage());
		return handleExceptionInternal(ex, message, headers, status, request);
	}

}
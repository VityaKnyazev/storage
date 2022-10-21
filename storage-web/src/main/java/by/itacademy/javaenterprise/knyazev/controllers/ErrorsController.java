package by.itacademy.javaenterprise.knyazev.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import by.itacademy.javaenterprise.knyazev.exceptions.responses.ErrorMessage;

@RestController
public class ErrorsController {
	@GetMapping("/errors")
	public ResponseEntity<ErrorMessage> handleError(HttpServletRequest request) throws Exception {
		Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
		
		if (status == 400) {
			ErrorMessage message = new ErrorMessage(status, new Date(),"Bad Request");
			return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
		}
		
		if (status == 401) {
			ErrorMessage message = new ErrorMessage(status, new Date(),"Unauthorized");
			return new ResponseEntity<ErrorMessage>(message, HttpStatus.UNAUTHORIZED);
		}
		
		if (status == 403) {
			ErrorMessage message = new ErrorMessage(status, new Date(),"Forbidden");
			return new ResponseEntity<ErrorMessage>(message, HttpStatus.FORBIDDEN);
		}
		
		if (status == 404) {
			ErrorMessage message = new ErrorMessage(status, new Date(),"Not Found");
			return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
		}
		
		throw new Exception("UNKNOWN_ERROR");
	}
}

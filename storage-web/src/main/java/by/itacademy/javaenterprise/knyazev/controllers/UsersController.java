package by.itacademy.javaenterprise.knyazev.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import by.itacademy.javaenterprise.knyazev.dto.UserDTO;
import by.itacademy.javaenterprise.knyazev.entities.User;
import by.itacademy.javaenterprise.knyazev.exceptions.ControllerException;
import by.itacademy.javaenterprise.knyazev.mappers.UserMapper;
import by.itacademy.javaenterprise.knyazev.services.UsersService;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;

@RestController
@Validated
public class UsersController {
	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	@Autowired
	private UsersService usersService;
	@Autowired
	UserMapper userMapperImpl;

	@GetMapping("/users")
	public List<UserDTO> getAll() {
		return userMapperImpl.toDTO(usersService.showAll());
	}

	@GetMapping("/users/{id}")
	public UserDTO getUser(@PathVariable @Min(value = 1L, message = "id must be greater than or equals to 1") Long id)
			throws ControllerException {

		try {
			return userMapperImpl.toDTO(usersService.showUserById(id));
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

	@PutMapping("/users")
	public ResponseEntity<UserDTO> changeUser(@Valid @RequestBody UserDTO userDTO) throws ControllerException {
		try {
			User user = usersService.updateUser(userMapperImpl.toUser(userDTO));
			return new ResponseEntity<UserDTO>(userMapperImpl.toDTO(user), HttpStatus.OK);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> removeCategory(
			@PathVariable @Min(value = 1L, message = "id must be greater than or equals to 1") Long id)
			throws ControllerException {
		String message = "user with id=" + id + " successfully deleted!";

		try {
			usersService.deleteUser(id);
			return new ResponseEntity<String>(message, HttpStatus.OK);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException(e);
		}
	}

}
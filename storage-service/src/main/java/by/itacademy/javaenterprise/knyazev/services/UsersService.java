package by.itacademy.javaenterprise.knyazev.services;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import by.itacademy.javaenterprise.knyazev.dao.RolesDAO;
import by.itacademy.javaenterprise.knyazev.dao.UsersDAO;
import by.itacademy.javaenterprise.knyazev.entities.Role;
import by.itacademy.javaenterprise.knyazev.entities.User;
import by.itacademy.javaenterprise.knyazev.security.SecurityUser;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;
import by.itacademy.javaenterprise.knyazev.utils.RoleType;

@Service
public class UsersService {

	private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

	@Autowired
	private UsersDAO usersDAO;

	@Autowired
	private RolesDAO rolesDAO;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Transactional
	public List<User> showAll() {
		return usersDAO.findAll();
	}

	@Transactional
	public User showUserById(Long id) throws ServiceException {
		User user = usersDAO.findById(id).orElse(null);

		if (user == null) {
			logger.error("User with id {} not found!", id);
			throw new ServiceException("User with id " + id + " not found!");
		}

		return user;
	}

	@Transactional
	public User saveUser(User user) throws ServiceException {
		if (usersDAO.existsByEmail(user.getEmail())) {
			logger.error("User with email {}  alredy exists", user.getEmail());
			throw new ServiceException("User with email " + user.getEmail() + " alredy exists");
		}

		if (usersDAO.existsByName(user.getName())) {
			logger.error("User with name {}  alredy exists", user.getName());
			throw new ServiceException("User with name " + user.getName() + " alredy exists");
		}

		Role role = rolesDAO.findByName(RoleType.ROLE_USER);
		user.addRole(role);

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		User savedUser = usersDAO.save(user);

		if (savedUser == null || savedUser.getId() <= 0L) {
			throw new ServiceException("Failed to register User with name " + user.getName());
		}

		return savedUser;
	}

	@Transactional
	public User updateUser(User user) throws ServiceException {
		Long userId = user.getId();
		SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (userId == null || userId != securityUser.getId()) {
			logger.error("User with id {}  is not saved in databse or user you want to change is not authorized", user.getId());
			throw new ServiceException("Can't update user with null or changed id!");
		}

		User savedUser = usersDAO.findById(userId).orElse(null);

		if (savedUser == null) {
			logger.error("User with id {}  is not saved in databse", user.getId());
			throw new ServiceException("Can't update user that not exists!");
		}

		if (user.getRoles() == null) {
			logger.error("User role or user roles doesn't present!");
			throw new ServiceException("User role or user roles doesn't present!");
		}

		if (user.getRoles().isEmpty()
				|| !user.getRoles().stream().allMatch(role -> rolesDAO.existsByName(role.getName()))) {
			logger.error("User role or user roles doesn't present or not valid!");
			throw new ServiceException("User role or user roles doesn't present or not valid!");
		}

		if (!user.getPassword().equals(savedUser.getPassword())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}

		User updatedUser = usersDAO.save(user);

		if (updatedUser == null || updatedUser.getId() < 1L) {
			throw new ServiceException("Failed to update User with name " + user.getName());
		}

		return updatedUser;
	}

	@Transactional
	public void deleteUser(Long id) throws ServiceException {
		if (usersDAO.existsById(id)) {
			usersDAO.deleteById(id);
		} else {
			logger.error("Error on deleting user on id={}!", id);
			throw new ServiceException("Error on deleting user on id=" + id + "!");
		}
	}

}

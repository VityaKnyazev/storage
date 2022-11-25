package by.itacademy.javaenterprise.knyazev.services;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	private SecurityUsersService securityUsersService;
	
	private UsersDAO usersDAO;

	private RolesDAO rolesDAO;

	private PasswordEncoder passwordEncoder;
	
	

	@Autowired
	public UsersService(SecurityUsersService securityUsersService, UsersDAO usersDAO, RolesDAO rolesDAO, PasswordEncoder passwordEncoder) {
		this.securityUsersService = securityUsersService;
		this.usersDAO = usersDAO;
		this.rolesDAO = rolesDAO;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public List<User> showAll() {
		return usersDAO.findAll();
	}

	@Transactional
	public User showUserById(Long id) throws ServiceException {
		if (id == null || id < 1L) {
			logger.error("Bad user id={}", id);
			throw new ServiceException("Bad user id=" + id);
		}

		User user = usersDAO.findById(id).orElse(null);

		if (user == null) {
			logger.error("User with id {} not found!", id);
			throw new ServiceException("User with id " + id + " not found!");
		}

		return user;
	}

	@Transactional
	public User saveUser(User user) throws ServiceException {
		if (user == null || user.getId() != null) {
			logger.error("User not exists");
			throw new ServiceException("User not exists");
		}

		String email = user.getEmail();
		if (email == null || usersDAO.existsByEmail(email)) {
			logger.error("User with email {}  already exists", email);
			throw new ServiceException("User with email " + email + "already exists");
		}

		String name = user.getName();
		if (name == null || usersDAO.existsByName(name)) {
			logger.error("User with name {}  already exists", name);
			throw new ServiceException("User with name " + name + " already exists");
		}

		Role role = rolesDAO.findByName(RoleType.ROLE_USER);
		user.addRole(role);

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		User savedUser = usersDAO.save(user);

		if (savedUser == null || savedUser.getId() == null || savedUser.getId() < 1L) {
			logger.error("Failed to register User with name {}", user.getName());
			throw new ServiceException("Failed to register User with name " + user.getName());
		}

		return savedUser;
	}


	@Transactional
	public User updateUser(User user) throws ServiceException {
		if (user == null || user.getId() == null) {
			logger.error("Can't update user. Provided user is not valid");
			throw new ServiceException("Can't update user. Provided user is not valid");
		}

		SecurityUser securityUser = securityUsersService.getSecurityUserFromSecurityContext();

		if (user.getId() != securityUser.getId()) {
			if (!securityUser.getAuthorities().stream()
					.anyMatch(ga -> ga.getAuthority().equals(RoleType.ROLE_ADMIN.name()))) {
				logger.error("Can't update user. Current user hasn't permissions.");
				throw new ServiceException("Can't update user. Current user hasn't permissions.");
			}
		}


		if (!user.getPassword().equals(securityUser.getPassword())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}

		User updatedUser = usersDAO.save(user);

		if (updatedUser == null || updatedUser.getId() == null || updatedUser.getId() < 1L) {
			throw new ServiceException("Failed to update User with name " + user.getName());
		}

		return updatedUser;
	}


	@Transactional
	public void deleteUser(Long id) throws ServiceException {
		if ((id != null) && (usersDAO.existsById(id))) {
			usersDAO.deleteById(id);
		} else {
			logger.error("Error on deleting user on id={}!", id);
			throw new ServiceException("Error on deleting user on id=" + id + "!");
		}
	}

}

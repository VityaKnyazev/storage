package by.itacademy.javaenterprise.knyazev.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import by.itacademy.javaenterprise.knyazev.dao.RolesDAO;
import by.itacademy.javaenterprise.knyazev.dao.UsersDAO;
import by.itacademy.javaenterprise.knyazev.entities.Role;
import by.itacademy.javaenterprise.knyazev.entities.User;
import by.itacademy.javaenterprise.knyazev.security.SecurityUser;
import by.itacademy.javaenterprise.knyazev.security.converters.UsersConverter;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;
import by.itacademy.javaenterprise.knyazev.utils.RoleType;

public class UsersServiceTests {
	private SecurityUsersService securityUsersServiceMock;
	private UsersDAO usersDAOMock;
	private RolesDAO rolesDAOMock;
	private PasswordEncoder passwordEncoderMock;

	private UsersService usersService;

	@BeforeEach
	public void setUpBeforeEachTest() {
		securityUsersServiceMock = Mockito.mock(SecurityUsersService.class);
		usersDAOMock = Mockito.mock(UsersDAO.class);
		rolesDAOMock = Mockito.mock(RolesDAO.class);
		passwordEncoderMock = Mockito.mock(PasswordEncoder.class);

		usersService = new UsersService(securityUsersServiceMock, usersDAOMock, rolesDAOMock, passwordEncoderMock);
	}

	@Test
	public void whenShowAllUsers() {
		List<User> users = new ArrayList<>();

		final Long checkingId = 2L;
		users.add(new User(1L, "Vano", "vano", "Vano@mail.ru", true, Set.of(new Role(1L, RoleType.ROLE_USER))));
		users.add(new User(checkingId, "Sergo", "sergo", "Sergo@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER))));

		Mockito.when(usersDAOMock.findAll()).thenReturn(users);

		assertEquals(checkingId, usersService.showAll().get(1).getId());
	}

	@Test
	public void whenShowUserById() throws ServiceException {
		Long showUserId = 12L;
		User user = new User(showUserId, "Vano", "vano", "Vano@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER)));

		Mockito.when(usersDAOMock.findById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L))))
				.thenReturn(Optional.of(user));

		assertEquals(showUserId, usersService.showUserById(showUserId).getId());
	}

	@Test
	public void whenShowUserByNullId() {
		Long showUserId = null;

		assertThrows(ServiceException.class, () -> usersService.showUserById(showUserId),
				"Service exception expected when show user by null id");
	}

	@Test
	public void whenShowUserByBadId() {
		Long showUserId = 0L;

		assertThrows(ServiceException.class, () -> usersService.showUserById(showUserId),
				"Service exception expected when show user by bad id");
	}

	@Test
	public void whenShowUserByIdReturnsNull() {
		Long showUserId = 12L;
		Optional<User> user = Optional.empty();

		Mockito.when(usersDAOMock.findById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)))).thenReturn(user);

		assertThrows(ServiceException.class, () -> usersService.showUserById(showUserId),
				"Service exception expected when show user returns null");
	}

	@Test
	public void whenSaveUser() throws ServiceException {
		Role role = new Role(1L, RoleType.ROLE_USER);
		String encodedPassword = "ENCODED_PASSWORD";
		final Long savedId = 5L;

		Mockito.when(usersDAOMock
				.existsByEmail(Mockito.argThat(str -> (str != null) && (str.length() > 0) && (str.length() <= 30))))
				.thenReturn(false);
		Mockito.when(usersDAOMock
				.existsByName(Mockito.argThat(str -> (str != null) && (str.length() > 3) && (str.length() <= 30))))
				.thenReturn(false);
		Mockito.when(rolesDAOMock.findByName(Mockito.any(RoleType.class))).thenReturn(role);
		Mockito.when(passwordEncoderMock.encode(Mockito.anyString())).thenReturn(encodedPassword);
		Mockito.when(usersDAOMock.save(Mockito.any(User.class))).thenAnswer(invocation -> {
			User savedUser = invocation.getArgument(0, User.class);
			savedUser.setId(savedId);
			return savedUser;
		});

		Long userId = null;
		User user = new User(userId, "Vano", "vano", "Vano@mail.ru", true, null);

		assertEquals(savedId, usersService.saveUser(user).getId());
	}

	@Test
	public void whenSaveNullUser() {
		User user = null;

		assertThrows(ServiceException.class, () -> usersService.saveUser(user),
				"Service exception expected when saving null user");
	}

	@Test
	public void whenSaveUserWithNonNullId() {
		Long userId = 2L;
		User user = new User();
		user.setId(userId);

		assertThrows(ServiceException.class, () -> usersService.saveUser(user),
				"Service exception expected when saving user with bad id");
	}

	@Test
	public void whenSaveUserOnExistingEmail() {
		Mockito.when(usersDAOMock
				.existsByEmail(Mockito.argThat(str -> (str != null) && (str.length() > 0) && (str.length() <= 30))))
				.thenReturn(true);

		Long userId = null;
		User user = new User(userId, "Vano", "vano", "Vano@mail.ru", true, null);

		assertThrows(ServiceException.class, () -> usersService.saveUser(user),
				"Service exception expected when saving user email already exists");
	}

	@Test
	public void whenSaveUserOnNullEmail() {
		Mockito.when(usersDAOMock.existsByEmail(Mockito.nullable(String.class))).thenReturn(true);

		Long userId = null;
		String userEmail = null;
		User user = new User(userId, "Vano", "vano", userEmail, true, null);

		assertThrows(ServiceException.class, () -> usersService.saveUser(user),
				"Service exception expected when saving user on null email");
	}

	@Test
	public void whenSaveUserOnExistingName() {
		Mockito.when(usersDAOMock
				.existsByEmail(Mockito.argThat(str -> (str != null) && (str.length() > 0) && (str.length() <= 30))))
				.thenReturn(false);
		Mockito.when(usersDAOMock
				.existsByName(Mockito.argThat(str -> (str != null) && (str.length() > 3) && (str.length() <= 30))))
				.thenReturn(true);

		Long userId = null;
		User user = new User(userId, "Vano", "vano", "Vano@mail.ru", true, null);

		assertThrows(ServiceException.class, () -> usersService.saveUser(user),
				"Service exception expected when saving user name already exists");
	}

	@Test
	public void whenSaveUserOnNullName() {
		Mockito.when(usersDAOMock
				.existsByEmail(Mockito.argThat(str -> (str != null) && (str.length() > 0) && (str.length() <= 30))))
				.thenReturn(false);
		Mockito.when(usersDAOMock.existsByName(Mockito.nullable(String.class))).thenReturn(true);

		Long userId = null;
		String userName = null;
		User user = new User(userId, userName, "vano", "Vano@mail.ru", true, null);

		assertThrows(ServiceException.class, () -> usersService.saveUser(user),
				"Service exception expected when saving user on null name");
	}

	@Test
	public void whenSaveUserReturnsNull() {
		Role role = new Role(1L, RoleType.ROLE_USER);
		String encodedPassword = "ENCODED_PASSWORD";

		Mockito.when(usersDAOMock
				.existsByEmail(Mockito.argThat(str -> (str != null) && (str.length() > 0) && (str.length() <= 30))))
				.thenReturn(false);
		Mockito.when(usersDAOMock
				.existsByName(Mockito.argThat(str -> (str != null) && (str.length() > 3) && (str.length() <= 30))))
				.thenReturn(false);
		Mockito.when(rolesDAOMock.findByName(Mockito.any(RoleType.class))).thenReturn(role);
		Mockito.when(passwordEncoderMock.encode(Mockito.anyString())).thenReturn(encodedPassword);
		Mockito.when(usersDAOMock.save(Mockito.any(User.class))).thenReturn(null);

		Long userId = null;
		User user = new User(userId, "Vano", "vano", "Vano@mail.ru", true, null);

		assertThrows(ServiceException.class, () -> usersService.saveUser(user),
				"Service exception expected when saving user returns null");
	}

	@Test
	public void whenSaveUserReturnsUserWithNullId() {
		Role role = new Role(1L, RoleType.ROLE_USER);
		String encodedPassword = "ENCODED_PASSWORD";
		Long savedId = null;

		Mockito.when(usersDAOMock
				.existsByEmail(Mockito.argThat(str -> (str != null) && (str.length() > 0) && (str.length() <= 30))))
				.thenReturn(false);
		Mockito.when(usersDAOMock
				.existsByName(Mockito.argThat(str -> (str != null) && (str.length() > 3) && (str.length() <= 30))))
				.thenReturn(false);
		Mockito.when(rolesDAOMock.findByName(Mockito.any(RoleType.class))).thenReturn(role);
		Mockito.when(passwordEncoderMock.encode(Mockito.anyString())).thenReturn(encodedPassword);
		Mockito.when(usersDAOMock.save(Mockito.any(User.class))).thenAnswer(invocation -> {
			User savedUser = invocation.getArgument(0, User.class);
			savedUser.setId(savedId);
			return savedUser;
		});

		Long userId = null;
		User user = new User(userId, "Vano", "vano", "Vano@mail.ru", true, null);

		assertThrows(ServiceException.class, () -> usersService.saveUser(user),
				"Service exception expected when saving user returns user with null id");
	}

	@Test
	public void whenSaveUserReturnsUserWithBadId() {
		Role role = new Role(1L, RoleType.ROLE_USER);
		String encodedPassword = "ENCODED_PASSWORD";
		Long savedId = 0L;

		Mockito.when(usersDAOMock
				.existsByEmail(Mockito.argThat(str -> (str != null) && (str.length() > 0) && (str.length() <= 30))))
				.thenReturn(false);
		Mockito.when(usersDAOMock
				.existsByName(Mockito.argThat(str -> (str != null) && (str.length() > 3) && (str.length() <= 30))))
				.thenReturn(false);
		Mockito.when(rolesDAOMock.findByName(Mockito.any(RoleType.class))).thenReturn(role);
		Mockito.when(passwordEncoderMock.encode(Mockito.anyString())).thenReturn(encodedPassword);
		Mockito.when(usersDAOMock.save(Mockito.any(User.class))).thenAnswer(invocation -> {
			User savedUser = invocation.getArgument(0, User.class);
			savedUser.setId(savedId);
			return savedUser;
		});

		Long userId = null;
		User user = new User(userId, "Vano", "vano", "Vano@mail.ru", true, null);

		assertThrows(ServiceException.class, () -> usersService.saveUser(user),
				"Service exception expected when saving user returns user with bad id");
	}

	@Test
	public void whenUpdateUser() throws ServiceException {
		User user = new User(1L, "Vano", "hashedPassword", "vano@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER)));
		SecurityUser securityUser = new UsersConverter().convertFromOneToTwo(user);

		final String updatedName = "Miko";

		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(passwordEncoderMock.encode(Mockito.argThat(str -> (str != null) && (str.length() > 3))))
				.thenReturn("hashedPassword");
		Mockito.when(usersDAOMock.save(Mockito.any(User.class))).thenAnswer(invocation -> {
			User updatedUser = invocation.getArgument(0, User.class);
			updatedUser.setName(updatedName);
			return updatedUser;
		});

		User userForUpdate = new User(1L, "Vano", "hashedPassword", "vano@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER)));

		assertEquals(updatedName, usersService.updateUser(userForUpdate).getName());

	}

	@Test
	public void whenUpdateUserOnNullUser() {
		User user = null;

		assertThrows(ServiceException.class, () -> usersService.updateUser(user),
				"Service exception expected when updating on null user");
	}

	@Test
	public void whenUpdateUserOnNullUserId() {
		Long userId = null;
		User user = new User(userId, "Vano", "hashedPassword", "vano@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER)));

		assertThrows(ServiceException.class, () -> usersService.updateUser(user),
				"Service exception expected when updating on null user id");
	}

	@Test
	public void whenUpdateUserIsDifferenttoSecurityUserOnIdOrRole() throws ServiceException {
		Long userId = 1L;
		RoleType userRoleType = RoleType.ROLE_USER;
		User user = new User(userId, "Vano", "hashedPassword", "vano@mail.ru", true,
				Set.of(new Role(2L, userRoleType)));
		SecurityUser securityUser = new UsersConverter().convertFromOneToTwo(user);

		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);

		Long userForUpdateId = 2L;
		User userForUpdate = new User(userForUpdateId, "Vano", "hashedPassword", "vano@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER)));

		assertThrows(ServiceException.class, () -> usersService.updateUser(userForUpdate),
				"Service exception expected when user id and security user id is different or user hasn't role admin");

	}

	@Test
	public void whenUpdateUserIsDifferenttoSecurityUserOnPassword() throws ServiceException {
		Long userId = 1L;
		RoleType userRoleType = RoleType.ROLE_USER;
		String userPassword = "HASHED_PASSWORD";
		User user = new User(userId, "Vano", userPassword, "vano@mail.ru", true, Set.of(new Role(2L, userRoleType)));
		SecurityUser securityUser = new UsersConverter().convertFromOneToTwo(user);

		final String updatedName = "Miko";

		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(passwordEncoderMock.encode(Mockito.argThat(str -> (str != null) && (str.length() > 3))))
				.thenReturn("hashedPassword");
		Mockito.when(usersDAOMock.save(Mockito.any(User.class))).thenAnswer(invocation -> {
			User updatedUser = invocation.getArgument(0, User.class);
			updatedUser.setName(updatedName);
			return updatedUser;
		});

		Long userForUpdateId = 1L;
		String passwordForUpdate = "NEW_PASSWORD";
		User userForUpdate = new User(userForUpdateId, "Vano", passwordForUpdate, "vano@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER)));

		assertEquals(updatedName, usersService.updateUser(userForUpdate).getName());

	}

	@Test
	public void whenUpdateUserAndReturnNullUser() throws ServiceException {
		Long userId = 1L;
		RoleType userRoleType = RoleType.ROLE_USER;
		String userPassword = "HASHED_PASSWORD";
		User user = new User(userId, "Vano", userPassword, "vano@mail.ru", true, Set.of(new Role(2L, userRoleType)));
		SecurityUser securityUser = new UsersConverter().convertFromOneToTwo(user);

		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(passwordEncoderMock.encode(Mockito.argThat(str -> (str != null) && (str.length() > 3))))
				.thenReturn("hashedPassword");
		Mockito.when(usersDAOMock.save(Mockito.any(User.class))).thenReturn(null);

		Long userForUpdateId = 1L;
		String passwordForUpdate = "NEW_PASSWORD";
		User userForUpdate = new User(userForUpdateId, "Vano", passwordForUpdate, "vano@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER)));

		assertThrows(ServiceException.class, () -> usersService.updateUser(userForUpdate),
				"ServiceException expected when null user returns on update");
	}

	@Test
	public void whenUpdateUserAndReturnUserWithNullId() throws ServiceException {
		Long userId = 1L;
		RoleType userRoleType = RoleType.ROLE_USER;
		String userPassword = "HASHED_PASSWORD";
		User user = new User(userId, "Vano", userPassword, "vano@mail.ru", true, Set.of(new Role(2L, userRoleType)));
		SecurityUser securityUser = new UsersConverter().convertFromOneToTwo(user);

		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(passwordEncoderMock.encode(Mockito.argThat(str -> (str != null) && (str.length() > 3))))
				.thenReturn("hashedPassword");
		Mockito.when(usersDAOMock.save(Mockito.any(User.class))).thenReturn(new User());

		Long userForUpdateId = 1L;
		String passwordForUpdate = "NEW_PASSWORD";
		User userForUpdate = new User(userForUpdateId, "Vano", passwordForUpdate, "vano@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER)));

		assertThrows(ServiceException.class, () -> usersService.updateUser(userForUpdate),
				"ServiceException expected when on update returns user with null id");

	}

	@Test
	public void whenUpdateUserAndReturnUserWithBadId() throws ServiceException {
		Long userId = 1L;
		RoleType userRoleType = RoleType.ROLE_USER;
		String userPassword = "HASHED_PASSWORD";
		User user = new User(userId, "Vano", userPassword, "vano@mail.ru", true, Set.of(new Role(2L, userRoleType)));
		SecurityUser securityUser = new UsersConverter().convertFromOneToTwo(user);

		Mockito.when(securityUsersServiceMock.getSecurityUserFromSecurityContext()).thenReturn(securityUser);
		Mockito.when(passwordEncoderMock.encode(Mockito.argThat(str -> (str != null) && (str.length() > 3))))
				.thenReturn("hashedPassword");
		Mockito.when(usersDAOMock.save(Mockito.any(User.class))).thenAnswer(invocation -> {
			User updatedUser = invocation.getArgument(0, User.class);
			updatedUser.setId(0L);
			return updatedUser;
		});

		Long userForUpdateId = 1L;
		String passwordForUpdate = "NEW_PASSWORD";
		User userForUpdate = new User(userForUpdateId, "Vano", passwordForUpdate, "vano@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER)));

		assertThrows(ServiceException.class, () -> usersService.updateUser(userForUpdate),
				"ServiceException expected when on update returns user with bad id");

	}

	@Test
	public void whenDeleteUser() {
		Long userId = 2L;

		Mockito.when(usersDAOMock.existsById(userId)).thenReturn(true);
		Mockito.doNothing().when(usersDAOMock).deleteById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)));

		assertDoesNotThrow(() -> usersService.deleteUser(userId), "Exception not expected when deleting user");
	}

	@Test
	public void whenDeleteUserOnNullId() {
		Long userId = null;

		Mockito.when(usersDAOMock.existsById(userId)).thenReturn(true);
		Mockito.doNothing().when(usersDAOMock).deleteById(Mockito.nullable(Long.class));

		assertThrows(ServiceException.class, () -> usersService.deleteUser(userId),
				"Service Exception expected when deleting user on null id");
	}

	@Test
	public void whenDeleteUserOnNonExistingId() {
		Long userId = 258L;

		Mockito.when(usersDAOMock.existsById(userId)).thenReturn(false);
		Mockito.doNothing().when(usersDAOMock).deleteById(Mockito.argThat(idVal -> (idVal != null) && (idVal > 0L)));

		assertThrows(ServiceException.class, () -> usersService.deleteUser(userId),
				"Service Exception expected when deleting user on non existing id");
	}
}

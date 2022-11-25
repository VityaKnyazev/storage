package by.itacademy.javaenterprise.knyazev.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import by.itacademy.javaenterprise.knyazev.dao.UsersDAO;
import by.itacademy.javaenterprise.knyazev.entities.Role;
import by.itacademy.javaenterprise.knyazev.entities.User;
import by.itacademy.javaenterprise.knyazev.security.SecurityUser;
import by.itacademy.javaenterprise.knyazev.security.converters.UsersConverter;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;
import by.itacademy.javaenterprise.knyazev.utils.RoleType;

public class SecurityUsersServiceTests {
	private UsersDAO usersDAOMock;

	private SecurityUsersService securityUsersService;

	@BeforeEach
	public void setUpBeforeEachTest() {
		usersDAOMock = Mockito.mock(UsersDAO.class);
		securityUsersService = new SecurityUsersService(usersDAOMock);
	}

	@Test
	public void whenLoadUserById() {
		Long userId = 12L;
		final String userName = "Miko";
		Role role = new Role(1L, RoleType.ROLE_USER);

		User userByName = new User(userId, "Miko", "Miko", userName, true, Set.of(role));

		Mockito.when(usersDAOMock.findByName(
				Mockito.argThat(strVal -> (strVal != null) && (strVal.length() > 3) && (strVal.length() <= 30))))
				.thenReturn(userByName);

		assertEquals(userName, securityUsersService.loadUserByUsername(userName).getUsername());
	}

	@Test
	public void whenLoadUserByIdReturnsNull() {
		final String userName = "Miko";

		User userByName = null;

		Mockito.when(usersDAOMock.findByName(Mockito.nullable(String.class))).thenReturn(userByName);

		assertThrows(UsernameNotFoundException.class, () -> securityUsersService.loadUserByUsername(userName),
				"Service exception expected when load user by user name returns null");
	}

	@Test
	public void whenGetSecurityUserFromSecurityContext() throws ServiceException {
		final Long userId = 12L;
		User user = new User(userId, "Vano", "hashedPassword", "vano@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER)));
		String[] authorities = user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList())
				.toArray(new String[0]);
		SecurityUser securityUser = new UsersConverter().convertFromOneToTwo(user);

		final String hashKey = "HASH_KEY";

		Authentication authentication = new AnonymousAuthenticationToken(hashKey, securityUser,
				AuthorityUtils.createAuthorityList(authorities));
		SecurityContext securityContext = new SecurityContextImpl(authentication);

		try (MockedStatic<SecurityContextHolder> securityContextHolder = Mockito
				.mockStatic(SecurityContextHolder.class)) {
			securityContextHolder.when(() -> SecurityContextHolder.getContext()).thenReturn(securityContext);

			assertEquals(userId, securityUsersService.getSecurityUserFromSecurityContext().getId());
		}
	}

	@Test
	public void whenGetSecurityUserFromSecurityContextOnNullAuthentication() {

		Authentication authentication = null;
		SecurityContext securityContext = new SecurityContextImpl(authentication);

		try (MockedStatic<SecurityContextHolder> securityContextHolder = Mockito
				.mockStatic(SecurityContextHolder.class)) {
			securityContextHolder.when(() -> SecurityContextHolder.getContext()).thenReturn(securityContext);

			assertThrows(ServiceException.class, () -> securityUsersService.getSecurityUserFromSecurityContext(),
					"Service exception expected when Authentication is null");
		}
	}

	@Test
	public void whenGetSecurityUserFromSecurityContextAndNonAuthenticated() {
		final Long userId = 0L;
		User unAuthenticatedUser = new User(userId, "Vano", "hashedPassword", "vano@mail.ru", true,
				Set.of(new Role(1L, RoleType.ROLE_USER)));
		String[] authorities = unAuthenticatedUser.getRoles().stream().map(role -> role.getName().name())
				.collect(Collectors.toList()).toArray(new String[0]);
		SecurityUser unAuthenticatedSecurityUser = new UsersConverter().convertFromOneToTwo(unAuthenticatedUser);

		final String hashKey = "HASH_KEY";

		Authentication authentication = new AnonymousAuthenticationToken(hashKey, unAuthenticatedSecurityUser,
				AuthorityUtils.createAuthorityList(authorities));
		authentication.setAuthenticated(false);
		SecurityContext securityContext = new SecurityContextImpl(authentication);

		try (MockedStatic<SecurityContextHolder> securityContextHolder = Mockito
				.mockStatic(SecurityContextHolder.class)) {
			securityContextHolder.when(() -> SecurityContextHolder.getContext()).thenReturn(securityContext);

			assertThrows(ServiceException.class, () -> securityUsersService.getSecurityUserFromSecurityContext(),
					"Service exception expected when security user not authenticated");
		}
	}

}
package by.itacademy.javaenterprise.knyazev.security;

import by.itacademy.javaenterprise.knyazev.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractUserFactory {
	User user;
	
	AbstractUserFactory(User user) {
		this.user = user;
	}
	
	public abstract SecurityUser create();
}

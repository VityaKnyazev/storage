package by.itacademy.javaenterprise.knyazev.spring.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@PropertySource(value = { "classpath:container.test.properties" })
public class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {	
	@Value("${test.db.url.key}")
	private String DB_URL;
	
	@Value("${test.db.username.key}")
	private String DB_USER;
	
	@Value("${test.db.password.key}")
	private String DB_PASSWORD;
	
	public PostgresContainer(String dockerImage) {
		super(dockerImage);
	}
	

	@Override
	public void start() {
		super.start();
		System.setProperty(DB_URL, getJdbcUrl());
		System.setProperty(DB_USER, getUsername());
		System.setProperty(DB_PASSWORD, getPassword());		
	}

	@Override
	public void stop() {
		super.stop();
	}

}

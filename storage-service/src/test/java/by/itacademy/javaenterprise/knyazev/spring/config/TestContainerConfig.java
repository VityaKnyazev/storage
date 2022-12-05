package by.itacademy.javaenterprise.knyazev.spring.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import by.itacademy.javaenterprise.knyazev.spring.beans.PostgresContainer;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

@Configuration
@PropertySource(value = { "classpath:container.test.properties" })
public class TestContainerConfig {
	@Value("${test.db.container}")
	private String POSTGRESQL_CONTAINER;
	@Value("${test.db.name}")
	private String DB_NAME;
	
	
	
	@SuppressWarnings("resource")
	@Bean(initMethod = "start", destroyMethod = "stop")
	public PostgresContainer postgresContainer() {
		return new PostgresContainer(POSTGRESQL_CONTAINER).withDatabaseName(DB_NAME);
	}
	
	@Bean(initMethod = "update", destroyMethod = "close")
	public Liquibase liquibase(PostgresContainer postgresContainer) throws DatabaseException, SQLException {
		String url = postgresContainer.getJdbcUrl();
		String username = postgresContainer.getUsername();
		String password = postgresContainer.getPassword();	
		
		
		Connection connection = DriverManager.getConnection(url, username, password);
		Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
		Liquibase liquibase = new Liquibase("/liquibase/database/dbchangelog.xml", new ClassLoaderResourceAccessor(), database);
		return liquibase;
	}
		
}
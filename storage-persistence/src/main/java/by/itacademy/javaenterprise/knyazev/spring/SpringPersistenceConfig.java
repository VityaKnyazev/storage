package by.itacademy.javaenterprise.knyazev.spring;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@Configuration
@EnableJpaRepositories(basePackages = { "by.itacademy.javaenterprise.knyazev.dao" })
@PropertySource(value = { "classpath:db.properties", "classpath:jpa.properties" })
@EnableTransactionManagement
public class SpringPersistenceConfig {
	@Autowired
	Environment env;

	@Bean
	HikariConfig hikariConfig() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(env.getProperty("db.driver"));
		hikariConfig.setJdbcUrl(env.getProperty("db.url"));
		hikariConfig.setUsername(env.getProperty("db.user"));
		hikariConfig.setPassword(env.getProperty("db.password"));
		hikariConfig.setMaximumPoolSize(env.getProperty("db.pool.size.max", Integer.class));
		
		return hikariConfig;
	}
	
	@Bean
	HikariDataSource dataSource() {
		return new HikariDataSource(hikariConfig());		
	}
	
	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactory.setPackagesToScan("by.itacademy.javaenterprise.knyazev.entities", "by.itacademy.javaenterprise.knyazev.embeddables");
			
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.show_sql", env.getProperty("hibernate.show.sql"));
		
		entityManagerFactory.setJpaProperties(jpaProperties);
		return entityManagerFactory;
	}
	
	@Bean
	JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory().getObject());
		return transactionManager;
	}
	
	@Bean
	TransactionTemplate transactionTemplate() {
		TransactionTemplate transactionTemplate = new TransactionTemplate();
		transactionTemplate.setTransactionManager(transactionManager());
		transactionTemplate.setTimeout(env.getProperty("transaction.timeout", Integer.class));
		
		return transactionTemplate;
	}
}

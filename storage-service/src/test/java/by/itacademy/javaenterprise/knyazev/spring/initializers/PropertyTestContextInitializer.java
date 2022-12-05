package by.itacademy.javaenterprise.knyazev.spring.initializers;


import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

public class PropertyTestContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {		
		String url = "db.url=" + System.getProperty("db.url");
		String user = "db.user=" + System.getProperty("db.user");
		String password = "db.password=" + System.getProperty("db.password");
		
		TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, url, user, password);
	}
	
}
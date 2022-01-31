package by.itacademy.javaenterprise.knyazev.spring;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import exceptions.handlers.ControllersExceptionHandler;

public class WebDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {WebSecurityConfig.class, ControllersExceptionHandler.class, SpringServiceConfig.class, SpringPersistenceConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {SpringMVCConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}	

}

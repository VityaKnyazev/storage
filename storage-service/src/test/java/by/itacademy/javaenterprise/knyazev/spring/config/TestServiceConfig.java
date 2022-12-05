package by.itacademy.javaenterprise.knyazev.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import by.itacademy.javaenterprise.knyazev.dao.CategoriesDAO;
import by.itacademy.javaenterprise.knyazev.dao.GoodsDAO;
import by.itacademy.javaenterprise.knyazev.services.CategoriesService;

@Configuration
public class TestServiceConfig {
	
	@Bean
	CategoriesService categoriesService(CategoriesDAO categoriesDAO, GoodsDAO goodsDAO) {
		return new CategoriesService(categoriesDAO, goodsDAO);
	}

}

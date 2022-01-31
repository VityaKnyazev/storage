package by.itacademy.javaenterprise.knyazev.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import by.itacademy.javaenterprise.knyazev.entities.Category;

public interface CategoriesDAO extends JpaRepository<Category, Long> {
	
	public Category findByName(String name);
	
}
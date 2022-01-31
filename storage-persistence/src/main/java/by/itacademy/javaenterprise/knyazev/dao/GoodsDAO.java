package by.itacademy.javaenterprise.knyazev.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import by.itacademy.javaenterprise.knyazev.entities.Good;


public interface GoodsDAO extends JpaRepository<Good, Long>{	
	
	@Query(name = "countGoodsByCategoryId")
	public Long findCountByCategoryId(Long categoryId);
	
	public List<Good> findByNameAndSort(String name, String sort);
}

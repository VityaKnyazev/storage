package by.itacademy.javaenterprise.knyazev.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import by.itacademy.javaenterprise.knyazev.entities.Storehouse;

public interface StorehouseDAO extends JpaRepository<Storehouse, Long>{
	public Storehouse findByGoodId(Long goodId);
}

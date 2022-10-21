package by.itacademy.javaenterprise.knyazev.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import by.itacademy.javaenterprise.knyazev.entities.Purchase;
import by.itacademy.javaenterprise.knyazev.utils.Status;

public interface PurchasesDAO extends JpaRepository<Purchase, Long>{
	public List<Purchase> findByUserIdAndStatus(Long userId, Status status);
	public List<Purchase> findByStatus(Status status);
}
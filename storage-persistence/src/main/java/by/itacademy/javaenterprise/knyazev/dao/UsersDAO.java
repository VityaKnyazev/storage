package by.itacademy.javaenterprise.knyazev.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.itacademy.javaenterprise.knyazev.entities.User;

@Repository
public interface UsersDAO extends JpaRepository<User, Long> {
	public User findByName(String name);
}
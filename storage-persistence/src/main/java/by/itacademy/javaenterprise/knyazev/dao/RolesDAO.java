package by.itacademy.javaenterprise.knyazev.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.itacademy.javaenterprise.knyazev.entities.Role;
import by.itacademy.javaenterprise.knyazev.utils.RoleType;

@Repository
public interface RolesDAO extends JpaRepository<Role, Long> {
	public Role findByName(RoleType name);
	public boolean existsByName(RoleType name);
	
}
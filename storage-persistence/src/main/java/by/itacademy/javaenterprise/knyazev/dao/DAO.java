package by.itacademy.javaenterprise.knyazev.dao;

import java.util.List;

import by.itacademy.javaenterprise.knyazev.dao.exceptions.DAOException;

public interface DAO<T> {
	Long save(T object) throws DAOException;
	T find(Long id) throws DAOException;
	List<T>findAll();
	void update(T object) throws DAOException;
	void delete(T object) throws DAOException;
}

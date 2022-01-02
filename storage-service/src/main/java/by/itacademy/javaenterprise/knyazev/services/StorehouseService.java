package by.itacademy.javaenterprise.knyazev.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import by.itacademy.javaenterprise.knyazev.dao.StorehouseDAO;
import by.itacademy.javaenterprise.knyazev.entities.Storehouse;

@Service
public class StorehouseService {
	@Autowired
	private StorehouseDAO storehouseDAO;
	

	@Transactional
	public List<Storehouse> showAll() {
		storehouseDAO.findAll();
		return null;
	}
}

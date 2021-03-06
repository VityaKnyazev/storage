package by.itacademy.javaenterprise.knyazev.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.itacademy.javaenterprise.knyazev.entities.Producer;

@Repository
public interface ProducersDAO extends JpaRepository<Producer, Long> {

}

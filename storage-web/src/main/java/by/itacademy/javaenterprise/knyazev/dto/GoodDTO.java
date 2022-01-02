package by.itacademy.javaenterprise.knyazev.dto;

import org.springframework.stereotype.Component;

import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.entities.Producer;
import lombok.Data;

@Data
@Component
public class GoodDTO {
	private Long id;
	private String name;
	private String sort;
	private String description;
	private Category category;
	private Producer producer;
}
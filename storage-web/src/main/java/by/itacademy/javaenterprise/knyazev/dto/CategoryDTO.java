package by.itacademy.javaenterprise.knyazev.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import by.itacademy.javaenterprise.knyazev.entities.Good;
import lombok.Data;

@Data
@Component
public class CategoryDTO {
	private Long goodsCount;
	private Long id;
	private String name;
	private String description;
	private List<Good> goods;
}
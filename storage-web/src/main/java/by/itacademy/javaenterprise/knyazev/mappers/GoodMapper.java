package by.itacademy.javaenterprise.knyazev.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import by.itacademy.javaenterprise.knyazev.dto.GoodDTO;
import by.itacademy.javaenterprise.knyazev.entities.Good;

@Mapper(componentModel = "spring")
public interface GoodMapper {
	
	@Mapping(target = "category.goods", ignore = true)
	@Mapping(target = "producer.goods", ignore = true)
	public GoodDTO toDTO(Good good);
	
	public List<GoodDTO> toListDTO(List<Good> goods); 
	
	
}

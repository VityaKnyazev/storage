package by.itacademy.javaenterprise.knyazev.mappers;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import by.itacademy.javaenterprise.knyazev.dto.StorehouseDTO;
import by.itacademy.javaenterprise.knyazev.entities.Storehouse;

@Mapper(componentModel = "spring")
public interface StorehouseMapper {
	@Mappings({
	@Mapping(target = "good.producer.goods", ignore = true),
	@Mapping(target = "good.category.goods", ignore = true),
	@Mapping(target = "good.storehouse", ignore = true),
	@Mapping(target = "dateTime", dateFormat = "dd.MM.yyyy HH:mm"),
	@Mapping(target = "quantity", numberFormat = "######.000"),
	@Mapping(target = "price", numberFormat = "#########.00")
	})
	public StorehouseDTO toDTO(Storehouse storehouse);
	
	@Mappings({
		@Mapping(target = "good.storehouse", ignore = true),
		@Mapping(target = "good.category.goods", ignore = true),
		@Mapping(target = "good.producer.goods", ignore = true)
		})
	@InheritInverseConfiguration
	public Storehouse toStorehouse(StorehouseDTO storehouseDTO);
	
	public List<StorehouseDTO> toListDTO(List<Storehouse> storehouseMult);
}
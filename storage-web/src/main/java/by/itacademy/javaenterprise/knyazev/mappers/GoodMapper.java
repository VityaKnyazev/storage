package by.itacademy.javaenterprise.knyazev.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import by.itacademy.javaenterprise.knyazev.dto.GoodDTO;
import by.itacademy.javaenterprise.knyazev.entities.Good;

@Mapper(componentModel = "spring")
public interface GoodMapper {
	@Mappings({ @Mapping(target = "category.goods", ignore = true),
			@Mapping(target = "producer.goods", ignore = true),
			@Mapping(target = "storehouse", source = "storehouse.id")
	})
	public GoodDTO toDTO(Good good);

	@Mappings({ @Mapping(target = "category.goods", ignore = true),
			@Mapping(target = "producer.goods", ignore = true),
			@Mapping(target = "storehouse", ignore = true)
	})
	public Good toGood(GoodDTO goodDTO);

	public List<GoodDTO> toListDTO(List<Good> goods);
}

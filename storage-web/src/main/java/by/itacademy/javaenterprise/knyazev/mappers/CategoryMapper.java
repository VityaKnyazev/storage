package by.itacademy.javaenterprise.knyazev.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import by.itacademy.javaenterprise.knyazev.dto.CategoryDTO;
import by.itacademy.javaenterprise.knyazev.entities.Category;
import by.itacademy.javaenterprise.knyazev.entities.Good;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	@Mappings({ @Mapping(target = "goodsCount", ignore = true), @Mapping(target = "goods.category", ignore = true),
			@Mapping(target = "goods.producer.goods", ignore = true) })
	public CategoryDTO toDTO(Category category);


	@Mapping(target = "goods", ignore = true)
	public Category toCategory(CategoryDTO categoryDTO);

	default List<CategoryDTO> toDTOList(Map<Category, Long> categoriesGoodsQuantity) {
		if (categoriesGoodsQuantity == null || categoriesGoodsQuantity.isEmpty()) {
			return null;
		}

		List<CategoryDTO> caegoriesDTO = new ArrayList<>();

		categoriesGoodsQuantity.forEach((c, q) -> {
			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setId(c.getId());
			categoryDTO.setName(c.getName());
			categoryDTO.setDescription(c.getDescription());
			categoryDTO.setGoodsCount(q);

			caegoriesDTO.add(categoryDTO);
		});

		return caegoriesDTO;

	}

	@Mappings({ @Mapping(target = "category", ignore = true), @Mapping(target = "producer.goods", ignore = true) })
	public Good ignoreCategoryAndGoodsInProducer(Good good);

}

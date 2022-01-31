package by.itacademy.javaenterprise.knyazev.mappers;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import by.itacademy.javaenterprise.knyazev.dto.PurchaseDTO;
import by.itacademy.javaenterprise.knyazev.entities.Purchase;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
	@Mappings({
	@Mapping(target = "storehouseId", source = "storehouse.id"),
	@Mapping(target = "goodId", source = "storehouse.good.id"),
	@Mapping(target = "goodName", source = "storehouse.good.name"),
	@Mapping(target = "goodSort", source = "storehouse.good.sort"),
	@Mapping(target = "categoryName", source = "storehouse.good.category.name"),
	@Mapping(target = "dateTime", dateFormat = "dd.MM.yyyy HH:mm"),
	@Mapping(target = "quantity", numberFormat = "######.000"),
	@Mapping(target = "price", numberFormat = "#########.00")
	})
	public PurchaseDTO toDTO(Purchase purchase);
	
	@Mappings({
		@Mapping(target = "status", ignore = true), 
		@Mapping(target = "user", ignore = true)
		})
	@InheritInverseConfiguration
	public Purchase toPurchase(PurchaseDTO purchaseDTO);
	
	public List<PurchaseDTO> toListDTO(List<Purchase> purchases);
}
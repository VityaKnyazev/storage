package by.itacademy.javaenterprise.knyazev.mappers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import by.itacademy.javaenterprise.knyazev.dto.PurchaseDTO;
import by.itacademy.javaenterprise.knyazev.dto.ReportDTO;
import by.itacademy.javaenterprise.knyazev.entities.Purchase;

@Mapper(componentModel = "spring")
public abstract class ReportMapper {
	@Autowired
	PurchaseMapper purchaseMapperImpl;
	
	public ReportDTO toDTO(List<Purchase> purchases) {
		ReportDTO reportDTO = new ReportDTO();
		List<BigDecimal> elemetnsMultiply = new ArrayList<>();
		
		purchases.forEach(p -> {
			elemetnsMultiply.add(p.getQuantity().multiply(p.getPrice()));
		});
		
		Optional<BigDecimal> totalOutcome = elemetnsMultiply.stream().reduce((a,b) -> a.add(b));
		
		List<PurchaseDTO> purchasesDTO = purchaseMapperImpl.toListDTO(purchases);
		
		reportDTO.setPurchases(purchasesDTO);
		reportDTO.setTotalOutcome(createDecimalFormat("###############.00").format(totalOutcome.get()));
		return reportDTO;
	}
	
	private DecimalFormat createDecimalFormat(String numberFormat) {
        DecimalFormat df = new DecimalFormat(numberFormat);
        df.setParseBigDecimal(true);
        return df;
    }

}
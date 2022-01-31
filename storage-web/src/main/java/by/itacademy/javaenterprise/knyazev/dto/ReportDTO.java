package by.itacademy.javaenterprise.knyazev.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReportDTO {
	private List<PurchaseDTO> purchases;
	private String totalOutcome;
}

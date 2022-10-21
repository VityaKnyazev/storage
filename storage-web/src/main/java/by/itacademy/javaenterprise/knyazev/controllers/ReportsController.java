package by.itacademy.javaenterprise.knyazev.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import by.itacademy.javaenterprise.knyazev.dto.ReportDTO;
import by.itacademy.javaenterprise.knyazev.exceptions.ControllerException;
import by.itacademy.javaenterprise.knyazev.mappers.ReportMapper;
import by.itacademy.javaenterprise.knyazev.services.PurchasesService;
import by.itacademy.javaenterprise.knyazev.services.exceptions.ServiceException;

@RestController
@Validated
public class ReportsController {
	private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);
	@Autowired
	PurchasesService purchasesService;
	@Autowired
	ReportMapper reportMapperImpl;
	

	@GetMapping("/reports")
	public ReportDTO getAll() throws ControllerException {
		try {
			return reportMapperImpl.toDTO(purchasesService.showAllBought());
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new ControllerException("Error on showing purchases", e);
		}
	}

}
package by.itacademy.javaenterprise.knyazev.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import by.itacademy.javaenterprise.knyazev.dto.GoodDTO;
import by.itacademy.javaenterprise.knyazev.mappers.GoodMapper;
import by.itacademy.javaenterprise.knyazev.services.GoodsService;

@RestController
public class GoodsController {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodMapper goodMapper;
	
	@GetMapping("/goods")
	public List<GoodDTO> getAll() {
		List<GoodDTO> goods = goodMapper.toListDTO(goodsService.showAll());
		return goods;
	}
}
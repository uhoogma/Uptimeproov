package com.example.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Data;
import com.example.service.CurrencyService;
import com.example.service.ProductService;

@RestController
@RequestMapping("tabledata")
public class MainController {

	private CurrencyService currencyService = new CurrencyService();	
	private ProductService productService = new ProductService();
	private int recordsPerPage = 13;
	
	@RequestMapping("/")
	public String home() {
		return "index";
	}
	
	@RequestMapping(value = "/currency/{target}", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Double> getCurrencyConversion(@PathVariable String target) {
		return currencyService.getCurrencies(target);
	}
	
	@RequestMapping(value = "/{id}/{keywords}", method = RequestMethod.POST, produces = "application/json")
	public Data getItemsInJSON(@PathVariable String id , @PathVariable String keywords) {
		int start = (Integer.parseInt(id) - 1) * recordsPerPage;
		int limit = start + 2* recordsPerPage;
		// System.out.println("start "+ start + " limit "+ limit);
		if (keywords != null) {
			return productService.fetchProducts(start, limit, keywords);
		}
		return new Data();
	}

	@RequestMapping(value = "/next/{id}", method = RequestMethod.POST, produces = "application/json")
	public Data getNextItems(@PathVariable String id) {
		int start = (Integer.parseInt(id) - 1) * recordsPerPage;
		int limit = start + recordsPerPage;
		// System.out.println("start "+ start + " limit "+ limit);
		return productService.fetchProducts(start, limit, "");
	}
}

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
public class DataController {

	private CurrencyService currencyService = new CurrencyService();
	private ProductService productService = new ProductService();
	private int recordsPerPage = 13;

	@RequestMapping(value = "/currency/{target}", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Double> getCurrencyConversion(@PathVariable String target) {
		return currencyService.getCurrencies(target);
	}

	/**
	 * Get first 26 records
	 */
	@RequestMapping(value = "/{id}/{keywords}", method = RequestMethod.POST, produces = "application/json")
	public Data getItemsInJSON(@PathVariable String id, @PathVariable String keywords) {
		int start = (Integer.parseInt(id) - 1) * recordsPerPage;
		int limit = start + 2 * recordsPerPage;
		if (keywords != null) {
			return productService.fetchProducts(start, limit, keywords);
		}
		return new Data();
	}

	/**
	 * Get next 13 records
	 */
	@RequestMapping(value = "/next/{id}/{keywords}", method = RequestMethod.POST, produces = "application/json")
	public Data getNextItems(@PathVariable String id, @PathVariable String keywords) {
		int start = (Integer.parseInt(id) - 1) * recordsPerPage;
		int limit = start + recordsPerPage;
		return productService.fetchProducts(start, limit, keywords);
	}
}

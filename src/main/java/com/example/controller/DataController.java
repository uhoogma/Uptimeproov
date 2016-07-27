package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Data;
import com.example.model.Item;
import com.example.service.CurrencyService;

@RestController
@RequestMapping("tabledata")
public class DataController {

	CurrencyService currencyService = new CurrencyService();	

	@RequestMapping(value = "/currency/{target}", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Double> getCurrencyConversion(@PathVariable String target) {
		return currencyService.getCurrencies(target);
	}

	public final int maxCount = 137;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public Data getItemInJSON(@PathVariable String id) {
		int i = (Integer.parseInt(id) - 1) * 13;
		List<Item> list = new ArrayList<>();
		int limit = i + 26;
		if (limit > maxCount) {
			limit = maxCount;
		}
		for (; i < limit; i++) {
			Item item = new Item();
			item.setTitle("MINGI NIMI" + i);
			item.setIsan("SGSGFDFFDBDFBA" + i);
			item.setPrice((2345 + i * 2345 / 10));
			item.setCurrency("GBP");
			list.add(item);
		}
		Data data = new Data(maxCount, list);
		return data;
	}

	@RequestMapping(value = "/next/{id}", method = RequestMethod.GET, produces = "application/json")
	public Data getItemInJSON2(@PathVariable String id) {
		int i = (Integer.parseInt(id) - 1) * 13;
		List<Item> list = new ArrayList<>();
		int limit = i + 13;
		if (limit > maxCount) {
			limit = maxCount;
		}
		for (; i < limit; i++) {
			Item item = new Item();
			item.setTitle("MINGI NIMI" + i);
			item.setIsan("SGSGFDFFDBDFBA" + i);
			item.setPrice((2345 + i * 2345 / 10));
			item.setCurrency("GBP");
			list.add(item);
		}
		Data data = new Data(maxCount, list);
		return data;
	}
}

/**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file 
 * except in compliance with the License. A copy of the License is located at
 *
 *       http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License. 
 *
 * ********************************************************************************************
 *
 *  Amazon Product Advertising API
 *  Signed Requests Sample Code
 *
 *  API Version: 2009-03-31
 *
 */

package com.example.service;

import com.example.amazon.SignedRequestsHelper;
import com.example.conf.PropertyLoader;
import com.example.model.Data;
import com.example.model.Item;
import com.example.model.Pair;
import com.example.model.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ProductService {

	static PropertyLoader propertyLoader = new PropertyLoader();
	private static final String AWS_ACCESS_KEY_ID = propertyLoader.getAwsAccessKeyId();
	private static final String AWS_SECRET_KEY = propertyLoader.getAwsSecretKey();
	private static final String AWS_ASSOCIATE_TAG = propertyLoader.getAwsAssociateTag();
	private static final int PAGINATION_LIMIT = 5;
	private static final String ENDPOINT = "ecs.amazonaws.co.uk";

	public Data fetchProducts(int start, int count, String keywords) {
		SignedRequestsHelper helper;
		try {
			helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY, AWS_ASSOCIATE_TAG);
		} catch (Exception e) {
			e.printStackTrace();
			return new Data();
		}

		int startPage = (start / 10) + 1;
		int lastPage = ((start + count) / 10) + 1;

		List<Item> itemsToReturn = new ArrayList<>();
		int itemCount = 0;
		for (int itemPage = startPage; itemPage <= lastPage; itemPage++) {
			if (itemPage <= PAGINATION_LIMIT) {
				mandatoryWait();
				int init = (itemPage == startPage) ? start - ((startPage - 1) * 10) : 0;
				int last = (itemPage == lastPage) ? start + count - ((lastPage - 1) * 10) : 10;
				Range range = new Range(init, last);
				// System.out.println("i "+ i + " init "+ init+ " last "+last);
				Data data = fetch(helper, itemPage, range, keywords);
				List<Item> items = data.getItemList();
				itemCount = data.getItemCount();
				itemsToReturn.addAll(items);
			}
		}
		return new Data(itemCount, itemsToReturn);
	}

	private Data fetch(SignedRequestsHelper helper, int itemPage, Range range, String keywords) {
		String requestUrl, requestUrlForPrices;

		Map<String, String> params = fillParameters(itemPage, keywords);

		requestUrl = helper.sign(params);
		mandatoryWait();

		List<Item> items = fetchItems(requestUrl, range).getItemList();
		int count = fetchItems(requestUrl, range).getItemCount();

		String itemList = "";
		for (Item item : items) {
			itemList = itemList + item.getAsin() + ",";
		}

		String queryStringForPrices = "Service=AWSECommerceService&Version=2009-03-31&Operation=ItemLookup&ResponseGroup=Offers&ItemId="
				+ itemList.substring(0, itemList.length() - 1);
		requestUrlForPrices = helper.sign(queryStringForPrices);
		mandatoryWait();

		Map<String, Pair> map = fetchAmount(requestUrlForPrices);
		for (Item item : items) {
			Pair pair = map.get(item.getAsin());
			if (pair != null) {
				String price = pair.getAmount();
				String currency = pair.getCurrencyCode();
				if (price.isEmpty()) {
					item.setPrice(0);
				} else {
					item.setPrice(new Integer(price));
				}
				item.setCurrency(currency);
			}
		}
		Data data = new Data(count, items);
		return data;
	}

	private Map<String, String> fillParameters(int itemPage, String keywords) {
		Map<String, String> params = new HashMap<>();
		params.put("Service", "AWSECommerceService");
		params.put("Version", "2009-03-31");
		params.put("Operation", "ItemSearch");
		params.put("SearchIndex", "All");
		params.put("Availability", "Available");
		params.put("ItemPage", String.valueOf(itemPage));
		params.put("Keywords", keywords);
		params.put("ResponseGroup", "Small");
		return params;
	}

	private Data fetchItems(String requestUrl, Range range) {
		String title, asin;
		List<Item> items = new ArrayList<Item>();
		String results;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(requestUrl);
			results = doc.getElementsByTagName("TotalResults").item(0).getTextContent();
			for (int i = range.getStart(); i < range.getEnd(); i++) {
				Item item = new Item();
				Node itemNode = doc.getElementsByTagName("Item").item(i);
				if (itemNode instanceof Element) {
					Element docElement = (Element) itemNode;
					asin = docElement.getElementsByTagName("ASIN").item(0).getTextContent();
					item.setAsin(asin);
				}
				if (itemNode instanceof Element) {
					Element docElement2 = (Element) itemNode;
					Node itemAttributes = docElement2.getElementsByTagName("ItemAttributes").item(0);
					if (itemAttributes instanceof Element) {
						Element docElement3 = (Element) itemAttributes;
						title = docElement3.getElementsByTagName("Title").item(0).getTextContent();
						item.setTitle(title);
					}
				}
				items.add(item);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new Data(Integer.parseInt(results), items);
	}

	private Map<String, Pair> fetchAmount(String requestUrl) {
		String price = "", asin = "", currency = "";
		Map<String, Pair> map = new HashMap<String, Pair>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(requestUrl);
			for (int i = 0; i < doc.getElementsByTagName("Item").getLength(); i++) {
				Pair pair = new Pair();
				Node itemNode = doc.getElementsByTagName("Item").item(i);
				if (itemNode instanceof Element) {
					Element docElement = (Element) itemNode;
					asin = docElement.getElementsByTagName("ASIN").item(0).getTextContent();
				}
				if (itemNode instanceof Element) {
					Element docElement2 = (Element) itemNode;
					Node offerSummary = docElement2.getElementsByTagName("OfferSummary").item(0);
					if (offerSummary instanceof Element) {
						Element docElement3 = (Element) offerSummary;
						Node lowestNewPrice = docElement3.getElementsByTagName("LowestNewPrice").item(0);
						if (lowestNewPrice instanceof Element) {
							Element docElement4 = (Element) lowestNewPrice;
							price = docElement4.getElementsByTagName("Amount").item(0).getTextContent();
							currency = docElement4.getElementsByTagName("CurrencyCode").item(0).getTextContent();
							if (price != null && currency != null) {
								pair.setAmount(price);
								pair.setCurrencyCode(currency);
								map.put(asin, pair);
							}
						}
					}
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return map;
	}

	private void mandatoryWait() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

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

/**
 * Based on Amazon's sample code (see copyright above)
 */
public class ProductService {

    private static PropertyLoader propertyLoader = new PropertyLoader();
    private static final String AWS_ACCESS_KEY_ID = propertyLoader.getAwsAccessKeyId();
    private static final String AWS_SECRET_KEY = propertyLoader.getAwsSecretKey();
    private static final String AWS_ASSOCIATE_TAG = propertyLoader.getAwsAssociateTag();
    /**
     * Maximum page count if parameter "SearchIndex" is set to "All"
     */
    private static final int PAGINATION_LIMIT = 5;
    private static final String ENDPOINT = "ecs.amazonaws.co.uk";

    /**
     * Since I need to fetch 13 or 26 products and I can load only 10 products
     * at the time, I must calculate ranges and perform several queries
     */
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
                Data data = fetch(helper, itemPage, range, keywords);
                List<Item> items = data.getItemList();
                itemCount = data.getItemCount();
                itemsToReturn.addAll(items);
            }
        }
        return new Data(itemCount, itemsToReturn);
    }

    /**
     * Users can only make 1 query/second otherwise Amazon returns errorcode 503
     */
    private void mandatoryWait() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Fetching up to 10 items at the time
     */
    private Data fetch(SignedRequestsHelper helper, int itemPage, Range range, String keywords) {
        String requestUrl;

        Map<String, String> params = fillParameters(itemPage, keywords);

        requestUrl = helper.sign(params);
        mandatoryWait();

        Data fetched = fetchItems(requestUrl, range);
        List<Item> items = fetched.getItemList();
        int count = fetched.getItemCount();

        fetchPrices(helper, items);
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
    
    /**
     * Fetching item details
     */
    private Data fetchItems(String requestUrl, Range range) {
        String title = null, asin = null, detailPageURL = null;
        List<Item> items = new ArrayList<Item>();
        String results;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            results = doc.getElementsByTagName("TotalResults").item(0).getTextContent();
            int itemCount = doc.getElementsByTagName("Item").getLength();
            for (int i = range.getStart(); i < range.getEnd() && i < itemCount; i++) {
                Item item = new Item();
                Node itemNode = doc.getElementsByTagName("Item").item(i);
                if (itemNode instanceof Element) {
                    Element docElement = (Element) itemNode;
                    asin = docElement.getElementsByTagName("ASIN").item(0).getTextContent();
                    item.setAsin(asin);
                    detailPageURL = docElement.getElementsByTagName("DetailPageURL").item(0).getTextContent();
                    item.setDetailPageURL(detailPageURL);
                }
                if (itemNode instanceof Element) {
                    Element docElement = (Element) itemNode;
                    Node itemAttributes = docElement.getElementsByTagName("ItemAttributes").item(0);
                    if (itemAttributes instanceof Element) {
                        Element docElement2 = (Element) itemAttributes;
                        title = docElement2.getElementsByTagName("Title").item(0).getTextContent();
                        item.setTitle(title);
                    }
                }
                if (asin != null && title != null && detailPageURL != null) {
                    items.add(item);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Data(Integer.parseInt(results), items);
    }

    /**
     * Preparing price query
     */
    private void fetchPrices(SignedRequestsHelper helper, List<Item> items) {
        String requestUrlForPrices;
        String itemList = "";
        for (Item item : items) {
            itemList = itemList + item.getAsin() + ",";
        }

        if (itemList.length() > 0) {
            itemList = itemList.substring(0, itemList.length() - 1);

            String queryStringForPrices = "Service=AWSECommerceService&Version=2009-03-31&Operation=ItemLookup&ResponseGroup=Offers&ItemId="
                    + itemList;
            requestUrlForPrices = helper.sign(queryStringForPrices);
            mandatoryWait();

            Map<String, Pair> map = fetchAmount(requestUrlForPrices);
            for (Item item : items) {
                Pair pair = map.get(item.getAsin());
                if (pair != null) {
                    setPrice(item, pair);
                    setCurrency(item, pair);
                }
            }
        }
    }

    /**
     * Fetching price details
     */
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
                    Element level = (Element) itemNode;
                    asin = level.getElementsByTagName("ASIN").item(0).getTextContent();
                }
                if (itemNode instanceof Element) {
                    Element level = (Element) itemNode;
                    Node offerSummary = level.getElementsByTagName("OfferSummary").item(0);
                    if (offerSummary instanceof Element) {
                        Element level2 = (Element) offerSummary;
                        Node lowestNewPrice = level2.getElementsByTagName("LowestNewPrice").item(0);
                        if (lowestNewPrice instanceof Element) {
                            Element level3 = (Element) lowestNewPrice;
                            price = level3.getElementsByTagName("Amount").item(0).getTextContent();
                            currency = level3.getElementsByTagName("CurrencyCode").item(0).getTextContent();
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
    
    /**
     * Setting price, default is 0
     */
    private void setPrice(Item item, Pair pair) {
        String price = pair.getAmount();
        if (price.isEmpty()) {
            item.setPrice(0);
        } else {
            item.setPrice(new Integer(price));
        }
    }

    /**
     * Setting currency, default is "GBP"
     */
    private void setCurrency(Item item, Pair pair) {
        String currency = pair.getCurrencyCode();
        if (currency.isEmpty()) {
            item.setCurrency("GBP");
        } else {
            item.setCurrency(currency);
        }
    }
}

package com.example.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.conf.PropertyLoader;

public class CurrencyService {

	private static PropertyLoader propertyLoader = new PropertyLoader();
	private static final String CURRENCY_ENDPOINT = "http://www.apilayer.net/api/live?access_key=";
	private static final String API_KEY = propertyLoader.getCurrencylayerApiKey();

	/**
	 * Currencylayer's free plan only returns exchange rate between "USD" and
	 * other currencies. Since in my case input currency is "GBP" I must first
	 * recalculate the entire map
	 */
	public Map<String, Double> getCurrencies(String target) {
		try {
			JSONObject obj = new JSONObject(callURL(CURRENCY_ENDPOINT + API_KEY));
			JSONObject quotes = obj.getJSONObject("quotes");
			String defaultCurrency = obj.getString("source");
			Map<String, Double> map = processJSON(quotes);
			Double rate = map.get(defaultCurrency + target);
			Map<String, Double> mappedToTargetCurrency = new HashMap<>();
			mappedToTargetCurrency = mapToTargetCurrency(quotes, rate);
			return mappedToTargetCurrency;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new HashMap<String, Double>();
	}

	/**
	 * HTTP call with Java's built-in resources
	 * 
	 * @see <a href=
	 *      "http://crunchify.com/java-url-example-getting-text-from-url/">http:
	 *      //crunchify.com/java-url-example-getting-text-from-url/</a>
	 */
	public String callURL(String myURL) {
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			if (in != null) {
				in.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:" + myURL, e);
		}

		return sb.toString();
	}

	/**
	 * Getting values from JSON
	 */
	private Map<String, Double> processJSON(JSONObject quotes) {
		Map<String, Double> map = new HashMap<String, Double>();
		Iterator<String> iter = quotes.keys();
		while (iter.hasNext()) {
			String key = ((String) iter.next()).toString();
			Object moneyAsObject = quotes.get(key);
			Double value = moneyAsObject instanceof Double ? value = (Double) quotes.get(key)
					: new Double((Integer) quotes.get(key));
			map.put(key, value);
		}
		return map;
	}

	/**
	 * Method that returns final map that is sorted by abbreviations
	 */
	private Map<String, Double> mapToTargetCurrency(JSONObject quotes, Double rate) {
		Map<String, Double> mappedToTargetCurrency = new HashMap<>();
		Iterator<String> iter = quotes.keys();
		while (iter.hasNext()) {
			String key = ((String) iter.next()).toString();
			String targetCurrency = key.substring(3, key.length());
			Object moneyAsObject = quotes.get(key);
			Double value = moneyAsObject instanceof Double ? value = (Double) quotes.get(key)
					: new Double((Integer) quotes.get(key));
			double finalRate = value / rate;
			mappedToTargetCurrency.put(targetCurrency, finalRate);
		}
		return new TreeMap<String, Double>(mappedToTargetCurrency);
	}
}

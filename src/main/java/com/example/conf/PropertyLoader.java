package com.example.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// http://alvinalexander.com/java/jwarehouse/axis2-1.3/modules/samples/deprecated/googlesearch/src/sample/google/common/util/PropertyLoader.java.shtml
public class PropertyLoader {

	private String currencylayerApiKey;

	public String getCurrencylayerApiKey() {
		return currencylayerApiKey;
	}

	public void setCurrencylayerApiKey(String currencylayerApiKey) {
		this.currencylayerApiKey = currencylayerApiKey;
	}

	public PropertyLoader() {
		try {
			Properties prop = new Properties();
			InputStream stream = this.getClass().getResourceAsStream("/com/example/conf/application.properties");
			prop.load(stream);
			this.currencylayerApiKey = prop.getProperty("currencylayer.apikey");
		} catch (IOException e) {
			System.exit(0);
		}
	}
}

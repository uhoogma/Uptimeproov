package com.example.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// http://alvinalexander.com/java/jwarehouse/axis2-1.3/modules/samples/deprecated/googlesearch/src/sample/google/common/util/PropertyLoader.java.shtml
public class PropertyLoader {

	private String currencylayerApiKey;
	private String awsAccessKeyId;
	private String awsSecretKey;
	private String awsAssociateTag;
	
	public String getAwsAccessKeyId() {
		return awsAccessKeyId;
	}

	public void setAwsAccessKeyId(String awsAccessKeyId) {
		this.awsAccessKeyId = awsAccessKeyId;
	}

	public String getAwsSecretKey() {
		return awsSecretKey;
	}

	public void setAwsSecretKey(String awsSecretKey) {
		this.awsSecretKey = awsSecretKey;
	}

	public String getAwsAssociateTag() {
		return awsAssociateTag;
	}

	public void setAwsAssociateTag(String awsAssociateTag) {
		this.awsAssociateTag = awsAssociateTag;
	}

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
			this.currencylayerApiKey = prop.getProperty("currencylayer.api-key");
			this.awsAccessKeyId = prop.getProperty("aws.access-key-id");
			this.awsSecretKey = prop.getProperty("aws.secret-key");
			this.awsAssociateTag = prop.getProperty("aws.associate-tag");
		} catch (IOException e) {
			System.exit(0);
		}
	}
}

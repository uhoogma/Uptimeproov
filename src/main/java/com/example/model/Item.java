package com.example.model;

public class Item {

	private String title;
	private String asin;
	private Integer price;
	private String currency;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "Item [title=" + title + ", asin=" + asin + ", price=" + price + ", currency=" + currency + "]";
	}

}

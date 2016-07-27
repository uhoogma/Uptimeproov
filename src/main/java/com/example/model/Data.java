package com.example.model;

import java.util.List;

public class Data {

	private int itemCount;
	private List<Item> itemList;
	
	public Data(int itemCount, List<Item> itemList) {
		this.itemCount = itemCount;
		this.itemList = itemList;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public List<Item> getItemList() {
		return itemList;
	}
	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}
}

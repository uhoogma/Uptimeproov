package com.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for data transfer
 */
public class Data {

	private int itemCount;
	private List<Item> itemList;

	public Data(int itemCount, List<Item> itemList) {
		this.itemCount = itemCount;
		this.itemList = new ArrayList<>(itemList);
	}

	public Data() {
		this.itemCount = 0;
		this.itemList = new ArrayList<>();
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

	@Override
	public String toString() {
		return "Data [itemCount=" + itemCount + ", itemList=" + itemList + "]";
	}

}

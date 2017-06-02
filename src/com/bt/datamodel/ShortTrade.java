package com.bt.datamodel;

public class ShortTrade extends Trade {

	public ShortTrade(float price, String ts) {
		this.tradeType = "SHORT";
		this.sellPrice = price;
		this.sellTs = ts;
	}

	@Override
	public void closeTrade(float price, String ts) {
		this.buyPrice = price;
		this.buyTs = ts;
	}

}

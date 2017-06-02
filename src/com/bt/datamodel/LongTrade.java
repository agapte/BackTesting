package com.bt.datamodel;

public class LongTrade extends Trade {

	public LongTrade(float price, String ts) {
		this.tradeType = "LONG";
		this.buyPrice = price;
		this.buyTs = ts;
	}

	@Override
	public void closeTrade(float price, String ts) {
		this.sellPrice = price;
		this.sellTs = ts;
	}

}

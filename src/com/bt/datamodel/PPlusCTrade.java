package com.bt.datamodel;

public class PPlusCTrade extends Trade {
	
	public PPlusCTrade(float putPrice, float callPrice, String ts) {
		this.putSellPrice = putPrice;
		this.callSellPrice = callPrice;
		this.tradeType = "SHORT";
		this.sellTs = ts;
	}

	@Override
	public void closeTrade(float putPrice, float callPrice , String ts) {
		this.callBuyPrice = callPrice;
		this.putBuyPrice = putPrice;
		this.buyTs = ts;
	}

	@Override
	public void closeTrade(float price, String ts) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public float getProfit() {
		return putSellPrice-putBuyPrice-callSellPrice-callBuyPrice - 0.2f;
	}

}

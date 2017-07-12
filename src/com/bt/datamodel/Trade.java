package com.bt.datamodel;

public abstract class  Trade {
	
	protected String tradeType = "LONG";
	float buyPrice = -1;
	float sellPrice = -1;
	float maxProfit = -1;
	String buyTs = "";
	String sellTs = "";
	public static int SLIPPAGE = 5;
	
	public Trade() {
	}
	
	public abstract void closeTrade(float price, String ts);

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public float getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(float buyPrice) {
		this.buyPrice = buyPrice;
	}

	public float getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(float sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getBuyTs() {
		return buyTs;
	}

	public void setBuyTs(String buyTs) {
		this.buyTs = buyTs;
	}

	public String getSellTs() {
		return sellTs;
	}

	public void setSellTs(String sellTs) {
		this.sellTs = sellTs;
	}
	
	public float getProfit()
	{
		if( sellPrice > -1 && buyPrice > -1)
		{
			return sellPrice-buyPrice-SLIPPAGE;
		}
		return 0;
	}
	
	public boolean isLongTrade()
	{
		if ( tradeType.equals("LONG"))
		{
			return true;
		}
		return false;
	}
	
	public boolean isShortTrade()
	{
		if ( tradeType.equals("SHORT"))
		{
			return true;
		}
		return false;
	}
	
	public boolean isTradeClosed()
	{
		if( sellPrice > -1 && buyPrice > -1)
		{
			return true;
		}
		
		return false;
	}
	
	public float getCurrentProfit(float close)
	{
		if ( tradeType.equals("LONG"))
		{
			return close-buyPrice;
		} 
		return sellPrice - close;
	}
	
}

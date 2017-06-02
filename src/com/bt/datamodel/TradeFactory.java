package com.bt.datamodel;

public class TradeFactory {
	
	public static Trade getLongTrade(float price, String ts)
	{
		return new LongTrade(price, ts);
	}
	
	public static Trade getShortTrade(float price, String ts)
	{
		return new ShortTrade(price, ts);
	}

}

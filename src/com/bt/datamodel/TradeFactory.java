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
	
	public static Trade getPPlusCTrade(float putPrice, float callPrice, String ts)
	{
		return new PPlusCTrade(putPrice, callPrice, ts);
	}

}

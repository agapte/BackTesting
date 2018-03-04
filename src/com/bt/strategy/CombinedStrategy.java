package com.bt.strategy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.bt.datamodel.StrategyOverview;
import com.bt.datamodel.Trade;
import com.bt.strategy.channelbreakout.ChannelBreakOut;
import com.bt.strategy.intraday.BNIntraDayStrategy;
import com.bt.strategy.intraday.IntraDayStrategy;

public class CombinedStrategy {
	
	public static void main(String[] args) throws ParseException {
		
		TreeMap<Date, Float> tradeProfitsMap = new TreeMap<>();
		
		ChannelBreakOut channelBreakOutStrtegy = new ChannelBreakOut();
		StrategyOverview result1 = channelBreakOutStrtegy.processData();
		
		IntraDayStrategy intraDayStrategy = new IntraDayStrategy();
		intraDayStrategy.setPeriodicity(1);
		intraDayStrategy.initData();
		StrategyOverview result2 = intraDayStrategy.processData();
		
		
		Trade.SLIPPAGE = 9;
		BNIntraDayStrategy bnIntraDayStrategy = new BNIntraDayStrategy();
		bnIntraDayStrategy.setPeriodicity(1);
		bnIntraDayStrategy.initData();
		StrategyOverview result3 = bnIntraDayStrategy.processData();
		
		DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
		for (Trade trade : result1.getTradeList()) {
			String tradeCloseTime = trade.isLongTrade() == true? trade.getSellTs():trade.getBuyTs();
			String tradeCloseDate = tradeCloseTime.substring(0, tradeCloseTime.indexOf("_"));
			
			float profit = trade.getProfit();
			if (tradeProfitsMap.get(f.parse(tradeCloseDate)) != null )
			{
				profit += tradeProfitsMap.get(f.parse(tradeCloseDate));
			}
			tradeProfitsMap.put(f.parse(tradeCloseDate), profit);
		}
		
		for (Trade trade : result2.getTradeList()) {
			String tradeCloseTime = trade.isLongTrade() == true? trade.getSellTs():trade.getBuyTs();
			String tradeCloseDate = tradeCloseTime.substring(0, tradeCloseTime.indexOf("_"));
			float profit = trade.getProfit()*2;
			if (tradeProfitsMap.get(f.parse(tradeCloseDate)) != null )
			{
				profit += tradeProfitsMap.get(f.parse(tradeCloseDate));
			}
			tradeProfitsMap.put(f.parse(tradeCloseDate), profit);
		}
		
		for (Trade trade : result3.getTradeList()) {
			String tradeCloseTime = trade.isLongTrade() == true? trade.getSellTs():trade.getBuyTs();
			String tradeCloseDate = tradeCloseTime.substring(0, tradeCloseTime.indexOf("_"));
			float profit = trade.getProfit()*2;
			if (tradeProfitsMap.get(f.parse(tradeCloseDate)) != null )
			{
				profit += tradeProfitsMap.get(f.parse(tradeCloseDate));
			}
			tradeProfitsMap.put(f.parse(tradeCloseDate), profit);
		}
		
		System.out.println(tradeProfitsMap);
	}

}

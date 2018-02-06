package com.bt.strategy.intraday;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.bt.datamodel.CandleStickData;
import com.bt.datamodel.StrategyOverview;
import com.bt.datamodel.Trade;
import com.bt.datamodel.TradeFactory;
import com.bt.strategy.BankNifty30;
import com.bt.util.LinearRegression;

public class BNIntraDayStrategy extends BankNifty30 {
	
	LinkedHashMap<String, List<CandleStickData>> intraDayMap = new LinkedHashMap<String, List<CandleStickData>>();
	
	@Override
	protected String[] getYears() {
		return new String[]{"2013","2014","2015","2016", "2017", "2018"};
//		return new String[]{"2017"};
	}
	
	public static void main(String[] args) {
		Trade.SLIPPAGE = 9;
		BNIntraDayStrategy strategy = new BNIntraDayStrategy();
		strategy.setPeriodicity(1);
		strategy.initData();
		strategy.processData();
	}

	private void processData() {
		createIntraDayMap();
		StrategyOverview overview = new StrategyOverview();
		Trade currentTrade = null;
		Set<Entry<String, List<CandleStickData>>> entrySet = intraDayMap.entrySet();
		List<CandleStickData> prevDayList = null;
		for (Entry<String, List<CandleStickData>> entry : entrySet) {
			
			String dateString = entry.getKey();
			dateString.trim();
			List<CandleStickData> candleStickDataList = entry.getValue();
			List<CandleStickData> intraDayList = new ArrayList<CandleStickData>(candleStickDataList);
			int index = 0;
			int tradeCount = 0;
			for (CandleStickData candleStickData : intraDayList) {
				if (prevDayList == null || prevDayList.size() < 24)
				{
					continue;
				}
				
				List<CandleStickData> subList = new ArrayList<CandleStickData>(prevDayList.subList(prevDayList.size()-24, prevDayList.size()));
				
				float channelMax = -1;
				float channelMin = 99999;
				for (CandleStickData csd : subList) {
//					float close = csd.getmClose();
					float high = csd.getmHigh();
					float low = csd.getmLow();
					if ( low < channelMin )
					{
						channelMin = low;
					}
					
					if ( high > channelMax )
					{
						channelMax = high;
					}
				}
				
				float close = candleStickData.getmClose();
				float high = candleStickData.getmHigh();
				float low = candleStickData.getmLow();
				float open = candleStickData.getmOpen();
				String ts = candleStickData.getTs();

				// Default combination, Low drawdown, Best consistency
				float stopLoss = Math.round(0.4f/100f*low);   // Try 0.3 also
                float positiveDelta = 70;
                float negativeDelta = 90;
                int timeCutOffInMinutesInMultipleOfTen = 11*10;

//                // Best returns with static values
//                stopLoss = Math.round(0.3f/100f*low); // Try 0.4 also
//                positiveDelta = 60;
//                negativeDelta = 90;
//                timeCutOffInMinutesInMultipleOfTen = 11*10;

//                // Best profit per trade, Least Drawdown, Least number of trades
//                stopLoss = Math.round(0.3f/100f*low);  // Try 0.4 also
//                positiveDelta = Math.round(0.6f/100f*low);
//                negativeDelta = Math.round(0.7f/100f*low);
//                timeCutOffInMinutesInMultipleOfTen = 6*10;

//                // Best Possible combination
//                stopLoss = Math.round(0.3f/100f*low); // Try 0.4 also
//                positiveDelta = Math.round((channelMax-channelMin)*0.3f);
//                negativeDelta = Math.round((channelMax-channelMin)*0.6f);
//                timeCutOffInMinutesInMultipleOfTen = 11*10;

				if( index ==0)
				{
					index++;
					continue;
				}
				if ( currentTrade == null && index < timeCutOffInMinutesInMultipleOfTen/10 && tradeCount < 1 )
				{
					if ( high > channelMax + positiveDelta)
					{
						float price = channelMax + positiveDelta;
						if( open > channelMax + positiveDelta)
						{
							price = open;
						}
						currentTrade = TradeFactory.getLongTrade(price, ts);
						tradeCount++;
						
					} 
					else if (low < channelMin-negativeDelta )
					{
						float price = channelMin - negativeDelta;
						if( open < channelMin - negativeDelta)
						{
							price = open;
						}
						currentTrade = TradeFactory.getShortTrade(price, ts);
						tradeCount++;
						
					} 
				} else if ( currentTrade != null )
				{
					if ( currentTrade.isLongTrade() && (low < channelMin ||currentTrade.getCurrentProfit(low) < -stopLoss) )
					{
						float price = channelMin;
						if( currentTrade.getCurrentProfit(low) < -stopLoss )
						{
							price = currentTrade.getBuyPrice()-stopLoss;
						}
						currentTrade.closeTrade(price, ts);
						overview.addTrade(currentTrade);
						currentTrade = null;
					}
					else if ( currentTrade.isShortTrade() && (high > channelMax ||currentTrade.getCurrentProfit(high) < -stopLoss))
					{
						float price = channelMax;
						if( currentTrade.getCurrentProfit(high) < -stopLoss )
						{
							price = currentTrade.getSellPrice()+stopLoss;
						}
						currentTrade.closeTrade(price, ts);
						overview.addTrade(currentTrade);
						currentTrade = null;
					}
					
				}
//				prevDayList.add(candleStickData);
				index++;
				if( index > 36 )
				{
					if( currentTrade != null)
					{
						
						currentTrade.closeTrade(close, ts);
						
						overview.addTrade(currentTrade);
						currentTrade = null;
					}
					break;
				}
			}
			
			prevDayList = candleStickDataList;
			
		}
		
		System.out.println(overview.toString());
//		System.out.println( jsonData);
		
	}

	private void createIntraDayMap() {
		List<CandleStickData> intraDayList  = null;
		for (CandleStickData ohlc : ohlc10MinDataList) 
		{
			if((ohlc.getHour() == 9 && ohlc.getMinute() == 19))
			{
				intraDayList = new ArrayList<CandleStickData>();
			}
			intraDayList.add(ohlc);
			
			if ( (ohlc.getHour() == 15 && ohlc.getMinute() == 29))
			{
				intraDayMap.put(ohlc.getTs(), intraDayList);
			}
			
		}
		
	}

	
	
	

}

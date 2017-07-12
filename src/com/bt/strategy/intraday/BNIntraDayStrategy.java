package com.bt.strategy.intraday;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.bt.datamodel.CandleStickData;
import com.bt.datamodel.StrategyOverview;
import com.bt.datamodel.Trade;
import com.bt.datamodel.TradeFactory;
import com.bt.strategy.BankNifty30;

public class BNIntraDayStrategy extends BankNifty30 {
	
	LinkedHashMap<String, List<CandleStickData>> intraDayMap = new LinkedHashMap<String, List<CandleStickData>>();
	
	@Override
	protected String[] getYears() {
		return new String[]{"2013","2014","2015","2016", "2017"};
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
			if( dateString.contains("01-03-2017"))
			{
				System.out.println("Break here");
			}
			dateString.trim();
			List<CandleStickData> candleStickDataList = entry.getValue();
			List<CandleStickData> intraDayList = new ArrayList<CandleStickData>(candleStickDataList);
			
			int index = 0;
			int tradeCount = 0;
			for (CandleStickData candleStickData : intraDayList) {
				if (prevDayList == null)
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
				float stopLoss = 0.35f/100f*low;
				stopLoss = Math.round(stopLoss);
				stopLoss = 100;
								
				if( index ==0)
				{
					index++;
					continue;
				}
				if ( currentTrade == null && index < 11 && tradeCount < 1 )
				{
					if ( high > channelMax + 130)
					{
						float price = channelMax + 130;
						if( open > channelMax + 130)
						{
							price = open;
						}
						currentTrade = TradeFactory.getLongTrade(price, ts);
						tradeCount++;
						
					} 
					else if (low < channelMin-120)
					{
						float price = channelMin - 120;
						if( open < channelMin - 120)
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

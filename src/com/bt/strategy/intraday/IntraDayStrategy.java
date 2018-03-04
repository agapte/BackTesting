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
import com.bt.strategy.Nifty30;
import com.bt.util.LinearRegression;
import com.bt.util.StatUtils;

public class IntraDayStrategy extends Nifty30 {
	
	LinkedHashMap<String, List<CandleStickData>> intraDayMap = new LinkedHashMap<String, List<CandleStickData>>();
	
	@Override
	protected String[] getYears() {
		return new String[]{"2013","2014","2015","2016", "2017", "2018"};
//		return new String[]{"2018"};
	}
	
	public static void main(String[] args) {
		IntraDayStrategy strategy = new IntraDayStrategy();
		strategy.setPeriodicity(1);
		strategy.initData();
		strategy.processData();
	}

	public StrategyOverview processData() {
		createIntraDayMap();
		StrategyOverview overview = new StrategyOverview();
		Trade currentTrade = null;
		LinearRegression lr = null;
		Set<Entry<String, List<CandleStickData>>> entrySet = intraDayMap.entrySet();
		List<CandleStickData> prevDayList = null;
		for (Entry<String, List<CandleStickData>> entry : entrySet) {
			
			String dateString = entry.getKey();

			dateString.trim();
			List<CandleStickData> candleStickDataList = entry.getValue();
			List<CandleStickData> intraDayList = new ArrayList<CandleStickData>(candleStickDataList);
			
			List<CandleStickData> subList = new ArrayList<>();
			List<CandleStickData> currentDayList = new ArrayList<>();
			if (prevDayList != null)
			{
				if (prevDayList.size() < 24 ){
//				System.out.println("Problem");
					subList = prevDayList;
				} else {
					subList = new ArrayList<CandleStickData>(prevDayList.subList(prevDayList.size()-24, prevDayList.size()));
				}
				lr = StatUtils.getLinearRegression(prevDayList);
			}
			
			float channelMax = -1;
			float channelMin = 99999;
			for (CandleStickData csd : subList) {
//				float close = csd.getmClose();
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
			
			
			int index = 0;
			int tradeCount = 0;
			float stopLoss = 0.4f/100f*channelMin;
			stopLoss = Math.round(stopLoss);
//			stopLoss = 30;
			float longDelta = 30;
			float shortDelta = 20;
//			longDelta = 0.25f/100f*channelMin;
			shortDelta = 0.25f/100f*channelMin;
			
			for (CandleStickData candleStickData : intraDayList) {
				currentDayList.add(candleStickData);
				if (prevDayList == null)
				{
					continue;
				}
				
				float close = candleStickData.getmClose();
				float high = candleStickData.getmHigh();
				float low = candleStickData.getmLow();
				float open = candleStickData.getmOpen();
				String ts = candleStickData.getTs();
				
				if( index ==0)
				{
					index++;
					continue;
				}
				if ( currentTrade == null && index < 6 && tradeCount < 1 )
				{
//					float tradePrice = canGoLong(prevDayList, currentDayList, channelMax, index);
//					if( tradePrice > 0)
//					{
//						if( open > tradePrice)
//						{
//							tradePrice = open;
//						}
//						currentTrade = TradeFactory.getLongTrade(tradePrice, ts);
//						tradeCount++;
//					}
					
					if ( lr.slope() > 0 && lr.R2() > 0.5 && high > channelMax + longDelta && index < 4)
					{
						float price = channelMax + longDelta;
						if( open > channelMax + longDelta)
						{
							price = open;
						}
						currentTrade = TradeFactory.getLongTrade(price, ts);
						tradeCount++;
						
					} 
					else if (low < channelMin-shortDelta && index < 2)
					{
						float price = channelMin - shortDelta;
						if( open < channelMin - shortDelta)
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
					candleStickData.getTs();
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
		return overview;
		
	}

	private float canGoLong(List<CandleStickData> currentDayList, List<CandleStickData> prevDayList, float channelMax, int index) {
		return 0;
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

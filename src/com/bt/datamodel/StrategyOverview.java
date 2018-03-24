package com.bt.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class StrategyOverview {
	
	List<Trade> tradeList = new ArrayList<>();
	
	ArrayList<String> drawDownList = new ArrayList<String>();
	
	public StrategyOverview() {
	}
	
	public void addTrade(Trade trade)
	{
		tradeList.add(trade);
	}
	
	public float getTotalProfit()
	{
		float returnValue = 0;
		for (Trade trade : tradeList) {
			returnValue += trade.getProfit();
		}
		
		return returnValue;
	}
	
	public int getTotalTrades()
	{
		return tradeList.size();
	}
	
	public float getLongProfit()
	{
		float returnValue = 0;
		for (Trade trade : tradeList) {
			if(trade.getTradeType().equals("LONG"))
			{
				returnValue += trade.getProfit();
			}
		}
		return returnValue;
	}
	
	public int getLongTrades()
	{
		int returnValue = 0;
		for (Trade trade : tradeList) {
			if(trade.getTradeType().equals("LONG"))
			{
				returnValue++;
			}
		}
		return returnValue;
	}
	
	public int getShortTrades()
	{
		int returnValue = 0;
		for (Trade trade : tradeList) {
			if(trade.getTradeType().equals("SHORT"))
			{
				returnValue++;
			}
		}
		return returnValue;
	}
	
	public float getShortProfit()
	{
		float returnValue = 0;
		for (Trade trade : tradeList) {
			if(trade.getTradeType().equals("SHORT"))
			{
				returnValue += trade.getProfit();
			}
		}
		return returnValue;
	}
	
	public float getAverageTrade()
	{
		return getTotalProfit()/getTotalTrades();
	}
	
	public float getMaxDrawdown()
	{
		StringBuilder profitData = new StringBuilder();
		
		float maxProfit = 0;
		float totalProfit = 0;
		float maxDrawdown = 0;
		for (Trade trade : tradeList) {
			totalProfit += trade.getProfit();
			String ts = trade.getSellTs();
			if(trade.getTradeType().equals("SHORT"))
			{
				ts = trade.getBuyTs();
			}
			profitData.append(ts + "," + totalProfit+"\n");
			if(totalProfit > maxProfit)
			{
				maxProfit = totalProfit;
			}
			drawDownList.add(ts+"#"+(maxProfit - totalProfit));
			if(maxDrawdown < (maxProfit - totalProfit))
			{
				maxDrawdown = maxProfit - totalProfit;
			}
		}
//		System.out.println(profitData);
		return maxDrawdown;
	}
	
	public float getCurrentDrawdown()
	{
		StringBuilder profitData = new StringBuilder();
		ArrayList<Float> drawDownList = new ArrayList<Float>();
		float maxProfit = 0;
		float totalProfit = 0;
		float maxDrawdown = 0;
		for (Trade trade : tradeList) {
			totalProfit += trade.getProfit();
			String ts = trade.getSellTs();
			if(trade.getTradeType().equals("SHORT"))
			{
				ts = trade.getBuyTs();
			}
			profitData.append(ts + "," + totalProfit+"\n");
			if(totalProfit > maxProfit)
			{
				maxProfit = totalProfit;
			}
			drawDownList.add(maxProfit - totalProfit);
			if(maxDrawdown < (maxProfit - totalProfit))
			{
				maxDrawdown = maxProfit - totalProfit;
			}
		}
//		System.out.println(profitData);
		return maxProfit - totalProfit;
	}
	
	public float getPercentProfitable()
	{
		float profitTrades = 0;
		for (Trade trade : tradeList) {
			float profit = trade.getProfit();
			if(profit > 0)
			{
				profitTrades++;
			} 
		}
		return (profitTrades/tradeList.size());
	}
	
	public float getPercentLongProfitable()
	{
		float profitTrades = 0;
		int longTrades = 0;
		for (Trade trade : tradeList) {
			if(trade.getTradeType().equals("LONG"))
			{
				longTrades++;
				float profit = trade.getProfit();
				if(profit > 0)
				{
					profitTrades++;
				} 
			}
		}
		return (profitTrades/longTrades);
	}
	
	public float getPercentShortProfitable()
	{
		float profitTrades = 0;
		int longTrades = 0;
		for (Trade trade : tradeList) {
			if(trade.getTradeType().equals("SHORT"))
			{
				longTrades++;
				float profit = trade.getProfit();
				if(profit > 0)
				{
					profitTrades++;
				} 
			}
		}
		return (profitTrades/longTrades);
	}
	
	public float getAverageWinningTrade()
	{
		float profit = 0;
		int index = 0;
		for (Trade trade : tradeList) {
			if ( trade.getProfit() >= 0)
			{
				profit += trade.getProfit();
				index++;
			}
		}
		return profit/index;
//		System.out.println("Average winning trade" + " " + (profit/index));
	}
	
	public float getAverageLosingTrade()
	{
		float profit = 0;
		int index = 0;
		for (Trade trade : tradeList) {
			if ( trade.getProfit() < 0)
			{
				profit += trade.getProfit();
				index++;
			}
		}
		return profit/index;
//		System.out.println("Average losing trade" + " " + (profit/index));
	}
	
	public void getMonthlyProfits()
	{
		
		Map<String, Float> profitMap = new TreeMap<String, Float>();
		for (Trade trade : tradeList) {
			String ts = trade.getSellTs();
			if(trade.getTradeType().equals("SHORT"))
			{
				ts = trade.getBuyTs();
			}
			String substring = ts.substring(6, 10) + "_"+ ts.substring(3, 5);
//			if(substring.equals("13_1"))
//			{
//				System.out.println(ts);
//			}
			Float float1 = profitMap.get(substring);
			if( float1 == null)
			{
				float1 = (float) 0;
			}
			float1 += trade.getProfit();
			profitMap.put(substring, float1);
		}
		float cumProfit = 0;
		for (Entry<String, Float> entry : profitMap.entrySet()) {
			cumProfit += entry.getValue();
			System.out.println(entry.getKey() + " " + entry.getValue() + " " + cumProfit);
			if (entry.getKey().endsWith("12") )
			{
				cumProfit = 0;
			}
		}
	}
	
	public void getFinYearWiseProfit()
	{
		Map<String, Float> profitMap = new LinkedHashMap<String, Float>();
		profitMap.put("2012_2013", 0f);
		profitMap.put("2013_2014", 0f);
		profitMap.put("2014_2015", 0f);
		profitMap.put("2015_2016", 0f);
		profitMap.put("2016_2017", 0f);
		profitMap.put("2017_2018", 0f);
		for (Trade trade : tradeList) {
			String ts = trade.getSellTs();
			if(trade.getTradeType().equals("SHORT"))
			{
				ts = trade.getBuyTs();
			}
			int year = Integer.parseInt( ts.substring(6, 10));
			int month = Integer.parseInt( ts.substring(3, 5));
//			if(yearString.equals("13_1"))
//			{
//				System.out.println(ts);
//			}
			
			String yearString =  Integer.toString(year)+ "_"+ Integer.toString(year+1);
			if ( month < 4)
			{
				yearString = Integer.toString(year-1)+"_"+Integer.toString(year);
			}
			
			Float float1 = profitMap.get(yearString);
			if( float1 == null)
			{
				float1 = (float) 0;
			}
			float1 += trade.getProfit();
			profitMap.put(yearString, float1);
		}
		System.out.println(profitMap);
	}
	
	public void getYearWiseProfit()
	{
		Map<String, Float> profitMap = new LinkedHashMap<String, Float>();
//		profitMap.put("2012_2013", 0f);
//		profitMap.put("2013_2014", 0f);
//		profitMap.put("2014_2015", 0f);
//		profitMap.put("2015_2016", 0f);
//		profitMap.put("2016_2017", 0f);
//		profitMap.put("2017_2018", 0f);
		for (Trade trade : tradeList) {
			String ts = trade.getSellTs();
			if(trade.getTradeType().equals("SHORT"))
			{
				ts = trade.getBuyTs();
			}
			int year = Integer.parseInt( ts.substring(6, 10));
			int month = Integer.parseInt( ts.substring(3, 5));
//			if(yearString.equals("13_1"))
//			{
//				System.out.println(ts);
//			}
			
			String yearString =  Integer.toString(year);
			
			Float float1 = profitMap.get(yearString);
			if( float1 == null)
			{
				float1 = (float) 0;
			}
			float1 += trade.getProfit();
			profitMap.put(yearString, float1);
		}
		System.out.println(profitMap);
	}
	
	public void getQuarterlyProfit()
	{
		Map<String, Float> profitMap = new LinkedHashMap<String, Float>();
		for (Trade trade : tradeList) {
			String ts = trade.getSellTs();
			if(trade.getTradeType().equals("SHORT"))
			{
				ts = trade.getBuyTs();
			}
			int year = Integer.parseInt( ts.substring(6, 10));
			int month = Integer.parseInt( ts.substring(3, 5));
//			if(yearString.equals("13_1"))
//			{
//				System.out.println(ts);
//			}
			
			int quarter = 1;
			if ( month < 4)
			{
				quarter = 1;
			} else if (month < 7) {
				quarter = 2;
			}else if (month < 10) {
				quarter = 3;
				
			}else if (month < 13) {
				quarter = 4;
			}
			
			String yearString =  Integer.toString(quarter) + "_"+ Integer.toString(year);
			
			Float float1 = profitMap.get(yearString);
			if( float1 == null)
			{
				float1 = (float) 0;
			}
			float1 += trade.getProfit();
			profitMap.put(yearString, float1);
		}
		System.out.println(profitMap);
	}
	
	public float getMaxProfit()
	{
		float maxProfit = 0;
		for (Trade trade : tradeList) {
			if( trade.getProfit() > maxProfit)
			{
				maxProfit = trade.getProfit();
			}
		}
		return maxProfit;
	}
	
	public float getMaxLoss()
	{
		float maxLoss = 0;
		for (Trade trade : tradeList) {
			if( trade.getProfit() < maxLoss)
			{
				maxLoss = trade.getProfit();
			}
		}
		return maxLoss;
	}
	
	@Override
	public String toString() {
		
		int consLossCount = 0;
		int maxConsLossCount = 0;
		int consProfCount = 0;
		int maxConsProfCount = 0;
		for (Trade trade : tradeList) {
			System.out.print(trade.getTradeType());
			System.out.print("," +trade.getBuyTs());
			System.out.print("," +trade.getSellTs());
			System.out.print("," +trade.getBuyPrice());
			System.out.print("," + trade.getSellPrice());
			System.out.print("," + trade.getProfit());
			System.out.println();
			if( trade.getProfit() > 0)
			{
				consProfCount++;
				if ( consProfCount > maxConsProfCount)
				{
					maxConsProfCount = consProfCount;
				}
				consLossCount = 0;
			} else
			{
				consLossCount++;
				if ( consLossCount > maxConsLossCount)
				{
					maxConsLossCount = consLossCount;
				}
				consProfCount = 0;
			}
		}
		this.getMonthlyProfits();
		this.getYearWiseProfit();
		this.getFinYearWiseProfit();
		this.getQuarterlyProfit();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\nTotal profit         " + getTotalProfit());
		stringBuffer.append("\nTotal trades         " + getTotalTrades());
		stringBuffer.append("\nAvg   profit         " + getAverageTrade());
		stringBuffer.append("\nPercent profit       " + getPercentProfitable());
		stringBuffer.append("\tPercent Long Profit  " + getPercentLongProfitable());
		stringBuffer.append("\tPercent Short Profit " + getPercentShortProfitable());
		stringBuffer.append("\nLong  profit         " + getLongProfit());
		stringBuffer.append("\tLong  Trades         " + getLongTrades());
		stringBuffer.append("\tAvg   Long           " + getLongProfit()/getLongTrades());
		stringBuffer.append("\tMax   Profit         " + getMaxProfit());
		stringBuffer.append("\nShort profit         " + getShortProfit());
		stringBuffer.append("\tShort Trades         " + getShortTrades());
		stringBuffer.append("\tAvg   Short          " + getShortProfit()/getShortTrades());
		stringBuffer.append("\tMax   Loss           " + getMaxLoss());
		stringBuffer.append("\nmax drawdown         " + getMaxDrawdown());
		stringBuffer.append("\nCurrent drawdown     " + getCurrentDrawdown());
		stringBuffer.append("\nmax cons loss        " + maxConsLossCount);
		stringBuffer.append("\nmax cons Profit      " + maxConsProfCount);
		stringBuffer.append("\nAvg Win Trade        " + getAverageWinningTrade());
		stringBuffer.append("\tAvg Loss Trade       " + getAverageLosingTrade());
		stringBuffer.append("\n");
		stringBuffer.append(drawDownList);
		return stringBuffer.toString();
	}
	
	public List<Trade> getTradeList() {
		return tradeList;
	}

}

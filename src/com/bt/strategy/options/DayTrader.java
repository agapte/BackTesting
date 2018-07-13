package com.bt.strategy.options;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.bt.datamodel.CandleStickData;
import com.bt.datamodel.PPlusCTrade;
import com.bt.datamodel.StrategyOverview;
import com.bt.datamodel.Trade;
import com.bt.datamodel.TradeFactory;

public class DayTrader {
	String date;
	TreeMap<String, TreeMap<Integer, Map<String, CandleStickData>>> dayData;
	Trade trade = null;
	boolean activeTrade = false;
	float sellCallPrice = 0;
	float sellPutPrice = 0;
	float buyCallPrice = 0;
	float buyPutPrice = 0;
	float callProfit = 999999;
	float putProfit = 999999;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
	public DayTrader(String date, TreeMap<String, TreeMap<Integer, Map<String, CandleStickData>>> dayData) {
		this.date = date;
		this.dayData = dayData;
	}
	
	public float trade(StrategyOverview strategyOverview, Trade putTrade, Trade callTrade) throws ParseException {
		Set<String> keySet = dayData.keySet();
		int closestStrikePrice = 0;
		int index = 0;
		
		for (String time : keySet) {
			TreeMap<Integer, Map<String, CandleStickData>> treeMap = dayData.get(time);
			if (time.contains("9:59:59")) {
				CandleStickData candleStickData = treeMap.firstEntry().getValue().get("NIFTY-I");
				float close = candleStickData.getmClose();
				closestStrikePrice = Math.round(close / 100) * 100;
				index = 0;
				while ( index < 10)
				{
					sellCallPrice = treeMap.ceilingEntry(100).getValue().get(closestStrikePrice+100*index + "CE").getmClose();
					sellPutPrice = treeMap.ceilingEntry(100).getValue().get(closestStrikePrice -100*index+ "PE").getmClose();
					if(sellCallPrice + sellPutPrice < 100)
					{
						System.out.println(time + " " +(closestStrikePrice -100*index)+ "PE" + " Put Sold " +sellPutPrice );
						System.out.println(time + " " +(closestStrikePrice +100*index)+ "CE" + " Call Sold " +sellCallPrice );
						activeTrade = true;
						break;
					}
					index++;
				}
			}
			
			if ( activeTrade) {

				if(treeMap.ceilingEntry(100).getValue().containsKey(closestStrikePrice+100*index + "CE") && treeMap.ceilingEntry(100).getValue().containsKey((closestStrikePrice-100*index + "PE")))
				{
					float callPrice = treeMap.ceilingEntry(100).getValue().get(closestStrikePrice+100*index + "CE").getmClose();
					float putPrice = treeMap.ceilingEntry(100).getValue().get(closestStrikePrice-100*index + "PE").getmClose();

					if ( callProfit == 999999 && callPrice > sellCallPrice*1.5)
					{
						System.out.println(time + " " +(closestStrikePrice +100*index)+ "CE" + " Call Squared off " +callPrice );
						callProfit = sellCallPrice - callPrice;
					}

					if ( putProfit == 999999 && putPrice > sellPutPrice*1.5)
					{
						System.out.println(time + " " +(closestStrikePrice -100*index)+ "PE" + " Put Squared off " +putPrice );
						putProfit = sellPutPrice - putPrice;
					}
				}


			}
			
			if ( activeTrade  && time.contains("15:15")) {
				
				if (putProfit == 999999 && treeMap.ceilingEntry(100).getValue().containsKey(closestStrikePrice-100*index + "PE")) {
					float putPrice = treeMap.ceilingEntry(100).getValue().get(closestStrikePrice-100*index + "PE").getmClose();
					System.out.println(time + " " +(closestStrikePrice -100*index)+ "PE" + " Put Squared off " +putPrice );
					putProfit = sellPutPrice - putPrice;
				}
				if ( callProfit == 999999 && treeMap.ceilingEntry(100).getValue().containsKey(closestStrikePrice+100*index + "CE")) {
					
					float callPrice = treeMap.ceilingEntry(100).getValue().get(closestStrikePrice+100*index + "CE").getmClose();
					System.out.println(time + " " +(closestStrikePrice +100*index)+ "CE" + " Call Squared off " +callPrice );
					callProfit = sellCallPrice - callPrice;
				}
				activeTrade = false;
				break;
			}
		}
		
		System.out.println("date " + date + " " + dateFormat.parse(date) + "  Profit " + (callProfit + putProfit));
		if ( (callProfit + putProfit) > 1000)
		{
			return 0;
		}
		return (callProfit + putProfit);
	}

}

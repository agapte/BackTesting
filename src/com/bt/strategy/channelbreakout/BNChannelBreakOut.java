package com.bt.strategy.channelbreakout;

import java.util.LinkedList;
import java.util.List;

import com.bt.datamodel.CandleStickData;
import com.bt.datamodel.StrategyOverview;
import com.bt.datamodel.Trade;
import com.bt.datamodel.TradeFactory;
import com.bt.strategy.BankNifty30;

public class BNChannelBreakOut extends BankNifty30 {
	
	int direction = 0;
	

	public void processData()
	{
		int PERIOD = 24;
		int STOPLOSS = 180;
		LinkedList<CandleStickData> fast = new LinkedList<>();
		LinkedList<Float> dayClose = new LinkedList<>();
		StrategyOverview overview = new StrategyOverview();
		Trade currentTrade = null;
		for (CandleStickData ohlc : ohlc30MinDataList) 
		{
			
			if( ohlc.getHour() == 15 && ohlc.getMinute() == 29)
			{
				dayClose.add(ohlc.getmClose());
			}

			if (fast.size() < PERIOD) {
				fast.addLast(ohlc);
				continue;
			}
			
			float channelHigh = -1;
			float channelLow = 99999;

			int index  = 0;
			List<CandleStickData> subFast = fast;
			for (CandleStickData candleStickData : subFast) {
				if( currentTrade != null && index < 0 && currentTrade.getTradeType().equals("SHORT"))
				{
					index++;
					continue;
				}
				float high = candleStickData.getmHigh();
				float low = candleStickData.getmLow();
				if (high > channelHigh) {
					channelHigh = high;
				}
				if (low < channelLow) {
					channelLow = low;
				}
				index++;
			}

			if (currentTrade == null) {
				if (ohlc.getmHigh() > channelHigh  ) {
					float tradeValue = channelHigh;
					if(ohlc.getmOpen() > channelHigh  )
					{
						tradeValue = ohlc.getmOpen();
					}
					currentTrade = TradeFactory.getLongTrade(tradeValue, ohlc.getTs());
					System.out.println("LONG,"+ tradeValue+","+ohlc.getTs() );
//					STOPLOSS = (int) Math.round(tradeValue*1/100);
				} else if (ohlc.getmLow() < channelLow ) {
					float tradeValue = channelLow;
					if(ohlc.getmOpen() < channelLow )
					{
						tradeValue = ohlc.getmOpen();
					}
					currentTrade =  TradeFactory.getShortTrade(tradeValue, ohlc.getTs());
					System.out.println("SHORT,"+ tradeValue+","+ohlc.getTs() );
//					STOPLOSS = (int) Math.round(tradeValue*0.85/100);
				}
				
			} else if (currentTrade.getTradeType().equals("SHORT")) 
			{
				
				float sl = currentTrade.getSellPrice() +STOPLOSS;
				boolean isSL = false;
				if( sl < channelHigh )
				{
					channelHigh = sl;
					isSL = true;
				}
				if (ohlc.getmHigh() > channelHigh ) 
				{
					float tradeClose = channelHigh;
					if(ohlc.getmOpen() > channelHigh )
					{
						tradeClose = ohlc.getmOpen();
					}
					currentTrade.closeTrade(tradeClose, ohlc.getTs());
					overview.addTrade(currentTrade);
					if(isSL)
					{
						System.out.println("NONE,"+ tradeClose+","+ohlc.getTs() );
						currentTrade = null;
					} else
					{
						currentTrade = TradeFactory.getLongTrade( tradeClose, ohlc.getTs());
						System.out.println("LONG,"+ tradeClose+","+ohlc.getTs() );
//												currentTrade = null;
					}
				} 

			} else if (currentTrade.getTradeType().equals("LONG")) {
				float sl = currentTrade.getBuyPrice() -STOPLOSS;
				boolean isSL = false;
				if( sl > channelLow )
				{
					channelLow = sl;
					isSL = true;
				}
				if (ohlc.getmLow() < channelLow	) 
				{
					boolean bigGapDown = false;
					float tradeClose = channelLow;
					if(ohlc.getmOpen() < channelLow)
					{
						tradeClose = ohlc.getmOpen();
						if(currentTrade.getBuyPrice() - tradeClose > 100    )
						{
							bigGapDown = true;
						}
					}
					
					currentTrade.closeTrade(tradeClose, ohlc.getTs());
					overview.addTrade(currentTrade);
					if(isSL)
					{
						System.out.println("NONE,"+ tradeClose+","+ohlc.getTs() );
						if(bigGapDown)
						{
							currentTrade = TradeFactory.getLongTrade( tradeClose, ohlc.getTs());
						} else
						{
							currentTrade = null;
						}
					} else
					{
						if(bigGapDown)
						{
							currentTrade = TradeFactory.getLongTrade( tradeClose, ohlc.getTs());
						} else
						{
//						currentTrade = TradeFactory.getShortTrade( tradeClose, ohlc.getTs());
						currentTrade = null;
						}
						System.out.println("NONE,"+ tradeClose+","+ohlc.getTs() );
					}
				}
			}

			fast.addLast(ohlc);
			if (fast.size() > PERIOD) {
				fast.removeFirst();
			}
			
		}
		//		overview.printTrades();
		System.out.println(overview.toString());
	}
	
	public static void main(String[] args) {
		BNChannelBreakOut strategy = new BNChannelBreakOut();
		strategy.processData();
	}

}

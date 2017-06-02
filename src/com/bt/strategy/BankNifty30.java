package com.bt.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bt.datamodel.CandleStickData;
import com.bt.strategy.BNBaseStrategy;

public class BankNifty30 extends BNBaseStrategy{
	protected List<CandleStickData> ohlc30MinDataList = new ArrayList<>();
	protected List<CandleStickData> ohlc1DayDataList = new ArrayList<>();
	protected List<CandleStickData> ohlc10MinDataList = new ArrayList<>();
	protected HashMap<CandleStickData, List<CandleStickData>> ohlcIntradayMap = new HashMap<CandleStickData, List<CandleStickData>>();
	
	public BankNifty30() {
		setPeriodicity(1);
		initData();
//		initBankNiftyData();
		init30MinBars();
		init10MinBars();
//		init1DayBars();
	}
	
	private void init30MinBars() {
		float min = 99999;
		float max = -1;
		float open30 = 0;
		float close30;
		String openTs = "";
		for (CandleStickData ohlcData : ohlcDataList) {
			if( min > ohlcData.getmLow())
			{
				min = ohlcData.getmLow();
			}
			if( max < ohlcData.getmHigh())
			{
				max = ohlcData.getmHigh();
			}
			if(ohlcData.getMinute() == 30 || ohlcData.getMinute() == 0 || (ohlcData.getHour() == 9 && ohlcData.getMinute() == 15))
			{
				openTs = ohlcData.getTs();
				open30 = ohlcData.getmOpen();
			}else if ( ohlcData.getMinute() == 29 || ohlcData.getMinute() == 59)
			{
				close30 = ohlcData.getmClose();
				CandleStickData candleStickData = new CandleStickData(open30, max, min, close30, ohlcData.getTs(), openTs);
				ohlc30MinDataList.add(candleStickData);
				min = 99999;
				max = -1;
			}
			
		}
		
	}
	
	private void init10MinBars() {
		float min = 99999;
		float max = -1;
		float open30 = 0;
		float close30;
		String openTs = "";
		for (CandleStickData ohlcData : ohlcDataList) {
			if( min > ohlcData.getmLow())
			{
				min = ohlcData.getmLow();
			}
			if( max < ohlcData.getmHigh())
			{
				max = ohlcData.getmHigh();
			}
			if(ohlcData.getMinute() % 10 == 0 || (ohlcData.getHour() == 9 && ohlcData.getMinute() == 15))
			{
				openTs = ohlcData.getTs();
				open30 = ohlcData.getmOpen();
			}else if ( ohlcData.getMinute()%10==9)
			{
				close30 = ohlcData.getmClose();
				CandleStickData candleStickData = new CandleStickData(open30, max, min, close30, ohlcData.getTs(), openTs);
				ohlc10MinDataList.add(candleStickData);
				min = 99999;
				max = -1;
			}
			
		}
		
	}
	

}

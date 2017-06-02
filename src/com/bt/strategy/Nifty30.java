package com.bt.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bt.datamodel.CandleStickData;
import com.bt.strategy.BaseStrategy;

public class Nifty30 extends BaseStrategy{
	protected List<CandleStickData> ohlc30MinDataList = new ArrayList<>();
	protected List<CandleStickData> ohlc1DayDataList = new ArrayList<>();
	protected List<CandleStickData> ohlc10MinDataList = new ArrayList<>();
	protected HashMap<CandleStickData, List<CandleStickData>> ohlcIntradayMap = new HashMap<CandleStickData, List<CandleStickData>>();
	
	public Nifty30() {
		setPeriodicity(1);
		initData();
//		initBankNiftyData();
		init30MinBars();
		init10MinBars();
//		init1DayBars();
	}
	
//	private void init1DayBars() {
//		float min = 99999;
//		float max = -1;
//		float open = 0;
//		float close;
//		List<CandleStickData> intradayList = null;
//		for (CandleStickData ohlcData : ohlcDataList) {
//			if( min > ohlcData.getmLow())
//			{
//				min = ohlcData.getmLow();
//			}
//			if( max < ohlcData.getmHigh())
//			{
//				max = ohlcData.getmHigh();
//			}
//			if((ohlcData.getHour() == 9 && ohlcData.getMinute() == 15))
//			{
//				intradayList = new ArrayList<CandleStickData>();
//				open = ohlcData.getmOpen();
//			}
//			
//			intradayList.add(ohlcData);
//			if ( (ohlcData.getHour() == 15 && ohlcData.getMinute() == 29))
//			{
//				close = ohlcData.getmClose();
//				CandleStickData candleStickData = new CandleStickData(open, max, min, close, ohlcData.getTs());
//				ohlc1DayDataList.add(candleStickData);
//				List<CandleStickData> get30MinOhlc = get30MinOhlc(intradayList);
//				ohlcIntradayMap.put(candleStickData, get30MinOhlc);
//				min = 99999;
//				max = -1;
//			}
//			
//		}
//		
//	}
	
//	private List<CandleStickData> get30MinOhlc(List<CandleStickData> intradayList)
//	{
//		float min = 99999;
//		float max = -1;
//		float open30 = 0;
//		float close30;
//		String openTs = "";
//		List<CandleStickData> ohlc30MinList = new ArrayList<CandleStickData>();
//		for (CandleStickData ohlcData : intradayList) {
//			if( min > ohlcData.getmLow())
//			{
//				min = ohlcData.getmLow();
//			}
//			if( max < ohlcData.getmHigh())
//			{
//				max = ohlcData.getmHigh();
//			}
//			if(ohlcData.getMinute() == 30 || ohlcData.getMinute() == 0 || (ohlcData.getHour() == 9 && ohlcData.getMinute() == 15))
//			{
//				openTs = ohlcData.getTs();
//				open30 = ohlcData.getmOpen();
//			}else if ( ohlcData.getMinute() == 29 || ohlcData.getMinute() == 59)
//			{
//				close30 = ohlcData.getmClose();
//				CandleStickData candleStickData = new CandleStickData(open30, max, min, close30, ohlcData.getTs(), openTs);
//				ohlc30MinList.add(candleStickData);
//				min = 99999;
//				max = -1;
//			}
//		}
//		return ohlc30MinList;
//	}

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

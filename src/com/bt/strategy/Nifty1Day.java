package com.bt.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bt.datamodel.CandleStickData;
import com.bt.strategy.BaseStrategy;

public class Nifty1Day extends BaseStrategy{
	protected List<CandleStickData> ohlc1DayDataList = new ArrayList<>();
	protected HashMap<CandleStickData, List<CandleStickData>> ohlcIntradayMap = new HashMap<CandleStickData, List<CandleStickData>>();
	
	public Nifty1Day() {
		setPeriodicity(1);
		initData();
		init1DayBars();
	}

	private void init1DayBars() {
		float min = 99999;
		float max = -1;
		float open = 0;
		float close;
		List<CandleStickData> intradayList = null;
		for (CandleStickData ohlcData : ohlcDataList) {
			if( min > ohlcData.getmLow())
			{
				min = ohlcData.getmLow();
			}
			if( max < ohlcData.getmHigh())
			{
				max = ohlcData.getmHigh();
			}
			if((ohlcData.getHour() == 9 && ohlcData.getMinute() == 15))
			{
				intradayList = new ArrayList<CandleStickData>();
				open = ohlcData.getmOpen();
			}
			
			intradayList.add(ohlcData);
			if ( (ohlcData.getHour() == 15 && ohlcData.getMinute() == 29))
			{
				close = ohlcData.getmClose();
				CandleStickData candleStickData = new CandleStickData(open, max, min, close, ohlcData.getTs());
				ohlc1DayDataList.add(candleStickData);
				ohlcIntradayMap.put(candleStickData, intradayList);
				min = 99999;
				max = -1;
			}
			
		}
		
	}

}

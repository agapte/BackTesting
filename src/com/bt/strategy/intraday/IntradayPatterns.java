package com.bt.strategy.intraday;

import java.util.List;
import java.util.Map.Entry;

import com.bt.datamodel.CandleStickData;
import com.bt.strategy.Nifty1Day;
import com.bt.strategy.Nifty30;

public class IntradayPatterns extends Nifty1Day{
	private int trendingBullish = 0;
	private int trendingBearish = 0;
	private int rangeBound = 0;
	private int failedGapUp = 0;
	private int failedGapDown = 0;
	private int upSpike = 0;
	private int downSpike = 0;
	private int upStep = 0;
	private int downStep = 0;
	
	public static void main(String[] args) {
		IntradayPatterns strategy = new IntradayPatterns();
		strategy.setPeriodicity(1);
		strategy.initData();
		strategy.processData();
	}

	private void processData() {
		
		for (Entry<CandleStickData, List<CandleStickData>> entry : ohlcIntradayMap.entrySet()) {
			System.out.println(entry.getKey().getTs() + ":" + entry.getValue().size());
			
		}
		
	}

}

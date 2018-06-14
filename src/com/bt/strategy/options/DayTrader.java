package com.bt.strategy.options;

import java.util.Map;
import java.util.TreeMap;

import com.bt.datamodel.CandleStickData;

public class DayTrader {
	String date;
	TreeMap<String, TreeMap<Integer, Map<String, CandleStickData>>> dayData;

	public DayTrader(String date, TreeMap<String, TreeMap<Integer, Map<String, CandleStickData>>> dayData) {
		this.date = date;
		this.dayData = dayData;
	}
	
	public void startTrading() {
		
	}

}

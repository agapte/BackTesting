package com.bt.util;

import java.util.LinkedList;

import com.bt.datamodel.CandleStickData;

public class StatUtils {

	public static float getAverage(LinkedList<Float> movingList) {
		float sum = 0;
		for (Float close : movingList) {
			sum = sum + close;
		}
		float average = sum / movingList.size();
		return average;
	}

	public static float getATR(LinkedList<CandleStickData> dataList) {
		float currentHigh = 0;
		float currentLow = 0;
		float prevClose = -1;
		float trSum = 0;
		int count =0;
		for (CandleStickData candleStickData : dataList) {
			currentHigh = candleStickData.getmHigh();
			currentLow = candleStickData.getmClose();
			if (prevClose != -1) {
				float m1 = currentHigh - currentLow;
				float m2 = Math.abs(currentHigh - prevClose);
				float m3 = Math.abs(currentLow - prevClose);
				float tr = Math.max(Math.max(m1, m2), m3);
				trSum += tr;
				count++;
			}
			prevClose = candleStickData.getmClose();

		}
		return trSum/count;
	}
	
	public static float getLongExit(LinkedList<CandleStickData> dataList) {
		float atr = getATR(dataList);
		float min = 99999;
		for (CandleStickData candleStickData : dataList) {
			if(candleStickData.getmLow() < min)
			{
				min = candleStickData.getmLow();
			}
		}
		float exit = Math.round(min + 3*atr);
		return exit;
	}
	
	public static float getShortExit(LinkedList<CandleStickData> dataList) {
		float atr = getATR(dataList);
		float max = 0;
		for (CandleStickData candleStickData : dataList) {
			if(candleStickData.getmHigh() > max)
			{
				max = candleStickData.getmHigh();
			}
		}
		float exit = Math.round(max - 3*atr);
		return exit;
	}

}

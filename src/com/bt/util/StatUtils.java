package com.bt.util;

import java.util.LinkedList;

public class StatUtils {
	
	public static float getAverage(LinkedList<Float> movingList) {
		float sum = 0;
		for (Float close : movingList) {
			sum = sum+close;
		}
		float average = sum/movingList.size();
		return average;
	}

}

package com.bt.datamodel;

public class CandleStickData {
	float mOpen;
	float mHigh;
	float mLow;
	float mClose;
	String mTs;
	String mopenTs;
	/**
	 * Constructor
	 * @param open
	 * @param high
	 * @param low
	 * @param close
	 */
	public CandleStickData(float open, float high, float low, float close, String ts) {
		mOpen = open;
		mHigh = high;
		mLow = low;
		mClose = close;
		mTs = ts;
	}
	
	/**
	 * Constructor
	 * @param open
	 * @param high
	 * @param low
	 * @param close
	 */
	public CandleStickData(float open, float high, float low, float close, String ts, String openTs) {
		mOpen = open;
		mHigh = high;
		mLow = low;
		mClose = close;
		mTs = ts;
		mopenTs = openTs;
	}
	public float getmOpen() {
		return mOpen;
	}
	public float getmHigh() {
		return mHigh;
	}
	public float getmLow() {
		return mLow;
	}
	public float getmClose() {
		return mClose;
	}
	
	public int getMinute(){
		String[] split = mTs.split("_");
		String timeString = split[1];
		String[] split2 = timeString.split(":");
		return Integer.parseInt(split2[1]);
	}

	public int getHour(){
		String[] split = mTs.split("_");
		String timeString = split[1];
		String[] split2 = timeString.split(":");
		return Integer.parseInt(split2[0]);
		
	}
	
	public String getTs() {
		return mTs;
	}
	
	@Override
	public String toString() {
		return "Open " + mOpen + " High " + mHigh + " Low " + mLow+ " Close " + mClose + " " + mopenTs + " " + mTs;
	}

}

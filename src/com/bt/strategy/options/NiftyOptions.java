package com.bt.strategy.options;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.bt.datamodel.CandleStickData;

public class NiftyOptions {
	
//	private static String[] years = new String[] {"14", "15","16", "17"};
	private static List<String> monthsValues = Arrays.asList(new String[] {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"});
	private static String[] monthsIndex = new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
	private static String[] years = new String[] {"15"};
	private static String[] months = new String[] {"Jan"};
	TreeMap<String, TreeMap<String, TreeMap<Integer, Map<String, CandleStickData>>>> cachedData = new TreeMap<>();
	
	public NiftyOptions() throws IOException {
		readData();
	}
	
	public static void main(String[] args) throws Exception {
		NiftyOptions niftyOptions = new NiftyOptions();
	}

	private void readData() throws IOException  {
		for (String year : years) {
			String path = "/Users/amit.apte/personal/BackTesting/NIFTY_OPTIONS/20"+year;// use your path
			for (String month : months) {
//				time , strikePrice, expiry, type
				
				String filepath = path + "/"+month+"_20"+year+".csv";
				BufferedReader bufferedReader;
				try {
					bufferedReader = new BufferedReader(new FileReader(filepath));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				String line = "";
				boolean firstLineSkipped = false;
				while ( line != null)
				{
				 line = bufferedReader.readLine();
				 if ( line!= null)
				 {
					 if(!firstLineSkipped)
					 {
						 firstLineSkipped = true;
						 continue;
					 }
					 String[] split = line.split(",");
					 String[] dateSplit = split[1].split("/");
					 String date = dateSplit[2]+dateSplit[1]+dateSplit[0];
					 
					 TreeMap<String, TreeMap<Integer, Map<String, CandleStickData>>> dateMap = cachedData.get(date);
					 if ( dateMap == null)
					 {
						 dateMap = new TreeMap<>();
						 cachedData.put(date, dateMap);
					 }
					 
					 String time = split[2];
					 TreeMap<Integer, Map<String, CandleStickData>> treeMap = dateMap.get(time);
					 if ( treeMap == null)
					 {
						 treeMap = new TreeMap<>();
						 dateMap.put(time, treeMap);
					 }
					 
					 Integer expiryRank = null;
					 if(split[0].equals("NIFTY-I"))
					 {
						 expiryRank = 1;
					 } else if (split[0].equals("NIFTY-II"))
					 {
						 expiryRank = 2;
					 }else if (split[0].equals("NIFTY-III"))
					 {
						 expiryRank = 3;
					 } else {
						 String yearIndex = split[0].substring(5, 7);
						 String monthValue = split[0].substring(7, 10);
						 String monthIndex = monthsIndex[monthsValues.indexOf(monthValue)];
						 expiryRank = Integer.valueOf(yearIndex+monthIndex);
					 }
					 
					 Map<String, CandleStickData> optionsData = treeMap.get(expiryRank);
					 
					 if ( optionsData == null)
					 {
						 optionsData = new HashMap<>();
						 treeMap.put(expiryRank, optionsData);
					 }
					 
					 String instrument = split[0];
					 if(split[0].length()> 10)
					 {
						 instrument = split[0].substring(10);
					 }
					 Float open = Float.parseFloat(split[3]);
					 Float high = Float.parseFloat(split[4]);
					 Float low = Float.parseFloat(split[5]);
					 Float close = Float.parseFloat(split[6]);
					 CandleStickData ohlcData = new CandleStickData(open, high, low, close, null);
					 optionsData.put(instrument, ohlcData);
					 
					 
				 }
				}
				
//				for (Entry<String,TreeMap<String,TreeMap<Integer,Map<String,CandleStickData>>>>  dateMapEntry : cachedData.entrySet()) {
//					String date = dateMapEntry.getKey();
//					TreeMap<String, TreeMap<Integer, Map<String, CandleStickData>>> dateMap = dateMapEntry.getValue();
//					for (Entry<String,TreeMap<Integer,Map<String,CandleStickData>>>  entry : dateMap.entrySet()) {
//						String time = entry.getKey();
//						TreeMap<Integer, Map<String, CandleStickData>> expiryWiseData = entry.getValue();
//						for (Map.Entry<Integer,Map<String,CandleStickData>> expiryWiseEntry : expiryWiseData.entrySet()) {
//							Integer expiry = expiryWiseEntry.getKey();
//							Map<String, CandleStickData> instrumentData = expiryWiseEntry.getValue();
//							for (Map.Entry<String,CandleStickData> instrumentDataEntry : instrumentData.entrySet()) {
//								String instrument = instrumentDataEntry.getKey();
//								CandleStickData value = instrumentDataEntry.getValue();
//								System.out.println(date + " " + time + " " + expiry + " " + instrument + " " + value.getmClose());
//							}
//						}
//					}
//				}
				
				for (Entry<String,TreeMap<String,TreeMap<Integer,Map<String,CandleStickData>>>>  dateMapEntry : cachedData.entrySet()) {
					String date = dateMapEntry.getKey();
					TreeMap<String, TreeMap<Integer, Map<String, CandleStickData>>> dateData = dateMapEntry.getValue();
					DayTrader dayTrader = new DayTrader(date, dateData);
				}
				bufferedReader.close();
			}
			
		}
		
		
	}

}

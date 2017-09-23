package com.bt.strategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.bt.datamodel.CandleStickData;

public class BNBaseStrategy {
	
	protected List<String> timeStamp = new ArrayList<>();
	protected List<Float> closeData = new ArrayList<>();
	protected List<CandleStickData> ohlcDataList = new ArrayList<>();
	private int periodicity = 5;
	
//	protected void initData() {
//		String[] years = getYears();
////		String[] years = {  "2016", "2017"};
////		String[] years = {"Test"};
//		for (String year : years) {
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"JanNIFTY.csv");
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"FebNIFTY.csv");
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"MarNIFTY.csv");
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"AprNIFTY.csv");
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"MayNIFTY.csv");
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"JunNIFTY.csv");
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"JulNIFTY.csv");
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"AugNIFTY.csv");
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"SepNIFTY.csv");
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"OctNIFTY.csv");
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"NovNIFTY.csv");
//			parseCSV("C:\\Trader\\NIFTY\\"+year+"\\"+year+"DecNIFTY.csv");
//		}
//		
//	}
	
	protected String[] getYears() {
		// TODO Auto-generated method stub
		return new String[]{"2013","2014", "2015", "2016", "2017"};
	}

	protected void initData() {
		final String dir = System.getProperty("user.dir");
		String[] years = getYears();
		String seperator = File.separator;
//		String[] years = {  "2016"};
//		String[] years = {"Test"};
		for (String year : years) {
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"AJAN"+seperator+"BANKNIFTY_F1.txt");
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"BFEB"+seperator+"BANKNIFTY_F1.txt");
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"CMAR"+seperator+"BANKNIFTY_F1.txt");
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"DAPR"+seperator+"BANKNIFTY_F1.txt");
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"EMAY"+seperator+"BANKNIFTY_F1.txt");
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"FJUN"+seperator+"BANKNIFTY_F1.txt");
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"GJUL"+seperator+"BANKNIFTY_F1.txt");
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"HAUG"+seperator+"BANKNIFTY_F1.txt");
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"ISEP"+seperator+"BANKNIFTY_F1.txt");
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"JOCT"+seperator+"BANKNIFTY_F1.txt");
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"KNOV"+seperator+"BANKNIFTY_F1.txt");
			parseCSVNew(dir+seperator+"BankNiftyData"+seperator+year+seperator+"LDEC"+seperator+"BANKNIFTY_F1.txt");
		}
		
	}
	
	
	private void parseCSVNew(String fileName)
	{
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String prevTime = "";

		try {

			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {

			        // use comma as separator
				String[] data = line.split(cvsSplitBy);

				String date = data[1];
				String time = data[2];
				Float open = Float.parseFloat(data[3]);
				Float high = Float.parseFloat(data[4]);
				Float low = Float.parseFloat(data[5]);
				Float close = Float.parseFloat(data[6]);
				
				String[] split = time.split(":");
				String min = split[1];
				String hour = split[0];
				int parseInt = Integer.parseInt(min);
				int hourValue = Integer.parseInt(hour);
				int minValue;
				if(parseInt == 0)
				{
					minValue = 59;
					hourValue = hourValue -1;
				} else
				{
					minValue = parseInt-1;
				}
				if(hourValue < 10)
				{
					hour = "0"+ hourValue;
				} else
				{
					hour = ""+ hourValue;
				}
				time = hour+ ":" + minValue+ ":59";
				if( !prevTime.equalsIgnoreCase(time) && (minValue+1)%getPeriodicity()==0)
				{
					String day = "";
					String month = "";
					String year = "";
					day = date.substring(6);
					month = date.substring(4, 6);
					year = date.substring(0, 4);
					
					date = day+ "-"+month+ "-"+ year;
					timeStamp.add(date+"_"+time);
					closeData.add(close);
					CandleStickData ohlcData = new CandleStickData(open, high, low, close, date+"_"+time);
					ohlcDataList.add(ohlcData);
				}
				prevTime = time;

			}

		} catch (FileNotFoundException e) {
//			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected int getPeriodicity() {
		return periodicity;
	}

	protected void setPeriodicity(int periodicity) {
		this.periodicity = periodicity;
	}


}

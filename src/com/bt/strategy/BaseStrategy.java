package com.bt.strategy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bt.datamodel.CandleStickData;

public class BaseStrategy {
	
	protected List<String> timeStamp = new ArrayList<>();
	protected List<Float> closeData = new ArrayList<>();
	protected List<CandleStickData> ohlcDataList = new ArrayList<>();
	private int periodicity = 5;
	
	protected void initData() {
		final String dir = System.getProperty("user.dir");
		String[] years = getYears();
//		String[] years = {  "2016", "2017"};
//		String[] years = {"Test"};
		for (String year : years) {
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"JanNIFTY.csv");
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"FebNIFTY.csv");
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"MarNIFTY.csv");
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"AprNIFTY.csv");
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"MayNIFTY.csv");
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"JunNIFTY.csv");
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"JulNIFTY.csv");
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"AugNIFTY.csv");
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"SepNIFTY.csv");
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"OctNIFTY.csv");
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"NovNIFTY.csv");
			parseCSV(dir + "\\NIFTY\\"+year+"\\"+year+"DecNIFTY.csv");
		}
		
	}
	
	protected String[] getYears() {
		// TODO Auto-generated method stub
		return new String[]{"2013","2014", "2015", "2016", "2017"};
	}

	protected void initBankNiftyData() {
		String[] years = getYears();
//		String[] years = {  "2016"};
//		String[] years = {"Test"};
		for (String year : years) {
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\AJAN+"+year+"\\BANKNIFTY_F1.txt");
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\BFEB+"+year+"\\BANKNIFTY_F1.txt");
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\CMAR+"+year+"\\BANKNIFTY_F1.txt");
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\DAPR+"+year+"\\BANKNIFTY_F1.txt");
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\EMAY+"+year+"\\BANKNIFTY_F1.txt");
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\FJUN+"+year+"\\BANKNIFTY_F1.txt");
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\GJUL+"+year+"\\BANKNIFTY_F1.txt");
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\HAUG+"+year+"\\BANKNIFTY_F1.txt");
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\ISEP+"+year+"\\BANKNIFTY_F1.txt");
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\JOCT+"+year+"\\BANKNIFTY_F1.txt");
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\KNOV+"+year+"\\BANKNIFTY_F1.txt");
			parseCSVNew("C:\\Trader\\BankNiftyData\\"+year+"\\LDEC+"+year+"\\BANKNIFTY_F1.txt");
			
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
	
	private void parseCSV(String fileName)
	{
		if(fileName.contains("2016") || fileName.contains("2017") )
		{
			parseCSVNew(fileName);
			return;
		}
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
				int parseInt = Integer.parseInt(min);
				if( !prevTime.equalsIgnoreCase(time) && (parseInt+1)%getPeriodicity()==0)
				{
					String day = "";
					String month = "";
					String year = "";
					String[] dateSplit = date.split("-");
					if( dateSplit[0].length() == 1)
					{
						day = "0"+dateSplit[0];
					} else
					{
						day = dateSplit[0];
					}

					if( dateSplit[1].length() == 1)
					{
						month = "0"+ dateSplit[1];
					} else
					{
						month = dateSplit[1];
					}

					year = dateSplit[2];
					if(year.length() == 2)
					{
						year = "20"+ year;
					}
					date = day+ "-"+month+ "-"+ year;
					timeStamp.add(date+"_"+time);
					closeData.add(close);
					CandleStickData ohlcData = new CandleStickData(open, high, low, close, date+"_"+time);
					ohlcDataList.add(ohlcData);
				}
				prevTime = time;

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

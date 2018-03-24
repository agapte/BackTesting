package com.bt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class DataAppender {
	
	public static void main(String[] args) throws Exception {
		File file = new File("C:\\Users\\Amit\\Downloads\\drive-download-20180315T171930Z-001");
		File niftyOut = new File("C:\\BackTesting\\niftyOut.csv");
		File bankNiftyOut = new File("C:\\BackTesting\\BANKNIFTY_F1.txt");
		niftyOut.createNewFile();
		bankNiftyOut.createNewFile();
		PrintWriter printWriter = new PrintWriter(niftyOut);
		PrintWriter bankNiftyWriter = new PrintWriter(bankNiftyOut);
		File[] listFiles = file.listFiles();
		for (File dateFolder : listFiles) {
			if(dateFolder.isDirectory())
			{
				File[] dataFiles = dateFolder.listFiles();
				for (File dataFile : dataFiles) {
					if(dataFile.getName().equals("NIFTY_F1.txt"))
					{
						System.out.println(dataFile.getAbsolutePath());
						BufferedReader br = new BufferedReader(new FileReader(dataFile));
						 String line = null;
						 while ((line = br.readLine()) != null) {
							 printWriter.write(line);
							 printWriter.write(System.getProperty("line.separator"));
						 }
						 br.close();
					}
					if(dataFile.getName().equals("BANKNIFTY_F1.txt"))
					{
						System.out.println(dataFile.getAbsolutePath());
						BufferedReader br = new BufferedReader(new FileReader(dataFile));
						 String line = null;
						 while ((line = br.readLine()) != null) {
							 bankNiftyWriter.write(line);
							 bankNiftyWriter.write(System.getProperty("line.separator"));
						 }
						 br.close();
					}
					
				}
			}
		}
		printWriter.flush();
		printWriter.close();
		bankNiftyWriter.flush();
		bankNiftyWriter.close();
	}

}

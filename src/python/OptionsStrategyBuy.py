from nsepy import get_history
from datetime import date
import datetime
import pandas as pd
import os
import glob

import nsepy.derivatives
from nsepy.derivatives import try_to_get_expiry_date, get_expiry_date, stk_exp

years = ['14', '15', '16', '17']
months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']

totalProfit = 0

for year in years:
    path = '/Users/amit.apte/personal/BackTesting/NIFTY_OPTIONS/20'+year # use your path
    print(path)
    yearProfit = 0
    for month in months:
        monthProfit = 0
        file = path + "/"+month+"_20"+year+".csv"
        if not os.path.isfile(file):
            continue
        df = pd.read_csv(file,index_col=['Date','Ticker','Time'], header=0)
        df.sort_index(inplace=True)
        month = file[-12:-9].upper()

        profit = 0;
        for date in df.index.get_level_values('Date').unique():
            df1 = df.ix[date];
        #
        #     # Code for next level of data set
        #
        #     print (df1.index.get_level_values('Time').unique())
        #     for time in df1.index.get_level_values('Time').unique():
        #         df2 = df1.ix[time];
        #         print (df2.index.get_level_values('Ticker').unique())


            if '09:59:59' not in df1.ix['NIFTY-I'].index.get_level_values('Time'):
                continue
            close = (df1.ix['NIFTY-I'].ix['09:59:59']['Close'])
            # print( date +" " + str(close))
            closestPutStrikePrice = int(close/100)*100
            closestCallStrikePrice = closestPutStrikePrice;
            if close - closestPutStrikePrice > 30 and close - closestPutStrikePrice < 70:
                closestCallStrikePrice = closestPutStrikePrice+100
            elif close - closestPutStrikePrice > 70 :
                closestPutStrikePrice = closestPutStrikePrice+100
                closestCallStrikePrice = closestCallStrikePrice+100

            closestPutStrikePrice -= 0;
            closestCallStrikePrice += 0;
        #
            # print(close)
            # print(closestPutStrikePrice)
            # print(closestCallStrikePrice)
            ceTicker = 'NIFTY'+year+month +str(closestPutStrikePrice)+ 'PE'
            peTicker = 'NIFTY'+year+month +str(closestCallStrikePrice)+ 'CE'

        #    print(df1.ix[ceTicker].index.get_level_values('Time').data)
        #
            putProfit = 0
            callProfit = 0

            positionTaken = False
            callSell = 0
            callBuy = 0
            peSqOffIndex = ""
            ceSqOffIndex = ""
            for index, row in df1.ix[ceTicker].iterrows():
                # print(index)
                if positionTaken == False and '09:59' in index:
                    callBuy = df1.ix[ceTicker].ix[index]['Low']
                    PutBuy = df1.ix[peTicker].ix[index]['Low']
                    positionTaken  = True

                if positionTaken == True:
                    callPrice = df1.ix[ceTicker].ix[index]['High']
                    if callPrice > 1.4*callSell:
                        callBuy = 1.4*callSell
                        ceSqOffIndex = index
                        break;
                    if '15:19' in index:
                        callBuy = callPrice
                        ceSqOffIndex = index
                        break;


            putSell = 0
            putBuy = 0
            positionTaken = False
            for index, row in df1.ix[peTicker].iterrows():
                if positionTaken == False and '09:59' in index:
                    putSell = df1.ix[peTicker].ix[index]['Low']
                    positionTaken  = True

                if positionTaken == True:
                    putPrice = df1.ix[peTicker].ix[index]['High']
                    if putPrice > 1.4*putSell:
                        putBuy = 1.4*putSell
                        peSqOffIndex = index
                        break;
                    if '15:19' in index:
                        putBuy = putPrice
                        peSqOffIndex = index
                        break;
            if putBuy != 0 and callBuy != 0 and callSell !=0 and putSell !=0 :
                print("Call sold at " + '09:59' + " " +str(callSell) + " Call bought at " + ceSqOffIndex + " " +str(callBuy))
                print("Put  sold at " + '09:59' + " " +str(putSell)  + " Put  bought at " + peSqOffIndex + " " +str(putBuy))
                putProfit = (putSell - putBuy)
                callProfit = (callSell - callBuy)
                profit = putProfit+callProfit
                print(date + " Net Profit " + str(profit))
                monthProfit = monthProfit + profit
                yearProfit = yearProfit + profit
                totalProfit = totalProfit + profit
                # print (date + ":" + str(profit) )

        print ("Month Profit" + ":" + str(monthProfit) )
    print ("Year Profit" + ":" + str(yearProfit) )

print('Total Profit ' + str(totalProfit))

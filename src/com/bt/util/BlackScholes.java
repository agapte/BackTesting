package com.bt.util;
/******************************************************************************
 *  Compilation:  javac BlackScholes.java MyMath.java
 *  Execution:    java BlackScholes s x r sigma t
 *  
 *  Reads in five command line inputs and calculates the option price
 *  according to the Black-Scholes formula.
 *
 *  % java BlackScholes 23.75 15.00 0.01 0.35 0.5
 *  8.879159279691955                                  (actual =  9.10)
 *      
 *  % java BlackScholes 30.14 15.0 0.01 0.332 0.25
 *  15.177462481562186                                 (actual = 14.50)
 *
 *
 *  Information calculated based on closing data on Monday, June 9th 2003.
 *
 *      Microsoft:   share price:             23.75
 *                   strike price:            15.00
 *                   risk-free interest rate:  1%
 *                   volatility:              35%          (historical estimate)
 *                   time until expiration:    0.5 years
 *
 *       GE:         share price:             30.14
 *                   strike price:            15.00
 *                   risk-free interest rate   1%
 *                   volatility:              33.2%         (historical estimate)
 *                   time until expiration     0.25 years
 *
 *
 *  Reference:  http://www.hoadley.net/options/develtoolsvolcalc.htm
 *
 ******************************************************************************/


public class BlackScholes {
	private static final double P = 0.2316419;
    private static final double B1 = 0.319381530;
    private static final double B2 = -0.356563782;
    private static final double B3 = 1.781477937;
    private static final double B4 = -1.821255978;
    private static final double B5 = 1.330274429;
 
    public static double[] calculate(boolean c, 
            double s, double k, double r, double t, double v) {
 
        double[] p = new double[6];
 
        double d1 = d1(s, k, r, t, v);
        double d2 = d2(s, k, r, t, v);
         
        double sd1 = standardNormalDistribution(d1);
        double cd1 = cumulativeDistribution(d1, sd1);
        double thetaLeft = -(s * sd1 * v) / (2 * Math.sqrt(t));
 
        if (c) {
 
            double cd2 = cumulativeDistribution(d2);
 
            // price
            p[0] = s * cd1 - k * Math.exp(-r * t) * cd2;
 
            // delta
            p[1] = cd1;
 
            // theta
            double thetaRight = r * k * Math.exp(-r * t) * cd2;
            p[4] = thetaLeft - thetaRight;
 
            // rho
            p[5] = k * t * Math.exp(-r * t) * cd2;
 
        } else {
 
            double pcd1 = cumulativeDistribution(-d1);
            double pcd2 = cumulativeDistribution(-d2);
 
            // price
            p[0] = k * Math.exp(-r * t) * pcd2 - s * pcd1;
 
            // delta
            p[1] = cd1 - 1;
 
            // theta
            double thetaRight = r * k * Math.exp(-r * t) * pcd2;
            p[4] = thetaLeft + thetaRight;
 
            // rho
            p[5] = -k * t * Math.exp(-r * t) * pcd2;
 
        }
 
        // gamma
        p[2] = sd1 / (s * v * Math.sqrt(t));
 
        // vega
        p[3] = s * sd1 * Math.sqrt(t);
 
        return p;
 
    }
 
    private static double d1(double s, double k, double r, double t, double v) {
        double top = Math.log(s / k) + (r + Math.pow(v, 2) / 2) * t;
        double bottom = v * Math.sqrt(t);
        return top / bottom;
    }
 
    private static double d2(double s, double k, double r, double t, double v) {
        return d1(s, k, r, t, v) - v * Math.sqrt(t);
    }
 
    public static double cumulativeDistribution(double x) {
        return cumulativeDistribution(x, standardNormalDistribution(x));
    }
 
    public static double cumulativeDistribution(double x, double sdx) {
        double t = 1 / (1 + P * Math.abs(x));
        double t1 = B1 * Math.pow(t, 1);
        double t2 = B2 * Math.pow(t, 2);
        double t3 = B3 * Math.pow(t, 3);
        double t4 = B4 * Math.pow(t, 4);
        double t5 = B5 * Math.pow(t, 5);
        double b = t1 + t2 + t3 + t4 + t5;
        double cd = 1 - sdx * b;
        return x < 0 ? 1 - cd : cd;
    }
 
    public static double standardNormalDistribution(double x) {
        double top = Math.exp(-0.5 * Math.pow(x, 2));
        double bottom = Math.sqrt(2 * Math.PI);
        return top / bottom;
    }
	
    public static void main(String[] args) {
    	boolean c;
        double s, k, r, t, v;
        double[] p;
 
        c = true;
        s = 8611.15;
        k = 8600;
        r = 0.10;
        t = 0.083;
        v = 0.1715;
 
        p = BlackScholes.calculate(c, s, k, r, t, v);
        for (double d : p) {
			System.out.println(round(d,2));
		}
 
        System.out.println();
        System.out.println();
        System.out.println();
        c = false;
        p = BlackScholes.calculate(c, s, k, r, t, v);
        for (double d : p) {
			System.out.println(round(d,2));
		}
 
    }
 
    static double round(double d, int places) {
        int factor = (int) Math.pow(10, places);
        return (double) Math.round(d * factor) / factor;
    }

}
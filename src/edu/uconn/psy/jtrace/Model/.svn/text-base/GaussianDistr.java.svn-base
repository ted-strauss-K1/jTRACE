/*
 * Created on October 14, 2004, 12:03 AM
 */

package edu.uconn.psy.jtrace.Model;
/**
 *
 * @author  tedstrauss
 */
public class GaussianDistr extends java.util.Random{
    double mean, sd;
    java.util.Random rand; 
    
    public GaussianDistr(double _m, double _sd) {
        mean = _m;
        sd=_sd;        
        rand=new java.util.Random();
    }
          
    public double nextGauss(){
        return (sd*rand.nextGaussian())+mean;
    }
    public static void main(String[] args){        
        GaussianDistr gss=new GaussianDistr(0.0,4.0);
        //for(double j=0;j<1;j+=0.01) System.out.println(Math.log(j));
        for(int i=0;i<4000;i++) System.out.println(gss.nextGaussian());    
    //gss.nextGaussian();
    }
}



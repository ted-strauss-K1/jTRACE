/*
 * SimThread.java
 *
 * Created on March 7, 2006, 10:48 AM
 */

package edu.uconn.psy.jtrace.IO;
import java.io.*;

/**
 *
 * @author tedstrauss
 */
public class SimThread  extends Thread{
    
    int progress;
    int cycles;
    boolean BREAK;
    edu.uconn.psy.jtrace.Model.TraceSim sim;
    
    /** Creates a new instance of SimThread */
    public SimThread(edu.uconn.psy.jtrace.Model.TraceSim _sim,int _cycles) {
        sim=_sim;
        progress=0;
        cycles = _cycles;
        BREAK=false;        
    }
    public void run(){ 
        
        sim.cycle(cycles);        
    }
    public edu.uconn.psy.jtrace.Model.TraceSim result(){
        return sim;
    }
    public int currentProgress(){
        progress = sim.getStepsRun();
        return progress;
    }
    public int totalProgress(){
        return cycles;
    }
    public boolean isDone(){
        if(cycles==-1)
            return BREAK;
        else
            return ((progress >= cycles)||BREAK);
    }
    public void BREAK(){
        BREAK=true;
        //script.BREAK();
        System.gc();
    }
    
}

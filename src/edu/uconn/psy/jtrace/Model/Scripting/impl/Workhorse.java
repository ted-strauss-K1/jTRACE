/*
 * ActionWorkhorse.java
 *
 * Created on May 4, 2005, 4:45 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;
import edu.uconn.psy.jtrace.Model.*;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author tedstrauss
 */
public abstract class Workhorse {
    edu.uconn.psy.jtrace.Model.TraceSim currSim;
    edu.uconn.psy.jtrace.Model.TraceParam currParam;
    edu.uconn.psy.jtrace.Model.TraceSimAnalysis currAnalysis;
    edu.uconn.psy.jtrace.UI.GraphParameters currGraphParameters;
    edu.uconn.psy.jtrace.Model.Scripting.TraceScript currScript;
    edu.uconn.psy.jtrace.IO.SimThread thread;
    javax.swing.Timer timer;
    
    /** Creates a new instance of ActionWorkhorse */
    public Workhorse(edu.uconn.psy.jtrace.Model.TraceParam p, edu.uconn.psy.jtrace.Model.TraceSim s, edu.uconn.psy.jtrace.Model.TraceSimAnalysis g,edu.uconn.psy.jtrace.UI.GraphParameters gp,edu.uconn.psy.jtrace.Model.Scripting.TraceScript scr){
        currParam=p;
        currSim=s;
        currAnalysis=g;
        currGraphParameters=gp;
        currScript=scr;
    }
    public void update(){
        currSim = new TraceSim(currParam);
        currSim.cycle(currScript.getCyclesPerSim());
    }
    public void runsim(edu.uconn.psy.jtrace.Model.TraceSim _sim){        
        try{
            thread = new edu.uconn.psy.jtrace.IO.SimThread(_sim,currScript.getCyclesPerSim());
            /*timer = new javax.swing.Timer(200, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    //System.out.print(thread.currentProgress()+" ");
                    if(thread.isDone()){
                        //System.out.print("** "+System.currentTimeMillis()+"\n");
                        currSim=thread.result();
                        timer.stop();
                        System.gc();
                    }
                }            
            });*/
            thread.start();
            thread.join();
        } catch(java.lang.InterruptedException iee){
            iee.printStackTrace();
        }
    }
}

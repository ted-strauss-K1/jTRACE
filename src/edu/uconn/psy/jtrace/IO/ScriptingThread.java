/*
 * ScriptingThread.java
 *
 * Created on May 25, 2005, 5:23 PM
 */

package edu.uconn.psy.jtrace.IO;
import java.io.*;

/**
 *
 * @author tedstrauss
 */
public class ScriptingThread extends Thread{
    int progress;
    int total;
    boolean BREAK;
    edu.uconn.psy.jtrace.Model.Scripting.TraceScript script;
    /** Creates a new instance of ScriptingThread */
    public ScriptingThread(edu.uconn.psy.jtrace.Model.Scripting.TraceScript _script) {
        script=_script;
        progress=0;
        total = script.getTotalIterations();
        BREAK=false;
    }
    public void run(){        
        script.interpretScript();
    }
    public int currentProgress(){
        progress = script.getProgress();
        return progress;
    }
    public int totalProgress(){
        return total;
    }
    public boolean isDone(){
        if(total<0)
            return BREAK;
        else
            return ((progress >= total)||BREAK);
    }
    public void BREAK(){
        BREAK=true;
        script.BREAK();
        System.gc();
    }
}

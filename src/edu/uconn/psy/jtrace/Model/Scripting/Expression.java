/*
 * Expression.java
 *
 * Created on May 3, 2005, 3:50 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;

/**
 *
 * @author tedstrauss
 */
public abstract class Expression implements Cloneable{
    /*protected edu.uconn.psy.jtrace.Model.TraceSim currSim;
    protected edu.uconn.psy.jtrace.Model.TraceParam currParam;
    protected edu.uconn.psy.jtrace.Model.Scripting.TraceScript currScript;*/
    /** Creates a new instance of ActionWorkhorse */
    /*public Expression(edu.uconn.psy.jtrace.Model.TraceParam p, edu.uconn.psy.jtrace.Model.TraceSim s, edu.uconn.psy.jtrace.Model.TraceGraph g,edu.uconn.psy.jtrace.Model.Scripting.TraceScript scr){
        currParam=p;
        currSim=s;
        currGraph=g;        
        currScript=scr;
    }*/
    public Expression(){}
    public abstract String XMLTag();
    public abstract Object clone();
}

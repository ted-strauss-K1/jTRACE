/*
 * LinearParameterFunction.java
 *
 * Created on February 21, 2007, 3:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.uconn.psy.jtrace.Model.Dynamics;

/**
 *
 * @author tedstrauss
 */
public class LinearParameterFunction implements TraceFunction {
    double init;
    double from;
    double to;
    int startCycle;
    int durCycle;
    int paramIndex;
    
    /** Creates a new instance of LinearParameterFunction */
    public LinearParameterFunction(double _init, double _from, double _to, int _startCycle, int _durCycle, int _paramIndex) {
        init=_init;
        from=_from;
        to=_to;
        startCycle=_startCycle;
        durCycle=_durCycle;
        paramIndex=_paramIndex;
    }
    /** Fetches the value of the function at the given cycle */
    public double getFunctionValue(int cyc){
        if(cyc<=startCycle){
            //System.out.println("\t1 cycle "+cyc+", value "+from);        
            return init;
        }
        else if(cyc>startCycle+durCycle){
            //System.out.println("\t2 cycle "+cyc+", value "+to);        
            return to;
        }
        else {
            double result = (from + ((to-from)*(double)(((double)cyc-(double)startCycle)/(double)durCycle)));
            //System.out.println("\t3 cycle "+cyc+", value "+result);
            return result;
        }
    }
    public int getParameterIndex(){return paramIndex;}
    public double getInit(){return init;}
    public double getFrom(){return from;}
    public double getTo(){return to;}
    public int getStartCycle(){return startCycle;}
    public int getDurCycle(){return durCycle;}
    
    public TraceFunction clone(){
        return this;
    }
    
    
}

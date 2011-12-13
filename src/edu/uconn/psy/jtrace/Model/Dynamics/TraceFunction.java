/*
 * TraceFunction.java
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
public interface TraceFunction {
            
    public TraceFunction clone();
    public String toString();
    public int getParameterIndex();
}



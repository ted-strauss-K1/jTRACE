/*
 * TraceError.java
 *
 * Created on April 28, 2004, 1:10 PM
 */

package edu.uconn.psy.jtrace.Model;
import java.util.*;
/**
 *
 * @author  Rafi
 */
public class TraceError {
    
    private Vector vError;
    /** Creates a new instance of TraceError */
    public TraceError() {
        vError = new Vector();
    }
    
    public void report(String report) {
        vError.add(report);
        System.out.println(report);
    }
    
    public Vector getReports() {
        return vError;
    }

    public void clearReports() {
        vError.clear();
    }
}

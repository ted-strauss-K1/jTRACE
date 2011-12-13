/*
 * TraceException.java
 *
 * Created on April 19, 2004, 1:11 PM
 */

package edu.uconn.psy.jtrace.Model;

/**
 *
 * @author  Rafi
 */
public class TraceException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>TraceException</code> without detail message.
     */
    public TraceException() {
    }
    
    
    /**
     * Constructs an instance of <code>TraceException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TraceException(String msg) {
        super(msg);
    }
}

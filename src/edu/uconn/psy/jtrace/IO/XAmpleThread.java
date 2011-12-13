/*
 * XAmpleThread.java
 *
 * Created on June 2, 2005, 6:13 PM
 */

package edu.uconn.psy.jtrace.IO;

/**
 *
 * @author tedstrauss
 */
public class XAmpleThread extends Thread{
    //FXDocumentModelImpl model;
    java.net.URL schema;
    java.net.URL instance;
    
    /** Creates a new instance of XAmpleThread */
    public XAmpleThread(java.net.URL _schema,java.net.URL _instance){        
        schema = _schema;
        instance = _instance;
    }
    public void run(){
        
    }
    public boolean isDone(){ 
        return false;
    }
    
}

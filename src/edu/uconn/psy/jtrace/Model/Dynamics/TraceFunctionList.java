/*
 * TraceFunctionList.java
 *
 * Created on February 21, 2007, 3:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.uconn.psy.jtrace.Model.Dynamics;
import edu.uconn.psy.jtrace.Model.*;
import java.util.*;

/**
 *
 * @author tedstrauss
 */
public class TraceFunctionList{
    
    private Vector funcs;
    
    /** Creates a new instance of TraceFunctionList */
    public TraceFunctionList(){
        funcs = new Vector();          
    }
    
    /** Creates a new instance of TraceFunctionList */
    public TraceFunctionList(TraceFunctionList old) 
    {
        funcs = new Vector();
        for(int i=0;i<old.funcs.size();i++)
            funcs.add(((TraceFunction)old.funcs.get(i)).clone());
    }
    
    public boolean equals(TraceFunctionList tl){
        if(this.size()!=tl.size()) return false;
        for(int i=0;i<size();i++)
            if(!((TraceFunction)funcs.get(i)).equals((TraceFunction)tl.get(i))){
                //System.out.println("lexicons !=");
                return false;                
            }
        //System.out.println("lexicons ==");
        return true;    
    }
    public Object clone(){
        return new TraceFunctionList(this);
    }
    
    /**
     * add word
     */
    public void add(TraceFunction tf)
    {
        funcs.add(tf);        
        System.out.println("function list has "+funcs.size()+" ["+tf.getParameterIndex()+"]");
    }
    
    /**
     * add a whole lexicon to the end of this lexicon.
     */    
    public void append(TraceFunctionList lst){
        for(int i=0;i<lst.size();i++)
            add(((TraceFunction)lst.get(i)).clone());    
    }
    
    public TraceFunction get(int i)
    {
        return (TraceFunction)funcs.get(i);
    }
    
    /**
     * get num funcs
     */
    public int size()
    {
        return funcs.size();
    }
    
    /**
     * reset
     */
    public void reset()
    {
        funcs.removeAllElements();
    }
    
    public int indexOf(String w){
        //for(int i=0;i<funcs.size();i++)
        //   if(w.equals(((TraceFunction)funcs.get(i)).getPhon()))
        //        return i;
        return -1;
    }
    public boolean indexIsInList(int idx){
        for(int i=0;i<size();i++)
            if(((TraceFunction)funcs.get(i)).getParameterIndex()==idx){                
                return true;                
            }
        return false;
    }
    public TraceFunction getFunctionByIndex(int idx){
        for(int i=0;i<size();i++)
            if(((TraceFunction)funcs.get(i)).getParameterIndex()==idx){                
                return (TraceFunction)funcs.get(i);                
            }
        return null;    
    }
    public void removeFunctionByIndex(int idx){
        for(int i=0;i<size();i++)
            if(((TraceFunction)funcs.get(i)).getParameterIndex()==idx){                
                funcs.remove(i);                
            }
        System.out.println("function list has "+funcs.size());
    }
}

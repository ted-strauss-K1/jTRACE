/*
 * FloatIterator.java
 *
 * Created on May 2, 2005, 6:10 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;
import edu.uconn.psy.jtrace.Model.Scripting.impl.*;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.Model.*;

/**
 *
 * @author tedstrauss
 */
public class DummyIterator extends Iterator{
    
    String parameterName;
    int curr, repetitions;
    boolean hasNext;
    
    /** Creates a new instance of VariableIterator */
    public DummyIterator(int _r,edu.uconn.psy.jtrace.Model.Scripting.Expression[] e){
        super(e);
        parameterName="n/a";
        repetitions=_r;
        curr=0;
        if(curr<repetitions) hasNext=true;
        else hasNext=false;
    }
    public Object clone(){
        Expression[] _e = new Expression[expressions.length];
        for(int i=0;i<expressions.length;i++)
            _e[i] = (Expression)expressions[i].clone();
        return new DummyIterator(repetitions,_e);
    }
    public String parameterName(){
        return parameterName;
    }
    public int numberOfIncrements(){
        return repetitions;
    }
    public int currentIncrement(){
        return curr;
    }    
    public boolean hasNext(){
        return hasNext;
    }
    public Object getCurr(){
        return (new Integer(curr));
    }
    public void iterate(){
        curr++;
        if(curr<repetitions) hasNext=true;
        else hasNext=false;        
    }    
    public void resetIterator(){
        curr=0;
        if(curr<repetitions) hasNext=true;
        else hasNext=false;        
    }
    public String XMLTag(){
        String result="";
        result+="<iterate>";
        result+="</iterate>";
        return result;
    }    
    public String iteratorType(){
        return "";
    }
}

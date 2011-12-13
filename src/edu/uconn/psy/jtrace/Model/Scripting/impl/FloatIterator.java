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
public class FloatIterator extends Iterator{
    
    String parameterName;
    int numIncrements, currentIncrement;
    float from, to, curr;
    boolean hasNext;
    
    /** Creates a new instance of VariableIterator */
    public FloatIterator(String pname,float f, float t, int n,edu.uconn.psy.jtrace.Model.Scripting.Expression[] e){
        super(e);
        parameterName=pname;
        from=f;
        to=t;
        if(from>to){ //force an ascending iterator
            from=t;
            to=f;
        }        
        numIncrements=n;
        currentIncrement=0;
        curr=from;
        if(currentIncrement<numIncrements) hasNext=true;
        else hasNext=false;
        expressions = e;
    }
    public Object clone(){
        Expression[] _e = new Expression[expressions.length];
        for(int i=0;i<expressions.length;i++)
            _e[i] = (Expression)expressions[i].clone();
        return new FloatIterator(parameterName,from,to,numIncrements,_e);
    }
    public String parameterName(){
        return parameterName;
    }
    public int numberOfIncrements(){
        return numIncrements;
    }
    public int currentIncrement(){
        return currentIncrement;
    }    
    public boolean hasNext(){
        return hasNext;
    }
    public Object getCurr(){
        return (new Float(curr));
    }
    public void iterate(){
        curr+=((to-from)/((float)numIncrements-1));
        currentIncrement++;
        if(currentIncrement<numIncrements) hasNext=true;
        else hasNext=false;        
    }    
    public void resetIterator(){
        currentIncrement=0;
        curr=from;
        if(currentIncrement<numIncrements) hasNext=true;
        else hasNext=false;        
    }
    public String XMLTag(){
        String result="";
        result+="<iterate>";
        result+="<incrementing-value>";
        result+="<target-parameter>"+parameterName+"</target-parameter>";
        result+="<type>decimal</type>";
        result+="<from>"+from+"</from>";
        result+="<to>"+to+"</to>";
        result+="<number-of-steps>"+numIncrements+"</number-of-steps>";
        result+="</incrementing-value>";        
        if(null!=expressions){
            for(int i=0;i<expressions.length;i++)
                result += "<instructions>"+expressions[i].XMLTag()+"</instructions>";        
        }
        result+="</iterate>";
        return result;
    }    
    public String iteratorType(){
        return "";
    }
}

/*
 * StringIterator.java
 *
 * Created on May 2, 2005, 6:25 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.Model.*;

/**
 *
 * @author tedstrauss
 */
public class StringIterator extends Iterator{
    
    String parameterName;
    String[] list;
    int numIncrements, currentIncrement;
    boolean hasNext;
    edu.uconn.psy.jtrace.Model.Scripting.Expression[] expressions;
    /** Creates a new instance of VariableIterator */
    public StringIterator(String pname,String[] l, Expression[] e){
        super(e);
        parameterName=pname;
        list=l;
        numIncrements=list.length;
        currentIncrement=0;
        expressions=e;
        if(currentIncrement<list.length) hasNext=true;
        else hasNext=false;
    }
     public Object clone(){
        Expression[] _e = new Expression[expressions.length];
        for(int i=0;i<expressions.length;i++)
            _e[i] = (Expression)expressions[i].clone();
        String[] _l = new String[list.length];
        for(int i=0;i<list.length;i++)
            _l[i] = new String(list[i]);        
        return new StringIterator(parameterName,_l,_e);
    }
    public edu.uconn.psy.jtrace.Model.Scripting.Expression[] expressions(){
        return expressions;
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
    public void iterate(){
        currentIncrement++;
        if(currentIncrement<list.length) hasNext=true;
        else hasNext=false;        
    }
    public Object getCurr(){
        if(!hasNext) return null;
        return list[currentIncrement];        
    }
    public void resetIterator(){
        currentIncrement=0;
        if(currentIncrement<list.length) hasNext=true;
        else hasNext=false;
    }
    public String XMLTag(){
        String result = "";
        result += "<iterate>";
        result += "<over-list-of-values>";
        result += "<target-parameter>"+parameterName+"</target-parameter>";
        result += "<type>text</type>";
        for(int i=0;i<list.length;i++)
            result += "<arg><text>"+list[i]+"</text></arg>";
        result += "</over-list-of-values>";
        if(null!=expressions){
            for(int i=0;i<expressions.length;i++)
                result += "<instructions>"+expressions[i].XMLTag()+"</instructions>";        
        }
        result += "</iterate>";        
        return result;
    }
    public String iteratorType(){
        return "";
    }    
}

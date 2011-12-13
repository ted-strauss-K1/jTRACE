/*
 * FourTupleIterator.java
 *
 * Created on June 10, 2005, 3:29 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;
import edu.uconn.psy.jtrace.Model.Scripting.*;
/**
 *
 * @author tedstrauss
 */
public class FourTupleIterator extends Iterator{
    FourTuple[] list;
    String parameterName;
    int numIncrements, currentIncrement;
    boolean hasNext;
    edu.uconn.psy.jtrace.Model.Scripting.Expression[] expressions;
    /** Creates a new instance of VariableIterator */
    public FourTupleIterator(FourTuple[] l, Expression[] e){
        super(e);
        parameterName = "modelInput";
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
        FourTuple[] _l = new FourTuple[list.length];
        for(int i=0;i<list.length;i++)
            _l[i] = (FourTuple)list[i].clone();                    
        return new FourTupleIterator(_l,_e);
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
        if(currentIncrement>=list.length) return null;
        return list[currentIncrement].target;        
    }
    public FourTuple getCurrFourTuple(){
        if(currentIncrement>=list.length) return null;
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
        result += "<over-eye-tracking-four-tuples>";
        if(list != null)
            for(int i=0;i<list.length;i++)
                result += list[i].XMLTag();                
        result += "</over-eye-tracking-four-tuples>";
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

/*
 * PhonemeIterator.java
 *
 * Created on May 2, 2005, 7:47 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.Model.*;

/**
 *
 * @author tedstrauss
 */
public class PhonemeIterator extends Iterator{
    String parameterName;
    String from, to;
    String origString, modString;
    int numIncrements, currentIncrement;
    boolean hasNext;
    
    /** Creates a new instance of PhonemeIterator */
    public PhonemeIterator(String f, String t,int n,Expression[] e){
       super(e);
       parameterName="continuumSpec";
       from=f;
       to=t;
       numIncrements=n;
       currentIncrement=0;
       if(currentIncrement<numIncrements) hasNext=true;
       else hasNext=false;
       expressions = e;
    }
    public Object clone(){
        Expression[] _e;
        if(expressions==null||expressions.length==0){
            _e=null;
        }
        else{
            _e = new Expression[expressions.length];
            for(int i=0;i<expressions.length;i++)
                _e[i] = (Expression)expressions[i].clone();
        }
        return new PhonemeIterator(from,to,numIncrements,_e);
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
       if(!hasNext) return null;
       String continuum = from.concat(to).concat((new Integer(numIncrements)).toString());
       return continuum;       
    }
    public void iterate(){
       currentIncrement++;
       if(currentIncrement<numIncrements) hasNext=true;
       else hasNext=false;       
    }
    public void resetIterator(){
       currentIncrement=0;
       //origString = null;
       //modString = null;
       if(currentIncrement<numIncrements) hasNext=true;
       else hasNext=false;       
    }
    public String from(){return from;}
    public String to(){return to;}
    
    public void setOriginalString(String _o){
        origString = new String(_o);
    }
    public String getOriginalString(){
        return origString;
    }
    public void setModString(String _m){
        modString = new String(_m);
    }
    public String getModString(){
        return modString;
    }
    
    public String XMLTag(){
        String result = "";
        result += "<iterate>";
        result += "<over-phoneme-continuum>";
        result += "<from>"+from+"</from>";
        result += "<to>"+to+"</to>";
        result += "<number-of=steps>"+numIncrements+"</number-of=steps>";
        result += "</over-phoneme-continuum>";
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

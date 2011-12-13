/*
 * PredicateIterator.java
 *
 * Created on May 5, 2005, 12:30 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.Model.*;
/**
 *
 * @author tedstrauss
 */
public class PredicateIterator extends Iterator{
    edu.uconn.psy.jtrace.Model.TraceSim currSim;
    edu.uconn.psy.jtrace.Model.TraceParam currParam;
    edu.uconn.psy.jtrace.Model.TraceSimAnalysis currAnalysis;
    edu.uconn.psy.jtrace.UI.GraphParameters currGraphParameters;
    edu.uconn.psy.jtrace.Model.Scripting.TraceScript currScript;
    
    int numIncrements, currentIncrement;
    boolean hasNext;
    edu.uconn.psy.jtrace.Model.Scripting.Predicate predicate;
    /** Creates a new instance of PredicateIterator */
    public PredicateIterator(Predicate pred,Expression[] e){
        super(e);
        numIncrements=-1;
        currentIncrement=0;
        predicate=pred;
        hasNext=true;
        expressions = e;
    }
    public Object clone(){
        Expression[] _e = new Expression[expressions.length];
        for(int i=0;i<expressions.length;i++)
            _e[i] = (Expression)expressions[i].clone();
        Predicate _p = (Predicate)predicate.clone();
        return new PredicateIterator(_p,_e);
    }
    public String parameterName(){
        String result = "";
        return result;
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
    public void updateState(TraceParam _currParam, TraceSim _currSim, TraceSimAnalysis _currAnalysis,edu.uconn.psy.jtrace.UI.GraphParameters _currGraphParameters,TraceScript _currScript){
        currParam=_currParam;
        currSim=_currSim;
        currScript=_currScript;
        currAnalysis=_currAnalysis;
        currGraphParameters=_currGraphParameters;        
    }
    public void iterate(){
        currentIncrement++;
        //try{
            //predicate.interpret(currParam,currSim,currAnalysis,currGraphParameters,currScript)) hasNext=true;
            if(currScript.interpretPredicate(predicate)) hasNext=true; 
            else hasNext=false;
        //} catch(JTraceScriptingException jtse){jtse.printStackTrace();}
    }
    public Object getCurr(){
        return null;
    }
    //cheaters version
    public void resetIterator(){
        currentIncrement=0;
        //try{
            //if(predicate.interpret(currParam,currSim,currAnalysis,currGraphParameters,currScript)) hasNext=true;
            if(currScript.interpretPredicate(predicate)) hasNext=true; 
            else hasNext=false;
        //} catch(JTraceScriptingException jtse){jtse.printStackTrace();}
    }
    public String iteratorType(){
        return "predicate-iterator";
    }
    public edu.uconn.psy.jtrace.Model.Scripting.Expression[] expressions(){
        return expressions;
    }
    public String XMLTag(){
        String result="";
        result+="<iterate>";
        result+="<while-predicate-is-true>";
        result+=predicate.XMLTag();
        for(int i=0;i<expressions.length;i++)
            result+=expressions[i].XMLTag();
        result+="</while-predicate-is-true>";
        if(null!=expressions){
            for(int i=0;i<expressions.length;i++)
                result += "<instructions>"+expressions[i].XMLTag()+"</instructions>";        
        }
        result+="</iterate>";        
        return result;
    }
    
}

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
public class LexicalIterator extends Iterator{
    
    String parameterName;
    TraceLexicon list;
    int numIncrements, currentIncrement;
    boolean hasNext;
    boolean isRandomized;
    FileLocator locator;
    edu.uconn.psy.jtrace.Model.Scripting.Expression[] expressions;
    /** Creates a new instance of VariableIterator */
    public LexicalIterator(String pname,TraceLexicon lex, FileLocator loc, boolean rando, Expression[] e){
        super(e);
        parameterName="modelInput";
        list=lex;
        isRandomized=rando;
        if(null == list)
            numIncrements=-1;        
        else
            numIncrements=list.size();
        currentIncrement=0;
        expressions=e;
        locator=loc;
        if(null == list) hasNext=false;        
        else if(currentIncrement<list.size()) hasNext=true;
        else hasNext=false;
    }
    public Object clone(){
        Expression[] _e = new Expression[expressions.length];
        for(int i=0;i<expressions.length;i++)
            _e[i] = (Expression)expressions[i].clone();
        TraceLexicon _l;
        if(list == null) 
            _l = null;
        else
            _l = (TraceLexicon)list.clone();
        return new LexicalIterator(parameterName,_l,locator,isRandomized,_e);
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
        if(currentIncrement<list.size()) hasNext=true;
        else hasNext=false;        
    }
    public Object getCurr(){
        if(!hasNext) return null;
        return list.get(currentIncrement).getPhon();                
    }
    public void setLexicon(TraceLexicon _l){
        list=_l;
        if(null == list)
            numIncrements=-1;        
        else
            numIncrements=list.size();
        currentIncrement=0;                
        if(null == list) hasNext=false;        
        else if(currentIncrement<list.size()) hasNext=true;
        else hasNext=false;
    }
    public FileLocator getLexFileLocator(){
        return locator;
    }
    public boolean isRandomized(){
        return isRandomized;
    }
    public void resetIterator(){
        currentIncrement=0;
        if(currentIncrement<list.size()) hasNext=true;
        else hasNext=false;
    }
    public String XMLTag(){
        String result = "";
        result += "<iterate>";
        result += "<over-items-in-a-lexicon>";
        result += "<target-parameter>"+parameterName+"</target-parameter>";
        if(null!=list && list.size()>0){
            result += "<arg>";        
            for(int i=0;i<list.size();i++)
                result+=list.get(i).XMLTag();
            result += "</arg>";
        }
        result += "</over-items-in-a-lexicon>";
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
    public void randomize(){
        java.util.TreeMap map = new java.util.TreeMap();
        sortableWord w;
        java.util.Random rand = new java.util.Random();
        double r;
        for(int i=0;i<list.size();i++){
            r=rand.nextDouble();            
            map.put(new sortableWord((TraceWord)list.get(i).clone(),new Double(r)),new sortableWord(list.get(i),new Double(r)));            
        }
        java.util.Iterator itera = map.values().iterator();
        TraceLexicon newLex = new TraceLexicon();
        while(itera.hasNext()){
            newLex.add(((sortableWord)itera.next()).wd);
        }        
        list = new TraceLexicon(newLex);
    }
    public class sortableWord implements Comparable{
        TraceWord wd;
        double key;
        
        public sortableWord(TraceWord _w, double _k){
            wd = _w;
            key = _k;
        }
        public sortableWord(){}        
        public int compareTo(Object o){
            //in order for the TreeMap class to not replace values
            //in the ADT, this comparable must be defined to distinguish
            //items which have the same peak value.
            int r;
            if(this.key == ((sortableWord)o).key){                 
                r = 0;
            }
            else if(this.key > ((sortableWord)o).key) r = -1;
            else //if(this.peak < ((sortableWordt)o1).peak) 
                r = 1;
            return r;
        }        
    }
}

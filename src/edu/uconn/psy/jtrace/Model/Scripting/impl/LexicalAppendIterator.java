/*
 * LexicalAppendIterator.java
 *
 * Created on June 13, 2006, 11:09 AM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.Model.*;

/**
 *
 * @author tedstrauss
 */
public class LexicalAppendIterator extends Iterator{
    
    /** Creates a new instance of LexicalAppendIterator */
    String parameterName;
    TraceLexicon list;
    TraceLexicon revertLex;
    int numIncrements, currentIncrement;
    int numberToCopyPerIteration;
    boolean duplicateFilter;
    boolean isRandomized;
    boolean hasNext;
    FileLocator locator;
    edu.uconn.psy.jtrace.Model.Scripting.Expression[] expressions;
    
    /** Creates a new instance of LexicalAppendIterator */
    public LexicalAppendIterator(String pname,TraceLexicon lex, FileLocator loc, int n, boolean filt, boolean rando, Expression[] e){
        super(e);
        expressions=e;
        parameterName=pname;
        numberToCopyPerIteration=n;
        list=lex;
        locator=loc;
        duplicateFilter=filt;
        isRandomized=rando;
        if(null == list)
            numIncrements=-1;        
        else
            numIncrements=Math.round(list.size()/numberToCopyPerIteration);
        currentIncrement=0;
        if(list == null && locator != null) hasNext=true;        
        else if(currentIncrement<=list.size()) hasNext=true;        
        else hasNext=false;
        //System.out.println("made a LexicalAppendIterator: "+list+" "+locator+" "+numberToCopyPerIteration+" "+numIncrements);
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
        return new LexicalAppendIterator(parameterName,_l,locator,numberToCopyPerIteration,duplicateFilter,isRandomized,_e);
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
    public boolean isRandomized(){
        return isRandomized;
    } 
    public boolean isDuplicateFilter(){
        return duplicateFilter;
    }
    public void iterate(){
        currentIncrement++;//=numberToCopyPerIteration;
        //System.out.println("current increment: "+currentIncrement+" of "+numIncrements);
        if(currentIncrement<=numIncrements||numIncrements==-1) hasNext=true;
        else hasNext=false;        
    }
    public Object getCurr(){
        if(!hasNext) return null;
        if(currentIncrement<0||currentIncrement>list.size()) return null;
        else return list;                
    }
    public int getNumberToCopyPerIteration(){
    return numberToCopyPerIteration;
    }
    public TraceLexicon getLexicon(){
        return list;
    }
    public void setLexicon(TraceLexicon _l){
        list=_l;
        if(list == null )
            numIncrements=-1;        
        else
            numIncrements=list.size();
        currentIncrement=0;                
        if(list == null) hasNext=false;        
        else if(currentIncrement<=list.size()) hasNext=true;
        else hasNext=false;
    }
    public void setRevertLex(TraceLexicon _l){
        revertLex=(TraceLexicon)_l.clone();
    }
    public TraceLexicon getRevertLex(){
        return revertLex;
    }
    public FileLocator getLexFileLocator(){
        return locator;
    }
    public void resetIterator(){
        revertLex=null;
        currentIncrement=0;
        if(currentIncrement<=list.size()) hasNext=true;
        else hasNext=false;
    }
    public String XMLTag(){
        String result = "";
        result += "<iterate>";
        result += "<appending-items-to-the-lexicon>";
        result += "<target-parameter>"+parameterName+"</target-parameter>";
        //@@@
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

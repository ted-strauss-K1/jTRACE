/*
 * Predicate.java
 *
 * Created on May 3, 2005, 6:40 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.Model.*;

/**
 *
 * @author tedstrauss
 */
public class Predicate implements Primitive{
    String name;
    Primitive[] arg;
    public Predicate(String n,Primitive[] a){
        name=n;
        arg=a;
    }
    public Object clone(){
        Primitive[] _arg = new Primitive[arg.length];
        for(int i=0;i<arg.length;i++)
            _arg[i] = (Primitive)arg[i].clone();        
        return new Predicate(name, _arg);
    }        
    /*public boolean interpret(TraceParam p, TraceSim s, TraceSimAnalysis g,edu.uconn.psy.jtrace.UI.GraphParameters gp,TraceScript scr)throws edu.uconn.psy.jtrace.Model.Scripting.impl.JTraceScriptingException{        
        edu.uconn.psy.jtrace.Model.Scripting.impl.PredicateWorkhorse pw=new edu.uconn.psy.jtrace.Model.Scripting.impl.PredicateWorkhorse(p,s,g,gp,scr);
        boolean result=false;
        //evaluates any queries that may be in the arguments and replaces them with a primitive value.
        arg=scr.replaceQueriesWithValues(arg);         
        try{
            result=pw.evaluatePredicate(this);
        }
        catch(edu.uconn.psy.jtrace.Model.Scripting.impl.JTraceScriptingException jtse){jtse.printStackTrace();}
        return result;
    }*/
    
    public String name(){return name;}
    public Primitive[] arguments(){return arg;}    
    public void setArguments(Primitive[] _arg){
        arg = _arg;        
    }
    
    public String XMLTag(){
        /*
        <xsd:element name="equals">
        <xsd:element name="not-equal">
        <xsd:element name="is-greater-than">
        <xsd:element name="is-less-than">
        <xsd:element name="is-member-of-list">
        <xsd:element name="true">
        <xsd:element name="false">
        <xsd:element name="and">
        <xsd:element name="or">
        <xsd:element name="not">
        */
        String result="";
        result+="<predicate>";
        result+="<"+name+">";
        if(name.equals("equals")){
            result+="<one>"+arg[0].XMLTag()+"</one>";
            result+="<two>"+arg[1].XMLTag()+"</two>";
        }
        else if(name.equals("not-equal")){
            result+="<one>"+arg[0].XMLTag()+"</one>";
            result+="<two>"+arg[1].XMLTag()+"</two>";
        }
        else if(name.equals("is-greater-than")){
            result+="<one>"+arg[0].XMLTag()+"</one>";
            result+="<two>"+arg[1].XMLTag()+"</two>";
        }
        else if(name.equals("is-less-than")){
            result+="<one>"+arg[0].XMLTag()+"</one>";
            result+="<two>"+arg[1].XMLTag()+"</two>";
        }
        else if(name.equals("is-member-of-list")){
            result+="<search-item>"+arg[0].XMLTag()+"</search-item>";
            result+="<list>"+arg[1].XMLTag()+"</list>";
        }
        else if(name.equals("and")){
            result+="<one>"+arg[0].XMLTag()+"</one>";
            result+="<two>"+arg[1].XMLTag()+"</two>";
        }
        else if(name.equals("or")){
            result+="<one>"+arg[0].XMLTag()+"</one>";
            result+="<two>"+arg[1].XMLTag()+"</two>";
        }
        else if(name.equals("not")){
            result+="<one>"+arg[0].XMLTag()+"</one>";         
        }
        else if(name.equals("true")){
            result+="<arg>n/a</arg>";         
        }
        else if(name.equals("false")){
            result+="<arg>n/a</arg>";         
        }
        result+="</"+name+">";
        result+="</predicate>";
        return result;
    }    
    public Boolean booleanValue(){return null;}
    public Predicate predicateValue(){ return this; }
    public Query queryValue(){ return null;}
    public FileLocator fileLocatorValue(){ return null;}
    public Text textValue(){ return null;}
    public Int intValue(){ return null;}
    public Decimal decimalValue(){ return null;}
    public ListOfPrimitives listValue(){return null;}
    public edu.uconn.psy.jtrace.Model.TraceLexicon lexiconValue(){ return null;}
    public edu.uconn.psy.jtrace.Model.TraceParam parametersValue(){ return null;}        
    
}

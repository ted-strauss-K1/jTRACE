/*
 * Text.java
 *
 * Created on May 5, 2005, 1:08 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;
import java.util.*;
/**
 *
 * @author tedstrauss
 */
public class ListOfPrimitives implements Primitive{
    LinkedList value;
    /** Creates a new instance of Text */
    public ListOfPrimitives(LinkedList _v) {
        value=_v;
    }
    public LinkedList value(){return value;}
    public Primitive get(int i){return (Primitive)value.get(i);}
    public String XMLTag(){
        String result="";
        result+="<list>";
        /*String type;
        if(null==value||value.size()==0)
            type="text";
        else if(value.get(0) instanceof Decimal)
            type="decimal";
        else if(value.get(0) instanceof FileLocator)
            type="file";
        else if(value.get(0) instanceof Int)
            type="int";
        else if(value.get(0) instanceof ListOfPrimitives)
            type="list";
        else if(value.get(0) instanceof Predicate)
            type="predicate";
        else if(value.get(0) instanceof Query)
            type="query";
        else if(value.get(0) instanceof Text)
            type="text";
        else if(value.get(0) instanceof ActiveObject)
            type=((ActiveObject)value.get(0)).value();
        else if(value.get(0) instanceof edu.uconn.psy.jtrace.Model.TraceLexicon)
            type="lexicon";
        else if(value.get(0) instanceof edu.uconn.psy.jtrace.Model.TraceParam)
            type="parameters";
        else
            type="text";
        result+="<type>"+type+"</type>";*/
        for(int i=0;i<value.size();i++)
            result+="<arg>"+((Primitive)value.get(i)).XMLTag()+"</arg>";        
        result+="</list>";
        return result;        
    }    
    public Boolean booleanValue(){return null;}
    public Predicate predicateValue(){ return null; }
    public Query queryValue(){ return null;}
    public FileLocator fileLocatorValue(){ return null;}
    public Text textValue(){ return null;}
    public Int intValue(){ return null;}
    public Decimal decimalValue(){ return null;}
    public ListOfPrimitives listValue(){return this;}
    public edu.uconn.psy.jtrace.Model.TraceLexicon lexiconValue(){ return null;}
    public edu.uconn.psy.jtrace.Model.TraceParam parametersValue(){ return null;}
    public Object clone(){
        LinkedList ll=new LinkedList();
        for(int i=0;i<value.size();i++)
            ll.add(value.get(i));
        return new ListOfPrimitives(ll);
    }
    
    
}

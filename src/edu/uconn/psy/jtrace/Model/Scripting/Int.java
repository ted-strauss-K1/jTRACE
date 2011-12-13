/*
 * Int.java
 *
 * Created on May 5, 2005, 1:10 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;

/**
 *
 * @author tedstrauss
 */
public class Int implements Primitive {
    int value;
    /** Creates a new instance of Int */
    public Int(int v) {
        value=v;
    }
    public int value(){return value;}
    public String XMLTag(){
        return "<int>"+value+"</int>";
    }    
    public Boolean booleanValue(){return null;}
    public Predicate predicateValue(){ return null; }
    public Query queryValue(){ return null;}
    public FileLocator fileLocatorValue(){ return null;}
    public Text textValue(){ return null;}
    public Int intValue(){ return this;}
    public Decimal decimalValue(){ return null;}
    public ListOfPrimitives listValue(){return null;}
    public edu.uconn.psy.jtrace.Model.TraceLexicon lexiconValue(){ return null;}
    public edu.uconn.psy.jtrace.Model.TraceParam parametersValue(){ return null;}
    public Object clone(){
        return new Int(value);
    }
}

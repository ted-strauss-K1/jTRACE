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
public class Boolean implements Primitive {
    boolean value;
    /** Creates a new instance of Int */
    public Boolean(boolean v) {
        value=v;
    }
    public boolean value(){return value;}
    public String XMLTag(){
        return "<boolean>"+value+"</boolean>";
    }    
    public Boolean booleanValue(){return this;}
    public Predicate predicateValue(){ return null;}
    public Query queryValue(){ return null;}
    public FileLocator fileLocatorValue(){ return null;}
    public Text textValue(){ return null;}
    public Int intValue(){ return null;}
    public Decimal decimalValue(){ return null;}
    public ListOfPrimitives listValue(){return null;}
    public edu.uconn.psy.jtrace.Model.TraceLexicon lexiconValue(){ return null;}
    public edu.uconn.psy.jtrace.Model.TraceParam parametersValue(){ return null;}
    public Object clone(){
        return new Boolean(value);
    }
}

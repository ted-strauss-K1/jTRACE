/*
 * Text.java
 *
 * Created on May 5, 2005, 1:08 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;

/**
 *
 * @author tedstrauss
 */
public class Text implements Primitive{
    String value;
    /** Creates a new instance of Text */
    public Text(String v) {
        value=v;
    }
    public String value(){return value;}
    public String XMLTag(){
        return "<text>"+value+"</text>";
    }    
    public Boolean booleanValue(){return null;}
    public Predicate predicateValue(){ return null; }
    public Query queryValue(){ return null;}
    public FileLocator fileLocatorValue(){ return null;}
    public Text textValue(){ return this;}
    public Int intValue(){ return null;}
    public Decimal decimalValue(){ return null;}
    public ListOfPrimitives listValue(){return null;}
    public edu.uconn.psy.jtrace.Model.TraceLexicon lexiconValue(){ return null;}
    public edu.uconn.psy.jtrace.Model.TraceParam parametersValue(){ return null;}
    public Object clone(){
        return new Text(value);
    }
    
}

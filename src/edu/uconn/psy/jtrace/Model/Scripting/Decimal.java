/*
 * Decimal.java
 *
 * Created on May 5, 2005, 1:11 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;

/**
 *
 * @author tedstrauss
 */
public class Decimal implements Primitive{
    double value;
    /** Creates a new instance of Decimal */
    public Decimal(double v) {
        value=v;
    }
    public double value(){return value;}
    public String XMLTag(){
        return "<decimal>"+value+"</decimal>";
    }    
    public Boolean booleanValue(){return null;}
    public Predicate predicateValue(){ return null; }
    public Query queryValue(){ return null;}
    public FileLocator fileLocatorValue(){ return null;}
    public Text textValue(){ return null;}
    public Int intValue(){ return null;}
    public Decimal decimalValue(){ return this;}
    public ListOfPrimitives listValue(){return null;}
    public edu.uconn.psy.jtrace.Model.TraceLexicon lexiconValue(){ return null;}
    public edu.uconn.psy.jtrace.Model.TraceParam parametersValue(){ return null;}
    public Object clone(){
        return new Decimal(value);
    }
}

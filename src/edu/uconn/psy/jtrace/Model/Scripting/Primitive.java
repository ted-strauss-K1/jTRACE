/*
 * Primitive.java
 *
 * Created on May 4, 2005, 4:57 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;

/**
 *
 * @author tedstrauss
 */
public interface Primitive extends java.lang.Cloneable{
    Boolean booleanValue();
    Text textValue();
    Int intValue();
    Decimal decimalValue();
    edu.uconn.psy.jtrace.Model.TraceLexicon lexiconValue();
    edu.uconn.psy.jtrace.Model.TraceParam parametersValue();
    Predicate predicateValue();
    Query queryValue();
    ListOfPrimitives listValue();
    FileLocator fileLocatorValue();
    String XMLTag();
    public Object clone();
}

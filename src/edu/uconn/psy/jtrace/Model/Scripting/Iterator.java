/*
 * Iterator.java
 *
 * Created on May 2, 2005, 12:58 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;
import edu.uconn.psy.jtrace.Model.*;
import edu.uconn.psy.jtrace.Model.Scripting.*;
      
/**
 *
 * @author tedstrauss
 */
public abstract class Iterator extends Expression {
    protected Expression[] expressions;
    
    //TODO?
    public Iterator(Expression[] e){
        expressions=e;
    }
    public abstract String parameterName();
    public abstract int numberOfIncrements();
    public abstract int currentIncrement();
    public abstract boolean hasNext();
    public abstract void iterate();
    public abstract Object getCurr();
    public abstract void resetIterator();
    public abstract String iteratorType();
    public Expression[] expressions(){return expressions;}
    public abstract String XMLTag();
    public abstract Object clone();
}

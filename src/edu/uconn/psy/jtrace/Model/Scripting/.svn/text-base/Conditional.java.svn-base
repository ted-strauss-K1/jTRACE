/*
 * Conditional.java
 *
 * Created on May 3, 2005, 3:52 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;
import edu.uconn.psy.jtrace.Model.*;
import edu.uconn.psy.jtrace.Model.Scripting.*;

/**
 *
 * @author tedstrauss
 */
public class Conditional extends Expression{
    Predicate If;
    Expression[] Then;
    Expression[] Else;
    public Conditional(Predicate pred,Expression[] _then,Expression[] _else){
        If=pred;
        Then=_then;
        Else=_else;
    }
    public Object clone(){
        Predicate _p = (Predicate)If.clone();
        Expression[] _then= new Expression[Then.length];
        for(int i=0;i<Then.length;i++)
            _then[i] = (Expression)Then[i].clone();        
        Expression[] _else= new Expression[Else.length];        
        for(int i=0;i<Else.length;i++)
            _else[i] = (Expression)Else[i].clone();                
        return new Conditional(_p, _then, _else);
    }
    public Predicate getPredicate(){
        return If;
    }
    public Expression[] thenExpressions(){
        return Then;
    }
    public Expression[] elseExpressions(){
        return Else;
    }
    public String XMLTag(){
        String result="";
        result+="<conditional>";
        result+="<if>"+If.XMLTag()+"</if>";
        for(int i=0;i<Then.length;i++)
            result+="<then>"+Then[i].XMLTag()+"</then>";
        for(int i=0;i<Else.length;i++)
            result+="<else>"+Else[i].XMLTag()+"</else>";        
        result+="</conditional>";
        return result;
    }
}

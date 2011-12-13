/*
 * PredicateWorkhorse.java
 *
 * Created on May 5, 2005, 7:05 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.Model.*;

/**
 *
 * @author tedstrauss
 */
public class PredicateWorkhorse extends Workhorse{
    
    /** Creates a new instance of PredicateWorkhorse */
    public PredicateWorkhorse(TraceParam p, TraceSim s, TraceSimAnalysis g,edu.uconn.psy.jtrace.UI.GraphParameters gp,TraceScript scr){
        super(p,s,g,gp,scr);
    }
    public boolean evaluatePredicate(Predicate pred)throws JTraceScriptingException{
        String name=pred.name();
        Primitive[] args=pred.arguments();
        
        if(name.equalsIgnoreCase("true")){
            return true;
        }
        else if(name.equalsIgnoreCase("false")){
            return false;
        }
        else if(name.equalsIgnoreCase("equals")){
            if(args.length!=2)
                throw new JTraceScriptingException("PredicateWorkhorse.evaluatePredicate() : incorrect arguments for predicate \'equals\'; requires (Primitive, Primitive)");
            return primitiveEquals(args[0],args[1]);
        }
        else if(name.equalsIgnoreCase("is-greater-than")){
            if((args.length!=2))
                throw new JTraceScriptingException("PredicateWorkhorse.evaluatePredicate() : incorrect arguments for predicate \'is-greater-than\'; requires (Int, Int) or (Decimal, Decimal)");
            if((null!=args[0].intValue()&&null!=args[1].intValue())) 
                return isGreaterThanInt(args[0].intValue(),args[1].intValue());
            else 
                return isGreaterThanDecimal(args[0].decimalValue(),args[1].decimalValue());            
        }
        else if(name.equalsIgnoreCase("is-less-than")){
            if((args.length!=2)||
              (!(null!=args[0].intValue()&&null!=args[1].intValue()))||
              (!(null!=args[0].decimalValue()&&null!=args[1].decimalValue())))
                throw new JTraceScriptingException("PredicateWorkhorse.evaluatePredicate() : incorrect arguments for predicate \'is-less-than\'; requires (Int, Int) or (Decimal, Decimal)");
            if((null!=args[0].intValue()&&null!=args[1].intValue())) return isGreaterThanInt(args[1].intValue(),args[0].intValue());
            else return isGreaterThanDecimal(args[1].decimalValue(),args[0].decimalValue());            
        }
        else if(name.equalsIgnoreCase("is-member-of-list")){
            //TODO.
            //if((args.length!=2)||
            //  (!(null!=args[1].)))
            return true;
        }
        else if(name.equalsIgnoreCase("and")){
            if((args.length!=2)||
              (!(null!=args[0].predicateValue()&&null!=args[1].predicateValue())))
                throw new JTraceScriptingException("PredicateWorkhorse.evaluatePredicate() : incorrect arguments for predicate \'and\'; requires (Predicate, Predicate)");
            return and(args[0].predicateValue(),args[1].predicateValue());
        }
        else if(name.equalsIgnoreCase("or")){
            if((args.length!=2)||
              (!(null!=args[0].predicateValue()&&null!=args[1].predicateValue())))
                throw new JTraceScriptingException("PredicateWorkhorse.evaluatePredicate() : incorrect arguments for predicate \'or\'; requires (Predicate, Predicate)");
            return or(args[0].predicateValue(),args[1].predicateValue());
        }
        else if(name.equalsIgnoreCase("not")){
            if((args.length!=1)||(null==args[0].predicateValue()))
                throw new JTraceScriptingException("PredicateWorkhorse.evaluatePredicate() : incorrect arguments for predicate \'not\'; requires (Predicate)");
            return not(args[0].predicateValue());
        }
        
        /*                      <!--<xsd:enumeration value="satisfies"/>
                                <xsd:enumeration value="exceeds"/>-->
          */
        
        return true;
    }    
    public boolean isGreaterThanDecimal(Decimal one,Decimal two){
        return (one.value()>two.value());                
    }
    public boolean isGreaterThanInt(Int one,Int two){
        return (one.value()>two.value());                
    }
    public boolean and(Predicate one,Predicate two) throws JTraceScriptingException{
        return(currScript.interpretPredicate(one)&&currScript.interpretPredicate(two));    
        //return(one.interpret(currParam,currSim,currAnalysis,currGraphParameters,currScript)&&two.interpret(currParam,currSim,currAnalysis,currGraphParameters,currScript));
    }
    public boolean or(Predicate one,Predicate two) throws JTraceScriptingException{
        return(currScript.interpretPredicate(one)||currScript.interpretPredicate(two));    
        //return(one.interpret(currParam,currSim,currAnalysis,currGraphParameters,currScript)||two.interpret(currParam,currSim,currAnalysis,currGraphParameters,currScript));
    }
    public boolean not(Predicate one) throws JTraceScriptingException{
        return(!currScript.interpretPredicate(one));    
        //return(!one.interpret(currParam,currSim,currAnalysis,currGraphParameters,currScript));
    }
    public boolean primitiveEquals(Primitive one,Primitive two){
        boolean result;        
        
        if(one.booleanValue()!=null&&two.booleanValue()!=null){
            return (one.booleanValue().value()==two.booleanValue().value());
        }
        else if(one.decimalValue()!=null&&two.decimalValue()!=null){
            return (one.decimalValue().value()==two.decimalValue().value());
        }
        else if(one.fileLocatorValue()!=null&&two.fileLocatorValue()!=null){
            return (one.fileLocatorValue().equals(two.fileLocatorValue()));
        }
        else if(one.intValue()!=null&&two.intValue()!=null){
            return (one.intValue().value()==two.intValue().value());
        }
        else if(one.predicateValue()!=null&&two.predicateValue()!=null){
            return (one.predicateValue().equals(two.predicateValue()));
        }
        else if(one.queryValue()!=null&&two.queryValue()!=null){
            return (one.queryValue().equals(two.queryValue()));
        }
        else if(one.textValue()!=null&&two.textValue()!=null){
            return (one.textValue().value().equals(two.textValue().value()));
        }
        else if(one.parametersValue()!=null&&two.parametersValue()!=null){
            return (one.parametersValue().equals(two.parametersValue()));
        }
        else if(one.lexiconValue()!=null&&two.lexiconValue()!=null){
            return (one.lexiconValue().equals(two.lexiconValue()));
        }
        else{
            return false;
        }        
    }
}

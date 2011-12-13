/*
 * FourTuple.java
 *
 * Created on June 10, 2005, 4:03 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;

/**
 *
 * @author tedstrauss
 */
public class FourTuple implements Cloneable{
    String target, cOne, cTwo, cThree;
    public FourTuple(String _target, String _cOne, String _cTwo, String _cThree){
        target = _target;
        cOne = _cOne;
        cTwo = _cTwo;
        cThree = _cThree;
    }
    public Object clone(){
        return new FourTuple(target,cOne,cTwo,cThree);
    }
    public String[] toStringArray(){
        String[] result = new String[4];
        result[0] = target;
        result[1] = cOne;
        result[2] = cTwo;
        result[3] = cThree;
        return result;
    }
    public String XMLTag(){
        String result = "<four-tuple>";
        result += "<target>"+target+"</target>";
        result += "<competitor-one>"+cOne+"</competitor-one>";
        result += "<competitor-two>"+cTwo+"</competitor-two>";
        result += "<competitor-three>"+cThree+"</competitor-three>";
        result += "</four-tuple>";
        return result;
    }
}
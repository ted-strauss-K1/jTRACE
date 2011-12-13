/*
 * FileLocator.java
 *
 * Created on May 5, 2005, 12:49 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;

/**
 *
 * @author tedstrauss
 */
public class FileLocator implements Primitive{
    String absolutePath;
    String relativePath;
    String fileName;
    
    public FileLocator(String a,String r,String n){
        absolutePath=a;
        relativePath=r;
        fileName=n;    
    }
    public String absolutePath(){return absolutePath;}
    public String relativePath(){return relativePath;}
    public String fileName(){return fileName;}
    
    public String XMLTag(){
        String result="";
        result+="<file>";
        if(null!=absolutePath)
            result+="<absolute-path>"+absolutePath+"</absolute-path>";
        else if(null!=relativePath)
            result+="<relative-path>"+relativePath+"</relative-path>";
        if(null!=fileName)
            result+="<name>"+fileName+"</name>";
        result+="</file>";
        return result;
    }   
    public Boolean booleanValue(){return null;}
    public Predicate predicateValue(){ return null; }
    public Query queryValue(){ return null;}
    public FileLocator fileLocatorValue(){ return this;}
    public Text textValue(){ return null;}
    public Int intValue(){ return null;}
    public Decimal decimalValue(){ return null;}
    public ListOfPrimitives listValue(){return null;}
    public edu.uconn.psy.jtrace.Model.TraceLexicon lexiconValue(){ return null;}
    public edu.uconn.psy.jtrace.Model.TraceParam parametersValue(){ return null;}
    public Object clone(){
        return new FileLocator(absolutePath, relativePath, fileName);
    }
    public String toString(){
        return new String("FileLocator("+absolutePath+", "+relativePath+", "+fileName+")");
    }
    
}

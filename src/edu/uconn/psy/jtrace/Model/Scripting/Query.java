/*
 * Query.java
 *
 * Created on May 4, 2005, 7:53 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;

/**
 *
 * @author tedstrauss
 */
public class Query implements Primitive{
    String name;
    String returnType;
    Primitive[] arg;
    
    public Query(String n,String t, Primitive[] a){
        name=n;
        returnType=t;
        arg=a;    
    } 
    public Query(Query old){
        name=old.name;
        returnType=old.returnType;
        if(old.arg.length>0){
            arg=new Primitive[old.arg.length];
            for(int i=0;i<old.arg.length;i++)
                arg[i] = (Primitive)old.arg[i].clone();        
        }
        else
            arg = new Primitive[0];
    }
    public Object clone(){
        /*Primitive[] _arg;
        if(arg.length>0){
            _arg = new Primitive[arg.length];
            for(int i=0;i<arg.length;i++)
                _arg[i] = (Primitive)arg[i].clone();        
        }
        else
            _arg = new Primitive[0];*/
        return new Query(this);
    }
    
    public String name(){ return name;}
    public String returnType(){ return returnType;}        
    public Primitive[] arguments(){return arg;}
    public void setArguments(Primitive[] a){
        arg = a;
    }
    public String XMLTag(){
        /* From jTRACESchema.xsd
        <xsd:element name="item-with-highest-peak">
        <xsd:element name="value-of-highest-peak">
        <xsd:element name="item-with-nth-highest-peak">
        <xsd:element name="value-of-nth-highest-peak">
        <xsd:element name="nth-item-in-lexicon">
        <xsd:element name="current-input">
        <xsd:element name="peak-value-of-item">
        <xsd:element name="cycle-when-item-exceeds-threshold">
        <xsd:element name="nth-item-to-exceed-threshold">
         */
        String result="";
        result+="<query>";
        result+="<"+name+">";
        result+="<return-type>"+returnType+"</return-type>";
        if(name.equals("item-with-highest-peak")||
           name.equals("value-of-highest-peak")||
           name.equals("current-input")){
            //no argument            
        }
        else if(name.equals("item-with-nth-highest-peak")){
            result+="<N>"+arg[0].intValue().value()+"</N>";
        }
        else if(name.equals("value-of-nth-highest-peak")){
            result+="<N>"+arg[0].intValue().value()+"</N>";
        }
        else if(name.equals("nth-item-in-lexicon")){
            result+="<N>"+arg[0].intValue().value()+"</N>";
        }
        else if(name.equals("peak-value-of-item")){
            result+="<arg>"+arg[0].XMLTag()+"</arg>";
        }
        else if(name.equals("cycle-when-item-exceeds-threshold")){
            result+="<item>"+arg[0].textValue().value()+"</item>";
            result+="<threshold>"+arg[1].decimalValue().value()+"</threshold>";
        }
        else if(name.equals("nth-item-to-exceed-threshold")){
            result+="<N>"+arg[0].intValue().value()+"</N>";
            result+="<threshold>"+arg[1].decimalValue().value()+"</threshold>";
        }
        else if(name.equals("decision-rule-report")){
            result+="<threshold>"+arg[0].decimalValue().value()+"</threshold>";
            result+="<item>"+arg[1].XMLTag()+"</item>";
            result+="<verbosity>"+arg[2].intValue().value()+"</verbosity>";
        }
        result+="</"+name+">";
        result+="</query>";
        return result;
    }
    public Boolean booleanValue(){return null;}
    public Predicate predicateValue(){ return null; }
    public Query queryValue(){ return this;}
    public FileLocator fileLocatorValue(){ return null;}
    public Text textValue(){ return null;}
    public Int intValue(){ return null;}
    public Decimal decimalValue(){ return null;}
    public ListOfPrimitives listValue(){return null;}
    public edu.uconn.psy.jtrace.Model.TraceLexicon lexiconValue(){ return null;}
    public edu.uconn.psy.jtrace.Model.TraceParam parametersValue(){ return null;}
}

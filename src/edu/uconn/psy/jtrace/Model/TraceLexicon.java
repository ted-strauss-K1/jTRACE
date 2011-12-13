package edu.uconn.psy.jtrace.Model;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.Model.*;
import java.util.*;

/**
 * A TraceLexicon is a Vector of TraceWords.
 * 
 * @author Rafi Pelosoff
 * @author Ted Strauss
 * @author Harlan Harris
 */
public class TraceLexicon implements edu.uconn.psy.jtrace.Model.Scripting.Primitive 
{
    private Vector words;
    private String description;
    /** 
     * void constructor
     */
    public TraceLexicon()
    {
       words = new Vector(); 
       description = "";
    }
    
    /**
     * copy constructor
     */
    public TraceLexicon(TraceLexicon old)
    {
        words = new Vector();
        for(int i=0;i<old.words.size();i++)
            words.add(((TraceWord)old.words.get(i)).clone());
        description = old.getDescription();
    }
    
    public boolean equals(TraceLexicon tl){
        if(this.size()!=tl.size()) return false;
        for(int i=0;i<size();i++)
            if(!get(i).getPhon().equals(tl.get(i).getPhon())){
                //System.out.println("lexicons !=");
                return false;                
            }
        //System.out.println("lexicons ==");
        return true;    
    }
    public Object clone(){
        return new TraceLexicon(this);
    }
    
    /**
     * add word
     */
    public void add(TraceWord wd)
    {
        words.add(wd);
        //System.out.println("adding: "+wd.getPhon()+" lexsize="+words.size());
    }
    
    /**
     * add a whole lexicon to the end of this lexicon.
     */    
    public void append(TraceLexicon lx){
        for(int i=0;i<lx.size();i++)
            add((TraceWord)lx.get(i).clone());    
    }
    public int countCohorts(TraceWord w, int cnt){
        int result=0;
        if(w.getPhon().length()<cnt) return result;
        String qry = w.getPhon().substring(0,cnt);        
        if(qry==null) return result;
        java.util.Iterator iter = words.iterator();
        TraceWord nex;
        while(iter.hasNext()){
            nex = (TraceWord)iter.next();
            if(nex.getPhon().length()<cnt)
                continue;
            if(nex.getPhon().substring(0,cnt).equals(qry))
                result++;
        }
        return result;
    }
    
    /**
     * get i'th word
     */
    public TraceWord get(int i)
    {
        return (TraceWord)words.get(i);
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String d){
        description = d;
    }
    
    /**
     * get num words
     */
    public int size()
    {
        return words.size();
    }
    
    /**
     * reset
     */
    public void reset()
    {
        words.removeAllElements();
    }
    
    public int indexOf(String w){
        for(int i=0;i<words.size();i++)
            if(w.equals(((TraceWord)words.get(i)).getPhon()))
                return i;
        return -1;
    }
    /**
     *
     */
    public double getMeanWordLength(){
        double res=0;
        for(int i=0;i<words.size();i++)
            res+=((TraceWord)words.get(i)).getPhon().length();
        res/=size();
        return res;
    }
    
    /**
     * return the strings in an array
     */
    public String [] toStringArray()
    {
        String [] res = new String[words.size()];
        for (int i = 0; i < res.length; i++)
        {
            res[i] = new String(((TraceWord)words.get(i)).getPhon());
        }
        return res;
    }
    public String XMLTag(){
        String result="";
        result+="<lexicon>";
        if(description==null||description.equals(""))
            result += "<lexicon-name>null</lexicon-name>";
        else
            result += "<lexicon-name>"+description+"</lexicon-name>";
        for(int i=0;i<words.size();i++)
            result+=((TraceWord)words.get(i)).XMLTag();
        result+="</lexicon>";        
        return result;
    }   
    public String XMLTagNamespace(){
        String result="";
        result+="\n\n<lexicon xmlns=\'http://xml.netbeans.org/examples/targetNS\'"+
             "\n\t\txmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\'"+
             "\n\txsi:schemaLocation=\'http://xml.netbeans.org/examples/targetNS file:"+edu.uconn.psy.jtrace.UI.jTRACEMDI.properties.rootPath.getAbsolutePath()+"/Schema/WebTraceSchema.xsd\'>"+
             "\n";
        if(description==null||description.equals(""))
            result += "<lexicon-name>null</lexicon-name>";
        else
            result += "<lexicon-name>"+description+"</lexicon-name>";
        for(int i=0;i<words.size();i++)
            result+=((TraceWord)words.get(i)).XMLTag();
        result+="</lexicon>";        
        return result;
    }
    public edu.uconn.psy.jtrace.Model.Scripting.Boolean booleanValue(){return null;}
    public Predicate predicateValue(){ return null;}
    public Query queryValue(){ return null;}
    public FileLocator fileLocatorValue(){ return null;}
    public Text textValue(){ return null;}
    public Int intValue(){ return null;}
    public Decimal decimalValue(){ return null;}
    public ListOfPrimitives listValue(){return null;}
    public edu.uconn.psy.jtrace.Model.TraceLexicon lexiconValue(){ return this;}
    public edu.uconn.psy.jtrace.Model.TraceParam parametersValue(){ return null;}
    
    
//    public TraceLexicon(){words=new TraceWord[5000]; idx=0; }
//    public TraceLexicon(int _s){words=new TraceWord[_s]; idx=0;}
//    public void add(edu.uconn.psy.jtrace.Model.TraceWord wd){
//        if(idx==(words.length-1)){
//            System.out.println("Dictionary is full, returning.");
//            return;
//        }
//        words[idx]=wd;
//        idx++;
//        //System.out.println("\tadd word: "+wd.getPhon());
//    }
//    public edu.uconn.psy.jtrace.Model.TraceWord elementAt(int i){return words[i];}
//    public int size(){return idx;}
//    public void reset(){idx = 0;}
//    public edu.uconn.psy.jtrace.Model.TraceWord[] getWords(){return words;}
//    public void setWords(edu.uconn.psy.jtrace.Model.TraceWord[] _w){words=_w;}
//    public int getIdx(){return idx;}
//    public void setIdx(int _i){idx=_i;}
//    public TraceWord get(int i){
//        return words[i];
//    }
//    public String[] toStringArray(){
//        String[] res=new String[size()];
//        for(int i=0;i<res.length;i++){
//            res[i]=new String(words[i].getPhon());
//        }
//        return res;
//    }
//    public String DictionaryToString(){
//        if(words==null) return null;
//        if(words.length==0) return null;
//        else{
//            String result="";
//            for(int i=0;i<idx;i++){
//                result+=(words[i].getPhon())+",";
//            }
//            return result;
//        }
//    }
//    public String FrequenciesToString(){
//        if(words==null) return null;
//        if(words.length==0) return null;
//        else{
//            String result="";
//            for(int i=0;i<idx;i++){
//                result+=(words[i].getFrequency())+",";
//            }
//            return result;
//        }
//    }
    
}
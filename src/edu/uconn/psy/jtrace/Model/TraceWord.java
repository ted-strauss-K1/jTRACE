
package edu.uconn.psy.jtrace.Model;

import edu.uconn.psy.jtrace.Model.TracePhones;
import java.lang.ClassCastException;

/**
 * TraceWord defines a tuple for representing TRACE words. The string, of course,
 * plus frequency and prime information. TraceWords are
 * immutable, like Strings, and are valid if they contain only legal phones.
 *
 * @author Rafi Pelosoff
 * @author Ted Strauss
 * @author Harlan Harris
 */
public class TraceWord implements Comparable {
    private String phon;
    private String label;
    private double freq;
    private double prime;
    private boolean valid;
    
    public TraceWord()
    {
        phon="this is not a word";
        valid = false;
    }
    
    public TraceWord(String _p) {
        phon = _p.trim();
        freq= 0.0;
        //valid = TracePhones.validTraceWord(phon);
    }
    
    public TraceWord(String _p, double _f) {
        phon = _p.trim();
        freq = _f;
        //valid = TracePhones.validTraceWord(phon);
    }
    
    public TraceWord(String _p, double _f, String _lbl, double _prm) {
        phon = _p.trim();
        freq = _f;
        prime=_prm;
        label=_lbl;
        //valid = TracePhones.validTraceWord(phon);
    }
    public TraceWord(TraceWord old){
        phon = old.phon.trim();
        freq = old.freq;
        prime= old.prime;
        label= old.label;
        //valid = TracePhones.validTraceWord(phon);
    }
    
    public Object clone(){
        return new TraceWord(this);
    }
    /** 
     * For Comparable interface.
     */
    // WEIRD!
    public int compareTo(Object that) throws ClassCastException
    {
        if (that instanceof TraceWord)
            return this.phon.compareTo(((TraceWord)that).phon); 
        else
            throw new ClassCastException("expected TraceWord object");
    }
    
    public String getPhon(){return phon;} 
    public String getLabel(){return label;} 
    public double getFrequency(){return freq;}
    public double getPrime(){return prime;}
    public void setPhon(String _p){phon=_p;} 
    public void setLabel(String _l){label=_l;} 
    public void setFrequency(double _f){freq=_f;}
    public void setPrime(double _p){prime=_p;}
    
    public boolean getValid(){return valid;}
    public String XMLTag(){
        String result="";
        result+="<lexeme>";
        result+="<phonology>"+phon+"</phonology>";
        if(freq!=0) result+="<frequency>"+freq+"</frequency>";
        if(label!=null) result+="<label>"+label+"</label>";
        if(prime!=0) result+="<prime>"+prime+"</prime>";
        result+="</lexeme>";
        return result;
    }
    
    /**
     * Given a string, construct a new string with leading and training
     * dashes (if any) removed.
     */
    public static String stripDashes(String s)
    {
       return s.replaceAll("\\A-*(.*?)-*\\Z", "$1"); 
    }
        
}
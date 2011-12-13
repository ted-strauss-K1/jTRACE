/*
 * TraceParam.java
 *
 * Created on April 16, 2004, 5:26 PM
 */

package edu.uconn.psy.jtrace.Model;
import edu.uconn.psy.jtrace.Model.Dynamics.TraceFunctionList;
import java.util.*;

import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.parser.*;
import edu.uconn.psy.jtrace.parser.impl.*;
import edu.uconn.psy.jtrace.Model.Scripting.*;

//import edu.uconn.psy.jtrace.parser.impl.JtParameterTypeImpl.*;

/**
 *
 * @author  Rafi
 */
public class TraceParam implements Primitive{
    
    // parameters, with defaults
    private String user;
    private String dateTime;
    private String comment;    
    private String modelInput = "-^br^pt-";    
    private int spread[] = {6, 6, 6, 6, 6, 6, 6};
    private double spreadScale[] = {1, 1, 1, 1, 1, 1, 1 };  // silly -- only first element is used!
    private double min=-0.3;
    private double max = 1.0; 
    private int nreps = 1; //how many cycles should trace do in each step; 
        //nreps can seriously affect the rate at which perceptual affects seem to take effect
        //versus how recently the input was presented.  with a high nreps value, an input will
        //be presented but words may seem to take relatively long time to become active.
    private int slicesPerPhon = 3; //tracenet will put a new phoneme/word unit every slicesPerPhon fslices
    private int fSlices = 99; //number of feature slices
    private Decay decay; //rate at which F P W layers dacay.
    private Rest rest; //resting level at F P W layers.
    private Alpha alpha; //strength of excitatory connections between following layer pairs: IF FP PF PW WP 
    private Gamma gamma; //strength of inhibitory connections at F P W layers.
    //private Lambda lambda; //strength of alternate inhibitory connections at F P W layers.
    private TraceLexicon lexicon; //current lexicon.
    private TraceFunctionList functionList;
    private int deltaInput = 6; //input a new feature every deltaInput slices, similar to PEAKp(i) calculation
    //noise parameters 
    private double noiseSD = 0; //amount of input noise 
    private double stochasticitySD = 0; //amount of processing noise
    //attention, phoneme learning rate params (cf. Mirman et al. 2005)
    private double atten = 1.0;
    private double bias = 0;
    private double learningrate = 0; 
    // length normalization: binary
    private int lengthNormalization = 0;


    //lexical frequency parameters        
    private wordFrequency freqNode; //stores variables for three type of lexical frequency effects
    //priming parameters
    private wordPriming primeNode; //stores variables for three type of lexical priming
    //ambiguous phoneme continuum parameters.
    private String continuumSpec = ""; //three character mnemonic specify the current phoneme continuum.
        /*
         * char #1 = phoneme at which the continuum starts
         * char #2 = phoneme at which the continuum ends
         * char #3 = number of continuum steps created, including the two end-points
         */
    private TracePhones phonology; 
    
    // other, non parameter variables
    //private int nwords = 0;
    //private String phonemeLabels[]={"/p/", "/b/", "/t/", "/d/", "/k/", "/g/", "/s/", "/S/", "/r/", "/l/", "/a/", "/i/", "/u/", "/^/", "/-/"};     
    private int legalPhonemeChars[] = {'p', 'b', 't', 'd', 'k', 'g', 's', 'S', 'r', 'l', 'a', 'i', 'u', '^', '-'};
    private int maxSpread; //max(spread[])
    private long updateCt = 0; //bookkeeping variable monitoring whether parameter have been changed from GUI panels.
    
    
    // structures for main parameters
    public class Alpha
    {
        public double IF = 1.0, FP=0.02, PW=0.05, WP=0.03, PF=0.00;
        
        public Alpha() {} // normal constructor
        public Alpha(Alpha fa) {IF = fa.IF; FP = fa.FP; PW = fa.PW; WP=fa.WP; PF=fa.PF;} // copy constructor
        
        public void reset()
        {
            IF = 1.0;
            FP = 0.02;
            PW = 0.05;
            WP = 0.03;
            PF = 0.0;
        }
        public boolean equals(Alpha a){
            if(IF==a.IF&&FP==a.FP&&PW==a.PW&&WP==a.WP&&PF==a.PF){
                return true;
            }
            else{
                return false;
            }
        }
    }
    
    public class Gamma
    {
        public double F = 0.04, P=0.04, W=0.03;
        
        public Gamma() {} // normal constructor
        public Gamma(Gamma fg) {F = fg.F; P = fg.P; W = fg.W;} // copy constructor
        
        public void reset()
        {
            F = 0.04;
            P = 0.04;
            W = 0.03;
        }
        public boolean equals(Gamma g){
            if(F==g.F&&P==g.P&&W==g.W){
                return true;
            }
            else {
                return false;
            }
        }
    }
    /*public class Lambda
    {
        public double F = 0.00, P=0.00, W=0.00;
        
        public Lambda() {} // normal constructor
        public Lambda(Lambda fg) {F = fg.F; P = fg.P; W = fg.W;} // copy constructor
        
        public void reset()
        {
            F = 0.00;
            P = 0.00;
            W = 0.00;
        }
    }*/
    
    public class Decay
    {
        public double F=0.01, P=0.03, W=0.05;
        
        public Decay() {} // normal constructor
        public Decay(Decay fd) {F = fd.F; P = fd.P; W = fd.W;} // copy constructor
        
        public void reset()
        {
            F = 0.01;
            P = 0.03;
            W = 0.05;
        }
        public boolean equals(Decay d){
            if(F==d.F&&P==d.P&&W==d.W){
                return true;
            }
            else {
                return false;
            }
        }
    }
    
    public class Rest
    {
        public double F =-.1, P =-.1 , W =-.01;
        
        public Rest() {} // normal constructor
        public Rest(Rest fr) {F = fr.F; P = fr.P; W = fr.W;} // copy constructor
        
        public void reset()
        {
            F = -.1;
            P = -.1;
            W = -.01;
        }
        public boolean equals(Rest r){
            if(F==r.F&&P==r.P&&W==r.W){
                return true;
            }
            else{
                return false;
            }
        }
    }
    
    //Acts as interface for calling up different hypothetical frequency effects
    //that may be applied at different points during a TRACE simulation.
    //Also may act as an interface between the TRACE model and lexicon if the 
    //lexical model requires such interactions.  For example, if an instance of
    //lexical access by the model led to a modification of a lexical representation.
    public class wordFrequency {

        //settings from Dahan et al. 2001; Original values are commented on right.
        /*
         * resting level frequency effect :
         *  resting level of word units are boosted accroding to their freq values.
         * ph->wd connection frequency effect :
         *  phoneme-to-word transmission is scaled by frequency values.
         * post-perceptual frequenct effect :
         *  LCR analysis calculation scales word responce probabilities by freq values.
        */
        public boolean RDL_rest;  // true (on)
        public double RDL_rest_s; // = 0.06d;
        public double RDL_rest_c; // = 1.0d;
        public boolean RDL_wt;  // true (on)
        public double RDL_wt_s; // = 0.13d;
        public double RDL_wt_c; // = 1.0d;
        public boolean RDL_post;  // true (on)
        //public double RDL_post_s; //purposely left out of specification
        public double RDL_post_c; // = 15.0d;

        /** Creates a new instance of wordFrequency */
        public wordFrequency() {
            reset();
        }
        public wordFrequency(wordFrequency fromwf) // copy constructor
        {
            RDL_rest = fromwf.RDL_rest;
            RDL_rest_s = fromwf.RDL_rest_s;
            RDL_rest_c = fromwf.RDL_rest_c;
            RDL_wt = fromwf.RDL_wt;
            RDL_wt_s = fromwf.RDL_wt_s;
            RDL_wt_c = fromwf.RDL_wt_c;
            RDL_post = fromwf.RDL_post;
            RDL_post_c = fromwf.RDL_post_c;
        }
        
        public void reset(){
            RDL_rest=false; 
            RDL_rest_s=0.0; 
            RDL_rest_c=1.0; 
            RDL_wt=false; 
            RDL_wt_s=0.0; 
            RDL_wt_c=1.0;
            RDL_post=false;
            RDL_post_c=0.0; 
        }
        public boolean equals(wordFrequency f){
            if(
                RDL_rest==f.RDL_rest&&
                RDL_rest_s==f.RDL_rest_s&&
                RDL_rest_c==f.RDL_rest_c&&
                RDL_wt==f.RDL_wt&&
                RDL_wt_s==f.RDL_wt_s&&
                RDL_wt_c==f.RDL_wt_c&&
                RDL_post==f.RDL_post&&
                RDL_post_c==f.RDL_post_c
                ){
                return true;
            }
            else{
                return false;
            }
        }
        public double applyRestFreqScaling(edu.uconn.psy.jtrace.Model.TraceWord word){
            if(RDL_rest_s != 0) RDL_rest = true;
            
            if(word==null) return 0.0;
            //System.out.print("."+word.getFrequency());
            if(RDL_rest) return (RDL_rest_s * (Math.log(RDL_rest_c + word.getFrequency())* 0.434294482));
            else return 0.0d;
        }
        //From Dahan et al 2001:  a_pi =   a_pi[ 1 +  (a_pi * s[log10(c   + f_i)])]
        //From JSM's code: 
        //  *wnexptr++ += (wp->wfrq * *wtptr++*t) + (*wtptr++*t);	  
        //  wp->wfrq = fscale * (log(.0 + wordfreq[i]));	    
        // AND
        /*
         for ( pnexptr = &pp->nex[pmin], wtptr = &pp->wpw[pwin]; pnexptr < pmaxptr;) {
	    *pnexptr++ += (1 + wp->wfrq) * (*wtptr++)*(*wpexptr);
	  }
         **/
        public double applyWeightFreqScaling(edu.uconn.psy.jtrace.Model.TraceWord word_i, double act_pi){
            if(RDL_wt_s != 0) RDL_wt = true;
            
            if(word_i==null) return act_pi;
            if(RDL_wt){
                //TODO!
                //NEW VERSION BASED ON JIM'S CODE:
                double wfrq = RDL_wt_s  * (Math.log(0 + word_i.getFrequency()) * 0.434294482);
                double result = (wfrq * act_pi) + act_pi;
                //HOW I ORIGINALLY DID IT, BASED ON THE PAPER:
                //double result=(act_pi * (1.0d + (act_pi * RDL_wt_s * (Math.log(RDL_wt_c + word_i.getFrequency()) * 0.434294482))));
                return result;                
            }
            else return act_pi;       
        }
        //S_i =  SWP_i =  e^(k*a_i) * [log 10( c +  f_i )], and we are passing in the exponentiated term.                    
        public double applyPostActivationFreqScaling(edu.uconn.psy.jtrace.Model.TraceWord word_i, double exp_act_i){
            if(RDL_post_c != 0) RDL_post = true;
            if(word_i==null) return exp_act_i;
            //System.out.print(","+word_i.getFrequency());
            if(RDL_post) return exp_act_i * (Math.log(RDL_post_c + word_i.getFrequency()) * 0.434294482);
            else return exp_act_i;
        }
        public String toString(){
            String result="word frequency modeling parameters:\n";
            result+="RDL_rest "+RDL_rest+" "+RDL_rest_s+" "+RDL_rest_c+"\n";
            result+="RDL_wt "+RDL_wt+" "+RDL_wt_s+" "+RDL_wt_c+"\n";
            result+="RDL_post "+RDL_post+" "+RDL_post_c+"\n";
            return result;
        }
    }   
    
    //Acts as interface for calling up different hypothetical priming effects
    public class wordPriming {

        //settings from Dahan et al. 2001; Original values are commented on right.
        
        public boolean RDL_rest;  // true (on)
        public double RDL_rest_s; // = 0.06d;
        public double RDL_rest_c; // = 1.0d;
        public boolean RDL_wt;  // true (on)
        public double RDL_wt_s; // = 0.13d;
        public double RDL_wt_c; // = 1.0d;
        public boolean RDL_post;  // true (on)
        //public double RDL_post_s; //purposely left out of specification
        public double RDL_post_c; // = 15.0d;

        /** Creates a new instance of wordFrequency */
        public wordPriming() {
            reset();
        }
        public wordPriming(wordPriming fromwf) // copy constructor
        {
            RDL_rest = fromwf.RDL_rest;
            RDL_rest_s = fromwf.RDL_rest_s;
            RDL_rest_c = fromwf.RDL_rest_c;
            RDL_wt = fromwf.RDL_wt;
            RDL_wt_s = fromwf.RDL_wt_s;
            RDL_wt_c = fromwf.RDL_wt_c;
            RDL_post = fromwf.RDL_post;
            RDL_post_c = fromwf.RDL_post_c;
        }
        
        public void reset(){
            RDL_rest=false; 
            RDL_rest_s=0.0; 
            RDL_rest_c=1.0; 
            RDL_wt=false; 
            RDL_wt_s=0.0; 
            RDL_wt_c=1.0;
            RDL_post=false;
            RDL_post_c=0.0; 
        }
        public boolean equals(wordPriming p){
            if(
                RDL_rest==p.RDL_rest&&
                RDL_rest_s==p.RDL_rest_s&&
                RDL_rest_c==p.RDL_rest_c&&
                RDL_wt==p.RDL_wt&&
                RDL_wt_s==p.RDL_wt_s&&
                RDL_wt_c==p.RDL_wt_c&&
                RDL_post==p.RDL_post&&
                RDL_post_c==p.RDL_post_c
                ){
                return true;
            }
            else{
                return false;
            }
        }
        public double applyRestPrimeScaling(edu.uconn.psy.jtrace.Model.TraceWord word){
            if(RDL_rest_s != 0) RDL_rest = true;
            
            if(word==null) return 0.0;
            //System.out.print("."+word.getFrequency());
            if(RDL_rest) return (RDL_rest_s * (Math.log(RDL_rest_c + word.getPrime())* 0.434294482));
            else return 0.0d;
        }
        public double applyWeightPrimeScaling(edu.uconn.psy.jtrace.Model.TraceWord word_i, double act_pi){
            if(RDL_wt_s != 0) RDL_wt = true;
            
            if(word_i==null) return act_pi;
            if(RDL_wt){
                double wfrq = RDL_wt_s  * (Math.log(0 + word_i.getPrime()) * 0.434294482);
                double result = (wfrq * act_pi) + act_pi;
                return result;                
            }
            else return act_pi;       
        }
        public double applyPostActivationPrimeScaling(edu.uconn.psy.jtrace.Model.TraceWord word_i, double exp_act_i){
            if(RDL_post_c != 0) RDL_post = true;
            if(word_i==null) return exp_act_i;
            if(RDL_post) return exp_act_i * (Math.log(RDL_post_c + word_i.getPrime()) * 0.434294482);
            else return exp_act_i;
        }        
    }  
    
    /** Creates a new instance of TraceParam with defaults */
    public TraceParam() {
        freqNode = new wordFrequency();
        primeNode = new wordPriming();
        
        alpha = new Alpha();
        decay = new Decay();
        rest = new Rest();
        gamma = new Gamma();
        //lambda = new Lambda();
        
        phonology = new TracePhones();
        lexicon = new TraceLexicon();
        functionList = new TraceFunctionList();
        loadDefaultlexicon();
        //nwords = lexicon.size();
        
        //calc max spread
        maxSpread = -1;
        for(int s = 0; s < spread.length; s++)
            if(spread[s] > maxSpread)
                maxSpread = spread[s];
            
    }
    
    /**
     * copy constructor
     */
    public TraceParam(TraceParam fromp)
    {        
        phonology = fromp.getPhonology().clone();
        
        if (fromp == null)
         {
             System.out.println("TraceParam copy constructor from null!");
             return;
         }
         if (fromp.user != null)
            user = new String(fromp.user);
         if (fromp.dateTime != null)
            dateTime = new String(fromp.dateTime);
         if (fromp.comment != null)
            comment = new String(fromp.comment);
         if (fromp.modelInput != null)
            modelInput = new String(fromp.modelInput);
        
        for (int i = 0; i < spread.length; i++)
        {
            spread[i] = fromp.spread[i];
            spreadScale[i] = fromp.spreadScale[i];
        }
        
        min = fromp.min;
        max = fromp.max;
        
        nreps = fromp.nreps;
        slicesPerPhon = fromp.slicesPerPhon;
        lengthNormalization = fromp.lengthNormalization;
        fSlices = fromp.fSlices;
        atten = fromp.atten;
        bias = fromp.bias;
        learningrate = fromp.learningrate;
        
        decay = new Decay(fromp.decay);
        rest = new Rest(fromp.rest);
        alpha = new Alpha(fromp.alpha);
        gamma = new Gamma(fromp.gamma);
        //lambda = new Lambda(fromp.lambda);
        
        lexicon = new TraceLexicon(fromp.lexicon);
        functionList = new TraceFunctionList();
        
        deltaInput = fromp.deltaInput;
        noiseSD = fromp.noiseSD;
        stochasticitySD = fromp.stochasticitySD;
        freqNode = new wordFrequency(fromp.freqNode);
        primeNode = new wordPriming(fromp.primeNode);
        if (fromp.continuumSpec != null){
            continuumSpec = new String(fromp.continuumSpec);
            phonology.compileAll();
        }
        //calc max spread
        maxSpread = -1;
        for(int s = 0; s < spread.length; s++)
            if(spread[s] > maxSpread)
                maxSpread = spread[s];
    }
    /**
     *Check the input string against the list of phonemes, 
     *changing the input if necessary.
     */
    public boolean verifyModelInput(){
        if(modelInput==null||modelInput=="")
            return false;
        boolean somethingHasBeenChanged=false;
        String[] phones = phonology.getLabels();
        String replacer="";
        for(int i=0;i<phones.length;i++)
            if(phones[i]=="-"){
                replacer="-";
                break;
            }        
        lp1:for(int i=0;i<modelInput.length();i++){            
            if(modelInput.charAt(i)=='{'||modelInput.charAt(i)=='}'||modelInput.charAt(i)=='?')
                continue lp1;
            if(Character.isDigit(modelInput.charAt(i)))
                continue lp1;
            for(int j=0;j<phones.length;j++){
                //System.out.println(modelInput.substring(i,(i+1))+"?="+phones[j]+"\t\t"+modelInput.substring(i,(i+1)).getClass()+"?="+phones[j].getClass());
                if(modelInput.substring(i,(i+1)).equals(phones[j])){
                    //the input phone found a match, so onto the next one.
                    continue lp1;
                }
            }
            //the input phone did not find a match, so replace it.
            char[] mi = modelInput.toCharArray();
            mi[i]=replacer.charAt(0);
            modelInput = new String(mi);
            somethingHasBeenChanged=true;
        }
        //System.out.println("modelInput after verification: "+modelInput);
        return somethingHasBeenChanged;
    }
    
    /**
     *Check the lexicon against the list of phonemes, 
     *changing the words if necessary.
     */
    public boolean verifyLexicon(){
        boolean somethingHasBeenChanged=false;
        
        return somethingHasBeenChanged;
    }
    /**
     *replace an old phone symbol with a new phone symbol in the
     *model input and in every word of the lexicon.
     */
    public void updatePhonChanged(String _old, String _new){        
        setModelInput(modelInput.replace(_old.charAt(0),_new.charAt(0)));
        //
        String curr;
        for(int i=0;i<lexicon.size();i++){
            curr=lexicon.get(i).getPhon();
            lexicon.get(i).setPhon(curr.replace(_old.charAt(0),_new.charAt(0)));
        }        
    }
    public void updatePhonDeleted(String _old){
        setModelInput(modelInput.replaceAll(_old,""));        
        //
        String curr;
        for(int i=0;i<lexicon.size();i++){
            curr=lexicon.get(i).getPhon();
            lexicon.get(i).setPhon(curr.replaceAll(_old,""));
        }
    }
    public int inputLength(){
        int len=0;
        boolean inSplice=false;
        for(int i=0;i<modelInput.length();i++){
            if(modelInput.charAt(i)=='{'){
                inSplice=true;
            }
            else if(modelInput.charAt(i)=='}'){
                if(inSplice==false) System.out.println("TraceParam.inputLength(): Something's wrong with the input syntax.");
                inSplice=false;
                len++;
            }
            else if(inSplice==true){
                continue;
            }
            else{
                len++;
            }
        }
        return len;
    }
    
    // these are not parameters, per se
    public String[] getPhonemeLabels(){return phonology.getLabels();}
    public int getNWords() {return lexicon.size();}
    public int getMaxSpread() {return maxSpread;}
    public int getPSlices() {return fSlices/slicesPerPhon;}
    public void incrUpdateCt() {updateCt++;}
    public long getUpdateCt() {return updateCt;}
    
    
    // parameter access methods
    public TracePhones getPhonology(){ return phonology;} 
    public void setPhonology(TracePhones _phonology){
        phonology=_phonology; 
        phonology.compileAll();
        updateCt++;
    }
    public String getUser() {return user;}
    public void setUser(String user) {this.user = user; updateCt++;}
    
    public int getFSlices() {return fSlices;}
    public void setFSlices(int f){this.fSlices=f;updateCt++;}
    
    public String getComment(){return comment;}
    public void setComment(String c){this.comment = c;updateCt++;}
    
    public String getDateTime(){return dateTime;}
    public void setDateTime(String dt){this.dateTime = dt;updateCt++;}
    
    public String getContinuumSpec(){return continuumSpec;}
    public boolean setContinuumSpec(String spec)
    {
        continuumSpec = spec;
        // check if it's OK, and return false if it's not
        if (spec.length() == 3)
        {
            char from = spec.charAt(0);
            char to = spec.charAt(1);
            int steps = (new Integer((new Character(spec.charAt(2))).toString())).intValue(); // - '0' - 1;  // off-by-one to convert to index, hopefully
            
            boolean fromLegal = true;
            boolean toLegal = true;
            /*for (int i = 0; i < legalPhonemeChars.length; i++)
            {
                if (legalPhonemeChars[i] == from)
                    fromLegal = true;
                if (legalPhonemeChars[i] == to)
                    toLegal = true;
            }*/
            
            //if (fromLegal && toLegal && (steps >= 0 && steps <= 9))
            //{
                continuumSpec = spec;
                try{
                    phonology.makePhonemeContinuum(from,to,steps);
                }
                catch(TraceException te)
                {
                    te.printStackTrace();
                }
                updateCt++;
                return true;
            //}
        }
        // fell through -- bogus spec!
        return false;
    }
    
    public void setModelInput(String i){
        modelInput=i;
        updateCt++;        
    }
    public String getModelInput(){
        return modelInput;
    }
    
    public int getNReps() {return nreps;}
    public void setNReps(int nr){nreps = nr;updateCt++;}
    
    public int[] getSpread() {return spread;}
    public boolean setSpread(int [] sp)
    {
        // sanity check first
        if (sp.length == spread.length)
        {
            spread = sp;
            updateCt++;
            return true;
        }
        else
            return false;
    }
    
    public double getMin() {return min;}
    public void setMin(double m){min = m;updateCt++;}
    
    public double getMax() {return max;}
    public void setMax(double m){max = m;updateCt++;}
    
    public Rest getRest() {return rest;}
    public void setRest(Rest r){rest = r;updateCt++;}
    
    public double[] getSpreadScale() {return spreadScale;}
    public boolean setSpreadScale(double [] ss)
    {
        // sanity check
        if (ss.length == spreadScale.length)
        {
            spreadScale = ss;
            updateCt++;
            return true;
        }
        else
            return false;
    }
    public boolean setSpreadScale(double ss)    // non-array version
    {
        for (int i = 0; i < spreadScale.length; i++)
            spreadScale[i] = ss;
        updateCt++;
        return true;
    }
    
    public int getSlicesPerPhon(){return slicesPerPhon;}
    public void setSlicesPerPhon(int spp){slicesPerPhon = spp;updateCt++;}
    
    public Decay getDecay() {return decay;}
    public Alpha getAlpha() {return alpha;}
    public Gamma getGamma() {return gamma;}
    //public Lambda getLambda() {return lambda;}
    public void setDecay(Decay d) {decay = d;updateCt++;}
    public void setAlpha(Alpha a) {alpha = a;updateCt++;}
    public void setGamma(Gamma g) {gamma = g;updateCt++;}
    //public void setLambda(Lambda l) {lambda = l; updateCt++;}
    
    public edu.uconn.psy.jtrace.Model.TraceLexicon getLexicon() {return lexicon;}
    public void setLexicon(edu.uconn.psy.jtrace.Model.TraceLexicon _d){lexicon=_d; updateCt++;}
    
    public int getDeltaInput() {return deltaInput;}
    public void setDeltaInput(int di) {deltaInput = di;updateCt++;}
    
    public double getNoiseSD(){return noiseSD;}
    public void setNoiseSD(double nsd) {noiseSD = nsd;updateCt++;}
    
    public double getStochasticitySD(){return stochasticitySD;}
    public void setStochasticitySD(double ssd) {stochasticitySD = ssd;updateCt++;}
    
    public double getAtten(){return atten;}
    public void setAtten(double at) {atten = at; updateCt++;}
    
    public double getBias(){return bias;}
    public void setBias(double bs) {bias = bs;updateCt++;}
    
    public double getLearningRate(){return learningrate;}
    public void setLearningRate(double lr) {learningrate = lr;updateCt++;}
    
    public int getLengthNormalization(){return lengthNormalization;}
    public void setLengthNormalization(int ln) {lengthNormalization = ln; updateCt++;}    
    
    //public double get(){return ;}
    //public void set(double ) {= ;updateCt++;}
    
    public edu.uconn.psy.jtrace.Model.TraceParam.wordFrequency getFreqNode(){return freqNode;}
    public void setFreqNode(edu.uconn.psy.jtrace.Model.TraceParam.wordFrequency fn) {freqNode = fn;updateCt++;}
    
    public edu.uconn.psy.jtrace.Model.TraceParam.wordPriming getPrimeNode(){return primeNode;}
    public void setPrimeNode(edu.uconn.psy.jtrace.Model.TraceParam.wordPriming pn) {primeNode = pn;updateCt++;}
    
    public edu.uconn.psy.jtrace.Model.Dynamics.TraceFunctionList getFunctionList(){ return functionList;}
    public void setFunctionList(edu.uconn.psy.jtrace.Model.Dynamics.TraceFunctionList fl){ functionList=fl;}
    
    public void preventUpdateCt(){ updateCt--;}
    
    // end parameter access methods
    
    public double clipWeight(double weight) {
        if(weight > max)
            weight = max;
        else if(weight < min)
            weight = min;
        return weight;
    }
    
    private void loadDefaultlexicon()
    {
        // default lexicon is first 20 words of slex, plus silence
        
        lexicon.reset();
        //try to load slex from file, otherwise load a small 20-item lexicon.
        try{
            java.io.File slex = new java.io.File(edu.uconn.psy.jtrace.UI.traceProperties.rootPath.getAbsolutePath()+"/lexicons","slex.jt");
            
            // try to read it
            edu.uconn.psy.jtrace.IO.WTFileReader fileReader = new edu.uconn.psy.jtrace.IO.WTFileReader(slex);
            
            if (!fileReader.validateLexiconFile()){             
                throw new Exception("Validation of lexicon file failed.");
            }
            
            // load it into our parameters
            lexicon = fileReader.loadJTLexicon();
            return;
        }        
        catch(Exception e){
            //e.printStackTrace();
        }
        
        lexicon.reset();
        lexicon.add(new TraceWord("^br^pt"));
        lexicon.add(new TraceWord("^dapt"));
        lexicon.add(new TraceWord("^d^lt"));
        lexicon.add(new TraceWord("^gri"));
        lexicon.add(new TraceWord("^lat"));
        lexicon.add(new TraceWord("^part"));
        lexicon.add(new TraceWord("^pil"));
        lexicon.add(new TraceWord("ark"));
        lexicon.add(new TraceWord("ar"));
        lexicon.add(new TraceWord("art"));
        lexicon.add(new TraceWord("art^st"));
        lexicon.add(new TraceWord("^slip"));
        lexicon.add(new TraceWord("bar"));
        lexicon.add(new TraceWord("bark"));
        lexicon.add(new TraceWord("bi"));
        lexicon.add(new TraceWord("bit"));
        lexicon.add(new TraceWord("bist"));
        lexicon.add(new TraceWord("blak"));
        lexicon.add(new TraceWord("bl^d"));
        lexicon.add(new TraceWord("blu"));

        lexicon.add(new TraceWord("-"));
        updateCt++;
        //nwords = lexicon.size();
    }
    
     public void resetToDefaults() {
        user = new String(" ");
        dateTime = new String(" ");
        comment = new String(" ");
         
        modelInput = "-^br^pt-";    
        
        for (int i = 0; i < spread.length; i++)
        {
            spread[i] = 6;
            spreadScale[i] = 1;
        }
        
        min=-0.3;
        max = 1.0; 
        
        nreps = 1; //how many cycles should trace cycle in one step
        slicesPerPhon = 3; //tracenet will put a new detector every slicesPerPhon slices
        fSlices = 99; //number of feature slices
        
        decay.reset();
        rest.reset();
        alpha.reset();
        gamma.reset();
        //lambda.reset();
        
        lexicon.reset();
        loadDefaultlexicon();
        
        deltaInput = 6; 
        noiseSD = 0;
        stochasticitySD = 0;
        freqNode.reset();
        continuumSpec = "";
        updateCt++;
    }

    public edu.uconn.psy.jtrace.Model.Scripting.Boolean booleanValue(){return null;}
    public Predicate predicateValue(){ return null;}
    public Query queryValue(){ return null;}
    public FileLocator fileLocatorValue(){ return null;}
    public Text textValue(){ return null;}
    public Int intValue(){ return null;}
    public Decimal decimalValue(){ return null;}
    public ListOfPrimitives listValue(){return null;}
    public edu.uconn.psy.jtrace.Model.TraceLexicon lexiconValue(){ return null;}
    public edu.uconn.psy.jtrace.Model.TraceParam parametersValue(){ return this;}    
             
    
    public Object clone(){
        return new TraceParam(this);
    }
         
     public String XMLTag(){
        String result="";
        try{
            //build up the XML with strings.
            //add xml tags using param information.
            result+="<parameters>";
            result+="<StringParam><name>modelInput</name><StringValue>"+modelInput+"</StringValue></StringParam>";
            result+=lexicon.XMLTag();
            result+=phonology.XMLTag();
            result+="<DecimalParam><name>alpha.if</name><DecimalValue>"+alpha.IF+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>alpha.fp</name><DecimalValue>"+alpha.FP+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>alpha.pw</name><DecimalValue>"+alpha.PW+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>alpha.pf</name><DecimalValue>"+alpha.PF+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>alpha.wp</name><DecimalValue>"+alpha.WP+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>decay.f</name><DecimalValue>"+decay.F+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>decay.p</name><DecimalValue>"+decay.P+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>decay.w</name><DecimalValue>"+decay.W+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>gamma.f</name><DecimalValue>"+gamma.F+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>gamma.p</name><DecimalValue>"+gamma.P+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>gamma.w</name><DecimalValue>"+gamma.W+"</DecimalValue></DecimalParam>";             
            //result+="<DecimalParam><name>lambda.f</name><DecimalValue>"+lambda.F+"</DecimalValue></DecimalParam>";
            //result+="<DecimalParam><name>lambda.p</name><DecimalValue>"+lambda.P+"</DecimalValue></DecimalParam>";
            //result+="<DecimalParam><name>lambda.w</name><DecimalValue>"+lambda.W+"</DecimalValue></DecimalParam>";             
            result+="<DecimalParam><name>min</name><DecimalValue>"+min+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>max</name><DecimalValue>"+max+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>rest.f</name><DecimalValue>"+rest.F+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>rest.p</name><DecimalValue>"+rest.P+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>rest.w</name><DecimalValue>"+rest.W+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>atten</name><DecimalValue>"+atten+"</DecimalValue></DecimalParam>";            
            result+="<DecimalParam><name>bias</name><DecimalValue>"+bias+"</DecimalValue></DecimalParam>";            
            result+="<DecimalParam><name>learningRate</name><DecimalValue>"+learningrate+"</DecimalValue></DecimalParam>";            
            result+="<DecimalParam><name>noiseSD</name><DecimalValue>"+noiseSD+"</DecimalValue></DecimalParam>";            
            result+="<DecimalParam><name>stochasticity</name><DecimalValue>"+stochasticitySD+"</DecimalValue></DecimalParam>";            
            
            result+="<DecimalParam><name>resting_frq_scale</name><DecimalValue>"+freqNode.RDL_rest_s+"</DecimalValue></DecimalParam>";            
            result+="<DecimalParam><name>resting_frq_constant</name><DecimalValue>"+freqNode.RDL_rest_c+"</DecimalValue></DecimalParam>";            
            result+="<StringParam><name>resting_frq_on</name><StringValue>"+freqNode.RDL_rest+"</StringValue></StringParam>";            
            result+="<DecimalParam><name>weight_frq_scale</name><DecimalValue>"+freqNode.RDL_wt_s+"</DecimalValue></DecimalParam>";            
            result+="<DecimalParam><name>weight_frq_constant</name><DecimalValue>"+freqNode.RDL_wt_c+"</DecimalValue></DecimalParam>";            
            result+="<StringParam><name>weight_frq_on</name><StringValue>"+freqNode.RDL_wt+"</StringValue></StringParam>";            
            result+="<DecimalParam><name>post_frq_constant</name><DecimalValue>"+freqNode.RDL_post_c+"</DecimalValue></DecimalParam>";            
            result+="<StringParam><name>post_frq_on</name><StringValue>"+freqNode.RDL_post+"</StringValue></StringParam>";            
            
            result+="<DecimalParam><name>resting_prim_scale</name><DecimalValue>"+freqNode.RDL_rest_s+"</DecimalValue></DecimalParam>";            
            result+="<DecimalParam><name>resting_prim_constant</name><DecimalValue>"+freqNode.RDL_rest_c+"</DecimalValue></DecimalParam>";            
            result+="<StringParam><name>resting_prim_on</name><StringValue>"+freqNode.RDL_rest+"</StringValue></StringParam>";            
            result+="<DecimalParam><name>weight_prim_scale</name><DecimalValue>"+freqNode.RDL_wt_s+"</DecimalValue></DecimalParam>";            
            result+="<DecimalParam><name>weight_prim_constant</name><DecimalValue>"+freqNode.RDL_wt_c+"</DecimalValue></DecimalParam>";            
            result+="<StringParam><name>weight_prim_on</name><StringValue>"+freqNode.RDL_wt+"</StringValue></StringParam>";            
            result+="<DecimalParam><name>post_prim_constant</name><DecimalValue>"+freqNode.RDL_post_c+"</DecimalValue></DecimalParam>";            
            result+="<StringParam><name>post_prim_on</name><StringValue>"+freqNode.RDL_post+"</StringValue></StringParam>";            
            
            result+="<IntParam><name>fSlices</name><IntValue>"+fSlices+"</IntValue></IntParam>";
            result+="<IntParam><name>deltaSlices</name><IntValue>"+deltaInput+"</IntValue></IntParam>";            
            result+="<IntParam><name>nreps</name><IntValue>"+nreps+"</IntValue></IntParam>";
            result+="<IntParam><name>slicesPerPhon</name><IntValue>"+slicesPerPhon+"</IntValue></IntParam>";
            result+="<IntParam><name>lengthNormalization</name><IntValue>"+lengthNormalization+"</IntValue></IntParam>";
            
            result+="<IntParamRep><name>"+"FETSPREAD"+"</name>";
            for(int i=0;i<spread.length;i++)
                result+="<IntValue>"+spread[i]+"</IntValue>";
            result+="</IntParamRep>";
            
            result+="<DecimalParamRep><name>"+"spreadScale"+"</name>";
            for(int i=0;i<spreadScale.length;i++)
                result+="<DecimalValue>"+spreadScale[i]+"</DecimalValue>";
            result+="</DecimalParamRep>";
            
            if(continuumSpec.equals(""))
                result+="<StringParam><name>continuumSpec</name><StringValue>null</StringValue></StringParam>";
            else
                result+="<StringParam><name>continuumSpec</name><StringValue>"+continuumSpec+"</StringValue></StringParam>";
            result+="<StringParam><name>user</name><StringValue>"+user+"</StringValue></StringParam>";
            result+="<StringParam><name>dateTime</name><StringValue>"+dateTime+"</StringValue></StringParam>";
            result+="<StringParam><name>comment</name><StringValue>"+comment+"</StringValue></StringParam>";            
            result+="</parameters>";
            
        }
        catch( Exception e ) {e.printStackTrace(); return "Exception";}
    
        return result;
    }    
    public String XMLTagNamespace(){
        String result="";
        try{
            result+="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
                "<parameters xmlns=\'http://xml.netbeans.org/examples/targetNS\'"+
                "\nxmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\'"+
                "\nxsi:schemaLocation=\'http://xml.netbeans.org/examples/targetNS file:"+edu.uconn.psy.jtrace.UI.jTRACEMDI.properties.rootPath.getAbsolutePath()+"/Schema/jTRACESchema.xsd\'>";
        
            //build up the XML with strings.
            //add xml tags using param information.
            result+="<StringParam><name>modelInput</name><StringValue>"+modelInput+"</StringValue></StringParam>";
            result+=lexicon.XMLTag();
            
            result+="<DecimalParam><name>alpha.if</name><DecimalValue>"+alpha.IF+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>alpha.fp</name><DecimalValue>"+alpha.FP+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>alpha.pw</name><DecimalValue>"+alpha.PW+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>alpha.pf</name><DecimalValue>"+alpha.PF+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>alpha.wp</name><DecimalValue>"+alpha.WP+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>decay.f</name><DecimalValue>"+decay.F+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>decay.p</name><DecimalValue>"+decay.P+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>decay.w</name><DecimalValue>"+decay.W+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>gamma.f</name><DecimalValue>"+gamma.F+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>gamma.p</name><DecimalValue>"+gamma.P+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>gamma.w</name><DecimalValue>"+gamma.W+"</DecimalValue></DecimalParam>";             
            //result+="<DecimalParam><name>lambda.f</name><DecimalValue>"+lambda.F+"</DecimalValue></DecimalParam>";
            //result+="<DecimalParam><name>lambda.p</name><DecimalValue>"+lambda.P+"</DecimalValue></DecimalParam>";
            //result+="<DecimalParam><name>lambda.w</name><DecimalValue>"+lambda.W+"</DecimalValue></DecimalParam>";             
            result+="<DecimalParam><name>min</name><DecimalValue>"+min+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>max</name><DecimalValue>"+max+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>rest.f</name><DecimalValue>"+rest.F+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>rest.p</name><DecimalValue>"+rest.P+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>rest.w</name><DecimalValue>"+rest.W+"</DecimalValue></DecimalParam>";
            result+="<DecimalParam><name>noiseSD</name><DecimalValue>"+noiseSD+"</DecimalValue></DecimalParam>";            
            result+="<DecimalParam><name>stochasticity</name><DecimalValue>"+stochasticitySD+"</DecimalValue></DecimalParam>";            
            
            result+="<DecimalParam><name>resting_frq_scale</name><DecimalValue>"+freqNode.RDL_rest_s+"</DecimalValue></DecimalParam>";            
            result+="<DecimalParam><name>resting_frq_constant</name><DecimalValue>"+freqNode.RDL_rest_c+"</DecimalValue></DecimalParam>";            
            result+="<StringParam><name>resting_frq_on</name><StringValue>"+freqNode.RDL_rest+"</StringValue></StringParam>";            
            result+="<DecimalParam><name>weight_frq_scale</name><DecimalValue>"+freqNode.RDL_wt_s+"</DecimalValue></DecimalParam>";            
            result+="<DecimalParam><name>weight_frq_constant</name><DecimalValue>"+freqNode.RDL_wt_c+"</DecimalValue></DecimalParam>";            
            result+="<StringParam><name>weight_frq_on</name><StringValue>"+freqNode.RDL_wt+"</StringValue></StringParam>";            
            result+="<DecimalParam><name>post_frq_constant</name><DecimalValue>"+freqNode.RDL_post_c+"</DecimalValue></DecimalParam>";            
            result+="<StringParam><name>post_frq_on</name><StringValue>"+freqNode.RDL_post+"</StringValue></StringParam>";            
            
            result+="<IntParam><name>fSlices</name><IntValue>"+fSlices+"</IntValue></IntParam>";
            result+="<IntParam><name>deltaSlices</name><IntValue>"+deltaInput+"</IntValue></IntParam>";            
            result+="<IntParam><name>nreps</name><IntValue>"+nreps+"</IntValue></IntParam>";
            result+="<IntParam><name>slicesPerPhon</name><IntValue>"+slicesPerPhon+"</IntValue></IntParam>";
            
            result+="<IntParamRep><name>"+"FETSPREAD"+"</name>";
            for(int i=0;i<spread.length;i++)
                result+="<IntValue>"+spread[i]+"</IntValue>";
            result+="</IntParamRep>";
            
            result+="<DecimalParamRep><name>"+"spreadScale"+"</name>";
            for(int i=0;i<spreadScale.length;i++)
                result+="<DecimalValue>"+spreadScale[i]+"</DecimalValue>";
            result+="</DecimalParamRep>";
            
            
            result+="<StringParam><name>continuumSpec</name><StringValue>"+continuumSpec+"</StringValue></StringParam>";
            result+="<StringParam><name>user</name><StringValue>"+user+"</StringValue></StringParam>";
            result+="<StringParam><name>dateTime</name><StringValue>"+dateTime+"</StringValue></StringParam>";
            result+="<StringParam><name>comment</name><StringValue>"+comment+"</StringValue></StringParam>";            
            result+="</parameters>";
            
        }
        catch( Exception e ) {e.printStackTrace(); return "Exception";}
    
        return result;
    }
    
    public boolean equals(TraceParam tp2){            
        if(
            //user.equals(tp2.getUser())&&
            //dateTime==tp2.getDateTime()&&
            //comment.equals(tp2.getComment())&&
            modelInput.equals(tp2.getModelInput())&&
            spread[0]==tp2.getSpread()[0]&&
            spread[1]==tp2.getSpread()[1]&&
            spread[2]==tp2.getSpread()[2]&&
            spread[3]==tp2.getSpread()[3]&&
            spread[4]==tp2.getSpread()[4]&&
            spread[5]==tp2.getSpread()[5]&&
            spread[6]==tp2.getSpread()[6]&&
            spreadScale[0]==tp2.getSpreadScale()[0]&&
            spreadScale[1]==tp2.getSpreadScale()[1]&&
            spreadScale[2]==tp2.getSpreadScale()[2]&&
            spreadScale[3]==tp2.getSpreadScale()[3]&&
            spreadScale[4]==tp2.getSpreadScale()[4]&&
            spreadScale[5]==tp2.getSpreadScale()[5]&&
            spreadScale[6]==tp2.getSpreadScale()[6]&&
            min==tp2.getMin()&&
            max==tp2.getMax()&&
            nreps==tp2.getNReps()&&
            slicesPerPhon==tp2.getSlicesPerPhon()&&
            fSlices==tp2.getFSlices()&&
            deltaInput==tp2.getDeltaInput()&&
            noiseSD==tp2.getNoiseSD()&&
            stochasticitySD==tp2.getStochasticitySD()&&
            atten==tp2.getAtten()&&
            bias==tp2.getBias()&&
            learningrate==tp2.getLearningRate()&&
            continuumSpec.equals(tp2.getContinuumSpec())&&
            freqNode.equals(tp2.getFreqNode())&&
            primeNode.equals(tp2.getPrimeNode())&&
            decay.equals(tp2.getDecay())&&
            rest.equals(tp2.getRest())&&
            alpha.equals(tp2.getAlpha())&&
            gamma.equals(tp2.getGamma())&&
            //lambda.equals(tp2.getLambda())&&
            lexicon.equals(tp2.getLexicon())){
            //System.out.println("tp==tp");
            return true;
        }
        else{
            //System.out.println("tp!=tp");
            return false;            
        }
    }
}


/*System.out.println(user+".equals("+tp2.getUser());
        System.out.println(comment+".equals("+tp2.getComment());
        System.out.println(modelInput+".equals("+tp2.getModelInput());
        System.out.println(spread[0]+"=="+tp2.getSpread()[0]);
        System.out.println(spread[1]+"=="+tp2.getSpread()[1]);
        System.out.println(spread[2]+"=="+tp2.getSpread()[2]);
        System.out.println(spread[3]+"=="+tp2.getSpread()[3]);
        System.out.println(spread[4]+"=="+tp2.getSpread()[4]);
        System.out.println(spread[5]+"=="+tp2.getSpread()[5]);
        System.out.println(spread[6]+"=="+tp2.getSpread()[6]);
        System.out.println(spreadScale[0]+"=="+tp2.getSpreadScale()[0]);
        System.out.println(spreadScale[1]+"=="+tp2.getSpreadScale()[1]);
        System.out.println(spreadScale[2]+"=="+tp2.getSpreadScale()[2]);
        System.out.println(spreadScale[3]+"=="+tp2.getSpreadScale()[3]);
        System.out.println(spreadScale[4]+"=="+tp2.getSpreadScale()[4]);
        System.out.println(spreadScale[5]+"=="+tp2.getSpreadScale()[5]);
        System.out.println(spreadScale[6]+"=="+tp2.getSpreadScale()[6]);
        System.out.println(min+"=="+tp2.getMin());
        System.out.println(max+"=="+tp2.getMax());
        System.out.println(nreps+"=="+tp2.getNReps());
        System.out.println(slicesPerPhon+"=="+tp2.getSlicesPerPhon());
        System.out.println(fSlices+"=="+tp2.getFSlices());
        System.out.println(deltaInput+"=="+tp2.getDeltaInput());
        System.out.println(noiseSD+"=="+tp2.getNoiseSD());
        System.out.println(stochasticitySD+"=="+tp2.getStochasticitySD());
        System.out.println(atten+"=="+tp2.getAtten());
        System.out.println(bias+"=="+tp2.getBias());
        System.out.println(learningrate+"=="+tp2.getLearningRate());
        System.out.println(continuumSpec+".equals("+tp2.getContinuumSpec());*/
        

/* bad encapsulation! the table should update its param object via the usual
     * access methods!
     */
    //this is for TraceParams created from table data
    /*public TraceParam(Object[] raw){
        freqNode = new wordFrequency();
        
        alpha = new Alpha();
        decay = new Decay();
        rest = new Rest();
        gamma = new Gamma();        
        lambda = new Lambda();        
        lexicon = new TraceLexicon();        
        
        for(int i=0;i<raw.length;i++){
            //System.out.println(i+" "+raw[i]);    
            if(i==-1) {}
            else if(i==1) {
                java.util.StringTokenizer tk=new java.util.StringTokenizer((String)raw[i],",");     
                this.lexicon=new TraceLexicon();
                if(null!=raw[2]&&!raw[2].equals("")){
                    java.util.StringTokenizer tk2=new java.util.StringTokenizer((String)raw[2],",");     
                    while(tk.hasMoreTokens())
                        if(tk2.hasMoreTokens())
                            this.lexicon.add(new TraceWord(tk.nextToken().trim(),(new Double(tk2.nextToken().trim())).doubleValue()));
                        else
                            this.lexicon.add(new TraceWord(tk.nextToken().trim()));
                }
                else{
                    while(tk.hasMoreTokens())
                        this.lexicon.add(new TraceWord(tk.nextToken().trim()));
                }
//                nwords=lexicon.size();    
            }            
            else if(i==2) continue;                        
            else if(i==3) this.modelInput=(String)raw[i];                
            else if(i==4) this.continuumSpec=(String)raw[i];                
            else if(i==5) this.dateTime=(String)raw[i];    
            else if(i==6) this.comment=(String)raw[i];    
            else if(i==7) this.user=(String)raw[i];                
            else if(i==9){
                if(raw[i].getClass().getName().endsWith("Double")) this.alpha.IF=((Double)raw[i]).doubleValue();    
                else this.alpha.IF=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==10){
                if(raw[i].getClass().getName().endsWith("Double")) this.alpha.FP=((Double)raw[i]).doubleValue();    
                else this.alpha.FP=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==11){
                if(raw[i].getClass().getName().endsWith("Double")) this.alpha.PW=((Double)raw[i]).doubleValue();    
                else this.alpha.PW=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==12){
                if(raw[i].getClass().getName().endsWith("Double")) this.alpha.PF=((Double)raw[i]).doubleValue();    
                else this.alpha.PF=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==13){
                if(raw[i].getClass().getName().endsWith("Double")) this.alpha.WP=((Double)raw[i]).doubleValue();    
                else this.alpha.WP=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==15){
                if(raw[i].getClass().getName().endsWith("Double")) this.gamma.F=((Double)raw[i]).doubleValue();    
                else this.gamma.F=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==16){
                if(raw[i].getClass().getName().endsWith("Double")) this.gamma.P=((Double)raw[i]).doubleValue();    
                else this.gamma.P=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==17){
                if(raw[i].getClass().getName().endsWith("Double")) this.gamma.W=((Double)raw[i]).doubleValue();    
                else this.gamma.W=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==18){
                if(raw[i].getClass().getName().endsWith("Double")) this.lambda.F=((Double)raw[i]).doubleValue();    
                else this.lambda.F=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==19){
                if(raw[i].getClass().getName().endsWith("Double")) this.lambda.P=((Double)raw[i]).doubleValue();    
                else this.lambda.P=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==20){
                if(raw[i].getClass().getName().endsWith("Double")) this.lambda.W=((Double)raw[i]).doubleValue();    
                else this.lambda.W=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==19){
                if(raw[i].getClass().getName().endsWith("Double")) this.decay.F=((Double)raw[i]).doubleValue();    
                else this.decay.F=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==20){
                if(raw[i].getClass().getName().endsWith("Double")) this.decay.P=((Double)raw[i]).doubleValue();    
                else this.decay.P=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==21){
                if(raw[i].getClass().getName().endsWith("Double")) this.decay.W=((Double)raw[i]).doubleValue();    
                else this.decay.W=(new Double((String)raw[i])).doubleValue();
            }
            else if(i==23){
                if(raw[i].getClass().getName().endsWith("Integer")) this.spread[0]=((Integer)raw[i]).intValue();    
                else this.spread[0]=(new Integer((String)raw[i])).intValue();
            }
            else if(i==24){
                if(raw[i].getClass().getName().endsWith("Integer")) this.spread[1]=((Integer)raw[i]).intValue();    
                else this.spread[1]=(new Integer((String)raw[i])).intValue();
            }
            else if(i==25){
                if(raw[i].getClass().getName().endsWith("Integer")) this.spread[2]=((Integer)raw[i]).intValue();    
                else this.spread[2]=(new Integer((String)raw[i])).intValue();
            }
            else if(i==26){
                if(raw[i].getClass().getName().endsWith("Integer")) this.spread[3]=((Integer)raw[i]).intValue();    
                else this.spread[3]=(new Integer((String)raw[i])).intValue();
            }
            else if(i==27){
                if(raw[i].getClass().getName().endsWith("Integer")) this.spread[4]=((Integer)raw[i]).intValue();    
                else this.spread[4]=(new Integer((String)raw[i])).intValue();
            }
            else if(i==28){
                if(raw[i].getClass().getName().endsWith("Integer")) this.spread[5]=((Integer)raw[i]).intValue();    
                else this.spread[5]=(new Integer((String)raw[i])).intValue();
            }
            else if(i==29){
                if(raw[i].getClass().getName().endsWith("Integer")) this.spread[6]=((Integer)raw[i]).intValue();    
                else this.spread[6]=(new Integer((String)raw[i])).intValue();
            }
            else if(i==31){
                if(raw[i].getClass().getName().endsWith("Double")) this.rest.F=((Double)raw[i]).doubleValue();    
                else this.rest.F=(new Double((String)raw[i])).doubleValue();
            } 
            else if(i==32){
                if(raw[i].getClass().getName().endsWith("Double")) this.rest.P=((Double)raw[i]).doubleValue();    
                else this.rest.P=(new Double((String)raw[i])).doubleValue();
            } 
            else if(i==33){
                if(raw[i].getClass().getName().endsWith("Double")) this.rest.W=((Double)raw[i]).doubleValue();    
                else this.rest.W=(new Double((String)raw[i])).doubleValue();
            } 
            else if(i==35){
                if(raw[i].getClass().getName().endsWith("Double")) this.noiseSD=((Double)raw[i]).doubleValue();    
                else this.noiseSD=(new Double((String)raw[i])).doubleValue();
            }            
            else if(i==37){
                if(raw[i].getClass().getName().endsWith("Double")) this.stochasticitySD=((Double)raw[i]).doubleValue();    
                else this.stochasticitySD=(new Double((String)raw[i])).doubleValue();
            }             
            else if(i==39) {      
                double sc;
                if(raw[i].getClass().getName().endsWith("Double")) sc=((Double)raw[i]).doubleValue();    
                else sc=(new Double((String)raw[i])).doubleValue();
                spreadScale[0]=sc;spreadScale[1]=sc;spreadScale[2]=sc;
                spreadScale[3]=sc;spreadScale[4]=sc;spreadScale[5]=sc;
                spreadScale[6]=sc;}
            else if(i==40){
                if(raw[i].getClass().getName().endsWith("Double")) this.min=((Double)raw[i]).doubleValue();      
                else this.min=(new Double((String)raw[i])).doubleValue();
            }  
            else if(i==41){
                if(raw[i].getClass().getName().endsWith("Double")) this.max=((Double)raw[i]).doubleValue();      
                else this.max=(new Double((String)raw[i])).doubleValue();
            }  
            else if(i==42){
                if(raw[i].getClass().getName().endsWith("Integer"))  this.fSlices=((Integer)raw[i]).intValue();  
                else this.fSlices=(new Integer((String)raw[i])).intValue();
            }
            else if(i==43){
                if(raw[i].getClass().getName().endsWith("Integer")) this.deltaInput=((Integer)raw[i]).intValue();     
                else this.deltaInput=(new Integer((String)raw[i])).intValue();
            } 
            else if(i==44){
                if(raw[i].getClass().getName().endsWith("Integer")) this.nreps=((Integer)raw[i]).intValue();
                else this.nreps=(new Integer((String)raw[i])).intValue();
            }
            else if(i==45){
                if(raw[i].getClass().getName().endsWith("Integer")) this.slicesPerPhon=((Integer)raw[i]).intValue();   
                else this.slicesPerPhon=(new Integer((String)raw[i])).intValue();
            }             
            else if(i==48){
                //System.out.println("RDL_rest");
                if(raw[i].getClass().getName().endsWith("Double")) this.freqNode.RDL_rest_s=((Double)raw[i]).doubleValue();    
                else this.freqNode.RDL_rest_s=(new Double((String)raw[i])).doubleValue();
                if(this.freqNode.RDL_rest_s > 0.0) this.freqNode.RDL_rest = true;
                else this.freqNode.RDL_rest = false;
            }
            else if(i==49){
                //System.out.println("RDL_wt");
                if(raw[i].getClass().getName().endsWith("Double")) this.freqNode.RDL_wt_s=((Double)raw[i]).doubleValue();    
                else this.freqNode.RDL_wt_s=(new Double((String)raw[i])).doubleValue();
                if(this.freqNode.RDL_wt_s > 0.0) this.freqNode.RDL_wt = true;
                else this.freqNode.RDL_wt = false;
            }
            else if(i==50){
                //System.out.println("RDL_post");
                if(raw[i].getClass().getName().endsWith("Double")) this.freqNode.RDL_post_c=((Double)raw[i]).doubleValue();    
                else this.freqNode.RDL_post_c=(new Double((String)raw[i])).doubleValue();
                if(this.freqNode.RDL_post_c > 0.0) this.freqNode.RDL_post = true;
                else this.freqNode.RDL_post = false;
            }                           
        }
        maxSpread = -1;
        for(int s = 0; s < spread.length; s++)
            if(spread[s] > maxSpread)
                maxSpread = spread[s];
    }*/

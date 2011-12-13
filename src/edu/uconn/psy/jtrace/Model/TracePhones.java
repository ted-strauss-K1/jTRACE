package edu.uconn.psy.jtrace.Model;
import java.util.Locale;
import java.util.TreeMap;
import java.text.*;
import java.util.regex.*;

/**
 * TracePhones defines TRACE phonemes, input representations, and ambiguous
 * phonemes. 
 * An instance of TracePhones can be created to create and refer to spread
 * input representations and phoneme continua. The static methods can be used
 * to get the list of valid phones and validate phoneme strings.
 *
 * Note that the phonDefs matrix contrains peak values for each phoneme. The
 * phonSpread matrix and related code computes ramp-up and ramp-down activations
 * for each phone.
 * 
 * @author Rafi Pelosoff
 * @author Ted Strauss
 * @author Harlan Harris
 */

/*
 *TODO: 
 *1. rework TracePhones to accept custom phoneme sets.
 * - setPhones(double[][][] p)
 * - the toChar and toPhone methods must be made dynamic
 * - 
 *2. rework makeContinuum to accomodate #1
 *3. update TraceNet and TraceSim to accomodate #1
 *
 */
public class TracePhones {
    TreeMap phonemes;
    
    double PhonDefs[][];
    double DurationScalar[][];
    String labels[]; //default set: pbtdkgsSrlaiu^-    
    String languageName;
    // constants
    public int NPHONS; //number of phonemes
    public int NFEATS;  //number of features
    public int NCONTS;  //number of values(continua) per feature
    public final int MAXSTEPS = 9;   // maximum number of steps in an ambiguous phone
    
    // having to do with phoneme spread
    private double phonSpread[][][]; //new double [NPHONS][NFEATS*NCONTS][offset*2+1]
    private double norm[];   // normalizer for each spreaded phoneme sum_x(sum_y(pix(x,y)^2))
    private int[] spreadOffset;
    private int offset;         // maximum spread
    
    // having to do with ambiguous phones
    private TreeMap ambigPhonemes;
    private double AmbiguousPhonDefs[][];    
    private double AmbiguousDurScalars[][];
    private int[] ambigSpreadOffset;
    private char ambigFrom, ambigTo;    
    private int numAmbiguousPhons;   
    
    // having to do with allophonic relations between phonemes.
    private boolean[][] allophonicRelations;
    
    RuleBasedCollator collator;
    //public String ambigLabels[]={"0", "1", "?", "2", "3", "4", "5", "6", "7", "8"}; //pbtdkgsSrlaiu^-            
    
    /** Constructor for TracePhones.  */
    public TracePhones(){
        languageName = "default";
        // Create an en_US Collator object
        RuleBasedCollator en_USCollator = (RuleBasedCollator)Collator.getInstance(new Locale("en", "US", ""));
        String en_USRules = en_USCollator.getRules();
        try{
            //the phoneme map will be alphabetically organized.
            collator = new RuleBasedCollator(en_USRules);
            phonemes = new TreeMap(collator);                        
            for(int i=0;i<DefaultPhonDefs.length;i++){
                phonemes.put(defaultLabels[i],new Phon(DefaultPhonDefs[i],DefaultDurationScalar[i],defaultLabels[i],Phon.PHON_ROLE_NORMAL));
            }
            NPHONS = DefaultPhonDefs.length; //number of phonemes            
            PhonDefs = compilePhonDefs();
            DurationScalar = compileDurationScalars();
            labels = compileLabels();            
            
        } catch(java.text.ParseException pe){
            pe.printStackTrace();
        }
        
        NFEATS = 9;  //number of features
        NCONTS = 7;  //number of values(continua) per feature
        
        // having to do with phoneme spread
        phonSpread = null; //new double [NPHONS][NFEATS*NCONTS][offset*2+1]
        norm = null;   // normalizer for each spreaded phoneme sum_x(sum_y(pix(x,y)^2))
        offset = 0;    // maximum spread

        //Having to do with ambiguous phones
        //AmbiguousPhonDefs = new double[MAXSTEPS][NFEATS * NCONTS];            
        numAmbiguousPhons = 0;      
        
        // having to do with allophonic relations 
        allophonicRelations = new boolean[NPHONS][NPHONS];
        
        //diagnostic
        /*Object[] lst=phonemes.values().toArray();
        for(int i=0;i<lst.length;i++){
            System.out.println("phon "+i+": "+((Phon)lst[i]).label.charAt(0));
        }*/
    }    
    public TracePhones(String _n,String[] _p, double[][] _f, double[][] _d){
        languageName = _n;
        try{
            // Create an en_US Collator object
            RuleBasedCollator en_USCollator = (RuleBasedCollator)Collator.getInstance(new Locale("en", "US", ""));
            String en_USRules = en_USCollator.getRules();        
            //the phoneme map will be alphabetically organized.
            RuleBasedCollator newCollator = new RuleBasedCollator(en_USRules);
            phonemes = new TreeMap(newCollator);                        
            for(int i=0;i<_p.length;i++){
                phonemes.put(_p[i],new Phon(_f[i],_d[i],_p[i],Phon.PHON_ROLE_NORMAL));
            }            
            NPHONS = _f.length; //number of phonemes
            PhonDefs = compilePhonDefs();
            DurationScalar = compileDurationScalars();
            labels = compileLabels();            
            
        } catch(java.text.ParseException pe){
            pe.printStackTrace();
        }
        NFEATS = 9;  //number of features
        NCONTS = 7;  //number of values(continua) per feature        
        
        // having to do with phoneme spread
        phonSpread = null; //new double [NPHONS][NFEATS*NCONTS][offset*2+1]
        norm = null;   // normalizer for each spreaded phoneme sum_x(sum_y(pix(x,y)^2))
        offset = 0;         // maximum spread

        // having to do with ambiguous phones
        //AmbiguousPhonDefs = new double[MAXSTEPS][NFEATS * NCONTS];            
        numAmbiguousPhons = 0;                   
        
        // having to do with allophonic relations 
        allophonicRelations = new boolean[NPHONS][NPHONS];
        
        //diagnostic
        Object[] lst=phonemes.values().toArray();
        for(int i=0;i<lst.length;i++){
            System.out.println("phon "+i+": "+((Phon)lst[i]).label.charAt(0));
        }
    }
    public TracePhones clone(){
        TracePhones _tp = new TracePhones(getLanguageName(),getLabels(),getPhonDefs(), getDurationScalar());
        _tp.setAllophoneRelations(getAllophoneRelations());
        return _tp;
    }
    
    public class Phon{
        public static final int PHON_ROLE_NORMAL = 1;
        public static final int PHON_ROLE_AMBIG = 2;
        public static final int PHON_ROLE_ALLOPHONE = 3;
        public static final int PHON_ROLE_OTHER = 4;
        
        public double[] features;
        public double[] durationScalar;
        public String label;
        private int index;
        private int phonologicalRole;
        //PhonologicalRole: 1=normal phoneme, 3=allophone (i.e. no competition)
        //2=ambiguous sound (possible merge w/2), 4=
        
        public Phon(double[] _f,double[] _d,String _l,int _r){
            features=_f;
            durationScalar=_d;
            label=_l;
            phonologicalRole=_r;
        }       
        public void setIndex(int idx){index=idx;}
        public int getIndex(){return index;}
        public void setRole(int r){phonologicalRole=r;}
        public int getRole(){return phonologicalRole;} 
        public boolean equals(Phon c){
            if(durationScalar==c.durationScalar&&
                features==c.features&&
                label==c.label)
                return true;
            else
                return false;
        }
    }    
    
    public void addPhoneme(String _p, double[] _ft, double[] _du){
        phonemes.put(_p,new Phon(_ft,_du,_p,Phon.PHON_ROLE_NORMAL));   
        NPHONS++;
        compileAll();
        // set the index variable        
    }
    public void removePhoneme(String _p){
        phonemes.remove(_p);
        NPHONS--;
        compileAll();
    }
    public void replacePhoneme(String _old, Phon _new){
        phonemes.remove(_old);
        phonemes.put(_new.label,_new);
        compileAll();
    }
    public void removeAllPhonemes(){
        Object[] iter = getLabels();
        for(int i=0;i<iter.length;i++){
            if(!((String)iter[i]).equals("-")){
                phonemes.remove((String)iter[i]);
                NPHONS--;            
            }
        }
    }
        
    /** 
     * Get some or all of the phonSpread matrix. Assumes (?!) that it's been
     * created!
     *
     * @param phon      if present, just get that element of phonSpread
     * @return          2-D or 3-D matrix of spread phoneme representations
     */
    public double[][][] getPhonSpread() {return phonSpread;}
    public double[][] getPhonSpread(int phon) { 
        if(phon < 0 || phon >= (50+numAmbiguousPhons) || (phon > NPHONS && phon < 50)) 
            return null;
        else 
            return phonSpread[phon];    // TODO: this will fail if not initialized!
    }
    
    /**
     * Get normalization matrix. Assumes (?!) that it's been created!
     *
     * @param phon      which norm component to get
     * @return          norm
     */
    public double getNorm(int phon) throws TraceException {
        if(phon < 0 || phon >= (50+numAmbiguousPhons) || (phon > NPHONS && phon < 50)) 
            throw new TraceException("Phoneme index out of bounds");            
        else
            return norm[phon];
    }
    
    public int getOffset() {return offset;}
    public int[] getSpreadOffset(){return spreadOffset;}
    public int[] getAmbigSpreadOffset(){return ambigSpreadOffset;}
    
    public String getLanguageName(){
        return languageName;
    }
    public void setLanguageName(String _ln){
        languageName=_ln;       
    }    
    public double[][] getPhonDefs(){
        return PhonDefs;
    }    
    private double[][] compilePhonDefs(){
        NPHONS = phonemes.size();
        PhonDefs = new double[phonemes.size()][];
        java.util.Iterator iter = phonemes.values().iterator();
        for(int i=0;iter.hasNext();i++)
            PhonDefs[i]=((Phon)iter.next()).features;
        return PhonDefs;
    }
    public double[][] getDurationScalar(){
        return DurationScalar;
    }        
    private double[][] compileDurationScalars(){
        DurationScalar = new double[phonemes.size()][];
        java.util.Iterator iter = phonemes.values().iterator();
        for(int i=0;iter.hasNext();i++)
            DurationScalar[i]=((Phon)iter.next()).durationScalar;
        return DurationScalar;
    }
    public String[] getLabels(){
        return labels;
    }    
    private String[] compileLabels(){
        labels = new String[phonemes.size()];
        java.util.Iterator iter = phonemes.values().iterator();
        for(int i=0;iter.hasNext();i++)
            labels[i]=((Phon)iter.next()).label;
        return labels;
    }
    private boolean[][] compileAllophonicRelations(){
        // having to do with allophonic relations 
        boolean[][] _allophonicRelations = new boolean[NPHONS][NPHONS];
        for(int i=0;i<allophonicRelations.length;i++)
            for(int j=0;j<allophonicRelations.length;j++)
                _allophonicRelations[i][j] = allophonicRelations[i][j];
        allophonicRelations=_allophonicRelations;
        return allophonicRelations;
    }
    public void compileAll(){
        NPHONS = phonemes.size();
        compilePhonDefs();
        compileDurationScalars();
        compileLabels();
        compileAllophonicRelations();
        if(ambigFrom!=' '&&ambigTo!=' '&&numAmbiguousPhons>0)
            try{
                makePhonemeContinuum(ambigFrom,ambigTo,numAmbiguousPhons);
            }
            catch(TraceException te){
                te.printStackTrace();
            }            
    }
    public double[][] getAmbiguousPhonDefs(){
        return AmbiguousPhonDefs;
    }
    public double[][] getAmbiguousDurScalars(){
        return AmbiguousDurScalars;
    }
    
    /**
     *Retrieve the phoneme specification array index given
     *the phoneme's character.
     *
     *@param feat   The phoneme's char
     * todo merge mapPhon with getKeyFromValue
     */
    public int mapPhon(char val){
        //
        if(val=='{'||val=='}') return 101;
        Object[] lst=phonemes.values().toArray();
        for(int i=0;i<lst.length;i++){
            if(((Phon)lst[i]).label.charAt(0)==val){
                //System.out.println("mapPhon("+val+")=>"+i);
                return i;
            }                
        }
        if(ambigPhonemes==null||ambigPhonemes.size()==0){
            //System.out.println("mapPhon("+val+")=>"+-1);                
            return -1;
        }
        //lst=ambigPhonemes.values().toArray();
        if(val=='?'){
            //question mark is mid-point of continuum
            //System.out.println("mapPhon("+val+")=>"+(50 + (numAmbiguousPhons/2)));                
            return 50 + (numAmbiguousPhons/2);
        }
        String _val = (new Character(val)).toString();
        //System.out.println("\t\t"+val+">>"+(new Integer(((TracePhones.Phon)ambigPhonemes.get(_val)).label)).intValue());
        return 50+(new Integer(((TracePhones.Phon)ambigPhonemes.get(_val)).label)).intValue();
        
        /*for(int i=0;i<lst.length;i++){
            //System.out.println("phon match "+((Phon)lst[i]).label.charAt(0)+" ? "+val);            
            if(((Phon)lst[i]).label.charAt(0)==val){
                //System.out.println("mapPhon("+val+")=>"+(50+i));                            
                return (50+i);
            }                
        }*/
        //if(val=='?')
        //    return 50+(int)(Math.floor((double)ambigPhonemes.values().toArray().length/2));
        //System.out.println("mapPhon("+val+")=>"+-1);                            
        //return -1;
    }
    /**
     *Retrieve the phoneme representation given the phon symbol.
     *
     *@param symbol The phoneme symbol.
     */
    public Phon getPhon(String symbol){
        java.util.Iterator iter = phonemes.values().iterator();
        for(int i=0;iter.hasNext();i++){
            Phon nex = ((Phon)iter.next());
            if(symbol.equals(nex.label))
                return nex;
        }
        if(ambigPhonemes==null||ambigPhonemes.size()==0)
            return null;
        iter = ambigPhonemes.values().iterator();
        for(int i=0;iter.hasNext();i++){
            Phon nex = ((Phon)iter.next());
            if(symbol.equals(nex.label))
                return nex;
        }
        return null;
    }
    public TreeMap getPhonemes(){
        return phonemes;
    }
    public char toChar(int idx){
        Object[] lst=phonemes.values().toArray();
        for(int i=0;i<lst.length;i++){
            if(idx==i){
                return ((Phon)lst[i]).label.charAt(0);
            }                
        }
        if(ambigPhonemes==null||ambigPhonemes.size()==0)
            return '-';        
        lst=ambigPhonemes.values().toArray();
        for(int i=0;i<lst.length;i++){
            if(idx==(i+50)){
                return ((Phon)lst[i]).label.charAt(0);
            }                
        }
        return '-';        
    }
    
    public int mapFeat(String ft){
        if(ft.equals("POW")) return 0;
        else if(ft.equals("VOC")) return 1;
        else if(ft.equals("DIF")) return 2;
        else if(ft.equals("ACU")) return 3;
        else if(ft.equals("GRD")) return 4;
        else if(ft.equals("VOI")) return 5;
        else //if(ft.equals("BUR")) 
            return 6;
    }
    /**
     * Determines if a string is a valid TRACE word or string. (At least one character,
     * and all characters are valid characters.)
     *
     * @param tw    TRACE word
     * @return      <code>true</code> if valid; <code>false</code> if not
     */
    public boolean validTraceWord(String tw)
    {
        Pattern p = Pattern.compile(getInputPattern());
        Matcher m = p.matcher(tw);
        return m.matches();
        
        /*if (tw.length() != 0)
        {
            for (int i = 0; i < tw.length(); i++)
            {
                if (mapPhon(tw.charAt(i)) == -1)
                    return false;
            }
            return true;
        }        
        return false;*/
    }
    
    public String getLexemePattern(){
        String patt = "";
        String lbls="";
        labels=compileLabels();
        for(int i=0;i<labels.length;i++){
            if(labels[i].equals("-")) lbls=lbls+"\\-";
            else if(labels[i].equals("^")) lbls=lbls+"\\^";            
            else lbls=lbls+labels[i];
        }
        patt = "["+lbls+"]+";
        //System.out.println("this is the pattern: "+patt);
        return patt;
    }
    public String getInputPattern(){
        String patt = "";
        String lbls="";
        labels=compileLabels();
        for(int i=0;i<labels.length;i++){
            if(labels[i].equals("-")) lbls=lbls+"\\-";
            else if(labels[i].equals("^")) lbls=lbls+"\\^";            
            else lbls=lbls+labels[i];
        }
        String ambg="";
        for(int i=0;i<numAmbiguousPhons;i++)
            ambg=ambg+(new Integer(i)).toString();        
        if(numAmbiguousPhons>0)
            lbls = "["+ambg+lbls+"\\?]";
        else
            lbls = "["+lbls+"\\-]";
        String nmbrs="";
        for(int i=0;i<10;i++)
            nmbrs=nmbrs+(new Integer(i)).toString();        
        nmbrs = "["+nmbrs+"]";
        //
        String spli="(\\{"+lbls+nmbrs+lbls+"\\})";
        //
        patt = "["+lbls+"|"+spli+"]+";        
        //System.out.println("this is the pattern: "+patt);
        return patt;
    }
    
    /** Spreads the phonesmes over time according to the spread array. This
     * should be run before a TraceSim is run, and after any change to the
     * ambiguous phoneme information.
     * 
     * @param spread        spread[] in TraceParam
     * @param scale         spreadScale[] in TraceParam
     * @param min           min in TraceParam
     * @param max           max in TraceParam
     */
    public void spreadPhons(int spread[], double scale[], double min, double max) throws TraceException
    {
        int spreadSteps, maxspread = 0;
        spreadOffset = new int[NPHONS];
        ambigSpreadOffset = new int[numAmbiguousPhons];
        
        double delta;
        
        if(spread.length != scale.length)
            throw new TraceException("spread and scale parameters have different scale");
        
        //norm = new double[NPHONS];
        // ambiguous phones start at #50, so allocate enough room for them
        norm = new double[50+MAXSTEPS];
        
        // find the max spread (spread*scale) for all features and scale the spread
        // values we received from our caller
        for(int phon = 0; phon < NPHONS; phon++) //loop over phonemes
        {
            spreadOffset[phon] = 0;
            for(int i = 0 ; i < spread.length; i++)
            {            
                if (spread[i]*scale[i]*DurationScalar[phon][0] > spreadOffset[phon])
                    spreadOffset[phon] = (int)java.lang.Math.ceil(spread[i]*scale[i]*DurationScalar[phon][0]);
                //
                if (spread[i]*scale[i]*DurationScalar[phon][0] > maxspread)
                    maxspread = (int)java.lang.Math.ceil(spread[i]*scale[i]*DurationScalar[phon][0]);
                //
                //spread[i] = (int)(spread[i]*scale[i]);
            }   
            
        }
        //max spread from ambiguous set
        for(int phon = 0; phon < numAmbiguousPhons; phon++) //loop over phonemes            
        {
            ambigSpreadOffset[phon] = 0;
            for(int i = 0 ; i < spread.length; i++)
            {            
                if (spread[i]*scale[i]*AmbiguousDurScalars[phon][0] > ambigSpreadOffset[phon])
                    ambigSpreadOffset[phon] = (int)java.lang.Math.ceil(spread[i]*scale[i]*AmbiguousDurScalars[phon][0]);
                //
                if (spread[i]*scale[i]*AmbiguousDurScalars[phon][0] > maxspread)
                    maxspread = (int)java.lang.Math.ceil(spread[i]*scale[i]*AmbiguousDurScalars[phon][0]);
                //
                spread[i] = (int)(spread[i]*scale[i]);                
            }
        }
        
        //allocate the table
        //phonSpread = new double [NPHONS][NFEATS*NCONTS][maxspread*2+1]; //add one double for safer code
        phonSpread = new double [50+MAXSTEPS][NFEATS*NCONTS][(maxspread*4)+1]; //add one double for safer code
        
        // save to private field for some reason
        offset = maxspread; // middle of the phonSpread matrix, so we can iterate to 
                            // left and right to ramp up and ramp down
        
        if(min<0) min = 0; //this appears to be how C trace is implemented.
        
        for(int phon = 0; phon < NPHONS; phon++) //loop over phonemes
        {
            offset = spreadOffset[phon];
            norm[phon] = 0;
            
            for(int cont = 0; cont < NFEATS*NCONTS; cont++) //loop over continuoum
                
                if(PhonDefs[phon][cont] > 0)
                {
                    spreadSteps = (int)(cont/NFEATS);//maxspread;
                    
                    // delta is the amount to ramp up/down
                    delta = ((PhonDefs[phon][cont]*max)-((PhonDefs[phon][cont]*min)))/(double) (spread[spreadSteps]*DurationScalar[phon][0]);
                        
                    for(int i = 0; i < (int)(spread[spreadSteps]*DurationScalar[phon][0]); i++)
                    {                        
                        // compute spread
                        phonSpread[phon][cont][offset+i] = (PhonDefs[phon][cont] * max) - (delta * i);
                        phonSpread[phon][cont][offset-i] = (PhonDefs[phon][cont] * max) - (delta * i);
                        // and normalization info
                        norm[phon] +=   phonSpread[phon][cont][offset+i]*phonSpread[phon][cont][offset+i] + 
                                        phonSpread[phon][cont][offset-i]*phonSpread[phon][cont][offset-i];
                    }
                }
        }
        
        // loop over ambiguous phonemes too
        // this is just like above, but with AmbiguousPhonDefs instead of phonDefs.
        for(int phon = 0; phon < numAmbiguousPhons; phon++) //loop over phonemes            
        {
            offset = ambigSpreadOffset[phon];
            norm[phon] = 0;
            for(int cont = 0; cont < NFEATS*NCONTS; cont++) //loop over continuoum
            {
                if (AmbiguousPhonDefs[phon][cont] > 0)
                {
                    spreadSteps = cont/NFEATS;//maxspread;
                    
                    delta = ((AmbiguousPhonDefs[phon][cont]*max)-((AmbiguousPhonDefs[phon][cont]*min)))/(double) (spread[spreadSteps]*AmbiguousDurScalars[phon][0]);
                        
                    for(int i = 0; i < (int)(spread[spreadSteps]*AmbiguousDurScalars[phon][0]); i++)
                    {
                        //System.out.println("phonSpread["+(50+phon)+"]["+cont+"][("+offset+"-"+i+")] = (AmbiguousPhonDefs["+phon+"]["+cont+"] * max) - (delta * i);");
                        phonSpread[50+phon][cont][offset+i] = (AmbiguousPhonDefs[phon][cont] * max) - (delta * i);
                        phonSpread[50+phon][cont][offset-i] = (AmbiguousPhonDefs[phon][cont] * max) - (delta * i);
                        
                        norm[phon] +=   phonSpread[50+phon][cont][offset+i]*phonSpread[50+phon][cont][offset+i] + 
                                        phonSpread[50+phon][cont][offset-i]*phonSpread[50+phon][cont][offset-i];
                    }
                }
            }
        }
        offset = maxspread;
    }
   
    
    //FOLLOWING CODE IS DEVOTED TO CREATING AND HANDLING AMBIGUOUS PHONEME CONTINUA.
    
    /**
     * Resets the phoneme continuum. 
     * 
     */
    public void clearPhonemeContinuum(){
        AmbiguousPhonDefs = new double[9][NCONTS*NFEATS];
        AmbiguousDurScalars = new double[9][NFEATS];
        numAmbiguousPhons = 0;
        ambigPhonemes.clear();
        //System.out.println("Phoneme continuum cleared.");
    }
    
    /**
     * Create in this object a phoneme continuum of the same format as the
     * phonDefs matricies.
     * Throws an exception if arguments are unreasonable.
     * Be sure to run spreadPhons() after running this!
     *
     * @param from      one endpoint
     * @param to        the other endpoint
     * @param steps     the number of steps (2-9)
     */
    public void makePhonemeContinuum(char from, char to, int steps) throws TraceException
    {        
        // sanity check
        int _from = mapPhon(from);
        int _to = mapPhon(to);
        if (_from == -1 || _to == -1 || steps <= 1 || steps > MAXSTEPS)
        {
            throw new TraceException("invalid arguments to makePhonemeContinuum");
            //return;
        }
        
        // make sure the arrays have the current values
        compilePhonDefs();
        compileDurationScalars();
        
        // store local copies
        this.ambigFrom=from;
        this.ambigTo=to;
        this.numAmbiguousPhons=steps;
        
        // create some arrays
        AmbiguousPhonDefs = new double[numAmbiguousPhons][NFEATS*NCONTS];
        AmbiguousDurScalars = new double[numAmbiguousPhons][NCONTS];                
        
        
        // incr is the size of the increment at each step of the continuum
        double[] incr_phon=new double[NFEATS*NCONTS];
        double[] incr_dur =new double[NCONTS];
        
        for(int cont = 0; cont < NFEATS*NCONTS; cont++){
            incr_phon[cont]=(PhonDefs[_to][cont] - PhonDefs[_from][cont]) / (numAmbiguousPhons-1);             
        }
        for(int cont = 0; cont < NCONTS; cont++){
            incr_dur[cont]=(DurationScalar[_to][cont] - DurationScalar[_from][cont]) / (numAmbiguousPhons-1); 
        }
        // now create the ambiguous phoneme arrays, i.e. the data used to create the phon objects
        for (int i=0;i<numAmbiguousPhons;i++)
        {            
            //loop over continuoum
            for (int cont = 0; cont < NFEATS*NCONTS; cont++)
            { 
                //continuum value is calculated as ith step in cont difference between ambigFrom to ambigTo:
                AmbiguousPhonDefs[i][cont] = PhonDefs[_from][cont] + (i * incr_phon[cont]);               
            }
            for (int cont = 0; cont < NCONTS; cont++)
            { 
                //continuum value is calculated as ith step in cont difference between ambigFrom to ambigTo:
                AmbiguousDurScalars[i][cont] = DurationScalar[_from][cont] + (i * incr_dur[cont]);               
            }
        }
        //System.out.print("continuum\t");            
        // now create the phon objects
        ambigPhonemes = new TreeMap(collator); 
        for (int i=0;i<numAmbiguousPhons;i++)            
        {
            //System.out.print(i+"\t");
            //ambigPhonemes.put((new Integer(i+1)).toString(),new Phon(AmbiguousPhonDefs[i],AmbiguousDurScalars[i],(new Integer(i+1)).toString(),Phon.PHON_ROLE_AMBIG));           
            ambigPhonemes.put((new Integer(i)).toString(),new Phon(AmbiguousPhonDefs[i],AmbiguousDurScalars[i],(new Integer(i)).toString(),Phon.PHON_ROLE_AMBIG));
        }       
        // and add the special-purpose question mark segment:
        int midpoint = numAmbiguousPhons / 2;
        ambigPhonemes.put("?",new Phon(AmbiguousPhonDefs[midpoint],AmbiguousDurScalars[midpoint],(new Integer(midpoint)).toString(),Phon.PHON_ROLE_AMBIG));
        //System.out.println("+?\t");                    
    }
    
    /**
     */
    public void setAllophoneRelation(int p1, int p2, boolean val){
        allophonicRelations[p1][p2] = val;
    }
    public void setAllophoneRelations(boolean[][] _al){
        allophonicRelations = _al;
    }
    public boolean getAllophoneRelation(int p1, int p2){
        return allophonicRelations[p1][p2];
    }
    public boolean[][] getAllophoneRelations(){
        return allophonicRelations;
    }
    
    /**
     */
    public boolean equals(TracePhones cmp){
        compileAll();
        cmp.compileAll();        
        //todo: make it.
        for(int phon = 0; phon < NPHONS; phon++){ //loop over phonemes
            
        }
        return false;
    }
    
    /** 
     * Returns the mean difference between the two TRACE phoneme representations.
     *
     * @param x     first phone character
     * @param y     second phone character
     * @return      the difference if both are valid characters; -1 otherwise
     */
    public double comparePhons(char x, char y){
        int _x = mapPhon(x);
        int _y = mapPhon(y);
        
        if (_x == -1 || _y == -1)
            return -1;
        
        double result=0;
        
        for(int cont = 0; cont < NFEATS*NCONTS; cont++) //loop over continuoum
            result += Math.abs( PhonDefs[_x][cont] - PhonDefs[_y][cont]);
        
        result /= NFEATS*NCONTS;       
        
        return result;
    }
    
    public String XMLTagNamespace(){
        String result="";
        result+="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
            "<phonology xmlns=\'http://xml.netbeans.org/examples/targetNS\'"+
            "\nxmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\'"+
            "\nxsi:schemaLocation=\'http://xml.netbeans.org/examples/targetNS file:"+edu.uconn.psy.jtrace.UI.jTRACEMDI.properties.rootPath.getAbsolutePath()+"/Schema/jTRACESchema.xsd\'>\n";
        //result+="<phonology>";
        result+="<languageName>"+languageName+"</languageName>\n";
        result+="<phonemes>\n";
        if(labels.length!=PhonDefs.length){
            System.out.println("TracePhones.XMLTag(): Inconsistency is label and phoneme definition arrays.");
        }
        for(int i=0;i<labels.length;i++){
            result+="<phoneme>\n";
            result+="\t<symbol>"+labels[i]+"</symbol>\n";
            result+="\t<features>"+arrayToString(PhonDefs[i])+"</features>\n";
            if(DurationScalar!=null)
                result+="\t<durationScalar>"+arrayToString(DurationScalar[i])+"</durationScalar>\n";            
            if(allophonicRelations!=null)
                result+="\t<allophonicRelations>"+allophonArrayToString(allophonicRelations[i])+"</allophonicRelations>\n";            
            result+="</phoneme>\n";        
        }
        result+="</phonemes>\n";
        result+="</phonology>";
        return result;
    }
    public String XMLTag(){
        String result="";
        result+="<phonology>";
        result+="<languageName>"+languageName+"</languageName>\n";
        result+="<phonemes>\n";
        if(labels.length!=PhonDefs.length){
            System.out.println("TracePhones.XMLTag(): Inconsistency is label and phoneme definition arrays.");
        }
        for(int i=0;i<labels.length;i++){
            result+="<phoneme>\n";
            result+="\t<symbol>"+labels[i]+"</symbol>\n";
            result+="\t<features>"+arrayToString(PhonDefs[i])+"</features>\n";
            if(DurationScalar!=null)
                result+="\t<durationScalar>"+arrayToString(DurationScalar[i])+"</durationScalar>\n";            
            if(allophonicRelations!=null)
                result+="\t<allophonicRelations>"+allophonArrayToString(allophonicRelations[i])+"</allophonicRelations>\n";            
            result+="</phoneme>\n";        
        }
        result+="</phonemes>\n";
        result+="</phonology>";
        return result;
    }
    
    private String arrayToString(double[] ar){
        String res="";
        for(int i=0;i<ar.length;i++)
            res+=ar[i]+" ";
        return res;
    }
    private String allophonArrayToString(boolean[] ar){
        String res="";
        for(int i=0;i<ar.length;i++)
            res+=ar[i]+" ";
        return res;
    }
    public String defaultLabels[] = {"p", "b", "t", "d", "k", "g", "s", "S", "r", "l", "a", "i", "u", "^", "-"};     
    
    public final double DefaultInidividualDurationScalar[] = {1, 1, 1, 1, 1, 1, 1};

    public final double DefaultIndividualPhonDefs[] =          
       {0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* ACU */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* GRD */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }; /* BUR */

    double DefaultDurationScalar[][] = {
       {1, 1, 1, 1, 1, 1, 1}, /*p*/
       {1, 1, 1, 1, 1, 1, 1}, /*b*/
       {1, 1, 1, 1, 1, 1, 1}, /*t*/
       {1, 1, 1, 1, 1, 1, 1}, /*d*/
       {1, 1, 1, 1, 1, 1, 1}, /*k*/
       {1, 1, 1, 1, 1, 1, 1}, /*g*/
       {1, 1, 1, 1, 1, 1, 1}, /*s*/
       {1, 1, 1, 1, 1, 1, 1}, /*S*/
       {1, 1, 1, 1, 1, 1, 1}, /*r*/
       {1, 1, 1, 1, 1, 1, 1}, /*l*/
       {1, 1, 1, 1, 1, 1, 1}, /*a*/
       {1, 1, 1, 1, 1, 1, 1}, /*i*/
       {1, 1, 1, 1, 1, 1, 1}, /*u*/
       {1, 1, 1, 1, 1, 1, 1}, /*^*/
       {1, 1, 1, 1, 1, 1, 1}, /*-*/
    };
    
    // define peak values of each feature/continuum.
    double DefaultPhonDefs[][] = {         
/*p*/  {0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* POW */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOC */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0   , 0 , /* ACU */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* GRD */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOI */
	1  ,  .2 ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */


/*b*/  {0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* POW */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOC */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0   , 0 , /* ACU */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* GRD */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	.2 ,  1  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */ 

/*t*/	{0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* POW */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOC */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* ACU */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* GRD */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOI */
	0  ,  0  ,  1  ,  .2 ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */

/*d*/	{0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* POW */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOC */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* ACU */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* GRD */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	0  ,  0  ,  .2 ,  1  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */

/*k*/	{0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* POW */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOC */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0   , 0 , /* DIF */
	0  ,  0  ,  0  , .1  , .3  ,  1. , .3  , .1   , 0 , /* ACU */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* GRD */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOI */
	0  ,  0  ,  0  ,  0  ,  1  ,  .2 ,  0  ,  0   , 0 }, /* BUR */

/*g*/	{0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* POW */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOC */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0   , 0 , /* DIF */
	0  ,  0  ,  0  , .1  , .3  ,  1. , .3  , .1   , 0 , /* ACU */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* GRD */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	0  ,  0  ,  0  ,  0  ,  .2 ,  1  ,  0  ,  0   , 0 }, /* BUR */
       
/*s*/  {0  ,  0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* VOC */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
	1. , .3  , .1  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* ACU */
	0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0  ,  0   , 0 , /* GRD */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOI */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */

/*S*/  {0  ,  0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* VOC */
	0  ,  0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
	0  ,  0  , .1  , .3  ,  1. , .3  , .1  ,  0   , 0 , /* ACU */
	0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0  ,  0   , 0 , /* GRD */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOI */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */
        
/*r*/  {0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	0  ,  0  ,  0  ,  0  ,  0  ,  0. ,  .5 ,  1.  , 0 , /* DIF */
        0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0   , 0 , /* ACU */
	0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* GRD */
	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */

/*l*/  {0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  .5  , 0 , /* DIF */
        0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* ACU */
	0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* GRD */
	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */      
    
/*a*/  {1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0   , 0 , /* DIF */
	0  ,  0  ,  0  ,  0  ,  0  , .1  , .3  ,  1   , 0 , /* ACU */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* GRD */
	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */

/*i*/	{1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
	1. , .3  , .1  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* ACU */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* GRD */
	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */

/*u*/	{1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	0  ,  0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
	0  ,  0  ,  0  ,  0  , .1  , .3  ,  1. , .3   , 0 , /* ACU */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* GRD */
	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */
                
/*^*/  {0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
	0  ,  0  ,  0  ,  0  ,  0  , .1  , .3  ,  1   , 0 , /* ACU */
	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* GRD */
	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */

/*-*/  	{0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  , 0 ,  1.  , /* POW */
	 0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  , 0 ,  1.  , /* VOC */
	 0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  , 0 ,  1.  , /* DIF */
	 0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  , 0 ,  1.  , /* ACU */
	 0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  , 0 ,  1.  , /* GRD */
	 0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  , 0 ,  1.  , /* VOI */
	 0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  , 0 ,  1.  },  /* BUR */

        //L AND R AMBIGUITY VALUES FOR USE IN MCCLELLAND 1991 SOTCHASTICITY REPLICATION
///*R*/  {0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
//	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
//	0  ,  0  ,  0  ,  0  ,  0  ,  0. ,  .5 ,  1.  , 0 , /* DIF */
//	0  ,  0  ,  0  ,  0  ,  .25  ,  .50  ,  .75 ,  0   , 0 , /* ACU */
//	0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0   , 0 , /* CNS */
//	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
//	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */
//
///*L*/  {0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
//	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
//        0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  .5  , 0 , /* DIF */
//	0  ,  0  ,  0  ,  0  ,  .75 ,  .50  ,  .25,  0  , 0 , /* ACU */
//	0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0   , 0 , /* CNS */
//	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
//	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BUR */
        
    //ORIGINAL L AND R VALUES USED IN MCCLELLAND 1991
/*r*/	//{0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	//0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	//0  ,  0  ,  0  ,  0  ,  0  ,  0. ,  .5 ,  1.  , 0 , /* DIF */
        //0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0   , 0 , /* ACU */
	//0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0   , 0 , /* CNS */
	//1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	//0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 } , /* BST */

/*l*/	//{0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	//0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	//0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  .5  , 0 , /* DIF */
        //0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* ACU */
	//0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0   , 0 , /* CNS */
	//1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	//0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 } , /* BST */
   
//McClelland 1991 /a/ representation
/*a*/	//{1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
        //1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	//0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0   , 0 , /* DIF */
	//0  ,  0  ,  0  ,  0  ,  0  , .1  , .3  ,  1   , 0 , /* ACU */
	//0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* CNS */
	//1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	//0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* BST */        
                
        //McClelland 1991 /^/ representation
/*^*/	//{0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	//1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	//0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
	//0  ,  0  ,  0  ,  0  ,  0  , .1  , .3  ,  1   , 0 , /* ACU */
	//0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* CNS */
	//1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	//0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 } , /* BST */

/*$*/  	//{1.  ,  1.  ,  1.,  1.  ,  1.  ,  1.  ,  1.  , 1. ,  1.  , /* POW */
	// 1.  ,  1.  ,  1.,  1.  ,  1.  ,  1.  ,  1.  , 1. ,  1.  , /* VOC */
	// 1.  ,  1.  ,  1.,  1.  ,  1.  ,  1.  ,  1.  , 1. ,  1.  , /* DIF */
	// 1.  ,  1.  ,  1.,  1.  ,  1.  ,  1.  ,  1.  , 1. ,  1.  , /* ACU */
	// 1.  ,  1.  ,  1.,  1.  ,  1.  ,  1.  ,  1.  , 1. ,  1.  , /* GRD */
	// 1.  ,  1.  ,  1.,  1.  ,  1.  ,  1.  ,  1.  , 1. ,  1.  , /* VOI */
	// 1.  ,  1.  ,  1.,  1.  ,  1.  ,  1.  ,  1.  , 1. ,  1.  },  /* BUR */

 // This is the ambiguous phoneme used by McClelland 1991, halfway tween l and r
 /*L*/	//0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* POW */
	//0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
	//0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  .5 , .5   , 0 , /* DIF */
        //0  ,  0  ,  0  ,  0  ,  .5 ,  .5 ,  .5 ,  0   , 0 , /* ACU */
	//0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0   , 0 , /* CNS */
	//1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
	//0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* BST */
 
 // /K/ has /g/ features for VOI.
 /*K*/	//{0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* POW */
//	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOC */
//	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0   , 0 , /* DIF */
//	0  ,  0  ,  0  , .1  , .3  ,  1. , .3  , .1   , 0 , /* ACU */
//	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* GRD */
//	0  ,  1.  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  , 0 , /* VOI */
//	0  ,  0  ,  0  ,  0  ,  1  ,  .2 ,  0  ,  0   , 0 }, /* BUR */
// /G/ has /k/ features for BUR.
/*G*/	//{0  ,  0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0   , 0 , /* POW */
//	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* VOC */
//	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1. ,  0   , 0 , /* DIF */
//	0  ,  0  ,  0  , .1  , .3  ,  1. , .3  , .1   , 0 , /* ACU */
//	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  1.  , 0 , /* GRD */
//	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
//	0  ,  0  ,  0  ,  0  ,  1 ,  .2  ,  0  ,  0   , 0 }, /* BUR */
        
/*?*///   {0  ,  0  ,  0  ,  0  , 1.  ,  0  ,  0  , 0 ,  0  , /* POW */
//	 0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,1. ,  0  , /* VOC */
//	 0  , 1.  ,  0  ,  0  ,  0  ,  0  ,  0  , 0 ,  0  , /* DIF */
//	 0  ,  0  ,  0  ,  0  ,  0  ,  0  , 1.  , 0 ,  0  , /* ACU */
//	 0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,1. ,  0  , /* GRD */
//	 0  , .5  ,  0  ,  0  ,  0  ,  0  ,  0  ,.5 ,  0  , /* VOI */
//	 0.6, .6  ,  0  ,  0  ,  0  ,  0  ,  0  , 0 ,  0  }  /* BUR */
        
//{
///*x same as ^*/	{0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 }, /* POW */
//	{1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOC */
//	0  ,  0  ,  0  ,  1. ,  0  ,  0  ,  0  ,  0   , 0 , /* DIF */
//	0  ,  0  ,  0  ,  0  ,  0  , .1  , .3  ,  1   , 0 , /* ACU */
//	0  ,  1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* GRD */
//	1. ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 , /* VOI */
//	0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0  ,  0   , 0 } /* BUR */
//    }

    };  
    
    /*
     *BACKUP OF spreadPhones(), july 18, 2006
     public void spreadPhons(int spread[], double scale[], double min, double max) throws TraceException
    {
        int spreadSteps, maxspread = 0;
        double delta;
        
        if(spread.length != scale.length)
            throw new TraceException("spread and scale parameters have different scale");
        
        //norm = new double[NPHONS];
        // ambiguous phones start at #50, so allocate enough room for them
        norm = new double[50+MAXSTEPS];
        
        // find the max spread (spread*scale) for all features and scale the spread
        // values we received from our caller
        for(int i = 0 ; i < spread.length; i++)
        {
            if (spread[i]*scale[i] > maxspread)
                maxspread = (int)java.lang.Math.ceil(spread[i]*scale[i]);
            
            spread[i] = (int)(spread[i]*scale[i]);
        }
        
        //allocate the table
        //phonSpread = new double [NPHONS][NFEATS*NCONTS][maxspread*2+1]; //add one double for safer code
        phonSpread = new double [50+MAXSTEPS][NFEATS*NCONTS][maxspread*2+1]; //add one double for safer code
        
        // save to private field for some reason
        offset = maxspread; // middle of the phonSpread matrix, so we can iterate to 
                            // left and right to ramp up and ramp down
        
        if(min<0) min = 0; //this appears to be how C trace is implemented.
        
        for(int phon = 0; phon < NPHONS; phon++) //loop over phonemes
        {
            norm[phon] = 0;
            
            for(int cont = 0; cont < NFEATS*NCONTS; cont++) //loop over continuoum
                
                if(PhonDefs[phon][cont] > 0)
                {
                    spreadSteps = cont/NFEATS;//maxspread;
                    
                    // delta is the amount to ramp up/down
                    delta = ((PhonDefs[phon][cont]*max)-((PhonDefs[phon][cont]*min)))/(double) spread[spreadSteps];
                        
                    for(int i = 0; i < spread[spreadSteps]; i++)
                    {                        
                        // compute spread
                        phonSpread[phon][cont][offset+i] = (PhonDefs[phon][cont] * max) - (delta * i);
                        phonSpread[phon][cont][offset-i] = (PhonDefs[phon][cont] * max) - (delta * i);
                        // and normalization info
                        norm[phon] +=   phonSpread[phon][cont][offset+i]*phonSpread[phon][cont][offset+i] + 
                                        phonSpread[phon][cont][offset-i]*phonSpread[phon][cont][offset-i];
                    }
                }
        }
        
        //loop over ambiguous phonemes too
        // this is just like above, but with AmbiguousPhonDefs instead of phonDefs.
        for(int phon = 50; phon < 50+numAmbiguousPhons; phon++) //loop over phonemes            
        {
            norm[phon] = 0;
            for(int cont = 0; cont < NFEATS*NCONTS; cont++) //loop over continuoum
            {
                if (AmbiguousPhonDefs[phon-50][cont] > 0)
                {
                    spreadSteps = cont/NFEATS;//maxspread;
                    
                    delta = ((AmbiguousPhonDefs[phon-50][cont]*max)-((AmbiguousPhonDefs[phon-50][cont]*min)))/(double) spread[spreadSteps];
                        
                    for(int i = 0; i < spread[spreadSteps]; i++)
                    {
                        phonSpread[phon][cont][offset+i] = AmbiguousPhonDefs[phon-50][cont] - delta * i;
                        phonSpread[phon][cont][offset-i] = AmbiguousPhonDefs[phon-50][cont] - delta * i;
                        
                        norm[phon] +=   phonSpread[phon][cont][offset+i]*phonSpread[phon][cont][offset+i] + 
                                        phonSpread[phon][cont][offset-i]*phonSpread[phon][cont][offset-i];
                    }
                }
            }
        }
    }
     */
}
/*
 * TraceNet.java
 *
 * Created on April 16, 2004, 5:18 PM
 */
 
package edu.uconn.psy.jtrace.Model;
import java.util.*;

/**
 * 
 *  The code implementing the TRACE model is located in this class.
 *  Many of the instance variables are stored in TraceParam.
 *  This class is responsible for computing one processing cycle at a time,
 *  wherein an entire simulation consists of multiple cycles. TraceSim
 *  is responsible for cycling this class and storing the data that results.
 *
 *  In order to quantitatively replicate the original TRACE model's performance,
 *  the original C code has been copied as closely as possible.  We have even 
 *  retained some bizarre variable names in case anyone wants to refer to the 
 *  original code.  If there are bugs in the C code, they have been replicated 
 *  here.  Comments attempt to clarify the flow of activation values.
 *  
 */
public class TraceNet {
    private int nwords; //number of words, calculated from the lexicon
    private int inputSlice = 0; //
    private double inputLayer[][], featLayer[][], phonLayer[][], wordLayer[][]; //current activation values
    private double featNet[][], phonNet[][], wordNet[][]; //used during processing to store intermediate states
    private int fSlices, pSlices, wSlices; //width of arrays (number of slices)
    double phonarr[][][]; //phoneme representations, fetched from TracePhones
    private TracePhones pd = null; //phoneme representations
    private TraceParam tp = null; //parameters for this net
    private TraceError terr = null;
    private double pOverlap;        //holds the overlap between instances of phoneme detectors, similar to <NPNODESp,ps>
    private double pww[][], wpw[][], pfw[][][], fpw[][][]; //represents the extent to which a unit can activate a non-overlapping unit on a different layer.
    private double wpdur = 2; //constant replicated cTrace
    private double pdur = 2; //constant replicated from cTrace
    private int __nreps;
            
    //public double[] reportCompetition; //tmp
    //public double[][] reportCompetition2; //tmp
    edu.uconn.psy.jtrace.IO.WTFileWriter diagnosticFileWriter; //tmp
    public String inputstring; //tmp    
    
    private edu.uconn.psy.jtrace.Model.GaussianDistr stochasticGauss; //gaussian noise applied to processing layers
    private edu.uconn.psy.jtrace.Model.GaussianDistr inputGauss; //gaussian noise applied to input representation
    
    private double atten=1d;
    private double bias=0d;
    private double learningrate=0d;    
    
    private boolean length_normalization;
    private double length_normalization_fulcrum;
    private double length_normalization_scale;
    double globalPhonemeCompetitionIndex;
    double globalLexicalCompetitionIndex;
    /** Creates a new instance of TraceNet */
    public TraceNet(TraceParam tp) {
        terr = new TraceError();
        if(tp == null){
            terr.report("Fatal Error: TraceParam = null passed to TraceNet");
            return; //fatal error
        }
        this.tp = tp;
        
        reset();
        
        
    } 
    //public TraceNet(){this(new TraceParam());}
    
    /**
     * Resets the net to its initial state, using the existing parameters.
     */
    
    public void reset()
    {
        if(tp.getLengthNormalization()==0)
            length_normalization=false;
        else
            length_normalization=true;
        //length_normalization_fulcrum=4.5;
        // a clever way to guess the optimal 'fulcrum' of length normalization
        length_normalization_scale=1/tp.getLexicon().getMeanWordLength();
        //System.out.println("length_normalization_scale:"+length_normalization_scale);
        inputSlice = 0;
        
        if(tp.getNoiseSD()!=0)  inputGauss=new edu.uconn.psy.jtrace.Model.GaussianDistr(0.0, tp.getNoiseSD());
        if(tp.getStochasticitySD()!=0) stochasticGauss=new edu.uconn.psy.jtrace.Model.GaussianDistr(0.0, tp.getStochasticitySD());
        
        if(tp.getAtten()!=1d) atten=tp.getAtten();
        if(tp.getBias()!=0d) bias=tp.getBias();
        if(tp.getLearningRate()!=0d) learningrate=tp.getLearningRate();        
        
        pd = tp.getPhonology();
        pd.compileAll();
        nwords = tp.getNWords();
        fSlices = tp.getFSlices();
        pSlices = fSlices / tp.getSlicesPerPhon(); 
        wSlices = pSlices; //currently word slices and phoneme slices are aligned 1:1
        inputLayer = new double[pd.NFEATS*pd.NCONTS][fSlices];
        featLayer = new double[pd.NFEATS*pd.NCONTS][fSlices];
        featNet = new double[pd.NFEATS*pd.NCONTS][fSlices];        
        phonLayer = new double[pd.NPHONS][pSlices];
        phonNet = new double[pd.NPHONS][pSlices];
        wordLayer = new double[nwords][wSlices];
        wordNet = new double[nwords][wSlices];
        
        pww = new double[pd.NPHONS][4];
        wpw = new double[pd.NPHONS][4];        
        fpw = new double[pd.NPHONS][pd.NCONTS][];
        pfw = new double[pd.NPHONS][pd.NCONTS][];        
        
        for(int p=0;p<pd.NPHONS;p++)
            for(int c=0;c<pd.NCONTS;c++){        
                fpw[p][c]= new double[tp.getSpread()[c]*2];
                pfw[p][c]= new double[tp.getSpread()[c]*2];
            }        
        // if there is a phoneme continuum defined in the parameters, create it here.
        if(tp.getContinuumSpec().trim().length()==3){
            int step=(new Integer(new Character(tp.getContinuumSpec().trim().charAt(2)).toString())).intValue();
            if(step>1&&step<10)
                try
                {
                    pd.makePhonemeContinuum(tp.getContinuumSpec().trim().charAt(0),tp.getContinuumSpec().trim().charAt(1),step);
                }
                catch (TraceException te)
                {
                    System.out.println("Problems in makePhonemeContinuum");
                }
        }
        
        try {
            pd.spreadPhons(tp.getSpread(), tp.getSpreadScale(), tp.getMin(), tp.getMax());
        }
        catch(TraceException td) {
            report(td.getMessage());
            return;
        }
        
        //init feature layer to resting value
        double rest;
        rest = tp.clipWeight(tp.getRest().F);
        for(int fslice = 0 ; fslice < fSlices; fslice++)
            for(int feat = 0 ; feat < pd.NCONTS * pd.NFEATS ; feat++)
                featLayer[feat][fslice] = rest;
        
        //init phon layer to resting value
        rest = tp.clipWeight(tp.getRest().P);
        for(int slice = 0; slice < pSlices; slice++)
            for(int phon = 0; phon < pd.NPHONS; phon++)
                phonLayer[phon][slice] = rest;          
        
        //init word layer to resting value
        //Original frequency implementation from cTRACE is being dropped: 
        //  wp->base = rest[W] + fscale*log(1. + wordfreq[i]);
        rest = tp.clipWeight(tp.getRest().W);
        for(int wslice = 0; wslice < wSlices; wslice++)
            for(int word = 0; word < nwords; word++){
                wordLayer[word][wslice] = rest;                                
            }
        //frequency applied to the resting level of lexical items
        if(tp.getFreqNode().RDL_rest_s != 0){
            for(int wslice = 0; wslice < wSlices; wslice++)
                for(int word = 0; word < nwords; word++){
                    if(tp.getLexicon().get(word).getFrequency()>0)
                        wordLayer[word][wslice]+= tp.getFreqNode().applyRestFreqScaling(tp.getLexicon().get(word));                                
                }
        }
        
        //priming applied to the resting level of lexical items
        if(tp.getPrimeNode().RDL_rest_s != 0){
            for(int wslice = 0; wslice < wSlices; wslice++)
                for(int word = 0; word < nwords; word++){
                    if(tp.getLexicon().get(word).getPrime()>0)
                        wordLayer[word][wslice]+= tp.getPrimeNode().applyRestPrimeScaling(tp.getLexicon().get(word));                                
                }
        }
                
        double d, dist;
        int spread[] = tp.getSpread();
        pOverlap = tp.getMaxSpread() / tp.getSlicesPerPhon();        
        
        double denom=0;
        double ft;
        // from C code: tdur = (float)(PWIDTH + POVERLAP)*pp->wscale/FPP = (((6+6)*1)/3)=4
        double tdur = 4; 
        __nreps=tp.getNReps();
        if(__nreps<=0) __nreps=1;
    
        //calculate the pww and wpw arrays.
        //how much can a phoneme at slice 4 activate a word at slice 5?
        //the pww array contains scalars stating how much to scale down per offset slices.
        //the wpw array is the same idea, except for w->p connections.
        for(int phon = 0; phon < pd.NPHONS; phon++){
                denom = 0;
                for(int pslice = 0; pslice <= 4; pslice++){
                    ft =  ((tdur - Math.abs(2 - pslice))/ tdur);
                    denom += ft * ft;
                }        
                for(int pslice = 0; pslice < 4; pslice++){
                    ft =  ((tdur - Math.abs(2 - pslice))/ tdur);
                    pww[phon][pslice] = ft / denom;
                    wpw[phon][pslice] = (1 * ft) / denom;                     
                }                
        }
        
        //calculate fpw, pfw 
        //how much can a feature influence a phoneme if there are mis-aligned.
        //these arrays state how much to scale down per offset slice.
        int spr, ispr;
        ft=0;
        for(int phon = 0; phon < pd.NPHONS; phon++){            
            for(int cont = 0; cont < pd.NCONTS; cont++){
                denom=0;
                spr=tp.getSpread()[cont]*1; //1 is stand in for pp->wscale (?)
                ispr=spr;
                for(int fslice=0;fslice < 2*ispr;fslice++){
                    ft = (double)(((double)spr - Math.abs((double)ispr -(double)fslice))/(double)spr);
                    denom += ((double)ft * (double)ft);
                }
                for(int fslice=0;fslice < 2*ispr;fslice++){
                    pfw[phon][cont][fslice] = (double)(((double)spr - Math.abs((double)ispr - (double)fslice))/(double)spr);
                    fpw[phon][cont][fslice] = (double)pfw[phon][cont][fslice] * (double)(1/denom);                                        
                }                
            }            
        }        
    }
    
    public void resetNoise(){
        if(tp.getNoiseSD()!=0)  inputGauss=new edu.uconn.psy.jtrace.Model.GaussianDistr(0.0, tp.getNoiseSD());
        if(tp.getStochasticitySD()!=0) stochasticGauss=new edu.uconn.psy.jtrace.Model.GaussianDistr(0.0, tp.getStochasticitySD());        
    }    
    
    /** Create the input layer
     *  loop through all the phonemes, and copy the corresponding features to it.
     *  the offset for the phoneme should be used inorder to center
     **/
    
    /** Variables which have been left out from original trace, M&E did not use them:
     *  WEIGHTp(i),c,fs   STRENGTHp(i)   PEAKp(i)   SUSp(i)   RATEp(i)
     **/
    //converts the model input into a pseudo-spectral input representation, store in inputLayer
    public void createInput(String phons)
    {
        if(edu.uconn.psy.jtrace.UI.jTRACEMDI.properties.startupOptions!=null&&
                edu.uconn.psy.jtrace.UI.jTRACEMDI.properties.startupOptions.equals("-cmp"))
            System.out.println("input= "+phons);
        if(phons==null||phons.length()==0) phons="";
        phons=phons.trim();
        System.out.println(phons);
        
        //store the target:
        if(phons.equals("-")) inputstring=phons;
        else{ 
            try{
                java.util.StringTokenizer tk = new java.util.StringTokenizer(phons.trim(),"-");
                if(tk.hasMoreTokens())
                    inputstring=tk.nextToken();
                else
                    inputstring="---";
            }
            catch(java.util.NoSuchElementException nsee){
                nsee.printStackTrace();
                inputstring="---";
            }
        }
        //create the input layer.
        int phon, maxwidth, slice = 0, inputOffset, phonOffset;
        int i,t,cont, syntactic_incr;
        int deltaInput = tp.getDeltaInput();
        slice += deltaInput; //attempt to fix something.
        double phonSpread[][][] = pd.getPhonSpread(); //fetch phoneme representations, may contain ambiguous phonemes.
        double durationScalar[][] = pd.getDurationScalar(); //fetch phoneme duration scalars
        double ambigDurScalar[][] = pd.getAmbiguousDurScalars(); //ambig phoneme duration scalars
        // loop over phoneme input. go to next phoneme and step 6 slices. until the end of the input is reached or 
        // FSLICES is reached
        for(i = 0, syntactic_incr=0; (i < tp.inputLength()) && (slice < fSlices); i++) //next bit moved to for-body: , slice += deltaInput) 
        {
            // if we encounter a 'splice' phone, proceed accordingly
            if(phons.charAt(i+syntactic_incr)=='{'){
                //System.out.println("Splice phone: "+phons.substring(i,i+5));
                int p1 = pd.mapPhon(phons.charAt(1+i+syntactic_incr)); 
                int splicePoint = Character.getNumericValue(phons.charAt(2+i+syntactic_incr)); 
                int p2 = pd.mapPhon(phons.charAt(3+i+syntactic_incr)); 
                
                // first half of the spliced phoneme.
                inputOffset = slice - pd.getSpreadOffset()[p1];
                for(t=inputOffset, phonOffset = 0; t < inputOffset + splicePoint; t++, phonOffset++)
                    for(cont = 0; cont < pd.NFEATS * pd.NCONTS; cont++)
                        if(t >= 0 && t < fSlices){
                            //System.out.println("tn:"+cont+","+t+","+phon+","+phonOffset);
                            inputLayer[cont][t] += phonSpread[p1][cont][phonOffset];                         
                        }
                // second half of the spliced phoneme.
                //inputOffset = slice - pd.getSpreadOffset()[p2];
                for(t=inputOffset+splicePoint, phonOffset = splicePoint; t < inputOffset + pd.getSpreadOffset()[p2]*2; t++, phonOffset++)
                    for(cont = 0; cont < pd.NFEATS * pd.NCONTS; cont++)
                        if(t >= 0 && t < fSlices){
                            //System.out.println("tn283: inputLayer["+cont+"]["+t+"] += phonSpread["+p2+"]["+cont+"]["+phonOffset+"];");
                            inputLayer[cont][t] += phonSpread[p2][cont][phonOffset];                         
                        }
                //change this int to make sure that iteration through the input string works right.
                syntactic_incr+=4;
                slice += deltaInput;
            }
            // otherwise, we are dealing with a normal, or ambiguous phoneme input.
            else{ // normal phoneme
                phon = pd.mapPhon(phons.charAt(i+syntactic_incr));
                //System.out.println("phon->char "+phons.charAt(i+syntactic_incr)+"->"+phon);
                if(phon<50){
                    inputOffset = slice - Math.round((float)(pd.getSpreadOffset()[phon]));
                    //copy the spread phonemes onto the input layer (aligned correctly)
                    for(t=inputOffset, phonOffset = 0; t < inputOffset + (Math.round((float)pd.getSpreadOffset()[phon]*2)); t++, phonOffset++)
                        for(cont = 0; cont < pd.NFEATS * pd.NCONTS; cont++)
                            if(t >= 0 && t < fSlices){
                                //durationScalar[phon][(int)(cont/pd.NFEATS)];
                                inputLayer[cont][t] += phonSpread[phon][cont][phonOffset];                         
                            }
                    // duration scaling!
                    slice += (int)Math.round((float)(deltaInput * durationScalar[phon][0]));
                }
                else{ //ambiguous phoneme
                    inputOffset = slice - Math.round((float)(pd.getAmbigSpreadOffset()[phon-50]));
                    //copy the spread phonemes onto the input layer (aligned correctly)
                    for(t=inputOffset, phonOffset = 0; t < inputOffset + (Math.round((float)(pd.getAmbigSpreadOffset()[phon-50]*2))); t++, phonOffset++) //&& phonOffset < phonSpread[phon][0].length
                        for(cont = 0; cont < pd.NFEATS * pd.NCONTS; cont++)
                            if(t >= 0 && t < fSlices){
                                //System.out.println("inputLayer["+cont+"]["+t+"] += phonSpread["+phon+"]["+cont+"]["+phonOffset+"];");
                                inputLayer[cont][t] += phonSpread[phon][cont][phonOffset];                         
                            }
                    // duration scaling!
                    slice += (int)Math.round((float)(deltaInput * ambigDurScalar[phon-50][0]));
                }
            }
            
        }
        //apply input noise here.
        if(tp.getNoiseSD()!=0d){ 
            for(int feat = 0; feat < pd.NCONTS*pd.NFEATS; feat++)
                for(int islice = 0; islice < fSlices; islice++){
                    inputLayer[feat][islice] += inputGauss.nextGauss();
                }
        }
        //make sure the input did not go out of bounds.
        for(int feat = 0; feat < pd.NCONTS*pd.NFEATS; feat++)
                for(int islice = 0; islice < fSlices; islice++){
                    inputLayer[feat][islice] = tp.clipWeight(inputLayer[feat][islice]);
                }
        //the next line copies one column of data, forcing the _feature layer_ to undergo one cycle immediately. 
        //this compensates for a discrepency between jTrace and cTrace; keeps things lined up.                
        initialSlice(); 
    }
    
    public double[][][] cycle() {                
        //int __nreps=tp.getNReps();
        //if(__nreps<=0) __nreps=1;
    
        //order of operation is critical here
        act_features();
        int cycles=tp.getNReps();
        if(cycles<0) cycles=Math.abs(cycles);
        else cycles=1;
        for(int j=0;j<cycles;j++){
            featToPhon();
            phonToPhon();
            //phonToFeat();  //not yet implemented correctly; no one has ever been interested in this aspect.
            phonToWord();
            wordToPhon();
            wordToWord();
            if(edu.uconn.psy.jtrace.UI.jTRACEMDI.properties.startupOptions!=null&&
                edu.uconn.psy.jtrace.UI.jTRACEMDI.properties.startupOptions.equals("-cmp"))
               System.out.println(globalPhonemeCompetitionIndex+"\t"+globalLexicalCompetitionIndex);
            featUpdate();
            phonUpdate();
            wordUpdate();                   
            //featurePhonemeWeightUpdate();
        }
        inputSlice += __nreps; //nrep steps in a cycle
        //array boundary check
        if(inputSlice >= fSlices) 
            inputSlice = fSlices-1;
        double D[][][]={wordLayer,featLayer,phonLayer,inputLayer};         
        return D;
    }
    
    //this method compensates for a small difference between jTrace and cTrace.
    //it is called during initialization
    public void initialSlice(){
        for(int c = 0;c < pd.NCONTS; c++)
            for(int f = 0;f < pd.NFEATS; f++)
                featNet[(c*pd.NFEATS)+f][0]+=inputLayer[(c*pd.NFEATS)+f][0];                   
        featUpdate();
        
    }
    
    //variable names taken from cTRACE.
    //input-to-feature activation, AND feature-to-feature inhibition.
    public void act_features(){
        double[][] fsum=new double[pd.NCONTS][fSlices]; //sum of prev slice's positive activations summed over each continuum at each fslice        
        double[][] ffi=new double[pd.NCONTS][fSlices]; //ff=[c][i]=fsum[c][i]*Gamma.F
        //computes total inhibition coming from a continuum to each node at that time slice
        for(int c = 0;c < pd.NCONTS; c++)  
            for(int f = 0;f < pd.NFEATS; f++)
                for(int fslice= -1;fslice < fSlices-1; fslice++)
                    if(featLayer[(c*pd.NFEATS)+f][fslice+1] > 0)
                        fsum[c][fslice+1] += featLayer[(c*pd.NFEATS)+f][fslice+1];
        //this block scales down the fsum value by Gamma.F
        for(int c = 0;c < pd.NCONTS; c++) 
            for(int fslice= -1;fslice < fSlices-1; fslice++)
                   ffi[c][fslice+1] = fsum[c][fslice+1] * tp.getGamma().F;        
        //this block copies input activations to the feature layer
        if(inputSlice < fSlices){ 
            for(int fIndex = 0;fIndex < pd.NCONTS*pd.NFEATS; fIndex++)
                for(int fslice = inputSlice; (fslice < fSlices-1) && (fslice < inputSlice + __nreps); fslice++){ //small variation from original
                    featNet[fIndex][fslice+1] += tp.clipWeight(tp.getAlpha().IF * inputLayer[fIndex][fslice+1]); //input->feature activation
                }
        }
        //this block applies ffi inhibition to each node in the featue layer, and compensates for self-inhibition
        for(int c = 0;c < pd.NCONTS; c++) 
            for(int f = 0;f < pd.NFEATS; f++)
                for(int fslice= -1;fslice < fSlices-1; fslice++)
                    if((ffi[c][fslice+1] - (featLayer[(c*pd.NFEATS)+f][fslice+1]*tp.getGamma().F)) > 0) 
                        featNet[(c*pd.NFEATS)+f][fslice+1] -= (ffi[c][fslice+1] - Math.max(0,(featLayer[(c*pd.NFEATS)+f][fslice+1]*tp.getGamma().F)));                            
    }    
    //feature to phoneme activations
    public void featToPhon(){
        int fspr, fmax, pstart, pend, winstart, c;
        double t;
        int FPP = tp.getSlicesPerPhon();
        //for every feature at every slice, if the units activation is above zero,
        //then send activation to phonNet from the featLayer scaled by PhonDefs, 
        //spread, fwp and alpha.
        for(int featIndex=0;featIndex<pd.NCONTS*pd.NFEATS;featIndex++){
            for(int fslice=0;fslice<fSlices;fslice++){
                if(featLayer[featIndex][fslice]>0){
                    //for all phonemes affected by the current feature.
                    //C code appears to ignore the first phoneme affected by feat (why?)
                    for(int phon=0;phon<pd.NPHONS;phon++){
                        //if the phoneme definition is blank here continue.
                        if(pd.PhonDefs[phon][featIndex]==0) continue;
                        //determine, based on current slice and spread, what range of
                        //phoneme units to send activation to.
                        fspr = tp.getSpread()[featIndex/pd.NFEATS];
                        fmax = fSlices - fspr;
                        if(fslice < fspr){
                            pstart = 0;
                            pend = (fslice + fspr - 1)/FPP;                            
                        }
                        else{
                            if(fslice > fmax) pend = pSlices - 1;
                            else pend = (fslice + fspr - 1)/FPP;
                            pstart = ((fslice - fspr)/FPP) + 1;
                        }
                        winstart = fspr - (fslice - (FPP*pstart));
                        
                        //include only positive acoustic evidence
                        if(featLayer[featIndex][fslice] > 0) 
                            t = pd.PhonDefs[phon][featIndex] * featLayer[featIndex][fslice] * tp.getAlpha().FP;
                        else 
                            t = 0;
                        
                        c = featIndex / pd.NFEATS;
                        for(int pslice=pstart;pslice<(pend+1)&&pslice<pSlices;pslice++){                            
                            //System.out.println(phon+"\t"+pslice+"\t"+phon+"\t"+c+"\t"+winstart);
                            phonNet[phon][pslice] += fpw[phon][c][winstart] * t; //crash here when FPP=1 (java.lang.ArrayIndexOutOfBoundsException: 14)
                            //winstart+=3; //changing this hard-coded line...
                            winstart+=FPP; //to this.  (seems to work 04/19/2007)
                        }
                    }
                }
            }
        }        
    }    
    /** calculate inhibitions in phoneme layer **/
    public void phonToPhon() {
        double inhibition;
        int ppeak, pmax, pmin, halfdur;
        halfdur=1; 
        double[] ppi=new double[pSlices];
        //the ppi accumulates all of the inhibition at a particular phoneme slice.
        //this amount of inhibition is later applied equally to all phonemes.        
        for(int slice=0;slice<pSlices;slice++)
            for(int phon=0;phon<pd.NPHONS;phon++)
                //if the phon unit has activation, determine its extent (does it hit an edge?) ...                
                if(phonLayer[phon][slice]>0){
                    pmax=slice+halfdur;
                    if(pmax>=pSlices){
                        pmax = pSlices-1;                    
                        pmin=slice-halfdur; 
                    }
                    else{ 
                        pmin=slice-halfdur; 
                        if(pmin < 0)
                            pmin=0;
                    }
                    //then add its activation to ppi, scaled by gamma.
                    for(int i=pmin;i<pmax;i++)
                        ppi[i]+=phonLayer[phon][slice]*tp.getGamma().P;                    
                }
        //now, determine again the extent of each phoneme unit,
        //then apply inhibition equally to phons lying on the same phon slice.
        globalPhonemeCompetitionIndex=0;
        for(int phon = 0; phon < pd.NPHONS; phon++){ //loop over phonemes             
            for(int slice = 0; slice < pSlices; slice++)  //loop over phoneme slices (original configuration 33)                
            {
                pmax=slice+halfdur;
                if(pmax>=pSlices){
                    pmax = pSlices-1;                    
                    pmin=slice-halfdur; 
                }
                else{ 
                    pmin=slice-halfdur; 
                    if(pmin < 0)
                        pmin=0;
                }
                for(int i=pmin;i<pmax;i++){
                    //application of inhibition occurs here
                    if(ppi[i]>0){
                        phonNet[phon][slice]-=ppi[i];                        
                        globalPhonemeCompetitionIndex+=ppi[i];
                    }
                }
                //here, we make up for self-inhibition, reimbursing nodes for inhibition that 
                //originated from themselves.
                if((phonLayer[phon][slice]*tp.getGamma().P)>0&&ppi[slice]>0){
                    phonNet[phon][slice]+=((pmax-pmin)*phonLayer[phon][slice])*tp.getGamma().P;                    
                    globalPhonemeCompetitionIndex-=((pmax-pmin)*phonLayer[phon][slice])*tp.getGamma().P;                    
                }
                //here, we make up for allophone-inhibition, reimbursing nodes for inhibition
                //that originate from allophones of the target, as defined in the allophon matrix.
                //note that this is an experimental feature of jtrace, implemented by tjs, 07/19/2007.
                for(int allophone = 0; allophone < pd.NPHONS; allophone++){ //loop over phonemes             
                    if(tp.getPhonology().getAllophoneRelation(phon, allophone)){
                        phonNet[phon][slice]+=((pmax-pmin)*phonLayer[allophone][slice])*tp.getGamma().P;                    
                    }
                }
            }            
        }
    }
    
    // feedback from phoneme to features - <PHONEXc,f,fs>
    // This is always off in the original trace.
    //TODO: this is not yet implemented correctly because it is never used.
    public void phonToFeat() {
        double activation;          
        int d, fpp = tp.getSlicesPerPhon();
        
            for(int fslice = 0; fslice < tp.getFSlices(); fslice++)
                for(int cont = 0; cont < pd.NCONTS; cont++ )  //loop over all continua (7)
                    for(int feat = 0 ; feat < pd.NFEATS; feat++)
                    {                
                        activation = 0;  //activation is basically <PFEXp,ps,c,f,fs>
                        for(int phon = 0; phon < pd.NPHONS; phon++) //loop over phonemes 
                            for(int pslice = 0; pslice < tp.getPSlices(); pslice++)  //loop over phoneme slices (original configuration 33)
                            {
	                        d = (int)Math.abs(pslice * fpp - fslice);
        	                if(d >= fSlices) d = fSlices - 1;
                                    if(phonLayer[phon][pslice] > 0) //aLPHA connections=only excitatory
                                        activation += pfw[phon][cont][d] * phonLayer[phon][pslice] * pd.PhonDefs[phon][cont * pd.NFEATS + feat];
                            }
                        featNet[cont * pd.NFEATS + feat][fslice] += tp.getAlpha().PF * activation;                    
                    }
    }     
    //This implementation actually depends on pdur being 2, re: pww dynamics.
    public void phonToWord() {
        double excitation = 0;
        edu.uconn.psy.jtrace.Model.TraceLexicon dict = tp.getLexicon();
        String str;     
        double t;
        int wpeak, wmin, winstart, wmax, pdur, strlen;
        //for each phoneme
        for(int phon=0;phon<pd.NPHONS;phon++){
            pdur=2;
            
            //hack
            if(tp.getDeltaInput()!=6||tp.getSlicesPerPhon()!=3)
                pdur=(int)Math.floor(tp.getDeltaInput()/tp.getSlicesPerPhon());
            //end hack
            
            //and for each phoneme slice
            for(int pslice=0;pslice<pSlices;pslice++){
                //if the current unit is below zero, skip it.
                if(phonLayer[phon][pslice]<=0) continue;
                //iterate over each word in the dictionary
                words: for(int word=0;word<dict.size();word++){                    
                    str = dict.get(word).getPhon();
                    strlen = str.length();
                    //for each letter in the current word
                    for(int offset=0;offset<strlen;offset++){
                        //if that letter corresponds to the phoneme we're now considering...
                         if(str.charAt(offset)==pd.toChar(phon)){
                             //then determine the temporal range of word units for which it
                             //makes sense that the current phoneme should send activation to it.
                             wpeak = pslice - (pdur * offset);
                             if(wpeak< -pdur) continue words;
                             wmin = 1 + wpeak - pdur; 
                             if(wmin < 0){
                                winstart = 1 - wmin;
                                wmin = 0;
                                wmax = wpeak + pdur;                                 
                             }
                             else{
                                 wmax = wpeak + pdur; 
                                 if(wmax > pSlices - 1)
                                     wmax = pSlices - 1;
                                 winstart = 1; 
                             }
                             //determine the raw amount of activation that is sent to the word units
                             t = 2 * phonLayer[phon][pslice] * tp.getAlpha().PW; //cTRACE: the 2 stands for word->scale
                             
                             double wfrq=0;
                             if(tp.getFreqNode().RDL_wt_s != 0&&dict.get(word).getFrequency()>0){
                               wfrq =  tp.getFreqNode().RDL_wt_s  * ((double)Math.log((double)dict.get(word).getFrequency()) * 0.434294482);
                             }
                             double wprm=0;
                             if(tp.getPrimeNode().RDL_wt_s != 0&&dict.get(word).getPrime()>0){
                               wprm =  tp.getPrimeNode().RDL_wt_s  * ((double)Math.log((double)dict.get(word).getPrime()) * 0.434294482);
                               //t = tp.getPrimeNode().applyWeightPrimeScaling(tp.getLexicon().get(word), t);                                
                             }
                             
                             double _t;
                             //now iterate over the temporal range determined about 15 lines above
                             for(int wslice = wmin; wslice<wmax && wslice<wSlices; wslice++, winstart++)
                                 if(winstart>=0 && winstart<4){
                                     //scale activation by pww; this determines how temporal offset should affect excitation                                        
                                     wordNet[word][wslice] += (1+wfrq+wprm) * pww[phon][winstart] * t;                                                                          
                                 }
                         }
                    }
                }
            }
        }        
    }    
    //word to word inhibition: operates the same as phoneme inhibition -- calculate
    //the total amount of inhibition at each slice and apply that equally to all words
    //that overlap with that slice somewhere.  this means that word length increases
    //the amount of lexical inhibition linearly.  
    public void wordToWord() {
        double[] wwi=new double[pSlices];
        double[] wisum=new double[pSlices];
        edu.uconn.psy.jtrace.Model.TraceLexicon dict=tp.getLexicon();        
        //reportCompetition2=new double[nwords][pSlices];
        int cmp_wmin=-1, cmp_wmax=-1;
        //for all word slices
        for(int wstart=0;wstart<pSlices;wstart++){
            //for all words
            for(int word=0;word<nwords;word++){
                //determine how many slices the current word lies on
                int wmin=wstart; //wstart - (1/2 phone width))
                if(wmin<0) wmin=0;
                int wmax=wstart+(dict.get(word).getPhon().length()*2); //!! wstart + (wlength*phone width) + (1/2 phone width)
                if(wmax>pSlices) wmax=pSlices-1;
                for(int l=wmin;l<wmax;l++){
                    //then add that word unit's activation to the wisum array,                    
                    if(wordLayer[word][wstart]>0){
                        wisum[l]+=wordLayer[word][wstart]*wordLayer[word][wstart];                                     
                    }
                }
            }       
        }
        //next, scale the wisum array by gamma, and it is now called the wwi array.
        //there is also a built-in ceiling here, preventing inhibition over 3.0d.
        for(int wstart=0;wstart<pSlices;wstart++){
            if(wisum[wstart] > 3.0d) wisum[wstart]=3.0d; 
            wwi[wstart]= wisum[wstart]*tp.getGamma().W;            
        }
        //now, repeat the looping over words and slices and apply the inhibition
        //accumulated at each slice to every word unit that overlaps with that slice.
        globalLexicalCompetitionIndex=0;
        for(int wstart=0;wstart<pSlices;wstart++){
            for(int word=0;word<nwords;word++){
                int wmin=wstart; //wstart - (1/2 phone width))
                if(wmin<0) wmin=0;
                int wmax=wstart+(dict.get(word).getPhon().length()*2); //!! wstart + (wlength*phone width) + (1/2 phone width)
                if(wmax>pSlices) wmax=pSlices-1;                                
                
                //length_normalization_scale = 1/(14  -dict.get(word).getPhon().length());
                //inhibition applied in this loop.
                for(int l=wmin;l<wmax;l++){
                    //EXTENSION
                    if(length_normalization){
                        double compensation_factor = 1 / (dict.get(word).getPhon().length() * length_normalization_scale);
                        if(compensation_factor>1) compensation_factor=1;
                                //double compensation_factor = (((dict.get(word).getPhon().length() / length_normalization_fulcrum ) - 1) * length_normalization_scale) + 1;
                                //if(compensation_factor<0) compensation_factor=1;
                        wordNet[word][wstart]-=wwi[l] * compensation_factor; //if(wwi[l]>0) //inhibition applied here                                                            
                        globalLexicalCompetitionIndex += wwi[l] * compensation_factor;
                    }
                    //END EXTENSION                    
                    else{
                        wordNet[word][wstart]-=wwi[l]; //if(wwi[l]>0) //inhibition applied here                                                            
                        globalLexicalCompetitionIndex += wwi[l];
                    }
                }
                //re-imbursement of self-inhibition occurs here.
                if(wordLayer[word][wstart]>0){  //self-inhibitiopn prevented here.
                    //EXTENSION                    
                    if(length_normalization){
                        double compensation_factor = 1 / (dict.get(word).getPhon().length() * length_normalization_scale);
                        if(compensation_factor>1) compensation_factor=1;
                        wordNet[word][wstart]+=((wmax-wmin)*(wordLayer[word][wstart]*wordLayer[word][wstart]*tp.getGamma().W)) * compensation_factor;                                                                          
                        globalLexicalCompetitionIndex -= ((wmax-wmin)*(wordLayer[word][wstart]*wordLayer[word][wstart]*tp.getGamma().W)) * compensation_factor;                                                                          
                    }
                    //END EXTENSION                    
                    else{
                        wordNet[word][wstart]+=((wmax-wmin)*(wordLayer[word][wstart]*wordLayer[word][wstart]*tp.getGamma().W));                                                                          
                        globalLexicalCompetitionIndex -= ((wmax-wmin)*(wordLayer[word][wstart]*wordLayer[word][wstart]*tp.getGamma().W));                                                                          
                    }
                }
            }
        }          
    }
    //lexical to phoneme feedback.
    public void wordToPhon() {
        //temporary monitoring variables
        int monitortarget=-1;
        int monitorslice=4;
        double[][][] monitorfdbk;        
        //initialize variables
        double excitation = 0;
        edu.uconn.psy.jtrace.Model.TraceLexicon dict = tp.getLexicon();
        int strlen, phon;
        String str;
        int wslot, t_o_p, pmin, pwin, pmax, currChar; 
        char t_c_p;
        //for every word in the lexicon
        for(int word = 0 ; word < dict.size(); word++){
            //for each word slice
            for(int wslice = 0; wslice < wSlices; wslice++){
                //if the word has activation above zero
                if(wordLayer[word][wslice]<=0) continue;
                //determine what range of slices (for that word unit) can be
                //fed back to the phoneme layer.
                str = dict.get(word).getPhon();                
                for(int wstart=0; wstart < str.length(); wstart++){
                    t_c_p = str.charAt(wstart);
                    currChar = pd.mapPhon(t_c_p);
                    wslot = wslice + (wstart*2);
                    pmin = wslot - 1; //??
                    if(pmin >= pSlices) break;
                    if(pmin < 0){
                        pwin = 1 - pmin;
                        pmin=0;
                        pmax = wslot + 2;  //from +2                        
                    }
                    else{
                        pmax = wslot + 2;  //from +2
                        if ( pmax > pSlices - 1)
			    pmax = pSlices - 1;                                                
                        pwin = 1;                        
                    }
                    
                    //now that we know the range to iterator over, iterate over the appropriate phoneme slices
                    for(int pslice = pmin ;pslice < pmax && pslice < pSlices && pwin<4; pslice++, pwin++){
                        //this check makes sure that ambiguous phonemes do not feedback
                        if(currChar > pd.NPHONS && currChar < 0){ 
                            int contIdx;
                            try{
                                contIdx=(new Integer(tp.getContinuumSpec().toCharArray()[2])).intValue();
                            } catch(Exception e){ e.printStackTrace(); contIdx=-1; }
                            if(contIdx==-1){ 
                                //there is something wrong with the input or the continuum.
                                //feedback will not be calculated for this character.
                                break;
                            }       
                            else{
                               if(currChar==50){ //this is the bottom of the continuum.
                                   currChar = pd.mapPhon(tp.getContinuumSpec().toCharArray()[0]);                                   
                               }
                               else if(currChar==(50+contIdx-1)){ //this is the top of the continuum
                                   currChar = pd.mapPhon(tp.getContinuumSpec().toCharArray()[2]);                                   
                               }
                               else{ //in the middle of the continuum
                                   //feedback will not be accumulated for any ambiguous phonemes representations
                                   break;
                               }
                            }
                        }
                        //if the current word activation is above zero
                        if(wordLayer[word][wslice] > 0){
                                //if lexical frequency is in effect.                            
                                //if(tp.getFreqNode().RDL_wt_s!=0&&dict.get(word).getFrequency()>0){
                            
                            double wfrq=0;
                            if(dict.get(word).getFrequency()>0&&tp.getFreqNode().RDL_wt_s>0)
                                wfrq = tp.getFreqNode().RDL_wt_s  * (Math.log(0 + dict.get(word).getFrequency()) * 0.434294482);                                            
                            double wprim=0;
                            if(dict.get(word).getFrequency()>0&&tp.getPrimeNode().RDL_wt_s>0)
                                wprim = tp.getPrimeNode().RDL_wt_s  * (Math.log(0 + dict.get(word).getPrime()) * 0.434294482);                
                            
                            //scale the activation by alpha and wpw
                            phonNet[currChar][pslice] += (1 + wfrq + wprim) * wordLayer[word][wslice] * tp.getAlpha().WP * wpw[currChar][pwin]; 
                                                        
                        }
                    }
                }                
            }
        }
        //output monitored feedback        
        /*String contents;
        double notzero;
        for(int h=0;h<monitorfdbk.length;h++){
            contents="";
            for(int i=0;i<monitorfdbk[0].length;i++){
                notzero=0;
                for(int j=0;j<monitorfdbk[0][0].length;j++)
                    if(monitorfdbk[h][i][j]>notzero) notzero=monitorfdbk[h][i][j];
                if(notzero==0) continue;
                contents+="\\N\t"; 
                contents+=tp.getAlpha().WP+"\t"; // alpha
                contents+=inputstring+"\t"; // input string  
                contents+=tp.getLexicon().get(h).getPhon()+"\t"; // feedbackER
                contents+=monitorslice+"\t"; //left edge of targ word
                contents+=(monitorslice+(tp.getLexicon().get(monitortarget).getPhon().length()*2)-1)+"\t"; //right edge of targ word        
                contents+=inputSlice+"\t"; //input cycle
                contents+=pd.toChar(i)+"\t"; // feedbackEE        
                for(int j=0;j<monitorfdbk[0][0].length;j++)
                    contents+=monitorfdbk[h][i][j]+"\t";
                contents+="\n";
            }
            //diagnosticFileWriter.write(contents);                        
        }    */    
    }
    //final processing of feature units incorporates stochasticity (if on) and
    //implements decay to resting level behavior.
    public void featUpdate() {
        double diff, rest, t, tt;        
        double min=tp.getMin();
        double max= tp.getMax();
        for(int slice = 0; slice < fSlices; slice++){
            for(int feat = 0; feat < pd.NFEATS*pd.NCONTS; feat++ )
            {   
                if(tp.getStochasticitySD()!=0d){ //apply gaussian noise here
                    featNet[feat][slice] += stochasticGauss.nextGauss(); //this adds the noise                    
                }
        
                t = featLayer[feat][slice];
                if(featNet[feat][slice] > 0)
                    t+= (max-t)*featNet[feat][slice];                
                else if(featNet[feat][slice] < 0)
                    t += (t-min)*featNet[feat][slice];                    
                tt = featLayer[feat][slice] - tp.getRest().F;
                //if(t!=0)
                t -= tp.getDecay().F * tt;
                if(t > max) t = max;
                if(t < min) t = min;
                //final update for feature layer
                featLayer[feat][slice] = t;                
            }
        }
        featNet = new double[pd.NFEATS*pd.NCONTS][fSlices];                        
    }    
    
    //final processing of phoneme units incorporates stochasticity (if on) and
    //implements decay to resting level behavior.    
    public void phonUpdate() {
        double diff, rest;        
        for(int pslice = 0; pslice < pSlices; pslice++)
            for(int phon = 0; phon < pd.NPHONS; phon++ )
            {
                if(tp.getStochasticitySD()!=0d){ //apply gaussian noise here
                    phonNet[phon][pslice] += stochasticGauss.nextGauss(); //this adds the noise                    
                }
                
                if(phonNet[phon][pslice] >= 0)
                    diff = tp.getMax() - phonLayer[phon][pslice];
                else
                    diff =  phonLayer[phon][pslice] - tp.getMin();
                
                rest = phonLayer[phon][pslice] - tp.getRest().P;

                //final update for phoneme layer 
                phonLayer[phon][pslice] += (diff * phonNet[phon][pslice]) - (tp.getDecay().P * rest);
                phonLayer[phon][pslice] = tp.clipWeight(phonLayer[phon][pslice]);                            
            }
        phonNet = new double[pd.NPHONS][pSlices];        
    }
    
    
     //final processing of word units incorporates stochasticity (if on) and
    //implements decay to resting level behavior.    
    public void wordUpdate(){            
        double t, tt, max, min;
        min=tp.getMin();
        max= tp.getMax();
        for(int word = 0; word < nwords; word++ )
            for(int slice = 0; slice < wSlices; slice++){
                
                //apply attention modulation (cf. Mirman et al., 2005)
                if(atten!=1d)
                    wordNet[word][slice] *= atten;
                if(bias!=0d)
                    wordNet[word][slice] -= bias;
                    
                if(tp.getStochasticitySD()!=0d){ //apply gaussian noise here
                    wordNet[word][slice] += stochasticGauss.nextGauss(); //this adds the noise                    
                }
                                
                t = wordLayer[word][slice];
                if( wordNet[word][slice] > 0)
                    t += (max - t) * wordNet[word][slice];
                else if( wordNet[word][slice] < 0)
                    t += (t - min) * wordNet[word][slice];
                //resting prime & resting freq effects
                if(tp.getFreqNode().RDL_rest&&tp.getLexicon().get(word).getFrequency()>0&&tp.getPrimeNode().RDL_rest&&tp.getLexicon().get(word).getPrime()>0)
                    tt = wordLayer[word][slice] - ((tp.getRest().W + tp.getFreqNode().applyRestFreqScaling(tp.getLexicon().get(word))) + (tp.getRest().W + tp.getPrimeNode().applyRestPrimeScaling(tp.getLexicon().get(word))));                
                //resting freq effects
                else if(tp.getFreqNode().RDL_rest&&tp.getLexicon().get(word).getFrequency()>0)
                    tt = wordLayer[word][slice] - (tp.getRest().W + tp.getFreqNode().applyRestFreqScaling(tp.getLexicon().get(word)));                
                //resting prime 
                else if(tp.getPrimeNode().RDL_rest&&tp.getLexicon().get(word).getPrime()>0)
                    tt = wordLayer[word][slice] - (tp.getRest().W + tp.getPrimeNode().applyRestPrimeScaling(tp.getLexicon().get(word)));                
                //no resting prime or resting freq effects
                else
                    tt = wordLayer[word][slice] - tp.getRest().W;
                //if(tt != 0)
                t -= tp.getDecay().W * tt;                                
                
                if(t > max) t = max;
                if(t < min) t = min;                
                wordLayer[word][slice] = t;
            }
        wordNet = new double[nwords][wSlices];        
    }    
    
    // ** START DAN MIRMAN'S FEATURE-PHONEME WEIGHT MOD ** //
    public void featurePhonemeWeightUpdate(){
        return;
        
        /*
        //from interact.c
        fpwtupdate(){
         int p, i;
         int c, v, windex;
         double weightchange, weightsum, fetact;
         double weightsum2[NCONTINS], nfeat[NCONTINS];
         
         PHONEME *pp;
         FEATURE *fp;
         //Dan's Hebbian weight update function

         for (p=0;p<PSLICES;p++){
           for ALLPHONEMES {
             //add up weights to get normalization factor
             weightsum = 0.0;
             for(i=0;i<NFVALS*NCONTINS;i++){
               weightsum += pp->feature[i];
             }
             //do continuum-wise normalization
             for(c=0; c<NCONTINS; c++){
               nfeat[c]=0.0;
               weightsum2[c]=0.0;
               for(v=0; v<NFVALS; v++){
                 fp=&feature[c][v];
                 if(fp->ex[p*FPP]>0){
                     nfeat[c]+=fp->ex[p*FPP]; //sum per continuum
                 }
                 windex=v+(c*NFVALS);
                 weightsum2[c]+=pp->feature[windex]; //sum of weights
                }//for feature values
               //if(nfeat[c]==0){nfeat[c]=1;}
                if(weightsum2[c]==0){weightsum2[c]=1;} //avoid divide by zero
               }
             //calculate weight change and make it
             if(pp->ex[p] > 0.0){
               //in principle, should loop through feature slices that feed into this phoneme
               //for now, just focus on the center ("fpeak")
               //not weighted by distance, because only exact matches are in play for now
               for(i=0, fp=&feature[0][0];i<NFVALS*NCONTINS;i++,fp++){
                 fetact=fp->ex[p*FPP];
                 if(fetact<0){fetact=0;} //clip feature activation for learning
                 v=i%NFVALS;
                 c=(i-v)/NFVALS;
                 if(nfeat[c]>0){ //only do weight change if there is activity in the feature continuum -- this should get replaced by temporally asynchronous feature summing-normalizing approach
                   weightchange = (pp->ex[p])*(fetact/nfeat[c]-(pp->feature[i])/weightsum2[c]);
                   if (weightchange>.1 || weightchange<-.1){
                     // fprintf(outjunk2, "Weight %d of %c from %1.3f (/%1.3f) by %1.3f due to feature act %1.3f (/%1.3f).\n", i, pp->sound, pp->feature[i],  weightsum2[c], weightchange, fetact, nfeat[c]);
                   }
                   pp->feature[i] += lrate*weightchange;
                 }
               }//for feature units
             } //if phoneme is active
           } //for ALLPHONEMES
         } //for all PSLICES         
        }
         */
    }    
    // ** END DAN MIRMAN'S FEATURE-PHONEME WEIGHT MOD ** //
    
    public void gc(){
        inputLayer=null;
        featLayer=null;
        phonLayer=null;
        wordLayer=null;
        featNet=null;
        phonNet=null;
        wordNet=null;        
        pww=null;
        wpw=null;
        pfw=null;
        fpw=null;
    }
    //Error Handling code
    public void report(String report) {
        terr.report(report);
    }
    public TraceParam getParameters(){return tp;}
    public int getInputSlice(){return inputSlice;}    
    public void setInputSlice(int i){inputSlice=0;}
    public String toString(){return "this is an object of type TraceNet.";}
    public boolean isReady(){return true;}
    public String isReadyVerbose(){return " ready ... ";}    
    public double[][] getInputLayer() {return inputLayer;}
    public void setInputLayer(double[][] _l){inputLayer=_l;}
    public double[][] getFeatureLayer() {return featLayer;}
    public void setFeatureLayer(double[][] _l){featLayer=_l;}
    public double[][] getPhonemeLayer() {return phonLayer;}
    public void setPhonemeLayer(double[][] _l){phonLayer=_l;}
    public double[][] getWordLayer() {return wordLayer;}
    public void setWordLayer(double[][] _l){wordLayer=_l;}
    public TracePhones getPhonDefs() {return pd;}
    public double[][] getFeatLayer() {return featLayer;}
    public double[][] getPhonLayer() {return phonLayer;}
    public TraceParam getParam() {return tp;}
    public double getGlobalLexicalCompetition(){return globalLexicalCompetitionIndex;}
    public double getGlobalPhonemeCompetition(){return globalPhonemeCompetitionIndex;}
    public double[][] clearArray(double[][] d){
        for(int i=0;i<d.length;i++)
            for(int j=0;j<d[0].length;j++)
                d[i][j]=0d;
        return d;
    }
    //rounds a double to the third significant digit
    private double roundingOp(double x){
        double y;
        String str=(new Double(x)).toString();
        if(str.length()<=5) return x;
        if(x<0.001&&x>0.0005) return 0.001d;
        if(x<0.0005) return 0.0d;
        y=((new Double(str.substring(0,6))).doubleValue())*1000;
        y=((double)Math.round((float)y))/1000;
        return y;
    }
    
    // ** START ALTERNATE IMPLEMENTATIONS **
    
    /** calculate inhibitions in phoneme layer using an alternate
     mothod, more akin to activation **/
    /*public void phonToPhonLambda() {
        double excitation = 0;
        edu.uconn.psy.jtrace.Model.TraceLexicon dict = tp.getLexicon();
        String str;     
        double t;
        int peak, min, start, max, pdur;
        //for each phoneme
        for(int phon=0;phon<pd.NPHONS;phon++){
            pdur=2;
            //and for each phoneme slice
            for(int pslice=0;pslice<pSlices;pslice++){
                //if the current unit is below zero, skip it.
                if(phonLayer[phon][pslice]<=0) continue;   
                //
                phon2 : for(int phon2=0;phon2<pd.NPHONS;phon2++){
                    //
                    //for(int pslice2=0;pslice2<pSlices;pslice2++){
                        //if phon1 corresponds to the phoneme we're now considering, pon2 ...
                         //if(pd.toChar(phon)==pd.toChar(phon2)){
                             //then determine the temporal range 
                             //...
                             //wpeak = pslice - (pdur * offset);                             
                             //peak = pslice - (pdur * Math.abs(pslice-pslice2));
                             
                             peak = pslice;
                             if(peak < -pdur) continue phon2;
                             min = 1 + peak - pdur; 
                             if(min < 0){
                                start = 1 - min;
                                min = 0;
                                max = peak + pdur;                                 
                             }
                             else{
                                 max = peak + pdur; 
                                 if(max > pSlices - 1)
                                     max = pSlices - 1;
                                 start = 1; 
                             }
                             //determine the raw amount of activation that is sent to the word units
                             t = 2 * phonLayer[phon][pslice] * tp.getLambda().P; //the 2 stands for word->scale
                             
                             double _t;
                             //now iterate over the temporal range determined about 15 lines above
                             for(int slice = min; slice<max && slice<pSlices; slice++, start++){
                                 if(phon==phon2 && slice==start) continue; //cancel self-inhibition
                                 if(start>=0 && start<4){
                                         phonNet[phon2][slice] -= pww[phon][start] * t;                                     
                                 }
                             }
                         }
                    //}
                //}
            }
        } 
    } */ 
    /*
    //alternate lexical inhibition calculation
    public void wordToWordLambda() {
        //initialize variables
        double excitation = 0;
        edu.uconn.psy.jtrace.Model.TraceLexicon dict = tp.getLexicon();
        int strlen, phon;
        String str,str2;
        int wslot, t_o_p, wmin, wwin, wmax, currChar; 
        char t_c_p;
        //for every word in the lexicon
        for(int word = 0 ; word < dict.size(); word++){
            //for each alignment of the word
            for(int wslice = 0; wslice < wSlices; wslice++){
                if(tp.getLambda().W==0) return;
                //if the word has activation above zero
                if(wordLayer[word][wslice]<=0) continue;
                //determine what range of alignments are ...
                str = dict.get(word).getPhon();                
                for(int wstart=0; wstart < str.length(); wstart++){
                    t_c_p = str.charAt(wstart);
                    currChar = pd.mapPhon(t_c_p);
                    wslot = wslice + (wstart*2);
                    wmin = wslot - 1; //??
                    if(wmin >= wSlices) break;
                    if(wmin < 0){
                        wwin = 1 - wmin;
                        wmin=0;
                        wmax = wslot + 2;  //from +2                        
                    }
                    else{
                        wmax = wslot + 2;  //from +2
                        if ( wmax > wSlices - 1)
			    wmax = wSlices - 1;                                                
                        wwin = 1;                        
                    }   
                    //this check makes sure that ambiguous phonemes DO NOT inhibit
                    //is this the correct behavior?  you decide.
                    if(currChar > 14 && currChar < 0){ 
                        int contIdx;
                        try{
                            contIdx=(new Integer(tp.getContinuumSpec().toCharArray()[2])).intValue();
                        } catch(Exception e){ e.printStackTrace(); contIdx=-1; }
                        if(contIdx==-1){ 
                            //there is something wrong with the input or the continuum.                                
                            break;
                        }       
                        else{
                           if(currChar==50){ //this is the bottom of the continuum.
                               currChar = pd.mapPhon(tp.getContinuumSpec().toCharArray()[0]);                                   
                           }
                           else if(currChar==(50+contIdx-1)){ //this is the top of the continuum
                               currChar = pd.mapPhon(tp.getContinuumSpec().toCharArray()[2]);                                   
                           }
                           else{ //in the middle of the continuum
                               //feedback will not be calculated for any ambiguous phonemes
                               break;
                           }
                        }
                    }
                    double difference, phondistance;
                    //now that we know the range to iterate over, iterate over the appropriate phoneme slices
                    for(int targslice = wmin-1 ;targslice < wmax && targslice < wSlices && wwin<4; targslice++, wwin++){                        
                        //if the current word activation is above zero                        
                        if(targslice<0) continue;
                        if(wordLayer[word][wslice] > 0){
                            //for every word in the lexicon
                            //:: inhibit dis-similar words scaled by overlap; closer-similar sounding words are inhibited less
                            for(int word2 = 0 ; word2 < dict.size(); word2++){
                                //if the inhibitED word has activation below zero
                                if(wordLayer[word2][targslice]<=0) continue;
                                if(word==word2&&wslice==targslice) continue; //cancel self-inhibition
                                str2 = dict.get(word2).getPhon();                                                    
                                //define simple similarity metric that permit uniform comparison between 
                                //different length items
                                if(wwin<=3&&wwin>=0){
                                    phondistance = (wslice-targslice)/this.pdur;
                                    wordNet[word2][targslice] -= wordLayer[word][wslice] * wpw[currChar][wwin] * difference_2(str,str2,phondistance) * tp.getLambda().W;                                 
                                }
                                else{
                                    //wordNet[word2][targslice] -= wordLayer[word][wslice] * difference * tp.getLambda().W;                                 
                                }
                                //for(int wstart2=0; wstart2 < str2.length(); wstart2++){
                                    //if characters mis-match, and line up temporally within the wpw matrix, then inhibit 
                                //    if(str.charAt(wstart)!=str2.charAt(wstart2)&&(wwin+(wstart2*2))<=3&&(wwin+(wstart2*2))>=0){
                                //        wordNet[word2][targslice] -= wordLayer[word][wslice] * (1/wpw[currChar][wwin+(wstart2*2)]) * tp.getLambda().W;                                 
                                //    }
                                //    else if(str.charAt(wstart)!=str2.charAt(wstart2)){
                                //        wordNet[word2][targslice] -= wordLayer[word][wslice] * tp.getLambda().W;                                 
                                //    }
                                //    else{ //matching characters do not trigger inhibition                                                                                                                
                                //}                                
                            }
                        }
                    }
                }                
            }
        }                      
    }*/
    //define a simple lexical simlarity metric that 
    //deals with word length in a straight-forward way  
    //dist is the number of phonemes offset between the two words.
    /*
     *this version defines difference as follows:
     *take the section of the two words that overlap temporally.
     *calculate the rmse of each pair of overlapping phonemes
     *compute running average of rmse, giving greater weight to onset
     *non-overlapping portions are ignored.
     */
    /*private double difference(String s1,String s2,double dist){
        //boolean b = Pattern.matches("a*b", "aaaaab");
        if(dist>0)
            dist = Math.ceil(dist);
        else if(dist<0)    
            dist = Math.floor(dist);        
        if(dist>0){
            for(int i=0;i<dist;i++)
                s1 = " ".concat(s1);
        }
        else if(dist<0){
            for(int i=0;i>dist;i--)
                s2 = " ".concat(s2);
        } 
        //t = pd.PhonDefs[phon][featIndex] * featLayer[featIndex][fslice] * tp.getAlpha().FP;
        //char[] c1=s1.toCharArray();
        //char[] c2=s2.toCharArray();
        int phon1;
        int phon2;
        int len=Math.min(s1.length(),s2.length());
        double difference=0;
        for(int i=len-1;i>=0;i--){
            if(s1.charAt(i)==' '||s2.charAt(i)==' ') continue;
            phon1 = pd.mapPhon(s1.charAt(i));
            phon2 = pd.mapPhon(s2.charAt(i));                    
            double rmse=0;
            for(int j=0;j<pd.NPHONS;j++)
                for(int k=0;k<pd.NFEATS*pd.NCONTS;k++)
                    rmse+=Math.abs(pd.PhonDefs[phon1][k]-pd.PhonDefs[phon2][k]);
            rmse/=pd.NFEATS*pd.NCONTS;
            rmse=Math.sqrt(rmse);
            difference+=rmse;
            if(i<len-1)
                difference/=2;
        }
        //System.out.println("dif\t"+s1+"\t"+s2+"\t"+difference);
        return difference;
    }*/
    /*
     *this version defines difference as follows:
     *take the section of the two words that overlap temporally.
     *for each mismatching phoneme add 1 to difference score
     *average over length of overlapping region
     *non-overlapping portions are ignored.
     */
    private double difference_2(String s1,String s2,double dist){
        //boolean b = Pattern.matches("a*b", "aaaaab");
        if(dist>0)
            dist = Math.ceil(dist);
        else if(dist<0)    
            dist = Math.floor(dist);        
        if(dist>0){
            for(int i=0;i<dist;i++)
                s1 = " ".concat(s1);
        }
        else if(dist<0){
            for(int i=0;i>dist;i--)
                s2 = " ".concat(s2);
        } 
        //t = pd.PhonDefs[phon][featIndex] * featLayer[featIndex][fslice] * tp.getAlpha().FP;
        //char[] c1=s1.toCharArray();
        //char[] c2=s2.toCharArray();
        int phon1;
        int phon2;
        int len=Math.min(s1.length(),s2.length());
        double difference=0;
        for(int i=len-1;i>=0;i--){
            if(s1.charAt(i)==' '||s2.charAt(i)==' ') continue;
            phon1 = pd.mapPhon(s1.charAt(i));
            phon2 = pd.mapPhon(s2.charAt(i));                    
            if(phon1!=phon2)
                difference++;            
        }        
        difference/=len;
        //System.out.println("dif\t"+s1+"\t"+s2+"\t"+difference);
        return difference;
    }
    
    // ** END ALTERNATE IMPLEMENTATIONS **
}
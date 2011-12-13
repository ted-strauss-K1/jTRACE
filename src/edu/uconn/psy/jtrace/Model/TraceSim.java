/*
 * TraceSim.java
 *
 * Created on May 4, 2005, 4:34 PM
 */

package edu.uconn.psy.jtrace.Model;

import edu.uconn.psy.jtrace.Model.*;
import edu.uconn.psy.jtrace.Model.Dynamics.*;
import java.io.*;       // for export
import java.text.DecimalFormat;

/**
 * Wrapper around TraceNet, with the full history of the sim, etc.
 *
 * @author Harlan Harris
 */
public class TraceSim {
    
    private TraceNet tn;
    private TraceParam tp;
    
    // full history of TraceNet object
    private double [][][] inputD;
    private double [][][] featureD;
    private double [][][] phonemeD;
    private double [][][] wordD;
    
    private double [] globalLexicalCompetition;
    private double [] globalPhonemeCompetition;
    
    // computed maximum size of various things
    private int maxDuration;

    /**
     * Holds value of property paramUpdateCt.
     */
    private long paramUpdateCt;

    /**
     * Holds value of property stepsRun.
     */
    private int stepsRun;

    /**
     * Utility field used by bound properties.
     */
    private java.beans.PropertyChangeSupport propertyChangeSupport =  new java.beans.PropertyChangeSupport (this);

    /**
     * Holds value of property inputString.
     */
    private String inputString;

    /**
     * Getter for property stepsRun.
     * @return Value of property stepsRun.
     */
    public int getStepsRun() {

        return this.stepsRun;
    }

    /**
     * Getter for property inputString.
     * @return Value of property inputString.
     */
    public String getInputString() {

        return this.inputString;
    }

    /**
     * Setter for property inputString.
     * @param inputString New value of property inputString.
     */
    public void setInputString(String inputString) {

        this.inputString = inputString;
    }

    /**
     * Getter for property paramUpdateCt.
     * @return Value of property paramUpdateCt.
     */
    public long getParamUpdateCt() {

        return this.paramUpdateCt;
    }
    
    
    /** Creates a new instance of TraceSim */
    public TraceSim(TraceParam _tp) {
        // initialize param and net objects
        tp = _tp;
        tn = new TraceNet(tp);
        
        paramUpdateCt = tp.getUpdateCt();
        
        reset();
    }
    public TraceSim clone(){        
        TraceSim _sim = new TraceSim((TraceParam)tp.clone());    
        _sim.cycle(stepsRun);
        return _sim;
    }
    
    public void gc(){
        tn=null;
        //tp=null;
        inputD=null;
        featureD=null;
        phonemeD=null;
        wordD=null;
    }
    
    /** Reset the TraceSim object. */
    public void reset() {
        // first, the TraceNet object
        tn.reset();
        
        stepsRun = 0;
        
        //mainly to ensure that ambiguity continuum is built.
        tp.getPhonology().compileAll();
        
        // input string (only one now)
        inputString = tp.getModelInput();
        
        tn.createInput(inputString);         
        
        // initialize our internal arrays
        maxDuration = 6 * inputString.length() * tp.getDeltaInput();                
        if (maxDuration < tp.getFSlices()) 
            maxDuration = tp.getFSlices();
        inputD = new double[maxDuration][][];
        featureD = new double[maxDuration][][];
        phonemeD = new double[maxDuration][][];
        wordD = new double[maxDuration][][];
        
        globalLexicalCompetition = new double[maxDuration];
        globalPhonemeCompetition = new double[maxDuration];
                
        // store initial set of data
        
	double[][] in=tn.getInputLayer();
        double[][] ft=tn.getFeatureLayer();
        double[][] ph=tn.getPhonemeLayer();
        double[][] wd=tn.getWordLayer();
        
        inputD[stepsRun]=new double[in.length][in[0].length];
        for(int i=0;i<in.length;i++)
            for(int j=0;j<in[0].length;j++)
                inputD[stepsRun][i][j]=in[i][j];
        featureD[stepsRun]=new double[ft.length][ft[0].length];
        for(int i=0;i<ft.length;i++)
            for(int j=0;j<ft[0].length;j++)
                featureD[stepsRun][i][j]=ft[i][j];
        phonemeD[stepsRun]=new double[ph.length][ph[0].length];
        for(int i=0;i<ph.length;i++)
            for(int j=0;j<ph[0].length;j++)
                phonemeD[stepsRun][i][j]=ph[i][j];
        wordD[stepsRun]=new double[wd.length][wd[0].length];
        for(int i=0;i<wd.length;i++)
            for(int j=0;j<wd[0].length;j++)
                wordD[stepsRun][i][j]=wd[i][j];
    }

    /**
     * Run the model the specified number of times. Internal state is stored.
     * If parameters have updated, reset first. The number of completed steps
     * is returned.
     *
     * @param numCycles     number of cycles to run
     * @return              total cumulative number of steps run in the model
     */
    public int cycle(int numCycles) {        
        checkForParamUpdate();
                
        if(numCycles > maxDuration) 
            numCycles = maxDuration;
            
        for (int c = 0; c < numCycles; c++)            
        {                        
            // get references to the data
            double[][] in=tn.getInputLayer();
            double[][] ft=tn.getFeatureLayer();
            double[][] ph=tn.getPhonemeLayer();
            double[][] wd=tn.getWordLayer();
            
            // and store it for others to peruse
            try{
                inputD[stepsRun]=new double[in.length][in[0].length];
                for(int i=0;i<in.length;i++)
                    for(int j=0;j<in[0].length;j++)
                        inputD[stepsRun][i][j]=in[i][j];
                featureD[stepsRun]=new double[ft.length][ft[0].length];
                for(int i=0;i<ft.length;i++)
                    for(int j=0;j<ft[0].length;j++)
                        featureD[stepsRun][i][j]=ft[i][j];
                phonemeD[stepsRun]=new double[ph.length][ph[0].length];
                for(int i=0;i<ph.length;i++)
                    for(int j=0;j<ph[0].length;j++)
                        phonemeD[stepsRun][i][j]=ph[i][j];
                wordD[stepsRun]=new double[wd.length][wd[0].length];
                for(int i=0;i<wd.length;i++)
                    for(int j=0;j<wd[0].length;j++)
                        wordD[stepsRun][i][j]=wd[i][j]; 
                
                globalLexicalCompetition[stepsRun]=tn.getGlobalLexicalCompetition();
                //System.out.println("TraceSim-212: "+stepsRun+"\t"+globalLexicalCompetition[stepsRun]);
                globalPhonemeCompetition[stepsRun]=tn.getGlobalPhonemeCompetition();        
            }
            catch(Exception e){e.printStackTrace();}
                        
            stepsRun++;
            
            applyParameterFunctions(stepsRun);            
            
            tn.cycle();                                  
        }
            
        return stepsRun;
    }
    
    public double validateAgainst(double[][][][] validationD){
        //order: input, feature, phoneme, word
        double[][][][] differenceD = new double[4][][][];
        differenceD[0]=new double[java.lang.Math.min(validationD[0].length,inputD.length)][java.lang.Math.min(validationD[0][0].length,inputD[0].length)][java.lang.Math.min(validationD[0][0][0].length,inputD[0][0].length)];
        differenceD[1]=new double[java.lang.Math.min(validationD[1].length,featureD.length)][java.lang.Math.min(validationD[1][0].length,featureD[0].length)][java.lang.Math.min(validationD[1][0][0].length,featureD[0][0].length)];
        differenceD[2]=new double[java.lang.Math.min(validationD[2].length,phonemeD.length)][java.lang.Math.min(validationD[2][0].length,phonemeD[0].length)][java.lang.Math.min(validationD[2][0][0].length,phonemeD[0][0].length)];
        differenceD[3]=new double[java.lang.Math.min(validationD[3].length,wordD.length)][java.lang.Math.min(validationD[3][0].length,wordD[0].length)][java.lang.Math.min(validationD[3][0][0].length,wordD[0][0].length)];
        
        double SMAD = 0;
        int SMAD_ct = 0;
        for(int i=0;i<differenceD[0].length;i++)
            for(int j=0;j<differenceD[0][0].length;j++)
                for(int k=0;k<differenceD[0][0][0].length;k++){
                    differenceD[0][i][j][k] = java.lang.Math.abs(validationD[0][i][j][k] - inputD[i][j][k]);
                    SMAD += java.lang.Math.abs(validationD[0][i][j][k] - inputD[i][j][k]);
                    SMAD_ct++;
                }
        for(int i=0;i<differenceD[1].length;i++)
            for(int j=0;j<differenceD[1][0].length;j++)
                for(int k=0;k<differenceD[1][0][0].length;k++){
                    differenceD[1][i][j][k] = java.lang.Math.abs(validationD[1][i][j][k] - featureD[i][j][k]);
                    SMAD += java.lang.Math.abs(validationD[1][i][j][k] - featureD[i][j][k]);
                    SMAD_ct++;
                }
        for(int i=0;i<differenceD[2].length;i++)
            for(int j=0;j<differenceD[2][0].length;j++)
                for(int k=0;k<differenceD[2][0][0].length;k++){
                    differenceD[2][i][j][k] = java.lang.Math.abs(validationD[2][i][j][k] - phonemeD[i][j][k]);
                    SMAD += java.lang.Math.abs(validationD[2][i][j][k] - phonemeD[i][j][k]);
                    SMAD_ct++;
                }
        for(int i=0;i<differenceD[3].length;i++)
            for(int j=0;j<differenceD[3][0].length;j++)
                for(int k=0;k<differenceD[3][0][0].length;k++){
                    differenceD[3][i][j][k] = java.lang.Math.abs(validationD[3][i][j][k] - wordD[i][j][k]);
                    SMAD += java.lang.Math.abs(validationD[3][i][j][k] - wordD[i][j][k]);
                    SMAD_ct++;
                }
        
        inputD = differenceD[0];
        featureD = differenceD[1];
        phonemeD = differenceD[2];
        wordD = differenceD[3];
        maxDuration = inputD.length;
        stepsRun = maxDuration;
        
        SMAD /= SMAD_ct;
        SMAD *= 100;
        
        //paramUpdateCt++;
        return SMAD;
    }
    
    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param targ  The index of the word/phone of interest
     * @param alingn    The alignment of the word/phone of interest
     * @param cycle The cycle of interest.
     * @param type words=0, phonemes=1.
     */    
    public int[][] competitorSet(String targ, int align, int cycle,int homePos, int type){
        int[][] result;
        java.util.Random rand= new java.util.Random();
        //words
        if(type==0){
            //identify the index of the target        
            int targIdx=-1;
            for(int i=0;i<tp.getLexicon().size();i++)
                if(targ.equals(tp.getLexicon().get(i).getPhon())){
                    targIdx=i;
                    break;
                }
            if(targIdx==-1) return null;
            
            //insert the target into the result array            
            result = new int[10][3];
            result[homePos][0] = targIdx; //item index
            result[homePos][1] = align; //alignment
            result[homePos][2] = -100; //competition strength
            
            //look into competitor items, and sort competitor strength
            //key=competition-strength (heuristic), value=item-alignment coordinate
            java.util.TreeMap tmap = new java.util.TreeMap();
            double[][] data;
            data = wordD[cycle-1];
            if(data==null) return null;
            double competitionStrength;
            int maxAlignment; // the competitor alignment that is most active for that item
            for(int i=0;i<data.length;i++){
                competitionStrength = 0d;
                maxAlignment=0;
                for(int j=0;j<data[i].length;j++){
                    if(i==targIdx&&j==align) continue;
                    if(competitionStrength(targIdx,align,i,j,type,cycle)>competitionStrength){
                        maxAlignment=j;
                        competitionStrength=competitionStrength(targIdx,align,i,j,type,cycle);
                    }
                }
                int[] coord = {i,maxAlignment};
                tmap.put(new Double(competitionStrength + (rand.nextGaussian()*0.00001)),coord);
            }
            for(int i=0;i<result.length&&i<tmap.size();i++){
                if(i==homePos) continue;
                Double key=(Double)tmap.lastKey();
                int[] nex = ((int[])tmap.remove(key));
                //System.out.println("key:"+key.doubleValue()+"\t"+nex[0]+","+nex[1]);
                result[i][0]=nex[0];
                result[i][1]=nex[1];                
                result[i][2]=(int)(100d*(double)competitionStrength(targIdx,align,nex[0],nex[1],type,cycle));
            }
            //make sure that result doesn't contain any null pointers
            //@@@
        }
        //phonemes
        else{
            result = new int[15][2];            
        }
        return result;
    }
    private double competitionStrength(int targIdx,int targAlign,int compIdx,int compAlign,int type,int cycle){
        double result;
        if(type==0){
            double[][] data = wordD[cycle-1];
            int tend=targAlign + (2*tp.getLexicon().get(targIdx).getPhon().length());
            int cend=compAlign + (2*tp.getLexicon().get(compIdx).getPhon().length());
            int overlap = 0;
            for(int i=0;i<Math.max(tend,cend);i++)
                if(i>=targAlign&&i>=compAlign&&i<tend&&i<cend)
                    overlap++;
            result = overlap * (data[compIdx][compAlign] * data[compIdx][compAlign]);
        }
        else{
            double[][] data = phonemeD[cycle-1];
            int tend=targAlign + 2;
            int cend=compAlign + 2;
            int overlap = 0;
            for(int i=0;i<Math.max(tend,cend);i++)
                if(i>=targAlign&&i>=compAlign&&i<tend&&i<cend)
                    overlap++;
            result = overlap * data[compIdx][compAlign];
        }
        return result;
    }
    
    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {

        propertyChangeSupport.addPropertyChangeListener (l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {

        propertyChangeSupport.removePropertyChangeListener (l);
    }

    /**
     * Getter for property inputD.
     * @return Value of property inputD.
     */
    public double[][][] getInputD() {

        return this.inputD;
    }

    /**
     * Getter for property featureD.
     * @return Value of property featureD.
     */
    public double[][][] getFeatureD() {
        return this.featureD;
    }

    /**
     * Getter for property phonemeD.
     * @return Value of property phonemeD.
     */
    public double[][][] getPhonemeD() {

        return this.phonemeD;
    }

    /**
     * Getter for property wordD.
     * @return Value of property wordD.
     */
    public double[][][] getWordD() {

        return this.wordD;
    }
    
    /**
     * Getter for property globalLexicalCompetition.
     * @return Value of property globalLexicalCompetition.
     */
    public double[] getGlobalLexicalCompetition() {

        return this.globalLexicalCompetition;
    }
    
    /**
     * Getter for property globalPhonemeCompetition.
     * @return Value of property globalPhonemeCompetition.
     */
    public double[] getGlobalPhonemeCompetition() {

        return this.globalPhonemeCompetition;
    }

    /**
     * If the param object has been updated, reset.
     */
    private void checkForParamUpdate() {
        if (tp.getUpdateCt() > paramUpdateCt)
        {
            paramUpdateCt = tp.getUpdateCt();
            
            reset();
        }
    }
    /**
     *If parameter functions have been added, calculate function
     *values, and apply values to appropriate parameters. 
     */
    private void applyParameterFunctions(int cyc) {
       if(tp.getFunctionList().size()==0) 
           return;
       for(int _i=0;_i<tp.getFunctionList().size();_i++){
           TraceFunction tf = tp.getFunctionList().get(_i);
           int idx = tf.getParameterIndex();
           double val = ((LinearParameterFunction)tf).getFunctionValue(cyc);
           //System.out.println("func "+cyc+" "+idx+" "+val);            
            if(idx==0) tp.getAlpha().IF=val;                
            else if(idx==1) tp.getAlpha().FP=val;                
            else if(idx==2) tp.getAlpha().PW=val;                
            else if(idx==3) tp.getAlpha().PF=val;                
            else if(idx==4) tp.getAlpha().WP=val;                
            else if(idx==5) tp.getGamma().F=val;                
            else if(idx==6) tp.getGamma().P=val;                
            else if(idx==7) tp.getGamma().W=val;                
            else if(idx==8) tp.getDecay().F=val;                
            else if(idx==9) tp.getDecay().F=val;                
            else if(idx==10) tp.getDecay().F=val;                
            else if(idx==11) tp.getRest().F=val;                
            else if(idx==12) tp.getRest().P=val;                
            else if(idx==13) tp.getRest().W=val;                
            else if(idx==14){ 
               tp.setNoiseSD(val);
               tp.preventUpdateCt();
               tn.resetNoise();
            }                
            else if(idx==15){ 
               tp.setStochasticitySD(val);            
               tp.preventUpdateCt();
               tn.resetNoise(); 
            }
            else if(idx==16){ tp.setAtten(val); tp.preventUpdateCt();}                
            else if(idx==17){ tp.setBias(val); tp.preventUpdateCt();}                 
            else if(idx==18){ tp.setLearningRate(val); tp.preventUpdateCt();}                
            else if(idx==19){ tp.setSpreadScale(val); tp.preventUpdateCt();}                
            else if(idx==20){ tp.setMin(val); tp.preventUpdateCt();}                
            else if(idx==21){ tp.setMax(val); tp.preventUpdateCt();}                
            else if(idx==22){ tp.getFreqNode().RDL_rest_s=val;}               
            else if(idx==23){ tp.getFreqNode().RDL_wt_s=val;}                
            else if(idx==24){ tp.getFreqNode().RDL_post_c=val;}                
            else if(idx==25){ tp.getPrimeNode().RDL_rest_s=val;}
            else if(idx==26){ tp.getPrimeNode().RDL_wt_s=val;}
            else if(idx==27){ tp.getPrimeNode().RDL_post_c=val;}           
       } 
    }

    public TraceParam getParameters() {
        return tp;
    }
    public void setParameters(TraceParam _tp){
        tp = _tp;
    }

    /**
     * Getter for property maxDuration.
     * @return Value of property maxDuration.
     */
    public int getMaxDuration() {

        return this.maxDuration;
    }
    
    public boolean isNullPointer(){        
        return (tn==null);
    }
    
    public String XMLTag(){
        String result="";
        result+="<action>";
        result+="<set-parameters>";
        result+=tp.XMLTag();
        result+="</set-parameters>";
        result+="</action>";       
        //result+="<action><name>load-sim-from-file</name></action>";
        return result;
    }
    /**
     * Export this sim to a directory tree. Subdirectories of the specified
     * directory are created called "input", "feature", "phoneme", and "word".
     * Each file is called <timestep>.dat, e.g., 17.dat. Each file contains the
     * state of its matrix at that timestep, in the same orientation as the
     * graphs on the sim panel (rows = items, cols = time slices). The first
     * column is a label for each row. Note that this will happily over-write
     * data, so if you don't want that, check before calling this method. Also
     * note that this may take a while, so in a GUI, you might set up an hourglass
     * cursor...
     *
     * @param directory     directory to save to
     * @returns             always true -- throws exception if not.
     */
    public boolean export(java.io.File directory) throws IOException
    {
        FileWriter out;
        
        String sep = ", ";
        
        DecimalFormat outFormat = new DecimalFormat("0.0000");
        
        double [][][][] alldata = new double [4][][][];
        String [] alldataNames = {"input", "feature", "phoneme", "word"};
        
        // for simplicity, make a big meta-array of all data
        alldata[0] = inputD;
        alldata[1] = featureD;
        alldata[2] = phonemeD;
        alldata[3] = wordD;
        
//        try
//        {
            // create directory
            if (!directory.isDirectory())
            {
                if (!directory.mkdirs())
                    throw new IOException("directory.mkdirs() failed");
            }

            // foreach subdirectory
            for (int subdir = 0; subdir < 4; subdir++)
            {
                // create the subdirectory
                File subdirFile = new File(directory, alldataNames[subdir]);
                if (!subdirFile.isDirectory() && !subdirFile.mkdir())
                    throw new IOException("subdirFile.mkdir() failed");

                // foreach timestep
                for (int iStepsRun = 0; iStepsRun < stepsRun; iStepsRun++)
                {
                    File stepFile = new File(subdirFile, Integer.toString(iStepsRun) + ".dat");
                    
                    // open that file
                    out = new FileWriter(stepFile);

                    // foreach item
                    for (int iItem = 0; iItem < alldata[subdir][iStepsRun].length; iItem++)
                    {
                        // figure out the item name, and write
                        switch (subdir)
                        {
                            case 0: // input
                                // @@@ just use index
                                out.write(Integer.toString(iItem));
                                break;
                            case 1: // features
                                // @@@ just use index
                                out.write(Integer.toString(iItem));
                                break;
                            case 2: // phonemes
                                // use phoneme index from TracePhones
                                out.write(tp.getPhonology().labels[iItem]);
                                break;
                            case 3: // words
                                // use word index from TraceParam
                                out.write(tp.getLexicon().get(iItem).getPhon());;
                                break;
                        }
                        out.write(sep);

                        // foreach time slice
                        for (int iSlice = 0; iSlice < alldata[subdir][iStepsRun][iItem].length; iSlice++)
                        {
                            // output the data
                            out.write(outFormat.format(alldata[subdir][iStepsRun][iItem][iSlice]));
                            
                            if (iSlice != alldata[subdir][iStepsRun][iItem].length - 1)
                                out.write(sep);
                            
                        }
                        
                        out.write("\n");
                    }
                    
                    // close the file
                    out.close();
                }
            }
//        }
//        catch (IOException ex)
//        {
//            return false;
//        }
        
        return true;
        
    }
    
}

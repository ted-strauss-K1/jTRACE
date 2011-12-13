/*
 * loadSimTask.java
 *
 * Created on July 12, 2004, 12:47 AM
 */


package edu.uconn.psy.jtrace.IO;
import java.io.*;
/**
 *
 * @author  tedstrauss
 */
public class simTask extends Thread {
    int progress,max,job;
    File path, F1, F2;
    File[] dir;
    double[][][][] D1, D2;
    boolean done;
    edu.uconn.psy.jtrace.UI.SimPanel sim;
    double[][][] inputD,featureD,phonemeD,wordD;
    edu.uconn.psy.jtrace.Model.TraceParam tp;
    edu.uconn.psy.jtrace.UI.jTRACE wtgui;
    String[] wordLabels, phonemeLabels;
    String progressMessage;    
    
    int phonTestIndex = 8;
    
    //#3 constructor for COMPARISON op, with one array, one file, #3
    public simTask(double[][][][] _D1, java.io.File _F2, edu.uconn.psy.jtrace.UI.SimPanel sim, String[] _pl, String[] _wl, int _j){
        job=_j;
        D1=_D1;
        F2=_F2;
        this.sim=sim;
        max=D1[0].length+F2.listFiles().length;
        progress=0;
        phonemeLabels=_pl;
        wordLabels=_wl;
        System.gc();
        Runtime.getRuntime().gc();               
    }
    //#4 constructor for COMPARISON op, with two files, #4    
    public simTask(java.io.File _F1, java.io.File _F2, edu.uconn.psy.jtrace.UI.SimPanel sim,int _j){
        job=_j;
        F1=_F1;
        F2=_F2;
        this.sim=sim;
        max=F1.listFiles().length+F2.listFiles().length;
        progress=0;
        System.gc();
        Runtime.getRuntime().gc();               
    }
    //#1 constructor for LOAD SIMULATION DATA operation, when called from SIM panel
    public simTask(java.io.File _p, edu.uconn.psy.jtrace.UI.SimPanel sim,int _j) {
        super();
        path=_p;
        done=false;
        job=_j;
        this.sim=sim;
        progress=0;
        System.gc();
        Runtime.getRuntime().gc();               
    }
    //#5 constructor for LOAD SIMULATION DATA operation, when called from WTGUI panel, #5
    public simTask(java.io.File _p, edu.uconn.psy.jtrace.UI.jTRACE g,int _j){
        super();
        wtgui=g;
        path=_p;
        job=_j;
        progress=0;
        done=false;
        System.gc();
        Runtime.getRuntime().gc();               
    }
    //#2 constructor for SAVE DATA operation
    public simTask(java.io.File path, edu.uconn.psy.jtrace.UI.SimPanel sim,int _j,double[][][] _i, double[][][] _f, double[][][] _p, double[][][] _w, String[] _pl,edu.uconn.psy.jtrace.Model.TraceParam tp,int _m) {
        super();
        this.path=path;
        this.max=_m;
        done=false;
        job=_j;
        this.sim=sim;
        this.tp=tp;
        inputD=_i;
        featureD=_f;
        phonemeD=_p;
        wordD=_w;
        phonemeLabels=_pl;
        progress=0;
        System.gc();
        Runtime.getRuntime().gc();               
    }
    //#6 CYCLE
    public simTask(edu.uconn.psy.jtrace.Model.TraceNet model,int slice){
        System.gc();
        Runtime.getRuntime().gc();               
    }
    //#7 COMPARISON : called from the regression test, minimal pop-ups
    public simTask(java.io.File[] _dir, edu.uconn.psy.jtrace.UI.jTRACE _wtgui,int _j){
        job=_j;
        wtgui=_wtgui;
        dir=_dir;
        max=dir.length;
        done=false;
        progress=0;
        progressMessage="Working ...";        
        System.gc();
        Runtime.getRuntime().gc();               
    }
    public simTask(){}
    
    public void run(){
        switch(job){
            case 1 : jobOne();  return; //load sim from WTSIMULATION
            case 2 : jobTwo(); return; //save data 
            case 3 : jobThree(); return; //sim comparison
            case 4 : jobFour(); return; //sim comparison
            //case 5 : jobFive(); return; //load sim from WTGUI
            case 6 : jobSix(); return; //cycle the model
            case 7 : jobSeven(); return; //regression test
        }
    }    
    //load simulation data, called from within sim panel
    private void jobOne(){
//        System.gc();
//        Runtime.getRuntime().gc();       
//        progress=0;
//        edu.uconn.psy.jtrace.IO.WTFileReader FR;
//        File dir[]=path.listFiles();                        
//        edu.uconn.psy.jtrace.Model.TraceParam tp=new edu.uconn.psy.jtrace.Model.TraceParam();
//        int tick=0;
//        int currTimeIncr = 0;
//        for(int i=0;i<dir.length;i++)                
//            if(!dir[i].getName().startsWith("word")&&!dir[i].getName().startsWith("input")&&
//               !dir[i].getName().startsWith("feat")&&!dir[i].getName().startsWith("phon"))
//                tick++;            
//        int currDataAccum = (int)((dir.length-tick)/4); 
//        tick=0;
//        int maxDuration = currDataAccum;
//        //System.out.println("# files="+dir.length+"  ... "+currDataAccum); 
//        String[] wordLabels={"",""};
//        double[][][] inputD=new double[currDataAccum][][];
//        double[][][] featureD=new double[currDataAccum][][];
//        double[][][] phonemeD=new double[currDataAccum][][];
//        double[][][] wordD=new double[currDataAccum][][];
//        //System.out.println("Array Sizes: "+currDataAccum);            
//        
//        max=dir.length;
//        for(;progress<dir.length;progress++){
//             //System.out.print(progress+" - ");                 
//             String name=dir[progress].getName();
//             FR=new edu.uconn.psy.jtrace.IO.WTFileReader(dir[progress]);             
//             if(name.startsWith("parameter")){ 
//                 FR=new edu.uconn.psy.jtrace.IO.WTFileReader(dir[progress]);
//                 tp=FR.readWTParameterFile(); 
//                 tick++;
//             }            
//             else if(name.startsWith("feat"))
//                    featureD[progress-tick]=FR.readWTSimData('f');                    
//             else if(name.startsWith("input"))
//                    inputD[progress-tick-currDataAccum]=FR.readWTSimData('i');                               
//             else if(name.startsWith("phon"))
//                    phonemeD[progress-tick-(2*currDataAccum)]=FR.readWTSimData('p');                    
//             else if(name.startsWith("word")){
//                    wordD[progress-tick-(3*currDataAccum)]=FR.readWTSimData('w');
//                    wordLabels=FR.readWordLabels();
//             }
//             else{
//                 tick++;
//             }
//        }
//        //System.out.println("i / "+inputD.length+"."+inputD[0].length+"."+inputD[0][0].length);
//        //System.out.println("f / "+featureD.length+"."+featureD[0].length+"."+featureD[0][0].length);
//        //System.out.println("p / "+phonemeD.length+"."+phonemeD[0].length+"."+phonemeD[0][0].length);
//        //System.out.println("w / "+wordD.length+"."+wordD[0].length+"."+wordD[0][0].length);
//        //System.out.println(wordLabels.length+" words");
//        sim.setVisible(false);
//        sim.remove(sim.getWordGraph()); sim.remove(sim.getFeatureGraph()); sim.remove(sim.getPhonemeGraph()); sim.remove(sim.getInputGraph()); 
//        sim.setWordGraph(new edu.uconn.psy.jtrace.UI.WordGraphViewer(wordD[0],maxDuration,tp.getNReps(),tp.getMin(),tp.getMax(),wordLabels));
//        sim.setFeatureGraph(new edu.uconn.psy.jtrace.UI.FeatureGraphViewer(featureD[0],maxDuration,tp.getNReps(),tp.getMin(),tp.getMax()));                        
//        sim.setPhonemeGraph(new edu.uconn.psy.jtrace.UI.PhonemeGraphViewer(phonemeD[0],maxDuration,tp.getNReps(),tp.getMin(),tp.getMax()));        
//        sim.setInputGraph(new edu.uconn.psy.jtrace.UI.InputGraphViewer(inputD[0],maxDuration,tp.getNReps(),tp.getMin(),tp.getMax()));                                                    
//        double[][][][] D={inputD,featureD,phonemeD,wordD};
//        sim.setD(D);
//        sim.setWordLabels(wordLabels);
//        sim.setTraceParam(tp);
//        sim.setFrameAll(0);
//        sim.initTraceGraphs();
//        sim.setVisible(true);                
//        sim.setModelInputTextField(tp.getModelInput());        
//        sim.setMaxDuration(wordD.length);
//        sim.setCurrDataAccum(wordD.length);
//        sim.setCurrTimeIncr(0);
//        sim.registerMouseMotionListeners();
//        progress=dir.length;
//        done=true;
    }
    //save simulation data, called from within sim panel
    private void jobTwo(){
//        System.gc();
//        Runtime.getRuntime().gc();       
//        progress=0;
//        //System.out.println("MAX= "+max);
//        edu.uconn.psy.jtrace.IO.WTFileWriter w=new edu.uconn.psy.jtrace.IO.WTFileWriter(path);
//        //write data for each time slice
//        for(;progress<max;progress++){
//            w.writeOneDataSlice(inputD[progress],featureD[progress],phonemeD[progress],phonemeLabels,wordD[progress],tp.getLexicon().toStringArray(),progress);
//        }
//        //save a copy of the parameters
//        w=new edu.uconn.psy.jtrace.IO.WTFileWriter(path,"parameters.xml");
//        String XMLparam=w.makeWTParameterOpeningTag();
//        XMLparam+=w.makeFileRemarks(tp);        
//        XMLparam+=w.makeWTParameterFileBody(tp);
//        XMLparam+=w.makeWTParameterClosingTag();
//        w.writeAndClose(XMLparam);                
//        w=null;
//        sim.registerMouseMotionListeners();
//        //System.out.println("ok");
//        done=true;
    }
    //compare data between array and directory, called from within sim panel.
    private void jobThree(){
//        System.gc();
//        Runtime.getRuntime().gc();       
//        progress=0;        
//        //D1 is loaded in from constructor        
//        //READING IN F2
//        int tick=0;
//        edu.uconn.psy.jtrace.IO.WTFileReader FR;
//        edu.uconn.psy.jtrace.Model.TraceParam tp=new edu.uconn.psy.jtrace.Model.TraceParam();
//        File dir[]=F2.listFiles();                        
//        int currTimeIncr = 0;
//        int dontCount=0;
//        for(int i=0;i<dir.length;i++)                
//            if(!dir[i].getName().startsWith("word")&&!dir[i].getName().startsWith("input")&&
//               !dir[i].getName().startsWith("feat")&&!dir[i].getName().startsWith("phon"))
//                dontCount++; 
//        int currDataAccum = (int)((dir.length-dontCount)/4); 
//        int maxDuration = currDataAccum;
//        //System.out.println("F2 # files="+dir.length+"  ... "+currDataAccum); 
//        D2=new double[4][currDataAccum][][];
//        String[] words1=wordLabels, words2={"",""}, phons1=phonemeLabels, phons2={"",""};        
//        //System.out.println("F2 Array Sizes: "+currDataAccum);                            
//        for(int i=0;i<dir.length;i++){
//             System.gc();
//             Runtime.getRuntime().gc();               
//             progress++;
//             String name=dir[i].getName();
//             FR=new edu.uconn.psy.jtrace.IO.WTFileReader(dir[i]);             
//             if(name.startsWith("parameter")){ 
//                 FR=new edu.uconn.psy.jtrace.IO.WTFileReader(dir[i]);
//                 tp=FR.readWTParameterFile(); 
//                 tick++;
//             }            
//             else if(name.startsWith("input"))
//                    D2[0][i-tick-currDataAccum]=FR.readWTSimData('i');                               
//             else if(name.startsWith("feat"))
//                    D2[1][i-tick]=FR.readWTSimData('f');                    
//             else if(name.startsWith("phon")){
//                    D2[2][i-tick-(2*currDataAccum)]=FR.readWTSimData('p');                    
//                    phons2=FR.readPhonemeLabels();
//             }
//             else if(name.startsWith("word")){
//                    D2[3][i-tick-(3*currDataAccum)]=FR.readWTSimData('w');
//                    words2=FR.readWordLabels();
//             }
//             else tick++;                          
//        }
//        //NOW WE HAVE D1 AND D2.  NEXT: CHECK FOR INCONSISTENCIES, CALCULATE DIFFERENCE
//        //COMPARE PHONE LABELS, give appropriate warnings.
//        for(int i=0;i<words1.length;i++){
//            if(i==words2.length) break;
//            if(!words1[i].equals(words2[i]))
//                System.out.println("WORD MIS-MATCH IN COMPARISON THREAD: "+words1[i]+" != "+words2[i]);
//                //TODO; POP-UP A OPTIONPANE TELLING USER                
//        }
//        //COMPARE WORD LABELS, give appropriate warnings.
//        for(int i=0;i<phons1.length;i++){
//            if(i==phons2.length) break;
//            //if(!phons1[i].equals(phons2[i]))                
//            //    System.out.println("PHONEME MIS-MATCH IN COMPARISON THREAD: "+phons1[i]+" != "+phons2[i]);
//            //TODO; POP-UP A OPTIONPANE TELLING USER                
//        }
//        //CALCULATE DIFFERENCE ARRAY
//        //normalization step
//        D1=arrayTool(D1);
//        D2=arrayTool(D2);
//        System.gc();
//        Runtime.getRuntime().gc();
//        
//        System.out.println("Dimensions of the 1st sim: "+D1.length);
//        System.out.println("x\t("+D1[0].length+"x"+D1[0][0].length+"x"+D1[0][0][0].length+")");
//        System.out.println("x\t("+D1[1].length+"x"+D1[1][0].length+"x"+D1[1][0][0].length+")");
//        System.out.println("x\t("+D1[2].length+"x"+D1[2][0].length+"x"+D1[2][0][0].length+")");
//        System.out.println("x\t("+D1[3].length+"x"+D1[3][0].length+"x"+D1[3][0][0].length+")");
//        System.out.println("Dimensions of the 2nd sim: "+D2.length);
//        System.out.println("x\t("+D2[0].length+"x"+D2[0][0].length+"x"+D2[0][0][0].length+")");
//        System.out.println("x\t("+D2[1].length+"x"+D2[1][0].length+"x"+D2[1][0][0].length+")");
//        System.out.println("x\t("+D2[2].length+"x"+D2[2][0].length+"x"+D2[2][0][0].length+")");
//        System.out.println("x\t("+D2[3].length+"x"+D2[3][0].length+"x"+D2[3][0][0].length+")");
//            
//        //absoluted difference step
//        double[][][][] DifData=absDiffOp(D1,D2,true,"");
//        System.gc();
//        Runtime.getRuntime().gc();
//        
//        //UPDATE SIM
//        sim.setVisible(false);
//        sim.setTraceParam(tp);
//        sim.setMaxDuration(DifData[0].length);
//        sim.setCurrDataAccum(DifData[0].length);
//        sim.setCurrTimeIncr(0);
//        sim.setModelInputTextField(" n/a ");                            
//        sim.setD(DifData);
//        sim.remove(sim.getWordGraph()); sim.remove(sim.getFeatureGraph()); sim.remove(sim.getPhonemeGraph()); sim.remove(sim.getInputGraph()); 
//        sim.setInputGraph(new edu.uconn.psy.jtrace.UI.InputGraphViewer(DifData[0][0],maxDuration,1,tp.getMin(),tp.getMax()));                                                    
//        sim.setFeatureGraph(new edu.uconn.psy.jtrace.UI.FeatureGraphViewer(DifData[1][0],maxDuration,1,tp.getMin(),tp.getMax()));                        
//        sim.setPhonemeGraph(new edu.uconn.psy.jtrace.UI.PhonemeGraphViewer(DifData[2][0],maxDuration,1,tp.getMin(),tp.getMax()));        
//        sim.setWordGraph(new edu.uconn.psy.jtrace.UI.WordGraphViewer(DifData[3][0],maxDuration,1,tp.getMin(),tp.getMax(),words1));
//        sim.initTraceGraphs();
//        sim.setFrameAll(0);
//        sim.disableGraphToggle();
//        sim.registerMouseMotionListeners();
//        sim.setVisible(true);                            
//        System.gc();
//        Runtime.getRuntime().gc();        
//        done=true;            
    }
    //compare data between directory and directory, called from within sim panel
    private void jobFour(){
//        System.gc();
//        Runtime.getRuntime().gc();       
//        progress=0;        
//        //READING IN F1
//        int tick=0;        
//        edu.uconn.psy.jtrace.IO.WTFileReader FR;
//        File dir[]=F1.listFiles();                        
//        edu.uconn.psy.jtrace.Model.TraceParam tp=new edu.uconn.psy.jtrace.Model.TraceParam();
//        int currTimeIncr = 0;
//        int dontCount=0;
//        for(int i=0;i<dir.length;i++)                
//            if(!dir[i].getName().startsWith("word")&&!dir[i].getName().startsWith("input")&&
//               !dir[i].getName().startsWith("feat")&&!dir[i].getName().startsWith("phon"))
//                dontCount++; 
//        int currDataAccum = (int)((dir.length-dontCount)/4); 
//        int maxDuration = currDataAccum;
//        System.out.println("F1 # files="+dir.length+"  ... "+currDataAccum); 
//        String[] words1={"",""}, words2={"",""}, phons1={"",""}, phons2={"",""};        
//        D1=new double[4][currDataAccum][][];
//        System.out.println("F1 Array Sizes: "+currDataAccum);                            
//        for(int i=0;i<dir.length;i++){
//             progress++;
//             String name=dir[i].getName();
//             FR=new edu.uconn.psy.jtrace.IO.WTFileReader(dir[i]);             
//             if(name.startsWith("parameter")){ 
//                 FR=new edu.uconn.psy.jtrace.IO.WTFileReader(dir[i]);
//                 tp=FR.readWTParameterFile(); 
//                 tick++;
//             }            
//             else if(name.startsWith("input"))
//                    D1[0][i-tick-currDataAccum]=FR.readWTSimData('i');                               
//             else if(name.startsWith("feat"))
//                    D1[1][i-tick]=FR.readWTSimData('f');                    
//             else if(name.startsWith("phon")){
//                    D1[2][i-tick-(2*currDataAccum)]=FR.readWTSimData('p');                    
//                    phons1=FR.readPhonemeLabels();
//             }
//             else if(name.startsWith("word")){
//                    D1[3][i-tick-(3*currDataAccum)]=FR.readWTSimData('w');
//                    words1=FR.readWordLabels();
//             }
//             else tick++;             
//        }
//        
//        //READING IN F2
//        tick=0;
//        dir=F2.listFiles();                        
//        currTimeIncr = 0;
//        currDataAccum = (int)((dir.length-1)/4); 
//        maxDuration = currDataAccum;
//        System.out.println("F2 # files="+dir.length+"  ... "+currDataAccum); 
//        D2=new double[4][currDataAccum][][];
//        System.out.println("F2 Array Sizes: "+currDataAccum);                            
//        for(int i=0;i<dir.length;i++){
//             progress++;
//             String name=dir[i].getName();
//             FR=new edu.uconn.psy.jtrace.IO.WTFileReader(dir[i]);             
//             if(name.startsWith("parameter")){ 
//                 FR=new edu.uconn.psy.jtrace.IO.WTFileReader(dir[i]);
//                 tp=FR.readWTParameterFile(); 
//                 tick++;
//             }            
//             else if(name.startsWith("input"))
//                    D2[0][i-tick-currDataAccum]=FR.readWTSimData('i');                               
//             else if(name.startsWith("feat"))
//                    D2[1][i-tick]=FR.readWTSimData('f');                    
//             else if(name.startsWith("phon")){
//                    D2[2][i-tick-(2*currDataAccum)]=FR.readWTSimData('p');                    
//                    phons2=FR.readPhonemeLabels();
//             }
//             else if(name.startsWith("word")){
//                    D2[3][i-tick-(3*currDataAccum)]=FR.readWTSimData('w');
//                    words2=FR.readWordLabels();
//             }
//             else tick++;                          
//        }
//        //NOW WE HAVE D1 AND D2.  NEXT: CHECK FOR INCONSISTENCIES, CALCULATE DIFFERENCE
//        //COMPARE PHONE LABELS, give appropriate warnings.
//        for(int i=0;i<words1.length;i++){
//            if(i==words2.length) break;
//            if(!words1[i].equals(words2[i]))
//                System.out.println("WORD MIS-MATCH IN COMPARISON THREAD!!");
//                //TODO; POP-UP A OPTIONPANE TELLING USER                
//        }
//        //COMPARE WORD LABELS, give appropriate warnings.
//        for(int i=0;i<phons1.length;i++){
//            if(i==phons2.length) break;
//            if(!phons1[i].equals(phons2[i]))                
//                System.out.println("PHONEME MIS-MATCH IN COMPARISON THREAD!!");
//                //TODO; POP-UP A OPTIONPANE TELLING USER                
//        }
//        //CALCULATE DIFFERENCE ARRAY
//        //normalization step
//        D1=arrayTool(D1);
//        //D1=normalize(D1,tp.getMin(),tp.getMax());
//        D2=arrayTool(D2);
//        //D2=normalize(D2,tp.getMin(),tp.getMax());       
//        //absoluted difference step        
//        double[][][][] DifData=absDiffOp(D1,D2,true,"");
//        
//        //UPDATE SIM
//        sim.setVisible(false);
//        sim.setTraceParam(tp);
//        sim.setMaxDuration(DifData[0].length);
//        sim.setCurrDataAccum(DifData[0].length);
//        sim.setCurrTimeIncr(0);
//        sim.setModelInputTextField(" n/a ");                    
//        sim.setD(DifData);
//        sim.remove(sim.getWordGraph()); sim.remove(sim.getFeatureGraph()); sim.remove(sim.getPhonemeGraph()); sim.remove(sim.getInputGraph()); 
//        sim.setInputGraph(new edu.uconn.psy.jtrace.UI.InputGraphViewer(DifData[0][0],maxDuration,1,tp.getMin(),tp.getMax()));                                                    
//        sim.setFeatureGraph(new edu.uconn.psy.jtrace.UI.FeatureGraphViewer(DifData[1][0],maxDuration,1,tp.getMin(),tp.getMax()));                        
//        sim.setPhonemeGraph(new edu.uconn.psy.jtrace.UI.PhonemeGraphViewer(DifData[2][0],maxDuration,1,tp.getMin(),tp.getMax()));        
//        sim.setWordGraph(new edu.uconn.psy.jtrace.UI.WordGraphViewer(DifData[3][0],maxDuration,1,tp.getMin(),tp.getMax(),words1));
//        sim.initTraceGraphs();
//        sim.setFrameAll(0);
//        sim.disableGraphToggle();
//        sim.registerMouseMotionListeners();
//        sim.setVisible(true);                            
//        done=true;
    }
    
    //load SIM data from setupPanel 
    private void jobFive(){
//        System.gc();
//        Runtime.getRuntime().gc();       
//        progress=0;
//        int tick=0;
//        java.io.File dir[]=path.listFiles();
//        max=dir.length;
//        edu.uconn.psy.jtrace.IO.WTFileReader FR;        
//        int dontCount=0;
//        for(int i=0;i<dir.length;i++)                
//            if(!dir[i].getName().startsWith("word")&&!dir[i].getName().startsWith("input")&&
//               !dir[i].getName().startsWith("feat")&&!dir[i].getName().startsWith("phon"))
//                dontCount++;                    
//        int dataAccum=(int)((dir.length-dontCount)/4);
//        System.out.println("# files="+dir.length+"  ... "+dataAccum); 
//        double[][][] inputD=new double[dataAccum][][];
//        double[][][] featureD=new double[dataAccum][][];
//        double[][][] phonemeD=new double[dataAccum][][];
//        double[][][] wordD=new double[dataAccum][][];
//        String[] wordLabels=new String[dataAccum];
//        System.out.println("Array Sizes: "+dataAccum);            
//
//        for(int i=0;i<dir.length;i++){
//             progress++;
//             System.out.print(".");             
//             String name=dir[i].getName();
//             if(name.startsWith("param")){ 
//                 FR=new edu.uconn.psy.jtrace.IO.WTFileReader(dir[i]);
//                 tp=FR.readWTParameterFile();
//                 wtgui.loadParameterFile(dir[i]);
//                 tick++;
//                 continue; 
//             }
//
//             FR=new edu.uconn.psy.jtrace.IO.WTFileReader(dir[i]);
//             if(dir[i].getName().startsWith("feat")){
//                    featureD[i-tick]=FR.readWTSimData('f');
//                    //System.out.println("featureD["+i+"]="+featureD[i].length+"x"+featureD[i][0].length);
//             }
//             else if(dir[i].getName().startsWith("input")){
//                    inputD[i-dataAccum-tick]=FR.readWTSimData('i');
//                    //System.out.println("inputD["+(i-currDataAccum)+"]="+inputD[(i-currDataAccum)].length+"x"+inputD[(i-currDataAccum)][0].length);
//             }                 
//             else if(dir[i].getName().startsWith("phon")){
//                    phonemeD[i-(2*dataAccum)-tick]=FR.readWTSimData('p');
//                    //System.out.println("phonemeD["+(i-1-(2*currDataAccum))+"]="+phonemeD[(i-1-(2*currDataAccum))].length+"x"+phonemeD[(i-1-(2*currDataAccum))][0].length);
//             }
//             else if(dir[i].getName().startsWith("word")){
//                    wordD[i-(3*dataAccum)-tick]=FR.readWTSimData('w');
//                    //System.out.println("wordD["+(i-1-(3*currDataAccum))+"]="+wordD[(i-1-(3*currDataAccum))].length+"x"+wordD[(i-1-(3*currDataAccum))][0].length);
//                    wordLabels=FR.readWordLabels();
//             }
//             else{
//                 tick++;
//             }
//        }
//
//        //now create the simulation tab
//        final edu.uconn.psy.jtrace.UI.WTSimulationPanel sim=new edu.uconn.psy.jtrace.UI.WTSimulationPanel(inputD,featureD,phonemeD,wordD,wordLabels,tp);
//        sim.setDoubleBuffered(true);            
//        sim.setSize(1200, 650);                                
//        sim.revalidate();
//        wtgui.addTab(new String("SIM/"+path.getName()), sim);
//        wtgui.registerSimPanelListeners(sim);
//        done=true;
    }
     
    public void jobSix(){
    }
    
    //#7 : regression test
    //NOTE :
    /*jobSeven implements regression testing, which proceeds as follows:
    1. load cTrace data from files, 2. instantiate a TraceNet based on parameter
     *file in cTrace directory; collect data from the jTrace simulations
     *3. compare jTrace and cTrace data in multiple ways 4. print or save 
     *those comparisons.
     */
    public void jobSeven(){
        /*
        System.gc();
        Runtime.getRuntime().gc();       
        progress=0;
        main : for(int h=0;h<dir.length;h++){
            if(!dir[h].isDirectory() ||
                dir[h].listFiles().length<4) continue;
            F2=dir[h];
            File simFiles[]=F2.listFiles();                        
            //make sure of existence of a parameter file.
            for(int i=0;i<simFiles.length;i++){
               if(simFiles[i].getName().startsWith("parameters")) break;
               else if(i==simFiles.length-1) continue main;
            }
            progress=0;
            progressMessage="Working : "+dir[h].getName();            
            //READING IN F2            
            edu.uconn.psy.jtrace.IO.WTFileReader FR;
            int tick=0;
            int currTimeIncr = 0;
            for(int i=0;i<simFiles.length;i++)                
                if(!simFiles[i].getName().startsWith("word")&&!simFiles[i].getName().startsWith("input")&&
                   !simFiles[i].getName().startsWith("feat")&&!simFiles[i].getName().startsWith("phon"))
                    tick++;            
            int currDataAccum = (int)((simFiles.length-tick)/4); 
            tick=0;
            int maxDuration = currDataAccum;
            max=simFiles.length+maxDuration;            
            //System.out.println("F2 # files="+simFiles.length+"  ... "+currDataAccum); 
            D2=new double[4][currDataAccum][][];
            String[] words1=wordLabels, words2={"",""}, phons1=phonemeLabels, phons2={"",""};        
            //System.out.println("F2 Array Sizes: "+currDataAccum);                            
            for(int i=0;i<simFiles.length;i++){
                 System.gc();
                 Runtime.getRuntime().gc();                                
                 String name=simFiles[i].getName();
                 FR=new edu.uconn.psy.jtrace.IO.WTFileReader(simFiles[i]);             
                 if(name.startsWith("parameter")){ 
                     //System.out.println("***");
                     FR=new edu.uconn.psy.jtrace.IO.WTFileReader(simFiles[i]);
                     tp=FR.readWTParameterFile(); 
                     tick++;
                 }            
                 else if(name.startsWith("input"))
                        D2[0][i-tick-currDataAccum]=FR.readWTSimData('i');                               
                 else if(name.startsWith("feat"))
                        D2[1][i-tick]=FR.readWTSimData('f');                    
                 else if(name.startsWith("phon")){
                        D2[2][i-tick-(2*currDataAccum)]=FR.readWTSimData('p');                    
                        phons2=FR.readPhonemeLabels();
                 }
                 else if(name.startsWith("word")){
                        D2[3][i-tick-(3*currDataAccum)]=FR.readWTSimData('w');
                        words2=FR.readWordLabels();
                 }
                 else tick++;         
                 progress++;
            }
            //USE tp TO CREATE A TraceNet OBJECT AND BUILD UP D1 DATA, of length maxDuration.
            D1=new double[4][maxDuration][][];
            //set up phoneme continuum if one exists (this should probably be handled in tracenet)
            if(tp.getContinuumSpec()!=null&&tp.getContinuumSpec().length()>0){
                if(tp.getContinuumSpec().trim().length()==3){
                    int step=(new Integer(new Character(tp.getContinuumSpec().trim().charAt(2)).toString())).intValue();
                    if(step>1&&step<10)
                        (new edu.uconn.psy.jtrace.Model.TracePhones()).makePhonemeContinuum(tp.getContinuumSpec().trim().charAt(0),tp.getContinuumSpec().trim().charAt(1),step);
                }
            }
            edu.uconn.psy.jtrace.Model.TraceNet net=new edu.uconn.psy.jtrace.Model.TraceNet(tp);                        
            int[][][] phonemeRoundingTest=new int[maxDuration][edu.uconn.psy.jtrace.Model.TracePhones.NPHONS][4];
            net.createInput(tp.getModelInput());
            for(int n=0;n<edu.uconn.psy.jtrace.Model.TracePhones.NPHONS;n++)
                for(int m=0;m<4;m++)
                    phonemeRoundingTest[0][n][m]=net.phonTest[n][m];
            double[][] in=net.getInputLayer();
            double[][] ft=net.getFeatureLayer();
            double[][] ph=net.getPhonemeLayer();
            double[][] wd=net.getWordLayer();
            
            D1[0][0]=new double[in.length][in[0].length];
            for(int i=0;i<in.length;i++)
                for(int j=0;j<in[0].length;j++)
                    D1[0][0][i][j]=in[i][j];
            D1[1][0]=new double[ft.length][ft[0].length];
            for(int i=0;i<ft.length;i++)
                for(int j=0;j<ft[0].length;j++)
                    D1[1][0][i][j]=ft[i][j];
            D1[2][0]=new double[ph.length][ph[0].length];
            for(int i=0;i<ph.length;i++)
                for(int j=0;j<ph[0].length;j++)
                    D1[2][0][i][j]=ph[i][j];
            D1[3][0]=new double[wd.length][wd[0].length];
            for(int i=0;i<wd.length;i++)
                for(int j=0;j<wd[0].length;j++)
                    D1[3][0][i][j]=wd[i][j];
            
            
            for(int k=1;k<maxDuration;k++){                
                double[][][] d=net.cycle();
                for(int n=0;n<edu.uconn.psy.jtrace.Model.TracePhones.NPHONS;n++)
                    for(int m=0;m<4;m++)
                        phonemeRoundingTest[k][n][m]=net.phonTest[n][m];
                wd=d[0]; //word
                ft=d[1]; //feat
                ph=d[2]; //phon
                in=d[3]; //inpu
                D1[0][k]=new double[in.length][in[0].length];
                for(int i=0;i<in.length;i++)
                    for(int j=0;j<in[0].length;j++)
                        D1[0][k][i][j]=in[i][j];
                D1[1][k]=new double[ft.length][ft[0].length];
                for(int i=0;i<ft.length;i++)
                    for(int j=0;j<ft[0].length;j++)
                        D1[1][k][i][j]=ft[i][j];
                D1[2][k]=new double[ph.length][ph[0].length];
                for(int i=0;i<ph.length;i++)
                    for(int j=0;j<ph[0].length;j++)
                        D1[2][k][i][j]=ph[i][j];
                D1[3][k]=new double[wd.length][wd[0].length];
                for(int i=0;i<wd.length;i++)
                    for(int j=0;j<wd[0].length;j++)
                        D1[3][k][i][j]=wd[i][j];
                progress++;                                             
            }
            //save phoneme round test data!
            edu.uconn.psy.jtrace.IO.WTFileWriter fw=new edu.uconn.psy.jtrace.IO.WTFileWriter(F2.getPath(),"aRoundingValidation.txt");
            fw.write("Phoneme unit number rounding verification.");
            fw.write("Model input: "+tp.getModelInput()+"\n");
            fw.write("8 Fields: phon, cycle_no, steps.FP, steps.PP, steps.WP, , , , , , , cumulative abs error, jTrace(cycle phon 8),cTrace(cycle phon ##) \n");            
            for(int n=0;n<15;n++){
                for(int m=0;m<D1[2].length&&m<D2[2].length;m++){
                    String next="";
                    next+="/"+edu.uconn.psy.jtrace.Model.TracePhones.toChar(n)+"/, ";
                    next+=m+", ";
                    next+=phonemeRoundingTest[m][n][0]+", ";
                    next+=phonemeRoundingTest[m][n][1]+", ";
                    next+=phonemeRoundingTest[m][n][2]+", ";
                    next+=phonemeRoundingTest[m][n][3]+", , , , , ,";
                    next+=Math.abs(D1[2][m][n][phonTestIndex]-D2[2][m][n][phonTestIndex])+", ";
                    next+=D1[2][m][n][phonTestIndex]+", ";
                    next+=D2[2][m][n][phonTestIndex]+", ";
                    fw.write(next+"\n");
                }
                fw.write("\n\n\n");
            }
            fw.writeAndClose("\nEOF");
            //end phoneme rounding test non-sense
            
            
            //NOW WE HAVE D1 AND D2.  
            //CALCULATE DIFFERENCE ARRAY
            D1=arrayTool(D1);
            D2=arrayTool(D2);
            System.gc();
            Runtime.getRuntime().gc();        
            
            //What are the three most active words in the sim?  #1          
            int w[][]={{0,0,0},{0,0,0}};
            words1=tp.getLexicon().toStringArray();
            for(int i=0;i<D1[3].length;i++){
                for(int j=0;j<D1[3][i].length;j++){
                    for(int k=0;k<D1[3][i][j].length;k++){
                        //System.out.print("|"+i+" "+j+" "+k+" | ");                                                        
                        if(D1[3][i][j][k]>D1[3][i][w[0][0]][w[1][0]]&&j!=w[0][0]&&j!=w[0][1]&&j!=w[0][2]){
                            //System.out.println("\ncandidate max "+h+": "+words1[j]+", "+D1[3][i][j][k]);
                            w[0][2]=w[0][1];
                            w[1][2]=w[1][1];
                            w[0][1]=w[0][0];
                            w[1][1]=w[1][0];
                            w[0][0]=j;
                            w[1][0]=k;
                        }
                        else if(D1[3][i][j][k]>D1[3][i][w[0][1]][w[1][1]]&&j!=w[0][0]&&j!=w[0][1]&&j!=w[0][2]){
                            //System.out.println("\ncandidate max 1: "+words1[j]+", "+D1[3][i][j][k]);                            
                            w[0][2]=w[0][1];
                            w[1][2]=w[1][1];
                            w[0][1]=j;
                            w[1][1]=k;
                        }
                        else if(D1[3][i][j][k]>D1[3][i][w[0][2]][w[1][2]]&&j!=w[0][0]&&j!=w[0][1]&&j!=w[0][2]){
                            //System.out.println("\ncandidate max 2: "+words1[j]+", "+D1[3][i][j][k]);                            
                            w[0][2]=j;
                            w[1][2]=k;
                        }
                    }
                }
            }                        
            String report="Three most activated word units in jTrace: "+tp.getLexicon().elementAt(w[0][0]).getPhon()+" ("+w[1][0]+"), "+
                tp.getLexicon().elementAt(w[0][1]).getPhon()+" ("+w[1][1]+"), "+tp.getLexicon().elementAt(w[0][2]).getPhon()+" ("+w[1][2]+").";                
            //System.out.println(report);
            //^^ What are the three most active words in the sim? ^^
            
            //What are the three most active words in the sim?  #2          
            w=new int[2][3];
            words1=tp.getLexicon().toStringArray();
            for(int i=0;i<D2[3].length;i++){
                for(int j=0;j<D2[3][i].length;j++){
                    for(int k=0;k<D2[3][i][j].length;k++){
                        //System.out.print("|"+i+" "+j+" "+k+" | ");                                                        
                        if(D2[3][i][j][k]>D2[3][i][w[0][0]][w[1][0]]&&j!=w[0][0]&&j!=w[0][1]&&j!=w[0][2]){
                            //System.out.println("\ncandidate max "+h+": "+words1[j]+", "+D2[3][i][j][k]);
                            w[0][2]=w[0][1];
                            w[1][2]=w[1][1];
                            w[0][1]=w[0][0];
                            w[1][1]=w[1][0];
                            w[0][0]=j;
                            w[1][0]=k;                            
                        }
                        else if(D2[3][i][j][k]>D2[3][i][w[0][1]][w[1][1]]&&j!=w[0][0]&&j!=w[0][1]&&j!=w[0][2]){
                            //System.out.println("\ncandidate max 1: "+words1[j]+", "+D2[3][i][j][k]);                            
                            w[0][2]=w[0][1];
                            w[1][2]=w[1][1];
                            w[0][1]=j;
                            w[1][1]=k;                            
                        }
                        else if(D2[3][i][j][k]>D2[3][i][w[0][2]][w[1][2]]&&j!=w[0][0]&&j!=w[0][1]&&j!=w[0][2]){
                            //System.out.println("\ncandidate max 2: "+words1[j]+", "+D2[3][i][j][k]);                            
                            w[0][2]=j;
                            w[1][2]=k;                         
                        }
                    }
                }
            }                        
            report+="\nThree most activated word units in cTrace: "+tp.getLexicon().elementAt(w[0][0]).getPhon()+" ("+w[1][0]+"), "+
                    tp.getLexicon().elementAt(w[0][1]).getPhon()+" ("+w[1][1]+"), "+tp.getLexicon().elementAt(w[0][2]).getPhon()+" ("+w[1][2]+").";                
            //System.out.println(report);
            //^^ What are the three most active words in the sim? ^^
            
            
            //absoluted difference step : error report file is automatically
            //saved to the directory of F2.
            double[][][][] DifData=absDiffOp(D1,D2,false,report);
            
            w=null; wd=null; ph=null; ft=null; in=null; 
            D1=null; D2=null; DifData=null; tp=null; simFiles=null; F2=null; net=null; //clear values
            (new edu.uconn.psy.jtrace.Model.TracePhones()).clearPhonemeContinuum();            
            System.gc();
            Runtime.getRuntime().gc();            
        }
        progress=max;
        done=true;     
         */   
    }
        
    public int getMax(){
        return max;
    }
    public int getProgress(){
        return progress;
    }
    public boolean isDone(){return done;}
      
    
    //absolute difference operation between two 4d arrays.
    //reports error between the two sets
    public double[][][][] absDiffOp(double[][][][] D1, double[][][][] D2, boolean popup, String addendum){
       System.gc();
       Runtime.getRuntime().gc();
       //System.out.println("Calculating simulation comparison.");
       double[][][][] DD=new double[4][][][];    
       if(D1.length!=4||D2.length!=4) return DD;
       DD[0]= new double[Math.min(D1[0].length,D2[0].length)][Math.min(D1[0][0].length,D2[0][0].length)][Math.min(D1[0][0][0].length,D2[0][0][0].length)];
       DD[1]= new double[Math.min(D1[1].length,D2[1].length)][Math.min(D1[1][0].length,D2[1][0].length)][Math.min(D1[1][0][0].length,D2[1][0][0].length)];
       DD[2]= new double[Math.min(D1[2].length,D2[2].length)][Math.min(D1[2][0].length,D2[2][0].length)][Math.min(D1[2][0][0].length,D2[2][0][0].length)];
       DD[3]= new double[Math.min(D1[3].length,D2[3].length)][Math.min(D1[3][0].length,D2[3][0].length)][Math.min(D1[3][0][0].length,D2[3][0][0].length)];
       //TODO : add a mean square error calculation and report here.
       double[][] err = new double[4][DD[0].length];
       double[][][] phonErr= new double[Math.min(D1[2].length,D2[2].length)][Math.min(D1[2][0].length,D2[2][0].length)][Math.min(D1[2][0][0].length,D2[2][0][0].length)];
       double[][][] wordErr= new double[Math.min(D1[3].length,D2[3].length)][Math.min(D1[3][0].length,D2[3][0].length)][Math.min(D1[3][0][0].length,D2[3][0][0].length)];
       double inME=0d, ftME=0d, phME=0d, wdME=0d;
       double inMAX=0d, ftMAX=0d, phMAX=0d, wdMAX=0d;
       int wdThree[][]=new int[3][3];
       if(sim!=null) wordLabels=sim.getWordLabels();
       else wordLabels=tp.getLexicon().toStringArray();
       double min;
       double max;       
       if(sim!=null){ min=sim.getMin(); max=sim.getMax();}
       else if(tp!=null){min=tp.getMin(); max=tp.getMax();}
       else{min=-0.3; max=1.0;}
       double val, anotherVal;
       int errorTracker[][]=new int[4][11]; //how many difference values are over onePercent??
       double errorTrackerPercentages[][]=new double[4][11];
       double pointOnePercent = ((max-min)*0.1)/100;
       double halfPercent = ((max-min)*0.5)/100;
       double onePercent = ((max-min)*1.0)/100;
       double twoPercent = ((max-min)*2.0)/100;
       double threePercent = ((max-min)*3.0)/100;
       double fourPercent = ((max-min)*4.0)/100;
       double fivePercent = ((max-min)*5.0)/100;
       double tenPercent = ((max-min)*10.0)/100;
       double twentyPercent = ((max-min)*20.0)/100;
       double fiftyPercent = ((max-min)*50.0)/100;
       double hundredPercent = ((max-min)*100.0)/100;
       
       for(int i=0;i<DD.length;i++)
           for(int j=0;j<DD[i].length;j++){
               for(int k=0;k<DD[i][j].length;k++)
                   for(int h=0;h<DD[i][j][k].length;h++){
                       //DD[i][j][k][h]=Math.abs(D1[i][j][k][h]-D2[i][j][k][h]);
                       //if(DD[i][j][k][h]<0.001) DD[i][j][k][h]=0;
                       
                       val=Math.abs(D1[i][j][k][h]-D2[i][j][k][h]);                       
                       DD[i][j][k][h]=val;
                       anotherVal = (D2[i][j][k][h]-D1[i][j][k][h]);
                       
                       //summing up the error values
                       if(i==0){ 
                           err[0][j]+=val;
                           //inME+=val;
                           if(val>inMAX) inMAX=val;
                           
                           if(val<pointOnePercent) errorTracker[0][0]++;
                           else if(val>=pointOnePercent&&val<halfPercent) errorTracker[0][1]++;                           
                           else if(val>=halfPercent&&val<onePercent) errorTracker[0][2]++;
                           else if(val>=onePercent&&val<twoPercent) errorTracker[0][3]++;
                           else if(val>=twoPercent&&val<threePercent) errorTracker[0][4]++;
                           else if(val>=threePercent&&val<fourPercent) errorTracker[0][5]++;
                           else if(val>=fourPercent&&val<fivePercent) errorTracker[0][6]++;
                           else if(val>=fivePercent&&val<tenPercent) errorTracker[0][7]++;
                           else if(val>=tenPercent&&val<twentyPercent) errorTracker[0][8]++;
                           else if(val>=twentyPercent&&val<fiftyPercent) errorTracker[0][9]++;
                           else if(val>=fiftyPercent&&val<=hundredPercent) errorTracker[0][10]++;                           
                       }
                       if(i==1){ 
                           err[1][j]+=val;
                           //ftME+=val;
                           if(val>ftMAX) ftMAX=val;
                           
                           if(val<pointOnePercent) errorTracker[1][0]++;
                           else if(val>=pointOnePercent&&val<halfPercent) errorTracker[1][1]++;                           
                           else if(val>=halfPercent&&val<onePercent) errorTracker[1][2]++;
                           else if(val>=onePercent&&val<twoPercent) errorTracker[1][3]++;
                           else if(val>=twoPercent&&val<threePercent) errorTracker[1][4]++;
                           else if(val>=threePercent&&val<fourPercent) errorTracker[1][5]++;
                           else if(val>=fourPercent&&val<fivePercent) errorTracker[1][6]++;
                           else if(val>=fivePercent&&val<tenPercent) errorTracker[1][7]++;
                           else if(val>=tenPercent&&val<twentyPercent) errorTracker[1][8]++;
                           else if(val>=twentyPercent&&val<fiftyPercent) errorTracker[1][9]++;
                           else if(val>=fiftyPercent&&val<=hundredPercent) errorTracker[1][10]++;
                       }
                       if(i==2){
                           err[2][j]+=val;                           
                           phonErr[j][k][h]=anotherVal;
                           //phME+=val;
                           if(val>phMAX) phMAX=val;
                           
                           if(val<pointOnePercent) errorTracker[2][0]++;
                           else if(val>=pointOnePercent&&val<halfPercent) errorTracker[2][1]++;                           
                           else if(val>=halfPercent&&val<onePercent) errorTracker[2][2]++;
                           else if(val>=onePercent&&val<twoPercent) errorTracker[2][3]++;
                           else if(val>=twoPercent&&val<threePercent) errorTracker[2][4]++;
                           else if(val>=threePercent&&val<fourPercent) errorTracker[2][5]++;
                           else if(val>=fourPercent&&val<fivePercent) errorTracker[2][6]++;
                           else if(val>=fivePercent&&val<tenPercent) errorTracker[2][7]++;
                           else if(val>=tenPercent&&val<twentyPercent) errorTracker[2][8]++;
                           else if(val>=twentyPercent&&val<fiftyPercent) errorTracker[2][9]++;
                           else if(val>=fiftyPercent&&val<=hundredPercent) errorTracker[2][10]++;
                       }
                       if(i==3){
                           //what 3 word units have the greatest error?             
                           //System.out.println("calculating the 3 word units with greatest error. ("+wordLabels.length+")");
                           //System.out.print("|"+i+" "+j+" "+k+" | ");                                                        
                           if(val>Math.abs(D1[i][wdThree[2][0]][wdThree[0][0]][wdThree[1][0]]-D2[i][wdThree[2][0]][wdThree[0][0]][wdThree[1][0]])){
                               wdThree[0][2]=wdThree[0][1];
                               wdThree[1][2]=wdThree[1][1];
                               wdThree[2][2]=wdThree[2][1];
                               wdThree[0][1]=wdThree[0][0];
                               wdThree[1][1]=wdThree[1][0];
                               wdThree[2][1]=wdThree[2][0];
                               wdThree[0][0]=k; //word where max is
                               wdThree[1][0]=h; //slice where max is
                               wdThree[2][0]=j; //cycle where max is
                           }
                           else if(val>Math.abs(D1[i][wdThree[2][1]][wdThree[0][1]][wdThree[1][1]]-D2[i][wdThree[2][1]][wdThree[0][1]][wdThree[1][1]])){
                               wdThree[0][2]=wdThree[0][1];
                               wdThree[1][2]=wdThree[1][1];
                               wdThree[2][2]=wdThree[2][1];
                               wdThree[0][1]=k; //word where max is
                               wdThree[1][1]=h; //slice where max is
                               wdThree[2][1]=j; //cycle where max is
                           }
                           else if(val>Math.abs(D1[i][wdThree[2][2]][wdThree[0][2]][wdThree[1][2]]-D2[i][wdThree[2][2]][wdThree[0][2]][wdThree[1][2]])){
                               wdThree[0][2]=k; //word where max is
                               wdThree[1][2]=h; //slice where max is
                               wdThree[2][2]=j; //cycle where max is
                           }
                           //^^ what 3 word units have the greatest error? ^^             
                           err[3][j]+=val;                           
                           wordErr[j][k][h]=anotherVal;
                           //wdME+=val;
                           if(val>wdMAX){ 
                               wdMAX=val;
                               //System.out.println(wdMAX+" <- wd[word "+j+"][slice "+k+"][cycle "+h+"]");
                           }
                           
                           if(val<pointOnePercent) errorTracker[3][0]++;
                           else if(val>=pointOnePercent&&val<halfPercent) errorTracker[3][1]++;                           
                           else if(val>=halfPercent&&val<onePercent) errorTracker[3][2]++;
                           else if(val>=onePercent&&val<twoPercent) errorTracker[3][3]++;
                           else if(val>=twoPercent&&val<threePercent) errorTracker[3][4]++;
                           else if(val>=threePercent&&val<fourPercent) errorTracker[3][5]++;
                           else if(val>=fourPercent&&val<fivePercent) errorTracker[3][6]++;
                           else if(val>=fivePercent&&val<tenPercent) errorTracker[3][7]++;
                           else if(val>=tenPercent&&val<twentyPercent) errorTracker[3][8]++;
                           else if(val>=twentyPercent&&val<fiftyPercent) errorTracker[3][9]++;
                           else if(val>=fiftyPercent&&val<=hundredPercent) errorTracker[3][10]++;
                       }
                       //summing up the squared error values              
                   }
               err[0][j]=100*((err[0][j]/(DD[0][0].length*DD[0][0][0].length))/(max-min));
               err[1][j]=100*((err[1][j]/(DD[1][0].length*DD[1][0][0].length))/(max-min));
               err[2][j]=100*((err[2][j]/(DD[2][0].length*DD[2][0][0].length))/(max-min));
               err[3][j]=100*((err[3][j]/(DD[3][0].length*DD[3][0][0].length))/(max-min));               
               //System.out.println("Err-"+j+": "+err[0][j]+"  "+err[1][j]+"  "+err[2][j]+"  "+err[3][j]);
           }
       
       //mean-ing the summed error values.
       for(int i=0;i<err[0].length;i++){
           inME+=err[0][i];
           ftME+=err[1][i];
           phME+=err[2][i];
           wdME+=err[3][i];           
       }
       inME/=err[0].length;
       ftME/=err[0].length;
       phME/=err[0].length;
       wdME/=err[0].length;
       double SMAD=(inME+ftME+phME+wdME)/4;
       String modelInput;
       if(sim!=null){ modelInput=sim.getModelInput();}
       else if(tp!=null){modelInput=tp.getModelInput();}
       else{modelInput="-";}       
       
       System.out.println("\t"+modelInput+"\t"+SMAD+"\t"+wdThree[1][0]+"\t\t"+inME+"\t"+ftME+"\t"+phME+"\t"+wdME);
       
       /*System.out.println("mean error of input layers: "+inME+"%, max err= "+(100*(inMAX/(max-min)))+"%.");
       System.out.println("mean error of feature layers: "+ftME+"%, max err= "+(100*(ftMAX/(max-min)))+"%.");
       System.out.println("mean error of phoneme layers: "+phME+"%, max err= "+(100*(phMAX/(max-min)))+"%.");
       System.out.println("mean error of word layers: "+wdME+"%, max err= "+(100*(wdMAX/(max-min)))+"%.");
       System.out.println("top three error word units: "+wordLabels[wdThree[0][0]]+" ("+wdThree[1][0]+"), "+wordLabels[wdThree[0][1]]+" ("+wdThree[1][1]+"), "+wordLabels[wdThree[0][2]]+" ("+wdThree[1][2]+"). ");
       */
       /*String report="Mini simulation report, model input /"+modelInput+"/\n";
       report+="% mean error between input layers: "+inME+"%\n";
       report+="% mean error between feature layers: "+ftME+"%\n";
       report+="% mean error between phoneme layers: "+phME+"%\n";
       report+="% mean error between word layers: "+wdME+"%\n";
       report+="Top three error word units (cTrace-jTrace) : ";
       if((new Double(D2[3][wdThree[2][0]][wdThree[0][0]][wdThree[1][0]]-D1[3][wdThree[2][0]][wdThree[0][0]][wdThree[1][0]])).toString().length()>6)
           report+=wordLabels[wdThree[0][0]]+" (cyc"+wdThree[2][0]+", sli"+wdThree[1][0]+")= "+(new Double(D2[3][wdThree[2][0]][wdThree[0][0]][wdThree[1][0]]-D1[3][wdThree[2][0]][wdThree[0][0]][wdThree[1][0]])).toString().substring(0,6)+"/"+(max-min)+", ";
       else
           report+=wordLabels[wdThree[0][0]]+" (cyc"+wdThree[2][0]+", sli"+wdThree[1][0]+")= "+(new Double(D2[3][wdThree[2][0]][wdThree[0][0]][wdThree[1][0]]-D1[3][wdThree[2][0]][wdThree[0][0]][wdThree[1][0]])).toString()+"/"+(max-min)+", ";
       if((new Double(D2[3][wdThree[2][1]][wdThree[0][1]][wdThree[1][1]]-D1[3][wdThree[2][1]][wdThree[0][1]][wdThree[1][1]])).toString().length()>6) 
           report+=wordLabels[wdThree[0][1]]+" (cyc"+wdThree[2][1]+", sli"+wdThree[1][1]+")= "+(new Double(D2[3][wdThree[2][1]][wdThree[0][1]][wdThree[1][1]]-D1[3][wdThree[2][1]][wdThree[0][1]][wdThree[1][1]])).toString().substring(0,6)+"/"+(max-min)+", ";
       else
           report+=wordLabels[wdThree[0][1]]+" (cyc"+wdThree[2][1]+", sli"+wdThree[1][1]+")= "+(new Double(D2[3][wdThree[2][1]][wdThree[0][1]][wdThree[1][1]]-D1[3][wdThree[2][1]][wdThree[0][1]][wdThree[1][1]])).toString()+"/"+(max-min)+", ";
       if((new Double(D2[3][wdThree[2][2]][wdThree[0][2]][wdThree[1][2]]-D1[3][wdThree[2][2]][wdThree[0][2]][wdThree[1][2]])).toString().length()>6)
           report+=wordLabels[wdThree[0][2]]+" (cyc"+wdThree[2][2]+", sli"+wdThree[1][2]+")= "+(new Double(D2[3][wdThree[2][2]][wdThree[0][2]][wdThree[1][2]]-D1[3][wdThree[2][2]][wdThree[0][2]][wdThree[1][2]])).toString().substring(0,6)+"/"+(max-min)+", ";
       else
           report+=wordLabels[wdThree[0][2]]+" (cyc"+wdThree[2][2]+", sli"+wdThree[1][2]+")= "+(new Double(D2[3][wdThree[2][2]][wdThree[0][2]][wdThree[1][2]]-D1[3][wdThree[2][2]][wdThree[0][2]][wdThree[1][2]])).toString()+"/"+(max-min)+", ";
       report+="\n";
       //report+="Histogram (in comparing the cTrace and jTrace results, how does the distribution of error values break down):\n";
       for(int i=0;i<4;i++){
           int denom=DD[i].length*DD[i][0].length*DD[i][0][0].length;
           System.out.print("\n");
           for(int j=0;j<errorTracker[i].length;j++){
               errorTrackerPercentages[i][j]=(double)100*((double)errorTracker[i][j]/(double)denom);
               if(i==2||i==3)
                   System.out.print(errorTrackerPercentages[i][j]+",  ");
           }
       }
       //report+="Input layer: out of "+(DD[0].length*DD[0][0].length*DD[0][0][0].length)+" error units, "+errorTrackerPercentages[0][0]+"% are <0.1% error, "+errorTrackerPercentages[0][1]+"% are 0.1<x<0.5% error, "+errorTrackerPercentages[0][2]+"% are 0.5<x<1.0% error, "+errorTrackerPercentages[0][3]+"% are 1.0<x<2.0% error, "+errorTrackerPercentages[0][4]+"% are 2.0<x<3.0% error, "+errorTrackerPercentages[0][5]+"% are 3.0<x<4.0% error, "+errorTrackerPercentages[0][6]+"% are 4.0<x<5.0% error, "+errorTrackerPercentages[0][7]+"% are 5.0<x10.0% error, "+errorTrackerPercentages[0][8]+"5 are >10.0% error.\n";
       //report+="Feature layer: out of "+(DD[1].length*DD[1][0].length*DD[1][0][0].length)+" error units, "+errorTrackerPercentages[1][0]+"% are <0.1% error, "+errorTrackerPercentages[1][1]+"% are 0.1<x<0.5% error, "+errorTrackerPercentages[1][2]+"% are 0.5<x<1.0% error, "+errorTrackerPercentages[1][3]+"% are 1.0<x<2.0% error, "+errorTrackerPercentages[1][4]+"% are 2.0<x<3.0% error, "+errorTrackerPercentages[1][5]+"% are 3.0<x<4.0% error, "+errorTrackerPercentages[1][6]+"% are 4.0<x<5.0% error. "+errorTrackerPercentages[1][7]+"% are 5.0<x10.0% error, "+errorTrackerPercentages[1][8]+"5 are >10.0% error.\n";
       //report+="Phoneme layer: out of "+(DD[2].length*DD[2][0].length*DD[2][0][0].length)+" error units, "+errorTrackerPercentages[2][0]+"% are <0.1% error, "+errorTrackerPercentages[2][1]+"% are 0.1<x<0.5% error, "+errorTrackerPercentages[2][2]+"% are 0.5<x<1.0% error, "+errorTrackerPercentages[2][3]+"% are 1.0<x<2.0% error, "+errorTrackerPercentages[2][4]+"% are 2.0<x<3.0% error, "+errorTrackerPercentages[2][5]+"% are 3.0<x<4.0% error, "+errorTrackerPercentages[2][6]+"% are 4.0<x<5.0% error. "+errorTrackerPercentages[2][7]+"% are 5.0<x10.0% error, "+errorTrackerPercentages[2][8]+"5 are >10.0% error.\n";
       //report+="Word layer: out of "+(DD[3].length*DD[3][0].length*DD[3][0][0].length)+" error units, "+errorTrackerPercentages[3][0]+"% are <0.1% error, "+errorTrackerPercentages[3][1]+"% are 0.1<x<0.5% error, "+errorTrackerPercentages[3][2]+"% are 0.5<x<1.0% error, "+errorTrackerPercentages[3][3]+"% are 1.0<x<2.0% error, "+errorTrackerPercentages[3][4]+"% are 2.0<x<3.0% error, "+errorTrackerPercentages[3][5]+"% are 3.0<x<4.0% error, "+errorTrackerPercentages[3][6]+"% are 4.0<x<5.0% error. "+errorTrackerPercentages[3][7]+"% are 5.0<x10.0% error, "+errorTrackerPercentages[3][8]+"5 are >10.0% error.\n";
       report+="\n";
       report+="Raw Histogram Data (in,ft,ph,wd| error ranges {0.0-0.1%,0.1-0.5%,0.5-1.0%,1-2%,2-3%,3-4%,4-5%,5-10%,10-20%,20-50%,50-100%}):\n";
       for(int i=0;i<4;i++){
           for(int j=0;j<errorTracker[i].length;j++)
               report+=errorTrackerPercentages[i][j]+",  ";
           report+="\n";
       }
       report+="\n";
       report+=addendum;
       System.out.println("\n\n"+report);
       java.io.File targetFile = new java.io.File(F2.getPath(),"error_graph.xml");                                
       edu.uconn.psy.jtrace.IO.WTFileWriter w=new edu.uconn.psy.jtrace.IO.WTFileWriter(targetFile.getParent(),targetFile.getName());
       int spread;
       if(sim!=null) spread=sim.getDeltaInput(); //TODO: update the Schema so it's delta, not spread.
       else if(tp!=null) spread=tp.getDeltaInput();  //TODO: update all spread to delta...
       else spread=6;
       //write error graph, where err is averaged error for each layer
       w.writeErrorGraphDataFile(err,spread,modelInput,report);
       //write error graph which summarizes phoneme error       
       targetFile = new java.io.File(F2.getPath(),"error_graph_phoneme.xml");                                
       w=new edu.uconn.psy.jtrace.IO.WTFileWriter(targetFile.getParent(),targetFile.getName());
       w.writePhonemeErrorGraphFile(phonErr, spread, modelInput, report);
       String[] wordLabels;
       if(sim!=null){ wordLabels=sim.getWordLabels();}
       else{wordLabels=tp.getLexicon().toStringArray();} //tp!=null
       //write error graph which summarizes word error       
       targetFile = new java.io.File(F2.getPath(),"error_graph_word.xml");                                
       w=new edu.uconn.psy.jtrace.IO.WTFileWriter(targetFile.getParent(),targetFile.getName());
       w.writeWordErrorGraphFile(wordErr, spread, modelInput, report, wordLabels);
       
       if(popup){
           javax.swing.JOptionPane prompt1=new javax.swing.JOptionPane("...",javax.swing.JOptionPane.INFORMATION_MESSAGE,javax.swing.JOptionPane.OK_OPTION);
           prompt1.showMessageDialog(sim,report,"Comparison Error Report",javax.swing.JOptionPane.INFORMATION_MESSAGE);        
       }*/
       return DD;
    }
    //normalizes values in each row, with row sum as normalizer
    public double[][][][] normalize(double[][][][] D,double min,double max){        
        System.gc();
        Runtime.getRuntime().gc();
        //System.out.println("NORMALIZING");
        double[][][][] result=new double[4][][][];
        result[0]=new double[D[0].length][D[0][0].length][D[0][0][0].length];
        result[1]=new double[D[1].length][D[1][0].length][D[1][0][0].length];
        result[2]=new double[D[2].length][D[2][0].length][D[2][0][0].length];
        result[3]=new double[D[3].length][D[3][0].length][D[3][0][0].length];
        
        for(int i=0;i<result.length;i++)
           for(int j=0;j<result[i].length;j++)
               for(int k=0;k<result[i][j].length;k++){
                   double norm=0d;
                   for(int h=0;h<result[i][j][k].length;h++)
                       norm+=((D[i][j][k][h]-min)/(max-min));
                   for(int h=0;h<result[i][j][k].length;h++){
                       if(norm>0) result[i][j][k][h]=(((D[i][j][k][h]-min)/(max-min))/norm);                       
                       else result[i][j][k][h]=((D[i][j][k][h]-min)/(max-min));
                   }
               }
        return result;
    }
    
    //truncates a 4d array that contains empty entries on it's 2nd dimension.
    public double[][][][] arrayTool(double[][][][] D){
        System.gc();
        Runtime.getRuntime().gc();
        int length=D[0].length;
        for(int j=0;j<D[0].length;j++)
            if(D[0][j]==null){ 
                length=j-1;                    
                //System.out.println("*** "+j);    
                break;
            }    
        if(length==D[0].length) return D;
        
        double[][][][] result=new double[4][][][];
        result[0]=new double[length][D[0][0].length][D[0][0][0].length];
        result[1]=new double[length][D[1][0].length][D[1][0][0].length];
        result[2]=new double[length][D[2][0].length][D[2][0][0].length];
        result[3]=new double[length][D[3][0].length][D[3][0][0].length];
        
        for(int i=0;i<result.length;i++)
           for(int j=0;j<result[i].length;j++)
               for(int k=0;k<result[i][j].length;k++){
                   for(int h=0;h<result[i][j][k].length;h++){
                       try{
                           result[i][j][k][h]=D[i][j][k][h];
                       }
                       catch(Exception e){                           
                           System.out.println("???");                                                    
                           e.getCause().printStackTrace();
                           //System.out.println("D["+i+"]["+j+"]["+k+"]["+h+"]= "+D[i][j][k][h]);                                                    
                       }
                   }
               }
        return result;                   
    }
    public int getTaskNum(){return job;}
    public String getProgressMessage(){return progressMessage;}
}

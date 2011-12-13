/*
 * TraceScript.java
 *
 * Created on May 2, 2005, 12:42 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import edu.uconn.psy.jtrace.Model.Scripting.impl.*;
import edu.uconn.psy.jtrace.Model.*;
import edu.uconn.psy.jtrace.IO.*;
import edu.uconn.psy.jtrace.UI.*;
/**
 *
 * @author tedstrauss
 */
public class TraceScript{
    
    String description;
    String rootDirectory;
    edu.uconn.psy.jtrace.UI.jTRACEMDI gui;    
    Expression[] expressions;
    edu.uconn.psy.jtrace.Model.TraceParam baseParam;
    edu.uconn.psy.jtrace.Model.TraceParam currParam;
    edu.uconn.psy.jtrace.Model.TraceSim currSim;
    edu.uconn.psy.jtrace.Model.TraceSimAnalysis currAnalysis;
    edu.uconn.psy.jtrace.UI.GraphParameters currGraphParameters;
        
    //progress
    edu.uconn.psy.jtrace.IO.ScriptingThread scriptthread;
    javax.swing.Timer scripttimer;
    int totalIterations;
    int progress;
    
    //gui versions/non-gui versions
    boolean hasGui;
    boolean BREAK;
    
    private File jtFileName;
    String loadedFilePath;
    //parameters specific to this script (i.e. above the expressions list)
    int cyclesPerSim;
        
    /** Creates a new instance of TraceScript */
    public TraceScript(edu.uconn.psy.jtrace.Model.TraceParam base, Expression[] e){        
        
        BREAK = false;
        baseParam = base;
        if(null==baseParam)
            currParam = new edu.uconn.psy.jtrace.Model.TraceParam();
        else
            currParam = base;
        currSim = new edu.uconn.psy.jtrace.Model.TraceSim(currParam);
        currAnalysis = new edu.uconn.psy.jtrace.Model.TraceSimAnalysis();
        currGraphParameters = new edu.uconn.psy.jtrace.UI.GraphParameters();

        //params=new LinkedList();
        cyclesPerSim=100;
        expressions=e;   
        hasGui=true;
        rootDirectory = traceProperties.rootPath.getAbsolutePath(); 
        countIterations();                    
    }    
    public TraceScript(edu.uconn.psy.jtrace.Model.TraceParam base){
        BREAK = false;
        baseParam = base;
        if(null==baseParam)
            currParam = new edu.uconn.psy.jtrace.Model.TraceParam();
        else
            currParam = base;        
        currParam = new edu.uconn.psy.jtrace.Model.TraceParam();
        currSim = new edu.uconn.psy.jtrace.Model.TraceSim(currParam);
        currAnalysis = new edu.uconn.psy.jtrace.Model.TraceSimAnalysis();
        currGraphParameters = new edu.uconn.psy.jtrace.UI.GraphParameters();

        //params=new LinkedList();
        cyclesPerSim=100;                
        hasGui=true;
        rootDirectory = traceProperties.rootPath.getAbsolutePath(); 
        countIterations();
    }
    public TraceScript(){
        new TraceScript(null);
    }
    
    //this is the GUI version
    public void interpret(final javax.swing.JDialog progressFrame){        
        if(BREAK) return;
        hasGui=true;                
        progress=0;
        countIterations();
        System.out.println("total iterations: "+totalIterations);
        if(totalIterations>0)
            ((javax.swing.JProgressBar)progressFrame.getContentPane().getComponent(0)).setMaximum(totalIterations);
        else
            ((javax.swing.JProgressBar)progressFrame.getContentPane().getComponent(0)).setIndeterminate(true);
        progress=0;
        
        javax.swing.JButton cancelButton=((javax.swing.JButton)progressFrame.getContentPane().getComponent(1));
        scriptthread = new edu.uconn.psy.jtrace.IO.ScriptingThread(this);
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                int confirm=javax.swing.JOptionPane.showConfirmDialog(progressFrame,"Stop the script?\n","Confirm",javax.swing.JOptionPane.OK_CANCEL_OPTION,javax.swing.JOptionPane.QUESTION_MESSAGE);
                if(confirm==javax.swing.JOptionPane.CANCEL_OPTION){ return; }                        
                else{
                    scriptthread.BREAK();                    
                }                
            }            
        });
        //TODO: send update messages to the label on the progress frame; via timer a listener...        
        scripttimer = new javax.swing.Timer(150, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateProgressBar(progressFrame);
            }            
        });
        scriptthread.start();
        scripttimer.start();                 
    }
    //this is the remote/no-gui version
    public void interpret(){
        if(BREAK) return;
        hasGui=false;
        countIterations();
        System.out.println("Executing a script.");            
        interpretScript();
        System.out.println("Done.");
        System.out.flush();
    }
    public void interpretScript(){        
        if(BREAK) return;
        
        if(null==baseParam)
            currParam = new edu.uconn.psy.jtrace.Model.TraceParam();
        else
            currParam = baseParam;        
        
        for(int i=0;i<expressions.length;i++){
            //TODO: if the first expr is a set-param, then put that in base param tab
            interpretExpression(expressions[i]);                    
        }
        System.out.println("Breaking ...");
        if(null!=scriptthread) scriptthread.BREAK();
        System.gc();
    }
    
    public void oy(){
        javax.swing.JInternalFrame[] dows = gui.getAllFrames();
        for(int i=0;i<dows.length;i++)
            if(dows[i] instanceof jTRACE)
                System.out.println("frame["+i+"]==null : "+(((jTRACE)dows[i]).getSim().isNullPointer()));
        System.out.println();
    }
    
    private void interpretExpression(Expression e){
        if(BREAK) return;
        e = (Expression)e.clone();
        if(e instanceof Action){
            interpretAction((Action)e);
        }
        else if(e instanceof Conditional){
            interpretConditional((Conditional)e);
        }
        if(e instanceof Iterator){
            interpretIterator((Iterator)e);
        }
        System.gc();
    }    
    private void interpretConditional(Conditional cond){
        if(BREAK) return;
        cond = (Conditional)cond.clone();
        if(interpretPredicate(cond.getPredicate())){
            for(int i=0;i<cond.thenExpressions().length;i++)
                interpretExpression(cond.thenExpressions()[i]);
        }
        else{
            for(int i=0;i<cond.elseExpressions().length;i++)
                interpretExpression(cond.elseExpressions()[i]);
        }
        System.gc();
    }
    private void interpretIterator(Iterator iter){        
        if(BREAK) return;
        iter = (Iterator)iter.clone();
        if(iter instanceof LexicalIterator){
            //if lexicon is specified, that means use the current lexicon: load it to the iterator here.
            if(((LexicalIterator)iter).numberOfIncrements()==-1&&((LexicalIterator)iter).getLexFileLocator()==null){
                LexicalIterator temp = new LexicalIterator("modelInput",currParam.getLexicon(),null,((LexicalIterator)iter).isRandomized(),iter.expressions());
                iter=temp;
            }
            //if the filelocator variable within the lexicaliterator indicates that 
            //the script wants to use the current root directory to locate the lexicon file,
            //then update the iterator by roloading the lexical file.
            //(this step is necessary because the root directory can change any time.)
            //if(((LexicalIterator)iter).getLexFileLocator().absolutePath()==null){            
            if(((LexicalIterator)iter).getLexFileLocator()!=null){
                //System.out.println("reloading lexicon for lexical iterator.");
                FileLocator lexFileLoc = ((LexicalIterator)iter).getLexFileLocator();
                FileNameFactory fnf = new FileNameFactory();
                File lexFile = fnf.getReferencedFile(lexFileLoc);       
                edu.uconn.psy.jtrace.IO.WTFileReader fileReader = new edu.uconn.psy.jtrace.IO.WTFileReader(lexFile);
                if(!fileReader.validateLexiconFile()){             
                    System.out.println("Invalid lexicon file: "+lexFile);               
                }
                TraceLexicon newlex = fileReader.loadJTLexicon();                
                LexicalIterator temp = new LexicalIterator("modelInput",newlex,lexFileLoc,((LexicalIterator)iter).isRandomized(),iter.expressions());
                iter=temp;                
            }
            if(((LexicalIterator)iter).isRandomized()) ((LexicalIterator)iter).randomize();
        }
        if(iter instanceof LexicalAppendIterator){            
            //see above about lexicon loading and file locating.
            //this block loads a lexicon file and re-initializes the iterator
            if(((LexicalAppendIterator)iter).getLexFileLocator()!=null){
                //System.out.println("reloading lexicon for lexical iterator.");
                FileLocator lexFileLoc = ((LexicalAppendIterator)iter).getLexFileLocator();
                FileNameFactory fnf = new FileNameFactory();
                File lexFile = fnf.getReferencedFile(lexFileLoc);       
                edu.uconn.psy.jtrace.IO.WTFileReader fileReader = new edu.uconn.psy.jtrace.IO.WTFileReader(lexFile);
                if(!fileReader.validateLexiconFile()){             
                    System.out.println("Invalid lexicon file: "+lexFile);               
                }
                TraceLexicon newlex = fileReader.loadJTLexicon();                
                LexicalAppendIterator temp = new LexicalAppendIterator(((LexicalAppendIterator)iter).parameterName(),newlex,lexFileLoc,((LexicalAppendIterator)iter).getNumberToCopyPerIteration(),((LexicalAppendIterator)iter).isDuplicateFilter(),((LexicalAppendIterator)iter).isRandomized(),iter.expressions());
                iter=temp;                                 
                //System.out.println("loaded a lexicon of "+newlex.size()+" words");
            }
            //in a LexicalAppendIterator, at each iteration add N items to the current lexicon
            //from a specified lexicon or a saved lexicon.  At the end of the iteration we must
            //revert to the original lexicon.  in this block we'll save copy of the original
            //in order to revert later on.
            ((LexicalAppendIterator)iter).setRevertLex(currParam.getLexicon());
            if(((LexicalAppendIterator)iter).isRandomized()) ((LexicalAppendIterator)iter).randomize();
        }
        
        //this is the main loop for all iterators
        while(iter.hasNext()){
            if(BREAK) return;
            
            //if we are iterating over a parameter value, set it here
            //for several of the iterators, this call has no effect.
            setTargetParameter(iter.parameterName(),iter.getCurr());            
            
            if(iter instanceof PhonemeIterator){
                interpretPhonemeContinuum((PhonemeIterator)iter);
            }            
            if(iter instanceof FourTupleIterator){
                //Default settings for an eye-tracking modeling design:                
                currAnalysis.setCalculationType(currAnalysis.STATIC);
                currAnalysis.setAlignment(4);
                currAnalysis.setDomain(currAnalysis.WORDS);
                currAnalysis.setContentType(currAnalysis.RESPONSE_PROBABILITIES);
                //currAnalysis.setKValue(7);
                currAnalysis.setChoice(currAnalysis.FORCED);
                currAnalysis.setWatchType(currAnalysis.WATCHSPECIFIED);
                currAnalysis.setItemsToWatch(((FourTupleIterator)iter).getCurrFourTuple().toStringArray());                
                //String[] __lst= ((FourTupleIterator)iter).getCurrFourTuple().toStringArray();
                //System.out.println("items "+__lst.length+" "+__lst[0]);                
            }
            //add words to the lexicon.
            if(iter instanceof LexicalAppendIterator){
                //after the first iteration, add items to the lexicon.
                if(((LexicalAppendIterator)iter).currentIncrement()>0){
                    int curr=((((LexicalAppendIterator)iter).currentIncrement()-1)*((LexicalAppendIterator)iter).getNumberToCopyPerIteration());
                    for(int i=0;i<((LexicalAppendIterator)iter).getNumberToCopyPerIteration();i++){
                        if(curr>((LexicalAppendIterator)iter).getLexicon().size()) break;
                        if(((LexicalAppendIterator)iter).isDuplicateFilter()&&currParam.getLexicon().indexOf(((LexicalAppendIterator)iter).getLexicon().get(curr).getPhon())!=-1){
                            curr++;
                            continue;
                        }
                        currParam.getLexicon().add(((LexicalAppendIterator)iter).getLexicon().get(curr));                    
                        curr++;                    
                    }                             
                }
                else{
                    //in the first iteration, nothing is added to the lexicon.                
                }
            }            
            
            //here is where the 'body' of the iterator gets executed
            for(int i=0;i<iter.expressions().length;i++){
                interpretExpression(iter.expressions()[i]);                
            }
            System.gc();
            //if a /?/ phone was changed into a digit, change it back
            if(iter instanceof PhonemeIterator){
                restoreInputString((PhonemeIterator)iter);
            }                        
            //ch9eck to see if the while predicate is still true
            if(iter instanceof PredicateIterator){ 
                ((PredicateIterator)iter).updateState(currParam,currSim,currAnalysis,currGraphParameters,this);
            }
            //
            currAnalysis.gc();
            currSim.gc();
            currSim = null;
            System.gc();
            currSim = new edu.uconn.psy.jtrace.Model.TraceSim(currParam);        
        
            //this increments iterators, where appropriate
            iter.iterate();
            
            if(!hasGui){ 
                System.out.print(".");
                System.out.flush();
            }
            progress++;            
        }
        if(!hasGui){ 
            System.out.println("#");
            System.out.flush();
        }
        //at completion of the iterator, if we were accumulating graph data for the 
        //sake of averaging it, then do the average and save the data as instructed.
        //this is accomplished by renaming the accumulation action to a save action and
        //re-interpreting it.
        if(currAnalysis.isCurrentlyIterating()){
            for(int i=0;i<iter.expressions().length;i++){
                applyAveragingExpression(iter.expressions()[i]);
            }
            currAnalysis.averageAnalysisReset();
        }
        //if iter instanceof LexicalAppendIterator, reload the revertLex
        //to the currParam, i.e. revert the lexicon to it's previous state.
        if(iter instanceof LexicalAppendIterator)
            currParam.setLexicon(((LexicalAppendIterator)iter).getRevertLex());
        
        //reset the iterator to its initial state.
        iter.resetIterator();
    }    
    private void interpretPhonemeContinuum(PhonemeIterator iter){
        if(BREAK) return;
        if(currParam.getModelInput().indexOf("?")==-1){ 
            //case: there never was any /?/ in the model input
            return;
        }
        else{
            //case: the /?/ needs to be switched to the current increment digit
            iter.setOriginalString(currParam.getModelInput());
            String ambiguous = currParam.getModelInput().replace('?', (new Integer(iter.currentIncrement())).toString().charAt(0));
            setTargetParameter("modelInput",ambiguous);
            iter.setModString(currParam.getModelInput());                        
        }
    }
    private void restoreInputString(PhonemeIterator iter){
        //case: the model input has not been altered , so
        //we return it to its original state, as stored in the iterator
        if(currParam.getModelInput().equals(iter.getModString())){
            setTargetParameter("modelInput",iter.getOriginalString());
        }
        //case: the model input was altered by an action (w/i the iterator)
        //so we leave it in that state.
        //TODO: this may lead to unintuitive behavior.  consider case where
        //an action add silece to edges vs. case updating entire input string.
        else{
            return;
        }
    }
    public boolean interpretPredicate(Predicate pred){        
        if(BREAK) return false;  
        pred = (Predicate)pred.clone();
        Primitive[] arguments=pred.arguments();
        //evaluates any queries that may be in the arguments and replaces them with a primitive value.        
        arguments=replaceQueriesWithValues(arguments); 
        pred.setArguments(arguments);
        
        PredicateWorkhorse pw=new PredicateWorkhorse(currParam,currSim,currAnalysis,currGraphParameters,this);
        boolean result=false;
        try{
            result=pw.evaluatePredicate(pred);
        }
        catch(JTraceScriptingException jtse){jtse.printStackTrace();}
        //implement script cancellation using an exception here.
        return result;
    }
    public Primitive interpretQuery(Query qry){
        if(BREAK) return null;
        qry = (Query)qry.clone();
        Primitive[] arguments=qry.arguments();
        //evaluates any queries that may be in the arguments and replaces them with a primitive value.
        arguments=replaceQueriesWithValues(arguments); 
        qry.setArguments(arguments);
        
        QueryWorkhorse qwh=new QueryWorkhorse(currParam,currSim,currAnalysis,currGraphParameters,this);
        Primitive result=qry;
        try{
            result=qwh.evaluateQuery(qry);            
        }
        catch(JTraceScriptingException jtse){
            System.out.println("problem query: "+qry.name());            
            jtse.printStackTrace();        
            System.exit(0);
        }
        //implement script cancellation using an exception here.
        qwh=null;
        return result;
    }
    //Directs the action towards the appropriate 'workhorse' and passes along
    //only the object who is to be affected by the action.
    private void interpretAction(Action act){        
        if(BREAK) return;          
        act = (Action)act.clone();
        Primitive[] arguments=act.arguments();
        //evaluates any queries that may be in the arguments and replaces them with a primitive value.
        arguments=replaceQueriesWithValues(arguments); 
        act.setArguments(arguments);
        
        String targetObject=this.objectToBeModifiedByScriptAction(act.name());
        if(targetObject.equals("currParam")){            
            currSim = new edu.uconn.psy.jtrace.Model.TraceSim(new TraceParam(currParam));            
            ParameterActionWorkhorse paw=new ParameterActionWorkhorse(currSim.getParameters(),currSim,(new TraceSimAnalysis(currAnalysis)),(new GraphParameters(currGraphParameters)),this);
            try{
                currParam=paw.applyAction(act);
            } catch(JTraceScriptingException jtse){jtse.printStackTrace();}
            //implement script cancellation using an exception here.        
            paw=null;
        }
        else if(targetObject.equals("currSim")){
            currSim = new edu.uconn.psy.jtrace.Model.TraceSim(new TraceParam(currParam));            
            SimActionWorkhorse saw=new SimActionWorkhorse(currSim.getParameters(),currSim,(new TraceSimAnalysis(currAnalysis)),(new GraphParameters(currGraphParameters)),this);
            try{
                currSim=saw.applyAction(act);
            } catch(JTraceScriptingException jtse){jtse.printStackTrace();}
            //implement script cancellation using an exception here.
            saw=null;
        }
        else if(targetObject.equals("currAnalysis")||targetObject.equals("currGraphParameters")){
            currSim = new edu.uconn.psy.jtrace.Model.TraceSim((TraceParam)currParam.clone());            
            GraphActionWorkhorse gaw=new GraphActionWorkhorse(currSim.getParameters(),currSim,(new TraceSimAnalysis(currAnalysis)),(new GraphParameters(currGraphParameters)),this);
            try{
                if(targetObject.equals("currAnalysis")){
                    currAnalysis=gaw.applyAnalysisAction(act);                    
                }
                else //targetObject.equals("currGraphPreferences")
                    currGraphParameters=gaw.applyDisplayAction(act);
            } catch(JTraceScriptingException jtse){jtse.printStackTrace();}
            //implement script cancellation using an exception here.        
            gaw=null;
        }        
        else if(targetObject.equals("currScript")){
            //script/meta actions are handled locally
            try{
                applyScriptAction(act);
            } 
            catch(JTraceScriptingException jtse){jtse.printStackTrace();}            
            //implement script cancellation using an exception here.        
        }
        else if(targetObject.equals("GUI")){
            //GUI actions are handled locally
            if(!hasGui||null==gui) return;
            try{
                applyGUIAction(act);
            }
            catch(JTraceScriptingException jtse){jtse.printStackTrace();}                        
        }
        else if(targetObject.equals("saveParam")){
            currSim = new edu.uconn.psy.jtrace.Model.TraceSim(new TraceParam(currParam));            
            ParameterActionWorkhorse paw=new ParameterActionWorkhorse(currSim.getParameters(),currSim,(new TraceSimAnalysis(currAnalysis)),(new GraphParameters(currGraphParameters)),this);
            try{
                paw.applyFileAction(act);
            } catch(JTraceScriptingException jtse){jtse.printStackTrace();}
            paw=null;
        }
        else if(targetObject.equals("saveSim")){
            currSim = new edu.uconn.psy.jtrace.Model.TraceSim(new TraceParam(currParam));            
            SimActionWorkhorse saw=new SimActionWorkhorse(currSim.getParameters(),currSim,(new TraceSimAnalysis(currAnalysis)),(new GraphParameters(currGraphParameters)),this);
            try{
                saw.applyFileAction(act);
            } catch(JTraceScriptingException jtse){jtse.printStackTrace();}
            saw=null;
        }
        else if(targetObject.equals("saveGraph")){
            currSim = new edu.uconn.psy.jtrace.Model.TraceSim(new TraceParam(currParam));            
            GraphActionWorkhorse gaw=new GraphActionWorkhorse(currSim.getParameters(),currSim,(new TraceSimAnalysis(currAnalysis)),(new GraphParameters(currGraphParameters)),this);
            try{
                gaw.applyFileAction(act);
            } catch(JTraceScriptingException jtse){jtse.printStackTrace();}
            gaw=null;
        }        
        else if(targetObject.equals("saveScript")){
            //script actions are handled in this class
            try{
                applyScriptFileAction(act);
            } catch(JTraceScriptingException jtse){jtse.printStackTrace();}            
        }        
    }
    public void BREAK(){
        BREAK = true;        
        System.gc();
    }
    //if a query is used as an argument, we must resolve its value
    //before passing it in: it is resolved here.
    public Primitive[] replaceQueriesWithValues(Primitive[] arg){
        if(null == arg || arg.length == 0) return arg;
        Primitive[] result=new Primitive[arg.length];
        for(int i=0;i<arg.length;i++){
            if(null==arg[i]) result[i]=null;
            else if(arg[i] instanceof Query){
                //System.out.println("<<-- "+arg[i].XMLTag());
                result[i]=interpretQuery((Query)arg[i]);
                //System.out.println("-->> "+result[i].XMLTag());                                
            }
            else{
                result[i]=arg[i];                
            }
        }
        return result;
    }
    //informs the setTargetParameter method whether the iterated parameter
    //refers to the currParam, curSim, or curGraph object
    private String objectToBeModifiedByIteratedParameter(String param){
        if(param.equals("k-value")||
                param.equals("top-n-items")){
            return "currAnalysis";
        }
        else if(param.equals("cyclesPerSim")){
            return "currScript";
        }
        else{ //if(param.equals("...")){ //most often will be a TRACE param
            return "currParam";
        }        
    }
    public String objectToBeModifiedByScriptAction(String a){
        if(a.equals("set-lexicon")||
           a.equals("load-lexicon")||
           a.equals("load-parameters")||
           a.equals("set-a-parameter")||
           a.equals("set-input")||
           a.equals("increment-parameter-by-this-amount")||
           a.equals("add-silence-to-input-edges")||
           a.equals("append-to-lexicon")||
           a.equals("set-parameters")){
            return "currParam";
        }            
        else if(a.equals("")){
            return "currSim";
        }    
        else if(a.equals("update-graph-object")||      
            a.equals("analysis-settings")||                
            a.equals("reset-graph-defaults")||
            a.equals("set-content-type")||
            a.equals("set-k-value")||                                
            a.equals("set-graph-domain")||
            a.equals("set-watch-type")|| 
            a.equals("set-watch-top-n")||
            a.equals("set-watch-items")||            
            a.equals("set-alignment")||
            a.equals("set-choice-type")||
            a.equals("set-analysis-type")||
            a.equals("add-one-analysis-item")||
            a.equals("remove-one-analysis-item")||
            a.equals("average-all-analyses-in-current-iteration-and-save-graph")            
            ){
            return "currAnalysis";
        }               
        else if(a.equals("set-graph-title")||
                a.equals("set-graph-x-axis-bounds")||
                a.equals("set-graph-y-axis-bounds")||
                a.equals("set-graph-x-axis-label")||                
                a.equals("set-graph-y-axis-label")||
                a.equals("set-graph-input-position")
                ){
            return "currGraphParameters";
        }
        else if(a.equals("set-cycles-per-sim")||
                a.equals("run-simulation")||
                a.equals("set-root-directory")||
                a.equals("write-to-a-file")||
                a.equals("load-sim-from-file")||
                a.equals("cancel-script")){                                
            return "currScript";
        }
        else if(a.equals("new-window")){
            return "GUI";
        }                    
        else if(a.equals("save-parameters-to-txt")||
                a.equals("save-parameters-to-jt")){
            return "saveParam";
        }
        else if(a.equals("save-simulation-to-jt")||
                a.equals("save-simulation-to-txt")){
            return "saveSim";
        }
        else if(a.equals("save-graph-to-png")||
                a.equals("save-graph-to-txt")||                
                a.equals("save-average-analysis-to-png-and-txt")
                ){
            return "saveGraph";
        }
        else{
            return "unknownParam";
        }
    }
    public void applyScriptAction(Action act)throws JTraceScriptingException{
        if(act.name().equals("set-cycles-per-sim")){
            if(act.arguments().length!=1||null==act.arguments()[0].intValue())
                throw new JTraceScriptingException("set-cycles-per-sim argument check failed : requires one argument (Int)");
            cyclesPerSim=act.arguments()[0].intValue().value();            
        }
        else if(act.name().equals("set-root-directory")){
            if(null==act.arguments()[0]||act.arguments().length!=1)
                throw new JTraceScriptingException("set-root-directory argument check failed : requires one argument (FileLocator)");            
            if(((Text)act.arguments()[0]).value().equals("use-script-file-path")){
                rootDirectory = this.loadedFilePath;
            }
            else{
                rootDirectory = ((Text)act.arguments()[0]).value();                
            }
            FileNameFactory.rootDirectory = this.rootDirectory;
            //System.out.println("root directory: "+rootDirectory);
        }
        else if(act.name().equals("cancel-script")){
            this.BREAK();
        }
        else if(act.name().equals("reset-parameters")){
            currParam = baseParam;
        }
        else if(act.name().equals("load-sim-from-file")){
            
            //executing the actions in the sim file loads the values therein.
            FileLocator fileLocator = (FileLocator)act.arguments()[0];            
            edu.uconn.psy.jtrace.IO.WTFileReader fr = new edu.uconn.psy.jtrace.IO.WTFileReader(fileLocator);
            TraceScript tempScript = fr.loadJTScript();
            tempScript.interpretScript();
            currParam = tempScript.getCurrParam();
            currSim = tempScript.getCurrSim();
            currAnalysis = tempScript.getCurrAnalysis();
            currGraphParameters = tempScript.getCurrGraphParameters();
            tempScript = null;
        }            
        else if(act.name().equals("write-to-a-file")){
            if(act.arguments().length!=2||null==act.arguments()[0]||null==act.arguments()[1].fileLocatorValue())
                throw new JTraceScriptingException("TraceScript.applyScriptAction : write-to-a-file takes 2 arguments (Primitive, FileLocator)" );            
            //convert the first arg to a string
            ListOfPrimitives value = (ListOfPrimitives)act.arguments()[0];            
            String contents="";
            for(int i=0;i<value.listValue().value().size();i++){
                if(value.listValue().value().get(i)!=null){
                    contents += primitiveToString((Primitive)value.listValue().value().get(i));
                    contents += "\t";
                }                
            }
            //make the file for writing
            FileNameFactory fnf =  new FileNameFactory(this.getRootDirectory());
            File file = fnf.makeOutputFile(act.arguments()[1].fileLocatorValue());
            try{
                if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
                if(!file.exists()) file.createNewFile();
                //write to the file                               
                WTFileWriter fw = new WTFileWriter(file,true);                
                fw.writeAndClose(contents+"\n");
                if(!hasGui) System.out.println(contents);
                //if(true) System.out.println(contents+"\n");
            }
            catch(Exception e){ e.printStackTrace();}            
        }
    }
    public void applyScriptFileAction(Action act)throws JTraceScriptingException{
        /*if(act.name().equals("save-script")){
            if(null==act.arguments()[0]||null==act.arguments()[0].fileLocatorValue()||act.arguments().length!=1)
                throw new JTraceScriptingException("save-script-file argument check failed : requires one parameter (FileLocator)");
            FileNameFactory fnf =  new FileNameFactory(currScript.getRootDirectory());
            File file = fnf.makeScriptFile(act.arguments()[0].fileLocatorValue());
            saveScriptToJt(file);
        }*/
    }
    public void applyGUIAction(Action act)throws JTraceScriptingException{
        if(act.name().equals("new-window")){
            currSim = new edu.uconn.psy.jtrace.Model.TraceSim(new TraceParam(currParam));
            currSim.cycle(cyclesPerSim);            
            //gui.loadJTRACEFrame(currSim.getParameters(),currSim,(new TraceSimAnalysis(currAnalysis)),(new GraphParameters(currGraphParameters)),jtFileName);                                                            
            TraceSim cloneSim = currSim.clone();
            gui.loadJTRACEFrame(cloneSim.getParameters(),cloneSim,(new TraceSimAnalysis(currAnalysis)),(new GraphParameters(currGraphParameters)),jtFileName);                                                            
        }        
    }
    public void applyAveragingExpression(Expression e){
        if(e instanceof Action){
            if(((Action)e).name().equalsIgnoreCase("average-all-analyses-in-current-iteration-and-save-graph")){
                Action saveAvgAction = (Action)e;
                saveAvgAction.setName("save-average-analysis-to-png-and-txt");
                interpretAction(saveAvgAction);
            }            
        }    
    }
    //All parameters over which an iterator can iterate use this 
    //method to affect that change.
    public void setTargetParameter(String param,Object value){
        //System.out.println("set "+param+" to "+value);
        String targetObject=objectToBeModifiedByIteratedParameter(param);
        //System.out.println("value: "+value);
        //TODO: implement setParameter in these three classes.
        if(targetObject.equals("currParam")){
            if(param.equalsIgnoreCase("user")) currParam.setUser((String)value);
            else if(param.equalsIgnoreCase("dateTime")) currParam.setDateTime((String)value);
            else if(param.equalsIgnoreCase("comment")) currParam.setComment((String)value);
            else if(param.equalsIgnoreCase("modelInput")) currParam.setModelInput((String)value);

            else if(param.equalsIgnoreCase("spread.1")) currParam.getSpread()[0]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spread.2")) currParam.getSpread()[1]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spread.3")) currParam.getSpread()[2]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spread.4")) currParam.getSpread()[3]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spread.5")) currParam.getSpread()[4]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spread.6")) currParam.getSpread()[5]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spread.7")) currParam.getSpread()[6]=((Integer)value).intValue();

            else if(param.equalsIgnoreCase("spreadscale.1")) currParam.getSpreadScale()[0]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spreadscale.2")) currParam.getSpreadScale()[1]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spreadscale.3")) currParam.getSpreadScale()[2]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spreadscale.4")) currParam.getSpreadScale()[3]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spreadscale.5")) currParam.getSpreadScale()[4]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spreadscale.6")) currParam.getSpreadScale()[5]=((Integer)value).intValue();
            else if(param.equalsIgnoreCase("spreadscale.7")) currParam.getSpreadScale()[6]=((Integer)value).intValue();

            else if(param.equalsIgnoreCase("min")) currParam.setMin(((Float)value).doubleValue());
            else if(param.equalsIgnoreCase("max")) currParam.setMax(((Float)value).doubleValue());
            else if(param.equalsIgnoreCase("nreps")) currParam.setNReps(((Float)value).intValue());
            else if(param.equalsIgnoreCase("slicesPerPhon")) currParam.setSlicesPerPhon(((Integer)value).intValue());
            else if(param.equalsIgnoreCase("fslices")) currParam.setFSlices(((Integer)value).intValue());
            else if(param.equalsIgnoreCase("decay.F")) currParam.getDecay().F=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("decay.P")) currParam.getDecay().P=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("decay.W")) currParam.getDecay().W=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("rest.F")) currParam.getRest().F=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("rest.P")) currParam.getRest().P=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("rest.W")) currParam.getRest().W=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("alpha.if")) currParam.getAlpha().IF=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("alpha.fp")) currParam.getAlpha().FP=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("alpha.pf")) currParam.getAlpha().PF=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("alpha.pw")) currParam.getAlpha().PW=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("alpha.wp")) currParam.getAlpha().WP=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("gamma.F")) currParam.getGamma().F=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("gamma.P")) currParam.getGamma().P=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("gamma.W")) currParam.getGamma().W=((Float)value).doubleValue();
            //else if(param.equalsIgnoreCase("lambda.F")) currParam.getLambda().F=((Float)value).doubleValue();
            //else if(param.equalsIgnoreCase("lambda.P")) currParam.getLambda().P=((Float)value).doubleValue();
            //else if(param.equalsIgnoreCase("lambda.W")) currParam.getLambda().W=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("lexicon")) currParam.setLexicon((edu.uconn.psy.jtrace.Model.TraceLexicon)value);
            else if(param.equalsIgnoreCase("dictionary")) currParam.setLexicon((edu.uconn.psy.jtrace.Model.TraceLexicon)value);
            else if(param.equalsIgnoreCase("deltaInput")) currParam.setDeltaInput(((Float)value).intValue());
            else if(param.equalsIgnoreCase("noiseSD")) currParam.setNoiseSD(((Float)value).doubleValue());
            else if(param.equalsIgnoreCase("stochasticitySD")) currParam.setStochasticitySD(((Float)value).doubleValue());
            else if(param.equalsIgnoreCase("continuumSpec")) currParam.setContinuumSpec((String)value);
            else if(param.equalsIgnoreCase("atten")) currParam.setAtten(((Float)value).doubleValue());
            else if(param.equalsIgnoreCase("bias")) currParam.setBias(((Float)value).doubleValue());
            else if(param.equalsIgnoreCase("learningRate")) currParam.setLearningRate(((Float)value).doubleValue());
            
            else if(param.equalsIgnoreCase("freqNode.RDL_wt")) currParam.getFreqNode().RDL_wt=((java.lang.Boolean)value).booleanValue();
            else if(param.equalsIgnoreCase("freqNode.RDL_wt_c")) currParam.getFreqNode().RDL_wt_c=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("freqNode.RDL_wt_s")) currParam.getFreqNode().RDL_wt_s=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("freqNode.RDL_rest")) currParam.getFreqNode().RDL_rest=((java.lang.Boolean)value).booleanValue();
            else if(param.equalsIgnoreCase("freqNode.RDL_rest_c")) currParam.getFreqNode().RDL_rest_c=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("freqNode.RDL_rest_s")) currParam.getFreqNode().RDL_rest_s=((Float)value).doubleValue();
            else if(param.equalsIgnoreCase("freqNode.RDL_post")) currParam.getFreqNode().RDL_post=((java.lang.Boolean)value).booleanValue();
            else if(param.equalsIgnoreCase("freqNode.RDL_psot_c")) currParam.getFreqNode().RDL_post_c=((Float)value).doubleValue();            
        }
        else if(targetObject.equals("currSim")){
            //if(param.equals("word-window-display")) currSim.notifyAll(); //TODO
        }
        else if(targetObject.equals("currAnalysis")){        
            if(param.equals("k-value")) currAnalysis.setKValue(((Float)value).intValue());
            //ETC
        }
        else if(targetObject.equals("currGraphParameters")){        
            if(param.equals("y-axis-label")) currGraphParameters.setYLabel((String)value);
            //ETC
        }
        else if(targetObject.equals("currScript")){
            if(param.equals("cyclespersim")) cyclesPerSim=((Integer)value).intValue();            
        }        
    }
    public TraceParam getCurrParam(){return currParam;}
    public TraceSim getCurrSim(){return currSim;}
    public TraceSimAnalysis getCurrAnalysis(){return currAnalysis;}
    public edu.uconn.psy.jtrace.UI.GraphParameters getCurrGraphParameters(){return currGraphParameters;}
    public int getCyclesPerSim(){return cyclesPerSim;}
    public int getProgress(){return progress;}
    public int getTotalIterations(){return totalIterations;}
    public String getRootDirectory(){return rootDirectory;}
    private void countIterations(){
        //@@@ this function does not take into account the possibility that a lexicon
        //is loaded during the script.
        int result=0;
        if(null!=expressions&&expressions.length>0){
            for(int i=0;i<expressions.length;i++)
                result+=countIterationsRecurse(expressions[i]);
        }
        if(result==0) result++;
        totalIterations=result;
        
    }
    private int countIterationsRecurse(Expression e){
        int result=0;
        if(e instanceof Action)
            result=0;
        else if(e instanceof Conditional){
            for(int i=0;i<((Conditional)e).thenExpressions().length;i++)
                result+=countIterationsRecurse(((Conditional)e).thenExpressions()[i]);
            for(int i=0;i<((Conditional)e).elseExpressions().length;i++)
                result+=countIterationsRecurse(((Conditional)e).elseExpressions()[i]);            
        }
        else if(e instanceof Iterator){
            int iterations;
            if(e instanceof LexicalIterator &&
               ((LexicalIterator)e).numberOfIncrements() == -1)
                iterations = currParam.getLexicon().size();
            else
                iterations =((Iterator)e).numberOfIncrements();
            
            for(int i=0;i<((Iterator)e).expressions().length;i++){
                result+=(iterations * countIterationsRecurse(((Iterator)e).expressions()[i]));            
            }
            if(result==0) result+=iterations;
        }
        //System.out.println("recurse "+result);
        return result;
    }
    public void setDescription(String str){
        description=str;
    }
    public String getDescription(){
        return description;
    }
    public void setGuiPointer(edu.uconn.psy.jtrace.UI.jTRACEMDI _gui){
        gui=_gui;
    }  
    public void setLoadedFileReference(java.io.File _file){
        if(_file==null) loadedFilePath=traceProperties.rootPath.getAbsolutePath();
        else loadedFilePath=_file.getParent();
    }      
    public void setBaseParameters(TraceParam base){
        baseParam = base;
    }
    public void setFileName(File _name){
        jtFileName = _name;
    }
    public void updateProgressBar(javax.swing.JDialog progressFrame){
        javax.swing.JProgressBar progressBar = (javax.swing.JProgressBar)progressFrame.getContentPane().getComponent(0);
        progressBar.setValue(scriptthread.currentProgress());                 
        if(scriptthread.isDone()){            
            progressBar.setValue(scriptthread.currentProgress());                 
            progressFrame.setEnabled(false);
            progressFrame.dispose();
            progressFrame.setVisible(false);
            progressBar.setVisible(false);            
            progressFrame=null;
            progressBar=null;
            scripttimer.stop();
            if(null!=gui) gui.tile();
        }
    }
    public String primitiveToString(Primitive prim){
        String result="";
        if(prim instanceof Int)
            result = ""+prim.intValue().value();
        if(prim instanceof Decimal)
            result = ""+prim.decimalValue().value();
        if(prim instanceof Text)
            result = ""+prim.textValue().value();
        if(prim instanceof Boolean)
            result = ""+prim.booleanValue().value();
        if(prim instanceof FileLocator){
            if(null!=prim.fileLocatorValue().absolutePath())
                result += prim.fileLocatorValue().absolutePath();
            else if(null!=prim.fileLocatorValue().relativePath())
                result += prim.fileLocatorValue().relativePath();
            if(null!=prim.fileLocatorValue().fileName())
                result += "/"+prim.fileLocatorValue().fileName();
        }
        if(prim instanceof Predicate){
            result = "Predicate: "+prim.predicateValue().name();
            if(null!=prim.predicateValue().arguments())
                for(int i=0;i<prim.predicateValue().arguments().length;i++){
                    result +="\n\tArgument "+i+": "+primitiveToString(prim.predicateValue().arguments()[i]);
                }
        }
        if(prim instanceof Query){
            Primitive interpreted = interpretQuery(prim.queryValue());
            result = primitiveToString(interpreted);
            /*result = "Query: "+prim.queryValue().name();
            if(null!=prim.queryValue().arguments())
                for(int i=0;i<prim.queryValue().arguments().length;i++){
                    result +="\n\tArgument "+i+": "+primitiveToString(prim.queryValue().arguments()[i]);
                }*/
        }
        if(prim instanceof ListOfPrimitives){
            result = "List: ";
            if(null!=prim.listValue().value())
                for(int i=0;i<prim.listValue().value().size();i++){
                    result +="\n"+i+"\t"+primitiveToString((Primitive)prim.listValue().value().get(i));
                }
        }
        if(prim instanceof TraceParam){
            result = "(TraceParameters)";
            //@@@
        }
        if(prim instanceof TraceLexicon){
            result = "TraceLexicon : "+prim.lexiconValue().getDescription();
            for(int i=0;i<prim.lexiconValue().size();i++)
                result += "\n\t"+prim.lexiconValue().get(i).getPhon();
            ///@@@
        }        
        return result;
    }
    
    public String XMLTag(){
        String result="";
        result+="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
                "<jt xmlns=\'http://xml.netbeans.org/examples/targetNS\'"+
                "\nxmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\'"+
                "\nxsi:schemaLocation=\'http://xml.netbeans.org/examples/targetNS http://maglab.psy.uconn.edu/jtraceschema.xsd\'>";
        if(null != description) result+="<description>"+description+"</description>";
        result+="<script>";
        //encorporate base parameters: 
        boolean foundPreviousParams, baseParamsAreDefault;
        if(null!=expressions &&
            expressions[0] instanceof Action &&
            ((Action)expressions[0]).name().equals("set-parameters"))
            foundPreviousParams = true;
        else 
            foundPreviousParams = false;
        
        if(baseParam.XMLTag().equals((new TraceParam()).XMLTag()))
            baseParamsAreDefault = true;
        else
            baseParamsAreDefault = false;
        
        if(!baseParamsAreDefault){
            result+="<action><set-parameters>"+baseParam.XMLTag()+"</set-parameters></action>";        
        }        
        //then load all scripting expressions        
        if(expressions == null){
            //System.out.println("null script.");
        }
        else{
            for(int i = 0; i < expressions.length; i++){
                //if we've encountered some new base params, then don't include the prev set.
                if(foundPreviousParams && !baseParamsAreDefault && i==0) continue;
                if(expressions[i] instanceof Action) result += ((Action)expressions[i]).XMLTag();
                else if(expressions[i] instanceof Conditional) result += ((Conditional)expressions[i]).XMLTag();
                else if(expressions[i] instanceof Iterator) result += ((Iterator)expressions[i]).XMLTag();
                //result+=expressions[i].XMLTag();                                
            }
        }
        result+="</script>";
        result+="</jt>";
        return result;
    }
}

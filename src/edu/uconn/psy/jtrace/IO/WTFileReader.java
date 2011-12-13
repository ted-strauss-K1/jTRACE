
package edu.uconn.psy.jtrace.IO;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.io.*;
import java.util.*;
import edu.uconn.psy.jtrace.parser.*;
import edu.uconn.psy.jtrace.parser.impl.*;
import edu.uconn.psy.jtrace.Model.*;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.Model.Scripting.impl.*;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Validator;

/**
 *
 * @author  tedstrauss
 */
public class WTFileReader {
    File path,source;
    /** Creates a new instance of FileReader */
    public WTFileReader(String _p, String _n) {
        path=new File(_p);
        
        try{
            File[] files=path.listFiles();
            //System.out.println("path "+path.getName()+" contains "+files.length+" files.");
            if(files.length==0){
                System.out.println("path is empty.");
                return;    
            }
            
            for(int i=0;i<files.length;i++){
                //System.out.println("\t"+files[i].getName());
                if(files[i].getName().equals(_n)){
                    source=files[i];
                    break;
                }
            }
            if(source==null){
                System.out.println("could not find file: "+_n);
                return;
            }
            if(!source.canRead()||!source.isFile()){
                System.out.println("problem with file: "+_n);
                return;
            }
        } 
        catch(Exception e){ e.printStackTrace();}        
        //System.out.println("File is usable: "+source.getName());
    }
    public WTFileReader(File f){
           source=f;
       try{
           if(source==null){
                System.out.println("File == null.");
                return;
            }
            /*if(!source.canRead()||!source.isFile()){
                System.out.println("problem with file: "+source.getName());
                return;
            }*/
        } 
        catch(Exception e){ e.printStackTrace();}
        //System.out.println("File is usable: "+source.getName());
    }
    public WTFileReader(edu.uconn.psy.jtrace.Model.Scripting.FileLocator f){
           String name = f.fileName();
           String absPath = f.absolutePath();
           String relPath = f.relativePath();
           if(null == name) return;
           if(null == absPath && null == relPath) return;
           if(null != absPath) source = new File(absPath,name);
           else source = new File(relPath,name);
           
       try{
           if(source==null){
                System.out.println("File == null.");
                return;
            }
            if(!source.canRead()||!source.isFile()){
                System.out.println("problem with file: "+source.getName());
                return;
            }
        } 
        catch(Exception e){ e.printStackTrace();}
        //System.out.println("File is usable: "+source.getName());
    }
    
    public TraceScript buildScript(JtType r){
        if(null == r) return null;
        String description=r.getDescription();        
        List rw=r.getScript().getIterateOrConditionOrAction();
        //List rw=new LinkedList();
        Expression[] expr=new Expression[rw.size()];        
        for(int i=0;i<expr.length;i++){
            expr[i]=buildExpression(rw.get(i));            
            //System.out.println("building expressions "+expr[i].XMLTag()+" \n");
        }
        TraceScript script=new TraceScript(null,expr);
        script.setDescription(description);
        return script;
    }
    public Expression buildExpression(Object r){
        if(null == r) return null;
        Expression expr;        
        if(r instanceof JtType.ScriptType.Iterate) 
            expr = buildIterator((JtIteratorTypeImpl)r);
        else if(r instanceof JtType.ScriptType.Condition)
            expr = buildConditional((JtConditionalType)r);
        else if(r instanceof JtType.ScriptType.Action) 
            expr = buildAction((JtActionType)r);
        else{ 
            System.out.println("Expression class?  "+r.getClass().getName());
            expr = null;
        }
        //System.out.println(expr.getClass().getName()+"\tbuild epxression: "+expr.XMLTag());
        return expr;
    }
    public Expression buildExpression(JtExpressionType r){
        if(null == r) return null;
        Expression expr;                
        if(null != r.getIterate()) 
            expr = buildIterator(r.getIterate());
        else if(null != r.getCondition())
            expr = buildConditional(r.getCondition());
        else //if(null != r.getAction()) 
            expr = buildAction(r.getAction());
        //System.out.println(expr.getClass().getName()+"\tbuild epxression: "+expr.XMLTag());
        return expr;
    }
    public edu.uconn.psy.jtrace.Model.Scripting.Iterator buildIterator(JtIteratorType r){
        if(null == r) return null;
        edu.uconn.psy.jtrace.Model.Scripting.Iterator iterator;        
        List instructions = r.getInstruction();
        Expression[] instr = new Expression[instructions.size()];
        ListIterator itr = instructions.listIterator();
        int i=0;
        while(itr.hasNext()){
            instr[i++] = buildExpression((JtExpressionType)itr.next());
        }        
        
        if(null != r.getIncrementingValue()) iterator = buildIncrementingIterator(r.getIncrementingValue(),instr);
        else if(null != r.getOverItemsInALexicon()) iterator = buildLexicalIterator(r.getOverItemsInALexicon(),instr); 
        else if(null != r.getOverListOfValues()) iterator = buildListIterator(r.getOverListOfValues(),instr);
        else if(null != r.getOverPhonemeContinuum()) iterator = buildPhonIterator(r.getOverPhonemeContinuum(),instr);
        else if(null != r.getOverEyeTrackingFourTuples()) iterator = buildFourTupleIterator(r.getOverEyeTrackingFourTuples(),instr);
        else if(null != r.getJustRepeatTheSameThing()) iterator = buildDummyIterator(r.getJustRepeatTheSameThing(),instr);
        else if(null != r.getAppendingItemsToTheLexicon()) iterator = buildLexicalAppendIterator(r.getAppendingItemsToTheLexicon(),instr);
        else //if(null != r.getWhilePredicateIsTrue()) 
            iterator = buildPredicateIterator(r.getWhilePredicateIsTrue(),instr);
        return iterator;
    }
    public FloatIterator buildIncrementingIterator(JtIteratorType.IncrementingValueType r,Expression[] instructions){                
        return new FloatIterator(r.getTargetParameter(),r.getFrom().floatValue(),r.getTo().floatValue(),r.getNumberOfSteps(),instructions);                
    }
    public PhonemeIterator buildPhonIterator(JtIteratorType.OverPhonemeContinuumType r,Expression[] expr){
        return new PhonemeIterator(r.getFrom(),r.getTo(),r.getNumberOfSteps(),expr);        
    }
    public DummyIterator buildDummyIterator(JtIteratorType.JustRepeatTheSameThingType r, Expression[] expr){
        return new DummyIterator(r.getRepetitions(),expr);
    }
    public PredicateIterator buildPredicateIterator(JtIteratorType.WhilePredicateIsTrueType r,Expression[] expr){
        Predicate pred = buildPredicate(r.getPredicate());
        return new PredicateIterator(pred,expr);        
    }
    //TODO: this is wrong!
    public StringIterator buildListIterator(JtIteratorType.OverListOfValuesType r,Expression[] expr){        
        String[] lex = new String[r.getArg().size()];
        String[] words = new String[r.getArg().size()];
        for(int i=0;i<r.getArg().size();i++)
            words[i] = (String)r.getArg().get(i);
        return new StringIterator(r.getTargetParameter(),words,expr);
    }
    public LexicalAppendIterator buildLexicalAppendIterator(JtIteratorType.AppendingItemsToTheLexiconType r,Expression[] expr){
        //constructor: LexicalAppendIterator(String pname,TraceLexicon lex, FileLocator loc, int n, Expression[] e){            
        if(r.getALexiconFromFile()!=null){
            return new LexicalAppendIterator(r.getTargetParameter(),null,buildFile(r.getALexiconFromFile()),r.getAppendNAtTime(),r.isDuplicateFilter(),r.isRandomizeOrder(),expr);
        }
        else{ //r.getAListOfWords() != null            
            return new LexicalAppendIterator(r.getTargetParameter(),buildLexicon(r.getAListOfWords()),null,r.getAppendNAtTime(),r.isDuplicateFilter(),r.isRandomizeOrder(),expr);
        }        
    }
    public LexicalIterator buildLexicalIterator(JtIteratorType.OverItemsInALexiconType r,Expression[] expr){
        TraceLexicon lex;
        if(r.getUseTheCurrentLexicon()!=null){
            lex=null;
            return new LexicalIterator(r.getUseTheCurrentLexicon().getTargetParameter(),lex,null,r.getUseTheCurrentLexicon().isRandomizeOrder(),expr);
        }
        else if(r.getUseANewlySpecifiedLexicon()!=null){
            lex = buildLexicon(r.getUseANewlySpecifiedLexicon().getArg());
            return new LexicalIterator(r.getUseANewlySpecifiedLexicon().getTargetParameter(),lex,null,r.getUseANewlySpecifiedLexicon().isRandomizeOrder(),expr);
        }
        else{ //if(r.getUseASavedLexicon()!=null){
            FileLocator lexFileLoc = buildFile(r.getUseASavedLexicon().getFile());
            FileNameFactory fnf = new FileNameFactory();
            File lexFile = fnf.getReferencedFile(lexFileLoc);
            if(lexFile==null)
                return new LexicalIterator("modelInput",null,lexFileLoc,r.getUseASavedLexicon().isRandomizeOrder(),expr);
            else{
                edu.uconn.psy.jtrace.IO.WTFileReader fileReader = new edu.uconn.psy.jtrace.IO.WTFileReader(lexFile);
                if(!fileReader.validateLexiconFile()){             
                    javax.swing.JOptionPane.showMessageDialog(null, "Invalid lexicon file.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);               }
                lex = fileReader.loadJTLexicon();
                return new LexicalIterator("modelInput",lex,lexFileLoc,false,expr);
            }
        }
    }
    public FourTupleIterator buildFourTupleIterator(JtIteratorType.OverEyeTrackingFourTuplesType r,Expression[] expr){
        List ll = r.getFourTuple();
        FourTuple[] tuples;
        if(ll != null){
            tuples = new FourTuple[ll.size()];
            JtIteratorType.OverEyeTrackingFourTuplesType.FourTupleType curr;        
            for(int i=0;i<ll.size();i++){
                curr = ((JtIteratorType.OverEyeTrackingFourTuplesType.FourTupleType)ll.get(i));
                tuples[i] = new FourTuple(curr.getTarget().trim(),curr.getCompetitorOne().trim(),curr.getCompetitorTwo().trim(),curr.getCompetitorThree().trim());
            }
        }
        else 
            tuples = null;
        return new FourTupleIterator(tuples,expr);
    }
    public Conditional buildConditional(JtConditionalType r){
        if(null == r) return null;
        Predicate pred = buildPredicate(r.getIf());
        Expression[] thens = new Expression[r.getThen().size()];
        for(int i=0;i<thens.length;i++)
            thens[i] = buildExpression((JtExpressionType)r.getThen().get(i));
        //Expression[] elses = new Expression[r.getThen().size()];
        //for(int i=0;i<elses.length;i++)
        //    elses[i] = buildExpression((JtExpressionType)r.getElse().get(i));
        Conditional cond = new Conditional(pred,thens,null);        
        //System.out.println(cond.getClass().getName()+"\tbuild conditional: "+cond.XMLTag());
        return cond;
    }        
    public Primitive buildPrimitive(edu.uconn.psy.jtrace.parser.JtPrimitiveType r){
        if(null == r) return null;
        Primitive prim;
        if(null != r.getDecimal()) prim = new Decimal(r.getDecimal().doubleValue());        
        else if(null != r.getFile()) prim = buildFile((JtFileNameType)r.getFile());
        else if(null != r.getLexicon()) prim = buildLexicon((JtLexiconType)r.getLexicon());
        //else if(null != r.getList()) prim = buildList((JtListOfPrimitivesType)r.getList());
        else if(null != r.getParameters()) prim = buildParam((JtParametersType)r.getParameters());
        else if(null != r.getPredicate()) prim = buildPredicate((JtPredicateType)r.getPredicate());
        else if(null != r.getQuery()) prim = buildQuery((JtQueryType)r.getQuery());
        else if(null != r.getText()) prim = new Text(r.getText());
        else prim = new Int(r.getInt());
        //System.out.println(prim.getClass().getName()+"\tbuild primitive: "+prim.XMLTag());
        return prim;
    } 
    public Primitive buildPrimitive(edu.uconn.psy.jtrace.parser.JtTextOrQueryType r){
        if(null == r) return null;
        Primitive prim;
        if(null != r.getQuery()) prim = buildQuery((JtQueryType)r.getQuery());
        else// if(null != r.getText()) 
            prim = new Text(r.getText());
        //System.out.println(prim.getClass().getName()+"\tbuild primitive: "+prim.XMLTag());
        return prim;
    }
    public TraceLexicon buildLexicon(edu.uconn.psy.jtrace.parser.JtLexiconType r){
        if(null == r) return null; 
        TraceLexicon lex = new TraceLexicon();        
        List lst = r.getLexeme();
        if(null==lst||lst.size()==0) return lex;
        for(int i=0;i<lst.size();i++){              
            TraceWord _word = new TraceWord(((JtLexemeType)lst.get(i)).getPhonology().trim());                            
            if(((JtLexemeType)lst.get(i)).getFrequency()!=null)
                _word.setFrequency(((JtLexemeType)lst.get(i)).getFrequency().doubleValue());
            if(((JtLexemeType)lst.get(i)).getLabel()!=null)
                _word.setLabel(((JtLexemeType)lst.get(i)).getLabel().trim());
            if(((JtLexemeType)lst.get(i)).getPrime()!=null)
                _word.setPrime(((JtLexemeType)lst.get(i)).getPrime().doubleValue());
            
            lex.add(_word);
        }
        //System.out.println("build lexicon: "+lex.XMLTag());        
        return lex;
    }
    public TracePhones buildPhonology(edu.uconn.psy.jtrace.parser.JtPhonologyType r){
        if(null == r) return null; 
        String langName=r.getLanguageName();
        List rw = r.getPhonemes().getPhoneme();
        String phonSymbols[]=new String[rw.size()];
        double features[][]=new double[rw.size()][];
        double durScale[][]=new double[rw.size()][];
        boolean allRel[][]=new boolean[rw.size()][];        
        try{      
            for(int i=0;i<rw.size();i++){
                phonSymbols[i]=((edu.uconn.psy.jtrace.parser.JtPhonologyType.PhonemesType.PhonemeType)rw.get(i)).getSymbol();
                List ftList=((edu.uconn.psy.jtrace.parser.JtPhonologyType.PhonemesType.PhonemeType)rw.get(i)).getFeatures();
                double ftArray[]=new double[ftList.size()]; 
                for(int j=0;j<ftList.size();j++){                
                    ftArray[j]=((java.math.BigDecimal)ftList.get(j)).doubleValue();
                }
                features[i]=ftArray;            
                //
                List durList=((edu.uconn.psy.jtrace.parser.JtPhonologyType.PhonemesType.PhonemeType)rw.get(i)).getDurationScalar();
                double durArray[]=new double[durList.size()]; 
                for(int k=0;k<durList.size();k++){                
                    durArray[k]=((java.math.BigDecimal)durList.get(k)).doubleValue();
                }                
                durScale[i]=durArray;
                //
                List allList=((edu.uconn.psy.jtrace.parser.JtPhonologyType.PhonemesType.PhonemeType)rw.get(i)).getAllophonicRelations();
                boolean cur;
                boolean allArray[]=new boolean[allList.size()]; 
                for(int k=0;k<allList.size();k++){                                    
                    cur=((java.lang.Boolean)allList.get(k)).booleanValue();
                    allArray[k]=cur;
                    //System.out.println("allophonic["+k+"]="+cur);
                    //if(cur==0) allArray[k]=false;
                    //else if(cur==1) allArray[k]=true;
                    //else allArray[k]=false;
                }                
                allRel[i]=allArray;
            }
        } catch(Exception e){
            e.printStackTrace();
            return new TracePhones();
        }
        TracePhones newPhon = new TracePhones(langName,phonSymbols,features,durScale);
        newPhon.setAllophoneRelations(allRel);
        return newPhon;
    }
           
    public ListOfPrimitives buildList(java.util.List l){
        if(null == l) return null;
        java.util.LinkedList ll = new java.util.LinkedList();
        for(int i=0;i<l.size();i++)
            ll.add(buildPrimitive((JtTextOrQueryType)l.get(i)));
        return new ListOfPrimitives(ll);        
    }    
    /*public ListOfPrimitives buildList(edu.uconn.psy.jtrace.parser.JtListOfPrimitivesType r){
        if(null == r) return null;
        java.util.LinkedList ll = new java.util.LinkedList();
        for(int i=0;i<r.getArg().size();i++)
            ll.add(buildPrimitive((JtPrimitiveType)r.getArg().get(i)));
        return new ListOfPrimitives(ll);        
    }*/
    public FileLocator buildFile(edu.uconn.psy.jtrace.parser.JtFileNameType r){
        if(null == r) return null;
        return new FileLocator(r.getAbsolutePath(), r.getRelativePath(), r.getName());       
    }
    
    public Query buildQuery(edu.uconn.psy.jtrace.parser.JtQueryType r){        
        if(null == r) return null;
        Query qry;
        String name; 
        String returnType; 
        Primitive[] args;
        if(null!=r.getCurrentInput()){ 
            name="current-input";
            returnType=r.getCurrentInput().getReturnType();
            args=new Primitive[0];
        }
        else if(null!=r.getQueryOneSimulationCell()){ 
            name="query-one-simulation-cell";
            returnType=r.getQueryOneSimulationCell().getReturnType();
            Text lyr; 
            Int ali,cyc;
            args=new Primitive[4];
            if(null!=r.getQueryOneSimulationCell().getLayer().getFeatureLayer()){
                lyr = new Text("feature");
                Text id = new Text(r.getQueryOneSimulationCell().getLayer().getFeatureLayer().getFeature());
                ali = new Int(r.getQueryOneSimulationCell().getLayer().getFeatureLayer().getAlignment());
                cyc = new Int(r.getQueryOneSimulationCell().getLayer().getFeatureLayer().getCycle());                
                args[1]=id;            
            }           
            else if(null!=r.getQueryOneSimulationCell().getLayer().getInputLayer()){
                lyr = new Text("input");
                Text id = new Text(r.getQueryOneSimulationCell().getLayer().getInputLayer().getFeature());
                ali = new Int(r.getQueryOneSimulationCell().getLayer().getInputLayer().getAlignment());
                cyc = new Int(r.getQueryOneSimulationCell().getLayer().getInputLayer().getCycle());                
                args[1]=id;
            } 
            else if(null!=r.getQueryOneSimulationCell().getLayer().getPhonemeLayer()){
                lyr = new Text("phoneme");
                Text id = new Text(r.getQueryOneSimulationCell().getLayer().getPhonemeLayer().getPhoneme());
                ali = new Int(r.getQueryOneSimulationCell().getLayer().getPhonemeLayer().getAlignment());
                cyc = new Int(r.getQueryOneSimulationCell().getLayer().getPhonemeLayer().getCycle());                
                args[1]=id;
            }
            else {//if(null!=r.getQueryOneSimulationCell().getLayer().getInputLayer()){
                lyr = new Text("word");
                Primitive id = buildPrimitive(r.getQueryOneSimulationCell().getLayer().getWordLayer().getWord());
                ali = new Int(r.getQueryOneSimulationCell().getLayer().getWordLayer().getAlignment());
                cyc = new Int(r.getQueryOneSimulationCell().getLayer().getWordLayer().getCycle());                
                args[1]=id;
            }
            args[0]=lyr;
            args[2]=ali;
            args[3]=cyc;
        }
        else if(null!=r.getFetchCurrentValueOfAParameter()){ 
            name="fetch-current-value-of-a-parameter";
            returnType="text";
            args=new Primitive[1];
            args[0] = new Text(r.getFetchCurrentValueOfAParameter().getParameterName());
        }
        else if(null!=r.getCycleWhenItemExceedsThreshold()){ 
            name = "cycle-when-item-exceeds-threshold";
            returnType = r.getCycleWhenItemExceedsThreshold().getReturnType();
            args = new Primitive[2];
            args[0] = buildPrimitive(r.getCycleWhenItemExceedsThreshold().getItem());
            args[1] = new Decimal(r.getCycleWhenItemExceedsThreshold().getThreshold().doubleValue());
        }
        else if(null!=r.getCycleWhenItemPeaks()){ 
            name = "cycle-when-item-peaks";
            returnType = r.getCycleWhenItemPeaks().getReturnType();
            args = new Primitive[1];
            args[0] = buildPrimitive(r.getCycleWhenItemPeaks().getItem());            
        }
        else if(null!=r.getItemWithHighestPeak()){ 
            name="item-with-highest-peak";
            returnType=r.getItemWithHighestPeak().getReturnType();            
            args = new Primitive[0];
        }
        else if(null!=r.getItemWithNthHighestPeak()){ 
            name="item-with-nth-highest-peak";
            returnType=r.getItemWithNthHighestPeak().getReturnType();                        
            args = new Primitive[1];
            args[0] = new Int(r.getItemWithNthHighestPeak().getN());
        }
        else if(null!=r.getSizeOfLexicon()){ 
            name="size-of-lexicon";
            returnType=r.getSizeOfLexicon().getReturnType();                        
            args = new Primitive[0];            
        }
        else if(null!=r.getNthItemInLexicon()){ 
            name="nth-item-in-lexicon";
            returnType=r.getNthItemInLexicon().getReturnType();                        
            args = new Primitive[1];
            args[0] = buildPrimitive(r.getNthItemInLexicon().getN());
        }
        else if(null!=r.getNthItemToExceedThreshold()){ 
            name="nth-item-to-exceed-threshold";
            returnType=r.getNthItemToExceedThreshold().getReturnType();                        
            args = new Primitive[2];
            args[0] = new Int(r.getNthItemToExceedThreshold().getN());
            args[1] = new Decimal(r.getNthItemToExceedThreshold().getThreshold().doubleValue());
        }
        else if(null!=r.getPeakValueOfItem()){ 
            name="peak-value-of-item";
            returnType=r.getPeakValueOfItem().getReturnType();                        
            args = new Primitive[1];
            args[0] = buildPrimitive(r.getPeakValueOfItem().getArg());            
        }
        else if(null!=r.getValueOfHighestPeak()){ 
            name="value-of-highest-peak";
            returnType=r.getValueOfHighestPeak().getReturnType();                        
            args = new Primitive[0];            
        }
        else if(null!=r.getValueOfNthHighestPeak()){ 
            name="value-of-nth-highest-peak";
            returnType=r.getValueOfNthHighestPeak().getReturnType();                                    
            args = new Primitive[1];            
            args[0] = new Int(r.getValueOfNthHighestPeak().getN());
        } 
        else if(null!=r.getDecisionRuleReport()){ 
            name="decision-rule-report";
            returnType=r.getDecisionRuleReport().getReturnType();                                    
            args = new Primitive[3];            
            args[0] = new Decimal(r.getDecisionRuleReport().getThreshold().doubleValue());
            args[1] = buildPrimitive(r.getDecisionRuleReport().getItem());
            args[2] = new Int(r.getDecisionRuleReport().getVerbosity());
        }
        else if(null!=r.getMcClellend1991RunningAverageMetric()){
            name="McClellend-1991-running-average-metric";
            returnType=r.getMcClellend1991RunningAverageMetric().getReturnType();
            args = new Primitive[5];
            args[0] = new Text(r.getMcClellend1991RunningAverageMetric().getPhonemeOne());
            args[1] = new Text(r.getMcClellend1991RunningAverageMetric().getPhonemeTwo());
            args[2] = new Int(r.getMcClellend1991RunningAverageMetric().getAlignment());
            args[3] = new Decimal(r.getMcClellend1991RunningAverageMetric().getLambda().doubleValue());
            args[4] = new Decimal(r.getMcClellend1991RunningAverageMetric().getInitialValue().doubleValue());
        }
        else{
            name="";
            returnType="";
            args = new Primitive[0];
        }
        qry = new Query(name,returnType,args);                
        //System.out.println("build query: "+qry.XMLTag());                
        return qry;
    }
    public Predicate buildPredicate(JtPredicateType r){
        if(null == r) return null;
        String name;
        Primitive[] args;
        if(null != r.getAnd()){
            name = "and";
            args = new Primitive[2];
            args[0] = buildPredicate(r.getAnd().getPredicateOne());
            args[1] = buildPredicate(r.getAnd().getPredicateTwo());
        }
        else if(null != r.getOr()){
            name = "or";
            args = new Primitive[2];
            args[0] = buildPredicate(r.getOr().getPredicateOne());
            args[1] = buildPredicate(r.getOr().getPredicateTwo());
        }
        else if(null != r.getNot()){
            name = "not";
            args = new Primitive[1];
            args[0] = buildPredicate(r.getNot().getPredicateOne());            
        }
        else if(null != r.getTrue()){
            name = "true";
            args = new Primitive[0];            
        }
        else if(null != r.getFalse()){
            name = "false";
            args = new Primitive[0];            
        }
        else if(null != r.getEquals()){
            name = "equals";
            args = new Primitive[2];
            args[0] = buildPrimitive(r.getEquals().getOne());
            args[1] = buildPrimitive(r.getEquals().getTwo());
        }
        else if(null != r.getNotEqual()){
            name = "not-equal";
            args = new Primitive[2];
            args[0] = buildPrimitive(r.getNotEqual().getOne());
            args[1] = buildPrimitive(r.getNotEqual().getTwo());
        }
        else if(null != r.getIsGreaterThan()){
            name = "is-greater-than";
            args = new Primitive[2];
            args[0] = buildPrimitive(r.getIsGreaterThan().getOne());
            args[1] = buildPrimitive(r.getIsGreaterThan().getTwo());
        }
        else if(null != r.getIsLessThan()){
            name = "is-greater-than";
            args = new Primitive[2];
            args[0] = buildPrimitive(r.getIsLessThan().getOne());
            args[1] = buildPrimitive(r.getIsLessThan().getTwo());
        }
        else if(null != r.getIsMemberOfList()){
            name = "is-member-of-list";
            args = new Primitive[2];
            args[0] = buildPrimitive(r.getIsMemberOfList().getSearchItem());
            args[1] = buildPrimitive(r.getIsMemberOfList().getList());
        }
        else{
            name = "";
            args = new Primitive[0];
        }
        return new Predicate(name, args);        
    }
    public Action buildAction(edu.uconn.psy.jtrace.parser.JtActionType r){
        if(null == r) return null;
        String name="";
        Primitive[] args=new Primitive[0];
        if(null != r.getAddOneAnalysisItem()){
            name = "add-one-analysis-item";
            args = new Primitive[1];
            args[0] = buildPrimitive(r.getAddOneAnalysisItem().getArg());
        }
        else if(null != r.getAverageAllAnalysesInCurrentIterationAndSaveGraph()){
            name = "average-all-analyses-in-current-iteration-and-save-graph";
            args = new Primitive[2];
            args[0] = buildList(r.getAverageAllAnalysesInCurrentIterationAndSaveGraph().getLabelsForAveragedGraph());
            args[1] = buildFile(r.getAverageAllAnalysesInCurrentIterationAndSaveGraph().getFile());            
        }
        else if(null != r.getIncrementParameterByThisAmount()){
            name = "increment-parameter-by-this-amount";
            args = new Primitive[2];
            args[0] = new Text(r.getIncrementParameterByThisAmount().getParameterName());
            args[1] = new Decimal(r.getIncrementParameterByThisAmount().getAmount().doubleValue());
        }        
        else if(null != r.getCancelScript()){
            name = "cancel-script";
            args = new Primitive[0];            
        }
        else if(null != r.getAddSilenceToInputEdges()){
            name = "add-silence-to-input-edges";
            args = new Primitive[0];            
        }
        else if(null != r.getWriteToAFile()){
            name = "write-to-a-file";
            args = new Primitive[2];
            args[0] = this.buildList(r.getWriteToAFile().getContent().getArg());            
            args[1] = this.buildFile(r.getWriteToAFile().getFile());            
        }
        else if(null != r.getLoadSimFromFile()){
            name = "load-sim-from-file";
            args = new Primitive[1];
            args[0] = buildFile(r.getLoadSimFromFile().getFile());
        }
        else if(null != r.getNewWindow()){
            name = "new-window";
            args = new Primitive[0];            
        }
        else if(null != r.getRemoveOneAnalysisItem()){
            name = "remove-one-analysis-item";
            args = new Primitive[1];
            args[0] = buildPrimitive(r.getRemoveOneAnalysisItem().getArg());
        }
        else if(null != r.getResetGraphDefaults()){
            name = "reset-graph-defaults";
            args = new Primitive[0];            
        }
        else if(null != r.getSaveGraphToPng()){
            name = "save-graph-to-png";
            args = new Primitive[1];
            args[0] = buildFile(r.getSaveGraphToPng().getFile());
        }        
        else if(null != r.getSaveGraphToTxt()){
            name = "save-graph-to-txt";
            args = new Primitive[2];
            args[0] = buildFile(r.getSaveGraphToTxt().getFile());
            args[1] = new Text(r.getSaveGraphToTxt().getSaveOption());
        }
        else if(null != r.getSaveParametersToJt()){
            name = "save-parameters-to-jt";
            args = new Primitive[1];
            args[0] = buildFile(r.getSaveParametersToJt().getFile());
        }
        else if(null != r.getSaveParametersToTxt()){
            name = "save-parameters-to-txt";
            args = new Primitive[1];
            args[0] = buildFile(r.getSaveParametersToTxt().getFile());
        }
        else if(null != r.getSaveSimulationToJt()){
            name = "save-simulation-to-jt";
            args = new Primitive[1];
            args[0] = buildFile(r.getSaveSimulationToJt().getFile());
        }
        else if(null != r.getSaveSimulationToTxt()){
            name = "save-simulation-to-txt";
            args = new Primitive[1];
            args[0] = buildFile(r.getSaveSimulationToTxt().getFile());
        }
        else if(null != r.getLoadParameters()){
            name = "load-parameters";
            args = new Primitive[1];
            args[0] = buildFile(r.getLoadParameters().getParameterFile());
        }
        else if(null != r.getSetAParameter()){
            name = "set-a-parameter";
            args = new Primitive[2];
            args[0] = new Text(r.getSetAParameter().getParameterName());
            args[1] = buildPrimitive(r.getSetAParameter().getValue());
        }
        else if(null != r.getSetInput()){
            name = "set-input";
            args = new Primitive[1];
            args[0] = new Text(r.getSetInput().getInput());            
        }
        else if(null != r.getSetAnalysisType()){
            name = "set-analysis-type";
            args = new Primitive[1];
            args[0] = new Text(r.getSetAnalysisType().getType());
        }
        else if(null != r.getSetChoiceType()){
            name = "set-choice-type";
            args = new Primitive[1];
            args[0] = new Text(r.getSetChoiceType().getType());
        }
        else if(null != r.getSetCyclesPerSim()){
            name = "set-cycles-per-sim";
            args = new Primitive[1];
            args[0] = new Int(r.getSetCyclesPerSim().getCycles());
        }
        else if(null != r.getSetContentType()){
            name = "set-content-type";
            args = new Primitive[1];
            args[0] = new Text(r.getSetContentType().getType());
        }
        else if(null != r.getAnalysisSettings()){
            name = "analysis-settings";
            args = new Primitive[10];
            //save: domain, content-type, analysis-type, watch-type
            //choice-type, alignment, k-value, watch-n, watch-list
            args[0] = new Text(r.getAnalysisSettings().getDomain());
            if(r.getAnalysisSettings().getContentType().getACTIVATIONS()!=null){
                args[1] = new Text("ACTIVATIONS");            
                args[2] = new Text("NORMAL");
                args[3] = new Int(7);
            }
            else if(r.getAnalysisSettings().getContentType().getCOMPETITION()!=null){
                args[1] = new Text("COMPETITION");
                args[2] = new Text(r.getAnalysisSettings().getContentType().getCOMPETITION().getProcessing());
                args[3] = new Int(r.getAnalysisSettings().getContentType().getCOMPETITION().getSamplingWidth());
            }
            else {
                args[1] = new Text("RESPONSE-PROBABILITIES");
                args[2] = new Text(r.getAnalysisSettings().getContentType().getRESPONSEPROBABILITIES().getChoiceType());
                args[3] = new Int(r.getAnalysisSettings().getContentType().getRESPONSEPROBABILITIES().getK());
            }
            if(r.getAnalysisSettings().getWatch().getWATCHSPECIFIED()!=null){
                args[4] = new Text("WATCH-SPECIFIED");
                args[5]=this.buildList(r.getAnalysisSettings().getWatch().getWATCHSPECIFIED().getWatch());
            }
            else{
                args[4] = new Text("WATCH-TOP-N");
                args[6]=new Int(r.getAnalysisSettings().getWatch().getWATCHTOPN());
            }
            if(r.getAnalysisSettings().getAnalysisType().getAVERAGE()!=null){
                args[7] = new Text("AVERAGE");                
            }
            else if(r.getAnalysisSettings().getAnalysisType().getFRAUENFELDER()!=null){
                args[7] = new Text("FRAUENFELDER");
                args[8] = new Int(r.getAnalysisSettings().getAnalysisType().getFRAUENFELDER().getAlignment());
            }
            else if(r.getAnalysisSettings().getAnalysisType().getSPECIFIED()!=null){
                args[7] = new Text("SPECIFIED");                
                args[8] = new Int(r.getAnalysisSettings().getAnalysisType().getSPECIFIED().getAlignment());
            }
            else if(r.getAnalysisSettings().getAnalysisType().getMAXADHOC()!=null){
                args[7] = new Text("MAX-ADHOC");                
            }
            else if(r.getAnalysisSettings().getAnalysisType().getMAXADHOC2()!=null){
                args[7] = new Text("MAX-ADHOC-2");                
            }
            else if(r.getAnalysisSettings().getAnalysisType().getMAXPOSTHOC()!=null){
                args[7] = new Text("MAX-POSTHOC");                
            }                        
        }
        else if(null != r.getSetCompCalcType()){
            name = "set-comp-calc-type";
            args = new Primitive[1];
            args[0] = new Text(r.getSetCompCalcType().getType());
        }
        else if(null != r.getSetCompSlopeWidth()){
            name = "set-comp-slope-width";
            args = new Primitive[1];
            args[0] = new Int(r.getSetCompSlopeWidth().getWidth());
        }
        else if(null != r.getSetGraphDomain()){
            name = "set-graph-domain";
            args = new Primitive[1];
            args[0] = new Text(r.getSetGraphDomain().getDomain());
        }
        else if(null != r.getSetGraphInputPosition()){
            name = "set-graph-input-position";
            args = new Primitive[1];
            args[0] = new Int(r.getSetGraphInputPosition().getPosition());
        }
        else if(null != r.getSetGraphTitle()){
            name = "set-graph-title";
            args = new Primitive[1];
            args[0] = new Text(r.getSetGraphTitle().getTitle());                                    
        }
        else if(null != r.getSetGraphXAxisBounds()){
            name = "set-graph-x-axis-bounds";
            args = new Primitive[2];
            args[0] = new Decimal(r.getSetGraphXAxisBounds().getLeftBound().doubleValue());
            args[1] = new Decimal(r.getSetGraphXAxisBounds().getRightBound().doubleValue());
        }
        else if(null != r.getSetGraphXAxisLabel()){
            name = "set-graph-x-axis-label";
            args = new Primitive[1];
            args[0] = new Text(r.getSetGraphXAxisLabel().getLabel());
        }
        else if(null != r.getSetGraphYAxisBounds()){
            name = "set-graph-y-axis-bounds";
            args = new Primitive[2];
            args[0] = new Decimal(r.getSetGraphYAxisBounds().getLowerBound().doubleValue());
            args[1] = new Decimal(r.getSetGraphYAxisBounds().getUpperBound().doubleValue());
        }
        else if(null != r.getSetGraphYAxisLabel()){
            name = "set-graph-y-axis-label";
            args = new Primitive[1];
            args[0] = new Text(r.getSetGraphYAxisLabel().getLabel());
        }
        else if(null != r.getSetKValue()){
            name = "set-k-value";
            args = new Primitive[1];
            args[0] = new Int(r.getSetKValue().getExponent());
        }
        else if(null != r.getSetAlignment()){
            name = "set-alignment";
            args = new Primitive[1];
            args[0] = new Int(r.getSetAlignment().getAlignment());
        }
        else if(null != r.getSetLexicon()){
            name = "set-lexicon";
            args = new Primitive[1];
            args[0] = buildLexicon(r.getSetLexicon().getLexicon());
        }
        else if(null != r.getLoadLexicon()){
            name = "load-lexicon";
            args = new Primitive[1];
            args[0] = buildFile(r.getLoadLexicon().getFile());
        }
        else if(null != r.getSetParameters()){            
            name = "set-parameters";
            args = new Primitive[1];
            args[0] = this.buildParam(r.getSetParameters().getParameters());
        }
        else if(null != r.getSetRootDirectory()){
            name = "set-root-directory";
            args = new Primitive[1];
            if(r.getSetRootDirectory().getDirectory()!=null)
                args[0] = new Text(r.getSetRootDirectory().getDirectory());
            else if(r.getSetRootDirectory().getUseScriptFilePath()!=null)
                args[0] = new Text("use-script-file-path");
        }
        else if(null != r.getSetWatchItems()){
            name = "set-watch-items";
            args = new Primitive[1];
            args[0] = buildList(r.getSetWatchItems().getList().getWatch());
        }
        else if(null != r.getSetWatchTopN()){
            name = "set-watch-top-n";
            args = new Primitive[1];
            args[0] = new Int(r.getSetWatchTopN().getN());
        }
        else if(null != r.getSetWatchType()){
            name = "set-watch-type";
            args = new Primitive[1];
            args[0] = new Text(r.getSetWatchType().getType());
        }
        else if(null != r.getAppendToLexicon()){
            name = "append-to-lexicon";
            args = new Primitive[1];
            if(r.getAppendToLexicon().getListOfWords()!=null){
                args[0] = buildLexicon(r.getAppendToLexicon().getListOfWords());
            }
            else if(r.getAppendToLexicon().getOneWord()!=null){
                TraceWord wd;
                TraceLexicon lx;
                if(r.getAppendToLexicon().getOneWord().getText()!=null){
                    wd = new TraceWord(r.getAppendToLexicon().getOneWord().getText());
                    lx = new TraceLexicon();
                    lx.add(wd);
                    args[0] = lx;
                }
                else{
                    Query qry = buildQuery(r.getAppendToLexicon().getOneWord().getQuery());                     
                    args[0] = qry;
                }
            }
            else{
                args[0] = buildFile(r.getAppendToLexicon().getLexiconFromFile());
            }            
        }        
        else {
            name = "";
            args = new Primitive[0];            
        }
        return new Action(name, args);
    }
    public TraceParam buildParam(edu.uconn.psy.jtrace.parser.JtParametersType r){
        if(null == r) return null;
        TraceParam tp = new TraceParam();
        List rw = r.getLexiconOrPhonologyOrStringParam();
        Object o;
        String name;
        //TODO : fill in parameter setting cases below.
        for(int i=0;i<rw.size();i++){
            o = rw.get(i);            
            if(null == o) continue;
            if(o instanceof JtParametersType.DecimalParam){
                name = ((JtParametersType.DecimalParam)o).getName();
                if(name.equalsIgnoreCase("alpha.IF")) tp.getAlpha().IF = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("alpha.FP")) tp.getAlpha().FP = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("alpha.PF")) tp.getAlpha().PF = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("alpha.PW")) tp.getAlpha().PW = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("alpha.WP")) tp.getAlpha().WP = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("decay.F")) tp.getDecay().F = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("decay.P")) tp.getDecay().P = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("decay.W")) tp.getDecay().W = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("rest.F")) tp.getRest().F = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("rest.P")) tp.getRest().P = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("rest.W")) tp.getRest().W = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("gamma.F")) tp.getGamma().F = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("gamma.P")) tp.getGamma().P = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("gamma.W")) tp.getGamma().W = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("min")) tp.setMin(((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue());
                else if(name.equalsIgnoreCase("max")) tp.setMax(((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue());
                else if(name.equalsIgnoreCase("noisesd")) tp.setNoiseSD(((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue());
                else if(name.equalsIgnoreCase("stochasticity")) tp.setStochasticitySD(((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue());
                
                else if(name.equalsIgnoreCase("atten")) tp.setAtten(((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue());
                else if(name.equalsIgnoreCase("bias")) tp.setBias(((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue());
                else if(name.equalsIgnoreCase("learningRate")) tp.setLearningRate(((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue());
                
                
                else if(name.equalsIgnoreCase("resting_frq_scale")) tp.getFreqNode().RDL_rest_s = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("resting_frq_constant")) tp.getFreqNode().RDL_rest_c = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("weight_frq_scale")) tp.getFreqNode().RDL_wt_s = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("weight_frq_constant")) tp.getFreqNode().RDL_wt_c = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("post_frq_constant")) tp.getFreqNode().RDL_post_c = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();                
                
                else if(name.equalsIgnoreCase("resting_prim_scale")) tp.getPrimeNode().RDL_rest_s = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("resting_prim_constant")) tp.getPrimeNode().RDL_rest_c = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("weight_prim_scale")) tp.getPrimeNode().RDL_wt_s = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("weight_prim_constant")) tp.getPrimeNode().RDL_wt_c = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();
                else if(name.equalsIgnoreCase("post_prim_constant")) tp.getPrimeNode().RDL_post_c = ((JtParametersType.DecimalParam)o).getDecimalValue().doubleValue();                
                
            }
            else if(o instanceof JtParametersType.IntParam){
                name = ((JtParametersType.IntParam)o).getName();
                if(name.equalsIgnoreCase("nreps")) tp.setNReps(((JtParametersType.IntParam)o).getIntValue().intValue());
                else if(name.equalsIgnoreCase("slicesPerPhon")) tp.setSlicesPerPhon(((JtParametersType.IntParam)o).getIntValue().intValue());
                else if(name.equalsIgnoreCase("fslices")) tp.setFSlices(((JtParametersType.IntParam)o).getIntValue().intValue());
                else if(name.equalsIgnoreCase("deltainput")) tp.setDeltaInput(((JtParametersType.IntParam)o).getIntValue().intValue());                                                
                else if(name.equalsIgnoreCase("lengthNormalization")) tp.setLengthNormalization(((JtParametersType.IntParam)o).getIntValue().intValue());                                                
            }
            else if(o instanceof JtParametersType.StringParam){
                name = ((JtParametersType.StringParam)o).getName();
                if(name.equalsIgnoreCase("modelInput")) tp.setModelInput(((JtParametersType.StringParam)o).getStringValue());
                else if(name.equalsIgnoreCase("user")) tp.setUser(((JtParametersType.StringParam)o).getStringValue());
                else if(name.equalsIgnoreCase("dateTime")) tp.setDateTime(((JtParametersType.StringParam)o).getStringValue());
                else if(name.equalsIgnoreCase("comment")) tp.setComment(((JtParametersType.StringParam)o).getStringValue());
                else if(name.equalsIgnoreCase("continuumspec")) tp.setContinuumSpec(((JtParametersType.StringParam)o).getStringValue());                
                else if(name.equalsIgnoreCase("resting_frq_on")){ 
                    String bool= ((JtParametersType.StringParam)o).getStringValue();
                    if(bool.equals("true")) tp.getFreqNode().RDL_rest = true;
                    else if(bool.equals("false")) tp.getFreqNode().RDL_rest = false;
                }
                else if(name.equalsIgnoreCase("weight_frq_on")){ 
                    String bool= ((JtParametersType.StringParam)o).getStringValue();
                    if(bool.equals("true")) tp.getFreqNode().RDL_wt = true;
                    else if(bool.equals("false")) tp.getFreqNode().RDL_wt = false;
                }
                else if(name.equalsIgnoreCase("post_frq_on")){ 
                    String bool= ((JtParametersType.StringParam)o).getStringValue();
                    if(bool.equals("true")) tp.getFreqNode().RDL_post = true;
                    else if(bool.equals("false")) tp.getFreqNode().RDL_post = false;
                }                
            }
            else if(o instanceof JtParametersType.DecimalParamRep){
                name = ((JtParametersType.DecimalParamRep)o).getName();
                if(name.equalsIgnoreCase("speadscale")){
                    List ll=((JtParametersType.DecimalParamRep)o).getDecimalValue();
                    for(int j=0;j<ll.size();j++)
                        tp.getSpreadScale()[j]=((java.math.BigDecimal)ll.get(j)).doubleValue();
                }            
            }
            else if(o instanceof JtParametersType.IntParamRep){
                name = ((JtParametersType.IntParamRep)o).getName();
                if(name.equalsIgnoreCase("spread")||name.equalsIgnoreCase("fetspread")){
                    List ll=((JtParametersType.IntParamRep)o).getIntValue();
                    for(int j=0;j<ll.size();j++)
                        tp.getSpread()[j]=((java.math.BigInteger)ll.get(j)).intValue();
                }                
            }
            else if(o instanceof JtParametersType.StringParamRep){
                name = ((JtParametersType.StringParamRep)o).getName();                
            }
            else if(o instanceof JtParametersType.Lexicon){
                edu.uconn.psy.jtrace.Model.TraceLexicon lex = buildLexicon((edu.uconn.psy.jtrace.parser.JtLexiconType)o);
                tp.setLexicon(lex);
            } 
            else if(o instanceof JtParametersType.Phonology){
                System.out.println("building JtParametersType.Phonology !");
                edu.uconn.psy.jtrace.Model.TracePhones phn = buildPhonology((JtParametersType.Phonology)o);
                tp.setPhonology(phn);
            }
        }
        return tp;
    }
    public boolean validateJT(){
        Object r;
        boolean result;
        try{
            JAXBContext ctx = JAXBContext.newInstance( "edu.uconn.psy.jtrace.parser" );
            Unmarshaller unm = ctx.createUnmarshaller();
            Validator v = ctx.createValidator();    
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source));
            r=unm.unmarshal(bis);
            result=v.validate(r);
            if(result==false) 
                System.out.println("file "+source.getName()+" did not validate.");
        }
        catch( Exception e ) {            
            System.out.println("WTFileReader.validateJT() : unmarshall attempt failed on file: "+source.getAbsolutePath()+".");
            e.printStackTrace(System.out); 
            return false;
        }
        //System.out.println(result+" = validation of "+source.getName());
        return result;
    }
    public String getFileAsAString(){
        try{
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source));
            byte[] buf=new byte[bis.available()];
            bis.read(buf);
            return new String(buf);    
        }        
        catch(IOException ioe){ioe.printStackTrace(); return new String("<>IO problem reading requested file</>\n ");}
        catch(Exception e){e.printStackTrace(); return new String("<>General problem reading requested file</>\n ");}
    }
    public edu.uconn.psy.jtrace.Model.TraceParam readWTParameterFile(){
        
        return new edu.uconn.psy.jtrace.Model.TraceParam();
    }
    public edu.uconn.psy.jtrace.Model.Scripting.TraceScript loadJTScript(){
        Object r;
        try{
            JAXBContext ctx = JAXBContext.newInstance( "edu.uconn.psy.jtrace.parser" );
            Unmarshaller unm = ctx.createUnmarshaller();
            Validator v = ctx.createValidator();    
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source));
            r=unm.unmarshal(bis);
            //System.out.println("result from validating source file "+source.getName()+"="+v.validate(r));            
            edu.uconn.psy.jtrace.parser.JtType raw=(edu.uconn.psy.jtrace.parser.JtType)r;            
            TraceScript result=buildScript(raw);
            bis.close();
            bis=null;
            return result;
            
        }
        catch( JAXBException je ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() : JAXBException : .");
            je.printStackTrace(System.out);            
            return new TraceScript();
        }
        catch( Exception e ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() :");
            e.printStackTrace(System.out); 
            return new TraceScript();
        }
    }
    public boolean validateLexiconFile(){
        Object r;
        boolean result;
        try{
            JAXBContext ctx = JAXBContext.newInstance( "edu.uconn.psy.jtrace.parser" );
            Unmarshaller unm = ctx.createUnmarshaller();
            Validator v = ctx.createValidator();    
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source));
            r=unm.unmarshal(bis);
            if(r instanceof edu.uconn.psy.jtrace.parser.Lexicon){
                edu.uconn.psy.jtrace.parser.Lexicon raw=(edu.uconn.psy.jtrace.parser.Lexicon)r;            
                result = v.validate(r);
            }
            else
                result = false;
            return result;
        }
        catch( JAXBException je ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() : JAXBException : .");
            je.printStackTrace(System.out);            
            return false;
        }
        catch( Exception e ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() :");
            e.printStackTrace(System.out); 
            return false;
        }
    }
    public edu.uconn.psy.jtrace.Model.TraceLexicon loadJTLexicon(){
        Object r;
        try{
            JAXBContext ctx = JAXBContext.newInstance( "edu.uconn.psy.jtrace.parser" );
            Unmarshaller unm = ctx.createUnmarshaller();
            Validator v = ctx.createValidator();    
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source));
            r=unm.unmarshal(bis);
            //System.out.println("result from validating source file "+source.getName()+"="+v.validate(r));            
            edu.uconn.psy.jtrace.parser.Lexicon raw=(edu.uconn.psy.jtrace.parser.Lexicon)r;            
            
            TraceLexicon result=buildLexicon((edu.uconn.psy.jtrace.parser.JtLexiconType)raw);
            bis.close();
            bis=null;
            return result;
            
        }
        catch( JAXBException je ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() : JAXBException : .");
            je.printStackTrace(System.out);            
            return new TraceLexicon();
        }
        catch( Exception e ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() :");
            e.printStackTrace(System.out); 
            return new TraceLexicon();
        }
    }
    public boolean validatePhonologyFile(){
        Object r;
        boolean result;
        try{
            JAXBContext ctx = JAXBContext.newInstance( "edu.uconn.psy.jtrace.parser" );
            Unmarshaller unm = ctx.createUnmarshaller();
            Validator v = ctx.createValidator();    
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source));
            r=unm.unmarshal(bis);
            if(r instanceof edu.uconn.psy.jtrace.parser.Phonology){
                edu.uconn.psy.jtrace.parser.Phonology raw=(edu.uconn.psy.jtrace.parser.Phonology)r;            
                result = v.validate(r);
            }
            else
                result = false;
            return result;
        }
        catch(JAXBException je ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() : JAXBException : .");
            je.printStackTrace(System.out);            
            return false;
        }
        catch( Exception e ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() :");
            e.printStackTrace(System.out); 
            return false;
        }
    }
    public edu.uconn.psy.jtrace.Model.TracePhones loadJTPhonology(){
        Object r;
        try{
            JAXBContext ctx = JAXBContext.newInstance( "edu.uconn.psy.jtrace.parser" );
            Unmarshaller unm = ctx.createUnmarshaller();
            Validator v = ctx.createValidator();    
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source));
            r=unm.unmarshal(bis);
            //System.out.println("result from validating source file "+source.getName()+"="+v.validate(r));            
            edu.uconn.psy.jtrace.parser.Phonology raw=(edu.uconn.psy.jtrace.parser.Phonology)r;            
            
            TracePhones result=buildPhonology((edu.uconn.psy.jtrace.parser.JtPhonologyType)raw);
            bis.close();
            bis=null;
            return result;
            
        }
        catch( JAXBException je ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() : JAXBException : .");
            je.printStackTrace(System.out);            
            return new TracePhones();
        }
        catch( Exception e ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() :");
            e.printStackTrace(System.out); 
            return new TracePhones();
        }
    }
    public boolean validateParametersFile(){
        Object r;
        boolean result;
        try{
            JAXBContext ctx = JAXBContext.newInstance( "edu.uconn.psy.jtrace.parser" );
            Unmarshaller unm = ctx.createUnmarshaller();
            Validator v = ctx.createValidator();    
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source));
            r=unm.unmarshal(bis);
            if(r instanceof edu.uconn.psy.jtrace.parser.Parameters){
                edu.uconn.psy.jtrace.parser.Parameters raw=(edu.uconn.psy.jtrace.parser.Parameters)r;            
                result = v.validate(r);
            }
            else
                result = false;
            return result;
        }
        catch( JAXBException je ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() : JAXBException : .");
            je.printStackTrace(System.out);            
            return false;
        }
        catch( Exception e ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() :");
            e.printStackTrace(System.out); 
            return false;
        }
    }
    public edu.uconn.psy.jtrace.Model.TraceParam loadJTParam(){
        Object r;
        try{
            JAXBContext ctx = JAXBContext.newInstance( "edu.uconn.psy.jtrace.parser" );
            Unmarshaller unm = ctx.createUnmarshaller();
            Validator v = ctx.createValidator();    
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source));
            r=unm.unmarshal(bis);
            //System.out.println("result from validating source file "+source.getName()+"="+v.validate(r));            
            edu.uconn.psy.jtrace.parser.Parameters raw=(edu.uconn.psy.jtrace.parser.Parameters)r;            
            
            TraceParam result=this.buildParam((edu.uconn.psy.jtrace.parser.JtParametersType)raw);
            bis.close();
            bis=null;
            return result;
            
        }
        catch( JAXBException je ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() : JAXBException : .");
            je.printStackTrace(System.out);            
            return new TraceParam();
        }
        catch( Exception e ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() :");
            e.printStackTrace(System.out); 
            return new TraceParam();
        }
    }
    
    public edu.uconn.psy.jtrace.Model.TraceParam loadJTParamFromJT(){
        Object r;
        try{
            JAXBContext ctx = JAXBContext.newInstance( "edu.uconn.psy.jtrace.parser" );
            Unmarshaller unm = ctx.createUnmarshaller();
            Validator v = ctx.createValidator();    
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source));
            r=unm.unmarshal(bis);
            edu.uconn.psy.jtrace.parser.JtType raw=(edu.uconn.psy.jtrace.parser.JtType)r;            
            //TraceScript script=buildScript(raw);
            edu.uconn.psy.jtrace.parser.JtParametersType jtparam = ((edu.uconn.psy.jtrace.parser.JtActionType.SetParametersType)((edu.uconn.psy.jtrace.parser.JtActionType)raw.getScript().getIterateOrConditionOrAction().get(0)).getSetParameters()).getParameters();
            TraceParam result=buildParam(jtparam);
            bis=null;
            return result;            
        }
        catch( JAXBException je ) {            
            System.out.println("Exception in JTFileReader.loadJTParamFromJT() : JAXBException : .");
            je.printStackTrace(System.out);            
            return null;
        }
        catch( Exception e ) {            
            System.out.println("Exception in JTFileReader.loadJTParamFromJT() :");
            e.printStackTrace(System.out); 
            return null;
        }
    }
    
    public double[][][][] loadValidationData(){
        Object r;
        double[][][][] validationD;
        try{
            JAXBContext ctx = JAXBContext.newInstance( "edu.uconn.psy.jtrace.parser" );
            Unmarshaller unm = ctx.createUnmarshaller();
            Validator v = ctx.createValidator();    
            if(!source.exists()||!source.isDirectory()) return null;
            int countCycles=0;
            File[] listFiles = source.listFiles();
            for(int i=0;i<listFiles.length;i++)
                if(listFiles[i].getName().startsWith("feature_"))
                    countCycles++;
            validationD = new double[4][countCycles][][];
            int inCt=0, ftCt=0, phCt=0, wdCt=0;
            List rows, line;
            double[][] frame;
            for(int i=0;i<listFiles.length;i++){
                //System.out. ("processing file: "+listFiles[i].getName());
                if(listFiles[i].getName().startsWith("input_")){
                    BufferedInputStream bis=new BufferedInputStream(new FileInputStream(listFiles[i]));
                    r=unm.unmarshal(bis);
                    rows=((edu.uconn.psy.jtrace.parser.InputData)r).getRow();            
                    frame = new double[rows.size()][];
                    for(int j=0;j<rows.size();j++){
                        line=((edu.uconn.psy.jtrace.parser.InputData.RowType)rows.get(j)).getA();
                        frame[j]=new double[line.size()];
                        for(int k=0;k<line.size();k++)
                            frame[j][k]=((java.math.BigDecimal)line.get(k)).doubleValue();
                    }
                    //System.out.println("validationD[0]["+inCt+"]["+frame.length+"]["+frame[0].length+"]");
                    if(inCt<validationD[0].length)
                        validationD[0][inCt++] = frame;
                    frame=null; rows=null; line=null;
                }
                else if(listFiles[i].getName().startsWith("feature_")){
                    BufferedInputStream bis=new BufferedInputStream(new FileInputStream(listFiles[i]));
                    r=unm.unmarshal(bis);
                    rows=((edu.uconn.psy.jtrace.parser.FeatureData)r).getRow();            
                    frame = new double[rows.size()][];
                    for(int j=0;j<rows.size();j++){
                        line=((edu.uconn.psy.jtrace.parser.FeatureData.RowType)rows.get(j)).getA();
                        frame[j]=new double[line.size()];
                        for(int k=0;k<line.size();k++)
                            frame[j][k]=((java.math.BigDecimal)line.get(k)).doubleValue();
                    }
                    //System.out.println("validationD[1]["+ftCt+"]["+frame.length+"]["+frame[0].length+"]");
                    if(ftCt<validationD[1].length)
                        validationD[1][ftCt++] = frame;
                    frame=null; rows=null; line=null;
                }
                else if(listFiles[i].getName().startsWith("phon_")){
                    BufferedInputStream bis=new BufferedInputStream(new FileInputStream(listFiles[i]));
                    r=unm.unmarshal(bis);
                    rows=((edu.uconn.psy.jtrace.parser.PhonemeData)r).getRow();            
                    frame = new double[rows.size()][];
                    for(int j=0;j<rows.size();j++){
                        line=((edu.uconn.psy.jtrace.parser.PhonemeData.RowType)rows.get(j)).getA();
                        frame[j]=new double[line.size()];
                        for(int k=0;k<line.size();k++)
                            frame[j][k]=((java.math.BigDecimal)line.get(k)).doubleValue();
                    }
                    //System.out.println("validationD[2]["+phCt+"]["+frame.length+"]["+frame[0].length+"]");
                    if(phCt<validationD[2].length)
                        validationD[2][phCt++] = frame;
                    frame=null; rows=null; line=null;
                }
                else if(listFiles[i].getName().startsWith("word_")){
                    BufferedInputStream bis=new BufferedInputStream(new FileInputStream(listFiles[i]));
                    r=unm.unmarshal(bis);
                    rows=((edu.uconn.psy.jtrace.parser.WordData)r).getRow();            
                    frame = new double[rows.size()][];
                    for(int j=0;j<rows.size();j++){
                        line=((edu.uconn.psy.jtrace.parser.WordData.RowType)rows.get(j)).getA();
                        frame[j]=new double[line.size()];
                        for(int k=0;k<line.size();k++)
                            frame[j][k]=((java.math.BigDecimal)line.get(k)).doubleValue();
                    }
                    //System.out.println("validationD[3]["+wdCt+"]["+frame.length+"]["+frame[0].length+"]");
                    if(wdCt<validationD[3].length)
                        validationD[3][wdCt++] = frame;
                    frame=null; rows=null; line=null;
                }
                else{
                
                }                
            }
            return validationD;            
        }
        catch( JAXBException je ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() : JAXBException : .");
            je.printStackTrace(System.out);            
            return null;
        }
        catch( Exception e ) {            
            System.out.println("Exception in JTFileReader.loadJTScript() :");
            e.printStackTrace(System.out); 
            return null;
        }
    }
    
}

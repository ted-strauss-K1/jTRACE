/*
 * ParameterActionWorkhorse.java
 *
 * Created on May 4, 2005, 2:22 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.Model.*;
/**
 *
 * @author tedstrauss
 */
public class ParameterActionWorkhorse extends Workhorse{
    
    /** Creates a new instance of ParameterActionWorkhorse */
    public ParameterActionWorkhorse(TraceParam p, TraceSim s, TraceSimAnalysis g,edu.uconn.psy.jtrace.UI.GraphParameters gp,TraceScript scr){
        super(p,s,g,gp,scr);
    }
    //DIRECTS PROCESS TOWARDS PARAMETER ACTIONS , THAT ALL RETURN TRACE PARAMETERS
    public TraceParam applyAction(edu.uconn.psy.jtrace.Model.Scripting.Action act) throws JTraceScriptingException{        
        TraceParam result=currParam;
        String name=act.name();
        /*
        <xsd:element name="set-lexicon">
        <xsd:element name="set-parameters">
        <xsd:element name="set-a-parameter">
        */
        
        if(name.equals("set-a-parameter")){            
            //argument check : if arguments don't match specs, throw an exception
            if(null==act.arguments()[0]||null==act.arguments()[1])
                throw new JTraceScriptingException("set-one-parameter-value argument check failed : requires two parameters (Text, Primitive)");
            if(act.arguments().length!=2)
                throw new JTraceScriptingException("set-one-parameter-value argument check failed : requires two parameters (Text, Primitive)");
            Primitive value = act.arguments()[1];
            if(value instanceof Query)
                value = currScript.interpretQuery(value.queryValue());
            result = setOneTraceParameterValue(result,act.arguments()[0].textValue().value(),value);
        }
        else if(name.equals("set-input")){
            if(null==act.arguments()||null==act.arguments()[0])
                throw new JTraceScriptingException("set-input argument check failed : requires one parameter (Text)");
            Text value = (Text)act.arguments()[0];
            currParam.setModelInput(value.value());
            result = currParam;
        }
        else if(name.equals("increment-parameter-by-this-amount")){            
            //argument check : if arguments don't match specs, throw an exception
            if(null==act.arguments()[0])
                throw new JTraceScriptingException("increment-parameter-by-this-amount argument check failed : requires two parameters (Text, Primitive)");
            if(null==act.arguments()[1])
                throw new JTraceScriptingException("increment-parameter-by-this-amount argument check failed : requires two parameters (Text, Primitive)");
            if(act.arguments().length!=2)
                throw new JTraceScriptingException("increment-parameter-by-this-amount argument check failed : requires two parameters (Text, Primitive)");
            Primitive value = act.arguments()[1];
            if(value instanceof Query)
                value = currScript.interpretQuery(value.queryValue());
            result = tweakOneTraceParameterValue(result,act.arguments()[0].textValue().value(),value);
        }        
        else if(name.equals("add-silence-to-input-edges")){ 
            currParam.setModelInput("-".concat(currParam.getModelInput()).concat("-"));
            result = currParam;
        }
        else if(name.equals("set-lexicon")){ 
            if(null==act.arguments()[0]||null==act.arguments()[0].lexiconValue()||act.arguments().length!=1)
                throw new JTraceScriptingException("load-lexicon argument check failed : requires one parameter (Lexicon)");
            result = setLexicon(act.arguments()[0].lexiconValue());
        }
        else if(name.equals("load-lexicon")){
            if(null==act.arguments()[0])
                throw new JTraceScriptingException("load-lexicon argument check failed : requires one parameter (FileLocator)");
            edu.uconn.psy.jtrace.IO.FileNameFactory fnf = new edu.uconn.psy.jtrace.IO.FileNameFactory(currScript.getRootDirectory());
            java.io.File lexFile = fnf.getReferencedFile(act.arguments()[0].fileLocatorValue());
            if(lexFile==null) return result; 
            edu.uconn.psy.jtrace.IO.WTFileReader fileReader = new edu.uconn.psy.jtrace.IO.WTFileReader(lexFile);
            if (!fileReader.validateLexiconFile()){             
                javax.swing.JOptionPane.showMessageDialog(null, "Invalid lexicon file.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
            // load it into our parameters
            TraceLexicon lexicon = fileReader.loadJTLexicon();
            //System.out.println("loading lexicon of "+lexicon.size()+" words.");
            result = setLexicon(lexicon);            
        }
        else if(name.equals("load-parameters")){ 
            if(null==act.arguments()[0]||null==act.arguments()[0].fileLocatorValue()||act.arguments().length!=1)
                throw new JTraceScriptingException("load-parameters argument check failed : requires one parameter (FileLocator)");
            edu.uconn.psy.jtrace.IO.FileNameFactory fnf = new edu.uconn.psy.jtrace.IO.FileNameFactory(currScript.getRootDirectory());
            java.io.File paramFile = fnf.getReferencedFile(act.arguments()[0].fileLocatorValue());
            if(paramFile==null) return result; 
            edu.uconn.psy.jtrace.IO.WTFileReader fileReader = new edu.uconn.psy.jtrace.IO.WTFileReader(paramFile);
            // validate
            if (!fileReader.validateJT()){             
                javax.swing.JOptionPane.showMessageDialog(null, "Invalid parameters file.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
            // load it into our parameters
            TraceParam newParam = fileReader.loadJTParamFromJT();
            //
            result = new TraceParam(newParam);                        
        }
        else if(name.equals("set-parameters")){ 
            if(null==act.arguments()[0]||null==act.arguments()[0].parametersValue()||act.arguments().length!=1)
                throw new JTraceScriptingException("load-parameters argument check failed : requires one parameter (TraceParam)");
            result = setParameters(act.arguments()[0].parametersValue());
        }
        else if(name.equals("append-to-lexicon")){ 
            if(act.arguments().length!=1)
                throw new JTraceScriptingException("load-parameters argument check failed : requires one parameter");
            TraceLexicon l1 = currParam.getLexicon();
            TraceLexicon l2;
            if(act.arguments()[0] instanceof TraceLexicon){
                l2 = (TraceLexicon)act.arguments()[0];
            }
            else if(act.arguments()[0] instanceof Text){
                l2 = new TraceLexicon();
                Text value = (Text)act.arguments()[0];
                //System.out.println("appending "+value.value()+" to the lexicon.");
                l2.add(new TraceWord(value.value()));                
            }       
            else{ // if(act.arguments()[0] instanceof FileLocator){
                edu.uconn.psy.jtrace.IO.FileNameFactory fnf = new edu.uconn.psy.jtrace.IO.FileNameFactory(currScript.getRootDirectory());
                java.io.File lexFile = fnf.getReferencedFile(act.arguments()[0].fileLocatorValue());
                if(lexFile==null){ 
                    //System.out.println("lexFile==null, returning");
                    return result; 
                }
                edu.uconn.psy.jtrace.IO.WTFileReader fileReader = new edu.uconn.psy.jtrace.IO.WTFileReader(lexFile);
                if (!fileReader.validateLexiconFile()){             
                    if(edu.uconn.psy.jtrace.UI.traceProperties.hasGUI)
                        javax.swing.JOptionPane.showMessageDialog(null, "Invalid lexicon file.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
                    else
                        System.out.println("ParameterActionWorkhorse.applyAction.name=append-to-lexicon : Invalid lexicon file.");
                }
                // load it 
                l2 = fileReader.loadJTLexicon();
            }                 
            l1.append(l2);            
            result = setLexicon(l1);    
            //System.out.println("Appended lexicon size "+l2.size()+", outcome is lexicon size "+l1.size());
        }
        
        return result;
    }
    //DIRECTS PROCESS TOWARDS FILE ACTIONS THAT ALL CREATE PARAMETER FILES, AND RETURN NOTHING.
    public void applyFileAction(edu.uconn.psy.jtrace.Model.Scripting.Action act)throws JTraceScriptingException{                
        String name=act.name();
        /*
        <xsd:element name="save-parameters-to-jt">
        <xsd:element name="save-parameters-to-txt">
        */                                        
        if(name.equals("save-parameters-to-jt")){
            //argument check : if arguments don't match specs, throw an exception
            if(null==act.arguments()[0]||null==act.arguments()[0].fileLocatorValue()||act.arguments().length!=1)
                throw new JTraceScriptingException("save-trace-parameters-to-xml argument check failed : requires one parameter (FileLocator)");            
            writeParametersToXml(currParam,act.arguments()[0].fileLocatorValue()); 
        }
        else if(name.equals("save-parameters-to-txt")){
            //argument check : if arguments don't match specs, throw an exception
            if(null==act.arguments()[0]||null==act.arguments()[0].fileLocatorValue()||act.arguments().length!=1)
                throw new JTraceScriptingException("save-trace-parameters-to-txt argument check failed : requires one parameter (FileLocator)");            
            writeParametersToTxt(currParam,act.arguments()[0].fileLocatorValue());
        }
    }
    
    //IMPLEMENTATION OF PARAMETER OBJECT ACTIONS (WILL RETURN A TRACEPARAM OBJECT)
    public TraceParam setParameters(TraceParam tp){
        return tp;
    }
    public TraceParam setLexicon(TraceLexicon lex){
        currParam.setLexicon(lex);
        return currParam;
    }
    public TraceParam loadParameters(edu.uconn.psy.jtrace.Model.Scripting.FileLocator fl)throws JTraceScriptingException{
        java.io.File path;
        String fileName;
        if(null!=fl.absolutePath()){
            path=new java.io.File(fl.absolutePath());
        }
        else if(null!=fl.relativePath()){
            path=new java.io.File(edu.uconn.psy.jtrace.UI.traceProperties.rootPath,fl.relativePath());
        }
        else{
            path=edu.uconn.psy.jtrace.UI.traceProperties.rootPath;
        }
        if(null!=fl.fileName()){
            fileName=fl.fileName();
        }
        else{
            //can't load with no file name, so throw exception
            throw new JTraceScriptingException("ParameterActionWorkhorse.loadParameters(FileLocator) exception : FileLocator argument contains no fileName value.");            
        }
        java.io.File f=new java.io.File(path,fileName);
        edu.uconn.psy.jtrace.IO.WTFileReader fr = new edu.uconn.psy.jtrace.IO.WTFileReader(f);
        TraceParam result=fr.readWTParameterFile();
        return result;
    }    
    
    //IMPLEMENTATION OF PARAMETER FILE SAVING ACTIONS (ALL WRITE FILES, NONE RETURN ANYTHING)
    public void writeParametersToXml(TraceParam param,FileLocator fl){
        //FILE SAVING SHOULD BE POSSIBLE WITH A RELATIVE PATH, AN ABSOLUTE PATH, A FILE NAME, OR NO FILE NAME.
        edu.uconn.psy.jtrace.IO.FileNameFactory fnf=new edu.uconn.psy.jtrace.IO.FileNameFactory(currParam,currScript.getRootDirectory());
        java.io.File theFile=fnf.makeParameterJtFile(fl);                    
        edu.uconn.psy.jtrace.IO.WTFileWriter fw = new edu.uconn.psy.jtrace.IO.WTFileWriter(theFile,false);
        fw.write(fw.makeWTParameterOpeningTag());
        fw.write(fw.makeWTParameterFileBody(param));
        fw.writeAndClose(fw.makeWTParameterClosingTag());
        fw=null;
    }
    public void writeParametersToTxt(TraceParam tp,FileLocator fl){
        edu.uconn.psy.jtrace.IO.FileNameFactory fnf=new edu.uconn.psy.jtrace.IO.FileNameFactory(currParam,currScript.getRootDirectory());
        java.io.File theFile=fnf.makeParameterTxtFile(fl);                    
        edu.uconn.psy.jtrace.IO.WTFileWriter fw = new edu.uconn.psy.jtrace.IO.WTFileWriter(theFile,false);
        fw.writeAndClose(fw.makeWTParameterFileBodyTXT(tp));
        fw=null;
    }
        
    public TraceParam setOneTraceParameterValue(TraceParam tp ,String name,Primitive value)throws JTraceScriptingException{
        //result = setOneTraceParameterValue(result,act.arguments()[0].textValue().value(),act.arguments()[1]);
        if(name.equalsIgnoreCase("user")) tp.setUser(value.textValue().value());
        else if(name.equalsIgnoreCase("dateTime")) tp.setDateTime(value.textValue().value());
        else if(name.equalsIgnoreCase("comment")) tp.setComment(value.textValue().value());
        else if(name.equalsIgnoreCase("modelInput")) tp.setModelInput(value.textValue().value());
        //(new Double(value.textValue().value())).doubleValue()
        else if(name.equalsIgnoreCase("spread.1")) tp.getSpread()[0]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spread.2")) tp.getSpread()[1]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spread.3")) tp.getSpread()[2]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spread.4")) tp.getSpread()[3]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spread.5")) tp.getSpread()[4]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spread.6")) tp.getSpread()[5]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spread.7")) tp.getSpread()[6]=(new Integer(value.textValue().value())).intValue();
        
        else if(name.equalsIgnoreCase("spreadscale.1")) tp.getSpreadScale()[0]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spreadscale.2")) tp.getSpreadScale()[1]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spreadscale.3")) tp.getSpreadScale()[2]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spreadscale.4")) tp.getSpreadScale()[3]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spreadscale.5")) tp.getSpreadScale()[4]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spreadscale.6")) tp.getSpreadScale()[5]=(new Integer(value.textValue().value())).intValue();
        else if(name.equalsIgnoreCase("spreadscale.7")) tp.getSpreadScale()[6]=(new Integer(value.textValue().value())).intValue();
        
        else if(name.equalsIgnoreCase("min")) tp.setMin((new Double(value.textValue().value())).doubleValue());
        else if(name.equalsIgnoreCase("max")) tp.setMax((new Double(value.textValue().value())).doubleValue());
        else if(name.equalsIgnoreCase("nreps")) tp.setNReps((new Integer(value.textValue().value())).intValue());
        else if(name.equalsIgnoreCase("slicesPerPhon")) tp.setSlicesPerPhon((new Integer(value.textValue().value())).intValue());
        else if(name.equalsIgnoreCase("fslices")) tp.setFSlices((new Integer(value.textValue().value())).intValue());
        else if(name.equalsIgnoreCase("decay.F")) tp.getDecay().F=value.decimalValue().value();
        else if(name.equalsIgnoreCase("decay.P")) tp.getDecay().P=value.decimalValue().value();
        else if(name.equalsIgnoreCase("decay.W")) tp.getDecay().W=value.decimalValue().value();
        else if(name.equalsIgnoreCase("rest.F")) tp.getRest().F=value.decimalValue().value();
        else if(name.equalsIgnoreCase("rest.P")) tp.getRest().P=value.decimalValue().value();
        else if(name.equalsIgnoreCase("rest.W")) tp.getRest().W=value.decimalValue().value();
        else if(name.equalsIgnoreCase("alpha.if")) tp.getAlpha().IF=value.decimalValue().value();
        else if(name.equalsIgnoreCase("alpha.fp")) tp.getAlpha().FP=value.decimalValue().value();
        else if(name.equalsIgnoreCase("alpha.pf")) tp.getAlpha().PF=value.decimalValue().value();
        else if(name.equalsIgnoreCase("alpha.pw")) tp.getAlpha().PW=value.decimalValue().value();
        else if(name.equalsIgnoreCase("alpha.wp")) tp.getAlpha().WP=value.decimalValue().value();
        else if(name.equalsIgnoreCase("gamma.F")) tp.getGamma().F=value.decimalValue().value();
        else if(name.equalsIgnoreCase("gamma.P")) tp.getGamma().P=value.decimalValue().value();
        else if(name.equalsIgnoreCase("gamma.W")) tp.getGamma().W=value.decimalValue().value();
        //else if(name.equalsIgnoreCase("lambda.F")) tp.getLambda().F=(new Double(value.textValue().value())).doubleValue();
        //else if(name.equalsIgnoreCase("lambda.P")) tp.getLambda().P=(new Double(value.textValue().value())).doubleValue();
        //else if(name.equalsIgnoreCase("lambda.W")) tp.getLambda().W=(new Double(value.textValue().value())).doubleValue();
        else if(name.   equalsIgnoreCase("lexicon")) tp.setLexicon(value.lexiconValue());
        else if(name.equalsIgnoreCase("dictionary")) tp.setLexicon(value.lexiconValue());
        else if(name.equalsIgnoreCase("deltaInput")) tp.setDeltaInput((new Integer(value.textValue().value())).intValue());
        else if(name.equalsIgnoreCase("noiseSD")) tp.setNoiseSD((new Double(value.textValue().value())).doubleValue());
        else if(name.equalsIgnoreCase("stochasticitySD")) tp.setStochasticitySD((new Double(value.textValue().value())).doubleValue());
        else if(name.equalsIgnoreCase("continuumSpec")) tp.setContinuumSpec(value.textValue().value());
        else if(name.equalsIgnoreCase("atten")) tp.setAtten((new Double(value.textValue().value())).doubleValue());
        else if(name.equalsIgnoreCase("bias")) tp.setBias((new Double(value.textValue().value())).doubleValue());
        
        else if(name.equalsIgnoreCase("freqNode.RDL_wt")) tp.getFreqNode().RDL_wt=value.booleanValue().value();
        else if(name.equalsIgnoreCase("freqNode.RDL_wt_c")) tp.getFreqNode().RDL_wt_c=(new Double(value.decimalValue().value())).doubleValue();
        else if(name.equalsIgnoreCase("freqNode.RDL_wt_s")) tp.getFreqNode().RDL_wt_s=(new Double(value.decimalValue().value())).doubleValue();
        else if(name.equalsIgnoreCase("freqNode.RDL_rest")) tp.getFreqNode().RDL_rest=value.booleanValue().value();
        else if(name.equalsIgnoreCase("freqNode.RDL_rest_c")) tp.getFreqNode().RDL_rest_c=(new Double(value.decimalValue().value())).doubleValue();
        else if(name.equalsIgnoreCase("freqNode.RDL_rest_s")) tp.getFreqNode().RDL_rest_s=(new Double(value.decimalValue().value())).doubleValue();
        else if(name.equalsIgnoreCase("freqNode.RDL_post")) tp.getFreqNode().RDL_post=value.booleanValue().value();
        else if(name.equalsIgnoreCase("freqNode.RDL_psot_c")) tp.getFreqNode().RDL_post_c=(new Double(value.decimalValue().value())).doubleValue();
                
        else{
            throw new JTraceScriptingException("ParameterActionWorkhorse.setOneTraceParameterValue(TraceParam, String , Primitive) : Unrecognized parameter label.");
        }
        
        return tp;
    }
    public TraceParam tweakOneTraceParameterValue(TraceParam tp ,String name,Primitive value)throws JTraceScriptingException{
        //result = setOneTraceParameterValue(result,act.arguments()[0].textValue().value(),act.arguments()[1]);
        if(name.equalsIgnoreCase("spread.1")) tp.getSpread()[0]+=(int)value.decimalValue().value();
        else if(name.equalsIgnoreCase("spread.2")) tp.getSpread()[1]+=(int)value.decimalValue().value();
        else if(name.equalsIgnoreCase("spread.3")) tp.getSpread()[2]+=(int)value.decimalValue().value();
        else if(name.equalsIgnoreCase("spread.4")) tp.getSpread()[3]+=(int)value.decimalValue().value();
        else if(name.equalsIgnoreCase("spread.5")) tp.getSpread()[4]+=(int)value.decimalValue().value();
        else if(name.equalsIgnoreCase("spread.6")) tp.getSpread()[5]+=(int)value.decimalValue().value();
        else if(name.equalsIgnoreCase("spread.7")) tp.getSpread()[6]+=(int)value.decimalValue().value();
        
        else if(name.equalsIgnoreCase("spreadscale.1")) tp.getSpreadScale()[0]+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("spreadscale.2")) tp.getSpreadScale()[1]+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("spreadscale.3")) tp.getSpreadScale()[2]+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("spreadscale.4")) tp.getSpreadScale()[3]+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("spreadscale.5")) tp.getSpreadScale()[4]+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("spreadscale.6")) tp.getSpreadScale()[5]+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("spreadscale.7")) tp.getSpreadScale()[6]+=value.decimalValue().value();        
        
        else if(name.equalsIgnoreCase("min")) tp.setMin(tp.getMin()+value.decimalValue().value());
        else if(name.equalsIgnoreCase("max")) tp.setMax(tp.getMax()+value.decimalValue().value());
        else if(name.equalsIgnoreCase("nreps")) tp.setNReps(tp.getNReps()+(int)value.decimalValue().value());
        else if(name.equalsIgnoreCase("slicesPerPhon")) tp.setSlicesPerPhon(tp.getSlicesPerPhon()+(int)value.decimalValue().value());
        else if(name.equalsIgnoreCase("fslices")) tp.setFSlices(tp.getFSlices()+(int)value.decimalValue().value());
        else if(name.equalsIgnoreCase("decay.F")) tp.getDecay().F+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("decay.P")) tp.getDecay().P+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("decay.W")) tp.getDecay().W+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("rest.F")) tp.getRest().F+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("rest.P")) tp.getRest().P+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("rest.W")) tp.getRest().W+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("alpha.if")) tp.getAlpha().IF+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("alpha.fp")) tp.getAlpha().FP+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("alpha.pf")) tp.getAlpha().PF+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("alpha.pw")) tp.getAlpha().PW+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("alpha.wp")) tp.getAlpha().WP+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("gamma.F")) tp.getGamma().F+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("gamma.P")) tp.getGamma().P+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("gamma.W")) tp.getGamma().W+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("deltaInput")) tp.setDeltaInput(tp.getDeltaInput()+(int)value.decimalValue().value());
        else if(name.equalsIgnoreCase("noiseSD")) tp.setNoiseSD(tp.getNoiseSD()+value.decimalValue().value());
        else if(name.equalsIgnoreCase("stochasticitySD")) tp.setStochasticitySD(tp.getStochasticitySD()+value.decimalValue().value());        
        else if(name.equalsIgnoreCase("atten")) tp.setAtten(tp.getAtten()+value.decimalValue().value());        
        else if(name.equalsIgnoreCase("bias")) tp.setBias(tp.getBias()+value.decimalValue().value());        
        else if(name.equalsIgnoreCase("freqNode.RDL_wt_c")) tp.getFreqNode().RDL_wt_c+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("freqNode.RDL_wt_s")) tp.getFreqNode().RDL_wt_s+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("freqNode.RDL_rest_c")) tp.getFreqNode().RDL_rest_c+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("freqNode.RDL_rest_s")) tp.getFreqNode().RDL_rest_s+=value.decimalValue().value();
        else if(name.equalsIgnoreCase("freqNode.RDL_psot_c")) tp.getFreqNode().RDL_post_c+=value.decimalValue().value();
                
        else{
            throw new JTraceScriptingException("ParameterActionWorkhorse.setOneTraceParameterValue(TraceParam, String , Primitive) : Unrecognized parameter label.");
        }
        
        return tp;
    }
}

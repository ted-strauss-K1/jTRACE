/*
 * QueryWorkhorse.java
 *
 * Created on May 5, 2005, 7:06 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import edu.uconn.psy.jtrace.Model.*;

/**
 *
 * @author tedstrauss
 */
public class QueryWorkhorse extends Workhorse{
        
    /** Creates a new instance of QueryWorkhorse */
    public QueryWorkhorse(TraceParam p, TraceSim s, TraceSimAnalysis g,edu.uconn.psy.jtrace.UI.GraphParameters gp,TraceScript scr){
        super(p,s,g,gp,scr);
    }
    public Primitive evaluateQuery(Query qry)throws JTraceScriptingException{
        String name=qry.name();
        String returnType=qry.returnType();
        Primitive[] args=qry.arguments();
        args=currScript.replaceQueriesWithValues(args);
        
        
        /* From jTRACESchema.xsd
        <xsd:element name="item-with-highest-peak">
        <xsd:element name="value-of-highest-peak">
        <xsd:element name="item-with-nth-highest-peak">
        <xsd:element name="value-of-nth-highest-peak">
        <xsd:element name="nth-item-in-lexicon">
        <xsd:element name="current-input">
        <xsd:element name="peak-value-of-item">
        <xsd:element name="cycle-when-item-peaks">
        <xsd:element name="cycle-when-item-exceeds-threshold">
        <xsd:element name="nth-item-to-exceed-threshold">
        <xsd:element name="McClellend-1991-running-average-metric">
                        
         */
        
        //QUERIES FOR ACCESSING PARAMETERS, LEXICON
        if(name.equals("current-input")){
            if(args.length!=0)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'model-input-value\' : takes no argument");
            return new Text(currParam.getModelInput());    
            //return new Text(currSim.getInputString());    
        } 
        else if(name.equals("fetch-current-value-of-a-parameter")){
            if(args.length!=1)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'fetch-current-value-of-a-parameter\' : requires one argument (Text)");
            return new Text(fetchValueOfParam(args[0].textValue().value()));
        }        
        else if(name.equals("nth-item-in-lexicon")){
            if(qry.arguments().length!=1)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'nth-item-in-lexicon\' : requires one argument");
            try{
                int value;
                if(qry.arguments()[0] instanceof Text){
                    value = (new Double(qry.arguments()[0].textValue().value())).intValue();
                }
                else if(qry.arguments()[0] instanceof Int){
                    value = qry.arguments()[0].intValue().value();
                }
                else if(qry.arguments()[0] instanceof Query){
                    Primitive intermediate = currScript.interpretQuery(qry.arguments()[0].queryValue());
                    if(intermediate==null||intermediate.intValue()==null) value=1;
                    else value = intermediate.intValue().value();  
                }
                else{
                    value=1;
                }
                if(value<=0) return new Text(currParam.getLexicon().get(0).getPhon());    
                return new Text(currParam.getLexicon().get(value-1).getPhon());    
            }
            catch(Exception e){
                e.printStackTrace();
                return new Text("-");
            }
        }
        else if(name.equals("size-of-lexicon")){
            if(args.length!=0)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'size-of-lexicon\' : requires no argument");            
            if(currParam.getLexicon()==null) return new Int(0);
            return new Int(currParam.getLexicon().size());
        }
        else if(name.equals("query-one-simulation-cell")){
            if(args.length!=4)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'query-one-simulation-cell\' : requires 4 arguments");            
            currSim = new TraceSim(currParam);
            runsim(currSim);                        
            double result;
            int idx;
            if(args[0].textValue().value().equals("word")){
                //find the index of the queried word
                String wrd;
                if(args[1] instanceof Query){
                    Primitive intermediate = currScript.interpretQuery(args[1].queryValue());
                    if(intermediate==null||intermediate.intValue()==null) wrd="-";
                    else wrd = intermediate.textValue().value();
                }
                else{
                    wrd=args[1].textValue().value();
                }
                idx=currParam.getLexicon().indexOf(wrd);
                if(null==currSim.getWordD())
                    result=-1;
                else
                    result=currSim.getWordD()[args[3].intValue().value()][idx][args[2].intValue().value()];
            }
            else if(args[0].textValue().value().equals("phoneme")){
                //find the index of the queried phoneme
                char phn = args[1].textValue().value().charAt(0);
                idx=currParam.getPhonology().mapPhon(phn);
                if(null==currSim.getPhonemeD())
                    result=-1;
                else
                 result=currSim.getPhonemeD()[args[3].intValue().value()][idx][args[2].intValue().value()];
            }
            else if(args[0].textValue().value().equals("feature")){
                //find the index of the queried feature
                idx=currParam.getPhonology().mapFeat(args[1].textValue().value());
                if(null==currSim.getFeatureD())
                    result=-1;
                else
                 result=currSim.getFeatureD()[args[3].intValue().value()][idx][args[2].intValue().value()];
            }
            else{ // if(args[0].textValue().value().equals("input")){
                //find the index of the queried feature
                idx=currParam.getPhonology().mapFeat(args[1].textValue().value());                
                if(null==currSim.getInputD())
                    result=-1;
                else
                 result=currSim.getInputD()[args[3].intValue().value()][idx][args[2].intValue().value()];
            }
            return new Decimal(result);
        }
        /*else if(name.equals("item-is-in-lexicon")){
            if(args.length!=1||null==args[0].textValue())
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'item-is-in-lexicon\' : requires one argument (Text)");
            return new edu.uconn.psy.jtrace.Model.Scripting.Boolean(currParam.getLexicon().toString()==new String("xc"));  //TODO    
        }*/        
        //QUERIES FOR ACCESSING DETAILS ABOUT GRAPH ANALYSES: DECISION RULES
        else if(name.equals("decision-rule-report")){
            if(args.length!=3)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'decision-rule-report\' : takes 3 arguments (Decimal, Text, Int)");
            currSim = new TraceSim(currParam);
            runsim(currSim);            
            TraceDecisionRule decision = new TraceDecisionRule(currAnalysis.doAnalysis(currSim));
            thread=null;
            Primitive value = qry.arguments()[1];
            if(value.queryValue()!=null)
                value = currScript.interpretQuery(value.queryValue());            
            String report = decision.decisionRuleReport(args[0].decimalValue().value(),value.textValue().value());
            return new Text(report);
        }
        
        else if(name.equals("item-with-highest-peak")){
            if(args.length!=0)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'nth-item-in-lexicon\' : takes no argument");
            currSim = new TraceSim(currParam);
            runsim(currSim);            
            //currSim.cycle(currScript.getCyclesPerSim());            
            TraceDecisionRule decision = new TraceDecisionRule(currAnalysis.doAnalysis(currSim));
            thread=null;
            return new Text(decision.itemWithHighestPeak());
        }
        else if(name.equals("value-of-highest-peak")){
            if(args.length!=0)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'nth-item-in-lexicon\' : takes no argument");
            currSim = new TraceSim(currParam);
            runsim(currSim);            
            //currSim.cycle(currScript.getCyclesPerSim());            
            TraceDecisionRule decision = new TraceDecisionRule(currAnalysis.doAnalysis(currSim));
            thread=null;
            return new Decimal(decision.highestPeak());
        }
        else if(name.equals("cycle-when-item-exceeds-threshold")){
            if(args.length!=2)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'cycle-when-item-exceeds-thresh\' : takes two arguments (Text/Query)");
            //System.out.println("query."+name+" thresh="+args[1].decimalValue().value());
            currSim = new TraceSim(currParam);
            runsim(currSim);            
            //currSim.cycle(currScript.getCyclesPerSim());            
            TraceDecisionRule decision = new TraceDecisionRule(currAnalysis.doAnalysis(currSim));
            thread=null;
            Primitive value = qry.arguments()[0];
            if(null != value.queryValue())
                value = currScript.interpretQuery(value.queryValue());            
            return new Int(decision.rtForItem(value.textValue().value(),args[1].decimalValue().value()));
        }
        else if(name.equals("cycle-when-item-peaks")){
            if(args.length!=1)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'cycle-when-item-peaks\' : takes two arguments (Text/Query)");
            currSim = new TraceSim(currParam);
            runsim(currSim);            
            //currSim.cycle(currScript.getCyclesPerSim());            
            TraceDecisionRule decision = new TraceDecisionRule(currAnalysis.doAnalysis(currSim));
            thread=null;
            Primitive value = qry.arguments()[0];
            if(null != value.queryValue())
                value = currScript.interpretQuery(value.queryValue());            
            return new Int(decision.peakRtForItem(value.textValue().value()));
        }
        else if(name.equals("nth-item-to-exceed-threshold")){
            if(args.length!=2)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'nth-item-to-exceed-given-thresh\' : takes one argument (Int)");
            currSim = new TraceSim(currParam);
            runsim(currSim);            
            //currSim.cycle(currScript.getCyclesPerSim());            
            TraceDecisionRule decision = new TraceDecisionRule(currAnalysis.doAnalysis(currSim));
            thread=null;
            return new Text(decision.nthItemToThreshold(args[0].intValue().value(),args[1].decimalValue().value()));
        }
        else if(name.equals("peak-value-of-item")){
            if(args.length!=1)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'peak-value-of-lexical-item\' : takes one argument (Text)");
            currSim = new TraceSim(currParam);
            runsim(currSim);            
            //currSim.cycle(currScript.getCyclesPerSim());            
            TraceDecisionRule decision = new TraceDecisionRule(currAnalysis.doAnalysis(currSim));
            thread=null;
            Primitive value = qry.arguments()[0];
            if(null != value.queryValue())
                value = currScript.interpretQuery(value.queryValue());            
            return new Decimal(decision.peakValueOfItem(value.textValue().value()));
        }
        else if(name.equals("item-with-nth-highest-peak")){
            if(args.length!=1)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'item-with-nth-highest-peak\' : takes one argument (Int)");
            currSim = new TraceSim(currParam);
            runsim(currSim);            
            //currSim.cycle(currScript.getCyclesPerSim());            
            TraceDecisionRule decision = new TraceDecisionRule(currAnalysis.doAnalysis(currSim));
            thread=null;
            return new Text(decision.nthHighestPeakItem(args[0].intValue().value()));
        }
        else if(name.equals("value-of-nth-highest-peak")){
            if(args.length!=1)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'value-of-nth-highest-peak\' : takes one argument (Int)");
            currSim = new TraceSim(currParam);
            runsim(currSim);            
            //currSim.cycle(currScript.getCyclesPerSim());            
            TraceDecisionRule decision = new TraceDecisionRule(currAnalysis.doAnalysis(currSim));
            thread=null;
            return new Decimal(decision.nthHighestPeakValue(args[0].intValue().value()));
        }
        else if(name.equals("McClellend-1991-running-average-metric")){
            if(args.length!=5)
                throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : arguments incorrect for query \'McClellend-1991-running-average-metric\' : takes four arguments (String, String, Int, Decimal, Decimal)");
            currSim = new TraceSim(currParam);
            runsim(currSim);            
            //currSim.cycle(currScript.getCyclesPerSim());           
            //set phoneme activation settings for the currAnalysis:
            currAnalysis.setAlignment(args[2].intValue().value());
            currAnalysis.setContentType(currAnalysis.ACTIVATIONS);
            currAnalysis.setDomain(currAnalysis.PHONEMES);
            currAnalysis.setWatchType(currAnalysis.WATCHSPECIFIED);
            currAnalysis.setItemsToWatch(currParam.getPhonology().getLabels());
            //did i forget something?
            TraceDecisionRule decision = new TraceDecisionRule(currAnalysis.doAnalysis(currSim));
            thread=null;
            return new Text(decision.runningAverageMetricReport(args[0].textValue().value(),args[1].textValue().value(),currSim.getInputString(),args[3].decimalValue().value(),args[4].decimalValue().value(),currParam.getPhonology().getLabels()));
        }
        else{
            throw new JTraceScriptingException("QueryWorkhorse.evaluateQuery() : unrecognized query name.");
        }
    }  
    
    public String fetchValueOfParam(String name){
        //System.out.println("fetching value of parameter "+name);
        if(name.equalsIgnoreCase("user")) return currParam.getUser();
        else if(name.equalsIgnoreCase("dateTime")) return currParam.getDateTime();
        else if(name.equalsIgnoreCase("comment")) return currParam.getComment();
        else if(name.equalsIgnoreCase("modelInput")) return currParam.getModelInput();
        
        else if(name.equalsIgnoreCase("spread.1")) return (new Int(currParam.getSpread()[0])).toString();
        else if(name.equalsIgnoreCase("spread.2")) return (new Int(currParam.getSpread()[1])).toString();
        else if(name.equalsIgnoreCase("spread.3")) return (new Int(currParam.getSpread()[2])).toString();
        else if(name.equalsIgnoreCase("spread.4")) return (new Int(currParam.getSpread()[3])).toString();
        else if(name.equalsIgnoreCase("spread.5")) return (new Int(currParam.getSpread()[4])).toString();
        else if(name.equalsIgnoreCase("spread.6")) return (new Int(currParam.getSpread()[5])).toString();
        else if(name.equalsIgnoreCase("spread.7")) return (new Int(currParam.getSpread()[6])).toString();
        
        else if(name.equalsIgnoreCase("spreadscale.1")) return (new Double(currParam.getSpreadScale()[0])).toString();
        else if(name.equalsIgnoreCase("spreadscale.2")) return (new Double(currParam.getSpreadScale()[1])).toString();
        else if(name.equalsIgnoreCase("spreadscale.3")) return (new Double(currParam.getSpreadScale()[2])).toString();
        else if(name.equalsIgnoreCase("spreadscale.4")) return (new Double(currParam.getSpreadScale()[3])).toString();
        else if(name.equalsIgnoreCase("spreadscale.5")) return (new Double(currParam.getSpreadScale()[4])).toString();
        else if(name.equalsIgnoreCase("spreadscale.6")) return (new Double(currParam.getSpreadScale()[5])).toString();
        else if(name.equalsIgnoreCase("spreadscale.7")) return (new Double(currParam.getSpreadScale()[6])).toString();
        else if(name.equalsIgnoreCase("atten")) return (new Double(currParam.getAtten())).toString();
        else if(name.equalsIgnoreCase("bias")) return (new Double(currParam.getBias())).toString();
        
        else if(name.equalsIgnoreCase("min")) return new String(currParam.getMin()+"");
        else if(name.equalsIgnoreCase("max")) return new String(currParam.getMax()+"");
        else if(name.equalsIgnoreCase("nreps")) return new String(currParam.getNReps()+"");
        else if(name.equalsIgnoreCase("slicesPerPhon")) return new String(currParam.getSlicesPerPhon()+"");
        else if(name.equalsIgnoreCase("fslices")) return new String(currParam.getFSlices()+"");
        else if(name.equalsIgnoreCase("decay.F")) return new String(currParam.getDecay().F+"");
        else if(name.equalsIgnoreCase("decay.P")) return new String(currParam.getDecay().P+"");
        else if(name.equalsIgnoreCase("decay.W")) return new String(currParam.getDecay().W+"");
        else if(name.equalsIgnoreCase("rest.F")) return new String(currParam.getRest().F+"");
        else if(name.equalsIgnoreCase("rest.P")) return new String(currParam.getRest().P+"");
        else if(name.equalsIgnoreCase("rest.W")) return new String(currParam.getRest().W+"");
        else if(name.equalsIgnoreCase("alpha.if")) return new String(currParam.getAlpha().IF+"");
        else if(name.equalsIgnoreCase("alpha.fp")) return new String(currParam.getAlpha().FP+"");
        else if(name.equalsIgnoreCase("alpha.pf")) return new String(currParam.getAlpha().PF+"");
        else if(name.equalsIgnoreCase("alpha.pw")) return new String(currParam.getAlpha().PW+"");
        else if(name.equalsIgnoreCase("alpha.wp")) return new String(currParam.getAlpha().WP+"");
        else if(name.equalsIgnoreCase("gamma.F")) return new String(currParam.getGamma().F+"");
        else if(name.equalsIgnoreCase("gamma.P")) return new String(currParam.getGamma().P+"");
        else if(name.equalsIgnoreCase("gamma.W")) return new String(currParam.getGamma().W+"");
        //else if(name.equalsIgnoreCase("lambda.F")) return new String(currParam.getLambda().F+"");
        //else if(name.equalsIgnoreCase("lambda.P")) return new String(currParam.getLambda().P+"");
        //else if(name.equalsIgnoreCase("lambda.w")) return new String(currParam.getLambda().W+"");
        else if(name.equalsIgnoreCase("deltaInput")) return new String(currParam.getDeltaInput()+"");
        else if(name.equalsIgnoreCase("noiseSD")) return new String(currParam.getNoiseSD()+"");
        else if(name.equalsIgnoreCase("stochasticitySD")) return new String(currParam.getStochasticitySD()+"");
        else if(name.equalsIgnoreCase("continuumSpec")) return new String(currParam.getContinuumSpec());
        
        else if(name.equalsIgnoreCase("freqNode.RDL_wt")) return new String(currParam.getFreqNode().RDL_wt+"");
        else if(name.equalsIgnoreCase("freqNode.RDL_wt_c")) return new String(currParam.getFreqNode().RDL_wt_c+"");
        else if(name.equalsIgnoreCase("freqNode.RDL_wt_s")) return new String(currParam.getFreqNode().RDL_wt_s+"");
        else if(name.equalsIgnoreCase("freqNode.RDL_rest")) return new String(currParam.getFreqNode().RDL_rest+"");
        else if(name.equalsIgnoreCase("freqNode.RDL_rest_c")) return new String(currParam.getFreqNode().RDL_rest_c+"");
        else if(name.equalsIgnoreCase("freqNode.RDL_rest_s")) return new String(currParam.getFreqNode().RDL_rest_s+"");
        else if(name.equalsIgnoreCase("freqNode.RDL_post")) return new String(currParam.getFreqNode().RDL_post+"");
        else if(name.equalsIgnoreCase("freqNode.RDL_psot_c")) return new String(currParam.getFreqNode().RDL_post_c+"");
        
        else if(name.equalsIgnoreCase("lexicon")) return new String(currParam.getLexicon().toString());
        else if(name.equalsIgnoreCase("dictionary")) return new String(currParam.getLexicon().toString());
        
        return "";        
    }
    
}

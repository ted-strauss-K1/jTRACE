/*
 * SimActionWorkhorse.java
 *
 * Created on May 4, 2005, 2:54 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;
import edu.uconn.psy.jtrace.Model.Scripting.*;
import org.jfree.chart.*;
import org.jfree.data.*;
import org.jfree.data.xy.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.StandardLegend;
import org.jfree.chart.title.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.annotations.*;
/**
 *
 * @author tedstrauss
 */
public class GraphActionWorkhorse extends Workhorse{
    
    /** Creates a new instance of SimActionWorkhorse */
    public GraphActionWorkhorse(edu.uconn.psy.jtrace.Model.TraceParam p, edu.uconn.psy.jtrace.Model.TraceSim s, edu.uconn.psy.jtrace.Model.TraceSimAnalysis g,edu.uconn.psy.jtrace.UI.GraphParameters gp,edu.uconn.psy.jtrace.Model.Scripting.TraceScript scr){
        super(p,s,g,gp,scr);
    }
    public edu.uconn.psy.jtrace.Model.TraceSimAnalysis applyAnalysisAction(edu.uconn.psy.jtrace.Model.Scripting.Action act)throws JTraceScriptingException{        
        //modify GraphParameter settings
        //execute analysis operations
        edu.uconn.psy.jtrace.Model.TraceSimAnalysis result=new edu.uconn.psy.jtrace.Model.TraceSimAnalysis(currAnalysis);
        String name=act.name();
        /*
        <xsd:element name="reset-graph-defaults">
        <xsd:element name="set-graph-domain">
        <xsd:element name="set-watch-type">
        <xsd:element name="set-watch-top-n">
        <xsd:element name="set-watch-items">
        <xsd:element name="set-analysis-type">
        <xsd:element name="set-choice-type">
        <xsd:element name="set-graph-content-type">
        <xsd:element name="set-k-value">
        <xsd:element name="set-alignment">
        <xsd:element name="add-one-analysis-item">
        <xsd:element name="remove-one-analysis-item">
         */
        
        if(name.equals("reset-graph-defaults")){
            if(act.arguments().length!=0)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'reset-analysis-default\'; takes no argument");
            result.reset();
        }
        else if(name.equals("analysis-settings")){
            if(act.arguments().length<0)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'analysis-settings\'; ");
            if(act.arguments()[0].textValue().value().equals("WORDS")){
                result.setDomain(result.WORDS);            
            }
            else{
                result.setDomain(result.PHONEMES);            
            }
            if(act.arguments()[1].textValue().value().equals("ACTIVATIONS")){
                result.setContentType(result.ACTIVATIONS);
            
            }
            else if(act.arguments()[1].textValue().value().equals("RESPONSE-PROBABILITIES")){
                result.setContentType(result.RESPONSE_PROBABILITIES);
                if(act.arguments()[2].textValue().value().equals("NORMAL")){
                    result.setChoice(result.NORMAL);                
                }
                else{
                    result.setChoice(result.FORCED);                
                } 
                result.setKValue(act.arguments()[3].intValue().value());
            }
            else{ //if(act.arguments()[2].textValue().value().equals("COMPETITION")){
                result.setContentType(result.COMPETITION_INDEX);
                //System.out.println("processing="+act.arguments()[2].textValue().value()+"\twidth="+act.arguments()[3].intValue().value());
                if(act.arguments()[2].textValue().value().equals("RAW")){
                    result.setCompetIndexType(result.RAW);
                }
                else if(act.arguments()[2].textValue().value().equals("FIRST-DERIVATIVE")){
                    result.setCompetIndexType(result.FIRST_DERIVATIVE);
                }
                else { //if(act.arguments()[2].textValue().value().equals("SECOND-DERIVATIVE")){
                    result.setCompetIndexType(result.SECOND_DERIVATIVE);
                }    
                result.setCompetIndexSlope(act.arguments()[3].intValue().value());
                //System.out.println("set competition graph to... "+result.getCompetIndexType()+"\t"+result.getCompetIndexSlope());
            }
            
            if(act.arguments()[4].textValue().value().equals("WATCH-TOP-N")){
                result.setWatchType(result.WATCHTOPN);
                result.setTopN(act.arguments()[6].intValue().value());
            }
            else{
                result.setWatchType(result.WATCHSPECIFIED);                
                java.util.LinkedList ll = act.arguments()[5].listValue().value();
                String[] watchItems = new String[ll.size()];
                for(int i=0;i<ll.size();i++){
                    if(!(ll.get(i) instanceof edu.uconn.psy.jtrace.Model.Scripting.Text)) throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : watch items must resolve to type Text.");
                    watchItems[i] = ((edu.uconn.psy.jtrace.Model.Scripting.Text)ll.get(i)).value();
                }            
                result.setItemsToWatch(watchItems);
            }
            if(act.arguments()[7].textValue().value().equals("AVERAGE")){
                result.setCalculationType(result.AVERAGE);
            }
            else if(act.arguments()[7].textValue().value().equals("SPECIFIED")){
                result.setCalculationType(result.STATIC);
                result.setAlignment(act.arguments()[8].intValue().value());
            }
            else if(act.arguments()[7].textValue().value().equals("FRAUENFELDER")){
                result.setCalculationType(result.FRAUENFELDER);
                result.setAlignment(act.arguments()[8].intValue().value());
            }
            else if(act.arguments()[7].textValue().value().equals("MAX-ADHOC")){
                result.setCalculationType(result.MAX_ADHOC);
            }
            else if(act.arguments()[7].textValue().value().equals("MAX-ADHOC-2")){
                result.setCalculationType(result.MAX_ADHOC_2);
            }
            else if(act.arguments()[7].textValue().value().equals("MAX-POSTHOC")){
                result.setCalculationType(result.MAX_POSTHOC);
            }                       
        }
        else if(name.equals("set-content-type")){
            if(act.arguments().length!=1)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'graph-activations\'; takes one argument (Text)");
            if(act.arguments()[0].textValue().value().equals("ACTIVATIONS"))
                result.setContentType(currAnalysis.ACTIVATIONS);
            else if(act.arguments()[0].textValue().value().equals("RESPONSE-PROBABILITIES"))
                result.setContentType(currAnalysis.RESPONSE_PROBABILITIES);
            else if(act.arguments()[0].textValue().value().equals("COMPETITION"))
                result.setContentType(currAnalysis.COMPETITION_INDEX);
        }
        else if(name.equals("set-graph-domain")){
            if(act.arguments().length!=1)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-analysis-domain-words\'; takes one argument (Text)");
            if(act.arguments()[0].textValue().value().equals("WORDS"))
                result.setDomain(currAnalysis.WORDS);
            else if(act.arguments()[0].textValue().value().equals("PHONEMES"))
                result.setDomain(currAnalysis.PHONEMES);
        }
        else if(name.equals("set-analysis-type")){
            if(act.arguments().length!=1)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-analysis-normal-attention\'; takes one argument (Text)");
            if(act.arguments()[0].textValue().value().equals("SPECIFIED"))
                result.setCalculationType(currAnalysis.STATIC);
            else if(act.arguments()[0].textValue().value().equals("AVERAGE"))
                result.setCalculationType(currAnalysis.AVERAGE);
            else if(act.arguments()[0].textValue().value().equals("FRAUNFELDER"))
                result.setCalculationType(currAnalysis.FRAUENFELDER);
            else if(act.arguments()[0].textValue().value().equals("MAX-ADHOC"))
                result.setCalculationType(currAnalysis.MAX_ADHOC);
            else if(act.arguments()[0].textValue().value().equals("MAX-ADHOC-2"))
                result.setCalculationType(currAnalysis.MAX_ADHOC_2);
            else if(act.arguments()[0].textValue().value().equals("MAX-POSTHOC"))
                result.setCalculationType(currAnalysis.MAX_POSTHOC);
        }
        else if(name.equals("set-choice-type")){
            if(act.arguments().length!=1)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-analysis-forced-attention\'; takes one argument (Text)");
            if(act.arguments()[0].textValue().value().equals("NORMAL"))
                result.setChoice(currAnalysis.NORMAL);
            else if(act.arguments()[0].textValue().value().equals("FORCED"))
                result.setChoice(currAnalysis.FORCED);
        }
        else if(name.equals("set-alignment")){
            if(act.arguments().length!=1||null==act.arguments()[0].intValue())
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-alignment\'; takes 1 argument (Int)");
            result.setAlignment(act.arguments()[0].intValue().value());
        }
        else if(name.equals("set-k-value")){
            if(act.arguments().length!=1||null==act.arguments()[0].intValue())
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-analysis-k-value\'; takes 1 argument (Int)");
            result.setKValue(act.arguments()[0].intValue().value());
        }
        else if(name.equals("set-watch-type")){
            if(act.arguments().length!=1||null==act.arguments()[0].textValue())
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-watch-type\'; takes 1 argument (Text)");
            if(act.arguments()[0].textValue().value().equals("WATCH-SPECIFIED"))
                result.setWatchType(currAnalysis.WATCHSPECIFIED);            
            else if(act.arguments()[0].textValue().value().equals("WATCH-TOP-N"))
                result.setWatchType(currAnalysis.WATCHTOPN);                        
        }
        else if(name.equals("set-watch-top-n")){
            if(act.arguments().length!=1||null==act.arguments()[0].intValue())
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-analysis-watch-top-n-items\'; takes 1 argument (Int)");
            result.setTopN(act.arguments()[0].intValue().value());
        }
        else if(name.equals("set-watch-items")){
            if(act.arguments().length!=1)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-analysis-watch-listed-items\'; takes 1 argument (Lexicon)");
            java.util.LinkedList ll=act.arguments()[0].listValue().value();
            String[] watchItems = new String[ll.size()];
            for(int i=0;i<ll.size();i++){
                if(!(ll.get(i) instanceof edu.uconn.psy.jtrace.Model.Scripting.Text)) throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : watch items must resolve to type Text.");
                watchItems[i] = ((edu.uconn.psy.jtrace.Model.Scripting.Text)ll.get(i)).value();
            }
            result.setItemsToWatch(watchItems);
        }
        else if(name.equals("add-one-analysis-item")){
            if(act.arguments().length!=1||null==act.arguments()[0].textValue())
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'add-one-watched-item\'; takes 1 argument (Text)");
            result.addOneWatchedItem(act.arguments()[0].textValue().value());
        }
        else if(name.equals("remove-one-analysis-item")){
            if(act.arguments().length!=1||null==act.arguments()[0].textValue())
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'remove-one-watched-item\'; takes 1 argument (Text)");
            result.removeOneWatchedItem(act.arguments()[0].textValue().value());
        }
        else if(name.equals("average-all-analyses-in-current-iteration-and-save-graph")){
            if(act.arguments().length!=2||null==act.arguments()[0].listValue())
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'\'; takes 2 argument (List, FileLocator)");            
            currSim = new edu.uconn.psy.jtrace.Model.TraceSim(currParam);
            runsim(currSim);            
            result.averageAnalysisStep(currSim,act.arguments()[0].listValue().value());
            thread=null;
        }
        else if(name.equals("set-comp-calc-type")){
            if(act.arguments().length!=1||null==act.arguments()[0].textValue())
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-comp-calc-type\'; takes 1 argument (Text)");
            if(act.arguments()[0].textValue().value().equals("RAW")){
                result.setCompetIndexType(result.RAW);
            }
            else if(act.arguments()[0].textValue().value().equals("FIRST-DERIVATIVE")){
                result.setCompetIndexType(result.FIRST_DERIVATIVE);
            }
            else if(act.arguments()[0].textValue().value().equals("SECOND-DERIVATIVE")){
                result.setCompetIndexType(result.SECOND_DERIVATIVE);
            }            
        }
        else if(name.equals("set-comp-slope-width")){
            if(act.arguments().length!=1||null==act.arguments()[0].textValue())
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-comp-slope-width\'; takes 1 argument (Int)");
            result.setCompetIndexSlope(act.arguments()[0].intValue().value());            
        }
        return result;        
    }
    public edu.uconn.psy.jtrace.UI.GraphParameters applyDisplayAction(edu.uconn.psy.jtrace.Model.Scripting.Action act)throws JTraceScriptingException{        
        
        edu.uconn.psy.jtrace.UI.GraphParameters result=new edu.uconn.psy.jtrace.UI.GraphParameters(currGraphParameters);
        String name=act.name();
        /*
        <xsd:element name="set-graph-x-axis-bounds">
        <xsd:element name="set-graph-y-axis-bounds">
        <xsd:element name="set-graph-title">
        <xsd:element name="set-graph-x-axis-label">
        <xsd:element name="set-graph-y-axis-label">
        <xsd:element name="set-graph-input-position">
        */
        if(name.equals("reset-display-defaults")){
            if(act.arguments().length!=0)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'reset-display-default\'; takes no argument");
            currGraphParameters.reset();
        }
        else if(name.equals("set-graph-title")){
            if(act.arguments().length!=1)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-graph-title\'; takes 1 argument (Text)");            
            Text value = (Text)act.arguments()[0];            
            String contents="";
            contents += value.value();            
            result.setGraphTitle(contents);
        }
        else if(name.equals("set-graph-x-axis-bounds")){
            if(act.arguments().length!=2)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-x-axis-bounds\'; takes 2 argument (Decimal, Decimal)");
            result.setXRange(act.arguments()[0].decimalValue().value(), act.arguments()[1].decimalValue().value());
        }
        else if(name.equals("set-graph-y-axis-bounds")){
            if(act.arguments().length!=2)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-y-axis-bounds\'; takes 2 argument (Decimal, Decimal)");
            result.setYRange(act.arguments()[0].decimalValue().value(), act.arguments()[1].decimalValue().value());
        }
        else if(name.equals("set-graph-x-axis-label")){
            if(act.arguments().length!=1)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-x-axis-label\'; takes 1 argument (Text)");
            result.setXLabel(act.arguments()[0].textValue().value());
        }
        else if(name.equals("set-graph-y-axis-label")){
            if(act.arguments().length!=1)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-y-axis-label\'; takes 1 argument (Text)");
            result.setYLabel(act.arguments()[0].textValue().value());
        }
        else if(name.equals("set-graph-input-position")){
            if(act.arguments().length!=1)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'set-y-axis-label\'; takes 1 argument (Text)");
            result.setInputPosition(act.arguments()[0].intValue().value());
        }
        return result;        
    }
    public void applyFileAction(edu.uconn.psy.jtrace.Model.Scripting.Action act)throws JTraceScriptingException{        
        //save graph as PNG, save graph as CSV
        /*
        <xsd:element name="save-graph-to-png">
        <xsd:element name="save-graph-to-txt">        
        */
        String name = act.name();
        if(name.equals("save-graph-to-png")){
            if(act.arguments().length!=1)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyFileAction() : incorrect arguments for action \'save-graph-to-png\'; takes 1 argument (FileLocator)");            
            try
            {   
                edu.uconn.psy.jtrace.IO.FileNameFactory fnf =  new edu.uconn.psy.jtrace.IO.FileNameFactory(currScript.getRootDirectory());            
                java.io.File saveFile = fnf.makeGraphPngFile((edu.uconn.psy.jtrace.Model.Scripting.FileLocator)act.arguments()[0]);
                if(!saveFile.getParentFile().exists())
                    saveFile.getParentFile().mkdirs();
                if(!saveFile.exists())
                    saveFile.createNewFile();
                currSim = new edu.uconn.psy.jtrace.Model.TraceSim(currParam);
                runsim(currSim);            
                XYSeriesCollection dataset = currAnalysis.doAnalysis(currSim);
                thread=null;
                // create the graph 
                JFreeChart graph = edu.uconn.psy.jtrace.UI.GraphPanel.createJTRACEChart(dataset, currGraphParameters);
                
                //annotate : 
                graph = edu.uconn.psy.jtrace.UI.GraphPanel.annotateJTRACEChart(graph, currGraphParameters, currParam);
                ChartUtilities.saveChartAsPNG(saveFile, graph, 1024, 768);
            }
            catch (java.io.IOException ex)
            {
                ex.printStackTrace();
            }            
        }
        else if(name.equals("save-graph-to-txt")){
            if(act.arguments().length!=2)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyFileAction() : incorrect arguments for action \'save-graph-to-txt\'; takes 1 argument (FileLocator)");
            try
            {   
                //arg 2 is a Text: "append-to-single-file" or "multiple-files"
                edu.uconn.psy.jtrace.IO.FileNameFactory fnf =  new edu.uconn.psy.jtrace.IO.FileNameFactory(currScript.getRootDirectory());                            
                java.io.File saveFile;
                if(((Text)act.arguments()[1]).value().equals("multiple-files")){
                    saveFile = fnf.makeGraphCsvFile((edu.uconn.psy.jtrace.Model.Scripting.FileLocator)act.arguments()[0],false);
                    currSim = new edu.uconn.psy.jtrace.Model.TraceSim(currParam);
                    runsim(currSim);            
                    XYSeriesCollection dataset = currAnalysis.doAnalysis(currSim);
                    thread=null;
                    currAnalysis.exportAnalysis(dataset, saveFile, false);                
                }
                else{
                    saveFile = fnf.makeGraphCsvFile((edu.uconn.psy.jtrace.Model.Scripting.FileLocator)act.arguments()[0],true);
                    currSim = new edu.uconn.psy.jtrace.Model.TraceSim(currParam);
                    runsim(currSim);            
                    XYSeriesCollection dataset = currAnalysis.doAnalysis(currSim);
                    thread=null;
                    java.util.Vector _w = currAnalysis.getItemsToWatch();
                    currAnalysis.exportAnalysis(dataset, saveFile, true);                
                }                
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }                
        }
        else if(name.equals("save-average-analysis-to-png-and-txt")){
            if(act.arguments().length!=2)
                throw new JTraceScriptingException("GraphActionWorkhorse.applyAnalysisAction() : incorrect arguments for action \'\'; takes 2 arguments (List, FileLocator)");            
            //save the raw data
            try{
                edu.uconn.psy.jtrace.IO.FileNameFactory fnf =  new edu.uconn.psy.jtrace.IO.FileNameFactory(currScript.getRootDirectory());            
                java.io.File saveCsvFile = fnf.makeAvgGraphCsvFile((edu.uconn.psy.jtrace.Model.Scripting.FileLocator)act.arguments()[1]);
                if(!saveCsvFile.getParentFile().exists())
                    saveCsvFile.getParentFile().mkdirs();
                if(!saveCsvFile.exists())
                    saveCsvFile.createNewFile();
                
                // fetch the data from the averagedAnalysis variable
                XYSeriesCollection dataset = currAnalysis.averageAnalysisCompletion();
                //save the csv file
                currAnalysis.exportAnalysis(dataset, saveCsvFile, false);                
            
                //then save the PNG file
                java.io.File savePngFile = fnf.makeAvgGraphPngFile((edu.uconn.psy.jtrace.Model.Scripting.FileLocator)act.arguments()[1]);
                // create the graph 
                JFreeChart graph = edu.uconn.psy.jtrace.UI.GraphPanel.createJTRACEChart(dataset, currGraphParameters);                
                //annotate  
                edu.uconn.psy.jtrace.UI.GraphParameters avgViewSettings = new edu.uconn.psy.jtrace.UI.GraphParameters(currGraphParameters);
                avgViewSettings.setIncludeInputAnnotation(false);
                graph = edu.uconn.psy.jtrace.UI.GraphPanel.annotateJTRACEChart(graph, avgViewSettings, currParam);
                ChartUtilities.saveChartAsPNG(savePngFile, graph, 1024, 768);
            }
            catch (java.io.IOException ex)
            {
                ex.printStackTrace();
            }
            
        }                
    }
}

/*
 * NoGuiTest.java
 *
 * Created on May 5, 2005, 9:41 AM
 */

package edu.uconn.psy.jtrace.Model;

import edu.uconn.psy.jtrace.Model.*;
import edu.uconn.psy.jtrace.UI.GraphParameters; 
//import edu.uconn.psy.jtrace.UI.GraphPanel;      // need a static method
import java.awt.event.*;
import java.io.*;
import org.jfree.data.xy.*;
import org.jfree.chart.*;


/**
 * Runs the default simulation with no GUI at all. Unit tests the Model 
 * components.
 *
 * @author harlan
 */
public class NoGuiTest {
    
    /** Creates a new instance of NoGuiTest */
    public NoGuiTest() {
    }
    
    public static void main(String[] args){     
        int ret;
        
        try
        {
            // create default params
            TraceParam param = new TraceParam();

            // create a sim
            TraceSim sim = new TraceSim(param);

            // run the sim for 10 steps, one at a time
            for (int i = 0; i < 10; i++)
            {
                ret = sim.cycle(1);
                
                if (ret != i+1)
                    throw new Exception("ret != i");
            }

            // run the sim for 10 steps, all at once
            ret = sim.cycle(10);
            if (ret != 20)
                throw new Exception("ret != 20");
            
            edu.uconn.psy.jtrace.UI.traceProperties prop = new edu.uconn.psy.jtrace.UI.traceProperties();
            prop.situate(null);
            java.io.File saveSim = new File(edu.uconn.psy.jtrace.UI.traceProperties.rootPath, "save_sim.jt");
            //next two lines save the file
            edu.uconn.psy.jtrace.IO.WTFileWriter fw = new edu.uconn.psy.jtrace.IO.WTFileWriter(saveSim,false);
            fw.writeAndClose(sim.XMLTag());
            
            // change the input and a parameter
            param.setModelInput("-gradu^l-");
            TraceParam.Decay d = param.getDecay();
            d.W = .03;
            param.setDecay(d);

            // run the sim for 20 steps (make sure it reset itself)
            ret = sim.cycle(20);
            if (ret != 20)
                throw new Exception("ret != 20 after reset");

            // create an analysis object
            TraceSimAnalysis an = new TraceSimAnalysis(TraceSimAnalysis.WORDS,
                    TraceSimAnalysis.WATCHTOPN, new java.util.Vector(), 10, 
                    TraceSimAnalysis.STATIC, 4, TraceSimAnalysis.FORCED, 4);
            // analyze the current run
            XYSeriesCollection analysis = an.doAnalysis(sim);

            // print the contents of the analysis object

            for (int iWords = 0; iWords < analysis.getSeriesCount(); iWords++)
            {
                XYSeries seriesData = analysis.getSeries(iWords);
                
                for (int iSteps = 0; iSteps < seriesData.getItemCount(); iSteps++)
                {
                    System.out.print(seriesData.getY(iSteps) + " ");
                }
                System.out.print("\n");
            }
            
            // run for 40 more steps
            ret = sim.cycle(40);
            if (ret != 60)
                throw new Exception("ret != 60");
            // and get the new analysis
            analysis = an.doAnalysis(sim);
            
            // save sim to disk
            // @@@ not yet implemented
            // @@@ calls a method in IO.FileWriter
            File saveFile = new File("noguitest.jt");
            if (saveFile == null)
                throw new Exception("can't open saveFile");
//            if (!FileWriter.saveSim(sim, saveFile))
//                throw new Exception("FileWriter.saveToDisk failed");
            
            // export raw data to disk
            File exportDir = new File("noguitest-raw");
            if (exportDir == null)
                throw new Exception("can't create exportDir");
            try
            {
                sim.export(exportDir);
            }
            catch (IOException ioe) 
            { 
                ioe.printStackTrace(); 
                throw new Exception("sim.export failed"); 
            }
            
            // export analysis object to disk
            File exportAnalysisFile = new File("noguitest.dat");
            if (exportAnalysisFile == null)
                throw new Exception("can't create exportAnalysisFile");
            if (!an.exportAnalysis(analysis, exportAnalysisFile,false))
                throw new Exception("an.exportAnalysis failed");
            
            // create and save graphical version of analysis object to disk
            GraphParameters graphParams = new GraphParameters();
            graphParams.setXLabel("X");
            graphParams.setYLabel("Y");
            graphParams.setGraphTitle("Title");
            // leave ranges as default null, to keep maximum ranges
            JFreeChart graph = edu.uconn.psy.jtrace.UI.GraphPanel.createJTRACEChart(analysis, graphParams);
            graph = edu.uconn.psy.jtrace.UI.GraphPanel.annotateJTRACEChart(graph, graphParams, sim.getParameters());
            File saveGraphFile = new File("noguitest.png");
            ChartUtilities.saveChartAsPNG(saveGraphFile, graph, 1024, 768);
            
            // try cloning the 3 major objects
            GraphParameters gpClone = new GraphParameters(graphParams);
            TraceSimAnalysis anClone = new TraceSimAnalysis(an);
            TraceParam pClone = new TraceParam(param);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

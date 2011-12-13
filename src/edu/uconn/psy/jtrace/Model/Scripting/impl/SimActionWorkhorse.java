/*
 * SimActionWorkhorse.java
 *
 * Created on May 4, 2005, 2:54 PM
 */

package edu.uconn.psy.jtrace.Model.Scripting.impl;

/**
 *
 * @author tedstrauss
 */
public class SimActionWorkhorse extends Workhorse{    
    
    /** Creates a new instance of SimActionWorkhorse */
    public SimActionWorkhorse(edu.uconn.psy.jtrace.Model.TraceParam p, edu.uconn.psy.jtrace.Model.TraceSim s, edu.uconn.psy.jtrace.Model.TraceSimAnalysis g,edu.uconn.psy.jtrace.UI.GraphParameters gp,edu.uconn.psy.jtrace.Model.Scripting.TraceScript scr){
        super(p,s,g,gp,scr);        
    }
    public edu.uconn.psy.jtrace.Model.TraceSim applyAction(edu.uconn.psy.jtrace.Model.Scripting.Action act)throws JTraceScriptingException{        
        //There are currently no actions that are applied just to the TraceSim object.
        //Display preferences would make suitable actions.
        return currSim;
    }
    public void applyFileAction(edu.uconn.psy.jtrace.Model.Scripting.Action act)throws JTraceScriptingException{        
        String name = act.name();
        if(name.equals("save-simulation-to-txt")){
            if(act.arguments().length!=1||null==act.arguments()[0].fileLocatorValue())
                throw new JTraceScriptingException("SimActionWorkhorse.applyFileAction() : incorrect arguments for action \'save-simulation-to-txt\'; takes 1 argument (FileLocator)");
            try
            {   
                edu.uconn.psy.jtrace.IO.FileNameFactory fnf =  new edu.uconn.psy.jtrace.IO.FileNameFactory(currScript.getRootDirectory());
                java.io.File saveDir = fnf.makeSimDumpFolder((edu.uconn.psy.jtrace.Model.Scripting.FileLocator)act.arguments()[0]);
                if(!saveDir.exists()) saveDir.mkdirs();
                currSim = new edu.uconn.psy.jtrace.Model.TraceSim(currParam);
                runsim(currSim);            
                thread=null;
                currSim.export(saveDir);
            }
            catch (Exception ex)
            {
            
            }                
        }
        else if(name.equals("save-simulation-to-jt")){
            if(act.arguments().length!=1||null==act.arguments()[0].fileLocatorValue())
                throw new JTraceScriptingException("SimActionWorkhorse.applyFileAction() : incorrect arguments for action \'save-simulation-to-jt\'; takes 1 argument (FileLocator)");
            try
            {   
                //save the TraceSim, TraceSimAnalysis and CurrGraphParameters to .jt file.
                currSim = new edu.uconn.psy.jtrace.Model.TraceSim(currParam);
                String result="";
                result+="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
                    "<jt xmlns=\'http://xml.netbeans.org/examples/targetNS\'"+
                    "\nxmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\'"+
                    "\nxsi:schemaLocation=\'http://xml.netbeans.org/examples/targetNS file:"+edu.uconn.psy.jtrace.UI.jTRACEMDI.properties.rootPath.getAbsolutePath()+"/Schema/jTRACESchema.xsd\'>";
                result+="<script>";
                result+=currSim.XMLTag();
                result+=currAnalysis.XMLTag();
                result+=currGraphParameters.XMLTag();
                result+="<action><set-cycles-per-sim><cycles>"+currScript.getCyclesPerSim()+"</cycles></set-cycles-per-sim></action>";                    
                result+="<action><new-window><arg>n/a</arg></new-window></action>";                    
                result+="</script>";
                result+="</jt>";
                
                edu.uconn.psy.jtrace.IO.FileNameFactory fnf =  new edu.uconn.psy.jtrace.IO.FileNameFactory(currScript.getRootDirectory());            
                java.io.File saveFile = fnf.makeSimFile((edu.uconn.psy.jtrace.Model.Scripting.FileLocator)act.arguments()[0]);
                edu.uconn.psy.jtrace.IO.WTFileWriter fw=new edu.uconn.psy.jtrace.IO.WTFileWriter(saveFile,false);

                fw.writeAndClose(result);
                fw=null;
            }
            catch (Exception ex)
            {
            
            }                
        }
    }    
}

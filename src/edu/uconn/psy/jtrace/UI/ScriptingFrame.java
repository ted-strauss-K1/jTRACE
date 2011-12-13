/*
 * ScriptingFrame.java
 *
 * Created on June 29, 2005, 12:30 PM
 */

package edu.uconn.psy.jtrace.UI;
import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import com.jaxfront.core.dom.*;
import com.jaxfront.core.type.*;
import com.jaxfront.core.ui.*;
import com.jaxfront.swing.ui.editor.*;
import com.jaxfront.swing.ui.beans.*;
import edu.uconn.psy.jtrace.IO.*;
import edu.uconn.psy.jtrace.parser.*;
import edu.uconn.psy.jtrace.parser.impl.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Validator;


/**
 *
 * @author tedstrauss
 */
public class ScriptingFrame extends JInternalFrame{
    
    java.net.URL defaultXmlURL;
    java.net.URL xmlURL;
    java.net.URL xsdURL;
    java.net.URL xuiURL;
    File defaultXmlFile;
    File xmlFile;
    File xsdFile;
    File xuiFile;
    
    edu.uconn.psy.jtrace.Model.TraceParam baseParam;
    edu.uconn.psy.jtrace.Model.Scripting.TraceScript script;
    edu.uconn.psy.jtrace.UI.jTRACEMDI gui;
    com.jaxfront.core.dom.Document dom;
    java.awt.Window parent;
    LinkedList templateList;
    java.io.File templateDirectory;
    java.io.File loadedFile;
    final String sep = System.getProperty("file.separator");
            
    /** Creates a new instance of ScriptingFrame */
    public ScriptingFrame(Window p) {
        super("Scripting",     // default title, will change later
              true, //resizable
              false, //closable -- although we'll handle it ourselves in jTRACEMDI
              true, //maximizable
              true);//iconifiable
        // add the tab            
        parent = p;
        baseParam = new edu.uconn.psy.jtrace.Model.TraceParam();
        try{
            defaultXmlFile = new File(jTRACEMDI.properties.rootPath.getAbsolutePath(),("templates"+sep+"basic-template.jt"));
            xmlFile = new File(jTRACEMDI.properties.rootPath.getAbsolutePath(),("templates"+sep+"basic-template.jt"));
            xsdFile = new File(jTRACEMDI.properties.rootPath.getAbsolutePath(), ("schema"+sep+"jTRACESchema.xsd"));
            xuiFile = new File(jTRACEMDI.properties.rootPath.getAbsolutePath(), ("schema"+sep+"jt.xui"));        
            
            defaultXmlURL = defaultXmlFile.toURI().toURL(); 
            xmlURL = xmlFile.toURI().toURL();
            xsdURL = xsdFile.toURI().toURL();
            xuiURL = xuiFile.toURI().toURL();            
            
            com.jaxfront.core.dom.Document dom = com.jaxfront.core.dom.DOMBuilder.getInstance().build(xsdURL, xmlURL, xuiURL, null);        
            
            templateDirectory = new java.io.File((jTRACEMDI.properties.rootPath.getAbsolutePath()+sep+"templates"+sep));
            loadedFile=null;
            //make a temporary XUI file in the same directory as the XML file
            makeXUI(templateDirectory);
            //create an NLS file containing bindings appropriate to this dom
            makeNLS(dom.getRootType(),templateDirectory);
            dom.release();
            dom = null;
            dom = com.jaxfront.core.dom.DOMBuilder.getInstance().build(xsdURL, xmlURL, xuiURL, null);
            initComponents(dom.getRootType(), templateDirectory);
            initHints();
        }
        catch(java.net.MalformedURLException urle){ urle.printStackTrace();}
        catch(com.jaxfront.core.dom.DocumentCreationException dce){ dce.printStackTrace();}                
    }
    private void initComponents(Type type, java.io.File dir){
        java.awt.GridBagConstraints gridBagConstraints;        
        getContentPane().setLayout(new java.awt.GridBagLayout());        
        //setBackground(java.awt.Color.white);        
                
        toolbarPanel = new javax.swing.JPanel();
        scriptActionLabel = new javax.swing.JLabel();
        runButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();
        sep1 = new javax.swing.JSeparator();
        sep2 = new javax.swing.JSeparator();
        loadTemplateButton = new javax.swing.JButton();
        baseParamButton = new javax.swing.JButton();
        templatesComboBox = new javax.swing.JComboBox();
        initTemplateMenu();
        
        toolbarPanel.setLayout(new java.awt.GridBagLayout());
        
        scriptActionLabel.setText("Script Action:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        toolbarPanel.add(scriptActionLabel, gridBagConstraints);


        runButton.setMnemonic('R');
        runButton.setText("Run");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runScript();
            }
        });
        toolbarPanel.add(runButton, gridBagConstraints);

        saveButton.setMnemonic('S');
        saveButton.setText("Save");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveScript();
            }
        });
        toolbarPanel.add(saveButton, gridBagConstraints);

        loadButton.setMnemonic('L');
        loadButton.setText("Load");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadScript(null);
            }
        });
        toolbarPanel.add(loadButton, gridBagConstraints);
        
        sep1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        toolbarPanel.add(sep1, gridBagConstraints);

        loadTemplateButton.setMnemonic('T');
        loadTemplateButton.setText("Load Template");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        loadTemplateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadScript((java.io.File)templateList.get(templatesComboBox.getSelectedIndex()));
            }
        });
        toolbarPanel.add(loadTemplateButton, gridBagConstraints);
        
        
        templatesComboBox.setPreferredSize(new java.awt.Dimension(175, 24));
        templatesComboBox.setMinimumSize(new java.awt.Dimension(125, 24));
        templatesComboBox.setMaximumSize(new java.awt.Dimension(250, 24));        
        toolbarPanel.add(templatesComboBox);
                
        sep2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        toolbarPanel.add(sep2, gridBagConstraints);

                
        paramDialog = new BaseParametersDialog((Frame)parent,baseParam);
                
        baseParamButton.setMnemonic('B');
        baseParamButton.setText("Set Base Parameters");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        baseParamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setBaseParameters();
            }
        });
        toolbarPanel.add(baseParamButton, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        getContentPane().add(toolbarPanel, gridBagConstraints);
        
        loadScript(null);
    }
    public void runScript(){
        try{
            //get the DOM object from the GUI.           
            StringBuffer xmlbuf = editorPanel.getDOM().serialize(true, true);
            //convert it to a byte stream
            java.io.ByteArrayInputStream byteinputstream = new java.io.ByteArrayInputStream(xmlbuf.toString().getBytes());
            BufferedInputStream bis=new BufferedInputStream(byteinputstream);
            //and parse it using the JAXB parser
            JAXBContext ctx = JAXBContext.newInstance( "edu.uconn.psy.jtrace.parser" );
            Unmarshaller unm = ctx.createUnmarshaller();
            //create the script object from the stream; which was created from the DOM tree.
            script = (new WTFileReader(new java.io.File(""))).buildScript((edu.uconn.psy.jtrace.parser.JtType)unm.unmarshal(bis));
            //encorporate the base parameters
            script.setBaseParameters(baseParam);
            script.setGuiPointer(gui);
            script.setLoadedFileReference(loadedFile);
            bis.close();
            String msg;
            //if(script.getTotalIterations()>0)            
            //    msg = "Run script?\nThere will be "+script.getTotalIterations()+"\nindividual simulations.";
            //else
            //@@@ scan the script for lexicon updates... then generate a dynamic message to the user about the script.
            msg = "Run script?\n";
            int confirm=javax.swing.JOptionPane.showConfirmDialog(gui,msg,"Question",javax.swing.JOptionPane.OK_CANCEL_OPTION,javax.swing.JOptionPane.QUESTION_MESSAGE);
            if(confirm==javax.swing.JOptionPane.CANCEL_OPTION){ return; }                       
        
            initProgressBar();
            script.interpret(progressFrame);        
            System.gc();
        }
        catch(com.jaxfront.core.schema.ValidationException ve){ve.printStackTrace();}
        catch(javax.xml.bind.JAXBException xe){xe.printStackTrace();}
        catch(Exception e){e.printStackTrace();}
    }
    public void loadScript(java.io.File jtFile){
        if(jtFile==null&&editorPanel!=null){
            //select a file
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser(traceProperties.rootPath.getAbsolutePath());        
            fileChooser.setDialogTitle("Open jTRACE Script");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.addChoosableFileFilter(new XMLFileFilter());
            fileChooser.setCurrentDirectory(traceProperties.workingPath);
            
            int returnVal=fileChooser.showOpenDialog(this);
            //then load the file
            if (returnVal == JFileChooser.APPROVE_OPTION){                    
                jtFile = fileChooser.getSelectedFile();
                traceProperties.workingPath = jtFile.getParentFile();           
                loadedFile=jtFile;
                //put the file name as the window title:
                setTitle(loadedFile.getName());            
            }
            else{ //file load is cancelled
                return;
            }
        }
        try{
            java.io.File currDirectory;
            if(jtFile==null){
                xmlURL = defaultXmlURL;
                currDirectory = templateDirectory;
            }
            else{
                xmlFile = jtFile;
                xmlURL = xmlFile.toURI().toURL();
                currDirectory = jtFile.getParentFile();
            }
            makeXUI(currDirectory);
            xuiFile = new File(currDirectory.getAbsolutePath(),"jt.xui");
            xuiURL = xuiFile.toURI().toURL();
            com.jaxfront.core.dom.Document dom = com.jaxfront.core.dom.DOMBuilder.getInstance().build(xsdURL, xmlURL, xuiURL, null);        
            //create an NLS file containing bindings appropriate to this dom
            makeNLS(dom.getRootType(),currDirectory);
            //then release the document and re-create it, now that an NLS file
            //exists which creates the name bindings that are not there due to
            //the JaxFront people not finishing their tool!!!!
            //specifically, choice schema elements (which are the basis for recursion).
            dom.release();
            dom = null;
            dom = com.jaxfront.core.dom.DOMBuilder.getInstance().build(xsdURL, xmlURL, xuiURL, null);
            if(editorPanel!=null){
                editorPanel.setTargetDOM(null);
                editorPanel.setVisible(false);            
            }
            //@@@ TODO - if there is a set-root-directory node in the DOM, then
            //set the filePath variable to jtFile.getPath(), and update the 
            //field within the DOM leaf, so that the path is displayed in the GUI
            editorPanel = new EditorPanel(dom.getRootType(),parent);
            //@@@ register listeners to handle events, cos the GUI is behaving weird.
            //editorPanel.treeNodesChanged(...);
            editorPanel.removeSaveButton();
            editorPanel.getWorkspace().removeCancelButton();
            editorPanel.getWorkspace().getMessageTablePanel().setVisible(false);
            editorPanel.getWorkspace().getStatusBar().setVisible(false);
            editorPanel.setHelpPanelPosition(BorderLayout.NORTH);
            editorPanel.setPreferredSize(new java.awt.Dimension(800, 600));
            editorPanel.setMinimumSize(new java.awt.Dimension(300, 150));
            editorPanel.setMaximumSize(new java.awt.Dimension(1200, 850));
            editorPanel.setSize(800, 600);
            //editorPanel.
            editorPanel.getTreeModel().addTreeModelListener(new javax.swing.event.TreeModelListener() {
                public void treeNodesChanged(TreeModelEvent treeModelEvent) {
                    refreshGUI();
                    System.out.println("tree1: "+treeModelEvent);
                }
                public void treeNodesInserted(TreeModelEvent treeModelEvent) {
                    refreshGUI();
                    System.out.println("tree2: "+treeModelEvent);
                }
                public void treeNodesRemoved(TreeModelEvent treeModelEvent) {
                    refreshGUI();
                    System.out.println("tree3: "+treeModelEvent);
                }
                public void treeStructureChanged(TreeModelEvent treeModelEvent) {
                    refreshGUI();
                    System.out.println("tree4: "+treeModelEvent);
                }
            });
            
            java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            getContentPane().add(editorPanel, gridBagConstraints);
            editorPanel.selectRootNode();        
            editorPanel.setVisible(true);
            
            baseParam.resetToDefaults();
            
            System.gc();
        }
        catch(java.net.MalformedURLException urle){ urle.printStackTrace();}
        catch(com.jaxfront.core.dom.DocumentCreationException dce){ dce.printStackTrace();}                
    }
    private void refreshGUI(){
        //save to tmp
        java.io.File templateDirectory = new java.io.File((jTRACEMDI.properties.rootPath.getAbsolutePath()+sep+"templates"+sep));            
        java.io.File jtFile = new java.io.File(templateDirectory,edu.uconn.psy.jtrace.IO.FileNameFactory.tmpFileName());
        edu.uconn.psy.jtrace.IO.WTFileWriter fw = new edu.uconn.psy.jtrace.IO.WTFileWriter(templateDirectory ,jtFile.getName(),false);                                
        try{
            StringBuffer src = editorPanel.getDOM().serialize(true, true);
            fw.writeAndClose(src.toString());                                                                                   
        }
        catch(com.jaxfront.core.schema.ValidationException ve){
            ve.printStackTrace();
        }
        //reload from tmp
        loadScript(jtFile);
        jtFile.delete();
        System.out.println("tried to reload the script.");
    }
    public void saveScript(){
        try{
            // ask for a file name
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser(jTRACEMDI.properties.rootPath.getAbsolutePath());
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.addChoosableFileFilter(new XMLFileFilter());
            fileChooser.setCurrentDirectory(traceProperties.workingPath);
            
            int returnVal=fileChooser.showSaveDialog(this);
            // then write the file
            if (returnVal == JFileChooser.APPROVE_OPTION){                    
                WTFileWriter fw = new edu.uconn.psy.jtrace.IO.WTFileWriter(fileChooser.getSelectedFile().getParentFile(),fileChooser.getSelectedFile().getName(),false);                                
                StringBuffer src = editorPanel.getDOM().serialize(true, true);
                //if base param is not default
                if(!baseParam.equals(new edu.uconn.psy.jtrace.Model.TraceParam())){
                    int insertPoint = src.indexOf("<script>") + 8;                        
                    // add base parameters to the editor automatically
                    src.insert(insertPoint, ("\n\t<action><set-parameters>\n\t"+baseParam.XMLTag()+"\n\t</set-parameters></action>"));
                }
                fw.writeAndClose(src.toString());                                                                           
                traceProperties.workingPath = fileChooser.getSelectedFile().getParentFile();
                loadedFile=fileChooser.getSelectedFile();
                setTitle(loadedFile.getName());            
            }
            System.gc();
        }
        catch(com.jaxfront.core.schema.ValidationException ve){ve.printStackTrace();}
        catch(Exception e){e.printStackTrace();}
    }
    public void setBaseParameters(){
        paramDialog = new BaseParametersDialog((Frame)parent,baseParam);                
        paramDialog.getDoneButton().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baseParam = paramDialog.getParameters();
                paramDialog.setEnabled(false);
                paramDialog.setVisible(false);
                paramDialog.dispose();
                paramDialog=null;
            }
        });
        paramDialog.getCancelButton().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paramDialog.setEnabled(false);
                paramDialog.setVisible(false);
                paramDialog.dispose();
                paramDialog=null;
            }
        });
        paramDialog.setVisible(true);
        //paramDialog.setEnabled(true);        
    }
            
    private void initTemplateMenu(){
        java.io.File templateDir = new java.io.File(edu.uconn.psy.jtrace.UI.traceProperties.rootPath.getAbsolutePath() +
                System.getProperty("file.separator") + "templates");
        
        if(templateDir.exists()&&templateDir.isDirectory())
        {
            StringTokenizer tk;
            templateList = new LinkedList();
            LinkedList ll = new LinkedList();
            java.io.File[] templateItems = templateDir.listFiles();
            for(int i=0;i<templateItems.length;i++){
                if(templateItems[i].getName().endsWith(".jt")||templateItems[i].getName().endsWith(".xml")){                    
                    WTFileReader fr = new WTFileReader(templateItems[i]);
                    if(fr.validateJT()){
                        tk = new StringTokenizer(templateItems[i].getName(),".");
                        ll.add(tk.nextToken());
                        templateList.add(templateItems[i]);
                    }                    
                }
            }            
            templatesComboBox = new JComboBox(ll.toArray());                        
        }
    }
    private void initProgressBar(){
        progressBar=new javax.swing.JProgressBar(0,100);
        progressBar.setValue(0);
        progressBar.setVisible(true);        
        progressBar.setSize(250,50);
        progressBar.setMinimumSize(new java.awt.Dimension(250, 50));
        progressBar.setPreferredSize(new java.awt.Dimension(250, 50));
        progressFrame=new javax.swing.JDialog(gui,"jTRACE Script Progress",false);
        progressLabel=new javax.swing.JLabel("Working ... ");
        progressFrame.getContentPane().setLayout(new java.awt.BorderLayout());
        progressFrame.getContentPane().add(progressBar);
        progressBar.setStringPainted(true);
        progressBar.setVisible(true);        
        progressLabel.setSize(250,50);
        progressCancelButton = new javax.swing.JButton("cancel");
        progressFrame.getContentPane().setLayout(new java.awt.BorderLayout());
        progressFrame.getContentPane().add(progressBar,java.awt.BorderLayout.NORTH);        
        progressFrame.getContentPane().add(progressCancelButton,java.awt.BorderLayout.CENTER);
        progressFrame.getContentPane().add(progressLabel,java.awt.BorderLayout.SOUTH);
        progressBar.setStringPainted(true);
        progressFrame.setSize(250,75);
        progressFrame.setLocation(400,400);       
        progressFrame.pack();
        progressFrame.setVisible(true);
    }    
    
    public void setGuiPointer(edu.uconn.psy.jtrace.UI.jTRACEMDI _gui){
        gui=_gui;
        if(script!=null)
            script.setGuiPointer(gui);        
    }    
    private static String recurseType(Type t){
        if(t==null) return "";
        //System.out.println(t.getXPathLocation());
        String result =  new String();
        String value;
        StringTokenizer tk = new StringTokenizer(t.getXPathLocation(),"/");
        String eltType="";
        String unnamed="";
        
        while(tk.hasMoreTokens()){
            eltType=unnamed;
            unnamed=tk.nextToken();            
        }        
        if(unnamed.startsWith("UNNAMED1")){            
            if(eltType.equals("parameters")){                
                if(unnamed.equals("UNNAMED1[list]")){
                    value="set-parameters-list";                    
                    result+=t.getXPathLocation()+"_label = "+value+" \n";
                    result+=t.getXPathLocation()+"_caption = "+value+" \n";
                    result+=t.getXPathLocation()+"_treeEntry = "+value+" \n";                    
                }
                else{
                    value="set-one-parameter";
                    result+=t.getXPathLocation()+"_label = "+value+" \n";
                    result+=t.getXPathLocation()+"_caption = "+value+" \n";
                    result+=t.getXPathLocation()+"_treeEntry = "+value+" \n";                   
                }
            }
            else if(eltType.equals("file")){
                value="Set relative path, absolute path, or leave blank.";
                result+=t.getXPathLocation()+"_label = "+value+" \n";
                result+=t.getXPathLocation()+"_caption = "+value+" \n";
                result+=t.getXPathLocation()+"_treeEntry = "+value+" \n";                   
            }
            else if(eltType.equals("script")){
                if(unnamed.equals("UNNAMED1[list]")){
                    value="Script Root";
                    result+=t.getXPathLocation()+"_label = "+value+" \n";
                    result+=t.getXPathLocation()+"_caption = "+value+" \n";
                    result+=t.getXPathLocation()+"_treeEntry = "+value+" \n";                   
                }
                else{
                    StringTokenizer tk2= new StringTokenizer(unnamed,"[]");
                    tk2.nextToken();
                    String idx = tk2.nextToken();                    
                    value="Instruction "+idx+"";
                    result+=t.getXPathLocation()+"_label = "+value+" \n";
                    result+=t.getXPathLocation()+"_caption = "+value+" \n";
                    result+=t.getXPathLocation()+"_treeEntry = "+value+" \n";                   
                }                    
            }
            else if(eltType.equals("iterate")){
                value="How do you want to iterate over simulations?";
                result+=t.getXPathLocation()+"_label = "+value+" \n";
                result+=t.getXPathLocation()+"_caption = "+value+" \n";
                result+=t.getXPathLocation()+"_treeEntry = "+value+" \n";                   
            }                       
        }
        Iterator iter = t.getDirectChildren().listIterator();
        if(iter==null)
            return result;
        else{
            while(iter.hasNext()){
                result += recurseType((Type)iter.next());
            }
        }
        return result;
        
    }
    public static void makeXUI(java.io.File dir){
        String xuiSrc = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
            "<?jaxfront version=1.60;time=2005-03-24 15:26:31.75;appversion=;xsd=jTRACESchema.xsd;xml=basic.jt?>\n"+
            "<XUI xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"xui.xsd\" version=\"1.6\">\n"+
            "</XUI>\n";
        java.io.File tempXUIFile = new java.io.File(dir,"jt.xui");            
        tempXUIFile.deleteOnExit();        
        edu.uconn.psy.jtrace.IO.WTFileWriter fw = new edu.uconn.psy.jtrace.IO.WTFileWriter(tempXUIFile,false);
        fw.writeAndClose(xuiSrc);        
    }
    public static void makeNLS(com.jaxfront.core.type.Type type, java.io.File dir){
        String[] NLS_Syntax = {
            "_label = ",
            "_caption = ",
            "_treeEntry = "
        };

        String close = "]";
        String slash = "/";
        String list = "list";

        String root_array = "/jt/script/UNNAMED1[";
        String[] expr_array ={
            "iterate/instruction[",
            "condition/then["        
        };
        String iter_label = "iterate/UNNAMED1"; 

        String path_recur_array_1 = "action/set-watch-items/watch[";
        String path_recur_array_2 = "]/UNNAMED1/file/UNNAMED1";    
        String[] path_recur = {
            "condition/if/one/file/UNNAMED1",
            "condition/if/two/file/UNNAMED1",
            "action/UNNAMED1/file/UNNAMED1",
            "action/UNNAMED1/save-script/file/UNNAMED1",
            "action/load-sim-from-file/file/UNNAMED1",
            "action/save-script/file/UNNAMED1",
            "action/save-parameters-to-jt/file/UNNAMED1",
            "action/save-parameters-to-txt/file/UNNAMED1",
            "action/save-simulation-to-jt/file/UNNAMED1",
            "action/save-simulation-to-txt/file/UNNAMED1",
            "action/save-graph-to-png/file/UNNAMED1",
            "action/save-graph-to-txt/file/UNNAMED1",
            "action/write-to-a-file/file/UNNAMED1",
            "action/set-a-parameter/value/file/UNNAMED1",
            "iterate/UNNAMED1/predicate/one/file/UNNAMED1",
            "iterate/UNNAMED1/predicate/two/file/UNNAMED1"
        };

        String query_recur_array_1 = "action/set-watch-items/watch[";
        String query_recur_array_2 = "]/UNNAMED1/query/UNNAMED1";
        String[] query_detail = {
            //"action/set-watch-items/watch[***]/query/UNNAMED1"," 
            "action/remove-one-analysis-item/query/UNNAMED1",
            "action/add-one-analysis-item/query/UNNAMED1",
            "condition/if/one/query/UNNAMED1",
            "condition/if/two/query/UNNAMED1",            
            "action/set-a-parameter/value/query/UNNAMED1"
        };

        String param_list = "action/set-parameters/parameters/UNNAMED1[list]";
        String param_array = "action/set-parameters/parameters/UNNAMED1[";

        String[] bindings = {        
            "Instruction ",
            "How do you want to iterate over simulations?",
            "Set relative path, absolute path, or leave blank.",
            "Give query details here.",
            "List of parameter settings",
            "Set one parameter."        
        };
        String nls="#NLS (National Language Support) JAXFront V1.50 \n"+
            "/jt_caption=Description \n"+
            "/jt_displayValue=Description \n"+
            "/jt_treeEntry=Description \n";
        
        for(int i=0;i<50;i++){
            for(int j=0;j<NLS_Syntax.length;j++){
                nls+=(root_array+i+close+NLS_Syntax[j]+bindings[0]+i+"\n"); 
            }
        }    
        
        for(int i=0;i<2;i++){
            for(int h=0;h<query_detail.length;h++){
                for(int j=0;j<NLS_Syntax.length;j++){
                    nls+=(root_array+i+close+slash+query_detail[h]+NLS_Syntax[j]+bindings[3]+"\n");
                }
            }
            /*for(int k=0;k<expr_array.length;k++){
                for(int l=0;l<3;l++){
                    for(int h=0;h<query_detail.length;h++){
                        for(int j=0;j<NLS_Syntax.length;j++){
                            nls+=(root_array+i+close+slash+expr_array[k]+l+close+slash+query_detail[h]+NLS_Syntax[j]+bindings[3]+"\n");
                        }
                    }                    
                }            
            }*/
        }
        
        for(int i=0;i<2;i++){            
            for(int j=0;j<NLS_Syntax.length;j++){
                nls+=(root_array+i+close+slash+iter_label+NLS_Syntax[j]+bindings[1]+"\n");                        
            }            
            /*for(int k=0;k<expr_array.length;k++){
                for(int l=0;l<3;l++){
                    for(int m=0;m<NLS_Syntax.length;m++){
                        nls+=(root_array+i+close+slash+expr_array[k]+l+close+slash+iter_label+NLS_Syntax[m]+bindings[1]+"\n");                        
                    }
                }
            }*/
        }
        for(int i=0;i<2;i++){
            for(int h=0;h<path_recur.length;h++){
                for(int j=0;j<NLS_Syntax.length;j++){
                    nls+=(root_array+i+close+slash+path_recur[h]+NLS_Syntax[j]+bindings[2]+"\n");
                }
            }
            /*for(int k=0;k<expr_array.length;k++){
                for(int l=0;l<3;l++){
                    for(int h=0;h<path_recur.length;h++){
                        for(int j=0;j<NLS_Syntax.length;j++){
                            nls+=(root_array+i+close+slash+expr_array[k]+l+close+slash+path_recur[h]+NLS_Syntax[j]+bindings[2]+"\n");
                        }
                    }                    
                }            
            }*/
        }
        
        nls+=recurseType(type);                
        //System.out.println(nls);
        java.io.File tempNLSFile = new java.io.File(dir,"jt_en.nls");            
        tempNLSFile.deleteOnExit();        
        edu.uconn.psy.jtrace.IO.WTFileWriter fw = new edu.uconn.psy.jtrace.IO.WTFileWriter(tempNLSFile,false);
        fw.writeAndClose(nls);        
    }
    public void initHints() 
    {
        MouseOverHintManager hintManager = jTRACEMDI.hintManager;
        
        //hintManager.addHintFor(scriptTab, "Edit script");
        hintManager.addHintFor(paramDialog,"Set the base parameters for this script.");
        hintManager.addHintFor(editorPanel,"Create a script by defining iterators, actions, queries, etc.");
        hintManager.addHintFor(progressFrame,"Progress of a running script.");
        hintManager.addHintFor(progressBar,"Progress of a running script.");
        hintManager.addHintFor(progressLabel,"");
        hintManager.addHintFor(progressCancelButton,"Cancel script in progress.");
        hintManager.addHintFor(runButton,"Run the script as currently defined.");
        hintManager.addHintFor(saveButton,"Save the script to file.");
        hintManager.addHintFor(loadButton,"Load a script from a file.");
        hintManager.addHintFor(loadTemplateButton,"Load scripting template selected from list.");
        hintManager.addHintFor(baseParamButton,"Open base parameters window.");
        hintManager.addHintFor(templatesComboBox,"Select a scripting template to build from.");
        hintManager.addHintFor(toolbarPanel,"Script administration buttons.");
        hintManager.enableHints(this);   
        
    }
    
    private BaseParametersDialog paramDialog;
    private EditorPanel editorPanel;
    //private javax.swing.JSplitPane splitPane; 
    //private javax.swing.JPanel leftPanel;
    //private javax.swing.JPanel rightPanel;
    private javax.swing.JDialog progressFrame;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JButton progressCancelButton;
    
    private javax.swing.JButton runButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton loadButton;
    private javax.swing.JButton loadTemplateButton;
    private javax.swing.JButton baseParamButton;
    private javax.swing.JComboBox templatesComboBox;
    private javax.swing.JLabel scriptActionLabel;
    private javax.swing.JSeparator sep1;
    private javax.swing.JSeparator sep2;
    private javax.swing.JPanel toolbarPanel;
//    private javax.swing.JLabel jLabel2;
//    private javax.swing.JLabel jLabel3;
//    private javax.swing.JToolBar jToolBar1;
    
}

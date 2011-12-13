/*
 * WTSimulationPanel.java
 *
 * Created on May 12, 2004, 4:44 PM
 */

package edu.uconn.psy.jtrace.UI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import edu.uconn.psy.jtrace.UI.*;
import edu.uconn.psy.jtrace.Model.*;


/**
 *
 * @author  tedstrauss
 */

//TODO: change order of graph panels!
public class SimPanel extends javax.swing.JPanel {
      
    //variable definitions for Swing objects at bottom.
    
    // shared with other panels
    TraceSim sim;
    TraceParam param;
    
    // the four graphs
    AbstractSimGraph tGraph1;
    AbstractSimGraph tGraph2;
    AbstractSimGraph tGraph3;
    AbstractSimGraph tGraph4;
    
    // displayable word and phoneme labels
    String[] wordLabels;
    
    java.awt.event.MouseEvent mouseEvt;
    
    int currTimeIncr;   // what time step is currently visualized
    
    long paramUpdateCt;    // local copy of the update count, to detect changes
    
    boolean isValidationPanel;
    
    public SimPanel(TraceParam tp, TraceSim _sim){
        // 
        param = tp;
        sim = _sim;
        
        paramUpdateCt = param.getUpdateCt();

        reset(); 

        // initialize graph objects
        tGraph1=new WordSimGraph(param, sim.getWordD()[0],sim.getMaxDuration(),tp.getNReps(),tp.getMin(),tp.getMax(),wordLabels);
        tGraph2=new FeatureSimGraph(param, sim.getFeatureD()[0],sim.getMaxDuration(),tp.getNReps(),tp.getMin(),tp.getMax());                        
        tGraph3=new PhonemeSimGraph(param, sim.getPhonemeD()[0],sim.getMaxDuration(),tp.getNReps(),tp.getMin(),tp.getMax());
        tGraph4=new InputSimGraph(param, sim.getInputD()[0],sim.getMaxDuration(),tp.getNReps(),0,1);                                        
        
        // initialize all the other visual components
        initComponents();        
        
        initHints();
        
        setVisible(true);    
    }
    
    public boolean checkForParamUpdate()
    {
        if (param.getUpdateCt() > paramUpdateCt)
        {
            //System.out.println("updating sim panel.");
            if(togglePhonemeButton.isSelected())
                togglePhonemeButtonMouseClicked(new java.awt.event.MouseEvent(this,0,0l,0,0,0,0,false));                
            if(toggleWordButton.isSelected())
                toggleWordButtonMouseClicked(new java.awt.event.MouseEvent(this,0,0l,0,0,0,0,false));                
            
            paramUpdateCt = param.getUpdateCt();
            
            sim.reset();
            reset();
            
            return true;
        }
        else
            return false;
    }
    public boolean checkForParamUpdateDontReset()
    {
        if (param.getUpdateCt() > paramUpdateCt)
        {
            paramUpdateCt = param.getUpdateCt();
            
            sim.reset();
                    
            return true;
        }
        else
            return false;
    }
    /**
     * Reset everything on the screen. This must be run after
     * the parameters and sim are updated.
     */
    private void reset()
    {
        // get labels for word panel
        wordLabels=param.getLexicon().toStringArray();
        
        // make sure we're displaying the right amount of stuff
        currTimeIncr = sim.getStepsRun();
        
        isValidationPanel=false;
        
        if (tGraph1 != null)    // have we called initComponents() yet?
        {
            resetGraphs();   
            
            // display the right input
            updateInputTextField.setText(param.getModelInput());
        }
    }
    
    public void initComponents(){
        java.awt.GridBagConstraints gridBagConstraints;
        
        //toolSelect = new javax.swing.JComboBox(toolsStrings);
        traceControlsPanel = new javax.swing.JPanel();
        leftToolbarPanel = new javax.swing.JPanel();
        bigToolPanel = new javax.swing.JPanel();
        
        backStepButton = new javax.swing.JButton();
        forwardStepButton = new javax.swing.JButton();
        playStopButton = new javax.swing.JButton();
        rewindButton = new javax.swing.JButton();
        fastForwardButton = new javax.swing.JButton();
        setInputButton= new javax.swing.JButton();
        toggleDisplayButton = new javax.swing.JToggleButton();
        exportSimButton = new javax.swing.JButton();
        saveImageButton = new javax.swing.JButton();
        validationButton = new javax.swing.JButton();
        toggleWordButton = new javax.swing.JToggleButton();        
        togglePhonemeButton = new javax.swing.JToggleButton(); 
        nextInputButton = new javax.swing.JButton();
        prevInputButton = new javax.swing.JButton();
        
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        
        rightToolbarPanel = new javax.swing.JToolBar();        
        //jToolBar3 = new javax.swing.JToolBar();
        
        traceControlsLabel = new javax.swing.JLabel();
        activationDisplayLabel = new javax.swing.JLabel();
        //jLabel3 = new javax.swing.JLabel();
           
        updateInputTextField = new javax.swing.JTextField();
        frameCounter = new javax.swing.JTextField();        
        timer = new javax.swing.Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                animateGraphs();    
            }
        });
//        progressTimer = new javax.swing.Timer(150, new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                updateProgressBar();    
//            }            
//        });
        
        
        setLayout(new java.awt.GridBagLayout());
        //setBackground(Color.WHITE); // want grey background now
        setName("Simulation");
        this.setMaximumSize(new Dimension(1300,800));
        this.setMinimumSize(new Dimension(800,600));
        this.setPreferredSize(new Dimension(1300,800));
        this.setToolTipText("TRACE simulation panel, with pseudo-acoustic input \""+param.getModelInput()+"\".");                               
        setSize(1200, 650);
        
        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(jSeparator2, gridBagConstraints);

        jSeparator5.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator5.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jSeparator5, gridBagConstraints);
                
        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator6.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jSeparator6, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;    
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor=gridBagConstraints.SOUTHEAST;        
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;        
        add(tGraph1,gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;      
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor=gridBagConstraints.SOUTHWEST;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(tGraph2,gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;      
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;        
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(tGraph3,gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;      
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;        
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(tGraph4,gridBagConstraints);
        
        // word and phoneme toggle buttons 
        toggleWordButton.setText("~");
        toggleWordButton.setMargin(new java.awt.Insets(1, 3, 1, 3));
        toggleWordButton.setMinimumSize(new java.awt.Dimension(22, 23));
        toggleWordButton.setToolTipText("Change visualization style of word activation window.");
        toggleWordButton.addMouseListener(new java.awt.event.MouseAdapter() {
            /*public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleWordButtonMouseClicked(evt);
            }*/
            public void mousePressed(java.awt.event.MouseEvent evt) {
                toggleWordButtonMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;        
        add(toggleWordButton, gridBagConstraints);
        
                
        togglePhonemeButton.setText("~");
        togglePhonemeButton.setMargin(new java.awt.Insets(1, 3, 1, 3));
        togglePhonemeButton.setMinimumSize(new java.awt.Dimension(22, 23));
        togglePhonemeButton.setToolTipText("Change visualization style of phoneme activation window.");
        togglePhonemeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            /*public void mouseClicked(java.awt.event.MouseEvent evt) {
                togglePhonemeButtonMouseClicked(evt);
            }*/
            public void mousePressed(java.awt.event.MouseEvent evt) {
                togglePhonemeButtonMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;        
        add(togglePhonemeButton, gridBagConstraints);
        
        bigToolPanel.setLayout(new java.awt.GridBagLayout());
        
        // leftToolbarPanel setup       
        
        leftToolbarPanel.setLayout(new java.awt.GridBagLayout());
        //leftToolbarPanel.setPreferredSize(new java.awt.Dimension(450, 25));                  
        
        // display toggle button (0,0)
        toggleDisplayButton.setText("Display enabled");
        toggleDisplayButton.setMargin(new java.awt.Insets(1, 3, 1, 3));
        toggleDisplayButton.setSelected(true);
        //toggleDisplayButton.setMinimumSize(new java.awt.Dimension(22, 23));
        //toggleDisplayButton.setToolTipText("Change visualization style of word activation window.");
        toggleDisplayButton.addMouseListener(new java.awt.event.MouseAdapter() {
            /*public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleDisplayButtonMouseClicked(evt);
            }*/
            public void mousePressed(java.awt.event.MouseEvent evt) {
                toggleDisplayButtonMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2,2,2,2); 
        leftToolbarPanel.add(toggleDisplayButton, gridBagConstraints);
        
        
        // save image button (0,1)
        saveImageButton.setText("Save image...");
        saveImageButton.setMargin(new java.awt.Insets(1, 3, 1, 3));
        saveImageButton.setEnabled(false);
        saveImageButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                //this.saveImageButtonMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2,2,2,2); 
        leftToolbarPanel.add(saveImageButton, gridBagConstraints);
        
        // save image button (0,1)
        validationButton.setText("Validation...");
        validationButton.setMargin(new java.awt.Insets(1, 3, 1, 3));
        validationButton.setEnabled(true);
        validationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                validationButtonMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2,2,2,2); 
        leftToolbarPanel.add(validationButton, gridBagConstraints);        
        
        // export sim button (1,0)
        exportSimButton.setText("Export data...");
        exportSimButton.setMargin(new java.awt.Insets(1, 3, 1, 3));
        //exportSimButton.setEnabled(false);
        exportSimButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                exportSimButtonMouseClicked(evt);
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2,2,2,2); 
        //leftToolbarPanel.add(exportSimButton, gridBagConstraints); @@@ export data button removed
        
        // then a separator (2,*)
        jSeparator8.setForeground(Color.LIGHT_GRAY);
        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator8.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2,2,2,2); 
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        leftToolbarPanel.add(jSeparator8, gridBagConstraints);              
        
        // the Set Input button (3,0)
        setInputButton.setText("Set Input");
        setInputButton.setToolTipText("Run simulation with specified string.");
        setInputButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                updateInputMouseClicked(true);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        //gridBagConstraints.gridwidth = 3;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;        
        leftToolbarPanel.add(setInputButton, gridBagConstraints);
        
        // the input text field (4,1)
        updateInputTextField.setEditable(true);
        updateInputTextField.setText(param.getModelInput());        
        updateInputTextField.setBackground(Color.WHITE);
        updateInputTextField.setBorder(new javax.swing.border.LineBorder(Color.GRAY)); 
        //updateInputTextField.setToolTipText("TRACE's phonemes = p b t d k g s S r l a i u ^ -  (case sensitive)");                
        updateInputTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (param.getPhonology().validTraceWord(updateInputTextField.getText())){
                    updateInputTextField.setBackground(java.awt.Color.WHITE);
                    param.setModelInput(updateInputTextField.getText());
                    updateInputMouseClicked(true);
                }
                else{
                    updateInputTextField.setBackground(java.awt.Color.RED);
                }
                
            }
        });
        updateInputTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (param.getPhonology().validTraceWord(updateInputTextField.getText())){
                    updateInputTextField.setBackground(java.awt.Color.WHITE);
                    param.setModelInput(updateInputTextField.getText());
                    updateInputMouseClicked(true);
                }
                else{
                    updateInputTextField.setBackground(java.awt.Color.RED);
                }
                
            }
        });
        updateInputTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (param.getPhonology().validTraceWord(updateInputTextField.getText())){
                    updateInputTextField.setBackground(java.awt.Color.WHITE);
                    param.setModelInput(updateInputTextField.getText());
                    //updateInputMouseClicked(true);
                }
                else{
                    updateInputTextField.setBackground(java.awt.Color.RED);
                }
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2,2,2,2); 
        leftToolbarPanel.add(updateInputTextField, gridBagConstraints);
        
        // put leftToolBar in bigToolPanel
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        bigToolPanel.add(leftToolbarPanel, gridBagConstraints);    
        
        // setup activationDisplayLabel 
        activationDisplayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        activationDisplayLabel.setText("Graph activation info.");
        activationDisplayLabel.setFont(new Font("sansserif", Font.PLAIN, 12));
        activationDisplayLabel.setPreferredSize(new java.awt.Dimension(240, 50));
        //activationDisplayLabel.setMinimumSize(new java.awt.Dimension(100, 50));
        activationDisplayLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);       
        activationDisplayLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));        
        // put activationDisplayLabel in bigToolPanel
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.anchor = gridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(4,4,4,4);                
        bigToolPanel.add(activationDisplayLabel, gridBagConstraints);
        
        
        // trace controls panel
        traceControlsPanel.setLayout(new java.awt.GridBagLayout());
        //traceControlsPanel.setPreferredSize(new java.awt.Dimension(300, 25));                
        
        rewindButton.setFont(new Font("sansserif", Font.BOLD, 13));
        rewindButton.setText("|<<");
        rewindButton.setToolTipText("Return to start of simulation.");
        rewindButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rewindButtonMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;        
        traceControlsPanel.add(rewindButton, gridBagConstraints);
        
        backStepButton.setFont(new Font("sansserif", Font.BOLD, 13));
        backStepButton.setText("<");
        backStepButton.setToolTipText("Go back one cycle in time.");
        backStepButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                backwardStepMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;        
        traceControlsPanel.add(backStepButton, gridBagConstraints);

        playStopButton.setFont(new Font("sansserif", Font.BOLD, 13));
        playStopButton.setText("Play");        
        playStopButton.setToolTipText("Animate TRACE simulation.");
        playStopButton.addMouseListener(new java.awt.event.MouseAdapter() {            
            public void mousePressed(java.awt.event.MouseEvent evt) {
                playButtonMouseClicked(evt);
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;        
        traceControlsPanel.add(playStopButton, gridBagConstraints);
        
        //counter box
        frameCounter.setEditable(false);
        frameCounter.setText((new Integer(tGraph4.getCurrFrame())).toString());        
        frameCounter.setBackground(Color.WHITE);
        frameCounter.setBorder(new javax.swing.border.LineBorder(Color.GRAY)); 
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        traceControlsPanel.add(frameCounter, gridBagConstraints);                
        
        forwardStepButton.setFont(new Font("sansserif", Font.BOLD, 13));
        forwardStepButton.setText(">");
        forwardStepButton.setToolTipText("Go forward one cycle in time.");
        forwardStepButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                forwardStepMouseClicked(evt);                
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;        
        traceControlsPanel.add(forwardStepButton, gridBagConstraints);
        
        fastForwardButton.setFont(new Font("sansserif", Font.BOLD, 13));
        fastForwardButton.setText(">>|");
        fastForwardButton.setToolTipText("Go to end of processed cycles and continue.");
        fastForwardButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fastForwardButtonMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;        
        traceControlsPanel.add(fastForwardButton, gridBagConstraints);
        
        traceControlsLabel.setFont(new Font("sansserif", Font.BOLD, 13));
        traceControlsLabel.setText(" TRACE Controls ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth=6;  //**5**
        traceControlsPanel.add(traceControlsLabel, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.anchor = gridBagConstraints.EAST;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        bigToolPanel.add(traceControlsPanel, gridBagConstraints);    
        
        //bigToolPanel.setBackground(Color.lightGray.brighter()); // very light gray
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(bigToolPanel, gridBagConstraints);    
        
        registerMouseMotionListeners();        
    }    
    
    private void exportSimButtonMouseClicked(java.awt.event.MouseEvent evt)
    {
        // pop up a directory-save dialog
        JFileChooser dirChooser = new JFileChooser(traceProperties.rootPath.getAbsolutePath());
        
        dirChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        dirChooser.setDialogTitle("Export simulation to directory"); 
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setApproveButtonText("Export");  
        dirChooser.setCurrentDirectory(traceProperties.workingPath);            
        
        int returnVal = dirChooser.showSaveDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File exportDir = dirChooser.getSelectedFile();
            traceProperties.workingPath = exportDir.getParentFile();
            
            // set cursor
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // save sim to a directory tree
            try
            {
                sim.export(exportDir);
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(null,
                    "Export failed.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }

            // restore cursor
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    private void validationButtonMouseClicked(java.awt.event.MouseEvent evt)
    {
        // pop up instructions
        
        // pop up a directory-open dialog
        JFileChooser dirChooser = new JFileChooser(traceProperties.rootPath.getAbsolutePath());
        
        dirChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        dirChooser.setDialogTitle("Choose directory containing comparison files."); 
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setApproveButtonText("Validate");  
        dirChooser.setCurrentDirectory(traceProperties.workingPath);                    
        
        int returnVal = dirChooser.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File loadDir = dirChooser.getSelectedFile();
            traceProperties.workingPath = loadDir.getParentFile();    
        
            // set cursor
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // load and unmarshall a directory tree
            // then do validation.            
            try
            {
                edu.uconn.psy.jtrace.IO.WTFileReader fr = new edu.uconn.psy.jtrace.IO.WTFileReader(loadDir);
                double[][][][] validationD = fr.loadValidationData();
                //update the current TraceSim with the difference array.
                double SMAD = sim.validateAgainst(validationD);
                //it all goes red
                this.setBackground(java.awt.Color.PINK);
                for(int i=0;i<getComponentCount();i++){
                    if(getComponent(i) instanceof AbstractSimGraph){ 
                        ((AbstractSimGraph)getComponent(i)).setMin(0);
                        ((AbstractSimGraph)getComponent(i)).setMax(Math.abs(getMax()-getMin()));                        
                        //continue;
                    }
                    getComponent(i).setBackground(java.awt.Color.PINK);                
                }
                traceControlsPanel.setBackground(java.awt.Color.PINK);
                leftToolbarPanel.setBackground(java.awt.Color.PINK);
                
                this.updateUI();
                //pop-up SMAD result
                String validationMsg; 
                if((new Double(SMAD)).toString().length()>5)
                    validationMsg = "Result of SMAD metric: "+(new Double(SMAD)).toString().substring(0,5)+"%";
                else
                    validationMsg = "Result of SMAD metric: "+SMAD+"%";
                currTimeIncr=sim.getMaxDuration()-1;                
                displayData(currTimeIncr);
                javax.swing.JOptionPane.showMessageDialog(this,validationMsg,"Validation result",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                isValidationPanel=true;
                setInputButton.setEnabled(false);                
                toggleDisplayButton.setEnabled(false);
                exportSimButton.setEnabled(false);
                saveImageButton.setEnabled(false);
                validationButton.setEnabled(false);
                updateInputTextField.setEnabled(false);
                rightToolbarPanel.setEnabled(false);
                setInputButton.setVisible(false);                
                toggleDisplayButton.setVisible(false);                
                exportSimButton.setVisible(false);                
                saveImageButton.setVisible(false);                
                validationButton.setVisible(false);                
                updateInputTextField.setVisible(false);                
                
                firePropertyChange("isValidationFrame",false,true);                               
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,
                    "Validation failed.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

            // restore cursor
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        
    }
    private void saveImageButtonMouseClicked(java.awt.event.MouseEvent evt)
    {
        // @@@
        
        // pop up a file-save dialog (*.png)
        
        // magically save screenshot to a file
        
        
    }
    
    private void togglePhonemeButtonMouseClicked(java.awt.event.MouseEvent evt) {
        if(tGraph3.getClass().getName().endsWith("PhonemeSimGraph")){            
            //tGraph3.setVisible(false);
            this.remove(tGraph3);
            tGraph3=new PhonemeBoxSimGraph(param, sim.getPhonemeD()[currTimeIncr-1],sim.getMaxDuration(),param.getFSlices(),param.getMin(),param.getMax());        
            
        }
        else {
            this.remove(tGraph3);
            tGraph3=new PhonemeSimGraph(param, sim.getPhonemeD()[currTimeIncr-1],sim.getMaxDuration(),param.getFSlices(),param.getMin(),param.getMax());        
            tGraph3.addMouseMotionListener(new myMouseMotionAdapter(){
            public void mouseMoved(java.awt.event.MouseEvent evt){
                    queryGraph(evt);
                }            
            });
        }
        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;      
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;        
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(tGraph3,gridBagConstraints);            
        tGraph3.setVisible(true);
        
        validate();
        displayData(currTimeIncr);
    }
    private void toggleWordButtonMouseClicked(java.awt.event.MouseEvent evt) {
          
        if(tGraph1.getClass().getName().endsWith("WordSimGraph")){            
            //tGraph1.setVisible(false);
            this.remove(tGraph1);
            tGraph1=new WordBoxSimGraph(param, sim.getWordD()[currTimeIncr-1],sim.getMaxDuration(),param.getFSlices(),param.getMin(),param.getMax(), wordLabels, param.getDeltaInput() / param.getSlicesPerPhon());        
            
        }
        else {
            this.remove(tGraph1);
            tGraph1=new WordSimGraph(param, sim.getWordD()[currTimeIncr-1],sim.getMaxDuration(),param.getFSlices(),param.getMin(),param.getMax(), wordLabels);        
            tGraph1.addMouseMotionListener(new myMouseMotionAdapter(){
                public void mouseMoved(java.awt.event.MouseEvent evt){
                            queryGraph(evt);
                    }            
                });            
        }        
        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;    
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor=gridBagConstraints.SOUTHEAST;        
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;        
        add(tGraph1,gridBagConstraints);            
        tGraph1.setVisible(true);  
        
        validate();
        displayData(currTimeIncr);
    }    
    // |<<
    private void rewindButtonMouseClicked(java.awt.event.MouseEvent evt) {
        timer.stop();
        resetGraphs();  
    }
    private void resetGraphs()
    {
        if(playStopButton.getText().equals("stop")){
            playStopButton.setVisible(false);
            playStopButton.setText("play");
            playStopButton.setVisible(true);                    
        }
        remove(tGraph1);
        remove(tGraph2);
        remove(tGraph3);
        remove(tGraph4);
        
        tGraph1=new WordSimGraph(param, sim.getWordD()[0],sim.getMaxDuration(),param.getNReps(),param.getMin(),param.getMax(),wordLabels);
        tGraph2=new FeatureSimGraph(param, sim.getFeatureD()[0],sim.getMaxDuration(),param.getNReps(),param.getMin(),param.getMax());                        
        tGraph3=new PhonemeSimGraph(param, sim.getPhonemeD()[0],sim.getMaxDuration(),param.getNReps(),param.getMin(),param.getMax());
        tGraph4=new InputSimGraph(param, sim.getInputD()[0],sim.getMaxDuration(),param.getNReps(),0,1);                                        
        
        java.awt.GridBagConstraints gridBagConstraints;        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;    
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor=gridBagConstraints.SOUTHEAST;        
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;        
        add(tGraph1,gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;      
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor=gridBagConstraints.SOUTHWEST;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(tGraph2,gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;      
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;        
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(tGraph3,gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;      
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;        
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(tGraph4,gridBagConstraints);
        
        currTimeIncr=0;
        setFrameAll(0);        
        displayData(0);       
    }
    
    /**
     * Displays the current frame (step) of data. If displaySwitch is false,
     * just updates the frame counter. If frame == 0, clears graphs.
     *
     * @param frame     frame to display -- usually currTimeIncr
     */
    private void displayData(int frame)
    {        
        if (frame == 0)
        {
            tGraph1.drawData(null);
            tGraph2.drawData(null);
            tGraph3.drawData(null);
            tGraph4.drawData(null);
        }
        else if (toggleDisplayButton.isSelected())
        {
            if(tGraph1.mouseOverGrid(mouseEvt)){
                queryGraph(mouseEvt);
            }  
            else if(wordLabels.length>10)
            {
                ((WordSimGraph)tGraph1).filterAndDrawData(sim.getWordD()[frame-1], wordLabels);
                //if(tGraph1.getClass().getName().endsWith("WordGraphViewer"))
                //  ((WordGraphViewer)tGraph1).filterAndDrawData(sim.getWordD()[frame-1],wordLabels);
                //else if(tGraph1.getClass().getName().endsWith("WordGraphViewerTwo"))
                //  ((WordGraphViewerTwo)tGraph1).filterAndDrawData(sim.getWordD()[frame-1],wordLabels);
            }
            else{
                tGraph1.drawData(sim.getWordD()[frame-1]);  
            }        
            tGraph2.drawData(sim.getFeatureD()[frame-1]);
            tGraph3.drawData(sim.getPhonemeD()[frame-1]);
            tGraph4.drawData(sim.getInputD()[frame-1]);
        }
        frameCounter.setText(Integer.toString(frame));                
    }
    
    // <
    private void backwardStepMouseClicked(java.awt.event.MouseEvent evt) {
        //!! necessary to incorporate ratios of slices between the graphs !!                
        timer.stop();
        if(playStopButton.getText().equals("stop")){
            playStopButton.setVisible(false);
            playStopButton.setText("play");
            playStopButton.setVisible(true);                    
        }
        if(currTimeIncr==0) return;
        currTimeIncr--;
        decrementAll();
        
        displayData(currTimeIncr);
        
    }
    // play
    private void playButtonMouseClicked(java.awt.event.MouseEvent evt) {
        //!! necessary to incorporate ratios of slices between the graphs !!
        if(timer.isRunning()||playStopButton.getText().equals("stop")){
            playStopButton.setVisible(false);
            playStopButton.setText("play");
            playStopButton.setVisible(true);            
            timer.stop();
        }
        else{
            playStopButton.setVisible(false);
            playStopButton.setText("stop");
            playStopButton.setVisible(true);            
            timer.start();        
        }
    }
    
    // >
    private void forwardStepMouseClicked(java.awt.event.MouseEvent evt) {
        //!! necessary to incorporate ratios of slices between the graphs !
        
        if(timer.isRunning()) timer.stop();
        
        if(playStopButton.getText().equals("stop")){
            playStopButton.setVisible(false);
            playStopButton.setText("play");
            playStopButton.setVisible(true);                    
        }
        
        if(currTimeIncr >= sim.getMaxDuration()) return;        
        
        if(currTimeIncr < sim.getStepsRun())
        {
            currTimeIncr++; 
            incrementAll();
            //if (currTimeIncr >= sim.getMaxDuration())
            //  return;                        
            
            displayData(currTimeIncr);
        }
        else if(currTimeIncr == sim.getStepsRun())
        {
            sim.cycle(1);
            
            currTimeIncr = sim.getStepsRun();
            
            incrementAll();
            
            displayData(currTimeIncr);
        
        }        
    }
        
    // >>|
    private void fastForwardButtonMouseClicked(java.awt.event.MouseEvent evt) {
        timer.stop();
        if(playStopButton.getText().equals("stop")){
            playStopButton.setVisible(false);
            playStopButton.setText("play");
            playStopButton.setVisible(true);                    
        }
        currTimeIncr=sim.getStepsRun();
        setFrameAll(currTimeIncr);
        
        displayData(currTimeIncr);
    }    
    public void animateGraphs(){
        if(currTimeIncr>=sim.getMaxDuration()) 
            return;        
        
        if(currTimeIncr < sim.getStepsRun()){
            currTimeIncr++;            
            incrementAll();

            displayData(currTimeIncr);
        }
        else if(currTimeIncr== sim.getStepsRun()||!isValidationPanel){
            sim.cycle(1);
            
            currTimeIncr = sim.getStepsRun();
            
            incrementAll();
            
            displayData(currTimeIncr);
            
            //System.out.println(currTimeIncr+"\t");
        }      
        if(tGraph4.isAtEnd()){
            timer.stop();
            if(playStopButton.getText().equals("stop")){
                playStopButton.setVisible(false);
                playStopButton.setText("play");
                playStopButton.setVisible(true);                    
            }
        }
    }
    public void incrementAll(){
        tGraph1.incrementFrame();
        tGraph2.incrementFrame();
        tGraph3.incrementFrame();
        tGraph4.incrementFrame();
    }
    public void decrementAll(){
        tGraph1.decrementFrame();
        tGraph2.decrementFrame();
        tGraph3.decrementFrame();
        tGraph4.decrementFrame();
    }
    public void setFrameAll(int i){
        tGraph1.setCurrFrame(i);
        tGraph2.setCurrFrame(i);
        tGraph3.setCurrFrame(i);
        tGraph4.setCurrFrame(i);
        displayData(i);
    }
    public void stop(){timer.stop();}
    

    /**
     * Update the input field and restart.
     *
     * @param flag      currently ignored
     */
    private void updateInputMouseClicked(boolean flag) {        
        // set it in the param field
        param.setModelInput(updateInputTextField.getText());
        param.incrUpdateCt();
        // and update/reset
        checkForParamUpdate();
    }
   
    public void setWordLabels(String[] _l){
        //((WordGraphViewer)tGraph1).setWordLabels(_l);   
        //((WordGraphViewerTwo)tGraph1).setWordLabels(_l);   
        wordLabels=_l;
        //repaint();
    }
    //public edu.uconn.psy.jtrace.UI.WebTraceGraphViewer getWordGraph(){return tGraph1;}
    //public edu.uconn.psy.jtrace.UI.WebTraceGraphViewer getFeatureGraph(){return tGraph2;}
    //public edu.uconn.psy.jtrace.UI.WebTraceGraphViewer getPhonemeGraph(){return tGraph3;}
    //public edu.uconn.psy.jtrace.UI.WebTraceGraphViewer getInputGraph(){return tGraph4;}
    public String[] getWordLabels(){ return param.getLexicon().toStringArray();} // used in simTask.absDiffOp    
//    public String[] getPhonemeLabels(){ return phonemeLabels;}
    public String getModelInput(){return param.getModelInput();} 
    public int getMaxSpread(){return param.getMaxSpread();}
    public int getCurrDataAccum(){return sim.getStepsRun();}
    public int getMaxDuration(){return sim.getMaxDuration();}
    public int getCurrTimeIncr(){return currTimeIncr;}
    public double getMin(){return param.getMin();}
    public double getMax(){return param.getMax();}
    public int getDeltaInput(){return param.getDeltaInput();}
    //public edu.uconn.psy.jtrace.Model.TraceLexicon getLexicon(){return param.getLexicon();}
//    public edu.uconn.psy.jtrace.Model.TraceParam getParam(){
//        if(null!=param) return param;
//        else return sim.getParameters();
//    }
//    public edu.uconn.psy.jtrace.Model.TraceParam.wordFrequency getFreqNode(){
//        if(null==sim) return param.getFreqNode();
//        else return sim.getParameters().getFreqNode();
//    }
    //public void setWordGraph(edu.uconn.psy.jtrace.UI.WebTraceGraphViewer graph){tGraph1=graph; repaint();}
    //public void setFeatureGraph(edu.uconn.psy.jtrace.UI.WebTraceGraphViewer graph){tGraph2=graph; repaint();}
    //public void setPhonemeGraph(edu.uconn.psy.jtrace.UI.WebTraceGraphViewer graph){tGraph3=graph; repaint();}
    //public void setInputGraph(edu.uconn.psy.jtrace.UI.WebTraceGraphViewer graph){tGraph4=graph; repaint();}
    //public void setModelInputTextField(String txt){updateInputTextField.setText(txt);}
    public void setTraceParam(edu.uconn.psy.jtrace.Model.TraceParam tp){this.param=tp;}
//    public void setCurrDataAccum(int x){currDataAccum=x;}
//    public void setMaxDuration(int x){maxDuration=x;}
    public void setCurrTimeIncr(int x){currTimeIncr=x;}
    public boolean isSimNullPointer(){return sim.isNullPointer();}
    public void disableGraphToggle(){
        togglePhonemeButton.setEnabled(false);
        togglePhonemeButton.removeAll();
        toggleWordButton.setEnabled(false);
        toggleWordButton.removeAll();
    }  

    public void registerMouseMotionListeners(){
        tGraph1.addMouseMotionListener(new myMouseMotionAdapter(){
            public void mouseMoved(java.awt.event.MouseEvent evt){
                mouseEvt=evt;    
                queryGraph(evt);
            }            
        });
        tGraph2.addMouseMotionListener(new myMouseMotionAdapter(){
            public void mouseMoved(java.awt.event.MouseEvent evt){
                mouseEvt=evt;    
                queryGraph(evt);
            }            
        });
        tGraph3.addMouseMotionListener(new myMouseMotionAdapter(){
            public void mouseMoved(java.awt.event.MouseEvent evt){
                mouseEvt=evt;    
                queryGraph(evt);
            }            
        });
        tGraph4.addMouseMotionListener(new myMouseMotionAdapter(){
            public void mouseMoved(java.awt.event.MouseEvent evt){
                mouseEvt=evt;    
                queryGraph(evt);
            }            
        });
        
    }
    public void queryGraph(java.awt.event.MouseEvent evt){                
        // 
        String tip="";
        tip+=((AbstractSimGraph)evt.getComponent()).queryDataCell(evt);
        activationDisplayLabel.setText(tip);
        activationDisplayLabel.repaint();
        
        if(currTimeIncr==0) return;
        //
        /*if(evt.getComponent().getClass().getName().endsWith("PhonemeSimGraph")&&
                ((AbstractSimGraph)evt.getComponent()).mouseOverGrid(evt))
        {
            tGraph1.setPlotCompetition(false); //words off 
        }
        else*/ 
        if(evt.getComponent().getClass().getName().endsWith("WordSimGraph")&&
                ((AbstractSimGraph)evt.getComponent()).mouseOverGrid(evt))
        {
            ((WordSimGraph)evt.getComponent()).setPlotCompetition(true);
            int[] theGridCoord = ((WordSimGraph)evt.getComponent()).queryGrid(evt);
            String theWord = ((WordSimGraph)tGraph1).getWordLabels()[theGridCoord[1]];                        
            int theAlignment = theGridCoord[0];
            //System.out.println("the word: "+theWord+", "+theAlignment);
            
            //now identify the items that should be in the display (competitors of this target)
            // competitorSet() returns an array pointing to [item,alignment] that are the competitors
            int[][] competitorSet = sim.competitorSet(theWord,theAlignment,currTimeIncr,theGridCoord[1],0);
            if(competitorSet==null) return;
            
            //normalize the competition strength field: from 0-100
            int min, max;
            if(theGridCoord[1]==0){
                min=competitorSet[1][2];
                max=competitorSet[1][2];
            }
            else{
                min=competitorSet[0][2];
                max=competitorSet[0][2];
            }
            for(int i=1;i<competitorSet.length;i++){
                if(i==theGridCoord[1]) continue;
                if(competitorSet[i][2]<min) min=competitorSet[i][2];
                if(competitorSet[i][2]>max) max=competitorSet[i][2];
            }
            for(int i=0;i<competitorSet.length;i++){
                if(i==theGridCoord[1]){ competitorSet[i][2]=100; continue;}                
                competitorSet[i][2]=(int)(100d*(((double)competitorSet[i][2]-(double)min)/((double)max-(double)min)));
            }
            
            //pull out data and labels that should be plotted.
            double[][] theData=new double[competitorSet.length][];
            String[] theLabels=new String[competitorSet.length];
            for(int i=0;i<competitorSet.length;i++){
                theData[i]=sim.getWordD()[currTimeIncr-1][competitorSet[i][0]];
                theLabels[i]=wordLabels[competitorSet[i][0]];
                competitorSet[i][0] = i;
            }
            
            //update the graphing objects.
            //((WordSimGraph)evt.getComponent()).setWordLabels(...);
            ((WordSimGraph)tGraph1).setWordLabels(theLabels);
            tGraph1.setCompetitionData(competitorSet);
            tGraph1.drawData(theData,theGridCoord[1]);
            tGraph3.setPlotCompetition(false); //phoneme off          
            
            //repaint the windows that need updating.                        
        }
        //else if(evt.getComponent().getClass().getName().endsWith("WordSimGraph")&&
        //        !((AbstractSimGraph)evt.getComponent()).mouseOverGrid(evt))
        //{
        //    tGraph1.setPlotCompetition(false);             
        //}
        else{
            tGraph1.setPlotCompetition(false);            
            if(wordLabels.length>10) {
                ((WordSimGraph)tGraph1).filterAndDrawData(sim.getWordD()[currTimeIncr-1], wordLabels);                
            }
            else{
                tGraph1.drawData(sim.getWordD()[currTimeIncr-1]); 
            }
        }
        
    }
    
    private javax.swing.JFrame progressFrame;
    private javax.swing.JProgressBar progressBar;
    
    private javax.swing.JButton backStepButton;
    private javax.swing.JButton forwardStepButton;
    private javax.swing.JButton playStopButton;
    private javax.swing.JButton rewindButton;
    private javax.swing.JButton fastForwardButton;
    private javax.swing.JButton setInputButton;
    private javax.swing.JToggleButton toggleDisplayButton;
    private javax.swing.JButton exportSimButton;
    private javax.swing.JButton saveImageButton;
    private javax.swing.JButton validationButton;
    private javax.swing.JToggleButton togglePhonemeButton;
    private javax.swing.JToggleButton toggleWordButton;
    private javax.swing.JButton nextInputButton;
    private javax.swing.JButton prevInputButton;
    
    private javax.swing.JLabel traceControlsLabel, activationDisplayLabel;
    private javax.swing.JPanel traceControlsPanel, leftToolbarPanel, bigToolPanel;
    
    private javax.swing.JSeparator jSeparator2;   
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSeparator jSeparator10;
    
    private javax.swing.JToolBar rightToolbarPanel;
    private javax.swing.Timer timer,progressTimer;
    private javax.swing.JTextField frameCounter;
    private javax.swing.JTextField updateInputTextField;
    
    public class myMouseMotionAdapter extends java.awt.event.MouseMotionAdapter{
        public myMouseMotionAdapter(){
            super();
        }
        public void mouseMoved(java.awt.event.MouseEvent evt){
                queryGraph(evt);
        }                
    }

    private void toggleDisplayButtonMouseClicked(java.awt.event.MouseEvent evt) {
        //displaySwitch = toggleDisplayButton.isSelected();
    }

    public void initHints() 
    {
        MouseOverHintManager hintManager = jTRACEMDI.hintManager;
        
        // the 4 graphs
        hintManager.addHintFor(tGraph1, "Word-level activation graph");
        hintManager.addHintFor(tGraph2, "Feature-level activation graph");
        hintManager.addHintFor(tGraph3, "Phoneme-level activation graph");
        hintManager.addHintFor(tGraph4, "Input graph");
        // the toggle graph buttons
        hintManager.addHintFor(togglePhonemeButton, "Toggle phoneme graph display type");
        hintManager.addHintFor(toggleWordButton, "Toggle word graph display type");
        // the toggle display button
        hintManager.addHintFor(toggleDisplayButton, "Toggle display of graphs");
        // the save image button
        hintManager.addHintFor(saveImageButton, "Save screenshot of graphs to a file");
        // the validation button
        hintManager.addHintFor(validationButton, "Validate simulation against external cTRACE data.");
        // the export sim button
        hintManager.addHintFor(exportSimButton, "Export raw simulation data to files");
        // the input button and box
        hintManager.addHintFor(setInputButton, "Set model input to specified string");
        hintManager.addHintFor(updateInputTextField, "TRACE's phonemes = p b t d k g s S r l a i u ^ -  (case sensitive)");
        // the controls
        hintManager.addHintFor(rewindButton, "Reset simulation");
        hintManager.addHintFor(backStepButton, "Step backwards");
        hintManager.addHintFor(playStopButton, "Start/stop simulation");
        hintManager.addHintFor(forwardStepButton, "Step forward");
        hintManager.addHintFor(fastForwardButton, "Fast-forward to last available time step");
        
        hintManager.enableHints(this);   
    }
    
        public void updateProgressBar(){
//        //System.out.println("prog-"+simTask.getProgress());
//        progressBar.setValue(task.getProgress());         
//        //if(task.getProgress()>=task.getMax()&&task.isDone()){            
//        if(task.isDone()){            
//            progressBar.setValue(task.getProgress());                 
//            progressFrame.setEnabled(false);
//            progressFrame.dispose();
//            progressFrame.setVisible(false);
//            progressBar.setVisible(false);            
//            progressFrame=null;
//            progressBar=null;
//            progressTimer.stop();
//            rewindButton.doClick();
//            setFrameAll(0);
//            frameCounter.setText((new Integer(tGraph4.getCurrFrame())).toString());
//            repaint();            
//        }
    }
    //compare two sets of SIM data
    private void compareSimData(java.awt.event.MouseEvent evt) {        
//        timer.stop();        
//        prompt1=new javax.swing.JOptionPane("...",JOptionPane.INFORMATION_MESSAGE,JOptionPane.OK_OPTION);
//        String msg="Use this feature to compare two sets of trace simulation data.\n"+
//                "  NOTE: YOU ARE RESPONSIBLE FOR SETTING UP TWO SIM \n"+
//                "  SETS FOR WHICH THIS COMPARISON IS MEANINGFUL.\n\n"+
//                "1. The first set of data may be loaded from file or you can use\n"+
//                "  the simulation data that is currently loaded.\n"+
//                "2. Select the directory location of the second set of data.\n"+
//                "3. After processing, the absolute difference between the two\n"+
//                "  simulations will be displayed like ordinary simulation data.\n"+
//                "  i.e. each cell[i,j] = | sim1[i,j] - sim2[i,j] | ";
//        prompt1.showMessageDialog(this,msg,"Instructions",JOptionPane.INFORMATION_MESSAGE);
//        Object[] possibleValues = { "Use current sim", "Use a saved sim" };
//        Object selectedValue = JOptionPane.showInputDialog(null, "Source: ", "Simulation set 1 of 2", JOptionPane.QUESTION_MESSAGE, null, possibleValues, possibleValues[0]);
//        
//        File file1=new File("../.."),file2=new File("../..");
//        double[][][][] Data1=new double[4][][][]; //Data2=new double[4][][][], DifData; //final dimensions will be: 4x{num_time_slices}x{x-axis}X{y-axis} ?
//        
//        //GET FIRST SET OF DATA
//        if(selectedValue==null){ 
//            return;
//        }
//        else if(((String)selectedValue).equals("Use current sim")){
//            Data1 = new double[4][][][];
//            Data1[0]=inputD; 
//            Data1[1]=featureD;
//            Data1[2]=phonemeD;
//            Data1[3]=wordD;
//            file1=null;
//        }
//        else if(((String)selectedValue).equals("Use a saved sim")){
//            fileChooser1 = new JFileChooser(jTRACE.properties.rootPath.getAbsolutePath());            
//            fileChooser1.setDialogTitle("Choose SIM directory: 1 of 2.");
//            fileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            int returnVal=fileChooser1.showOpenDialog(this);                
//            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                file1 = fileChooser1.getSelectedFile();
//                //check that selected directory is usable.
//                if(!file1.isDirectory()){
//                    msg="You must select a directory containing simulation files.";
//                    prompt1.showMessageDialog(this,msg,"Alert",JOptionPane.INFORMATION_MESSAGE);
//                    return;
//                }
//                File dir[]=file1.listFiles();                        
//                if(dir.length==0){ //add more conditions for bad selections here.
//                    msg="The contents of this folder do not appear to be simulation data.";
//                    prompt1.showMessageDialog(this,msg,"Alert",JOptionPane.INFORMATION_MESSAGE);
//                    return;
//                }                                                      
//            }
//            Data1=null;
//        }
//        //GET FIRST SET OF DATA
//            
//        //GET SECOND SET OF DATA
//        fileChooser1 = new JFileChooser(jTRACE.properties.rootPath.getAbsolutePath());
//        fileChooser1.setDialogTitle("Choose SIM directory: 2 of 2.");
//        fileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        int returnVal=fileChooser1.showOpenDialog(this);                
//
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            file2 = fileChooser1.getSelectedFile();
//            //check that selected directory is usable.
//            if(!file2.isDirectory()){
//                msg="You must select a directory containing simulation files.";
//                prompt1.showMessageDialog(this,msg,"Alert",JOptionPane.INFORMATION_MESSAGE);
//                return;
//            }
//            File[] dir2=file2.listFiles();                        
//            if(dir2.length==0){ //add more conditions for bad selections here.
//                msg="The contents of this folder do not appear to be simulation data.";
//                prompt1.showMessageDialog(this,msg,"Alert",JOptionPane.INFORMATION_MESSAGE);
//                return;
//            }                            
//        }            
//        //END GET SECOND SET OF DATA
//        
//        if(file1==null){
//            task=new edu.uconn.psy.jtrace.IO.simTask(Data1,file2,this,this.phonemeLabels,this.wordLabels,3);
//            progressBar=new javax.swing.JProgressBar(0,(file2.listFiles().length));        
//        }
//        else{ //if(Data1==null){
//            task=new edu.uconn.psy.jtrace.IO.simTask(file1,file2,this,4);            
//            progressBar=new javax.swing.JProgressBar(0,(file1.listFiles().length+file2.listFiles().length));
//        }        
//        progressBar.setValue(0);
//        progressFrame=new javax.swing.JFrame("comparing...");
//        progressFrame.getContentPane().add(progressBar);        
//        progressBar.setStringPainted(true);
//        progressBar.setVisible(true);        
//        progressBar.setSize(250,150);
//        progressFrame.setSize(250,150);
//        progressFrame.setLocation(400,400);       
//        progressFrame.pack();
//        progressFrame.setVisible(true);                            
//        progressTimer.start();         
//        task.start();                                                
    }
    
    //load sim data !
    private void loadSimMouseClicked(java.awt.event.MouseEvent evt) {
//        timer.stop();        
//        prompt1=new javax.swing.JOptionPane("...",JOptionPane.INFORMATION_MESSAGE,JOptionPane.OK_OPTION);
//        String msg="Use this feature to load simulations previously generated by WebTrace or Trace.\n"+
//            "Select a directory containing XML simulation files.\n";            
//        prompt1.showMessageDialog(this,msg,"Instructions",JOptionPane.INFORMATION_MESSAGE);
//        File file;
//        fileChooser1 = new JFileChooser(jTRACE.properties.rootPath.getAbsolutePath());
//        fileChooser1.setDialogTitle("Load SIM data from a directory.");
//        fileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        int returnVal=fileChooser1.showOpenDialog(this);                
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            file = fileChooser1.getSelectedFile();
//            //check that selected directory is usable.
//            if(!file.isDirectory()){
//                msg="You must select a directory containing simulation files.";
//                prompt1.showMessageDialog(this,msg,"Alert",JOptionPane.INFORMATION_MESSAGE);
//                loadSimMouseClicked(evt); //reloads the file chooser.
//                return;
//            }
//            File dir[]=file.listFiles();                        
//            if(dir.length==0){ //add more conditions here.
//                msg="The contents of this folder do not appear to be simulation data.";
//                prompt1.showMessageDialog(this,msg,"Alert",JOptionPane.INFORMATION_MESSAGE);
//                loadSimMouseClicked(evt); //reloads the file chooser.
//                return;
//            }            
//            for(int i=0;i<dir.length;i++){
//                if(dir[i].getName().endsWith("000.xml")) break;
//                if(i==dir.length-1){
//                    msg="The contents of this folder do not appear to be simulation data.";
//                    prompt1.showMessageDialog(this,msg,"Alert",JOptionPane.INFORMATION_MESSAGE);
//                    loadSimMouseClicked(evt); //reloads the file chooser.
//                    return;
//                }
//            }
//            {progressBar=new javax.swing.JProgressBar(0,dir.length);
//            progressBar.setValue(0);
//            progressFrame=new javax.swing.JFrame("loading ...");
//            progressFrame.getContentPane().add(progressBar);        
//            progressBar.setStringPainted(true);
//            progressBar.setVisible(true);        
//            progressBar.setSize(250,150);
//            progressFrame.setSize(250,150);
//            progressFrame.setLocation(400,400);       
//            progressFrame.pack();
//            progressFrame.setVisible(true);}            
//            task=new edu.uconn.psy.jtrace.IO.simTask(file,this,1);
//            task.start();
//            progressTimer.start();            
//        }         
    }    
    
    //data dump button    
    private void saveButtonMouseClicked(java.awt.event.MouseEvent evt) {
//        //POPS a prompt which asks for a path, and then asks until what slice
//        //should be dumped!  eg. input:(save path) /user/trace_sim_4 ;(from) 0;(to) 33.
//        
//        // stop the currently running sim, if it's running
//        timer.stop();        
//        
//        // tell the user that they're saving to a directory, not a file
//        prompt1=new javax.swing.JOptionPane("...",JOptionPane.INFORMATION_MESSAGE,JOptionPane.OK_OPTION);
//        String msg="jTRACE simulations are saved to a collection of XML files in a\n"+
//                "directory. Choose a directory to save the data (you can create a\n"+
//                "new directory in the selection box). Note that all time steps seen\n"+
//                " so far will be saved.\n";
//        prompt1.showMessageDialog(this,msg,"Instructions",JOptionPane.INFORMATION_MESSAGE);
//        
//        File targetDir;
//        
//        // ask for a directory name
//        fileChooser1 = new JFileChooser(jTRACE.properties.rootPath.getAbsolutePath());
//        fileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        int returnVal=fileChooser1.showSaveDialog(this);
//        
//        if (returnVal == JFileChooser.APPROVE_OPTION) 
//        {
//            // got a directory name, now save that directory name to a File object
//            targetDir = new java.io.File(fileChooser1.getSelectedFile().getPath());
//            
//            // I don't think this should be necessary, but just in case...
//            if(!targetDir.exists()) 
//            {
//                System.out.println("WTSimulationPanel.dataDumpButtonMouseClicked() : directory created");
//                targetDir.mkdir();    
//            }
//        }
//        else if(returnVal == JFileChooser.CANCEL_OPTION){
//            return;
//        }
//        else{
//            System.out.println("WTSimulationPanel.dataDumpButtonMouseClicked() : problem creating directory.");
//            return;
//            //dataDumpButtonMouseClicked(evt); // what? recurse? on an error?
//        }
//        
//        //TODO: make a JOptionPane to specify the range of data to be saved. 
//        
//        // and create the progress bar and the IO task to do the saving...
//        progressBar=new javax.swing.JProgressBar(0,currDataAccum);
//        progressBar.setValue(0);
//        progressFrame=new javax.swing.JFrame("saving ...");
//        progressFrame.getContentPane().add(progressBar);        
//        progressBar.setStringPainted(true);
//        progressBar.setVisible(true);        
//        progressBar.setSize(250,150);
//        progressFrame.setSize(250,150);
//        progressFrame.setLocation(400,400);       
//        progressFrame.pack();
//        progressFrame.setVisible(true);
//        task=new edu.uconn.psy.jtrace.IO.simTask(targetDir,this,2,inputD,featureD,phonemeD,wordD,phonemeLabels,param,currDataAccum);
//        task.start();
//        progressTimer.start(); 
    }

}

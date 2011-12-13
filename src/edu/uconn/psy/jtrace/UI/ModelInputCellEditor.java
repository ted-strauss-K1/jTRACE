/*
 * ModelInputCellEditor.java
 *
 * Created on July 20, 2007, 3:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.uconn.psy.jtrace.UI;

/**
 *
 * @author tedstrauss
 */
/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.InternationalFormatter;
import java.text.*;
import edu.uconn.psy.jtrace.Model.*;

/**
 * Implements a cell editor that uses a formatted text field
 * to edit Integer values.
 */
public class ModelInputCellEditor extends DefaultCellEditor {
    JFormattedTextField ftf;
    InternationalFormatter inputFormatter;
    private boolean DEBUG = false;
    TraceParam tp;
    
    class RegexFormat extends java.text.Format{
        public RegexFormat(){
            super();
        }
        
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos){                        
            toAppendTo=toAppendTo.insert(pos.getBeginIndex(),obj);
            return toAppendTo;
        } 
        //public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        //    return new AttributedCharacterIterator();
        //}
        public Object parseObject(String source, ParsePosition pos){
            //System.out.println("parseObject("+source+", ["+pos.getIndex()+","+pos.getErrorIndex()+"])= ...");
            return source;
        }
        public Object parseObject(String source)throws ParseException{
            //System.out.println("parseObject("+source+")= ...");
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(tp.getPhonology().getInputPattern());
            java.util.regex.Matcher m = p.matcher(source);
            if(m.matches()){
                ftf.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white));                
                return source;
            }
            else{
                throw new ParseException(source,0);
            }
        }
    }
    
    public ModelInputCellEditor (TraceParam _tp) {
        super(new JFormattedTextField());
        ftf = (JFormattedTextField)getComponent();
        tp = _tp;
        
        //Set up the editor for the integer cells.
        inputFormatter = new InternationalFormatter();
        String pattern = tp.getPhonology().getInputPattern();
        inputFormatter.setFormat(new RegexFormat());
                
        ftf.setFormatterFactory(new DefaultFormatterFactory(inputFormatter));
        ftf.setHorizontalAlignment(JTextField.TRAILING);
        ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);

        //React when the user presses Enter while the editor is
        //active.  (Tab is handled as specified by
        //JFormattedTextField's focusLostBehavior property.)
        ftf.getInputMap().put(KeyStroke.getKeyStroke(
                                        KeyEvent.VK_ENTER, 0),
                                        "check");
        ftf.getActionMap().put("check", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
		if (!ftf.isEditValid()) { //The text is invalid.
                    if (userSaysRevert()) { //reverted
		        ftf.postActionEvent(); //inform the editor
		    }
                } else try {              //The text is valid,
                    ftf.commitEdit();     //so use it.
                    ftf.postActionEvent(); //stop editing
                } catch (java.text.ParseException exc) { }
            }
        });
    }
    

    //Override to ensure that the value remains an Integer.
    public Object getCellEditorValue() {
        JFormattedTextField ftf = (JFormattedTextField)getComponent();
        Object o = ftf.getValue();        
        if (DEBUG) {
            System.out.println("getCellEditorValue: o isn't a Number");
        }
        try {
            return o;            
        } catch (Exception te) {
            System.err.println("getCellEditorValue: o doesn't validate: " + o);
            return null;
        }        
    }

    //Override to check whether the edit is valid,
    //setting the value if it is and complaining if
    //it isn't.  If it's OK for the editor to go
    //away, we need to invoke the superclass's version 
    //of this method so that everything gets cleaned up.
    public boolean stopCellEditing() {
        JFormattedTextField ftf = (JFormattedTextField)getComponent();
        try {
            ftf.commitEdit();                
        } catch (java.text.ParseException exc) { 
            exc.printStackTrace();
            Toolkit.getDefaultToolkit().beep();                
            ftf.setBorder(new javax.swing.border.LineBorder(java.awt.Color.red));
            return false;
        }
	return super.stopCellEditing();
    }

    /** 
     * Lets the user know that the text they entered is 
     * bad. Returns true if the user elects to revert to
     * the last good value.  Otherwise, returns false, 
     * indicating that the user wants to continue editing.
     */
    protected boolean userSaysRevert() {
        ftf.selectAll();
        ftf.setValue(ftf.getValue());
        return true;        
    }
}

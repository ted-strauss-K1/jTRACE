/*
 * LexiconTableModel.java
 *
 * Created on April 13, 2005, 9:08 PM
 */

package edu.uconn.psy.jtrace.UI;

import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author harlan
 */
public class LexiconTableModel extends DefaultTableModel {
    
    private String[] columnNames = {"Lexical Item", "Frequency", "Label"};
    
    private Object[][] data;
    
    
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        if (data == null)
            return 0;
        else
            return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    
    public Class getColumnClass(int c) {
        if (c == 0)
            return Object.class;
        else
            return Number.class;
        
    }
    
    public boolean isCellEditable(int row, int col) {
        return true;
    }
    
    public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    
    /** Creates a new instance of LexiconTableModel */
    public LexiconTableModel() {
    }
    
}

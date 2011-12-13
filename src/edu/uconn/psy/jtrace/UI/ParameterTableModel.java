 
package edu.uconn.psy.jtrace.UI;
import javax.swing.table.*;
/**
 *
 * @author  tedstrauss
 */
public class ParameterTableModel extends javax.swing.table.AbstractTableModel {
    // know our owner, so we know which data type to use
    javax.swing.JTable owner;
    
    private String DOUBLE_TABLE_NAME = "parametersTable";
    private String STRING_TABLE_NAME = "parametersStringTable";
    private String INT_TABLE_NAME = "parametersIntTable";
    
    private boolean LOCKED = false;
    // what goes into the table by default
    private Object [][] stringData = {
                {"Comment", new String(" "), "-", new String(" "), new String(" ")},
                {"User", new String(" "), "-", new String(" "), new String(" ")},
                {"Date", new String(" "), "-", new String(" "), new String(" ")}
    };
    private Object [][] intData = {
                {"FETSPREAD.pow", new Integer(6), "-", new Integer(6), "Power feature spread"},
                {"FETSPREAD.voc", new Integer(6), "-", new Integer(6), "Vocalic feature spread"},
                {"FETSPREAD.dif", new Integer(6), "-", new Integer(6), "Diffuse feature spread"},
                {"FETSPREAD.acu", new Integer(6), "-", new Integer(6), "Acute feature spread"},
                {"FETSPREAD.gra", new Integer(6), "-", new Integer(6), "Gradiant/Consonental feature spread"},
                {"FETSPREAD.voi", new Integer(6), "-", new Integer(6), "Voiced feature spread"},
                {"FETSPREAD.bur", new Integer(6), "-", new Integer(6), "Burst feature spread"},
                {"fSlices", new Integer(99), "-", new Integer(99), "Number of time steps"},
                {"deltaInput", new Integer(6), "-", new Integer(6), "Input phoneme interval"},
                {"nreps", new Integer(1), "-", new Integer(1), "Input presentation rate"},
                {"slicesPerPhon", new Integer(3), "-", new Integer(3), "Phoneme/Word slices per Feature slice"},
                {"lengthNormalization", new Integer(0), "-", new Integer(0), "0 or 1; Normalize length effects."}
    };
    private Object [][] doubleData = {
                {"aLPHA[if]", new Double(1), "-", new Double(1), "Input-Feature weights"},
                {"aLPHA[fp]", new Double(0.02), "-", new Double(0.02), "Feature-Phoneme weights"},
                {"aLPHA[pw]", new Double(0.05), "-", new Double(0.05), "Phoneme-Word weights"},
                {"aLPHA[pf]", new Double(0), "-", new Double(0), "Phoneme-Feature  weights (locked)"},
                {"aLPHA[wp]", new Double(0.03), "-", new Double(0.03), "Word-Phoneme weights"},
                {"GAMMA[f]", new Double(0.04), "-", new Double(0.04), "Feature-layer inhibition"},
                {"GAMMA[p]", new Double(0.04), "-", new Double(0.04), "Phoneme-layer inhibition"},
                {"GAMMA[w]", new Double(0), "-", new Double(0.03), "Word-layer inhibition"},
                {"DECAY[f]", new Double(0.01), "-", new Double(0.01), "Feature decay"},
                {"DECAY[p]", new Double(0.03), "-", new Double(0.03), "Phoneme decay"},
                {"DECAY[w]", new Double(0.05), "-", new Double(0.05), "Word decay"},
                {"REST.F", new Double(-0.1), "-", new Double(-0.1), "Feature resting activation"},
                {"REST.P", new Double(-0.1), "-", new Double(-0.1), "Phoneme resting activation"},
                {"REST.W", new Double(-0.01),"-", new Double(-0.01), "Word resting activation"},
                {"Input Noise (SD)", new Double(0), "-", new Double(0), ""},
                {"Stochasticity (SD)", new Double(0), "-", new Double(0), "McClelland: 0.02"},
                {"Attention", new Double(1d), "-", new Double(1d), "Lexical gain"},
                {"Bias", new Double(0d), "-", new Double(0d), "Lexical bias"},
                {"Learning Rate", new Double(0d), "-", new Double(0d), "Coming soon... (ft->ph learning)"},                        
                //{"LAMBDA[f]", new Double(0), "-", new Double(0), "Feature 2û inhibition"},
                //{"LAMBDA[p]", new Double(0), "-", new Double(0), "Phoneme 2û inhibition"},
                //{"LAMBDA[w]", new Double(0), "-", new Double(0), "Word 2û inhibition"},
                {"spreadScale", new Double(1), "-", new Double(1), "Scales FETSPREADs"},
                {"min", new Double(-0.3), "-", new Double(-0.3), "Minimum activation"},
                {"max", new Double(1.0), "-", new Double(1.0), "Maximum activation"},
                {"frq resting levels", new Double(0),  "-", new Double(0), "Dahan et al.: 0.06"},
                {"frq p->w wts", new Double(0), "-", new Double(0), "Dahan et al.: 0.13"},
                {"frq post-act", new Double(0), "-", new Double(0), "Dahan et al.: c=15"},
                {"Priming (rest)", new Double(0), "-", new Double(0), "E.g. 0.06"},
                {"Priming (weight)", new Double(0), "-", new Double(0), "E.g. 0.13"},
                {"Priming (post-act)", new Double(0), "-", new Double(0), "E.g. 15"}
    };
    
    private String[] columnNames = {"Parameter","Value","Function","Default", "Notes"};
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    
    //public ParameterTableModel() { super();}
    public ParameterTableModel(javax.swing.JTable _o) {
        super();
        owner=_o;
//        addTableModelListener(new javax.swing.event.TableModelListener(){        
//            public void tableChanged(javax.swing.event.TableModelEvent e){
//                //we could implement error checking here...                
//            }
//        });        
    }    
    
    public Object getValueAt(int row, int col) {
        Object ret;
        if (owner.getName().equals(DOUBLE_TABLE_NAME))
        {
            // double version
            if(row>=doubleData.length||col>=doubleData[0].length) 
                return null;
            if (col == 1 || col == 3)
                ret = new Double(((Double)doubleData[row][col]).doubleValue());
            else
                ret = doubleData[row][col];
        }
        else if (owner.getName().equals(INT_TABLE_NAME))
        {
            //  int version
            if(row>=intData.length||col>=intData[0].length) 
                return null;
            if (col == 1 || col == 3)
                ret = new Integer(((Integer)intData[row][col]).intValue());
            else
                ret = intData[row][col];
        }
        else if (owner.getName().equals(STRING_TABLE_NAME))
        {
            // string version
            if(row>=stringData.length||col>=stringData[0].length) 
                return null;
            ret = new String((String)stringData[row][col]);
        }
        else
            ret = null;
        
        return ret;
        
    }
    
    public Object[] getValuesColumn(){
        Object[] vals=new Object[getRowCount()];
        for(int i=0;i<getRowCount();i++){
            vals[i]=getValueAt(i,1);
        }
        return vals;
    }

    public Class getColumnClass(int c) {
        //System.out.println("getColumnClass " + Integer.toString(c));
        
        //return java.lang.Object.class;
        
        Object o = getValueAt(0, c);
        if (o != null)
            return o.getClass();
        else
        {
            o = getValueAt(0,c);
        
            return null;
        }
    }
    public Class getCellClass(int r,int c){
        return getValueAt(r, c).getClass();    
    }
    
    public boolean isCellEditable(int row, int col) {
        if(LOCKED) return false;
        
        String name = owner.getName();
        
        if (name == null)
            return false;
        
        if (col == 1 || col == 2) 
        {
            if (name.equals(DOUBLE_TABLE_NAME) && row == 3)
                return false;
            else
                return true;
        } else {
            return false;
        }
    }
    public void lock(){ LOCKED=true;}
    public void unlock(){ LOCKED=false;}
    
    public void setValueAt(Object value, int row, int col) {
        //System.out.println("setValueAt: " + Integer.toString(row) + ", " + Integer.toString(col));
        if (owner.getName().equals(DOUBLE_TABLE_NAME))
        {
            // double version
            doubleData[row][col] = value;
            
        }
        else if (owner.getName().equals(INT_TABLE_NAME))
        {
            //  int version
            intData[row][col] = value;
        }
        else if (owner.getName().equals(STRING_TABLE_NAME))
        {
            // string version
            if (value == null)
                value = new String(" ");
            stringData[row][col] = value;
            //System.out.println("string!");
        }
        
        //fireTableDataChanged();
        fireTableCellUpdated(row, col);
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        String name = owner.getName();
        
        if (name == null)
            return 0;
        
        if (name.equals(DOUBLE_TABLE_NAME))
        {
            // double version
            return doubleData.length;
        }
        else if (name.equals(INT_TABLE_NAME))
        {
            //  int version
            return intData.length;
        }
        else if (name.equals(STRING_TABLE_NAME))
        {
            // string version
            return stringData.length;
        }
        return 0;
    }
}
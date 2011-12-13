/*
 * XMLFileFilter.java
 *
 * Created on April 13, 2005, 5:13 PM
 */

package edu.uconn.psy.jtrace.IO;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *
 * @author harlan
 */
public class XMLFileFilter extends FileFilter {
    
    // no constructor...
    
    public final static String xml = "xml";
    public final static String jt = "jt";
    
    // true for XML files and directories
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;    // why???
        }

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(xml)) {
                    return true;
            }
            else if (extension.equals(jt)) {
                    return true;
            
            } else {
                return false;
            }
        }

        return false;

    
    }
    
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    //The description of this filter
    public String getDescription() {
        return "jTRACE files (*.jt, *.xml)";
    }
}

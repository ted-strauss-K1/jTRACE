/*
 * WindowMenuList.java
 *
 * Created on June 29, 2005, 9:03 PM
 */

package edu.uconn.psy.jtrace.UI;

import java.util.*;
import javax.swing.JMenuItem;
import edu.uconn.psy.jtrace.UI.jTRACE;

/**
 * A linked list of Vectors, with item 0 being a JMenuItem and item 1 being
 * a jTRACE. 
 *
 * @author harlan
 */
public class WindowMenuList {
    
    LinkedList l;
    
    /** Creates a new instance of WindowMenuList */
    public WindowMenuList() {
        l = new LinkedList();
    }
    
    public void add(JMenuItem jmi, jTRACE jt)
    {
        Vector v = new Vector(2);
        v.add(0, jmi);
        v.add(1, jt);
        l.add(v);
    }
    
    public void delete(int idx)
    {
        l.remove(idx);
    }
    
    public int indexOf(JMenuItem jmi)
    {
        for (ListIterator it = l.listIterator(0); it.hasNext(); )
        {
            Vector v = (Vector)it.next();
            
            if (v.get(0) == jmi)
            {
                return it.previousIndex();
            }
        }
        
        return -1;
        
    }
    
    public int indexOf(jTRACE jt)
    {
        for (ListIterator it = l.listIterator(0); it.hasNext(); )
        {
            Vector v = (Vector)it.next();
            
            if (v.get(1) == jt)
            {
                return it.previousIndex();
            }
        }
        
        return -1;
        
    }
    
    public JMenuItem getJMI(int idx)
    {
        Vector v = (Vector)l.get(idx);
        if (v == null)
            return null;
        return (JMenuItem)v.get(0);
    }
    
    public jTRACE getJT(int idx)
    {
        Vector v = (Vector)l.get(idx);
        if (v == null)
            return null;
        return (jTRACE)v.get(1);
    }
    
    
}

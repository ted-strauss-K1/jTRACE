/*
 * MouseOverHintManager.java
 *
 * Created on May 16, 2005, 1:59 PM
 */

package edu.uconn.psy.jtrace.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author http://techrepublic.com.com/5100-22_11-5185994.html?tag=nl.e027
 */
public class MouseOverHintManager implements MouseListener {
    
    private Map hintMap;
    private JLabel hintLabel;



      public MouseOverHintManager( JLabel hintLabel ) {

        hintMap = new WeakHashMap();

        this.hintLabel = hintLabel;

      }



      public void addHintFor( Component comp, String hintText ) {

        hintMap.put( comp, hintText );

      }



      public void enableHints( Component comp ) {

        comp.addMouseListener( this );

        if ( comp instanceof Container ) {

          Component[] components = ((Container)comp).getComponents();

          for ( int i=0; i<components.length; i++ )

           enableHints( components[i] );

        }

        if ( comp instanceof MenuElement ) {

          MenuElement[] elements = ((MenuElement)comp).getSubElements();

          for ( int i=0; i<elements.length; i++ )

           enableHints( elements[i].getComponent() );

        }

      }



      private String getHintFor( Component comp ) {

        String hint = (String)hintMap.get(comp);

        if ( hint == null ) {

          if ( comp instanceof JLabel )

           hint = (String)hintMap.get(((JLabel)comp).getLabelFor());

          else if ( comp instanceof JTableHeader )

           hint = (String)hintMap.get(((JTableHeader)comp).getTable());

        }

        return hint;

      }





      public void mouseEntered( MouseEvent e ) {

        Component comp = (Component)e.getSource();

        String hint;

        do {

          hint = getHintFor(comp);

          comp = comp.getParent();

        } while ( (hint == null) && (comp != null) );

        if ( hint != null )

          hintLabel.setText( hint );

      }



      public void mouseExited( MouseEvent e ) {

        hintLabel.setText( " " );

      }



      public void mouseClicked( MouseEvent e ) {}

      public void mousePressed( MouseEvent e ) {}

      public void mouseReleased( MouseEvent e ) {}
    
}

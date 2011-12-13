/*
 * FeatureSimGraph.java
 *
 * Created on May 10, 2005, 5:45 PM
 */

package edu.uconn.psy.jtrace.UI;

import java.awt.*;
import java.awt.geom.*;
import edu.uconn.psy.jtrace.Model.*;

/**
 *
 * @author harlan
 */
public class FeatureSimGraph extends AbstractSimGraph 
{
    
    /** Creates a new instance of FeatureSimGraph */
    public FeatureSimGraph(TraceParam _tp, double [][] _d, int _n, int _i,
            double _min, double _max) 
    {
        super(_tp, _d, _n, _i, _min, _max);
        
        xAxisLabel = "Temporal Alignment";
        yAxisLabel = "Feature Continua";
        title = "Feature Activations";
        
    }
    
    /** 
     * Over-ride abstract method.
     */
    protected void plotTicks(Rectangle plotRect, Graphics2D g2)
    {
        int tickLength = (int)(szSmallBuf * .7);
        
        double boxHeight = plotRect.getHeight() / rows;
        double boxWidth = plotRect.getWidth() / cols;
       
        g2.setColor(Color.BLACK);
        g2.setFont(fTickLabel);
        
        // foreach row, plot a tick mark. 
        // foreach col, plot a tick mark.
        // and plot 15 X tick labels
        
        // Y tick labels are difficult:
        // for each type of feature, plot a label and edges
        String[] fts={"POW", "VOC", "DIF", "ACU", "GRD", "VOI", "BUR"};
            
        
        // plot if col % colLabelInterval == 0
        int colLabelInterval = (int)(cols / 15);
        
        // foreach feature
        for (int f = 0; f < fts.length; f++)
        {
            // figure out the top and bottom row of the feature and the corresponding positions
            int yTopTick = (int)Math.round(plotRect.getY() + f * plotRect.getHeight() / fts.length);
            int yBottomTick = (int)Math.round(plotRect.getY() + (f+1) * plotRect.getHeight() / fts.length);
            // great, and the label will be centered between
            
            // draw the tick marks (bottom only for the last feature)
            g2.drawLine((int)Math.round(plotRect.getX() - tickLength), yTopTick, 
                (int)Math.round(plotRect.getX()), yTopTick);
            if (f == fts.length-1)
                g2.drawLine((int)Math.round(plotRect.getX() - tickLength), yBottomTick, 
                    (int)Math.round(plotRect.getX()), yBottomTick);
            
            // and the tick label
            
            // compute the size of this label
            String label = fts[f];
            Rectangle2D labelRect =(g2.getFontMetrics(fTickLabel)).getStringBounds(label, g2);

            // lower-left of label is a szSmallBuf left of the plot and centered between its tickmarks
            int xLabelPos = (int)Math.round(plotRect.getX() - szSmallBuf);
            int yLabelPos = (int)Math.round(labelRect.getWidth() / 2 + (yTopTick + yBottomTick) / 2);

            g2.rotate(-java.lang.Math.PI/2, xLabelPos, yLabelPos);
            g2.drawString(label, xLabelPos, yLabelPos);
            g2.rotate(java.lang.Math.PI/2, xLabelPos, yLabelPos);
            
        }
        
        // this is the same as in AbstractSimGraph
        for (int col = 0; col < cols; col++)
        {
            // position of the left of each column
            int xPos = (int)Math.round(plotRect.getX() + (col * boxWidth));
            
            // draw the tick mark
            g2.drawLine(xPos, (int)Math.round(plotRect.getY() + plotRect.getHeight()),
                    xPos, (int)Math.round(plotRect.getY() + plotRect.getHeight() + tickLength));
            
            if (col % colLabelInterval == 0)
            {
                // compute the size of this label
                String label = Integer.toString(col);
                Rectangle2D labelRect =(g2.getFontMetrics(fTickLabel)).getStringBounds(label, g2);
                
                // lower-left of label is a szSmallBuf + szTickLabel below the plot and centered on its tickmark
                int xLabelPos = (int)Math.round(xPos - labelRect.getWidth() / 2);
                int yLabelPos = (int)Math.round(plotRect.getY() + plotRect.getHeight() + szSmallBuf + szXTickLabel);
                
                g2.drawString(Integer.toString(col), xLabelPos, yLabelPos);
                
            }
        }
    }
    
}

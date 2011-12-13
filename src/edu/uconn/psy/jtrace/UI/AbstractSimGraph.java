/*
 * AbstractSimGraph.java
 *
 * Created on May 10, 2005, 4:55 PM
 */

package edu.uconn.psy.jtrace.UI;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.JPanel;
import java.text.DecimalFormat;
import edu.uconn.psy.jtrace.Model.*;



/**
 * This is a re-write of WebTraceGraphViewer.
 *
 * @author harlan
 */
public abstract class AbstractSimGraph extends JPanel 
{
    //
    TraceParam tp;
    
    // decorations
    protected String xAxisLabel;
    protected String yAxisLabel;
    protected String title;
    // plotting information about these
    protected Rectangle2D titleRect;
    protected Rectangle2D xAxisRect;
    protected Rectangle2D yAxisRect;
    
    // the data
    protected double [][] data;
    protected int rows;
    protected int cols;
    
    // competition data (re: word, phoneme plots)
    //contains indexes (x,y) pointing to cells in the data array
    //the first pair {(0,0) (0,1)} is the target, and all others are competitors.
    protected int [][] competitionData; 
    protected boolean plotCompetition = false;
    protected int focusRow;
    
    
    // frame stuff
    protected int currFrame;
    protected int numFrames;
    protected int interval;
    
    // fonts
    protected Font fAxisLabel;
    protected Font fTickLabel;
    protected Font fTitle;
    
    // size of fixed components
    protected int szBigBuf;
    protected int szSmallBuf;
    protected int szAxisLabel;
    protected int szXTickLabel;   
    protected int szYTickLabel;   // may be rotated
    protected int szTitle;
    
    // min and max values
    protected double maxVal;
    protected double minVal;
    
    // last known plot coordinates
    protected Rectangle plotRect;
    
    // formatter for query number
    private DecimalFormat formQuery;
    
   
    
    /** Creates a new instance of AbstractSimGraph */
    public AbstractSimGraph(TraceParam _tp, double [][] _d, int _n, int _i, double _min, double _max) 
    {
        tp = _tp; 
        
        data = _d;
        rows = data.length;
        cols = data[0].length;
        
        numFrames = _n;
        if(_i<0) interval=1;
        else interval = _i;
        
        minVal = _min;
        maxVal = _max;
        
        setupFonts();
        
        // set up a formatter to display doubles
        formQuery = new DecimalFormat("0.0000");
        
    }
    
    protected void setupFonts()
    {
        fAxisLabel = new Font("SansSerif", Font.BOLD, 12);
        fTickLabel = new Font("SansSerif", Font.BOLD, 10);
        fTitle = new Font("SansSerif", Font.BOLD, 16);
    }
    
    /**
     * 
     */
    protected void getFixedSizes(Graphics g)
    {
        FontMetrics fm;
        
        fm = g.getFontMetrics(fAxisLabel);
        szAxisLabel = fm.getHeight();
        fm = g.getFontMetrics(fTickLabel);
        szXTickLabel = fm.getHeight();
        szYTickLabel = szXTickLabel;    // override for rotated Y tick labels
        fm = g.getFontMetrics(fTitle);
        szTitle = fm.getHeight();
        szBigBuf = szXTickLabel;
        szSmallBuf = szXTickLabel / 3;
        
    }
    
    /**
     * Paint the screen!
     */
    public void paintComponent(Graphics g)
    {   
        // first time only
        if (szAxisLabel == 0)
        {
            getFixedSizes(g);
            
            // get label rectangles
            FontMetrics fm;
            fm = g.getFontMetrics(fTitle);
            titleRect = fm.getStringBounds(title, g);
            fm = g.getFontMetrics(fAxisLabel);
            xAxisRect = fm.getStringBounds(xAxisLabel, g);
            yAxisRect = fm.getStringBounds(yAxisLabel, g);
        }
        
        // paint the default
        super.paintComponent(g);   
        
        // we need a 2D for some things
        Graphics2D g2 = (Graphics2D)g;
        
        // how big are we?
        Dimension size = getSize();
        
        // compute location and size of plot rectangle
        int x = szSmallBuf + szAxisLabel + szSmallBuf + szYTickLabel + szSmallBuf;
        int y = szSmallBuf + szTitle + szSmallBuf;
        plotRect = new Rectangle(x, y, (int)(size.getWidth() - x - szBigBuf),
                (int)(size.getHeight() - y - (szSmallBuf + szAxisLabel + szSmallBuf +
                szXTickLabel + szSmallBuf)));
        
        // plot labels
        plotLabels(plotRect, g2);
        
        // plot ticks
        plotTicks(plotRect, g2);
        
        // plot data
        plotData(plotRect, g2);
        
        // plot competitors
        //System.out.print(getClass().getName()+"\t"+plotCompetition);
        if(plotCompetition&&(getClass().getName().endsWith("WordSimGraph")||getClass().getName().endsWith("PhonemeSimGraph")))                    
            if(competitionData!=null&&competitionData.length>0){
                plotCompetitors(plotRect, g2);
                //System.out.println("\tplotting");
            }
            else{
                //System.out.println("\tnot plotting");
            }
        
        // plot edge box
        g2.setColor(Color.BLACK);
        g2.draw(plotRect);
    }
    
    /** 
     * Plot the tick marks and what-not. The abstract version plots tick marks
     * and just numbers for labels.
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
        // and plot 10 Y tick labels and 20 X tick labels
        
        // plot if row % rowLabelInterval == 0, and likewise
        int rowLabelInterval = (int)(rows / 10);
        int colLabelInterval = (int)(cols / 15);
        
        for (int row = 0; row < rows; row++)
        {
            // position of the top of each row
            int yPos = (int)Math.round(plotRect.getY() + (row * boxHeight));
            
            // draw the tick mark
            g2.drawLine((int)Math.round(plotRect.getX() - tickLength), yPos, 
                    (int)Math.round(plotRect.getX()), yPos);
            
            if (row % rowLabelInterval == 0)
            {
                // compute the size of this label
                String label = Integer.toString(row);
                Rectangle2D labelRect =(g2.getFontMetrics(fTickLabel)).getStringBounds(label, g2);
                
                // lower-left of label is a szSmallBuf left of the plot and centered on its tickmark
                int xLabelPos = (int)Math.round(plotRect.getX() - szSmallBuf);
                int yLabelPos = (int)Math.round(yPos + labelRect.getWidth() / 2);
                
                g2.rotate(-java.lang.Math.PI/2, xLabelPos, yLabelPos);
                g2.drawString(label, xLabelPos, yLabelPos);
                g2.rotate(java.lang.Math.PI/2, xLabelPos, yLabelPos);
            }
        }
        
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
                
                g2.drawString(label, xLabelPos, yLabelPos);
                
            }
        }
    }
    
    /**
     * Plot the title and X and Y axis labels.
     *
     * @param plotRect      rectangle defining the relative location of the box in the panel
     * @param g2            graphics context
     */
    protected void plotLabels(Rectangle plotRect, Graphics2D g2)
    {
        // title is centered above the plotRect
        g2.setColor(Color.BLACK);        
        g2.setFont(fTitle);        
        g2.drawString(title, (int)(plotRect.getX() + (plotRect.getWidth()/2) - titleRect.getWidth()/2),
                (int)(szSmallBuf + titleRect.getHeight()));
        // x axis label is centered below the plotRect
        g2.setFont(fAxisLabel);
        g2.drawString(xAxisLabel, (int)(plotRect.getX() + (plotRect.getWidth()/2) - xAxisRect.getWidth()/2),
                (int)(plotRect.getY() + plotRect.getHeight() + szSmallBuf + szXTickLabel + szSmallBuf + xAxisRect.getHeight()));
        // y axis label is rotated and centered left of the plotRect
        int xYPos = (int)(szSmallBuf + yAxisRect.getHeight());
        int yYPos = (int)(plotRect.getY() + (plotRect.getHeight()/2) + (yAxisRect.getWidth()/2));
        g2.rotate(-java.lang.Math.PI/2, xYPos, yYPos);
        g2.drawString(yAxisLabel, xYPos, yYPos);
        g2.rotate(java.lang.Math.PI/2, xYPos, yYPos);
    }
    
    /**
     * Plot just the stuff inside the box.
     *
     * @param plotRect      rectangle defining the relative location of the box in the panel
     * @param g2            graphics context
     */
    protected void plotData(Rectangle plotRect, Graphics2D g2)
    {
        if (data == null)
            return;
        
        double boxHeight = plotRect.getHeight() / rows;
        double boxWidth = plotRect.getWidth() / cols;
        for (int row = 0; row < data.length; row++)
        {
            int yPos = (int)Math.round(plotRect.getY() + (row * boxHeight));
            
            for (int col = 0; col < data[row].length; col++)
            {
                int xPos = (int)Math.round(plotRect.getX() + (col * boxWidth));
            
                // compute value between 0 and 255
                int value;
                
                if (data[row][col] < 0) 
                    value = 0;       // threshold at 0
                else
                    value = (int)Math.round(255f * (data[row][col] - minVal) / (maxVal - minVal));
                
                // construct a greyscale color
                Color c = new Color((255-value), (255-value), (255-value));
                    
                // draw the box
                g2.setColor(c);
                g2.fill(new Rectangle(xPos, yPos, (int)(boxWidth+1), (int)(boxHeight+1)));                                
                    
            }
        }
    }
    
    /**
     * Plot information about competition between phoneme/word units.
     *
     * @param plotRect      rectangle defining the relative location of the box in the panel
     * @param g2            graphics context
     */
    protected void plotCompetitors(Rectangle plotRect, Graphics2D g2)
    {
        if (competitionData == null||data == null)
            return;
        
        double boxHeight = plotRect.getHeight() / rows;
        double boxWidth = plotRect.getWidth() / cols;
        for (int i = 0; i < competitionData.length; i++)
        {
            int yPos = (int)Math.round(plotRect.getY() + (i * boxHeight));            
            int xPos = (int)Math.round(plotRect.getX() + (competitionData[i][1] * boxWidth));

            // compute value between 0 and 255
            double value = ((double)competitionData[i][2]/(double)100);
            
            // draw the box            
            if(i==focusRow)
                g2.setColor(Color.GREEN);
            else{
                //Orange: 255,200,000
                //Maroon: 255,000,000
                g2.setColor(new Color(255, 200-(int)(value*200d), 0));
            }
            g2.fill(new Rectangle(xPos, yPos, (int)(boxWidth+1), (int)(boxHeight+1)));                                                    
            //g2.draw(new Rectangle(xPos, yPos, (int)(boxWidth+1), (int)(boxHeight+1)));                                                    

        }
    }
    
    public void drawData(double[][] d)
    {
        data=d;
        focusRow=-1;
        repaint();
    }
    public void drawData(double[][] d, int r)
    {
        data=d;
        focusRow=r;
        repaint();
    }
    
    public boolean mouseOverGrid(java.awt.event.MouseEvent evt)
    {
        
        if(evt==null||plotRect==null) return false;
            
        // calculate the size of plot boxes
        double boxHeight = plotRect.getHeight() / rows;
        double boxWidth = plotRect.getWidth() / cols;
                
        // find out where we are on the panel
        int xgPos = (int)Math.round(evt.getX() - plotRect.getX());   // relative to upper-left of graph
        int ygPos = (int)Math.round(evt.getY() - plotRect.getY());
        
        // if not on the graph, go no farther
        if (xgPos < 0 || ygPos < 0 || xgPos > plotRect.getWidth() || ygPos > plotRect.getHeight())
            return false;
        else
            return true;        
    }
    public int[] queryGrid(java.awt.event.MouseEvent evt){
        // calculate the size of plot boxes
        double boxHeight = plotRect.getHeight() / rows;
        double boxWidth = plotRect.getWidth() / cols;
        
        // initialize 
        int[] result = new int[2];
        
        if (data == null)
            return null;
        
        // find out where we are on the panel
        int xgPos = (int)Math.round(evt.getX() - plotRect.getX());   // relative to upper-left of graph
        int ygPos = (int)Math.round(evt.getY() - plotRect.getY());
        
        // if not on the graph, go no farther
        if (xgPos < 0 || ygPos < 0 || xgPos > plotRect.getWidth() || ygPos > plotRect.getHeight())
            return null;
        
        // get the activation level at this location
        int xCell = (int)(xgPos / boxWidth); //alignment
        int yCell = (int)(ygPos / boxHeight); //item
        
        result[0] = xCell;
        result[1] = yCell;
        
        return result;   
    }
    
    public String queryDataCell(java.awt.event.MouseEvent evt)
    {
        // calculate the size of plot boxes
        double boxHeight = plotRect.getHeight() / rows;
        double boxWidth = plotRect.getWidth() / cols;
        
        // initialize result string
        String result;
        if (getClass().getName().indexOf("Input") != -1) result = "Input ";
        else if(getClass().getName().indexOf("Feature") != -1) result = "Feature ";
        else if(getClass().getName().indexOf("Phoneme") != -1) result = "Phoneme ";
        else if(getClass().getName().indexOf("Word") != -1) result = "Word ";
        else result = getClass().getName();
        
        if (data == null)
            return "Graph activation info.";
        
        // find out where we are on the panel
        int xgPos = (int)Math.round(evt.getX() - plotRect.getX());   // relative to upper-left of graph
        int ygPos = (int)Math.round(evt.getY() - plotRect.getY());
        
        // if not on the graph, go no farther
        if (xgPos < 0 || ygPos < 0 || xgPos > plotRect.getWidth() || ygPos > plotRect.getHeight())
            return "Graph activation info.";
        
        // get the activation level at this location
        int xCell = (int)(xgPos / boxWidth);
        int yCell = (int)(ygPos / boxHeight);
        // there's a subtle off-by-one error than can occur... catch it
        double value;
        try
        {
            value = data[yCell][xCell];  // swapped! x = time, y = item
        }
        catch (java.lang.ArrayIndexOutOfBoundsException ex) {value = minVal;}
        
        // construct a return string
        String displayValue = formQuery.format(value);
        if (getClass().getName().indexOf("Input") != -1)
        {
            result += "Activation at (" + xCell + "," + yCell + ") =" + displayValue;
        }
        else if(getClass().getName().indexOf("Feature") != -1)
        {
            result += "Activation at (" + xCell + "," + yCell + ") =" + displayValue;
        }
        else if(getClass().getName().indexOf("Phoneme") != -1)
        {
            if (yCell >= tp.getPhonology().getLabels().length)
                yCell = tp.getPhonology().getLabels().length - 1;  // fix boundary condition error
                
            result += "Activation at (" + xCell + "," + tp.getPhonology().getLabels()[yCell] + ") =" + displayValue;        
        
        }
        else if(getClass().getName().indexOf("Word") != -1)
        {
            if (yCell >= ((WordSimGraph)this).getWordLabels().length)
                yCell = ((WordSimGraph)this).getWordLabels().length - 1;
            result += "Activation at (" + xCell + "," + ((WordSimGraph)this).getWordLabels()[yCell] + ") =" + displayValue;
        }
        
        
        return result;
    }
    public int[][] queryCompetition(java.awt.event.MouseEvent evt)
    {
        int[][] result = new int[10][2];
        
        // calculate the size of plot boxes
        double boxHeight = plotRect.getHeight() / rows;
        double boxWidth = plotRect.getWidth() / cols;
                
        if (data == null)
            return null;
        
        // find out where we are on the panel
        int xgPos = (int)Math.round(evt.getX() - plotRect.getX());   // relative to upper-left of graph
        int ygPos = (int)Math.round(evt.getY() - plotRect.getY());
        
        // if not on the graph, go no farther
        if (xgPos < 0 || ygPos < 0 || xgPos > plotRect.getWidth() || ygPos > plotRect.getHeight())
            return null;
        
        // get the activation level at this location
        int xCell = (int)(xgPos / boxWidth);
        int yCell = (int)(ygPos / boxHeight);
        
        result[0][0] = yCell; 
        result[0][1] = xCell; 
                
        //now select top items that are within 1 alignment of target
        int h=1;
        H: while(h<10){        
            I: for(int i=0;i<data.length;i++)
                J: for(int j=0;j<data[i].length;j++) {
                    if(data[i][j]>0.1){
                        result[h][0] = i;
                        result[h][1] = j;                    
                        h++;
                        if(h>=10) return result;
                        continue I;
                    }
                }
        }
        return result;
    }
    
    public void setCompetitionData(int[][] _c){
        competitionData=_c;
    }
    public void setPlotCompetition(boolean _b){
        plotCompetition=_b;
    }
            
    //Frame methods
    public void incrementFrame(){
        if(currFrame>=(numFrames*interval)) return;
        else currFrame+=interval;
    }
    public void decrementFrame(){
        if(currFrame<=0) return;
        else currFrame-=interval;
    }
    public void setCurrFrame(int i){
        if(i>=numFrames||i<0) return;
        else currFrame=i;
    }
    public void setMin(double _m){
        minVal=_m;
    }
    public void setMax(double _m){
        maxVal=_m;
    }
    public boolean isAtEnd(){return (currFrame>=numFrames);}                 
    public int getCurrFrame(){return currFrame;}
    public int getNumFrames(){return numFrames;}
    public double[][] getData(){ return data;}
    public void setData(double[][] _d){data=_d;}
}

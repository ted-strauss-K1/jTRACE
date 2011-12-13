/*
 * GraphParameters.java
 *
 * Created on May 9, 2005, 5:48 PM
 */

package edu.uconn.psy.jtrace.UI;

import org.jfree.data.*;


/**
 * A GraphParameters stores the information about how to render the results of a
 * TraceSimAnalysis for display in the GraphPanel.
 *
 * @author Harlan Harris
 */
public class GraphParameters {
    // X and Y-axis ranges    
    Range xRange;
    Range yRange;
    // X and Y-axis labels
    String xLabel;
    String yLabel;
    // title
    String graphTitle;
    // relative input label position (0-100)
    int inputPosition;
    boolean includeInputAnnotation;
    
    /** Creates a new instance of GraphParameters */
    public GraphParameters() 
    {
        reset();
    }
    public void reset(){
        xRange = null;
        yRange = null;
        xLabel = "Cycle";
        yLabel = "Activation / Response Strength";
        graphTitle = null;
        inputPosition = 90;
        includeInputAnnotation = true;
    }
    
    /**
     * Copy constructor.
     */
    public GraphParameters(GraphParameters old)
    {
        if (old.xRange != null)
            xRange = new Range(old.xRange.getLowerBound(), old.xRange.getUpperBound());
        if (old.yRange != null)
            yRange = new Range(old.yRange.getLowerBound(), old.yRange.getUpperBound());
        
        if (old.xLabel != null)
            xLabel = new String(old.xLabel);
        if (old.yLabel != null)
            yLabel = new String(old.yLabel);
        
        if (old.graphTitle != null)
            graphTitle = new String(old.graphTitle);
        
        inputPosition = old.inputPosition;
        includeInputAnnotation = old.includeInputAnnotation;
    }

    /**
     * Getter for property xRange.
     * @return Value of property xRange.
     */
    public Range getXRange() {

        return this.xRange;
    }

    /**
     * Setter for property xRange.
     * @param xRange New value of property xRange.
     */
    public void setXRange(Range xRange) {
        
        this.xRange = xRange;        
    }
    public void setXRange(double left, double right) {
        
        this.xRange = new Range(left,right);        
    }
    /**
     * Getter for property yRange.
     * @return Value of property yRange.
     */
    public Range getYRange() {

        return this.yRange;
    }
    public void setYRange(double bottom, double top) {
        
        this.yRange = new Range(bottom,top);        
    }
    /**
     * Setter for property yRange.
     * @param yRange New value of property yRange.
     */
    public void setYRange(Range yRange) {

        this.yRange = yRange;
    }

    /**
     * Getter for property xLabel.
     * @return Value of property xLabel.
     */
    public String getXLabel() {

        return this.xLabel;
    }

    /**
     * Setter for property xLabel.
     * @param xLabel New value of property xLabel.
     */
    public void setXLabel(String xLabel) {

        this.xLabel = xLabel;
    }

    /**
     * Getter for property yLabel.
     * @return Value of property yLabel.
     */
    public String getYLabel() {

        return this.yLabel;
    }

    /**
     * Setter for property yLabel.
     * @param yLabel New value of property yLabel.
     */
    public void setYLabel(String yLabel) {

        this.yLabel = yLabel;
    }

    /**
     * Getter for property graphTitle.
     * @return Value of property graphTitle.
     */
    public String getGraphTitle() {

        return this.graphTitle;
    }

    /**
     * Setter for property graphTitle.
     * @param graphTitle New value of property graphTitle.
     */
    public void setGraphTitle(String graphTitle) {

        this.graphTitle = graphTitle;
    }
    /**
     * Getter for property inputPosition.
     * @return Value of property inputPosition.
     */
    public int getInputPosition() {

        return this.inputPosition;
    }

    /**
     * Setter for property inputPosition.
     * @param inputPosition New value of property inputPosition.
     */
    public void setInputPosition(int inputPosition) {

        if (inputPosition >= 0 && inputPosition <= 100)
            this.inputPosition = inputPosition;
    }
    
    /**
     * Getter for property includeInputAnnotation.
     * @return Value of property includeInputAnnotation.
     */
    public boolean getIncludeInputAnnotation() {

        return this.includeInputAnnotation;
    }

    /**
     * Setter for property includeInputAnnotation.
     * @param inputPosition New value of property includeInputAnnotation.
     */
    public void setIncludeInputAnnotation(boolean includeInputAnnotation) {

        this.includeInputAnnotation = includeInputAnnotation;
    }
    
    public String XMLTag(){
        String result="";
        if(null!=xRange) result+="<action><set-graph-x-axis-bounds><left-bound>"+xRange.getLowerBound()+"</left-bound><right-bound>"+xRange.getUpperBound()+"<right-bound></set-graph-x-axis-bounds></action>";
        if(null!=yRange) result+="<action><set-graph-y-axis-bounds><lower-bound>"+yRange.getLowerBound()+"</lower-bound><upper-bound>"+yRange.getUpperBound()+"<upper-bound></set-graph-y-axis-bounds></action>";                
        if(null!=graphTitle) result+="<action><set-graph-title><title>"+graphTitle+"</title></set-graph-title></action>";
        if(!xLabel.equals("X")) result+="<action><set-graph-x-axis-label><label>"+xLabel+"</label></set-graph-x-axis-label></action>";
        if(!yLabel.equals("Y")) result+="<action><set-graph-y-axis-label><label>"+yLabel+"</label></set-graph-y-axis-label></action>";
        if(inputPosition != 90) result+="<action><set-graph-input-position><position>"+inputPosition+"</position></set-graph-input-position></action>";
        return result;
    }
    
}

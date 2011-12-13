/*
 * TraceDecisionRule.java
 *
 * Created on May 26, 2005, 3:42 PM
 */

package edu.uconn.psy.jtrace.Model;
import java.util.Vector;
import java.io.*;
import java.util.*;
import org.jfree.data.xy.*;

/**
 *
 * @author tedstrauss
 */
public class TraceDecisionRule {
    XYSeriesCollection data;

    /** Creates a new instance of TraceDecisionRule */
    public TraceDecisionRule(XYSeriesCollection _data) {
        data=_data;                
    }
    /*public String itemWithNthHighestPeak(int n){
        TreeMap peaks = peakResults();
        peakResult next = new peakResult();
        for(int i=0;peaks.size()>0&&i<n;i++){
            next=(peakResult)(peaks.remove(peaks.firstKey()));
        }
        return next.name;        
    } */  
    public String decisionResult(double thresh){
        TreeMap decisions = decisionResults(thresh);
        String result;
        if(decisions.size()>0)
            result = ((decisResult)decisions.firstKey()).name;
        else
            result = "(null)";
        return result;
    }
    public String itemWithHighestPeak(){
        String result="";
        double max = data.getSeries(0).getDataItem(0).getY().doubleValue();
        for(int i=0;i<data.getSeriesCount();i++)
            for(int j=0;j<data.getSeries(i).getItemCount();j++){
                if(data.getSeries(i).getDataItem(j).getY().doubleValue() > max){
                    max = data.getSeries(i).getDataItem(j).getY().doubleValue();
                    result = data.getSeriesName(i);
                }
            }
        return result;
    }
    public double highestPeak(){
        
        double max = data.getSeries(0).getDataItem(0).getY().doubleValue();
        for(int i=0;i<data.getSeriesCount();i++)
            for(int j=0;j<data.getSeries(i).getItemCount();j++){
                if(data.getSeries(i).getDataItem(j).getY().doubleValue() > max){
                    max = data.getSeries(i).getDataItem(j).getY().doubleValue();        
                }
            }
        return max;
    }
    public int rtForItem(String target,double threshold){
        if(target.startsWith("-")&&target.length()>1){
            if(target.equals("-"))
                target="-";
            else if(target.split("-").length<=0)
                target="-";
            else
                target = target.split("-")[1];
        }
        int targetSeries=-1;
        for(int i=0;i<data.getSeriesCount();i++)
            if(data.getSeriesName(i).equals(target)){
                targetSeries=i;
                break;
            }
       
        if(targetSeries==-1) return -1;
        for(int j=0;j<data.getSeries(targetSeries).getItemCount();j++)
            if(data.getSeries(targetSeries).getDataItem(j).getY().doubleValue() > threshold)
                return j;                    
        return -1;
    }
    public int peakRtForItem(String target){
        if(target.startsWith("-")&&target.length()>1){
            target = target.split("-")[1];
        }
        int targetSeries=-1;
        for(int i=0;i<data.getSeriesCount();i++)
            if(data.getSeriesName(i).equals(target)){
                targetSeries=i;
                break;
            }       
        if(targetSeries==-1) return -1;
        int peakCycle=0;
        for(int j=0;j<data.getSeries(targetSeries).getItemCount();j++)
            if(data.getSeries(targetSeries).getDataItem(j).getY().doubleValue() > data.getSeries(targetSeries).getDataItem(peakCycle).getY().doubleValue())
                peakCycle=j;                    
        return peakCycle;
    }    
    public String nthItemToThreshold(int n,double threshold){
        String result="";
        TreeMap decisions = decisionResults(threshold);
        Iterator iter = decisions.values().iterator();        
        decisResult next = new decisResult();        
        if(decisions.size()>0){
            /*for(int i=0;i<n&&decisions.size()>0;i++){
                next = (decisResult)(decisions.remove(decisions.firstKey()));                
            }*/
            for(int i=0;i<n&&iter.hasNext();i++){
                next = (decisResult)iter.next();
            }
            if(null!=next)
                result = next.name;
            else
                result+="(null)";
        }
        else{
            result+="(null)";
            return result;
        }        
        return result;
    }
    public double peakValueOfItem(String item){
        int targetSeries=-1;
        if(item.startsWith("-")&&item.length()>1){
            item = item.split("-")[1];
        }
        //System.out.println("peakValueOfItem("+item+")");
        for(int i=0;i<data.getSeriesCount();i++)
            if(data.getSeriesName(i).equals(item)){
                targetSeries=i;
                break;
            }
       
        if(targetSeries==-1) return 0d;
        double max=data.getSeries(targetSeries).getDataItem(0).getY().doubleValue();
        for(int j=0;j<data.getSeries(targetSeries).getItemCount();j++)
            if(data.getSeries(targetSeries).getDataItem(j).getY().doubleValue() > max)
                max = data.getSeries(targetSeries).getDataItem(j).getY().doubleValue();
        return max;
    }
    public String nthHighestPeakItem(int n){
        String result="";
        TreeMap peaks = peakResults();                
        Iterator iter = peaks.values().iterator();
        iter = peaks.values().iterator();        
        peakResult next = new peakResult();        
        if(peaks.size()>0){            
            for(int i=0;i<n&&iter.hasNext();i++){
                next=(peakResult)(iter.next());
                //System.out.print(next.name+"  ");
            }   
            //System.out.println();
            if(null!=next)
                result = next.name;
            else
                result+="(null)";            
            return result;
        }
        else{            
            return "(null)";
        }                
    }
    public double nthHighestPeakValue(int n){
        double result=0d;
        TreeMap peaks = peakResults();        
        Iterator iter = peaks.values().iterator();
        peakResult next = new peakResult();        
        if(peaks.size()>0){            
            for(int i=0;i<n&&iter.hasNext();i++){
                next=(peakResult)(iter.next());
            }           
            if(null!=next)
                result = next.peak;
            else
                result+=-1d;            
        }
        else{
            result=-1d;
            return result;
        }        
        return result;
    }           
    //See McClelland 1991 for details.
    //given two phonemes and some parameters, returns which of the two phonemes is most active according to the rule.
    //result is of the form: model_input    phon1   win/lose    phon2   win/lose 
    //winner=1, loser=0.
    public String runningAverageMetricReport(String phon1, String phon2, String input, double lambda, double init,String[] labels){        
        int p1_idx=-1, p2_idx=-1;        
        for(int i=0;i<labels.length;i++){
            if(labels[i].equals(phon1))
                p1_idx=i;
            if(labels[i].equals(phon2))
                p2_idx=i;                            
        }
        //case: one or both of the phonemes was not found in the array of labels.
        if(p1_idx==-1||p2_idx==-1)
            return "";
        double p1_val=init,p2_val=init;
        int length = Math.min(data.getItemCount(p1_idx),data.getItemCount(p2_idx));
        for(int i=0;i<length;i++){
            p1_val = (lambda*data.getY(p1_idx, i).doubleValue())+((1-lambda)*p1_val);
        p2_val = (lambda*data.getY(p2_idx, i).doubleValue())+((1-lambda)*p2_val);
        }
        String result=input+"\t";
        result+=phon1+"\t";
        if(p1_val>p2_val) result+="1\t";
        else result+="0\t";
        result+=phon2+"\t";
        if(p2_val>p1_val) result+="1\t";
        else result+="0\t";    
        return result;
    }            
    public String decisionRuleReport(double thresh,String target){
        /*format of output:
          winning-item, thresh, rt, peak, peak-cycle, (#target-item, thresh, rt, peak, peak-cycle) 
            # first-item-to-thresh,second-item-to-thresh,...tenth-item-to-thresh BASTA
          three possible outcomes:
            1. target is recognized first: report its stats only. # #
            2. another item is recognized first: report the winner, # then target stats. #
            3. no item is recognized: # report the target's stats. #
          TODO: right now, the silence segment is always the winner: this could be prevented if we wanted
          TODO: instead of top-ten to thresh, top-ten peaks might be more useful
         */
        
        if(target.startsWith("-")&&target.length()>1){
            String[] split = target.split("-");
            if(split==null||split.length==0||(split.length==1&&split[0].equals("")))
                target="-";
            else{
                target = split[1];
            }
        }
        String result="target=\t"+target+"\t";
        TreeMap decisions = decisionResults(thresh);
        decisResult next;
        //record winning-item stats
        if(decisions.size()>0){
            next = (decisResult)(decisions.remove(decisions.firstKey()));
            result += next.name+"\t"+next.thresh+"\t";
            if(next.recogRt==12345)
                result+="\\N\t";
            else
                result+=next.recogRt+"\t";
            result +=next.peak+"\t"+next.peakRt+"\t";
        }
        //no decision results!  
        else{
            result+="(null)";
            //System.out.println(result);
            return result;
        }
        result += "#\t";
        //is the winning item the target?  
        if(next.name.equals(target)){
           //yes : great! don't report anything else
        }
        else{
            //if not, find the target and report its stats.
            while(decisions.size()>0){
                next = (decisResult)(decisions.remove(decisions.firstKey()));
                if(next.name.equals(target)){
                    result += next.name+"\t"+next.thresh+"\t";
                    if(next.recogRt==12345)
                        result+="-1\t";
                    else
                        result+=next.recogRt+"\t";
                    result +=next.peak+"\t"+next.peakRt;
                    break;
                }
            }
        }
        decisions = decisionResults(thresh);
        result += "#\t";
        for(int i=0;decisions.size()>0&&i<10;i++){
                next = (decisResult)(decisions.remove(decisions.firstKey()));
                result += next.name+"\t";
        }
        //System.out.println(result);
        return result;
    }
    private TreeMap decisionResults(double thresh){
        TreeMap map = new TreeMap();
        decisResult res;
        int peakRt = 12345, recogRt = 12345;
        double peak = -1;
        boolean recognized = false;
        for(int i=0;i<data.getSeriesCount();i++){
            peakRt = 12345; 
            recogRt = 12345; 
            peak = data.getSeries(i).getDataItem(0).getY().doubleValue();
            recognized = false;
            for(int j=0;j<data.getSeries(i).getItemCount();j++){
                if(data.getSeries(i).getDataItem(j).getY().doubleValue() > peak){
                    peak = data.getSeries(i).getDataItem(j).getY().doubleValue();
                    peakRt = j;
                }
                if(data.getSeries(i).getDataItem(j).getY().doubleValue() >= thresh && !recognized){
                    recogRt = j;
                    recognized = true;
                }
            }
            System.out.println("put "+data.getSeriesName(i)+" "+recogRt+" "+peakRt+" "+thresh+" "+peak);
            map.put((new decisResult(data.getSeriesName(i),recogRt,peakRt,thresh,peak)),(new decisResult(data.getSeriesName(i),recogRt,peakRt,thresh,peak)));
        }   
        System.out.println("\n");
        //return ((Collection)map.values()).iterator();
        return map;
    }
    public class decisResult implements Comparable{
        String name; //is the word/phon label
        int recogRt;
        int peakRt;
        double thresh;
        double peak;
        
        public decisResult(String _n,int _rrt,int _prt,double _th, double _pk){
            name = _n;
            recogRt = _rrt;
            peakRt = _prt;
            thresh = _th;
            peak = _pk;                    
        }
        public decisResult(){}
        
        public int compareTo(Object o){
            //here the smallest RT is considered the largest item.
            int r;
            if(((decisResult)o).recogRt == this.recogRt) r = 0;
            else if(this.recogRt > ((decisResult)o).recogRt) r = +1;
            else //if(this.recogRt < ((decisResult)o).recogRt) 
                r = -1;
            return r;
        }        
    }
    private TreeMap peakResults(){
        TreeMap map = new TreeMap();
        peakResult res;
        int peakRt = 0;
        double peak = -1;
        for(int i=0;i<data.getSeriesCount();i++){
            peakRt = 0; 
            peak = data.getSeries(i).getDataItem(0).getY().doubleValue();
            for(int j=0;j<data.getSeries(i).getItemCount();j++){
                if(data.getSeries(i).getDataItem(j).getY().doubleValue() > peak){
                    peak = data.getSeries(i).getDataItem(j).getY().doubleValue();
                    peakRt = j;
                }                
            }            
            map.put(new peakResult(data.getSeriesName(i),peakRt,peak),new peakResult(data.getSeriesName(i),peakRt,peak));            
        }
        return map;
    }
    public class peakResult implements Comparable{
        String name; //is the word/phon label
        int peakRt;
        double peak;
        
        public peakResult(String _n,int _prt,double _pk){
            name = _n;
            peakRt = _prt;
            peak = _pk;                    
        }
        public peakResult(){}
        
        public int compareTo(Object o){
            //in order for the TreeMap class to not replace values
            //in the ADT, this comparable must be defined to distinguish
            //items which have the same peak value.
            int r;
            if(this.peak == ((peakResult)o).peak){ 
                //if peak and peakRt are the same, then compare using
                //the string.  
                if(this.peakRt == ((peakResult)o).peakRt){
                    return this.name.compareTo(((peakResult)o).name);
                }
                //if this peak occurs earlier (reakRt lower) then this
                //peak result is GREATER than than the argument.
                else if(this.peakRt < ((peakResult)o).peakRt) r = -1;
                else //if(this.peakRt > ((peakResult)o).peakRt) 
                    r = 1;
                r = 0;
            }
            //in most cases, the peak value suffices for the comparison:
            //greater peak value equals greater object
            else if(this.peak > ((peakResult)o).peak) r = -1;
            else //if(this.peak < ((peakResult)o1).peak) 
                r = 1;
            return r;
        }        
    }
}

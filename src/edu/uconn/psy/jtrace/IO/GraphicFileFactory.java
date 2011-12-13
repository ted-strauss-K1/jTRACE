/*
 * GraphicFileFactory.java
 *
 * Created on March 27, 2005, 2:34 PM
 */

package edu.uconn.psy.jtrace.IO;
/*
 * Imaging.java
 *
 * Created on March 22, 2005, 7:07 PM
 */

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import com.sun.image.codec.jpeg.*;
import com.sun.imageio.plugins.png.*;
import java.io.*;


/**
 *
 * @author  tedstrauss
 */
public class GraphicFileFactory {
    javax.swing.JComponent subject;
    
    //Component subject;
    /** Creates a new instance of Imaging */
    public GraphicFileFactory(javax.swing.JComponent _s) {
        subject=_s;        
    }
    
    private BufferedImage getBufferedImage(int _type) {
        BufferedImage _img; 
        Graphics2D _g2d;         
        //System.out.println(subject.getClass());
        if(subject.getClass().getName().equals("javax.swing.JPanel")){
            _img = new BufferedImage(700, 550, _type);        
            _g2d = _img.createGraphics();
            _g2d.setClip(0,0,550,550); 
            subject.getComponents()[0].setSize(550,550);
            subject.getComponents()[0].print(_g2d);       
            _g2d.setClip(550,0,250,550);             
            subject.getComponents()[1].setSize(250,550);
            _g2d.translate(550,0);
            subject.getComponents()[1].print(_g2d);       
        }
        else{// if(subject.getClass().getName().equals("")){
            _img = new BufferedImage(50, 50, _type);        
        } 
        //subject.print(_g2d);                    
        return _img;        
    }
    public void exportPNG(File path,String name) {
        BufferedImage _buf = getBufferedImage(BufferedImage.TYPE_INT_RGB);              
        try {
                
                File _f = new File(path,name.concat(".png"));                  
                //System.out.print(_f.getAbsolutePath());               
                if(!path.exists()) path.mkdirs();
                _f.createNewFile();                
                PNGImageWriterSpi _enc = new PNGImageWriterSpi();                
                PNGImageWriter pngW = new PNGImageWriter(_enc);
                pngW.setOutput(new javax.imageio.stream.FileImageInputStream(_f));
                pngW.write(_buf);
                ((FileOutputStream)pngW.getOutput()).flush();
                ((FileOutputStream)pngW.getOutput()).close();                                
                _buf.flush();       
        } catch(Exception e) {
                e.printStackTrace();
        }
    }
    public void exportJPEG(File path,String name) {
        BufferedImage _buf = getBufferedImage(BufferedImage.TYPE_INT_RGB);              
        try {
                File _f = new File(path,name.concat(".jpg"));                  
                //System.out.print(_f.getAbsolutePath());               
                if(!path.exists()) path.mkdirs();
                _f.createNewFile();                
                JPEGImageEncoder _enc = JPEGCodec.createJPEGEncoder(new FileOutputStream(_f));                                                                                                          
                
                _enc.encode(_buf);
                _enc.getOutputStream().flush();                
                _enc.getOutputStream().close();
                _buf.flush();       
        } catch(Exception e) {
                e.printStackTrace();
        } 
    } 
}

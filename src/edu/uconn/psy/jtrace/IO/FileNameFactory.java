/*
 * FileNameFactory.java
 *
 * Created on May 5, 2005, 2:49 PM
 */

package edu.uconn.psy.jtrace.IO;
import java.io.*;
import edu.uconn.psy.jtrace.Model.Scripting.*;        
/**
 *
 * @author tedstrauss
 */
public class FileNameFactory {
    edu.uconn.psy.jtrace.Model.TraceParam tp;
    public static String rootDirectory;
    
    /** Creates a new instance of FileNameFactory */
    public FileNameFactory() {
        tp = null;        
        if(rootDirectory==null)
            rootDirectory=edu.uconn.psy.jtrace.UI.traceProperties.rootPath.getAbsolutePath();                
    }
    /** Creates a new instance of FileNameFactory */
    public FileNameFactory(String rootDir) {
        tp = null;        
        if(rootDir==null)
            rootDirectory=edu.uconn.psy.jtrace.UI.traceProperties.rootPath.getAbsolutePath();
        else
            rootDirectory=rootDir;
    }
    public FileNameFactory(edu.uconn.psy.jtrace.Model.TraceParam _tp,String rootDir) {
        tp = _tp;
        if(rootDir==null)
            rootDirectory=edu.uconn.psy.jtrace.UI.traceProperties.rootPath.getAbsolutePath();
        else
            rootDirectory=rootDir;
    }
    
    public File makeScriptFile(FileLocator f){
        String name;
        File path;                
        path = new File(getResolvedPath(f));
        //name
        if(null==f.fileName())
            name = "script-"+getFileNameCount("script",path)+".jt";
        else{ 
            name = f.fileName();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,".");
            String nameId=tk.nextToken();        
            if(getFileNameCount(nameId,path).equals("000"))
                name = f.fileName().concat(".jt");
            else
                name = f.fileName()+"-"+getFileNameCount(nameId,path)+".jt";            
        }
        return new File(path,name);        
    }
    public File makeSimFile(FileLocator f){
        String name;
        File path;                
        path = new File(getResolvedPath(f));
        //name
        if(null != f.fileName()){ 
            name = f.fileName();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,".");
            String nameId=tk.nextToken();        
            //if(getFileNameCount(nameId,path).equals("000"))
            //    name = f.fileName()+".jt";
            //else
            name = f.fileName()+"-"+getFileNameCount(nameId,path)+".jt";            
        }
        else if(null != tp){
            name = tp.getModelInput();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,"-");
            String nameId=tk.nextToken();        
            name = f.fileName()+"-"+getFileNameCount(nameId,path)+".jt";            
        }
        else{
            name = "sim-"+getFileNameCount("sim",path)+".jt";
        }
        return new File(path,name);        
    }
    public File makeSimDumpFolder(FileLocator f){
        String name;
        File path;                
        path = new File(getResolvedPath(f));
        //name
        if(null!=f.fileName()){
            name = f.fileName();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,".");
            String nameId=tk.nextToken();        
            //if(getFileNameCount(nameId,path).equals("000"))
            //    name = f.fileName();
            //else
                name = f.fileName()+"-"+getFileNameCount(nameId,path);            
        }
        else if(null!=tp){
            name = tp.getModelInput();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,"-");
            String nameId=tk.nextToken();        
            name = f.fileName()+"-"+getFileNameCount(nameId,path);            
        }
        else{
            name = "datadump-"+getFileNameCount("datadump",path);
        }        
        return new File(path,name);        
    }
    
    public File makeGraphPngFile(FileLocator f){
        String name;
        File path;        
        path = new File(getResolvedPath(f));
        //name
        if(null!=f.fileName()){ 
            name = f.fileName();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,".");
            String nameId=tk.nextToken();        
            //if(getFileNameCount(nameId,path).equals("000"))
            //    name = f.fileName()+".png";
            //else
                name = f.fileName()+"-"+getFileNameCount(nameId,path)+".png";            
        }
        else if(null!=tp){
            name = tp.getModelInput();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,"-");
            String nameId=tk.nextToken();        
            name = f.fileName()+"-"+getFileNameCount(nameId,path)+".png";            
        }
        else
            name = "graph-"+getFileNameCount("graph",path)+".png";
        
        return new File(path,name);        
    }
    public File makeGraphCsvFile(FileLocator f, boolean append){
        String name;
        File path;                
        path = new File(getResolvedPath(f));
        //name
        if(null!=f.fileName()){
            name = f.fileName();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,".");
            String nameId=tk.nextToken();        
            name = f.fileName();
            if(!append) name+="-"+getFileNameCount(nameId,path);
            name+=".txt";            
        }
        else if(null!=tp){
            name = tp.getModelInput();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,"-");
            String nameId=tk.nextToken();        
            name = f.fileName();
            if(!append) name+="-"+getFileNameCount(nameId,path);
            name+=".txt";                        
        }
        else{ 
            name = "graph";
            if(!append) name+="-"+getFileNameCount("graph-",path);
            name+=".txt";
        }
        return new File(path,name);        
    }
    public File makeAvgGraphPngFile(FileLocator f){
        String name;
        File path;        
        path = new File(getResolvedPath(f));
        //name
        if(null!=f.fileName()){ 
            name = f.fileName();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,".");
            String nameId=tk.nextToken();        
            //if(getFileNameCount(nameId,path).equals("001"))
            //    name = f.fileName()+".png";
            //else
                name = f.fileName()+"-"+getFileNameCount(nameId,path)+".png";            
        }
        else if(null!=tp){
            name = tp.getModelInput();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,"-");
            String nameId=tk.nextToken();        
            name = f.fileName()+"-"+getFileNameCount(nameId,path)+".png";            
        }
        else
            name = "avgGraph-"+getFileNameCount("avgGraph",path)+".png";
        
        return new File(path,name);        
    }
    public File makeAvgGraphCsvFile(FileLocator f){
        String name;
        File path;                
        path = new File(getResolvedPath(f));
        //name
        if(null!=f.fileName()){
            name = f.fileName();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,".");
            String nameId=tk.nextToken();        
            name = f.fileName()+"-"+getFileNameCount(nameId,path)+".txt";            
        }
        else if(null!=tp){
            name = tp.getModelInput();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,"-");
            String nameId=tk.nextToken();        
            name = f.fileName()+"-"+getFileNameCount(nameId,path)+".txt";            
        }
        else{ 
            name = "avgGraph-"+getFileNameCount("avgGraph",path)+".txt";
        }
        return new File(path,name);        
    }
    
    public File makeOutputFile(FileLocator f){
        String name;
        File path;                
        path = new File(getResolvedPath(f));
        //name
        if(null==f.fileName())
            name = "script_output.txt";
        else{ 
            if(f.fileName().indexOf(".")==-1)
                name = f.fileName().concat(".txt");
            else
                name = f.fileName();            
        }
        return new File(path,name);        
    } 
    public File makeParameterJtFile(FileLocator f){
        String name;
        File path;                
        path = new File(getResolvedPath(f));
        //name
        if(null != f.fileName()){ 
            name = f.fileName();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,".");
            String nameId=tk.nextToken();        
            //if(getFileNameCount(nameId,path).equals("001"))
            //    name = f.fileName()+".jt";
            //else
            name = f.fileName()+"-"+getFileNameCount(nameId,path)+".jt";            
        }
        else if(null != tp){
            name = tp.getModelInput();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,"-");
            String nameId=tk.nextToken();        
            name = f.fileName()+"-"+getFileNameCount(nameId,path)+".jt";            
        }
        else{
            name = "param-"+getFileNameCount("param",path)+".jt";
        }
        return new File(path,name);        
    }
    public File makeParameterTxtFile(FileLocator f){
        String name;
        File path;                
        path = new File(getResolvedPath(f));
        //name
        if(null != f.fileName()){ 
            name = f.fileName();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,".");
            String nameId=tk.nextToken();        
            //if(getFileNameCount(nameId,path).equals("001"))
            //    name = f.fileName()+".jt";
            //else
            name = f.fileName()+"-"+getFileNameCount(nameId,path)+".txt";            
        }
        else if(null != tp){
            name = tp.getModelInput();
            java.util.StringTokenizer tk = new java.util.StringTokenizer(name,"-");
            String nameId=tk.nextToken();        
            name = f.fileName()+"-"+getFileNameCount(nameId,path)+".txt";            
        }
        else{
            name = "param-"+getFileNameCount("param",path)+".txt";
        }
        return new File(path,name);        
    }    
    public File getReferencedFile(FileLocator f){
        if(f==null) return null;
        String path = getResolvedPath(f);
        String name = f.fileName();
        if(name==null||name.equals(""))
            return null;
        java.io.File res = new java.io.File(path,name);
        //System.out.println("referenced file: "+res);
        
        if(res.exists()&&res.canRead())
            return res;
        else 
            return null;
    }
    private String getResolvedPath(FileLocator f){
        String path;
        if(null==f){            
            path = edu.uconn.psy.jtrace.UI.traceProperties.rootPath.getAbsolutePath();
        }
        else if(null!=f.absolutePath()){            
            path = f.absolutePath();
        }
        else if(null!=f.relativePath()){            
            if(!f.relativePath().startsWith(File.separator))
                path = rootDirectory.concat(File.separator).concat(f.relativePath());
            else
                path = rootDirectory.concat(f.relativePath());
        }
        else{
            path = rootDirectory;
        }
        
        return path;
    }
    
    private String getFileNameCount(String name, File path){
        if(!path.exists()||!path.isDirectory())
            return "001";
        java.util.StringTokenizer tk = new java.util.StringTokenizer(name,".");
        name=tk.nextToken();        
        File[] files = path.listFiles();
        int num=0;
        for(int i=0;i<files.length;i++)
            if(files[i].getName().startsWith(name))
                num++;
        num++; //we want to add the next one!
        String result;
        if(num<10) result="00"+num;
        else if(num<100) result="0"+num;
        else if(num<1000) result=""+num;
        else result=""+num;
        return result;                    
    }
    public static String tmpFileName(){
        String result = "tmp";
        long i = System.currentTimeMillis();
        Integer j = new Integer((int)i);
        result=result+j.toHexString((int)i)+".jt";
        System.out.println(result);
        return result;        
    }
    public static java.io.File quickFile(edu.uconn.psy.jtrace.Model.Scripting.FileLocator f){
        java.io.File res;
        String mne=(new Long(System.currentTimeMillis())).toString().substring(7);
        String fileName;
        if(null == f){ 
            res = new java.io.File(rootDirectory.concat("/temp"),mne.concat("jt"));
        }
        else{
            if(null != f.fileName()) fileName=f.fileName();
            else fileName=mne.concat(".jt");
            if(null!=f.absolutePath()) res = new java.io.File(f.absolutePath(),fileName);
            else if(null!=f.relativePath()) res = new java.io.File(rootDirectory.concat(f.relativePath()),fileName);
            else res = new java.io.File(rootDirectory.concat("/temp"),fileName);
        }
        if(!res.getParentFile().exists()) 
            res.getParentFile().mkdirs();
        try{
            if(!res.exists()) 
                res.createNewFile();
        }
        catch(java.io.IOException ioe){ioe.printStackTrace();}
        return res;
    }            
}

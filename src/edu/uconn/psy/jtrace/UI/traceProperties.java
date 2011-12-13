/*
 * traceProperties.java
 *
 * Created on December 3, 2004, 1:18 PM
 */

package edu.uconn.psy.jtrace.UI;

import java.io.File;

/**
 *
 * @author  tedstrauss
 */
public class traceProperties extends java.util.Properties {
    public static String startupOptions;
    public static java.io.File workingPath ;
    public static java.io.File rootPath ;
    public static java.io.File diagnosticFile=new java.io.File("/home/tedstrauss/neighb/fdb_1.txt");
    public static String jtVersion = "0.63 alpha";
    public static boolean hasGUI;
    
    /** Creates a new instance of traceProperties */
    public traceProperties() {        
        //setProperty("WorkingDirectory","null");
        //setProperty("situated","false");        
        
    }
    
    // never called, I think!
//    public traceProperties(String path){        
//        setProperty("WorkingDirectory",path);
//        try{
//            rootPath=new java.io.File(path);
//            if(rootPath.exists()){
//                //System.out.println("root path exists ");
//                setProperty("situated","true");
//            }
//            else{
//                //System.out.println("specified root path does not exist");
//                setProperty("situated","false");
//                setProperty("path",path+" (invalid)");
//            }
//            
//        }
//        catch(Exception e){e.printStackTrace();}        
//        
//    }
//    public boolean isSituated(){
//        // never occurs
//        if(getProperty("situated").equals("true")) return true;
//        else if(getProperty("situated").equals("false")) return false;
//        else return false;
//    }
    public void situate(java.awt.Component component){
        //checks for the ~/.jtrace file, loads it's contents or creates a new one.
        // on Windows, ~\Application Data\jTRACE\jtrace.ini
        // everywhere else, ~/.jtrace
        
        // confirms that WorkingDirectory is valid (lexicons/initial_lexicon.xml exists)
        // if not, looks for it in ~ and $CWD
        // if not, asks the user (avoid if possible!)
        
        try
        {           
            
            String userHome = System.getProperty("user.home");
            String userDir = System.getProperty("user.dir");
            String jarRelPath = System.getProperty("file.separator") + 
                    "jTRACE.jar";
            String dotFilePath;
            
            // construct the dot-file location name
            if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)
            {
                // it's a Windows box
                dotFilePath = userHome + "\\Application Data\\jTRACE\\jtrace.ini";
            }
            else
            {
                // it's not a Windows box
                dotFilePath = userHome + "/.jtrace";
            }
            
            // does it exist?
            java.io.File paramFile=new java.io.File(dotFilePath);
            if(paramFile.exists()&&paramFile.isFile()&&paramFile.canRead())
            {
                // yes, it exists, now try to see if the WorkingDirectory is there
                
                load(new java.io.BufferedInputStream(new java.io.FileInputStream(paramFile)));
            }
            // otherwise, we'll fail in getProperty, which is fine.
                
            // if WorkingDirectory property exists, then see if the init_lex 
            // file exists. 

            if (getProperty("WorkingDirectory") != null)
            {
                // property exists, but is it valid?
                rootPath = new java.io.File(getProperty("WorkingDirectory"));
                workingPath = rootPath;
                
                if (rootPath.exists())
                {
                    java.io.File jarFile = new java.io.File(rootPath.getAbsolutePath() + jarRelPath);

                    if (jarFile.exists() && jarFile.canRead())
                    {
                        // we're good
                        return;
                    }
                }
            }

            //System.out.println("Can't find working directory... Looking for it...");
            
            // OK, we've fallen through for some reason. So, try to find the
            // initial_lexicon.xml file, somewhere...
            // 1. user.dir/lexicons/?
            // 2. user.home/lexicons/?
            // 3. that's it for now... 

            java.io.File userDirJarPath = new java.io.File(userDir, jarRelPath);
            java.io.File userHomeJarPath = new java.io.File(userHome, jarRelPath);

            if (userDirJarPath.exists() && userDirJarPath.canRead())
            {
                setProperty("WorkingDirectory", userDir);
                rootPath = new java.io.File(userDir);
                workingPath = rootPath;                
            }
            else if (userHomeJarPath.exists() && userHomeJarPath.canRead())
            {
                setProperty("WorkingDirectory", userHome);
                rootPath = new java.io.File(userHome);
                workingPath = rootPath;
            }
            else
            {                
                // OK, where is it?! Ask the user...
                String userSuggestedPath; 
                if(null == component){
                    userSuggestedPath = locateRootFolderDialog();
                }
                else{
                    userSuggestedPath = locateRootFolderDialog(component);
                }
                if (userSuggestedPath.length() == 0)
                {
                    // they canceled
                    System.out.println("No directory selected -- aborting.");
                    System.exit(0);
                }

                java.io.File userSuggestedJarPath = new java.io.File(userSuggestedPath, jarRelPath);

                if (userSuggestedJarPath.exists() && userSuggestedJarPath.canRead())
                {
                    // good job, user!
                    setProperty("WorkingDirectory", userSuggestedPath);
                    rootPath = new java.io.File(userSuggestedPath);
                    workingPath = rootPath;                    
                }
                else
                {
                    // OK, they couldn't find it. Quit.
                    System.out.println("Sorry, the directory specified did not seem to be the program folder.");
                    System.out.println("You may need to check folder permissions, then try again.");
                    
                    System.exit(0);
                }                 
            }

            //have we stored the last visitted directory?            
            if (getProperty("LastDirectory") != null){
                workingPath = new java.io.File(getProperty("LastDirectory"));            
            }
            
            // great, now we've got the property set. Save it and we're done.
            try
            {
                File dotFile = new File(dotFilePath);
                dotFile.getParentFile().mkdirs();   // create parent directories, useful only on Windows...
                
                store(new java.io.BufferedOutputStream(new java.io.FileOutputStream(dotFilePath)),"---jTRACE properties file---");
            }
            catch(java.io.FileNotFoundException fe){fe.printStackTrace();}
            catch(Exception e){e.printStackTrace();}
            
        }
        catch(java.io.IOException ioe){ioe.printStackTrace();}
        catch(Exception e){e.printStackTrace();}
    }
    
    /**
     * GUI version.
     */
    private String locateRootFolderDialog(java.awt.Component component){
        javax.swing.JOptionPane.showMessageDialog(component,"I am unable to locate the jTRACE working directory.\nPlease find the directory that contains jTRACE.jar for me.");            
        String pathName;
        try{
            javax.swing.JFileChooser fileChooser1 = new javax.swing.JFileChooser(System.getProperty("user.home"));
            fileChooser1.setApproveButtonText("Choose");
            fileChooser1.setDialogTitle("Select \'jTRACE\' directory."); 
            fileChooser1.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
            fileChooser1.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
            int returnVal=fileChooser1.showOpenDialog(component);
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                //System.out.println("*** "+fileChooser1.getSelectedFile().getCanonicalPath());            
                pathName=fileChooser1.getSelectedFile().getAbsolutePath();
                return pathName;
                //rootPath=new java.io.File(pathName);                
            } 
            else
                return "";
        }
        catch(Exception e){ e.printStackTrace();}
        
        return "";
    }
    
    /**
     * Standard I/O version.
     */
    private String locateRootFolderDialog(){
        String rootPath="";
        try{
            System.out.println("I am unable to locate the jTRACE working directory.\n"+
                    "Please carefully type in the full directory path where jTRACE is located.\n"+
                    "For example, \'/my-docs/psychology/jTRACE\'.\n"+
                    "> ");
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.StringReader(" "));
            rootPath = br.readLine();            
        }
        catch(java.io.IOException ioe)
        {
            ioe.printStackTrace();
        }
        return rootPath;
    }
    public void storeWorkingDir(){
        setProperty("LastDirectory", workingPath.getAbsolutePath());
    }

}

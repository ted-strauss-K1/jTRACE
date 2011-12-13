//svn copy svn://merganser.psy.uconn.edu/jtrace/trunk svn://merganser.psy.uconn.edu/jtrace/release-1.0a5 -m "tag for 1.0a5"

/*
 * probability.java
 *
 * Created on April 27, 2005, 6:09 PM
 */

package edu.uconn.psy.jtrace;
import java.util.*;
/**
 *
 * @author tedstrauss
 */
public class probability {
    static String[] slex={"-x","-xbrxpt","-xdapt","-xdxlt","-xgri","-xlat","-xpart","-xpil","-ark","-ar","-art","-artxst","-xslip","-bar","-bark","-bi","-bit","-bist","-blak","-blxd","-blu","-bab","-babi","-badi","-bust","-but","-batxl","-baks","-brid","-brud","-brxS","-bxbxl","-bxk","-bxs","-bxt","-kar","-kard","-karpxt","-sis","-klak","-klxb","-klu","-kalig","-kul","-kap","-kapi","-kxpxl","-krip","-kru","-krap","-kruSxl","-kruxl","-krxS","-kxp","-kxt","-dark","-dart","-dil","-did","-dip","-du","-dal","-dat","-dxbxl","-dru","-drap","-drxg","-dxk","-dxl","-dxst","-duti","-ist","-it","-glu","-gad","-gat","-grik","-grit","-gru","-grup","-gard","-gxtar","-kip","-ki","-lid","-lig","-lip","-list","-ligxl","-labi","-lak","-lup","-lus","-lat","-lxk","-lxki","-lxkSxri","-ad","-papx","-park","-part","-parSxl","-partli","-parti","-par","-pi","-pik","-pipxl","-pis","-plat","-plxg","-plxs","-pakxt","-pxlis","-palxsi","-pul","-pap","-pasxbxl","-pasxbli","-pat","-prist","-prabxbxl","-prabxbli","-pradus","-pradxkt","-pragrxs","-pxt","-rid","-ril","-rili","-rab","-rak","-rakxt","-rad","-rut","-rxb","-rxgxd","-rul","-rupi","-rxS","-rxsxl","-skar","-skul","-skru","-sil","-sit","-sikrxt","-si","-sid","-sik","-Sarp","-Si","-Sip","-Sit","-Sild","-Sak","-Sut","-Sap","-Sat","-Srxg","-Sxt","-slip","-slit","-slxg","-salxd","-sari","-spark","-spik","-spid","-spat","-star","-start","-startxl","-stil","-stip","-stak","-stap","-strik","-strit","-strxk","-strxgxl","-stxdid","-stxdi","-stupxd","-sxbstxtut","-sxtxl","-sxksid","-sxk","-su","-sut","-sutxbxl","-tar","-targxt","-ti","-tu","-tul","-tap","-trit","-triti","-tri","-trup","-trat","-trxbxl","-trxk","-tru","-truli","-trxst","-trxsti","-tub","-xgli","-xp","-xs","--"};
    static String[] slexEnemies={"-xp","-x","-x","-x","-xgli","-x","-xp","-xp","-ar","-ark","-ar","-art","-xs","-bark","-bar","-bit","-bi","-bi","-blu","-blu","-brud","-babi","-bab","-babi","-but","-bust","-bab","-bab","-brud","-brid","-brud","-bxt","-bxt","-bxt","-bxk","-kard","-kar","-kar","-si","-klu","-klu","-kru","-kar","-skul","-kapi","-kap","-kxp","-kru","-skru","-kru","-kru","-kru","-kru","-kxpxl","-kxp","-dart","-dark","-did","-dip","-did","-duti","-dark","-dal","-dxk","-drap","-dru","-dru","-dxl","-dxk","-dxk","-du","-bist","-bit","-gru","-gat","-gad","-gru","-gru","-grup","-gru","-gad","-gat","-ki","-kip","-lig","-ligxl","-slip","-ist","-lig","-lak","-blak","-lus","-lup","-lak","-lxki","-lxk","-lxk","-gad","-pap","-par","-par","-par","-part","-part","-park","-pik","-pi","-pi","-pi","-lat","-plxs","-plxg","-pap","-pxt","-par","-par","-papx","-pasxbli","-pasxbxl","-pap","-pi","-prabxbli","-prabxbxl","-pradxkt","-pradus","-pradus","-pat","-brid","-rili","-ril","-rad","-rakxt","-rak","-rab","-rupi","-rxS","-rxb","-rut","-rut","-rxb","-rxS","-skul","-skar","-skar","-si","-si","-sik","-sis","-si","-si","-Sap","-Sip","-Si","-Si","-Si","-Sap","-Sat","-Sat","-Sap","-rxgxd","-Sat","-slit","-slip","-slip","-sari","-si","-star","-spid","-spik","-spark","-start","-star","-start","-stip","-stil","-stap","-stak","-strit","-strik","-strxgxl","-strxk","-stxdi","-stxdid","-stap","-sxk","-sxk","-sxk","-sxksid","-sut","-su","-sut","-targxt","-tar","-ti","-tul","-tu","-stap","-triti","-trit","-trit","-tru","-tru","-tru","-tru","-trup","-tru","-trxsti","-trxst","-tu","-xgri","-x","-x","-xbrxpt"};
    static String phonemeLabels[]={"p", "b", "t", "d", "k", "g", "s", "S", "r", "l", "a", "i", "u", "x", "-"};     
    //static String slex[]={"-x","-xbrxpt","-xdapt","-xdxlt","-xgri","-xlat","-xpart","-xpil","-ark","-ar","-art","-artxst","-xslip","-bar","-bark","-bi","-bit","-bist","-blak","-blxd","-blu","-bab","-babi","-badi","-bust","-but","-batxl","-baks","-brid","-brud","-brxS","-bxbxl","-bxk","-bxs","-bxt","-kar","-kard","-karpxt","-sis","-klak","-klxb","-klu","-kalig","-kul","-kap","-kapi","-kxpxl","-krip","-kru","-krap","-kruSxl","-kruxl","-krxS","-kxp","-kxt","-dark","-dart","-dil","-did","-dip","-du","-dal","-dat","-dxbxl","-dru","-drap","-drxg","-dxk","-dxl","-dxst","-duti","-ist","-it","-glu","-gad","-gat","-grik","-grit","-gru","-grup","-gard","-gxtar","-kip","-ki","-lid","-lig","-lip","-list","-ligxl","-labi","-lak","-lup","-lus","-lat","-lxk","-lxki","-lxkSxri","-ad","-papx","-park","-part","-parSxl","-partli","-parti","-par","-pi","-pik","-pipxl","-pis","-plat","-plxg","-plxs","-pakxt","-pxlis","-palxsi","-pul","-pap","-pasxbxl","-pasxbli","-pat","-prist","-prabxbxl","-prabxbli","-pradus","-pradxkt","-pragrxs","-pxt","-rid","-ril","-rili","-rab","-rak","-rakxt","-rad","-rut","-rxb","-rxgxd","-rul","-rupi","-rxS","-rxsxl","-skar","-skul","-skru","-sil","-sit","-sikrxt","-si","-sid","-sik","-Sarp","-Si","-Sip","-Sit","-Sild","-Sak","-Sut","-Sap","-Sat","-Srxg","-Sxt","-slip","-slit","-slxg","-salxd","-sari","-spark","-spik","-spid","-spat","-star","-start","-startxl","-stil","-stip","-stak","-stap","-strik","-strit","-strxk","-strxgxl","-stxdid","-stxdi","-stupxd","-sxbstxtut","-sxtxl","-sxksid","-sxk","-su","-sut","-sutxbxl","-tar","-targxt","-ti","-tu","-tul","-tap","-trit","-triti","-tri","-trup","-trat","-trxbxl","-trxk","-tru","-truli","-trxst","-trxsti","-tub","-xgli","-xp","-xs","--"};
    //static String slexEnemies[]={"-xp","-x","-x","-x","-xgli","-x","-xp","-xp","-ar","-ark","-ar","-art","-xs","-bark","-bar","-bit","-bi","-bi","-blu","-blu","-brud","-babi","-bab","-babi","-but","-bust","-bab","-bab","-brud","-brid","-brud","-bxt","-bxt","-bxt","-bxk","-kard","-kar","-kar","-si","-klu","-klu","-kru","-kar","-skul","-kapi","-kap","-kxp","-kru","-skru","-kru","-kru","-kru","-kru","-kxpxl","-kxp","-dart","-dark","-did","-dip","-did","-duti","-dark","-dal","-dxk","-drap","-dru","-dru","-dxl","-dxk","-dxk","-du","-bist","-bit","-gru","-gat","-gad","-gru","-gru","-grup","-gru","-gad","-gat","-ki","-kip","-lig","-ligxl","-slip","-ist","-lig","-lak","-blak","-lus","-lup","-lak","-lxki","-lxk","-lxk","-gad","-pap","-par","-par","-par","-part","-part","-park","-pik","-pi","-pi","-pi","-lat","-plxs","-plxg","-pap","-pxt","-par","-par","-papx","-pasxbli","-pasxbxl","-pap","-pi","-prabxbli","-prabxbxl","-pradxkt","-pradus","-pradus","-pat","-brid","-rili","-ril","-rad","-rakxt","-rak","-rab","-rupi","-rxS","-rxb","-rut","-rut","-rxb","-rxS","-skul","-skar","-skar","-si","-si","-sik","-sis","-si","-si","-Sap","-Sip","-Si","-Si","-Si","-Sap","-Sat","-Sat","-Sap","-rxgxd","-Sat","-slit","-slip","-slip","-sari","-si","-star","-spid","-spik","-spark","-start","-star","-start","-stip","-stil","-stap","-stak","-strit","-strik","-strxgxl","-strxk","-stxdi","-stxdid","-stap","-sxk","-sxk","-sxk","-sxksid","-sut","-su","-sut","-targxt","-tar","-tu","-tul","-tu","-stap","-triti","-trit","-trit","-tru","-tru","-tru","-tru","-trup","-tru","-trxsti","-trxst","-tu","-xgri","-x","-x","-xbrxpt"};
    static String NFA[]={"-artxst","-kard","-sis","-klu","-kalig","-krip","-krap","-kruSxl","-kruxl","-kxt","-glu","-lxkSxri","-parSxl","-partli","-parti","-palxsi","-pul","-prabxbli","-skul","-sil","-sikrxt","-sid","-startxl","-strxgxl","-stxdid","-stupxd","-sxksid","-triti","-trup","-truli","-trxsti"};
    static String FA[]={"-xbrxpt","-xdxlt","-xdapt","-xgli","-xgri","-xlat","-xpart","-xpil","-xslip","-ark","-art","-bxbxl","-bxk","-bxs","-bxt","-bab","-babi","-badi","-baks","-bar","-bark","-batxl","-bist","-bit","-blxd","-blak","-blu","-brxS","-brid","-brud","-bust","-but","-dxbxl","-dxk","-dxl","-dxst","-dal","-dark","-dart","-dat","-did","-dil","-dip","-drxg","-drap","-dru","-duti","-gxtar","-gad","-gard","-gat","-grik","-grit","-gru","-grup","-ist","-kxp","-kap","-kar","-kip","-klxb","-klak","-kru","-kul","-lxk","-labi","-lak","-lat","-lid","-lig","-ligxl","-lip","-list","-lup","-lus","-pxt","-pakxt","-pap","-papx","-par","-part","-pasxbxl","-pat","-pik","-pis","-plxg","-plxs","-plat","-pradxkt","-pragrxs","-prist","-rxb","-rxgxd","-rxS","-rxsxl","-rab","-rad","-rak","-rid","-ril","-rili","-rul","-rupi","-rut","-sxbstxtut","-sxk","-Sxt","-sxtxl","-Sak","-Sap","-sari","-Sarp","-Sat","-sik","-Sild","-Sip","-sit","-Sit","-skar","-skru","-slxg","-slip","-slit","-spat","-spid","-spik","-Srxg","-stxdi","-stak","-stap","-star","-start","-stil","-stip","-strxk","-strik","-strit","-Sut","-sut","-tap","-tar","-targxt","-trxk","-trxst","-trat","-tri","-trit","-tru","-tub","-tul"};

    public static void main(String[] args){
        
        tree t=new tree(slex,phonemeLabels);
        //System.out.println(t.toString());
        //System.exit(0);
        
        
        String[] enemies;
        double[] enemyEval;
        int numEnemies=0;
        for(int i=0;i<NFA.length;i++){
            enemyEval=new double[slex.length];
            for(int j=0;j<slex.length;j++){
                if(NFA[i].length()<3) enemyEval[j]=t.proportionOfWeight(NFA[i].substring(0,NFA[i].length()-1), slex[j]);            
                else enemyEval[j]=t.proportionOfWeight(NFA[i].substring(0,3), slex[j]);            
                if(enemyEval[j]>0.5) numEnemies++;
            }
            enemies=new String[numEnemies];
            int h=0;
            for(int j=0;j<slex.length;j++){
                if(enemyEval[j]>0.5) enemies[h++]=slex[j];
            }
            System.out.print("\n");
            if(t.indexOf(probability.NFA,NFA[i])>-1) System.out.print("NFA\t");
            else if(t.indexOf(probability.FA,NFA[i])>-1) System.out.print("FA\t");
            else System.out.print("XX\t");            
            System.out.print("target= "+NFA[i]+"\n");                
            for(int j=0;j<enemies.length;j++){        
                if(null==enemies[j]) continue;
                System.out.print("\'"+enemies[j]+"\t");
                for(int k=2;k<=NFA[i].length();k++){
                    System.out.print(t.proportionOfWeight(NFA[i].substring(0,k), enemies[j])+"\t");
                }
                System.out.print("\n");            
            }
            enemyEval=null;
            enemies=null;
            numEnemies=0;
        }
        
        //MULTI-TREE VERSION
        /*tree[] t=new tree[15];
        String[][] sublex=new String[15][];
        int size;
        for(int i=0;i<t.length;i++){
            size=0;
            for(int j=0;j<slex.length;j++){
                if(phonemeLabels[i].equals(slex[j].substring(1,2))){ 
                    //System.out.println(slex[j].substring(1,2));
                    size++;
                }
            }
            sublex[i]=new String[size];
            int k=0;
            for(int j=0;j<slex.length;j++)
                if(phonemeLabels[i].equals(slex[j].substring(1,2))) 
                    sublex[i][k++]=slex[j];
            t[i]=new tree(sublex[i],phonemeLabels);                
        }
        
        for(int i=0;i<slex.length;i++){
            if(t[0].indexOf(probability.NFA,slex[i])>-1) System.out.print("NFA\t");
            else if(t[0].indexOf(probability.FA,slex[i])>-1) System.out.print("FA\t");
            else System.out.print("XX\t");
            System.out.print("\'"+slex[i]+"\t");
            for(int j=0;j<slex[i].length();j++)
                System.out.print(t[tree.indexOf(phonemeLabels,slex[i].substring(1,2))].densityRankForItem(slex[i].substring(0, j+1),slex[i])+"\t");                
            System.out.println();
        }*/
        
        
        //SINGLE TREE VERSION HERE
        /*tree t=new tree(slex,phonemeLabels);
        //System.out.print(t.evidenceRankForItem("-bxbxl".substring(0, 2),"-bxbxl")+"\t\n\n");        
        
        for(int i=0;i<slex.length;i++){
            if(t.indexOf(probability.NFA,slex[i])>-1) System.out.print("NFA\t");
            else if(t.indexOf(probability.FA,slex[i])>-1) System.out.print("FA\t");
            else System.out.print("XX\t");
            System.out.print("\'"+slex[i]+"\t");
            for(int j=0;j<slex[i].length();j++)
                System.out.print(t.densityRankForItem(slex[i].substring(0, j+1),slex[i])+"\t");
            System.out.println();
        }*/
        /*for(int i=0;i<slex.length;i++){
            System.out.print("\'"+slex[i]+"\t");
            for(int j=0;j<target.length();j++)
                System.out.print(t.evidenceRankForItem(target.substring(0, j+1),slex[i])+"\t");
                //System.out.print(t.proportionOfEvidence(target.substring(0, j+1),slex[i])+"\t");
            System.out.print("\n");
        }*/
        /*System.out.println("NFA\nNFA\nNFA\nNFA\nNFA\n");
        for(int i=0;i<NFA.length;i++){
            t.followPath(NFA[i]);
            //System.out.print("\n");
        }
        System.out.println("FA\nFA\nFA\nFA\nFA\n");
        for(int i=0;i<FA.length;i++){
            t.followPath(FA[i]);
            //System.out.print("\n");
        }
        */        
        
    }
    
}
class tree{
    String[] lexicon;
    String[] labels;
    node[][] grid;
    node root;
    int treeWeight;
    
    public tree(String[] lex,String[] lbl){
        lexicon=lex;
        labels=lbl;
        if(lexicon.length==0) return;
        //determine dimensions of grid; make grid
        int max=0;
        for(int i=0;i<lexicon.length;i++)
            if(lexicon[i].length()>max) max=lexicon[i].length();
        
        grid=new node[labels.length][max];
        for(int i=0;i<grid.length;i++)
            for(int j=0;j<grid[i].length;j++)
                grid[i][j]=new node(labels[i],j);
        root=grid[14][0]; //this is the silence node at the top.
        //insert lexical items into grid, creating connections
        node parent, child, lexicalBearer;
        for(int lx=0;lx<lexicon.length;lx++){
            for(int len=0;len<lexicon[lx].length()-1;len++){
                parent=grid[indexOf(labels,lexicon[lx].substring(len,len+1))][len];
                child=grid[indexOf(labels,lexicon[lx].substring(len+1,len+2))][len+1];
                parent.addChild(child);
                child.addParent(parent);                
            }            
            //apply lexicality value to terminal node of each word.
            lexicalBearer=grid[indexOf(labels,lexicon[lx].substring(lexicon[lx].length()-1,lexicon[lx].length()))][lexicon[lx].length()-1];
            lexicalBearer.incrementLexicality();
        }        
        //cardinality, weight, density stats
        int nodes=0;
        treeWeight=0;
        for(int j=grid[0].length-1;j>=0;j--) //necessary to start at the bottom for weightCalc()
            for(int i=0;i<grid.length;i++){
                if(null==grid[i][j]||(grid[i][j].parents().size()==0&&grid[i][j].children().size()==0)){ 
                    //grid[i][j]=null;
                    continue;
                }
                unmarkAll();
                grid[i][j].calcCardinality();        
                unmarkAll();
                grid[i][j].calcWeight();        
                treeWeight+=grid[i][j].weight();
                unmarkAll();                
                grid[i][j].calcDensity();                
                nodes++;
            }           
        //System.out.println("nodes="+nodes);    
        //probability stats, normalize probability.
        //a nodes probability is equal to it's potential to arrive at a terminal node.
        //therefore probability=weight/(total tree weight)
        for(int j=0;j<grid[0].length;j++)
            for(int i=0;i<grid.length;i++){
                if(null==grid[i][j]||(grid[i][j].parents().size()==0&&grid[i][j].children().size()==0)) continue;
                grid[i][j].setProbability((double)grid[i][j].weight()/(double)treeWeight);
                //System.out.println(i+"x"+j+"\t"+grid[i][j].toString());
            }
        //calculate depth scaled weights
        double currDenom;
        for(int j=0;j<grid[0].length;j++){
            currDenom=0d;
            for(int i=0;i<grid.length;i++){
                if(null==grid[i][j]||(grid[i][j].parents().size()==0&&grid[i][j].children().size()==0)) continue;
                currDenom+=grid[i][j].weight();            
            }
            for(int i=0;i<grid.length;i++){
                grid[i][j].setDepthScaledWeight((double)((double)grid[i][j].weight()/currDenom));
            }
        }
        
        //calculate path weights (similar to earlier tree definition)
        node curr;
        for(int i=0;i<lexicon.length;i++){
            //System.out.println("\n"+lexicon[i]);
            for(int j=0;j<lexicon[i].length();j++){
                if(null==grid[indexOf(labels,lexicon[i].substring(j,j+1))][j]) continue;
                curr=grid[indexOf(labels,lexicon[i].substring(j,j+1))][j];
                curr.setPathWeight(curr.pathWeight()+1);
                //System.out.print(lexicon[i].substring(j,j+1)+" "+curr.pathWeight()+" ");                
            }
        }
        
    }    
    public int evidenceRankForItem(String in,String candidate){
        double[] evidence=new double[lexicon.length];
        for(int i=0;i<lexicon.length;i++)
            evidence[i]=proportionOfEvidence(in, lexicon[i]);
        int targetIndex=indexOf(lexicon, candidate);
        int rank=0;
        for(int i=0;i<lexicon.length;i++){
            if(i==targetIndex){ 
                //System.out.println(lexicon[i]+"\t"+evidence[i]+"\t"+proportionOfDensity(in, lexicon[i]));
                continue;                
            }
            if(evidence[i]>=evidence[targetIndex]){ 
                //System.out.println(lexicon[i]+"\t"+evidence[i]+"\t"+proportionOfDensity(in, lexicon[i]));
                rank++;                
            }
        }
        return rank;
    }
    public double proportionOfEvidence(String in,String candidate){
        String target=candidate;
        unmarkAll();
        LinkedList targetTotal=totalEvidenceFor(target);
        int targetTotalSize=targetTotal.size();
        unmarkAll();
        int subtract=0;
        LinkedList evidence=new LinkedList();
        for(int i=0;i<in.length();i++){
            evidence.addAll(grid[indexOf(labels,in.substring(i,i+1))][i].subsumedNodes());
            if(i==0) subtract=evidence.size();
            unmarkAll();
        }
        //how much of the targetTotal is satisfied by the current evidence?
        node curr;
        ListIterator iter=evidence.listIterator();            
        int targetPoints=0;
        while(iter.hasNext()){
            curr=((node)iter.next());
            if(targetTotal.contains(curr)){
                targetTotal.remove(curr);
                targetPoints++;
                //tgtWtpts+=curr.weight();
            }            
            else{ //penalty for counter-evidence
                //if(targetPoints>0)
                //    targetPoints--;
            }
        }
        if(targetPoints>subtract) targetPoints-=subtract;
        else targetPoints=0;
        if(targetTotalSize>subtract) targetTotalSize-=subtract;
        else targetTotalSize=1;
        double proportion=((double)((double)(targetPoints)/(double)(targetTotalSize)));
        return proportion;
        
    }
    public double proportionOfWeight(String in,String candidate){
        String target=candidate;
        unmarkAll();
        LinkedList targetTotal=totalEvidenceFor(target);        
        unmarkAll();
        LinkedList evidence=totalEvidenceFor(in);
        unmarkAll();
        LinkedList _subtract=totalEvidenceFor("-");
        unmarkAll();
        ListIterator iter;
        node curr;
        //remove nodes from first segment exposure, from evidence and total lists.
        iter=_subtract.listIterator();
        while(iter.hasNext()){
            curr=(node)iter.next();
            evidence.remove(curr);
            targetTotal.remove(curr);
        }
        //how much of the targetTotal is satisfied by the current evidence?        
        iter=evidence.listIterator();                        
        int targetTotalSize=totalPathWeight(targetTotal);        
        int targetPoints=0;
        while(iter.hasNext()){
            curr=((node)iter.next());
            if(targetTotal.contains(curr)){                            
                targetTotal.remove(curr);
                targetPoints+=curr.pathWeight();
            }            
            else{ //penalty for counter-evidence
                if(targetPoints>0)
                    targetPoints-=curr.pathWeight();
            }
        }
        //penalty for non-satisfied evidence
        iter=targetTotal.listIterator();
        while(iter.hasNext()){
            curr=((node)iter.next());
            targetPoints-=curr.pathWeight();
        }
        double proportion=((double)((double)(targetPoints)/(double)(targetTotalSize)));
        if(proportion<0) proportion=0d;
        return proportion;
        
    }
    public int totalLexicality(LinkedList ll){
        int result=0;
        for(int i=0;i<ll.size();i++)
            result+=((node)ll.get(i)).lexicality();
        return result;
    }
    public double totalDSW(LinkedList ll){
        int result=0;
        for(int i=0;i<ll.size();i++)
            result+=((node)ll.get(i)).depthScaledWeight();
        return result;
    }
    public int totalPathWeight(LinkedList ll){
        int result=0;
        for(int i=0;i<ll.size();i++)
            result+=((node)ll.get(i)).pathWeight();
        return result;
    }
    public int totalWeight(LinkedList ll){
        int result=0;
        for(int i=0;i<ll.size();i++)
            result+=((node)ll.get(i)).weight();
        return result;
    }
    public double totalDensity(LinkedList ll){
        double result=0;
        for(int i=0;i<ll.size();i++)
            result+=((node)ll.get(i)).density();
        return result;
    }
    public LinkedList totalEvidenceFor(String word){
        //includes duplicates as each step towards target contains similar items.
        LinkedList result=new LinkedList();
        node curr;
        unmarkAll();
        for(int i=0;i<word.length()&&i<grid[0].length;i++){            
            unmarkAll();        
            //result.addAll(grid[indexOf(labels,word.substring(i,i+1))][i].subsumedNodes());
            result.addAll(grid[indexOf(labels,word.substring(i,i+1))][i].lexicallySubsumedNodes());
            unmarkAll();        
        }
        //System.out.println("totalEvidenceFor("+word+")="+result.size());
        return result;
    }
    public void followPath(String str){
        //walk down the path given in the input string.
        //at each step, add up the "evidence" available and        
        //report the "proportion of evidence" for the TARGET and ENEMY.
        String target=str;
        String enemy=probability.slexEnemies[indexOf(probability.slex,target)];
        unmarkAll();
        LinkedList targetTotal=totalEvidenceFor(target);
        int targetTotalLexicalityScore=0;
        for(int i=0;i<targetTotal.size();i++)
            targetTotalLexicalityScore+=((node)targetTotal.get(i)).weight();
        unmarkAll();
        LinkedList enemyTotal=totalEvidenceFor(probability.slexEnemies[indexOf(probability.slex,target)]);
        int enemyTotalLexicalityScore=0;
        for(int i=0;i<enemyTotal.size();i++)
            enemyTotalLexicalityScore+=((node)enemyTotal.get(i)).weight();
        unmarkAll();
        int subtract=grid[indexOf(labels,"-")][0].subsumedNodes().size();
        unmarkAll();
        LinkedList tot;
        LinkedList evidence=new LinkedList();
        node curr;
        ListIterator iter;
        //System.out.println("target= "+target+"\tenemey= "+probability.slexEnemies[indexOf(lexicon,target)]); 
        int tgtWtpts=0, tgtPoints=0; 
        if(indexOf(probability.NFA, target)>-1) System.out.print("NFA\ttarget\t");
        else System.out.print("FA\ttarget\t");
        System.out.print("\'"+target+"\t");
        for(int i=0;i<target.length();i++){
            evidence.addAll(grid[indexOf(labels,target.substring(i,i+1))][i].subsumedNodes());
            unmarkAll();
            //how much of the targetTotal and enemyTotal's are satisfied by the current evidence?
            tot=cloneList(targetTotal);
            iter=evidence.listIterator();            
            tgtPoints=0;
            tgtWtpts=0;
            while(iter.hasNext()){
                curr=((node)iter.next());
                if(tot.contains(curr)){
                    tot.remove(curr);
                    tgtPoints++;
                    tgtWtpts+=curr.weight();
                }                                                
            }                                    
            String out=(new Double(((double)((double)(tgtPoints-subtract)/(double)(targetTotal.size()-subtract))))).toString();
            if(out.length()>5) out=out.substring(0,5);
            if((((double)((double)(tgtPoints-subtract)/(double)(targetTotal.size()-subtract))))<0.001) out="0.0";
            System.out.print(out+"\t");  
            //System.out.println(i+"\t"+str.substring(0,i+1)+"\t"+target+"\t"+((double)((double)(tgtPoints-87)/(double)(targetTotal.size()-87)))+"\t"+enemy+"\t"+((double)((double)(emyPoints-87)/(double)(enemyTotal.size()-87))));  
            //System.out.println(i+"\t"+str.substring(0,i+1)+"\t"+target+"\t"+((double)((double)(tgtPoints-87)/(double)(targetTotal.size()-87)))+"\t"+((double)((double)(tgtWtpts-6035)/(double)(targetTotalLexicalityScore-6035)))+"\t"+enemy+"\t"+((double)((double)(emyPoints-87)/(double)(enemyTotal.size()-87)))+"\t"+((double)((double)(emyWtpts-6035)/(double)(enemyTotalLexicalityScore-6035))));  
        }
        evidence=new LinkedList();        
        int emyWtpts=0, emyPoints=0;
        if(indexOf(probability.NFA, target)>-1) System.out.print("\nNFA\tenemy\t");
        else System.out.print("\nFA\tenemy\t");
        System.out.print("\'"+enemy+"\t");
        for(int i=0;i<str.length();i++){
            evidence.addAll(grid[indexOf(labels,target.substring(i,i+1))][i].subsumedNodes());
            unmarkAll();
            //how much of enemyTotal is satisfied by the current evidence?            
            tot=cloneList(enemyTotal);
            iter=evidence.listIterator();
            curr=null;
            emyPoints=0;
            emyWtpts=0;
            while(iter.hasNext()){
                curr=((node)iter.next());
                if(tot.contains(curr)){
                    tot.remove(curr);
                    emyPoints++;                    
                    emyWtpts+=curr.weight();
                }                                                
            }
            String out=(new Double(((double)((double)(emyPoints-subtract)/(double)(enemyTotal.size()-subtract))))).toString();
            if(out.length()>5) out=out.substring(0,5);
            if((((double)((double)(emyPoints-subtract)/(double)(enemyTotal.size()-subtract))))<0.001) out="0.0";
            System.out.print(out+"\t");              
            //System.out.println(i+"\t"+str.substring(0,i+1)+"\t"+target+"\t"+((double)((double)(tgtPoints-87)/(double)(targetTotal.size()-87)))+"\t"+((double)((double)(tgtWtpts-6035)/(double)(targetTotalLexicalityScore-6035)))+"\t"+enemy+"\t"+((double)((double)(emyPoints-87)/(double)(enemyTotal.size()-87)))+"\t"+((double)((double)(emyWtpts-6035)/(double)(enemyTotalLexicalityScore-6035))));  
        }
        System.out.print("\n");        
    }    
    public static int indexOf(String[] l,String x){
        for(int i=0;i<l.length;i++)
            if(x.equals(l[i])) return i;
        return -1;
    }
    public void unmarkAll(){
        for(int i=0;i<grid.length;i++)
            for(int j=0;j<grid[i].length;j++){
                if(null==grid[i][j]) continue;
                grid[i][j].unmark();
            }
    }
    public LinkedList cloneList(LinkedList ll){
        LinkedList result=new LinkedList();
        for(int i=0;i<ll.size();i++)
            result.add(ll.get(i));
        return result;
    }
    public String toString(){
        String result="";
        for(int i=0;i<grid[0].length;i++)
            for(int j=0;j<grid.length;j++){
                if(grid[j][i].parents().size()==0&&grid[j][i].children().size()==0) continue;
                result+=i+"x"+j+"\t"+grid[j][i].toString()+"\n";
            }
        return result;
    }
}
class node{
    String[] slex={"-x","-xbrxpt","-xdapt","-xdxlt","-xgri","-xlat","-xpart","-xpil","-ark","-ar","-art","-artxst","-xslip","-bar","-bark","-bi","-bit","-bist","-blak","-blxd","-blu","-bab","-babi","-badi","-bust","-but","-batxl","-baks","-brid","-brud","-brxS","-bxbxl","-bxk","-bxs","-bxt","-kar","-kard","-karpxt","-sis","-klak","-klxb","-klu","-kalig","-kul","-kap","-kapi","-kxpxl","-krip","-kru","-krap","-kruSxl","-kruxl","-krxS","-kxp","-kxt","-dark","-dart","-dil","-did","-dip","-du","-dal","-dat","-dxbxl","-dru","-drap","-drxg","-dxk","-dxl","-dxst","-duti","-ist","-it","-glu","-gad","-gat","-grik","-grit","-gru","-grup","-gard","-gxtar","-kip","-ki","-lid","-lig","-lip","-list","-ligxl","-labi","-lak","-lup","-lus","-lat","-lxk","-lxki","-lxkSxri","-ad","-papx","-park","-part","-parSxl","-partli","-parti","-par","-pi","-pik","-pipxl","-pis","-plat","-plxg","-plxs","-pakxt","-pxlis","-palxsi","-pul","-pap","-pasxbxl","-pasxbli","-pat","-prist","-prabxbxl","-prabxbli","-pradus","-pradxkt","-pragrxs","-pxt","-rid","-ril","-rili","-rab","-rak","-rakxt","-rad","-rut","-rxb","-rxgxd","-rul","-rupi","-rxS","-rxsxl","-skar","-skul","-skru","-sil","-sit","-sikrxt","-si","-sid","-sik","-Sarp","-Si","-Sip","-Sit","-Sild","-Sak","-Sut","-Sap","-Sat","-Srxg","-Sxt","-slip","-slit","-slxg","-salxd","-sari","-spark","-spik","-spid","-spat","-star","-start","-startxl","-stil","-stip","-stak","-stap","-strik","-strit","-strxk","-strxgxl","-stxdid","-stxdi","-stupxd","-sxbstxtut","-sxtxl","-sxksid","-sxk","-su","-sut","-sutxbxl","-tar","-targxt","-ti","-tu","-tul","-tap","-trit","-triti","-tri","-trup","-trat","-trxbxl","-trxk","-tru","-truli","-trxst","-trxsti","-tub","-xgli","-xp","-xs","--"};
    String label;
    LinkedList parents, children;
    
    boolean leaf;
    //boolean root;
    double probability;
    int depth;
    int lexicality;
    int cardinality;
    int weight;
    int pathWeight;
    double depthScaledWeight;
    double density;
    boolean marked;
    
    public node(String lbl,int d){
        marked=false;
        label=lbl;
        depth=d;
        parents=new LinkedList();
        children=new LinkedList();
        pathWeight=0;
    }
    
    public String label(){return label;}
    public int lexicality(){return lexicality;}
    public int weight(){return weight;}
    public int cardinality(){return cardinality;}
    public double density(){return density;}
    public int depth(){return depth;}
    public double probability(){return probability;}
    public void setProbability(double p){probability=p;}
    public double depthScaledWeight(){return depthScaledWeight;}
    public void setDepthScaledWeight(double d){depthScaledWeight=d;}
    public int pathWeight(){return pathWeight;}
    public void setPathWeight(int p){pathWeight=p;}
    public void mark(){marked=true;}
    public void unmark(){marked=false;}
    public boolean isMarked(){return marked;}
    public LinkedList parents(){return parents;}
    public LinkedList children(){return children;}
    
    public void addParent(node p){
        //duplicate check
        for(int i=0;i<parents.size();i++)
            if(((node)parents.get(i))==p) return;
        parents.add(p);
    }
    public void addChild(node c){
        //duplicate check
        for(int i=0;i<children.size();i++)
            if(((node)children.get(i))==c) return;
        children.add(c);
    }    
    public LinkedList subsumedNodes(){
        //includes potential duplicates.
        LinkedList ll=new LinkedList();
        if(isMarked()) return ll;
        ll.add(this);
        if(children.size()==0) return ll;
        for(int i=0;i<children.size();i++)
            ll.addAll(((node)children.get(i)).subsumedNodes());
        mark();
        ll=killDuplicates(ll);
        return ll;
    }
    //return all nodes that would be visited by words have this node
    public LinkedList lexicallySubsumedNodes(){
        LinkedList ll=new LinkedList();
        if(isMarked()) return ll;
        ll.add(this);
        for(int i=0;i<slex.length;i++){
            if(slex[i].length()<depth+1) continue;
            if(label.equals(slex[i].substring(depth,depth+1))){
                if(slex[i].length()==depth+1) break;
                for(int j=0;j<children.size();j++)
                    if(((node)children.get(j)).label().equals(slex[i].substring(depth+1,depth+2)))
                        ll.addAll(((node)children.get(j)).lexicallySubsumedNodes());
            }
        }
        mark();
        ll=killDuplicates(ll);
        return ll;
    }
    public void incrementLexicality(){
        lexicality++;
    }
    public void calcCardinality(){
        cardinality=subsumedNodes().size();        
    }
    public void calcWeight(){
        LinkedList ll=subsumedNodes();
        for(int i=0;i<ll.size();i++)
            weight+=((node)ll.get(i)).lexicality();        
    }
    public void calcDensity(){
        density=(double)((double)weight/(double)cardinality);        
    }
    public boolean isLeaf(){
        return (children.size()==0);
    }
    public static LinkedList killDuplicates(LinkedList ll){
        node curr;
        for(int i=0;i<ll.size();i++){
            if(ll.lastIndexOf(ll.get(i))!=i){
                //duplicate!    
                ll.remove(ll.lastIndexOf(ll.get(i)));
                ll=killDuplicates(ll);
                return ll;
            }
        }        
        /*for(int i=0;i<ll.size();i++){
           curr=(node)ll.get(i);
           for(int j=0;j<ll.size();j++){
               if(i==j) continue;
               if(curr==ll.get(j)) ll.remove(j);
           }
        }*/
        return ll;        
    }
    public String toString(){
        return (label+"\t"+parents.size()+" parent(s)\t"+children.size()+" child(ren)\t"+cardinality+" cardinality\t"+weight+" weight\t"+density+" density\t"+depthScaledWeight+" DSW");
    }
}

//MULTI TREE VERSION HERE
        /*tree[] t=new tree[15];
        String[] sublex;
        int size;
        for(int i=0;i<t.length;i++){
            size=0;
            for(int j=0;j<slex.length;j++){
                if(phonemeLabels[i].equals(slex[j].substring(1,2))){ 
                    //System.out.println(slex[j].substring(1,2));
                    size++;
                }
            }
            sublex=new String[size];
            int k=0;
            for(int j=0;j<slex.length;j++)
                if(phonemeLabels[i].equals(slex[j].substring(1,2))) 
                    sublex[k++]=slex[j];
            t[i]=new tree(sublex,phonemeLabels);
            sublex=null;                                            
        }
        for(int i=0;i<slex.length;i++){
            t[tree.indexOf(phonemeLabels,slex[i].substring(1,2))].followPath(slex[i]);
        }*/

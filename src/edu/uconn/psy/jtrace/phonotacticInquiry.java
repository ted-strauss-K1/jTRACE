
package edu.uconn.psy.jtrace;
import java.sql.*;
import java.util.*;

public class phonotacticInquiry {
        
	public static void main ( String args[] ) throws Exception {
            String slex[]={"-x-","-xbrxpt-","-xdapt-","-xdxlt-","-xgri-","-xlat-","-xpart-","-xpil-","-ark-","-ar-","-art-","-artxst-","-xslip-","-bar-","-bark-","-bi-","-bit-","-bist-","-blak-","-blxd-","-blu-","-bab-","-babi-","-badi-","-bust-","-but-","-batxl-","-baks-","-brid-","-brud-","-brxS-","-bxbxl-","-bxk-","-bxs-","-bxt-","-kar-","-kard-","-karpxt-","-sis-","-klak-","-klxb-","-klu-","-kalig-","-kul-","-kap-","-kapi-","-kxpxl-","-krip-","-kru-","-krap-","-kruSxl-","-kruxl-","-krxS-","-kxp-","-kxt-","-dark-","-dart-","-dil-","-did-","-dip-","-du-","-dal-","-dat-","-dxbxl-","-dru-","-drap-","-drxg-","-dxk-","-dxl-","-dxst-","-duti-","-ist-","-it-","-glu-","-gad-","-gat-","-grik-","-grit-","-gru-","-grup-","-gard-","-gxtar-","-kip-","-ki-","-lid-","-lig-","-lip-","-list-","-ligxl-","-labi-","-lak-","-lup-","-lus-","-lat-","-lxk-","-lxki-","-lxkSxri-","-ad-","-papx-","-park-","-part-","-parSxl-","-partli-","-parti-","-par-","-pi-","-pik-","-pipxl-","-pis-","-plat-","-plxg-","-plxs-","-pakxt-","-pxlis-","-palxsi-","-pul-","-pap-","-pasxbxl-","-pasxbli-","-pat-","-prist-","-prabxbxl-","-prabxbli-","-pradus-","-pradxkt-","-pragrxs-","-pxt-","-rid-","-ril-","-rili-","-rab-","-rak-","-rakxt-","-rad-","-rut-","-rxb-","-rxgxd-","-rul-","-rupi-","-rxS-","-rxsxl-","-skar-","-skul-","-skru-","-sil-","-sit-","-sikrxt-","-si-","-sid-","-sik-","-Sarp-","-Si-","-Sip-","-Sit-","-Sild-","-Sak-","-Sut-","-Sap-","-Sat-","-Srxg-","-Sxt-","-slip-","-slit-","-slxg-","-salxd-","-sari-","-spark-","-spik-","-spid-","-spat-","-star-","-start-","-startxl-","-stil-","-stip-","-stak-","-stap-","-strik-","-strit-","-strxk-","-strxgxl-","-stxdid-","-stxdi-","-stupxd-","-sxbstxtut-","-sxtxl-","-sxksid-","-sxk-","-su-","-sut-","-sutxbxl-","-tar-","-targxt-","-ti-","-tu-","-tul-","-tap-","-trit-","-triti-","-tri-","-trup-","-trat-","-trxbxl-","-trxk-","-tru-","-truli-","-trxst-","-trxsti-","-tub-","-xgli-","-xp-","-xs-","---"};
            String slexEnemies[]={"-xp-","-x-","-x-","-x-","-xgli-","-x-","-xp-","-xp-","-ar-","-ark-","-ar-","-art-","-xs-","-bark-","-bar-","-bit-","-bi-","-bi-","-blu-","-blu-","-brud-","-babi-","-bab-","-babi-","-but-","-bust-","-bab-","-bab-","-brud-","-brid-","-brud-","-bxt-","-bxt-","-bxt-","-bxk-","-kard-","-kar-","-kar-","-si-","-klu-","-klu-","-kru-","-kar-","-skul-","-kapi-","-kap-","-kxp-","-kru-","-skru-","-kru-","-kru-","-kru-","-kru-","-kxpxl-","-kxp-","-dart-","-dark-","-did-","-dip-","-did-","-duti-","-dark-","-dal-","-dxk-","-drap-","-dru-","-dru-","-dxl-","-dxk-","-dxk-","-du-","-bist-","-bit-","-gru-","-gat-","-gad-","-gru-","-gru-","-grup-","-gru-","-gad-","-gat-","-ki-","-kip-","-lig-","-ligxl-","-slip-","-ist-","-lig-","-lak-","-blak-","-lus-","-lup-","-lak-","-lxki-","-lxk-","-lxk-","-gad-","-pap-","-par-","-par-","-par-","-part-","-part-","-park-","-pik-","-pi-","-pi-","-pi-","-lat-","-plxs-","-plxg-","-pap-","-pxt-","-par-","-par-","-papx-","-pasxbli-","-pasxbxl-","-pap-","-pi-","-prabxbli-","-prabxbxl-","-pradxkt-","-pradus-","-pradus-","-pat-","-brid-","-rili-","-ril-","-rad-","-rakxt-","-rak-","-rab-","-rupi-","-rxS-","-rxb-","-rut-","-rut-","-rxb-","-rxS-","-skul-","-skar-","-skar-","-si-","-si-","-sik-","-sis-","-si-","-si-","-Sap-","-Sip-","-Si-","-Si-","-Si-","-Sap-","-Sat-","-Sat-","-Sap-","-rxgxd-","-Sat-","-slit-","-slip-","-slip-","-sari-","-si-","-star-","-spid-","-spik-","-spark-","-start-","-star-","-start-","-stip-","-stil-","-stap-","-stak-","-strit-","-strik-","-strxgxl-","-strxk-","-stxdi-","-stxdid-","-stap-","-sxk-","-sxk-","-sxk-","-sxksid-","-sut-","-su-","-sut-","-targxt-","-tar-","-tu-","-tul-","-tu-","-stap-","-triti-","-trit-","-trit-","-tru-","-tru-","-tru-","-tru-","-trup-","-tru-","-trxsti-","-trxst-","-tu-","-xgri-","-x-","-x-","-xbrxpt-"};
            String NFA[]={"-artxst-","-kard-","-sis-","-klu-","-kalig-","-krip-","-krap-","-kruSxl-","-kruxl-","-kxt-","-glu-","-lxkSxri-","-parSxl-","-partli-","-parti-","-palxsi-","-pul-","-prabxbli-","-skul-","-sil-","-sikrxt-","-sid-","-startxl-","-strxgxl-","-stxdid-","-stupxd-","-sxksid-","-triti-","-trup-","-truli-","-trxsti-"};
            String FA[]={"-xbrxpt-","-xdxlt-","-xdapt-","-xgli-","-xgri-","-xlat-","-xpart-","-xpil-","-xslip-","-ark-","-art-","-bxbxl-","-bxk-","-bxs-","-bxt-","-bab-","-babi-","-badi-","-baks-","-bar-","-bark-","-batxl-","-bist-","-bit-","-blxd-","-blak-","-blu-","-brxS-","-brid-","-brud-","-bust-","-but-","-dxbxl-","-dxk-","-dxl-","-dxst-","-dal-","-dark-","-dart-","-dat-","-did-","-dil-","-dip-","-drxg-","-drap-","-dru-","-duti-","-gxtar-","-gad-","-gard-","-gat-","-grik-","-grit-","-gru-","-grup-","-ist-","-kxp-","-kap-","-kar-","-kip-","-klxb-","-klak-","-kru-","-kul-","-lxk-","-labi-","-lak-","-lat-","-lid-","-lig-","-ligxl-","-lip-","-list-","-lup-","-lus-","-pxt-","-pakxt-","-pap-","-papx-","-par-","-part-","-pasxbxl-","-pat-","-pik-","-pis-","-plxg-","-plxs-","-plat-","-pradxkt-","-pragrxs-","-prist-","-rxb-","-rxgxd-","-rxS-","-rxsxl-","-rab-","-rad-","-rak-","-rid-","-ril-","-rili-","-rul-","-rupi-","-rut-","-sxbstxtut-","-sxk-","-Sxt-","-sxtxl-","-Sak-","-Sap-","-sari-","-Sarp-","-Sat-","-sik-","-Sild-","-Sip-","-sit-","-Sit-","-skar-","-skru-","-slxg-","-slip-","-slit-","-spat-","-spid-","-spik-","-Srxg-","-stxdi-","-stak-","-stap-","-star-","-start-","-stil-","-stip-","-strxk-","-strik-","-strit-","-Sut-","-sut-","-tap-","-tar-","-targxt-","-trxk-","-trxst-","-trat-","-tri-","-trit-","-tru-","-tub-","-tul-"};
            String _slex[] = {"x","xbrxpt","xdapt","xdxlt","xgri","xlat","xpart","xpil","ark","ar","art","artxst","xslip","bar","bark","bi","bit","bist","blak","blxd","blu","bab","babi","badi","bust","but","batxl","baks","brid","brud","brxS","bxbxl","bxk","bxs","bxt","kar","kard","karpxt","sis","klak","klxb","klu","kalig","kul","kap","kapi","kxpxl","krip","kru","krap","kruSxl","kruxl","krxS","kxp","kxt","dark","dart","dil","did","dip","du","dal","dat","dxbxl","dru","drap","drxg","dxk","dxl","dxst","duti","ist","it","glu","gad","gat","grik","grit","gru","grup","gard","gxtar","kip","ki","lid","lig","lip","list","ligxl","labi","lak","lup","lus","lat","lxk","lxki","lxkSxri","ad","papx","park","part","parSxl","partli","parti","par","pi","pik","pipxl","pis","plat","plxg","plxs","pakxt","pxlis","palxsi","pul","pap","pasxbxl","pasxbli","pat","prist","prabxbxl","prabxbli","pradus","pradxkt","pragrxs","pxt","rid","ril","rili","rab","rak","rakxt","rad","rut","rxb","rxgxd","rul","rupi","rxS","rxsxl","skar","skul","skru","sil","sit","sikrxt","si","sid","sik","Sarp","Si","Sip","Sit","Sild","Sak","Sut","Sap","Sat","Srxg","Sxt","slip","slit","slxg","salxd","sari","spark","spik","spid","spat","star","start","startxl","stil","stip","stak","stap","strik","strit","strxk","strxgxl","stxdid","stxdi","stupxd","sxbstxtut","sxtxl","sxksid","sxk","su","sut","sutxbxl","tar","targxt","ti","tu","tul","tap","trit","triti","tri","trup","trat","trxbxl","trxk","tru","truli","trxst","trxsti","tub","xgli","xp","xs","-"};
            String _slexEnemies[] = {"xp","x","x","x","xgli","x","xp","xp","ar","ark","ar","art","xs","bark","bar","bit","bi","bi","blu","blu","brud","babi","bab","babi","but","bust","bab","bab","brud","brid","brud","bxt","bxt","bxt","bxk","kard","kar","kar","si","klu","klu","kru","kar","skul","kapi","kap","kxp","kru","skru","kru","kru","kru","kru","kxpxl","kxp","dart","dark","did","dip","did","duti","dark","dal","dxk","drap","dru","dru","dxl","dxk","dxk","du","bist","bit","gru","gat","gad","gru","gru","grup","gru","gad","gat","ki","kip","lig","ligxl","slip","ist","lig","lak","blak","lus","lup","lak","lxki","lxk","lxk","gad","pap","par","par","par","part","part","park","pik","pi","pi","pi","lat","plxs","plxg","pap","pxt","par","par","papx","pasxbli","pasxbxl","pap","pi","prabxbli","prabxbxl","pradxkt","pradus","pradus","pat","brid","rili","ril","rad","rakxt","rak","rab","rupi","rxS","rxb","rut","rut","rxb","rxS","skul","skar","skar","si","si","sik","sis","si","si","Sap","Sip","Si","Si","Si","Sap","Sat","Sat","Sap","rxgxd","Sat","slit","slip","slip","sari","si","star","spid","spik","spark","start","star","start","stip","stil","stap","stak","strit","strik","strxgxl","strxk","stxdi","stxdid","stap","sxk","sxk","sxk","sxksid","sut","su","sut","targxt","tar","tu","tul","tu","stap","triti","trit","trit","tru","tru","tru","tru","trup","tru","trxsti","trxst","tu","xgri","x","x","xbrxpt"};
            String[] _NFA={"artxst","kard","sis","klu","kalig","krip","krap","kruSxl","kruxl","kxt","glu","lxkSxri","parSxl","partli","parti","palxsi","pul","prabxbli","skul","sil","sikrxt","sid","startxl","strxgxl","stxdid","stupxd","sxksid","triti","trup","truli","trxsti"};
            String phonemeLabels[]={"p", "b", "t", "d", "k", "g", "s", "S", "r", "l", "a", "i", "u", "x", "-"};     

            Tree t=new Tree();
            for(int i=0;i<FA.length;i++){
                //t=new Tree();
                //t.branchCharacteristics(NFA[i],8);
                //t.targetVSenemyTwo(NFA[i],slexEnemies[t.indexOf(slex,NFA[i])],0);
                //System.out.println();
                System.out.print("FA\t#\t");
                t.JIMsDiagnostic(FA[i]);
            }
            /*System.out.println("\n\n");
            SLEX : for(int i=0;i<slex.length;i++){
                for(int j=0;j<NFA.length;j++)
                    if(slex[i].equals(NFA[j])){ continue SLEX;}
                t.branchCharacteristics(slex[i],7);                
           }*/
            //t.branchCharacteristics("-kalig-",6);
            /*for(int i=0;i<NFA.length;i++){
                t=new Tree();
                System.out.println("\n\n");
                System.out.println("**NFA**\t"+NFA[i]+"\tvs.\t"+slexEnemies[t.indexOf(slex,NFA[i])]);
                t.decisionRule(NFA[i]);
            }*/
            //t.decisionRule("-kalig-");
            //t.forkInTheRoad("-kalig-",5);                                                   
           
           /*for(int i=0;i<NFA.length;i++){
                System.out.println("\n");
                System.out.println("**NFA**\t"+NFA[i]+"\tvs.\t"+slexEnemies[t.indexOf(slex,NFA[i])]);
                t.forkInTheRoad(NFA[i],4);                                                   
           }*/
           /*SLEX : for(int i=0;i<slex.length;i++){
                System.out.println("\n");
                for(int j=0;j<NFA.length;j++)
                    if(slex[i].equals(NFA[j])){ continue SLEX;}
                System.out.println(""+slex[i]+"\tvs.\t"+slexEnemies[t.indexOf(slex,slex[i])]);
                t.decisionRule(slex[i]);
                //t.forkInTheRoad(slex[i],4);                                                   
           }*/
                     
            
        }
    }
        class Tree{
            double[][] phonRep={{1,0.76,0.706666667,0.573333333,0.56,0.426666667,0.36,0.213333333,0.22,0.086666667,0.133333333,0.093333333,0.206666667,0.133333333,0.053333333},
            {0.76,1,0.573333333,0.706666667,0.426666667,0.56,0.226666667,0.08,0.22,0.086666667,0.133333333,0.093333333,0.206666667,0.133333333,0.053333333},
            {0.706666667,0.573333333,1,0.76,0.52,0.386666667,0.4,0.2,0.086666667,0.086666667,0.093333333,0.133333333,0.073333333,0.093333333,0.053333333},
            {0.573333333,0.706666667,0.76,1,0.386666667,0.52,0.266666667,0.066666667,0.086666667,0.086666667,0.093333333,0.133333333,0.073333333,0.093333333,0.053333333},
            {0.56,0.426666667,0.52,0.386666667,1,0.76,0.173333333,0.253333333,0.14,0.206666667,0.24,0.04,0.126666667,0.106666667,0},
            {0.426666667,0.56,0.386666667,0.52,0.76,1,0.04,0.12,0.14,0.206666667,0.24,0.04,0.126666667,0.106666667,0},
            {0.36,0.226666667,0.4,0.266666667,0.173333333,0.04,1,0.666666667,0.14,0.14,0.146666667,0.333333333,0.126666667,0.146666667,0.106666667},
            {0.213333333,0.08,0.2,0.066666667,0.253333333,0.12,0.666666667,1,0.126666667,0.246666667,0.146666667,0.133333333,0.3,0.146666667,0.08},
            {0.22,0.22,0.086666667,0.086666667,0.14,0.14,0.14,0.126666667,1,0.8,0.38,0.273333333,0.386666667,0.446666667,0.1},
            {0.086666667,0.086666667,0.086666667,0.086666667,0.206666667,0.206666667,0.14,0.246666667,0.8,1,0.406666667,0.273333333,0.266666667,0.406666667,0.1},
            {0.133333333,0.133333333,0.093333333,0.093333333,0.24,0.24,0.146666667,0.146666667,0.38,0.406666667,1,0.68,0.753333333,0.733333333,0.106666667},
            {0.093333333,0.093333333,0.133333333,0.133333333,0.04,0.04,0.333333333,0.133333333,0.273333333,0.273333333,0.68,1,0.66,0.546666667,0.106666667},
            {0.206666667,0.206666667,0.073333333,0.073333333,0.126666667,0.126666667,0.126666667,0.3,0.386666667,0.266666667,0.753333333,0.66,1,0.62,0.086666667},
            {0.133333333,0.133333333,0.093333333,0.093333333,0.106666667,0.106666667,0.146666667,0.146666667,0.446666667,0.406666667,0.733333333,0.546666667,0.62,1,0.106666667},
            {0.053333333,0.053333333,0.053333333,0.053333333,0,0,0.106666667,0.08,0.1,0.1,0.106666667,0.106666667,0.086666667,0.106666667,1}};
            
            String slex[]={"-x-","-xbrxpt-","-xdapt-","-xdxlt-","-xgri-","-xlat-","-xpart-","-xpil-","-ark-","-ar-","-art-","-artxst-","-xslip-","-bar-","-bark-","-bi-","-bit-","-bist-","-blak-","-blxd-","-blu-","-bab-","-babi-","-badi-","-bust-","-but-","-batxl-","-baks-","-brid-","-brud-","-brxS-","-bxbxl-","-bxk-","-bxs-","-bxt-","-kar-","-kard-","-karpxt-","-sis-","-klak-","-klxb-","-klu-","-kalig-","-kul-","-kap-","-kapi-","-kxpxl-","-krip-","-kru-","-krap-","-kruSxl-","-kruxl-","-krxS-","-kxp-","-kxt-","-dark-","-dart-","-dil-","-did-","-dip-","-du-","-dal-","-dat-","-dxbxl-","-dru-","-drap-","-drxg-","-dxk-","-dxl-","-dxst-","-duti-","-ist-","-it-","-glu-","-gad-","-gat-","-grik-","-grit-","-gru-","-grup-","-gard-","-gxtar-","-kip-","-ki-","-lid-","-lig-","-lip-","-list-","-ligxl-","-labi-","-lak-","-lup-","-lus-","-lat-","-lxk-","-lxki-","-lxkSxri-","-ad-","-papx-","-park-","-part-","-parSxl-","-partli-","-parti-","-par-","-pi-","-pik-","-pipxl-","-pis-","-plat-","-plxg-","-plxs-","-pakxt-","-pxlis-","-palxsi-","-pul-","-pap-","-pasxbxl-","-pasxbli-","-pat-","-prist-","-prabxbxl-","-prabxbli-","-pradus-","-pradxkt-","-pragrxs-","-pxt-","-rid-","-ril-","-rili-","-rab-","-rak-","-rakxt-","-rad-","-rut-","-rxb-","-rxgxd-","-rul-","-rupi-","-rxS-","-rxsxl-","-skar-","-skul-","-skru-","-sil-","-sit-","-sikrxt-","-si-","-sid-","-sik-","-Sarp-","-Si-","-Sip-","-Sit-","-Sild-","-Sak-","-Sut-","-Sap-","-Sat-","-Srxg-","-Sxt-","-slip-","-slit-","-slxg-","-salxd-","-sari-","-spark-","-spik-","-spid-","-spat-","-star-","-start-","-startxl-","-stil-","-stip-","-stak-","-stap-","-strik-","-strit-","-strxk-","-strxgxl-","-stxdid-","-stxdi-","-stupxd-","-sxbstxtut-","-sxtxl-","-sxksid-","-sxk-","-su-","-sut-","-sutxbxl-","-tar-","-targxt-","-ti-","-tu-","-tul-","-tap-","-trit-","-triti-","-tri-","-trup-","-trat-","-trxbxl-","-trxk-","-tru-","-truli-","-trxst-","-trxsti-","-tub-","-xgli-","-xp-","-xs-","---"};
            String slexEnemies[]={"-xp-","-x-","-x-","-x-","-xgli-","-x-","-xp-","-xp-","-ar-","-ark-","-ar-","-art-","-xs-","-bark-","-bar-","-bit-","-bi-","-bi-","-blu-","-blu-","-brud-","-babi-","-bab-","-babi-","-but-","-bust-","-bab-","-bab-","-brud-","-brid-","-brud-","-bxt-","-bxt-","-bxt-","-bxk-","-kard-","-kar-","-kar-","-si-","-klu-","-klu-","-kru-","-kar-","-skul-","-kapi-","-kap-","-kxp-","-kru-","-skru-","-kru-","-kru-","-kru-","-kru-","-kxpxl-","-kxp-","-dart-","-dark-","-did-","-dip-","-did-","-duti-","-dark-","-dal-","-dxk-","-drap-","-dru-","-dru-","-dxl-","-dxk-","-dxk-","-du-","-bist-","-bit-","-gru-","-gat-","-gad-","-gru-","-gru-","-grup-","-gru-","-gad-","-gat-","-ki-","-kip-","-lig-","-ligxl-","-slip-","-ist-","-lig-","-lak-","-blak-","-lus-","-lup-","-lak-","-lxki-","-lxk-","-lxk-","-gad-","-pap-","-par-","-par-","-par-","-part-","-part-","-park-","-pik-","-pi-","-pi-","-pi-","-lat-","-plxs-","-plxg-","-pap-","-pxt-","-par-","-par-","-papx-","-pasxbli-","-pasxbxl-","-pap-","-pi-","-prabxbli-","-prabxbxl-","-pradxkt-","-pradus-","-pradus-","-pat-","-brid-","-rili-","-ril-","-rad-","-rakxt-","-rak-","-rab-","-rupi-","-rxS-","-rxb-","-rut-","-rut-","-rxb-","-rxS-","-skul-","-skar-","-skar-","-si-","-si-","-sik-","-sis-","-si-","-si-","-Sap-","-Sip-","-Si-","-Si-","-Si-","-Sap-","-Sat-","-Sat-","-Sap-","-rxgxd-","-Sat-","-slit-","-slip-","-slip-","-sari-","-si-","-star-","-spid-","-spik-","-spark-","-start-","-star-","-start-","-stip-","-stil-","-stap-","-stak-","-strit-","-strik-","-strxgxl-","-strxk-","-stxdi-","-stxdid-","-stap-","-sxk-","-sxk-","-sxk-","-sxksid-","-sut-","-su-","-sut-","-targxt-","-tar-","-tu-","-tul-","-tu-","-stap-","-triti-","-trit-","-trit-","-tru-","-tru-","-tru-","-tru-","-trup-","-tru-","-trxsti-","-trxst-","-tu-","-xgri-","-x-","-x-","-xbrxpt-"};
            String NFA[]={"-artxst-","-kard-","-sis-","-klu-","-kalig-","-krip-","-krap-","-kruSxl-","-kruxl-","-kxt-","-glu-","-lxkSxri-","-parSxl-","-partli-","-parti-","-palxsi-","-pul-","-prabxbli-","-skul-","-sil-","-sikrxt-","-sid-","-startxl-","-strxgxl-","-stxdid-","-stupxd-","-sxksid-","-triti-","-trup-","-truli-","-trxsti-"};
            String FA[]={"-xbrxpt-","-xdxlt-","-xdapt-","-xgli-","-xgri-","-xlat-","-xpart-","-xpil-","-xslip-","-ark-","-art-","-bxbxl-","-bxk-","-bxs-","-bxt-","-bab-","-babi-","-badi-","-baks-","-bar-","-bark-","-batxl-","-bist-","-bit-","-blxd-","-blak-","-blu-","-brxS-","-brid-","-brud-","-bust-","-but-","-dxbxl-","-dxk-","-dxl-","-dxst-","-dal-","-dark-","-dart-","-dat-","-did-","-dil-","-dip-","-drxg-","-drap-","-dru-","-duti-","-gxtar-","-gad-","-gard-","-gat-","-grik-","-grit-","-gru-","-grup-","-ist-","-kxp-","-kap-","-kar-","-kip-","-klxb-","-klak-","-kru-","-kul-","-lxk-","-labi-","-lak-","-lat-","-lid-","-lig-","-ligxl-","-lip-","-list-","-lup-","-lus-","-pxt-","-pakxt-","-pap-","-papx-","-par-","-part-","-pasxbxl-","-pat-","-pik-","-pis-","-plxg-","-plxs-","-plat-","-pradxkt-","-pragrxs-","-prist-","-rxb-","-rxgxd-","-rxS-","-rxsxl-","-rab-","-rad-","-rak-","-rid-","-ril-","-rili-","-rul-","-rupi-","-rut-","-sxbstxtut-","-sxk-","-Sxt-","-sxtxl-","-Sak-","-Sap-","-sari-","-Sarp-","-Sat-","-sik-","-Sild-","-Sip-","-sit-","-Sit-","-skar-","-skru-","-slxg-","-slip-","-slit-","-spat-","-spid-","-spik-","-Srxg-","-stxdi-","-stak-","-stap-","-star-","-start-","-stil-","-stip-","-strxk-","-strik-","-strit-","-Sut-","-sut-","-tap-","-tar-","-targxt-","-trxk-","-trxst-","-trat-","-tri-","-trit-","-tru-","-tub-","-tul-"};
            String _slex[] = {"x","xbrxpt","xdapt","xdxlt","xgri","xlat","xpart","xpil","ark","ar","art","artxst","xslip","bar","bark","bi","bit","bist","blak","blxd","blu","bab","babi","badi","bust","but","batxl","baks","brid","brud","brxS","bxbxl","bxk","bxs","bxt","kar","kard","karpxt","sis","klak","klxb","klu","kalig","kul","kap","kapi","kxpxl","krip","kru","krap","kruSxl","kruxl","krxS","kxp","kxt","dark","dart","dil","did","dip","du","dal","dat","dxbxl","dru","drap","drxg","dxk","dxl","dxst","duti","ist","it","glu","gad","gat","grik","grit","gru","grup","gard","gxtar","kip","ki","lid","lig","lip","list","ligxl","labi","lak","lup","lus","lat","lxk","lxki","lxkSxri","ad","papx","park","part","parSxl","partli","parti","par","pi","pik","pipxl","pis","plat","plxg","plxs","pakxt","pxlis","palxsi","pul","pap","pasxbxl","pasxbli","pat","prist","prabxbxl","prabxbli","pradus","pradxkt","pragrxs","pxt","rid","ril","rili","rab","rak","rakxt","rad","rut","rxb","rxgxd","rul","rupi","rxS","rxsxl","skar","skul","skru","sil","sit","sikrxt","si","sid","sik","Sarp","Si","Sip","Sit","Sild","Sak","Sut","Sap","Sat","Srxg","Sxt","slip","slit","slxg","salxd","sari","spark","spik","spid","spat","star","start","startxl","stil","stip","stak","stap","strik","strit","strxk","strxgxl","stxdid","stxdi","stupxd","sxbstxtut","sxtxl","sxksid","sxk","su","sut","sutxbxl","tar","targxt","ti","tu","tul","tap","trit","triti","tri","trup","trat","trxbxl","trxk","tru","truli","trxst","trxsti","tub","xgli","xp","xs","-"};
            String _slexEnemies[] = {"xp","x","x","x","xgli","x","xp","xp","ar","ark","ar","art","xs","bark","bar","bit","bi","bi","blu","blu","brud","babi","bab","babi","but","bust","bab","bab","brud","brid","brud","bxt","bxt","bxt","bxk","kard","kar","kar","si","klu","klu","kru","kar","skul","kapi","kap","kxp","kru","skru","kru","kru","kru","kru","kxpxl","kxp","dart","dark","did","dip","did","duti","dark","dal","dxk","drap","dru","dru","dxl","dxk","dxk","du","bist","bit","gru","gat","gad","gru","gru","grup","gru","gad","gat","ki","kip","lig","ligxl","slip","ist","lig","lak","blak","lus","lup","lak","lxki","lxk","lxk","gad","pap","par","par","par","part","part","park","pik","pi","pi","pi","lat","plxs","plxg","pap","pxt","par","par","papx","pasxbli","pasxbxl","pap","pi","prabxbli","prabxbxl","pradxkt","pradus","pradus","pat","brid","rili","ril","rad","rakxt","rak","rab","rupi","rxS","rxb","rut","rut","rxb","rxS","skul","skar","skar","si","si","sik","sis","si","si","Sap","Sip","Si","Si","Si","Sap","Sat","Sat","Sap","rxgxd","Sat","slit","slip","slip","sari","si","star","spid","spik","spark","start","star","start","stip","stil","stap","stak","strit","strik","strxgxl","strxk","stxdi","stxdid","stap","sxk","sxk","sxk","sxksid","sut","su","sut","targxt","tar","tu","tul","tu","stap","triti","trit","trit","tru","tru","tru","tru","trup","tru","trxsti","trxst","tu","xgri","x","x","xbrxpt"};
            String[] _NFA={"artxst","kard","sis","klu","kalig","krip","krap","kruSxl","kruxl","kxt","glu","lxkSxri","parSxl","partli","parti","palxsi","pul","prabxbli","skul","sil","sikrxt","sid","startxl","strxgxl","stxdid","stupxd","sxksid","triti","trup","truli","trxsti"};
            String phonemeLabels[]={"p", "b", "t", "d", "k", "g", "s", "S", "r", "l", "a", "i", "u", "x", "-"};     
            int phoneCount[]={29,22,18,16,21,9,40,11,14,13,5,2,0,12,1};
            Node[] tree;
            public Tree(){
                
                //System.out.println("slex "+slex.length);
                tree=new Node[phonemeLabels.length];            
                for(int i=0;i<tree.length;i++){
                    tree[i]=new Node(null,phonemeLabels[i]);  
                    tree[i].normalizeProbabilities();
                    tree[i].calculateNumNodes();
                    tree[i].calculateWeight();
                    tree[i].calculateTwoStepWeight();
                    tree[i].calculateDensity();
                    tree[i].calculateSumProb();
                    tree[i].calculateLexSumProb();
                }
                
            }
            public void targetVSenemyTwo(String target, String enemy,int verbosity){
                //count up the actual nodes that each word activates and then
                //compare how they'return activation and eliminating interact 
                //while descending the tree towards the target terminal.
                LinkedList l1=new LinkedList();
                LinkedList l2=new LinkedList();
                int idx=0;
                for(int phons=0;phons<phonemeLabels.length;phons++)
                    if(target.substring(0,1).equals(phonemeLabels[phons])){
                        idx=phons;        
                        break;
                    }
                Node tgt=tree[idx];
                Node emy=tree[idx];
                for(int j=0;j<tgt.children().length;j++){
                    if(null==tgt.children()[j]) continue;
                    if(tgt.children()[j].label().equals(target.substring(1,2))){ 
                        tgt=tgt.children()[j];     
                        break;
                    }
                }
                for(int j=0;j<emy.children().length;j++){
                    if(null==emy.children()[j]) continue;
                    if(emy.children()[j].label().equals(enemy.substring(1,2))){ 
                        emy=emy.children()[j];                            
                        break;
                    }
                }
                //add the cumulatively _activated_ nodes to the list
                //l1 will contain 'length' many duplicates of the target terminal
                for(int i=2;i<target.length();i++){
                    l1.addAll((Collection)tgt.subsumedNodes());
                    for(int j=0;j<tgt.children().length;j++){
                        if(null==tgt.children()[j]) continue;
                        if(tgt.children()[j].label().equals(target.substring(i,i+1))){ 
                            tgt=tgt.children()[j];                            
                            break;
                        }
                    }
                }
                //add the cumulatively _activated_ nodes to the list
                //l2 will contain 'length' many duplicates of the enemy terminal
                for(int i=2;i<enemy.length();i++){
                    l2.addAll((Collection)emy.subsumedNodes());
                    for(int j=0;j<emy.children().length;j++){
                        if(null==emy.children()[j]) continue;
                        if(emy.children()[j].label().equals(enemy.substring(i,i+1))){ 
                            emy=emy.children()[j];                            
                            break;
                        }
                    }
                }
                //reset the positions of the tgt and emy nodes
                tgt=tree[idx];
                emy=tree[idx];
                for(int j=0;j<tgt.children().length;j++){
                    if(null==tgt.children()[j]) continue;
                    if(tgt.children()[j].label().equals(target.substring(1,2))){ 
                        tgt=tgt.children()[j];     
                        break;
                    }
                }
                for(int j=0;j<emy.children().length;j++){
                    if(null==emy.children()[j]) continue;
                    if(emy.children()[j].label().equals(enemy.substring(1,2))){ 
                        emy=emy.children()[j];                            
                        break;
                    }
                }
                String s1="target\t"+target+"\t";
                String s2="enemy\t"+enemy+"\t";
                LinkedList tgtList=new LinkedList(), emyList=new LinkedList();
                LinkedList tgtCumList, emyCumList;
                //double accumTgt=0d,totalTgt=0d,accumEmy=0d, totalEmy=0d;                
                int accumTgt=0,totalTgt=0,accumEmy=0,totalEmy=0;
                for(int i=2;i<target.length();i++){  
                    totalTgt=0; totalEmy=0; accumTgt=0; accumEmy=0;
                    tgtCumList=l1;
                    System.out.print(tgt.label()+"\t");
                    for(int j=0;j<tgtCumList.size();j++){
                        //if(((Node)tgtCumList.get(j)).isWord()) 
                        //totalTgt+=((Node)tgtCumList.get(j)).prob();
                        totalTgt+=((Node)tgtCumList.get(j)).cardinality();
                    }
                    emyCumList=l2;
                    for(int j=0;j<emyCumList.size();j++){
                        //if(((Node)emyCumList.get(j)).isWord()) 
                        //totalEmy+=((Node)emyCumList.get(j)).prob();
                        totalEmy+=((Node)emyCumList.get(j)).cardinality();;
                    }
                    tgtList.addAll((Collection)tgt.subsumedNodes());
                    //emyList.addAll((Collection)emy.subsumedNodes()); 
                    
                    //based on the current target path, what proportion of target 
                    //terminals have been satisfied.
                    for(int j=0;j<tgtList.size();j++)
                        for(int k=0;k<tgtCumList.size();k++)
                            if(((Node)tgtList.get(j))==((Node)tgtCumList.get(k))){
                                //((Node)tgtList.get(j)).isWord()&&
                                //accumTgt+=((Node)tgtCumList.get(k)).prob();
                                accumTgt++;
                                tgtCumList.remove(k);
                                break;
                            }
                    //based on the current target path, what proportion of enemy
                    //terminals have been satisfied.
                    for(int j=0;j<tgtList.size();j++)
                        for(int k=0;k<emyCumList.size();k++)
                            if(((Node)tgtList.get(j))==((Node)emyCumList.get(k))){
                                //((Node)tgtList.get(j)).isWord()&&
                                //accumEmy+=((Node)emyCumList.get(k)).prob();
                                accumEmy++;
                                emyCumList.remove(k);
                                break;
                            }
                    
                    //output results
                    //s1+=(new Double((double)accumTgt)).toString()+"\t";
                    //s2+=(new Double((double)accumEmy)).toString()+"\t";
                    s1+=(new Double((double)accumTgt/(double)totalTgt)).toString()+"\t";
                    s2+=(new Double((double)accumEmy/(double)totalEmy)).toString()+"\t";
                            
                    //next step in target path
                    for(int j=0;j<tgt.children().length;j++){
                        if(null==tgt.children()[j]) continue;
                        if(tgt.children()[j].label().equals(target.substring(i,i+1))){ 
                            tgt=tgt.children()[j];                            
                            break;
                        }
                    }
                    //next step in enemy path
                    /*for(int j=0;j<emy.children().length;j++){
                        if(null==emy.children()[j]) continue;
                        if(emy.children()[j].label().equals(enemy.substring(i,i+1))){ 
                            emy=emy.children()[j];                            
                            break;
                        }
                    }*/
                }
                System.out.println("\n"+s1+"\n"+s2);
            }
            public void JIMsDiagnostic(String target){
                int idx=0;
                for(int phons=0;phons<phonemeLabels.length;phons++)
                    if(target.substring(0,1).equals(phonemeLabels[phons])){
                        idx=phons;        
                        break;
                    }
                String[] lbl=new String[target.length()];
                int[] wt=new int[target.length()];
                int[] penalty=new int[target.length()];
                int[] branch=new int[target.length()];
                int[] lexicality=new int[target.length()];
                int[] cd=new int[target.length()];
                double[] density=new double[target.length()];
                //double[] probability=new double[target.length()];
                
                
                Node tgt=tree[idx];                
                double targetWeight=0,maxWeight=0;
                for(int i=0;i<target.length()-1;i++){
                    lbl[i]=tgt.label();
                    wt[i]=tgt.weight();
                    cd[i]=tgt.cardinality();
                    density[i]=(double)((double)wt[i]/(double)cd[i]);
                    if(tgt.isWord()) lexicality[i]=1; 
                    else lexicality[i]=0; 
                    targetWeight=0; maxWeight=0;
                    for(int j=0;j<tgt.children().length;j++){
                        if(null==tgt.children()[j]) continue;
                        else if(tgt.children()[j].isLeaf()&&!tgt.children()[j].isWord()) continue;
                        else branch[i]++;
                        if(tgt.children()[j].weight()>maxWeight) maxWeight=tgt.children()[j].weight();
                        if(tgt.children()[j].label().equals(target.substring(i+1,i+2))) targetWeight=tgt.children()[j].weight();                        
                    }
                    if(targetWeight<maxWeight) penalty[i]=1;
                    else penalty[i]=0;
                    
                    for(int j=0;j<tgt.children().length;j++){
                        if(null==tgt.children()[j]) continue;
                        if(tgt.children()[j].label().equals(target.substring(i+1,i+2))){ 
                            tgt=tgt.children()[j];     
                            break;
                        }
                    }
                }
                //NFA/TIE/FA (in main) #target#iterate labels#iterate weight#iterate binary weight penalty#
                //iterate lexicality#iterate num branches#iterate cardinality#iterate density#(iterate probability)
                System.out.print(target+"\t#\t");
                for(int i=0;i<target.length()-1;i++)
                    System.out.print(lbl[i]+"\t");
                System.out.print("#\t");
                for(int i=0;i<target.length()-1;i++)
                    System.out.print(wt[i]+"\t");
                System.out.print("#\t");
                for(int i=0;i<target.length()-1;i++)
                    System.out.print(penalty[i]+"\t");
                System.out.print("#\t");
                for(int i=0;i<target.length()-1;i++)
                    System.out.print(lexicality[i]+"\t");
                System.out.print("#\t");
                for(int i=0;i<target.length()-1;i++)
                    System.out.print(branch[i]+"\t");
                System.out.print("#\t");
                for(int i=0;i<target.length()-1;i++)
                    System.out.print(cd[i]+"\t");
                System.out.print("#\t");
                for(int i=0;i<target.length()-1;i++)
                    System.out.print(density[i]+"\t");
                System.out.print("#\t");
                System.out.print("\n");
            }
            public void targetVSenemy(String target, String enemy,int verbosity){
                int idx=0;
                for(int phons=0;phons<phonemeLabels.length;phons++)
                    if(target.substring(0,1).equals(phonemeLabels[phons])){
                        idx=phons;        
                        break;
                    }
                //PRE-PROCESSING
                //calculate summed-weight-for-target, summed-weight-for-enemy
                int cumWtTgt=0,cumWtEmy=0;
                //calculate summed-cardinality-for-target, summed-cardinality-for-enemy
                int cumCdTgt=0,cumCdEmy=0;                
                //calculate summed-probability-for-target, summed-probability-for-enemy
                double cumPrbTgt=0d,cumPrbEmy=0d;
                //calculate summed-lexical-probability-for-target, summed-lexical-probability-for-enemy
                double cumLexPrbTgt=0d,cumLexPrbEmy=0d;
                Node tgt=tree[idx];
                Node emy=tree[idx];
                for(int j=0;j<tgt.children().length;j++){
                    if(null==tgt.children()[j]) continue;
                    if(tgt.children()[j].label().equals(target.substring(1,2))){ 
                        tgt=tgt.children()[j];     
                        break;
                    }
                }
                for(int j=0;j<emy.children().length;j++){
                    if(null==emy.children()[j]) continue;
                    if(emy.children()[j].label().equals(enemy.substring(1,2))){ 
                        emy=emy.children()[j];                            
                        break;
                    }
                }
                for(int i=2;i<target.length();i++){
                    cumCdTgt+=tgt.cardinality();
                    cumWtTgt+=tgt.weight();
                    cumPrbTgt+=(double)tgt.sumProb();
                    cumLexPrbTgt+=(double)tgt.sumLexProb();
                    for(int j=0;j<tgt.children().length;j++){
                        if(null==tgt.children()[j]) continue;
                        if(tgt.children()[j].label().equals(target.substring(i,i+1))){ 
                            tgt=tgt.children()[j];                            
                            break;
                        }
                    }
                }
                for(int i=2;i<enemy.length();i++){
                    cumCdEmy+=emy.cardinality();
                    cumWtEmy+=emy.weight();
                    cumPrbEmy+=(double)emy.sumProb();
                    cumLexPrbEmy+=(double)emy.sumLexProb();
                    for(int j=0;j<emy.children().length;j++){
                        if(null==emy.children()[j]) continue;
                        if(emy.children()[j].label().equals(enemy.substring(i,i+1))){ 
                            emy=emy.children()[j];                            
                            break;
                        }
                    }
                }
                
                //END PRE-PROCESSING                
                tgt=tree[idx];
                emy=tree[idx];      
                for(int j=0;j<tgt.children().length;j++){
                    if(null==tgt.children()[j]) continue;
                    if(tgt.children()[j].label().equals(target.substring(1,2))){ 
                        tgt=tgt.children()[j];                            
                        System.out.println(target.substring(1,2));
                        break;
                    }
                }
                for(int j=0;j<emy.children().length;j++){
                    if(null==emy.children()[j]) continue;
                    if(emy.children()[j].label().equals(enemy.substring(1,2))){ 
                        emy=emy.children()[j];                            
                        System.out.println(enemy.substring(1,2));
                        break;
                    }
                }
                String s1="target\t"+target+"\t";
                String s2="enemy\t"+enemy+"\t";
                double accumTgt=0d, accumEmy=0d;
                if(verbosity==0){
                    for(int i=2;i<target.length();i++){
                        //s1+=(double)((double)tgt.weight()/(double)(cumCdTgt-tgt.cardinality()))+"\t";
                        //s2+=(double)((double)emy.weight()/(double)(cumCdEmy-emy.cardinality()))+"\t";
                        //s1+=(double)((double)tgt.sumProb()/(double)cumPrbTgt)+"\t";
                        //s2+=(double)((double)emy.sumProb()/(double)cumPrbEmy)+"\t";
                        accumTgt+=tgt.weight();
                        accumEmy+=tgt.weight(); //**
                        s1+=(double)(accumTgt/(double)cumCdTgt)+"\t";
                        s2+=(double)(accumEmy/(double)cumCdEmy)+"\t";
                        for(int j=0;j<tgt.children().length;j++){
                            if(null==tgt.children()[j]) continue;
                            if(tgt.children()[j].label().equals(target.substring(i,i+1))){ 
                                tgt=tgt.children()[j];                            
                            }
                        }
                        for(int j=0;j<emy.children().length;j++){
                            if(null==emy.children()[j]) continue;
                            if(emy.children()[j].label().equals(enemy.substring(i,i+1))){ 
                                emy=emy.children()[j];                            
                            }
                        }
                    }
                    System.out.println(s1+"\n"+s2);
                }
            }
            public void branchCharacteristics(String target,int verbosity){
                //walk down towards the target, reporting on the target path characteristics,
                //versus alternative path characteristics.
                int idx=0;
                for(int phons=0;phons<phonemeLabels.length;phons++)
                    if(target.substring(0,1).equals(phonemeLabels[phons]))
                        idx=phons;        
                Node curr=tree[idx];
                if(verbosity==0){                    
                    for(int i=1;i<target.length();i++){
                        //System.out.println(curr.isWord());
                        System.out.print(curr.label()+"\t"+curr.cardinality()+"\t"+curr.weight()+"\t"+curr.density());
                        if(curr.isWord()) System.out.println("\t*");
                        else System.out.println("\t");                    
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(curr.children()[j].label().equals(target.substring(i,i+1))){ 
                                curr=curr.children()[j];                            
                            }
                        }
                    }
                }
                //print the target, followed by the avg density, then the followed density.
                else if(verbosity==1){
                    System.out.print(target+"\t");
                    double avgDensity=0;
                    String msg="";
                    for(int i=1;i<target.length();i++){
                        avgDensity+=curr.density();
                        msg+=curr.density()+"\t";
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(curr.children()[j].label().equals(target.substring(i,i+1))){ 
                                curr=curr.children()[j];                            
                            }
                        }
                    }
                    avgDensity/=(double)target.length();
                    System.out.println(avgDensity+"\t\t"+msg);
                }
                else if(verbosity==2){
                    System.out.print(target+"\t");
                    double avg=0;
                    String msg="";
                    for(int i=1;i<target.length();i++){
                        avg+=curr.weight();
                        msg+=curr.weight()+"\t";
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(curr.children()[j].label().equals(target.substring(i,i+1))){ 
                                curr=curr.children()[j];                            
                            }
                        }
                    }
                    avg/=(double)target.length();
                    System.out.println(avg+"\t\t"+msg);
                }
                else if(verbosity==3){
                    System.out.print(target+"\t");
                    double avg=0;
                    String msg="";
                    for(int i=1;i<target.length();i++){
                        avg+=curr.cardinality();
                        msg+=curr.cardinality()+"\t";
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(curr.children()[j].label().equals(target.substring(i,i+1))){ 
                                curr=curr.children()[j];                            
                            }
                        }
                    }
                    avg/=(double)target.length();
                    System.out.println(avg+"\t\t"+msg);
                }
                //print the target-path density divided by the (next-)heaviest-path density, while walking down
                else if(verbosity==4){
                    double targetDensity;
                    double competDensity;
                    Node next=curr;
                    for(int i=1;i<target.length();i++){                    
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(curr.children()[j].label().equals(target.substring(i,i+1))){ 
                                next=curr.children()[j];                            
                            }
                        }
                        targetDensity=next.density();                        

                        competDensity=0;
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(next==curr.children()[j]) continue;
                            if(curr.children()[j].density()>competDensity){ 
                                competDensity=curr.children()[j].density();
                            }
                        }
                        double densityRatio=targetDensity/competDensity;
                        System.out.print(densityRatio);
                        if(curr.weight()==1) System.out.print(" *\t");
                        else System.out.print("\t");
                        curr=next;
                    }
                }
                //5 is like 4 but more verbose
                //print the target-path density divided by the (next-)heaviest-path density, while walking down
                else if(verbosity==5){
                    double targetDensity;
                    double competDensity;
                    Node next=curr;
                    Node comp=curr;
                    System.out.print(target+"\t");
                    for(int i=1;i<target.length();i++){                    
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(curr.children()[j].label().equals(target.substring(i,i+1))){ 
                                next=curr.children()[j];                            
                            }
                        }
                        targetDensity=next.density();
                        System.out.print("("+targetDensity+" "+next.label()+" : ");
                        
                        competDensity=0;
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(next==curr.children()[j]) continue;
                            if(curr.children()[j].density()>competDensity){ 
                                competDensity=curr.children()[j].density();                            
                                comp=curr.children()[j];
                            }
                        }
                        System.out.print(competDensity+" "+comp.label());                                                
                        //double densityRatio=targetDensity/competDensity;
                        //System.out.print(densityRatio);
                        if(next.weight()==1) System.out.print(" *)\t");
                        else System.out.print(")\t");
                        curr=next;
                    }
                }
                else if(verbosity==6){
                    double targetWeight;
                    double competWeight;
                    Node next=curr;
                    Node comp=curr;
                    System.out.print(target+"\t");
                    Node UP=curr;
                    for(int i=1;i<target.length();i++){                    
                        for(int j=0;j<UP.children().length;j++){
                            if(null==UP.children()[j]) continue;
                            if(UP.children()[j].label().equals(target.substring(i,i+1))){ 
                                UP=UP.children()[j];
                                break;
                            }                            
                        }
                        if(UP.weight()==1){ System.out.print(""+i+"\t\t"); break;}
                        else if(i==target.length()-1) System.out.print(""+target.length()+"\t\t");
                    }
                    for(int i=1;i<target.length();i++){                    
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(curr.children()[j].label().equals(target.substring(i,i+1))){ 
                                next=curr.children()[j];                            
                                break;
                            }
                        }
                        targetWeight=next.weight();
                        //System.out.print("("+targetWeight+" "+next.label()+" : ");
                        
                        competWeight=0;
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(next==curr.children()[j]) continue;
                            if(curr.children()[j].weight()>competWeight){ 
                                competWeight=curr.children()[j].weight();                            
                                comp=curr.children()[j];
                            }
                        }
                        double weightRatio=targetWeight/competWeight;
                        //System.out.print(competWeight+" "+comp.label());                                                
                        //if(next.weight()==1) System.out.print(" *)\t");
                        //else System.out.print(")\t");
                        if(comp.weight()==0||next.weight()==1) break;
                        System.out.print(weightRatio+"\t");                        
                        curr=next;
                    }
                    System.out.print("\n");
                }
                //print the target-path _weight_ divided by the (next-)heaviest-path _weight_, while walking down
                else if(verbosity==7){
                    int targetWeight;
                    int competWeight;
                    Node next=curr;
                    Node comp=curr;
                    System.out.print(target+"\t");
                    for(int i=1;i<target.length();i++){                    
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(curr.children()[j].label().equals(target.substring(i,i+1))){ 
                                next=curr.children()[j];                            
                            }
                        }
                        targetWeight=next.twoStepWeight();
                        System.out.print("("+targetWeight+" "+next.label()+" : ");
                        
                        competWeight=0;
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(next==curr.children()[j]) continue;
                            if(curr.children()[j].twoStepWeight()>competWeight){ 
                                competWeight=curr.children()[j].twoStepWeight();                            
                                comp=curr.children()[j];
                            }
                        }
                        System.out.print(competWeight+" "+comp.label());                                                
                        //if(next.weight()==1) System.out.print(" *)\t");
                        System.out.print(")\t");
                        curr=next;
                    }
                }
                //give them sum probability as walking towards the target.
                //asterix identifies post-UP
                else if(verbosity==8){
                    System.out.print(target+"\t");
                    
                    String msg="";
                    for(int i=1;i<target.length();i++){
                        //System.out.print((curr.sumProb())+"\t");                        
                        System.out.print((new Double(curr.sumProb())).toString().substring(0,5)+"\t");                        
                        for(int j=0;j<curr.children().length;j++){
                            if(null==curr.children()[j]) continue;
                            if(curr.children()[j].label().equals(target.substring(i,i+1))){ 
                                curr=curr.children()[j];                            
                            }
                        }
                    }                                        
                }
            }
            public void forkInTheRoad(String str,int verbosity){
                int idx=0;
                for(int phons=0;phons<phonemeLabels.length;phons++)
                    if(str.substring(0,1).equals(phonemeLabels[phons]))
                        idx=phons;                    
                for(int expose=0;expose<str.length();expose++){
                    System.out.print("\""+tree[idx].lookup(str.substring(0,expose+1)).rawCharPath()+"\": \t");
                    //decisionRule(String target,String[] lst,double[] prob,int verbosity){
                    prototype(tree[idx].lookup(str.substring(0,expose+1)).subsumedWords(),tree[idx].lookup(str.substring(0,expose+1)).subsumedProbabilities(),verbosity);
                    
                }
            }
            public void diagnostic(boolean wordsAndBranches){
                for(int i=0;i<tree.length;i++)
                    tree[i].diagnostic(wordsAndBranches);                                    
            }
            public int totalWords(){                
                int total=0;
                for(int i=0;i<tree.length;i++)
                    total+=tree[i].lexicalCardinality();                    
                return total;
            }
            public double probabilityLookup(String str){
                for(int i=0;i<tree.length;i++)
                    if(tree[i].lookup(str)!=null) return tree[i].lookup(str).prob();
                return -1;            
            }
            public void showBranches(String str){
                for(int i=0;i<tree.length;i++)
                    if(tree[i].lookup(str)!=null) tree[i].lookup(str).diagnostic(true);                
            }
            public void showLexicalBranches(String str){
                for(int i=0;i<tree.length;i++)
                    if(tree[i].lookup(str)!=null) tree[i].lookup(str).diagnostic(false);                
            }
            public void enemyComparison(){
                for(int i=0;i<slex.length;i++)
                    System.out.println(slex[i]+"\t"+probabilityLookup(slex[i])+"\t"+slexEnemies[i]+"\t"+probabilityLookup(slexEnemies[i]));
            }
            public void decisionRule(String target){
                //this.decisionRule(str, tree[idx].lookup(str.substring(0,2)).subsumedWords(), tree[idx].lookup(str.substring(0,2)).subsumedProbabilities(),0);
                String[] sublex={""};
                double[][] lexicalActivations=new double[target.length()][slex.length];                      
                int idx=0;
                for(int phons=0;phons<phonemeLabels.length;phons++)
                    if(target.substring(0,1).equals(phonemeLabels[phons]))
                        idx=phons;
                String[] lst;
                double[] prob;
                for(int expose=0;expose<target.length();expose++){
                    lst = tree[idx].lookup(target.substring(0,expose+1)).subsumedWords();
                    prob = tree[idx].lookup(target.substring(0,expose+1)).subsumedProbabilities();
                
                    //calculate phoneme probability grid from input-so-far
                    int x=lst.length;
                    int y=0;
                    //grid is as wide as the longest word in lexicon                    
                    for(int i=0;i<x;i++)
                        if(lst[i].length()>y) y=lst[i].length(); 
                    String[][] charGrid=new String[x][y];
                    for(int i=0;i<x;i++)
                        for(int j=0;j<y;j++)
                            if(j<lst[i].length()) 
                                charGrid[i][j]=lst[i].substring(j,j+1);
                            else
                                charGrid[i][j]=".";

                    double[][] probabilityGrid=new double[phonemeLabels.length][y];
                    int[] cardinalityGrid=new int[y];
                    double[] normrow=new double[y];
                    int maxInRow;
                    for(int j=0;j<y;j++){
                        //norm row: calculate how much this row of phonemes will add up to.
                        normrow[j]=0d;
                        for(int i=0;i<x;i++){
                            if(!charGrid[i][j].equals(".")) normrow[j]+=prob[i];
                        }
                        //compute the positional phoneme-probability grid: currently not normalizing here.
                        for(int wd=0;wd<x;wd++)
                            for(int ph=0;ph<phonemeLabels.length;ph++)
                                if(charGrid[wd][j].equals(phonemeLabels[ph])){
                                    probabilityGrid[ph][j]+=(prob[wd]);       
                                    cardinalityGrid[j]++;
                                }
                    }
                    //normalize for each column (which represents word length)
                    for(int j=0;j<y;j++)
                        for(int ph=0;ph<phonemeLabels.length;ph++)
                            probabilityGrid[ph][j]/=normrow[j];

                    //now generate the lexical activations
                    double[][] phonemeGrid=probabilityGrid;
                    double window;
                    double alpha=0.95d;
                    sublex=lst;                   
                    //String[] sublex={""};
                    for(int i=0;i<phonemeGrid.length;i++){
                        if(phonemeGrid[i][0]>0.95d){ sublex=tree[i].subsumedWords(); break;}
                        else if(i==phonemeGrid.length-1) sublex=slex;                    
                    }
                    for(int i=0;i<sublex.length;i++){
                        window=0;                            
                        for(int j=phonemeGrid[0].length-1;j>-1;j--){
                            double penalty=0;
                            for(int ph=0;ph<phonemeGrid.length;ph++){                            
                                //bottom-up activation; with no notion of phoneme similarity, only identity                                                        
                                penalty+=phonemeGrid[ph][j];                            
                                if(j<sublex[i].length()&&sublex[i].substring(j,j+1).equals(phonemeLabels[ph])){ 
                                    window+=phonemeGrid[ph][j];
                                    window*=alpha; //this implements a word onset bias.
                                }
                                //if this phone-position has no match, penalize activation score.
                                if(ph==phonemeGrid.length-1){
                                    penalty/=phonemeGrid.length;
                                    window-=penalty;
                                    window*=alpha; //this implements a word onset bias.
                                }
                            }                        
                        }
                        window/=sublex[i].length();  //word-length normalization
                        lexicalActivations[expose][i]=window;
                    }
                }
                //now identifty the target in the sublexicon
                int targetIdx=-1;
                for(int i=0;i<sublex.length;i++)
                    if(target.equals(sublex[i])){ 
                        targetIdx=i; break;
                    }
                if(targetIdx==-1){
                    System.out.println("target "+target+" doesn't seem to be here; aborting.");
                    return;                    
                }
                //select top ten most active items here:
                int[] topTenPeaks=new int[10];
                double min=0;
                double max;
                int maxIdx=0;
                for(int i=0;i<10;i++){
                    max=min;                
                    J : for(int j=0;j<lexicalActivations[0].length;j++){
                       K : for(int k=0;k<lexicalActivations.length;k++){
                            if(indexOf(topTenPeaks,j)!=-1) continue J;
                            if(lexicalActivations[k][j]>max&&indexOf(topTenPeaks,j)==-1){
                                max=lexicalActivations[k][j];
                                maxIdx=j;
                            }
                       }
                    }
                    topTenPeaks[i]=maxIdx;
                }
                for(int i=0;i<10;i++){
                   System.out.print(sublex[topTenPeaks[i]]+"\t");                    
                   for(int j=0;j<lexicalActivations.length;j++){
                       System.out.print(lexicalActivations[j][topTenPeaks[i]]+"\t");
                   }
                   System.out.print("\n");                                                       
                }
                
                
                /*int strongerEqualTarget=0,weakerThanTarget=0;
                for(int i=0;i<sublex.length;i++){
                    if(i==targetIdx) continue;
                    if(lexicalActivations[i]>=lexicalActivations[targetIdx]) strongerEqualTarget++;
                    else weakerThanTarget++;
                }
                System.out.println(weakerThanTarget+"\t<\t"+target+"\t<=\t"+strongerEqualTarget);*/
                
            }
            
            public void prototype(String[] lst,double[] prob,int verbosity){
                int x=lst.length;
                int y=0;
                //grid is as wide as the longest word in lexicon                    
                for(int i=0;i<x;i++)
                    if(lst[i].length()>y) y=lst[i].length(); 
                String[][] charGrid=new String[x][y];
                for(int i=0;i<x;i++)
                    for(int j=0;j<y;j++)
                        if(j<lst[i].length()) 
                            charGrid[i][j]=lst[i].substring(j,j+1);
                        else
                            charGrid[i][j]=".";
                
                double[][] probabilityGrid=new double[phonemeLabels.length][y];
                int[] cardinalityGrid=new int[y];
                double[] normrow=new double[y];
                int maxInRow;
                for(int j=0;j<y;j++){
                    //norm row: calculate how much this row of phonemes will add up to.
                    normrow[j]=0d;
                    for(int i=0;i<x;i++){
                        if(!charGrid[i][j].equals(".")) normrow[j]+=prob[i];
                    }
                    //compute the positional phoneme-probability grid: currently not normalizing here.
                    for(int wd=0;wd<x;wd++)
                        for(int ph=0;ph<phonemeLabels.length;ph++)
                            if(charGrid[wd][j].equals(phonemeLabels[ph])){
                                probabilityGrid[ph][j]+=(prob[wd]);       
                                cardinalityGrid[j]++;
                            }
                    maxInRow=0;
                    if(verbosity==2){
                        System.out.print("(");
                        //show all phonemes in the row
                        for(int ph=0;ph<phonemeLabels.length;ph++){
                            if(probabilityGrid[ph][j]>0)
                                if((new Double(probabilityGrid[ph][j])).toString().length()>4)
                                    System.out.print(phonemeLabels[ph]+" "+(new Double(probabilityGrid[ph][j])).toString().substring(0,5)+", ");
                                else
                                    System.out.print(phonemeLabels[ph]+" "+(new Double(probabilityGrid[ph][j])).toString()+", ");
                        }
                        //show the sum of that phoneme row.
                        if((new Double(normrow[j])).toString().length()>4)
                            System.out.print("/"+(new Double(normrow[j])).toString().substring(0,5));                                                                                        
                        else
                            System.out.print("/"+normrow[j]);                                                                                   
                        System.out.print(")");
                    }
                    else if(verbosity==1){
                        System.out.print("(");
                        //show highest probability phoneme : i.e. the prototypical one 
                        for(int ph=0;ph<phonemeLabels.length;ph++){
                            if(probabilityGrid[ph][j]>probabilityGrid[maxInRow][j])
                                maxInRow=ph;                            
                        }
                        if((new Double(probabilityGrid[maxInRow][j])).toString().length()>4)
                            System.out.print(phonemeLabels[maxInRow]+" "+(new Double(probabilityGrid[maxInRow][j])).toString().substring(0,5)+" ");
                        else
                            System.out.print(phonemeLabels[maxInRow]+" "+(new Double(probabilityGrid[maxInRow][j])).toString()+" ");
                        
                        //show the sum of that phoneme row.
                        if((new Double(normrow[j])).toString().length()>4)
                            System.out.print("/"+(new Double(normrow[j])).toString().substring(0,5));                                                                                        
                        else
                            System.out.print("/"+normrow[j]);
                        System.out.print(")");
                    }
                    else if(verbosity==0){
                        //show normalized max-phone representation.
                        for(int ph=0;ph<phonemeLabels.length;ph++)
                            if(probabilityGrid[ph][j]>probabilityGrid[maxInRow][j])
                                maxInRow=ph;                                                    
                        int howManyAtMax=0;
                        for(int ph=0;ph<phonemeLabels.length;ph++)                            
                            if(probabilityGrid[ph][j]==probabilityGrid[maxInRow][j])
                                howManyAtMax++;     
                        
                        double maxInRowProbability=(probabilityGrid[maxInRow][j]/normrow[j]);
                        if(howManyAtMax>1){
                            for(int ph=0;ph<phonemeLabels.length;ph++)                            
                                if(probabilityGrid[ph][j]==probabilityGrid[maxInRow][j])                        
                                    System.out.print(phonemeLabels[ph]+" ");                        
                        }
                        else System.out.print(phonemeLabels[maxInRow]+" ");                        
                        System.out.print("\t"+(new Double(100*maxInRowProbability)).intValue()+"\t"+cardinalityGrid[j]+"\t");                                                                        
                    }
                    else if(verbosity==3){ //shows only the number of phoneme 'signals' sent to each position in the word
                        //this is to be used to look for a short word advantage via more long word inhibition.
                        System.out.print("\t"+cardinalityGrid[j]);                                                                                            
                    }
                }
                if(verbosity==4){
                    //first normalize each row of the probability grid:
                    for(int j=0;j<y;j++)
                        for(int ph=0;ph<phonemeLabels.length;ph++)
                            probabilityGrid[ph][j]/=normrow[j];
                    mostActiveWords(probabilityGrid,7,lst);                    
                }
                if(verbosity==5){
                    //first normalize each row of the probability grid:
                    for(int j=0;j<y;j++){
                        for(int ph=0;ph<phonemeLabels.length;ph++){
                            probabilityGrid[ph][j]/=normrow[j];                            
                        }
                    }
                    mostActiveWords(probabilityGrid,-1,lst);                    
                }
                if(verbosity==6){
                    //first normalize each row of the probability grid:
                    for(int j=0;j<y;j++){
                        for(int ph=0;ph<phonemeLabels.length;ph++){
                            probabilityGrid[ph][j]/=normrow[j];                            
                        }
                    }
                    System.out.print("\n\n");
                    for(int ph=0;ph<phonemeLabels.length;ph++){
                        System.out.print(phonemeLabels[ph]+"\t");
                        for(int j=0;j<y;j++){
                            if(probabilityGrid[ph][j]==0) System.out.print("0\t  ");
                            else if((new Double(probabilityGrid[ph][j])).toString().length()>4)
                                System.out.print((new Double(probabilityGrid[ph][j])).toString().substring(0,5)+"\t  ");
                            else
                                System.out.print(probabilityGrid[ph][j]+"\t  ");                                                                                                    
                        }
                        System.out.print("\n");
                    }
                    System.out.print("\n");                                        
                }
                if(verbosity==7){
                    
                }
                System.out.println();                                
            }
            public String[] mostActiveWords(double[][] phonemeGrid, int n,String[] lst){
                //simple ways of understanding how the phoneme probabilities should
                //activate word representations.                                
                
                String[] sublex=lst;                
                //String[] sublex={""};
                for(int i=0;i<phonemeGrid.length;i++){
                    if(phonemeGrid[i][0]>0.95d){ sublex=tree[i].subsumedWords(); break;}
                    else if(i==phonemeGrid.length-1) sublex=slex;                    
                }
                //System.out.print("\n");
                double alpha = 0.95d; //constant for scaling running average value.
                double[] lexicalActivations=new double[sublex.length];
                
                int[] topTen;
                double[] topAct;
                int num;
                if(n>0){
                    topTen=new int[n];
                    topAct=new double[n];                                
                    num=n;
                }
                else{
                    topTen=new int[sublex.length];
                    topAct=new double[sublex.length];                                
                    num=sublex.length;
                }
                double window;
                for(int i=0;i<sublex.length;i++){
                    window=0;                            
                    for(int j=phonemeGrid[0].length-1;j>-1;j--){
                    //for(int j=0;j<phonemeGrid[0].length;j++){
                        double penalty=0;
                        for(int ph=0;ph<phonemeGrid.length;ph++){
                            //bottom-up activation; with phoneme similarity from the TRACE phon metric
                            /*if(j>=sublex[i].length()) window+=(phonemeGrid[ph][j]*phonRep[indexOf(phonemeLabels,"-")][ph]);
                            else window+=(phonemeGrid[ph][j]*phonRep[indexOf(phonemeLabels,sublex[i].substring(j,j+1))][ph]);
                            window*=alpha; //this implements a word onset bias.
                            */
                            
                            //bottom-up activation; with no notion of phoneme similarity, only identity                                                        
                            penalty+=phonemeGrid[ph][j];                            
                            if(j<sublex[i].length()&&sublex[i].substring(j,j+1).equals(phonemeLabels[ph])){ 
                                window+=phonemeGrid[ph][j];
                                window*=alpha; //this implements a word onset bias.
                            }
                            //if this phone-position has no match, penalize activation score.
                            if(ph==phonemeGrid.length-1){
                                penalty/=phonemeGrid.length;
                                window-=penalty;
                                window*=alpha; //this implements a word onset bias.
                            }
                        }                        
                    }
                    window/=sublex[i].length();
                    lexicalActivations[i]=window;
                }
                int min=0;
                for(int i=0;i<sublex.length;i++)
                    if(lexicalActivations[i]<lexicalActivations[min])
                        min=i;                      
                if(n>0){
                    for(int top=0;top<num&&top<sublex.length;top++){
                        int max=min;
                        for(int i=0;i<sublex.length;i++)
                            if(lexicalActivations[i]>lexicalActivations[max])
                                max=i;
                        topTen[top]=max;
                        topAct[top]=lexicalActivations[max];
                        lexicalActivations[max]=-1;
                    }
                    String[] result=new String[num];
                    for(int top=0;top<num&&top<sublex.length;top++){
                        result[top]=sublex[topTen[top]];
                        System.out.print(sublex[topTen[top]]+"="+(new Double(topAct[top])).toString().substring(0,6)+"\t");
                    }
                    return result;
                }
                else{
                    double _min=0d;
                    for(int top=0;top<sublex.length;top++)
                        if(lexicalActivations[top]<_min) _min=lexicalActivations[top];
                    for(int top=0;top<sublex.length;top++)
                        lexicalActivations[top]+=_min;
                    double rowTotal=0;
                    for(int top=0;top<sublex.length;top++)
                        rowTotal+=lexicalActivations[top];                                        
                    for(int top=0;top<sublex.length;top++)
                        topAct[top]=lexicalActivations[top]/rowTotal;                        
                    for(int i=0;i<sublex.length;i++){
                        System.out.print(sublex[i]+"\t");
                    }
                    System.out.println();
                    System.out.print("\t\t\t");
                    for(int top=0;top<num&&top<sublex.length;top++){
                        if((new Double(topAct[top])).toString().length()>7)
                            System.out.print((new Double(topAct[top])).toString().substring(0,7)+"\t");
                        else System.out.print(topAct[top]+"\t");
                    }
                    return sublex;                    
                }                
            }
            public int indexOf(String[] lst,String item){
                for(int i=0;i<lst.length;i++)
                    if(item.equals(lst[i])) return i;
                return -1;
            
            }
            public int indexOf(int[] lst,int item){
                for(int i=0;i<lst.length;i++)
                    if(item==lst[i]) return i;
                return -1;
            }
        
        }
        class Node{
            String slex[]={"-x-","-xbrxpt-","-xdapt-","-xdxlt-","-xgri-","-xlat-","-xpart-","-xpil-","-ark-","-ar-","-art-","-artxst-","-xslip-","-bar-","-bark-","-bi-","-bit-","-bist-","-blak-","-blxd-","-blu-","-bab-","-babi-","-badi-","-bust-","-but-","-batxl-","-baks-","-brid-","-brud-","-brxS-","-bxbxl-","-bxk-","-bxs-","-bxt-","-kar-","-kard-","-karpxt-","-sis-","-klak-","-klxb-","-klu-","-kalig-","-kul-","-kap-","-kapi-","-kxpxl-","-krip-","-kru-","-krap-","-kruSxl-","-kruxl-","-krxS-","-kxp-","-kxt-","-dark-","-dart-","-dil-","-did-","-dip-","-du-","-dal-","-dat-","-dxbxl-","-dru-","-drap-","-drxg-","-dxk-","-dxl-","-dxst-","-duti-","-ist-","-it-","-glu-","-gad-","-gat-","-grik-","-grit-","-gru-","-grup-","-gard-","-gxtar-","-kip-","-ki-","-lid-","-lig-","-lip-","-list-","-ligxl-","-labi-","-lak-","-lup-","-lus-","-lat-","-lxk-","-lxki-","-lxkSxri-","-ad-","-papx-","-park-","-part-","-parSxl-","-partli-","-parti-","-par-","-pi-","-pik-","-pipxl-","-pis-","-plat-","-plxg-","-plxs-","-pakxt-","-pxlis-","-palxsi-","-pul-","-pap-","-pasxbxl-","-pasxbli-","-pat-","-prist-","-prabxbxl-","-prabxbli-","-pradus-","-pradxkt-","-pragrxs-","-pxt-","-rid-","-ril-","-rili-","-rab-","-rak-","-rakxt-","-rad-","-rut-","-rxb-","-rxgxd-","-rul-","-rupi-","-rxS-","-rxsxl-","-skar-","-skul-","-skru-","-sil-","-sit-","-sikrxt-","-si-","-sid-","-sik-","-Sarp-","-Si-","-Sip-","-Sit-","-Sild-","-Sak-","-Sut-","-Sap-","-Sat-","-Srxg-","-Sxt-","-slip-","-slit-","-slxg-","-salxd-","-sari-","-spark-","-spik-","-spid-","-spat-","-star-","-start-","-startxl-","-stil-","-stip-","-stak-","-stap-","-strik-","-strit-","-strxk-","-strxgxl-","-stxdid-","-stxdi-","-stupxd-","-sxbstxtut-","-sxtxl-","-sxksid-","-sxk-","-su-","-sut-","-sutxbxl-","-tar-","-targxt-","-ti-","-tu-","-tul-","-tap-","-trit-","-triti-","-tri-","-trup-","-trat-","-trxbxl-","-trxk-","-tru-","-truli-","-trxst-","-trxsti-","-tub-","-xgli-","-xp-","-xs-","---"};
            String slexEnemies[]={"-xp-","-x-","-x-","-x-","-xgli-","-x-","-xp-","-xp-","-ar-","-ark-","-ar-","-art-","-xs-","-bark-","-bar-","-bit-","-bi-","-bi-","-blu-","-blu-","-brud-","-babi-","-bab-","-babi-","-but-","-bust-","-bab-","-bab-","-brud-","-brid-","-brud-","-bxt-","-bxt-","-bxt-","-bxk-","-kard-","-kar-","-kar-","-si-","-klu-","-klu-","-kru-","-kar-","-skul-","-kapi-","-kap-","-kxp-","-kru-","-skru-","-kru-","-kru-","-kru-","-kru-","-kxpxl-","-kxp-","-dart-","-dark-","-did-","-dip-","-did-","-duti-","-dark-","-dal-","-dxk-","-drap-","-dru-","-dru-","-dxl-","-dxk-","-dxk-","-du-","-bist-","-bit-","-gru-","-gat-","-gad-","-gru-","-gru-","-grup-","-gru-","-gad-","-gat-","-ki-","-kip-","-lig-","-ligxl-","-slip-","-ist-","-lig-","-lak-","-blak-","-lus-","-lup-","-lak-","-lxki-","-lxk-","-lxk-","-gad-","-pap-","-par-","-par-","-par-","-part-","-part-","-park-","-pik-","-pi-","-pi-","-pi-","-lat-","-plxs-","-plxg-","-pap-","-pxt-","-par-","-par-","-papx-","-pasxbli-","-pasxbxl-","-pap-","-pi-","-prabxbli-","-prabxbxl-","-pradxkt-","-pradus-","-pradus-","-pat-","-brid-","-rili-","-ril-","-rad-","-rakxt-","-rak-","-rab-","-rupi-","-rxS-","-rxb-","-rut-","-rut-","-rxb-","-rxS-","-skul-","-skar-","-skar-","-si-","-si-","-sik-","-sis-","-si-","-si-","-Sap-","-Sip-","-Si-","-Si-","-Si-","-Sap-","-Sat-","-Sat-","-Sap-","-rxgxd-","-Sat-","-slit-","-slip-","-slip-","-sari-","-si-","-star-","-spid-","-spik-","-spark-","-start-","-star-","-start-","-stip-","-stil-","-stap-","-stak-","-strit-","-strik-","-strxgxl-","-strxk-","-stxdi-","-stxdid-","-stap-","-sxk-","-sxk-","-sxk-","-sxksid-","-sut-","-su-","-sut-","-targxt-","-tar-","-tu-","-tul-","-tu-","-stap-","-triti-","-trit-","-trit-","-tru-","-tru-","-tru-","-tru-","-trup-","-tru-","-trxsti-","-trxst-","-tu-","-xgri-","-x-","-x-","-xbrxpt-"};
            String NFA[]={"-artxst-","-kard-","-sis-","-klu-","-kalig-","-krip-","-krap-","-kruSxl-","-kruxl-","-kxt-","-glu-","-lxkSxri-","-parSxl-","-partli-","-parti-","-palxsi-","-pul-","-prabxbli-","-skul-","-sil-","-sikrxt-","-sid-","-startxl-","-strxgxl-","-stxdid-","-stupxd-","-sxksid-","-triti-","-trup-","-truli-","-trxsti-"};
            String _slex[] = {"x","xbrxpt","xdapt","xdxlt","xgri","xlat","xpart","xpil","ark","ar","art","artxst","xslip","bar","bark","bi","bit","bist","blak","blxd","blu","bab","babi","badi","bust","but","batxl","baks","brid","brud","brxS","bxbxl","bxk","bxs","bxt","kar","kard","karpxt","sis","klak","klxb","klu","kalig","kul","kap","kapi","kxpxl","krip","kru","krap","kruSxl","kruxl","krxS","kxp","kxt","dark","dart","dil","did","dip","du","dal","dat","dxbxl","dru","drap","drxg","dxk","dxl","dxst","duti","ist","it","glu","gad","gat","grik","grit","gru","grup","gard","gxtar","kip","ki","lid","lig","lip","list","ligxl","labi","lak","lup","lus","lat","lxk","lxki","lxkSxri","ad","papx","park","part","parSxl","partli","parti","par","pi","pik","pipxl","pis","plat","plxg","plxs","pakxt","pxlis","palxsi","pul","pap","pasxbxl","pasxbli","pat","prist","prabxbxl","prabxbli","pradus","pradxkt","pragrxs","pxt","rid","ril","rili","rab","rak","rakxt","rad","rut","rxb","rxgxd","rul","rupi","rxS","rxsxl","skar","skul","skru","sil","sit","sikrxt","si","sid","sik","Sarp","Si","Sip","Sit","Sild","Sak","Sut","Sap","Sat","Srxg","Sxt","slip","slit","slxg","salxd","sari","spark","spik","spid","spat","star","start","startxl","stil","stip","stak","stap","strik","strit","strxk","strxgxl","stxdid","stxdi","stupxd","sxbstxtut","sxtxl","sxksid","sxk","su","sut","sutxbxl","tar","targxt","ti","tu","tul","tap","trit","triti","tri","trup","trat","trxbxl","trxk","tru","truli","trxst","trxsti","tub","xgli","xp","xs","-"};
            String _slexEnemies[] = {"xp","x","x","x","xgli","x","xp","xp","ar","ark","ar","art","xs","bark","bar","bit","bi","bi","blu","blu","brud","babi","bab","babi","but","bust","bab","bab","brud","brid","brud","bxt","bxt","bxt","bxk","kard","kar","kar","si","klu","klu","kru","kar","skul","kapi","kap","kxp","kru","skru","kru","kru","kru","kru","kxpxl","kxp","dart","dark","did","dip","did","duti","dark","dal","dxk","drap","dru","dru","dxl","dxk","dxk","du","bist","bit","gru","gat","gad","gru","gru","grup","gru","gad","gat","ki","kip","lig","ligxl","slip","ist","lig","lak","blak","lus","lup","lak","lxki","lxk","lxk","gad","pap","par","par","par","part","part","park","pik","pi","pi","pi","lat","plxs","plxg","pap","pxt","par","par","papx","pasxbli","pasxbxl","pap","pi","prabxbli","prabxbxl","pradxkt","pradus","pradus","pat","brid","rili","ril","rad","rakxt","rak","rab","rupi","rxS","rxb","rut","rut","rxb","rxS","skul","skar","skar","si","si","sik","sis","si","si","Sap","Sip","Si","Si","Si","Sap","Sat","Sat","Sap","rxgxd","Sat","slit","slip","slip","sari","si","star","spid","spik","spark","start","star","start","stip","stil","stap","stak","strit","strik","strxgxl","strxk","stxdi","stxdid","stap","sxk","sxk","sxk","sxksid","sut","su","sut","targxt","tar","tu","tul","tu","stap","triti","trit","trit","tru","tru","tru","tru","trup","tru","trxsti","trxst","tu","xgri","x","x","xbrxpt"};
            String[] _NFA={"artxst","kard","sis","klu","kalig","krip","krap","kruSxl","kruxl","kxt","glu","lxkSxri","parSxl","partli","parti","palxsi","pul","prabxbli","skul","sil","sikrxt","sid","startxl","strxgxl","stxdid","stupxd","sxksid","triti","trup","truli","trxsti"};
            String phonemeLabels[]={"p", "b", "t", "d", "k", "g", "s", "S", "r", "l", "a", "i", "u", "x", "-"};     
            int phoneCount[]={29,22,18,16,21,9,40,11,14,13,5,2,0,12,1};
            Node parent;
            Node[] children; 
            String label; //the phoneme living here
            double probability; //# lexical items subsumed by this node, including current, divided by total.
            boolean leaf; //no more lexical items  exist below this node.
            boolean word; //matches a lexical item
            
            double sumProb;
            double sumLexProb;
            int weight; //# lexical nodes subsumed
            int twoStepWeight; //# lexical nodes subsumed to limit 2 steps down.
            int cardinality; //# nodes subsumed
            double density; //weight/cardinality
            
            public Node(Node p,String l){
                parent=p;
                label=l;
                children=new Node[15];
                
                //create the string represented here.
                Node climb=this;
                String id=label;
                while(!climb.isRoot()){
                    climb=climb.parent();
                    id=id.concat(climb.label());
                }               
                id=reverse(id);
                //System.out.println(id);
                climb=null;
                //is this word in the lexicon?
                word=false;
                for(int i=0;i<slex.length;i++)
                    if(id.concat("-").equals(slex[i])) word=true;
                //is this item a leaf? (i.e. NOT a properly embedded portion of a word)
                leaf=true;
                for(int i=0;i<slex.length;i++)
                    if(slex[i].startsWith(id)&&!slex[i].equals(id)) leaf=false;
                if(leaf){
                    probability=(double)lexicalCardinality()/(double)phoneCount[rootIndex()];                    
                }
                else{ //if(!leaf){
                    for(int i=0;i<children.length;i++){
                        addChild(phonemeLabels[i],i);                    
                    }
                    probability=(double)lexicalCardinality()/(double)phoneCount[rootIndex()];                    
                }                
            }
            public String toString(){
                if(leaf&&!word) return null;
                //create the string represented here.
                Node climb=this;
                String id="";
                if(!word) id=id.concat("_"); //shows that this is an unfinished word beginning.
                id=id.concat(label);
                while(!climb.isRoot()){
                    climb=climb.parent();
                    id=id.concat(climb.label());
                }               
                //if(!word) id=id.concat("_"); //shows that this is an unfinished word beginning.
                id=reverse(id);
                climb=null;
                return id;
            }
            public String rawCharPath(){
                Node climb=this;
                String id="";
                id=id.concat(label);
                while(!climb.isRoot()){
                    climb=climb.parent();
                    id=id.concat(climb.label());
                }               
                id=reverse(id);
                climb=null;
                return id;            
            }
            public Node lookup(String str){
                if(this.rawCharPath()==null) return null;
                if(this.rawCharPath().equals(str)) return this;
                for(int i=0;i<children.length;i++)
                    if(null!=children[i])
                        if(null!=children[i].lookup(str))
                            return children[i].lookup(str);
                return null;
            }            
            public double sumProb(){return sumProb;}
            public double sumLexProb(){return sumLexProb;}            
            public int weight(){return weight;}
            public int twoStepWeight(){return twoStepWeight;}
            public int cardinality(){return cardinality;}
            public double density(){return density;}
            public void setWeight(int w){weight=w;}
            public void setCardinality(int c){cardinality=c;}
            public void setDensity(double d){density=d;}
            
            public double calculateSumProb(){
                double result=0;
                result+=probability;                    
                if(leaf){ 
                    sumProb=result;
                    return result;
                }
                else{
                    for(int i=0;i<children.length;i++)
                        result+=children[i].calculateSumProb();
                    sumProb=result;
                    return result;
                }
            }
            public double calculateLexSumProb(){
                double result=0;
                if(word) result+=probability;                    
                if(leaf){ 
                    sumLexProb=result;
                    return result;
                }
                else{
                    for(int i=0;i<children.length;i++)
                        result+=children[i].calculateLexSumProb();
                    sumLexProb=result;
                    return result;
                }
            }
            public int calculateNumNodes(){
                int result=0;
                if(leaf){ 
                    if(word) result++;
                    cardinality=result;
                    return result;
                }
                else{
                    result++;                
                    for(int i=0;i<children.length;i++)
                        result+=children[i].calculateNumNodes();
                    cardinality=result;
                    return result;
                }
            }
            public void calculateTwoStepWeight(){
                twoStepWeight=calculateTwoStepWeight(3);
                for(int i=0;i<children.length;i++){
                    if(null==children[i]) continue;
                    children[i].calculateTwoStepWeight();
                }
            }
            public int calculateTwoStepWeight(int step){                
                int result=0;
                if(word) result++;
                if(step==0){
                    return result;
                }
                else{
                    //not recursive! because object variable is not assigned here.
                    for(int i=0;i<children.length;i++){
                        if(null==children[i]) continue;
                        result+=children[i].calculateTwoStepWeight(step-1);                
                    }
                    return result;
                }                
            }
            public int calculateWeight(){
                int result=0;
                if(word) result++;
                if(leaf){ 
                    weight=result;
                    return result;
                }
                for(int i=0;i<children.length;i++)
                    result+=children[i].calculateWeight();
                weight=result;
                return result;
            }
            public void calculateDensity(){
                if(cardinality==0){ density=0; return;}
                else density=(double)((double)weight/(double)cardinality);
                if(leaf) return;
                for(int i=0;i<children.length;i++)
                    children[i].calculateDensity();
                
            }
                        
            public boolean isLeaf(){return leaf;}
            public boolean isWord(){return word;}
            public double prob(){return probability;}
            public void setProb(double p){probability=p;}
            public void addChild(String l,int idx){  children[idx]=new Node(this,l);}
            public Node parent(){return parent;}
            public String label(){return label;}
            public boolean isRoot(){return (parent==null);}
            public Node[] children(){return children;}
            public int rootIndex(){
                if(isRoot()){
                    for(int i=0;i<phonemeLabels.length;i++)
                        if(label.equals(phonemeLabels[i])) return i;
                }
                return parent().rootIndex();                
            }
            public String reverse(String str){
                String result="";
                for(int i=str.length()-1;i>=0;i--)
                    result=result.concat(str.substring(i,i+1));
                return result;
            }
            public void normalizeProbabilities(){
                //1. calculate the sum of lexical probabilities subsumed under this node (should be a root)
                //2. normalize such that all lexical items' probabilities sum to 1.0
                //3. non-lexical items should be normalized as well
                double sum=sumWordProbabilities();
                refactorProbabilities(sum);
            
            }
            public double sumWordProbabilities(){
                if(leaf) 
                    if(word) return probability;
                    else return 0d;
                else{
                    double result;
                    if(word) result=probability;
                    else result=0d;                        
                        for(int i=0;i<children.length;i++)
                            result+=children[i].sumWordProbabilities();
                    return result;
                }                                
            }
            public void refactorProbabilities(double factor){
                probability/=factor;
                if(leaf) return;
                for(int i=0;i<children.length;i++)
                    children[i].refactorProbabilities(factor);         
            }
            public void diagnostic(boolean wordsAndBranches){
                if(leaf){
                    if(word) System.out.println(toString()+"\t"+prob());                    
                }
                else{
                    if(word||wordsAndBranches) System.out.println(toString()+"\t"+prob());                                                            
                    for(int i=0;i<children.length;i++)
                        children[i].diagnostic(wordsAndBranches);
                }          
            }
            public int lexicalCardinality(){
                //how many words does this node parent, plus 1 for this nodes lexicality?
                //Q:how is this different from weight? 
                if(leaf){
                    if(word) return 1;                    
                    else return 0;               
                }
                else{
                    int result;
                    if(word) result=1;
                    else result=0;
                    for(int i=0;i<children.length;i++){
                        if(children[i]==null) continue;
                        result+=children[i].lexicalCardinality();
                    }
                    return result;
                }
            }
            public String[] tokenizeString(String str){
                java.util.StringTokenizer tk=new java.util.StringTokenizer(str,",");
                if(tk.countTokens()==0) return null;
                String[] result=new String[tk.countTokens()];
                int i=0;
                while(tk.hasMoreTokens())
                    result[i++]=tk.nextToken().trim();                                
                return result;
            }
            public double[] tokenizeDoubles(String str){
                java.util.StringTokenizer tk=new java.util.StringTokenizer(str,",");
                if(tk.countTokens()==0) return null;
                double[] result=new double[tk.countTokens()];
                int i=0;
                while(tk.hasMoreTokens())
                    result[i++]=(new Double(tk.nextToken().trim())).doubleValue();                                
                return result;
            }
            public int[] sortedOrderingOf(double[] list){
                int[] sorted=new int[list.length];
                int max=0;
                for(int i=0;i<list.length;i++){
                    max=-1;
                    for(int j=0;j<list.length;j++)
                        if(list[j]>=list[max]) max=j;
                    sorted[i]=max;
                    list[max]=-1;
                }
                return sorted;
            }
            public String[] subsumedWords(){
                return tokenizeString(subsumedWordsRecursive());
            }
            public String subsumedWordsRecursive(){
                if(leaf){
                    if(word) return toString()+",";
                    else return "";
                }
                else{
                    String result="";
                if(word) result+=toString()+",";
                    for(int i=0;i<children.length;i++)
                        result+=children[i].subsumedWordsRecursive();
                    return result;
                }                                
            }
            public double[] subsumedProbabilities(){
                return this.tokenizeDoubles(subsumedProbabilitiesRecursive());
            }
            public String subsumedProbabilitiesRecursive(){
                if(leaf){
                    if(word) return (new Double(probability)).toString()+",";
                    else return "";
                }
                else{
                    String result="";
                    if(word) result+=(new Double(probability)).toString()+",";
                    for(int i=0;i<children.length;i++)
                        result+=children[i].subsumedProbabilitiesRecursive();
                    return result;
                }                                
            }
            public LinkedList subsumedNodes(){
                LinkedList result=new LinkedList();
                result.add(this);
                if(leaf) return result;
                for(int i=0;i<children.length;i++){
                    if(null==children[i]) continue;
                    if(children[i].isLeaf()&&!children[i].isWord()) continue;
                    result.addAll((Collection)children[i].subsumedNodes());
                }
                return result;
            }

        }
        

    //String[] biglex={"Sa","Sadi","Sak","Sakr","Sap","Sapr","Sard","Sari","Sark","Sarlxt","Sarp","Sartrx","Sat","Si","Sik","Sild","Silx","Sip","Sit","Srik","Srk","Srlak","Srli","Srt","Srud","Srxb","Srxbxri","Srxg","Su","Subrt","Sut","Sutr","Sxdr","Sxt","Sxtl","Sxtr","a","aSikaga","absxlit","ad","adlat","aidx","akita","aks","akskart","algx","alla","aprx","aprxbl","apt","ar","arbxtr","ardr","argas","ark","arkxs","arp","art","arti","artlxs","artri","as","asixs","askr","asr","asxlat","atr","ba","baS","bab","babi","babl","badi","badigard","badxli","baix","baks","bakskar","baksr","baku","bali","bap","bar","barb","barbd","barbr","barbrx","barbxrxs","bard","bari","bark","barli","bart","bartak","batista","batl","bi","bibap","bibi","bid","bidi","bidl","bikr","bip","bist","bit","bitl","bitxp","blab","blak","blaki","blat","blid","blik","blikli","blit","blr","blrt","blu","blukalr","blxS","blxbr","blxd","blxdSat","blxdi","blxdrut","blxstr","br","brak","brakxli","brd","brdi","brg","brglr","brglri","brid","brk","brkli","brl","brli","brst","brt","brti","bru","brud","brudi","brus","brut","brutl","brutli","bruxri","brxS","brxskli","bu","bubi","bubr","bui","bust","bustr","but","buti","butl","bxbl","bxbli","bxd","bxdi","bxg","bxgi","bxk","bxkSat","bxkl","bxkli","bxks","bxlb","bxlk","bxlki","bxrix","bxs","bxsl","bxst","bxstr","bxt","bxtlr","bxtr","bxtxri","dad","dag","dak","daktr","daktrxt","dal","dali","dalr","daplr","dark","darkli","dart","dasl","daslli","dat","di","did","dikris","dil","dilr","dip","dipli","dipsi","dixti","drap","dras","drbi","drt","drti","dru","drup","drxg","du","dubixs","dui","duk","dul","duli","dup","duplxkxbl","dus","duti","dxb","dxbl","dxbli","dxbri","dxd","dxdli","dxg","dxga","dxglxs","dxgri","dxk","dxkri","dxkt","dxl","dxli","dxlud","dxlxks","dxlxs","dxpart","dxsk","dxski","dxst","dxsti","gaS","gab","gabl","gablr","gad","gag","gagl","galata","gali","garb","garbl","gard","gargl","gartr","gaspl","gasxp","glasi","glastr","glatl","gli","glu","glxt","gragi","grd","grdl","grdr","grgl","grid","gridi","grik","gris","grisi","grit","grl","grli","grtrud","grup","grxb","grxbi","gui","gul","guld","guru","gus","gxS","gxSr","gxl","gxli","gxlp","gxlx","gxlxbl","gxs","gxst","gxsti","gxt","gxtr","gxtrl","igl","igr","igrli","igrxt","ik","il","ist","istr","it","itxbl","kab","kablr","kad","kadl","kag","kak","kaki","kaks","kali","kalig","kalr","kalrx","kalxki","kap","kapi","kapli","kapr","kapxri","kar","kard","karlx","karp","karpxt","kart","kartr","kasi","katr","ki","kiask","kil","kilr","kip","kipr","klabr","klad","klag","klak","klark","klat","klik","klit","klrk","klu","klxb","klxk","klxstr","klxtr","kr","krakt","krap","krb","krbi","krd","krdl","kri","krid","krik","krip","kripi","kripr","krir","kris","krk","krl","krli","krs","krsri","krt","krtixs","krtli","krtsi","kru","kruSl","kruSlli","krud","krudli","krugr","krul","krulli","krulti","krupir","krusxbl","krxS","krxks","krxpr","krxpt","krxst","ku","kul","kuli","kulr","kup","kupr","kxb","kxbrd","kxd","kxlasl","kxlr","kxlrd","kxlt","kxp","kxpl","kxplr","kxrakxs","kxrarx","kxrxptxbl","kxsp","kxstr","kxt","kxtlxs","kxtlxt","kxtr","lab","labi","lablali","labstr","lagr","lak","lakr","lakxp","lal","lap","lard","lardr","lark","larkspr","lat","lati","latri","li","liS","lidr","lig","ligl","ligr","lik","lil","lilx","lip","lis","list","lisx","lit","litr","lrk","lu","luSixs","luSx","lud","ludi","ludli","lugr","lui","luk","lukriSxs","lukxs","luli","lulu","lup","lus","lusi","lusil","lut","lutuli","lxS","lxbxk","lxdu","lxg","lxk","lxkSri","lxki","lxkxli","lxl","lxst","lxsti","lxstr","lxstrxs","pa","pad","palxk","palxti","pap","papi","paplr","par","parSl","parSli","park","parli","parlr","parsl","parsli","part","parti","partli","pasi","pat","patr","patri","pi","pik","pikak","pikt","pil","pip","pipl","pis","pisxbl","pitr","pitri","pitsx","plad","plap","plat","pli","plid","plidr","plit","plxS","plxg","plxk","plxs","pr","prabxbl","prabxbli","prad","pradxkt","prag","praksi","praktr","prap","prapr","praprli","praprti","praspr","prasprxs","prasr","priistr","prisid","priskul","prist","pristli","prk","prki","prl","prli","prpl","prpxs","prs","prsi","prsu","prsut","prt","prtrb","prust","prxdusr","pudl","pul","pxb","pxdl","pxkr","pxlis","pxlp","pxls","pxlut","pxp","pxpi","pxpxt","pxru","pxt","pxti","pxtit","pxtr","rab","rabat","rabri","rad","rak","raki","rakr","rastr","rat","ri","rid","ridbxk","ridi","ridxbl","rigl","rik","ril","rili","rilti","riltr","rip","ristak","rl","rli","rp","ru","rubarb","rubi","rud","rudi","rul","rulr","rupi","rus","rust","rut","rxS","rxSx","rxb","rxbl","rxbr","rxbrtx","rxbxri","rxdi","rxdr","rxg","rxkxs","rxs","rxsk","rxsl","rxslr","rxst","rxsti","rxstxs","rxt","sab","sad","sadi","sadr","sagi","sagx","sak","sakr","sal","sali","salxdli","salxs","sap","sari","sartrx","si","sid","sik","sikr","sil","sip","sipix","sis","sit","skalr","skalxp","skar","skarlxt","skat","skati","ski","skri","skrlxs","skrt","skru","skrxb","skul","skulgrl","skup","skxd","skxl","skxlk","skxlptr","skxtl","slap","slapi","slat","slik","slip","slipi","slipr","slit","slup","slus","slxg","spa","spar","spark","sparkl","spars","spat","spiSi","spid","spidi","spik","spikr","spr","spri","sprt","spru","sprus","spuki","spxtr","sr","srkl","srkx","srli","srplxs","srsis","srtu","srxp","stak","staki","stalxd","stalxdli","stap","stapr","star","starbrd","stark","start","startl","startr","stid","stil","stilr","stip","stipl","str","strap","strdi","strik","strit","stritkar","stru","strxgl","strxp","strxt","stu","studixs","studixsli","stul","stup","stupr","stupxdli","sturd","stxb","stxbi","stxbl","stxd","stxdi","su","sup","supr","suprsid","sur","surd","sut","sutr","sutxbl","suxbl","sxb","sxbdu","sxbgrup","sxbrb","sxbrbix","sxk","sxkr","sxksid","sxli","sxlk","sxlki","sxltri","sxlubrixs","sxlut","sxp","sxpr","sxtl","sxtli","sxtlti","tad","tag","talrxbl","tap","tapl","taps","tar","tardi","tart","tartr","tartxri","taskx","tat","ti","tidixs","tik","tikart","tipi","titr","trali","tralxp","trat","tratr","tri","trit","triti","trk","trki","trs","trsli","trtl","tru","truli","trup","trupr","trus","trxbl","trxk","trxkr","trxs","trxst","trxsti","tu","tub","tubr","tubx","tudr","tukxlr","tul","tuli","tulxp","tut","tutr","txb","txdu","txg","txk","txkatx","txkr","txpikx","txrisx","txsk","x","xSr","xblikli","xbrxpt","xbrxptli","xbsrd","xbstrxkt","xbtrud","xdapt","xdu","xdus","xdxlt","xdxltri","xdxltrxs","xgri","xgrixbl","xklak","xklud","xkr","xkru","xlat","xlit","xlrt","xlrtli","xlsr","xlud","xp","xpSat","xpart","xpasl","xpbit","xpil","xpis","xpkip","xpr","xpriSxbl","xprkxt","xprut","xslip","xsrt","xstut","xtap","xtr","-"};
        /*
         *int[] phoneCount=new int[15];
                for(int i=0;i<phonemeLabels.length;i++){
                    for(int j=0;j<slex.length;j++)
                        if(slex[j].startsWith(phonemeLabels[i]))
                            phoneCount[i]++;
                    System.out.println(phonemeLabels[i]+"\t"+phoneCount[i]);
                }*/
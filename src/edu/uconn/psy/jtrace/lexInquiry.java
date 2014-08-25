
package edu.uconn.psy.jtrace;
import java.sql.*;

public class lexInquiry {

	public static void main ( String args[] ) throws Exception {
            String[] slex={"-","x","xbrxpt","xdapt","xdxlt","xgri","xlat","xpart","xpil","ark","ar","art","artxst","xslip","bar","bark","bi","bit","bist","blak","blxd","blu","bab","babi","badi","bust","but","batxl","baks","brid","brud","brxS","bxbxl","bxk","bxs","bxt","kar","kard","karpxt","sis","klak","klxb","klu","kalig","kul","kap","kapi","kxpxl","krip","kru","krap","kruSxl","kruxl","krxS","kxp","kxt","dark","dart","dil","did","dip","du","dal","dat","dxbxl","dru","drap","drxg","dxk","dxl","dxst","duti","ist","it","glu","gad","gat","grik","grit","gru","grup","gard","gxtar","kip","ki","lid","lig","lip","list","ligxl","labi","lak","lup","lus","lat","lxk","lxki","lxkSxri","ad","papx","park","part","parSxl","partli","parti","par","pi","pik","pipxl","pis","plat","plxg","plxs","pakxt","pxlis","palxsi","pul","pap","pasxbxl","pasxbli","pat","prist","prabxbxl","prabxbli","pradus","pradxkt","pragrxs","pxt","rid","ril","rili","rab","rak","rakxt","rad","rut","rxb","rxgxd","rul","rupi","rxS","rxsxl","skar","skul","skru","sil","sit","sikrxt","si","sid","sik","Sarp","Si","Sip","Sit","Sild","Sak","Sut","Sap","Sat","Srxg","Sxt","slip","slit","slxg","salxd","sari","spark","spik","spid","spat","star","start","startxl","stil","stip","stak","stap","strik","strit","strxk","strxgxl","stxdid","stxdi","stupxd","sxbstxtut","sxtxl","sxksid","sxk","su","sut","sutxbxl","tar","targxt","ti","tu","tul","tap","trit","triti","tri","trup","trat","trxbxl","trxk","tru","truli","trxst","trxsti","tub","xgli","xp","xs"};
            int[] slexEnemies={210,0,0,0,209,0,210,210,9,8,9,10,211,14,13,16,15,15,20,20,29,22,21,22,25,24,21,21,29,28,29,34,34,34,32,36,35,35,147,41,41,48,35,142,45,44,53,48,143,48,48,48,48,46,53,56,55,58,59,58,70,55,61,67,65,64,64,68,67,67,60,17,16,78,75,74,78,78,79,78,74,75,83,82,85,88,161,71,85,90,18,92,91,90,95,94,94,74,116,104,104,104,100,100,99,106,105,105,105,93,111,110,116,126,104,104,98,118,212,116,105,212,212,124,123,123,119,28,129,128,133,132,131,130,138,139,135,134,134,135,139,142,141,141,147,147,149,38,147,147,157,152,151,151,151,157,158,158,157,136,158,162,161,161,165,147,170,168,167,166,171,212,170,174,173,176,175,178,177,180,212,212,212,176,187,187,187,186,189,188,189,192,191,194,195,194,176,198,197,197,212,204,204,204,200,204,212,212,194,4,0,0,2};
            //int[] slexEnemies={0,212,210,0,0,209,0,210,210,9,8,9,10,211,14,13,16,15,15,20,20,29,22,21,21,25,24,21,21,29,28,29,34,34,34,32,36,35,35,147,41,41,48,35,35,45,44,53,48,51,48,48,48,48,46,53,56,55,58,59,58,70,55,61,67,65,64,64,68,67,67,60,72,71,78,75,74,78,78,79,78,74,75,83,82,85,88,84,84,85,90,93,92,91,90,95,94,94,9,116,104,104,104,100,100,99,106,105,105,105,110,111,110,116,104,104,104,98,118,117,116,105,122,121,124,123,123,119,128,129,128,133,132,131,130,138,139,135,134,134,135,139,142,141,141,147,147,149,38,147,147,157,152,151,151,151,157,158,158,157,151,158,162,161,161,165,147,169,168,167,166,171,170,170,174,173,176,175,178,177,180,179,182,181,176,187,187,187,186,189,188,189,192,191,194,195,194,191,199,197,204,204,204,204,204,200,204,207,206,194,4,0,0};
            String[] biglex={"Sa","Sadi","Sak","Sakr","Sap","Sapr","Sard","Sari","Sark","Sarlxt","Sarp","Sartrx","Sat","Si","Sik","Sild","Silx","Sip","Sit","Srik","Srk","Srlak","Srli","Srt","Srud","Srxb","Srxbxri","Srxg","Su","Subrt","Sut","Sutr","Sxdr","Sxt","Sxtl","Sxtr","a","aSikaga","absxlit","ad","adlat","aidx","akita","aks","akskart","algx","alla","aprx","aprxbl","apt","ar","arbxtr","ardr","argas","ark","arkxs","arp","art","arti","artlxs","artri","as","asixs","askr","asr","asxlat","atr","ba","baS","bab","babi","babl","badi","badigard","badxli","baix","baks","bakskar","baksr","baku","bali","bap","bar","barb","barbd","barbr","barbrx","barbxrxs","bard","bari","bark","barli","bart","bartak","batista","batl","bi","bibap","bibi","bid","bidi","bidl","bikr","bip","bist","bit","bitl","bitxp","blab","blak","blaki","blat","blid","blik","blikli","blit","blr","blrt","blu","blukalr","blxS","blxbr","blxd","blxdSat","blxdi","blxdrut","blxstr","br","brak","brakxli","brd","brdi","brg","brglr","brglri","brid","brk","brkli","brl","brli","brst","brt","brti","bru","brud","brudi","brus","brut","brutl","brutli","bruxri","brxS","brxskli","bu","bubi","bubr","bui","bust","bustr","but","buti","butl","bxbl","bxbli","bxd","bxdi","bxg","bxgi","bxk","bxkSat","bxkl","bxkli","bxks","bxlb","bxlk","bxlki","bxrix","bxs","bxsl","bxst","bxstr","bxt","bxtlr","bxtr","bxtxri","dad","dag","dak","daktr","daktrxt","dal","dali","dalr","daplr","dark","darkli","dart","dasl","daslli","dat","di","did","dikris","dil","dilr","dip","dipli","dipsi","dixti","drap","dras","drbi","drt","drti","dru","drup","drxg","du","dubixs","dui","duk","dul","duli","dup","duplxkxbl","dus","duti","dxb","dxbl","dxbli","dxbri","dxd","dxdli","dxg","dxga","dxglxs","dxgri","dxk","dxkri","dxkt","dxl","dxli","dxlud","dxlxks","dxlxs","dxpart","dxsk","dxski","dxst","dxsti","gaS","gab","gabl","gablr","gad","gag","gagl","galata","gali","garb","garbl","gard","gargl","gartr","gaspl","gasxp","glasi","glastr","glatl","gli","glu","glxt","gragi","grd","grdl","grdr","grgl","grid","gridi","grik","gris","grisi","grit","grl","grli","grtrud","grup","grxb","grxbi","gui","gul","guld","guru","gus","gxS","gxSr","gxl","gxli","gxlp","gxlx","gxlxbl","gxs","gxst","gxsti","gxt","gxtr","gxtrl","igl","igr","igrli","igrxt","ik","il","ist","istr","it","itxbl","kab","kablr","kad","kadl","kag","kak","kaki","kaks","kali","kalig","kalr","kalrx","kalxki","kap","kapi","kapli","kapr","kapxri","kar","kard","karlx","karp","karpxt","kart","kartr","kasi","katr","ki","kiask","kil","kilr","kip","kipr","klabr","klad","klag","klak","klark","klat","klik","klit","klrk","klu","klxb","klxk","klxstr","klxtr","kr","krakt","krap","krb","krbi","krd","krdl","kri","krid","krik","krip","kripi","kripr","krir","kris","krk","krl","krli","krs","krsri","krt","krtixs","krtli","krtsi","kru","kruSl","kruSlli","krud","krudli","krugr","krul","krulli","krulti","krupir","krusxbl","krxS","krxks","krxpr","krxpt","krxst","ku","kul","kuli","kulr","kup","kupr","kxb","kxbrd","kxd","kxlasl","kxlr","kxlrd","kxlt","kxp","kxpl","kxplr","kxrakxs","kxrarx","kxrxptxbl","kxsp","kxstr","kxt","kxtlxs","kxtlxt","kxtr","lab","labi","lablali","labstr","lagr","lak","lakr","lakxp","lal","lap","lard","lardr","lark","larkspr","lat","lati","latri","li","liS","lidr","lig","ligl","ligr","lik","lil","lilx","lip","lis","list","lisx","lit","litr","lrk","lu","luSixs","luSx","lud","ludi","ludli","lugr","lui","luk","lukriSxs","lukxs","luli","lulu","lup","lus","lusi","lusil","lut","lutuli","lxS","lxbxk","lxdu","lxg","lxk","lxkSri","lxki","lxkxli","lxl","lxst","lxsti","lxstr","lxstrxs","pa","pad","palxk","palxti","pap","papi","paplr","par","parSl","parSli","park","parli","parlr","parsl","parsli","part","parti","partli","pasi","pat","patr","patri","pi","pik","pikak","pikt","pil","pip","pipl","pis","pisxbl","pitr","pitri","pitsx","plad","plap","plat","pli","plid","plidr","plit","plxS","plxg","plxk","plxs","pr","prabxbl","prabxbli","prad","pradxkt","prag","praksi","praktr","prap","prapr","praprli","praprti","praspr","prasprxs","prasr","priistr","prisid","priskul","prist","pristli","prk","prki","prl","prli","prpl","prpxs","prs","prsi","prsu","prsut","prt","prtrb","prust","prxdusr","pudl","pul","pxb","pxdl","pxkr","pxlis","pxlp","pxls","pxlut","pxp","pxpi","pxpxt","pxru","pxt","pxti","pxtit","pxtr","rab","rabat","rabri","rad","rak","raki","rakr","rastr","rat","ri","rid","ridbxk","ridi","ridxbl","rigl","rik","ril","rili","rilti","riltr","rip","ristak","rl","rli","rp","ru","rubarb","rubi","rud","rudi","rul","rulr","rupi","rus","rust","rut","rxS","rxSx","rxb","rxbl","rxbr","rxbrtx","rxbxri","rxdi","rxdr","rxg","rxkxs","rxs","rxsk","rxsl","rxslr","rxst","rxsti","rxstxs","rxt","sab","sad","sadi","sadr","sagi","sagx","sak","sakr","sal","sali","salxdli","salxs","sap","sari","sartrx","si","sid","sik","sikr","sil","sip","sipix","sis","sit","skalr","skalxp","skar","skarlxt","skat","skati","ski","skri","skrlxs","skrt","skru","skrxb","skul","skulgrl","skup","skxd","skxl","skxlk","skxlptr","skxtl","slap","slapi","slat","slik","slip","slipi","slipr","slit","slup","slus","slxg","spa","spar","spark","sparkl","spars","spat","spiSi","spid","spidi","spik","spikr","spr","spri","sprt","spru","sprus","spuki","spxtr","sr","srkl","srkx","srli","srplxs","srsis","srtu","srxp","stak","staki","stalxd","stalxdli","stap","stapr","star","starbrd","stark","start","startl","startr","stid","stil","stilr","stip","stipl","str","strap","strdi","strik","strit","stritkar","stru","strxgl","strxp","strxt","stu","studixs","studixsli","stul","stup","stupr","stupxdli","sturd","stxb","stxbi","stxbl","stxd","stxdi","su","sup","supr","suprsid","sur","surd","sut","sutr","sutxbl","suxbl","sxb","sxbdu","sxbgrup","sxbrb","sxbrbix","sxk","sxkr","sxksid","sxli","sxlk","sxlki","sxltri","sxlubrixs","sxlut","sxp","sxpr","sxtl","sxtli","sxtlti","tad","tag","talrxbl","tap","tapl","taps","tar","tardi","tart","tartr","tartxri","taskx","tat","ti","tidixs","tik","tikart","tipi","titr","trali","tralxp","trat","tratr","tri","trit","triti","trk","trki","trs","trsli","trtl","tru","truli","trup","trupr","trus","trxbl","trxk","trxkr","trxs","trxst","trxsti","tu","tub","tubr","tubx","tudr","tukxlr","tul","tuli","tulxp","tut","tutr","txb","txdu","txg","txk","txkatx","txkr","txpikx","txrisx","txsk","x","xSr","xblikli","xbrxpt","xbrxptli","xbsrd","xbstrxkt","xbtrud","xdapt","xdu","xdus","xdxlt","xdxltri","xdxltrxs","xgri","xgrixbl","xklak","xklud","xkr","xkru","xlat","xlit","xlrt","xlrtli","xlsr","xlud","xp","xpSat","xpart","xpasl","xpbit","xpil","xpis","xpkip","xpr","xpriSxbl","xprkxt","xprut","xslip","xsrt","xstut","xtap","xtr","-"};
            String phonemeLabels[]={"p", "b", "t", "d", "k", "g", "s", "S", "r", "l", "a", "i", "u", "^", "-"};     
    
            int onsetHits[][][]=new int[slex.length][3][slex.length];
            int enemyScore[][]=new int[slex.length][3];
            
            //onsets?
            int oneCoh=0, twoCoh=0;
            for(int i=0;i<slex.length;i++){
                oneCoh=0; twoCoh=0;
                for(int j=0;j<slex.length;j++){
                    if(j==i) continue;
                    for(int k=0;slex[j].length()>=(k+1)&&k<2;k++){
                        if(slex[i].startsWith(slex[j].substring(0,k+1))){
                            if(k==0){ 
                                onsetHits[i][0][0]++; //number of primary cohorts for that word    
                                onsetHits[i][0][2]++; //number of pri+sec cohorts for that word   
                                onsetHits[i][1][oneCoh++]=j; //identity of primary cohorts                            
                            }
                            if(k==1){ 
                                onsetHits[i][0][1]++; //number of secondary cohorts for that word       
                                onsetHits[i][0][2]++; //number of pr+sec cohorts for that word       
                                onsetHits[i][2][twoCoh++]=j; //identity of secondary cohorts                           
                            }
                        }
                    }
                }
            }
            for(int i=0;i<slex.length;i++){
                enemyScore[i][0]+=onsetHits[slexEnemies[i]][0][0];
                enemyScore[i][1]+=onsetHits[slexEnemies[i]][0][1];
                enemyScore[i][2]+=onsetHits[slexEnemies[i]][0][2];
                
                /* FOR THE ENEMY-STAR CALCULATION
                 for(int j=0;j<onsetHits[i][1].length&&onsetHits[i][1][j]!=0;j++){
                    //enemyScore[i][0]+=onsetHits[onsetHits[i][1][j]][0][0];
                    //enemyScore[i][1]+=onsetHits[onsetHits[i][1][j]][0][1];
                    //enemyScore[i][2]+=onsetHits[onsetHits[i][1][j]][0][2];
                }*/
            }
            
            System.out.println("results: 1+2~ enemy score, 1~ enemy score, 2~ enemy score, 1+2~ cohort, 1~ cohort, 2~ cohort.");
            for(int i=0;i<slex.length;i++){
                System.out.println(slex[i]+"\t"+enemyScore[i][2]+"\t"+enemyScore[i][0]+"\t"+enemyScore[i][1]+"\t"+onsetHits[i][0][2]+"\t"+onsetHits[i][0][0]+"\t"+onsetHits[i][0][1]);
            }
            
            
        }
}
    
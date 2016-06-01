import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Set;
import java.util.Scanner;

public class getTaxID2 {
        public static HashMap<String, String>  getAccNum(File filename){

                HashMap<String, String> re = new HashMap<String, String>();
                BufferedReader br = null;
                try {
                        br = new BufferedReader(new FileReader(filename), 5*1024*1024);

                        String line = new String();
                        //int i = 0;
                                                    
                        while((line = br.readLine()) != null) {
                                String[] colsArr = line.split("\\t");
                                //i ++;
                                if (colsArr.length==3) {
                                        re.put(colsArr[2],colsArr[0]);
                                }
                        }

                        br.close();
                } catch (IOException e) {
                        e.printStackTrace();
                } finally {
                        if(br != null) {
                                try {
                                        br.close();
                                } catch (IOException e1) {
                                        e1.printStackTrace();
                                }
                        }
                }
                return re;
        }

        public static void main(String[] args) throws IOException{
                File in = new File("/home/qiaozy/B3_ESS/Conserved_pro/test_blastoutput3_GeneAccession.txt");
                HashMap<String, String> IDContig = getAccNum(in);
                
                //Set<Entry<String, String>> idTaxSet = IDContig.entrySet();
                // Find the taxonomy file contain GenBank Accession Number and tax ID.
                
                File ANToTax = new File("/home/qiaozy/giPro2tax/prot_accession2tax.txt");
                FileInputStream inputStream = null;
                //BufferedReader ANTax = new BufferedReader(new FileReader(ANToTax), 20*1024*1024);
                //String tempString = new String();
                Scanner sc = null;
                
                try {
                        inputStream = new FileInputStream(ANToTax);
                        sc = new Scanner(inputStream, "UTF-8");
                        while (sc.hasNextLine()) {
                                String tempString = sc.nextLine();
                                String[] a = tempString.split("\\t");
                                String s=a[0];
                                String st=a[1];
                        //for(Entry<String,String> i: idTaxSet){
                                if (IDContig.containsKey(s)){
                                        System.out.println(IDContig.get(s)+"\t"+s+"\t"+st);
                                }
                        }
                        if (sc.ioException() != null) {
                                throw sc.ioException();
                        }
                } finally {
                        if (inputStream != null) {
                                inputStream.close();
                        }
                        if (sc != null) {
                                sc.close();
                        }
                }
                //ANTax.close();
        }
}

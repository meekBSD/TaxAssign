
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Map.Entry;

public class SplitLineandHashGenus {
        public static ArrayList<String> readFileLines(File filename) throws IOException {
                FileInputStream fis = new FileInputStream(filename);
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String line = "";
                ArrayList<String> pre_classifi = new ArrayList<String>();
                HashMap<String, String> p = new HashMap<String, String>();


                ArrayList<String> classification = new ArrayList<String>();

                while ((line = br.readLine()) != null ) {
                        String[] colsa = line.split("\\t\\|\\t");
                        //find the class and assign it and its parent tax to pre_classifi List
                        if  (colsa[2].equals("class")) {
                                pre_classifi.add(colsa[1]+";"+colsa[0]);
                        }
                        else if (colsa[2].equals("subphylum") || colsa[2].equals("phylum")) {
                                p.put(colsa[1],colsa[0]);
                        }
                }

                br.close();
                isr.close();
                fis.close();

        // print each element in the ArrayList pre_classifi
                /**for(String pc : pre_classifi) {
                        System.out.println(pc);

                } */

                Set<Entry<String,String>> pset = p.entrySet();
                for(Entry<String, String> e: pset) {
                        for(String s: pre_classifi) {
                                String spe = s.split(";")[1];
                                //System.out.println(spe);
                                if (e.getValue().equals(s.split(";")[0]) ) {
                                        classification.add(e.getKey()+";"+e.getValue()+";"+ spe);
                                }
                        }
                }

        // print each key and each Value of kingdom and phylum HashMap
                /**Iterator<String> it_hashclass = p.keySet().iterator();
                while (it_hashclass.hasNext()) {
                        String k = it_hashclass.next();
                        System.out.print("kingdom/superphylum-"+k+" ; ");
                        System.out.println("phylum-"+p.get(k));
                } */

                return classification;
        }

        public static void main(String[] args) throws IOException {
                File a = new File(args[0]);
                FileOutputStream out = new FileOutputStream("result_p.txt");

                String Header = "kingdom\\superphylum;phylum;class\n";
                byte[] header_b = Header.getBytes();
                out.write(header_b);
                ArrayList<String> x = readFileLines(a);
                int i = 0;
                for(String s: x) {
                        i ++;
                        //System.out.println(s);
                        byte[] res_s = (s+"\n").getBytes();
                        out.write(res_s);
                        if (i == 5) {
                                break;
                        }
                }
        }
}




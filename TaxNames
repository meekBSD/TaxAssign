/**
    *  sed '1d' result_p.txt | awk -F ';' '{for(i=1;i<=NF;i++){printf("%s\n",$i)}}' | sort | uniq > Taxnames_test.txt
    *  The Taxnames_test.txt stored ID of some tax. It will be read as input for the following java codes. 
    *  Another input file is the taxdmp/names.dmp file.
    */
    
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;

public class SplitAndGettaxName {
        public static ArrayList<String> getTaxIds(String filepath) throws IOException {
                File readfile = new File(filepath);
                FileReader fr = new FileReader(readfile);
                ArrayList<String> TaxIds = new ArrayList<String>();

                BufferedReader br = new BufferedReader(fr);

                String myreadLine;
                //int i = 0;
                while(br.ready()) {
                        myreadLine = br.readLine();
                        if (!TaxIds.contains(myreadLine)) {
                                TaxIds.add(myreadLine);
                                //i ++;
                        }
                }
                br.close();
                fr.close();
                // System.out.println(i);
                return TaxIds;
        }

        public static Map<String, Object> readFileTest(String filename) throws IOException {
                FileInputStream fis = new FileInputStream(filename);
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");

                BufferedReader br = new BufferedReader(isr);
                
                String line = "";
                HashMap<String, Object> id_name = new HashMap<String, Object>();

                String sci_name = "scientific name";

                Pattern r = Pattern.compile(sci_name);

                while ( (line = br.readLine()) != null ) {
                        Matcher m = r.matcher(line);
                        if (m.find()) {
                                String[] columns = line.split("\\t\\|\\t");
                                //System.out.println(line);
                                id_name.put(columns[0], columns[1]);
                        }
                }

                br.close();
                isr.close();
                fis.close();

                return id_name;
        }
        public static void main(String[] args) throws IOException {
                Map<String, Object> idToName = readFileTest("/Users/Somebody/Downloads/taxdmp/names.dmp");
                ArrayList<String> re = getTaxIds("Taxnames_test.txt");

                int i = 0;
                Set<Entry<String, Object>> idNameSet = idToName.entrySet();
                for(Entry<String, Object> e: idNameSet) {
                        String k = e.getKey();
                        if (re.contains(k)) {
                                i ++;
                                System.out.println(i + "\t" + k + "\t" + e.getValue().toString());
                        }
                }
        }
}

/**
 *  Use Awk
 *  awk -F"\t" 'NR==FNR \
 *             {val[$1]=$3;next}; \
 *             {if($3 in val)print $0"\t"val[$3];else print $0"\tNone"}' taxonomy_name.txt result1.txt > tax_result1.txt
 * 
 *  awk -F"\t" 'NR==FNR \
 *             {val[$2]=$3;next}; \
 *             {if($3 in val)print $0"\t"val[$3];else print $0"\t-"}' phylum_tax.txt tax_result1.txt > tax_result2.txt
 * 
 * */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TestTaxonomyOutput {
	public static String concat2(String s1, String s2){
		return s1+"\t"+s2;
	}
	public static void  getAccNum(String filename, String anofilename, String resultfile){
		HashSet<String> taxId = new HashSet<String>();		

		BufferedReader br = null;
		try {
                        br = new BufferedReader(new FileReader(filename), 5*1024*1024);

                        String line = new String();
                        while((line = br.readLine()) != null) {
                                String[] colsArr = line.split("\\t");
                                String v = colsArr[2];
				if (!taxId.contains(v)) {
                                	taxId.add(v);
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

		HashMap<String, String> re = new HashMap<String, String>();
		BufferedReader br1 = null;
		try {
			br1 = new BufferedReader(new FileReader(anofilename), 5*1024*1024);

			String line = new String();
			while((line = br1.readLine()) != null) {
				String[] colsArr1 = line.split("\\t");
				if (taxId.contains(colsArr1[0])) {
					re.put(colsArr1[0], colsArr1[2]);
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br1 != null) {
				try {
					br1.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		BufferedReader br2 = null;
                try {
                        br2 = new BufferedReader(new FileReader(filename), 5*1024*1024);
			FileWriter resFile = new FileWriter(resultfile);

                        String line = new String();
                        while((line = br2.readLine()) != null) {
                                String[] colsArr2 = line.split("\\t");
                                String v1 = concat2(colsArr2[0] , colsArr2[1]);
                                
                                if (re.containsKey(colsArr2[2])) {
                                        resFile.write(v1 + "\t" + colsArr2[2] + "\t" + re.get(colsArr2[2])+"\n");
                                        // resFile.write(line + "\t" + re.get(colsArr2[2]) + "\n");
                                } else {
					resFile.write(v1 + "\t" + colsArr2[2] + "\t" + "None"+"\n");
					// resFile.write(line + "\t-\n");
				}
                        }

			resFile.flush();
			resFile.close();
                } catch (IOException e) {
                        e.printStackTrace();
                } finally {
                        if(br2 != null) {
                                try {
                                        br2.close();
                                } catch (IOException e1) {
                                        e1.printStackTrace();
                                }
                        }
                }
	}

	public static void main(String[] args) throws IOException{
		getAccNum("testInputFile1","taxonomy_name.txt","test_outputfile.txt");

	}
}

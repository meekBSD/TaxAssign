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
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

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
                                //String v1 = concat2(colsArr[0] , colsArr[1]);
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
                                } else {
					resFile.write(v1 + "\t" + colsArr2[2] + "\t" + "None"+"\n");
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
		getAccNum("/home/qiaozy/B3_ESS/Conserved_pro/archaea/result1.txt","/home/qiaozy/giPro2tax/taxonomy_name.txt","result_testjava.txt");

	}
}

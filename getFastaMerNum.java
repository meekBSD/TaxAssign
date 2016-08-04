import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getFastaMerNum {
        public static ArrayList<String> getLines(String filename) throws IOException {
                File id_uniq = new File(filename);
                ArrayList<String> re = new ArrayList<String>();
                try {
                        Scanner sc = new Scanner(id_uniq);
                        while (sc.hasNextLine()) {
                                String id = sc.nextLine();

                                re.add(id);
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }

		return re;
        }
	public static String getUniprot(String each_id) {
		String content = "";
		String MerNum = "";
		String res = "";
		HttpURLConnection conn = null;
		
		String s;
		String s2 = "";
		try {
			String page = "http://merops.sanger.ac.uk/cgi-bin/pepsum?id=" + each_id;
			URL link = new URL(page);
			conn = (HttpURLConnection)link.openConnection();
			conn.connect();
			conn.setConnectTimeout(20000); 
			conn.setReadTimeout(20000);
			
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);

			String curLine = "";
			while ( (curLine = br.readLine()) != null ) {
				// content += curLine;
				if (curLine.indexOf("/cgi-bin/aaseq?mernum=") != -1) {
					int mernum_start = curLine.indexOf("mernum=") + 7;
					int mernum_end = curLine.indexOf("\'", mernum_start);
					s = curLine.substring(mernum_start, mernum_end);
					MerNum = s.replaceAll("\\s*", "");
					//System.out.println(MerNum);
				}
			}
			br.close();
			System.out.println(MerNum);
			
			String page2 = "http://merops.sanger.ac.uk/cgi-bin/aaseq?mernum=" + MerNum;
			URL link2 = new URL(page2);
			HttpURLConnection fconnect2 = (HttpURLConnection)link2.openConnection();
			InputStreamReader in2 = new InputStreamReader(fconnect2.getInputStream());
			BufferedReader br2 = new BufferedReader(in2);

			String curLine2 = "";
			while ( (curLine2 = br2.readLine()) != null ) {
				content += curLine2;
			}
			br2.close();
			
			Pattern r = Pattern.compile("<pre class=\"sequences ajax\">(.*)</pre>");
			Matcher m = r.matcher(content);
			if (m.find()) {
				res = m.group(1);
			}
			
			String fa = res.replaceAll("<.*?>","");
			String fa_r2 = fa.replace("&gt;",">");
			String fa_r3 = fa_r2.replaceAll("\\s1\\s+","\n");
			int idEnd = fa_r3.indexOf("\n");
			String faC = fa_r3.substring(idEnd).replaceAll("\\s+\\d+\\s+","");
			
			s2 = fa_r3.substring(0,idEnd) + faC + "\n";
			//System.out.println(fa_r3.substring(0,idEnd) + faC);
			
		} catch (Exception Ex) {
			System.out.println("failed " + each_id);
		}
		return s2;
	}

        public static void main(String[] args) throws IOException{
                ArrayList<String> x = getLines("re1.txt");
		
		FileWriter fw = new FileWriter("D:/ME2_protein.fas", true);
		for (String s: x) {
			
			String merops_fa = getUniprot(s);
			fw.write(merops_fa);
		}
		fw.flush();
		fw.close();
	}
}


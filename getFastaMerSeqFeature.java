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

public class getFastaMerSeqFeature {
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
	public static ArrayList<String> getUniprot(String each_id) {
		String content = "";
		String MerNum = "";
		String merops_link = "";
		
		HttpURLConnection conn = null;
		
		ArrayList<String> M_IDs = new ArrayList<String>();
		String s = new String();
		try {
			
			merops_link = "http://merops.sanger.ac.uk/cgi-bin/sequence_features?mid=" + each_id;
			URL link = new URL(merops_link);
			conn = (HttpURLConnection)link.openConnection();
			conn.connect();
			conn.setConnectTimeout(20000); 
			conn.setReadTimeout(20000);
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);

			while ( (content = br.readLine()) != null ) {
				// content += curLine;
				if (content.indexOf("/cgi-bin/aaseq?mernum=") != -1) {
					int mernum_start = content.indexOf("mernum=") + 7;
					int mernum_end = content.indexOf("\'", mernum_start);
					s = content.substring(mernum_start, mernum_end);
					MerNum = s.replaceAll("\\s*", "");
					if (! M_IDs.contains(MerNum)) {
						M_IDs.add(MerNum);
					}
					//System.out.println(uniprot_link);
				}
			}
			br.close();
			in.close();
		} catch (Exception Ex) {
			System.out.println("failed " + each_id);
		}
		return M_IDs;

	}
	public static String getFasta(String MeropsNum) {
		System.out.println(MeropsNum);
		String PageContent = new String();
		String pre_result = "";
		String fasta_content = "";
		
		try { 
			String page2 = "http://merops.sanger.ac.uk/cgi-bin/aaseq?mernum=" + MeropsNum;
			URL link2 = new URL(page2);
			HttpURLConnection fconnect2 = (HttpURLConnection)link2.openConnection();
			fconnect2.connect();
			fconnect2.setConnectTimeout(20000); 
			fconnect2.setReadTimeout(20000);
			InputStreamReader in2 = new InputStreamReader(fconnect2.getInputStream());
			BufferedReader br2 = new BufferedReader(in2);

			String curLine2 = "";
			while ( (curLine2 = br2.readLine()) != null ) {
				PageContent += curLine2;
			}
			br2.close();
			in2.close();
			Pattern r = Pattern.compile("<pre class=\"sequences ajax\">(.*)</pre>");
			Matcher m = r.matcher(PageContent);
			if (m.find()) {
				pre_result = m.group(1);
			}
			
			String fa = pre_result.replaceAll("<.*?>","");
			String fa_r2 = fa.replace("&gt;",">");
			String fa_r3 = fa_r2.replaceAll("\\s1\\s\\s\\s+","\n");
			int idEnd = fa_r3.indexOf("\n");
			String faC = fa_r3.substring(idEnd).replaceAll("\\s+\\d+\\s+","");
			
			fasta_content = fa_r3.substring(0,idEnd) + faC + "\n";
			//System.out.println(fa_r3.substring(0,idEnd) + faC);
			
		} catch (Exception Ex) {
			System.out.println("failed " + MeropsNum);
		}
		return fasta_content;
	}

        public static void main(String[] args) throws IOException{
                
                ArrayList<String> x = getLines("nohits.txt");
		
		FileOutputStream out = new FileOutputStream("Merops_restProteins1.txt");
		for (String s: x) {
			ArrayList<String> menums = getUniprot(s);
			for (String merID: menums) {
				
				String merops_fa = getFasta(merID);
				byte[] in_fa = merops_fa.getBytes();
				out.write(in_fa);
			}
		} 
	}
}


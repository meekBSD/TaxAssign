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

public class getFastaFromMeroPS_re {
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
		String uniprot_link = "";
		
		ArrayList<String> U_IDs = new ArrayList<String>();
		String s, s2;
		try {
			String page = "http://merops.sanger.ac.uk/cgi-bin/pepsum?id=" + each_id;
			URL link = new URL(page);
			URLConnection fconnect = (URLConnection)link.openConnection();
			InputStreamReader in = new InputStreamReader(fconnect.getInputStream());
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
			
			String page2 = "http://merops.sanger.ac.uk/cgi-bin/sequence_data?mid=" + each_id;
			URL link2 = new URL(page2);
			URLConnection fconnect2 = (URLConnection)link2.openConnection();
			InputStreamReader in2 = new InputStreamReader(fconnect2.getInputStream());
			BufferedReader br2 = new BufferedReader(in2);

			String curLine2 = "";
			while ( (curLine2 = br2.readLine()) != null ) {
				// content += curLine;
				if (curLine2.indexOf(MerNum) != -1 && curLine2.indexOf("http://www.uniprot.org/uniprot/") != -1) {
					int uniprot_start = curLine2.indexOf("http://www.uniprot.org/uniprot/") + 31;
					int uniprot_end = curLine2.indexOf("\'", uniprot_start);
					s2 = curLine2.substring(uniprot_start, uniprot_end);
					uniprot_link = s2.replaceAll("\\s*", "");
					if (! U_IDs.contains(uniprot_link)) {
						U_IDs.add(uniprot_link);
					}
					//System.out.println(uniprot_link);
				}
			}
		} catch (Exception Ex) {
			System.out.println("failed " + each_id);
		}
		return U_IDs;

	}
	public static String writeFasta(String uniprotID) {
		System.out.println(uniprotID);
		String fa_link = "http://www.uniprot.org/uniprot/" + uniprotID + ".fasta";
		String file_content = "";
		try { 
			//FileOutputStream out = new FileOutputStream(local_fasta);
			URL linkfa = new URL(fa_link);
			URLConnection connectfa = (URLConnection)linkfa.openConnection();
			InputStreamReader info = new InputStreamReader(connectfa.getInputStream());

			BufferedReader br = new BufferedReader(info);

			String curline = "";
			while ( (curline = br.readLine()) != null ) {
				file_content += curline + "\n";
			}
			
		} catch (Exception Ex) {
			System.out.println("failed :"+ " no uniprot Result about " + uniprotID);
		}
		return file_content;
	}

        public static void main(String[] args) throws IOException{
                
                ArrayList<String> x = getLines("D:/AM_ID.txt");
		
		FileWriter fw = new FileWriter("D:/ME1_protein.fas", true);
		for (String s: x) {
			
			ArrayList<String> all_uniprot = getUniprot(s);
			for (String one_Uniprot: all_uniprot) {
				
				String fastr = writeFasta(one_Uniprot);
				fw.write(fastr);
			}
		}
		fw.flush();
		fw.close();
	}
}


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

public class getFastaFromMeroPS {
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
                /** for (String i: re){
                        System.out.println(i);
                } */
		return re;
        }
	public static String getURL(String each_id) {
		String content = "";
		String add = "";
		String s;
		try {
			String page = "http://merops.sanger.ac.uk/cgi-bin/pepsum?id=" + each_id;
			URL link = new URL(page);
			URLConnection connect = (URLConnection)link.openConnection();
			InputStreamReader in = new InputStreamReader(connect.getInputStream());
			BufferedReader br = new BufferedReader(in);

			String curLine = "";
			while ( (curLine = br.readLine()) != null ) {
				content += curLine;
			}
			if ( content.indexOf("Uniprot accession") != -1) {
				int UniprotAccession = content.indexOf("Uniprot accession <a href=");
				int link_end = content.indexOf("\'",UniprotAccession+28);
				s = content.substring(UniprotAccession+27, link_end);
				add = s.replaceAll("\\s*","");
			}
		} catch (Exception Ex) {
			System.out.println("failed" + each_id);
		}
		return add;

	}
	public static String writeFasta(String uniprotLink) {
		String fa_link = uniprotLink + ".fasta";
		//File local_fasta = new File("Saved" + uniprotLink + ".fas");
		String file_content = "";
		//byte[] srcbyte = null;
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
			//srcbyte = file_content.getBytes();

			//out.write(srcbyte);
		} catch (Exception Ex) {
			System.out.println("failed "+ "no uniprot Result" + uniprotLink);
		}
		return file_content;
	}

        public static void main(String[] args) throws IOException{
                ArrayList<String> x = getLines("/home/wuyz/task/MEROPS/ID.txt");
		//FileOutputStream  all_fa = new FileOutputStream("allMeroPS.fas");
		FileWriter fw = new FileWriter("all_protein.fas", true);
		for (String s: x) {
			String a_uniprot = getURL(s);
			String fastr = writeFasta(a_uniprot);
			//byte[] fabyte = fastr.getBytes();
			//all_fa.write(fabyte);
			fw.write(fastr);
		}
		fw.flush();
		fw.close();
	}
}


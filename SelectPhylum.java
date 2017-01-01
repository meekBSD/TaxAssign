import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.HashSet;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Random;

class MyBioinfoPipeline {
	
    public void readFa() {
        HashSet<String> sp = new HashSet<String>();
        try{
            File speName = new File("/Users/EK/Downloads/select_ID.txt");
            BufferedReader vbr = new BufferedReader(new FileReader(speName));
            String line = null;

            while((line = vbr.readLine()) != null) {
                String[] sp_ta = line.split("\\t");
                sp.add(sp_ta[0]);
            }
            vbr.close();
            
      	    // File input=new File("E:/redsea/primer/test_F");
      	    File input=new File("/Users/qiao/Downloads/example.fasta");
      	    Scanner sc = new Scanner(input);   
      	    String fas = new String();
            sc.useDelimiter(">");
   
            // FileOutputStream out = new FileOutputStream("d:/redsea/Out.txt");
            FileOutputStream out = new FileOutputStream("/Users/qiao/Downloads/out.fasta");
            while (sc.hasNext()) {
                String fa_Content = sc.next(); //System.out.println(fa_Content);
               	int eachIDEnd = fa_Content.indexOf("\n");
		String eachID = fa_Content.substring(0,eachIDEnd);
		if (sp.contains(eachID)) {
                    byte[] srtbyte = (">"+fa_Content).getBytes();
                    out.write(srtbyte);
               	} 
            }                  
            out.flush();
            out.close();              	    	                  	  	  
      	} catch (Exception e) {}
    }
    public static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;
        
        Random randGen = new Random();
        int[] orig = new int[len];
        
        for (int i = min; i < min+len; i++) {
            orig[i-min] = i;
        }
        if (max <= min || n > len) {
            return orig;
        }
        
        int[] a = new int[n];
        
        int tmp;
        int s = 0;
        for(s = 0; s < n; s ++) {
            int choice = randGen.nextInt(len-1-s);
            a[s] = orig[choice];
            tmp = orig[choice];
            orig[choice]= orig[len-1-s];
            orig[len-1-s] = tmp;
        }
        return a;
    }      
}
public class SelectPhylum {
    public static void main(String[] args){
        MyBioinfoPipeline pro = new MyBioinfoPipeline();
        pro.readFa();
        int[] x = pro.randomArray(2,100,8);
        for (int i: x){
            System.out.println(i);
        }

        // set default value of each parameter
        boolean prarameter1 = false;  
        int prarameter2 = 0;  
        String prarameter3 = "";  
          
        /* 
         *  offSet variable , locate the position of each argument 
         */  
        int optSetting = 0;  
        for (; optSetting < args.length; optSetting++) {  
            if ("-b".equals(args[optSetting])) {  
                prarameter1 = true;  
            } else if ("-i".equals(args[optSetting])) {  
                prarameter2 = Integer.parseInt(args[++optSetting]);  
            } else if ("-s".equals(args[optSetting])) {  
                prarameter3 = args[++optSetting];  
            }  
        }  
          
        /* 
         * print each parameter.
         */  
        System.out.println(prarameter1);  
        System.out.println(prarameter2);  
        System.out.println(prarameter3); 
    }
}

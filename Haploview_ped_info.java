import java.io.File;  
import java.io.FileOutputStream;
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  
import java.io.OutputStreamWriter;    
import java.io.BufferedReader;  
import java.util.ArrayList;  
import java.util.Date; 
import java.util.List;  
import java.util.logging.Logger;  
import java.util.logging.Level;


class MyBioinfoPipeline {


    // usually path 
    private static final String basePath = "D:/";  
  
    private static final String executeShellLogFile = basePath + "Output_Shell.log";  
  
    private static final String sendKondorShellName = basePath + "test_bash_function.sh";  
    
    public void tansform_ped(String ped) throws IOException{


        StringBuffer stringBuffer = new StringBuffer();  
        BufferedReader vbr = null;  
         //
         //Read content of file
        try { 
            File ped_f = new File(ped);
            FileInputStream in=new FileInputStream(ped_f);
            InputStreamReader inReader=new InputStreamReader(in, "utf-8");
            vbr = new BufferedReader(inReader);
            String line = null;
            String each_field = null;
            int pheno ;

            while((line = vbr.readLine()) != null) {
                //String[] all_C = line.split("\\t");
                String[] all_C = line.split(" ");
                // for(str s:all_C)
                for(int i = 0; i<all_C.length; i++){
                    switch(i>=0?(i>4?(i>5?3:2):1):-1){
                        case (1):
                            each_field = all_C[i];
                            break;
                        case (2):
                            pheno = Integer.parseInt(all_C[i]);
                            each_field = pheno==-9?"0":all_C[i];
                            break;
                        case (3):
                            each_field = all_C[i].replace("A", "1").replace("C","2").replace("G","3").replace("T","4");
                            break;
                        default:
                            each_field = "";
                            break;
                        }

                    stringBuffer.append(each_field).append(" "); 
                    }
                stringBuffer.append("\n");
                }
                
            vbr.close();
 
   
            // FileOutputStream out = new FileOutputStream("d:/redsea/Out.txt");
            // write output to file.  
            OutputStream outputStream = new FileOutputStream("new_" + ped);
            OutputStreamWriter out = new OutputStreamWriter(outputStream, "UTF-8");  
            out.write(stringBuffer.toString()); 

            out.flush();
            out.close(); 

        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

    }


    public void tansform_info(String info) throws IOException{
        File infoName = new File(info);
        int extenName = info.indexOf(".map");
        String newName = "new_" + info.substring(0, extenName) + ".info";
        BufferedReader brInput = null;
        File outN = new File(newName);

        try{


            FileInputStream inputStream=new FileInputStream(infoName);
            brInput = new BufferedReader(new InputStreamReader(inputStream, "utf-8")); 
            
            FileOutputStream out = new FileOutputStream(outN); 

            String line = null;

            while((line = brInput.readLine()) != null) {
                String[] st = line.split("\\t"); 
                int i = 0; 
                for(String s: st){
                    i++;
                    if(i==2){
                        byte[] srtbyte = (s+"\t").getBytes();
                        out.write(srtbyte); 
                
                    }else if(i==4){
                        byte[] srtbyte = (s+"\n").getBytes();
                        out.write(srtbyte); 
                    }  
                    

                    //System.out.println("Token:" + st.nextToken());  
                }  
            }

            out.flush();
            out.close(); 
            brInput.close();
   
 
        } catch (IOException e) {  
            e.printStackTrace();  
        }  

        // ref   http://lgd-java2eye.iteye.com/blog/755743

    }


    public static String[] getFileArgs(String[] parameters) {

        String[] para = new String[3];

         // set default value of each parameter
        boolean parameter1 = false;  
        int parameter2 = 0;  

        String parameter3 = "";
        String parameter4 = "";
        String haploJar = ""; 
          
        /* 
         *  offSet variable , locate the position of each argument , and store each parameter in string array.
         */  
        int optSetting = 0;  
        for (; optSetting < parameters.length; optSetting++) {  
            if ("-b".equals(parameters[optSetting])) {  
                parameter1 = true;  
            } else if ("-i".equals(parameters[optSetting])) {  
                parameter2 = Integer.parseInt(parameters[++optSetting]);  
            } else if ("-p".equals(parameters[optSetting])) {  
                //parameter3 = parameters[++optSetting]; 
                para[0] =  parameters[++optSetting];
            } else if ("-m".equals(parameters[optSetting])) {  
                //parameter4 = parameters[++optSetting]; 
                para[1] = parameters[++optSetting]; 
            } else if ("-h".equals(parameters[optSetting])) {  
                //parameter4 = parameters[++optSetting]; 
                para[2] = parameters[++optSetting]; 
            }
        }  
          
        /* 
         *  return string array 
         */  
        return para; 
    }



    public int executeShell(String shellCommand) throws IOException {  
      
        Logger log = Logger.getLogger("lavasoft"); 
        log.setLevel(Level.INFO);
        Process process = null;  
        // List<String> processList = new ArrayList<String>(); 

        String os = System.getProperty("os.name");  
        if(os.toLowerCase().startsWith("win")){  
            System.out.println(os + " can't gunzip");  
        }   
        try {
            try{  
                //process = Runtime.getRuntime().exec("ps -aux");
                process = Runtime.getRuntime().exec(shellCommand);
                process.waitFor(); 
                int exitValue = process.waitFor();
                if (0 != exitValue) {
                    log.info("call shell failed. error code is :" + exitValue);
                }
            } catch (Throwable e) {
                process.getOutputStream().close();
                process.getInputStream().close();
                process.getErrorStream().close();
                log.info("call shell failed. " + e);
            } 
          
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));  
            FileOutputStream out = new FileOutputStream(executeShellLogFile);
            String line = "";  
            while ((line = input.readLine()) != null) {  
                // processList.add(line); 
                byte[] srtbyte = (line+"\n").getBytes();
                out.write(srtbyte); 
            }  
            input.close();
            out.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  

        return 0;
    }        


    public boolean deleteFile(String filePath) {   // delete single file
        boolean flag = false;
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {  // check if it exists.
            file.delete();                     // delete file
            flag = true;
        }
        return flag;
    }
}

public class Haploview_ped_info {
    public static void main(String[] args) throws IOException {
        MyBioinfoPipeline pro = new MyBioinfoPipeline();
        String[] input_Para = pro.getFileArgs(args);

        if (input_Para == null || input_Para.length ==0){
            System.exit(1);
        } else {
            /*for(String f: input_Para){
                if (f !=null && f.endsWith(".ped")){
                    pro.tansform_ped(f);
                } else if (f !=null && f.endsWith(".map")){
                    pro.tansform_info(f);
                } 
            } */

            if (input_Para[0] != null && input_Para[1] != null){
                String newP = "new_" + input_Para[0];
                File hE = new File(newP);
                if (!hE.exists()){
                    pro.tansform_ped(input_Para[0]);
                }

                int extenName = input_Para[1].indexOf(".map");
                String newI = "new_" + input_Para[1].substring(0, extenName) + ".info";
                File hI = new File(newI);
                if (!hI.exists()){
                    pro.tansform_info(input_Para[1]);
                }
    
                if (input_Para[2] != null){
                    String cmdA = "java -jar " + input_Para[2] + " -memory 2000 -pedfile new_" + input_Para[0] +" -info " +
                    newI +" -minMAF 0.2 -minGeno 0.3 -hwcutoff 0.0001 -maxDistance 250 -n -png -ldcolorscheme RSQ -dprime";
                    pro.executeShell("cmd /c start "+ cmdA);
                    System.out.println(cmdA);

                    pro.deleteFile(newP);
                    pro.deleteFile(newI);
                }       
            }

        }
        
    }
}


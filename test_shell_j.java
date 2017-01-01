import java.io.File;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  
import java.io.OutputStreamWriter;  
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.io.BufferedReader;  
import java.util.ArrayList;  
import java.util.Date; 
import java.util.List;  
import java.util.logging.Logger;  
import java.util.logging.Level;

class JavaShellUtil {  
//基本路径  
private static final String basePath = "/Users/EK/Downloads/";  
  
//记录Shell执行状况的日志文件的位置(绝对路径)  
private static final String executeShellLogFile = basePath + "executeShell.log";  
  
//发送文件到系统的Shell的文件名(绝对路径)  
private static final String sendKondorShellName = basePath + "sendKondorFile.sh";  
  
public int executeShell(String shellCommand) throws IOException {  
     Logger log = Logger.getLogger("lavasoft"); 
     log.setLevel(Level.INFO);
        Process process = null;  
        List<String> processList = new ArrayList<String>();  
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
                processList.add(line); 
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
  
}

public class test_shell_j {
    public static void main(String[] args) throws IOException {
        JavaShellUtil pro = new JavaShellUtil();
        pro.executeShell("ls -l");
    }  
}

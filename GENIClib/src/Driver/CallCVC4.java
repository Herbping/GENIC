package Driver;

import java.io.*;

import parser.parser;
import ast.CoderNode;
import java_cup.runtime.Symbol;

public class CallCVC4 {
	public CallCVC4(){
		
	}
	
	static public String CallByString(String s) throws InterruptedException{

		File dir = new File("tmp");
		dir.mkdirs();
		File tmp = new File(dir, "tmp.txt");
		Runtime rt = Runtime.getRuntime();  
        String result = "";
		try {
			tmp.createNewFile();
			WriteStringToFile(tmp,s);
            Process proc = rt.exec(new String[]{"lib/cvc4","--lang=sygus","--dump-synth","tmp/tmp.txt"});  
            InputStream stderr = proc.getErrorStream();  
            InputStreamReader isr = new InputStreamReader(stderr);  
            BufferedReader br = new BufferedReader(isr);  
            InputStreamReader ir=new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader (ir);

            String line = null;  
            line = input.readLine();
            if(line == null){
            	return "<error>";
            }else 
            if(line.equals("unsat")){            	
            //result = "<output>\n";
            while ((line = input.readLine ()) != null)
            	result = result + line + "\n";
            }	else{
            result = "<unknown>";
            while ((line = br.readLine()) != null)  
                result = result + line + "\n";  
            }
			tmp.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	static void WriteStringToFile(File f, String s) throws IOException{
		FileWriter fileWriter = new FileWriter(f);
		fileWriter.write(s);
		fileWriter.close();
	}

}

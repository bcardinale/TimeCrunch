package mhat.sands.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrunchService implements Runnable {

	String crunchpath = "/usr/local/bin/crunch";
	String inputfile;
	String outputfile;
	List<String> variables;
	
	
	
	public CrunchService(String inputfile, String outputfile, List<String> variables){
		
		this.inputfile = inputfile;
		this.variables = variables;
		this.outputfile = outputfile;
		
	}
	
	//TODO Way too many disc writes, need to make more efficient, need to create our own permutation service;
	@Override
	public void run() {
		
		
		//TODO: Need to update these numbers to the length of all the variables.
		UUID dId = UUID.randomUUID();
		
		String sCurrentLine; 
		BufferedReader br;
		try {
			Path tempDir = Files.createTempDirectory("sands-" + dId.toString());
			System.out.println("Temp Directory: " + tempDir.toString());
			
			br = new BufferedReader(new FileReader(this.inputfile));
			while ((sCurrentLine = br.readLine()) != null) {
				 
				String tempfile = tempDir.toString() + File.separator + sCurrentLine + ".crunch";
				System.out.println(tempfile);
				
				List<String> cmdList = new ArrayList<String>();
				cmdList.add(crunchpath);
				cmdList.add("100");
				cmdList.add("100");
				cmdList.add("-o");
				cmdList.add(tempfile);
				cmdList.add("-p");
				cmdList.add(sCurrentLine);
				cmdList.addAll(variables);
				
				ProcessBuilder   ps=new ProcessBuilder(cmdList.toArray(new String[cmdList.size()]));
				
				ps.redirectErrorStream(true);

				Process pr = ps.start();  

				BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
				    System.out.println(line);
				}
				//pr.waitFor();
				System.out.println("ok!");

				in.close();

			}
			
			String ls = System.getProperty("line.separator");
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.outputfile))) {

				File folder = new File(tempDir.toString());
				File[] listOfFiles = folder.listFiles();

				    for (int i = 0; i < listOfFiles.length; i++) {
				      if (listOfFiles[i].isFile()) {
				    	  
				    	  br = new BufferedReader(new FileReader(listOfFiles[i]));
							while ((sCurrentLine = br.readLine()) != null) {
								 
								bw.write(sCurrentLine + ls);
								
							}
							listOfFiles[i].delete();
				      } 
				    }
				    
				    File tempDirF = tempDir.toFile();
				    tempDirF.delete();

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
		
		
		
		
	}

	
	
	
}

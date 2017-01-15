package mhat.sands.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mhat.sands.object.MutableInt;

public class CollisionDetectionService implements Runnable {

	private String inputfile;
	
	private Set<String> lineitems;
	private Map<String,MutableInt> duplicates;
	
	public CollisionDetectionService(String inputfile){
		this.inputfile = inputfile;
		this.lineitems = new HashSet<String>();
		this.duplicates = new HashMap<String, MutableInt>();
	}
	
	public boolean CheckCollision(String candidate){
		return this.lineitems.contains(candidate);
	}
	
	public Map<String,MutableInt> getDuplicates(){
		return this.duplicates;
	}

	@Override
	public void run() {
		BufferedReader br;
		String sCurrentLine; 
		
		try{
		
			br = new BufferedReader(new FileReader(this.inputfile));
			while ((sCurrentLine = br.readLine()) != null) {
		
				if(!this.lineitems.add(sCurrentLine)){
					if(this.duplicates.containsKey(sCurrentLine)){
						MutableInt count = this.duplicates.get(sCurrentLine);
						if (count == null) {
							this.duplicates.put(sCurrentLine, new MutableInt());
						}
						else {
						    count.increment();
						}
					}else{
						this.duplicates.put(sCurrentLine, new MutableInt());
					}
					
				}
				
			}
			br.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
}



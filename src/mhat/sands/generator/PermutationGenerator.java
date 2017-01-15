package mhat.sands.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermutationGenerator implements Runnable {

	private String inputfile;
	private String outputfile;
	private String delimiter;
	private List<String> variables;
	private List<Long> increments;
	private List<String> permutations;
	
	public List<String> getPermutations(){
		return this.permutations;
	}
	
	public PermutationGenerator(String inputfile, String outputfile, String delimiter, List<String> variables){
		this.inputfile = inputfile;
		this.outputfile = outputfile;
		this.delimiter = delimiter;
		this.variables = variables;
	}
	
	public PermutationGenerator(List<Long> increments, String delimiter, List<String> variables){
		this.delimiter = delimiter;
		this.variables = variables;
		this.increments = increments;
		this.permutations = new ArrayList<String>();
	}
	
	public void PermutateIncrements(){
		
		for(Long i : increments){
			List<String> vars = new ArrayList<String>(this.variables);
			
		     BigDecimal phphack = BigDecimal.valueOf(i).divide(BigDecimal.valueOf(1000),2, BigDecimal.ROUND_DOWN);
		//	BigDecimal phphack = BigDecimal.valueOf(i).divide(100, 2, BigDecimal.ROUND_DOWN);
			//value.setScale(2, RoundingMode.CEILING)
			
			vars.add(phphack.toString());
			
			Set<List<String>> permlists = this.permutation(vars);
			List<String> permutes = this.buildstrings(permlists);
			
			for(String p : permutes){
				this.permutations.add(p);
			}
		}
	
		
	}

	@Override
	public void run() {
		String ls = System.getProperty("line.separator");
		
		if(inputfile.isEmpty()){
			Set<List<String>> permlists = this.permutation(this.variables);
			List<String> permutes = this.buildstrings(permlists);
			
			BufferedWriter bw;
			try{
				bw = new BufferedWriter(new FileWriter(this.outputfile));
				for(String p : permutes){
					bw.write(p + ls);
				}
				bw.close();
			}catch(IOException e){
				e.printStackTrace();
			}			
		}else{
			
			
			String sCurrentLine; 
			BufferedReader br;
			BufferedWriter bw;
			
			try {
				
				bw = new BufferedWriter(new FileWriter(this.outputfile));
				br = new BufferedReader(new FileReader(this.inputfile));
				while ((sCurrentLine = br.readLine()) != null) {
					
					List<String> vars = new ArrayList<String>(this.variables);
					vars.add(sCurrentLine);
					
					Set<List<String>> permlists = this.permutation(vars);
					List<String> permutes = this.buildstrings(permlists);
					
					for(String p : permutes){
						bw.write(p+ls);
					}
				}
				
				bw.close();
				br.close();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	private List<String> buildstrings(Set<List<String>> partslist){
		List<String> r = new ArrayList<String>();
		
		for(List<String> permlist : partslist){
			
			int last = permlist.size()-1; //last part will be blank string we created
			int current = 1;
			StringBuilder sb = new StringBuilder();
			for(String perm : permlist){
				sb.append(perm);
				
				if(current < last){
					sb.append(this.delimiter);
				}
				current++;
				
			}
			r.add(sb.toString());
		}	
		return r;
	}
	
	private Set<List<String>> permutation(List<String> str){

		Set<List<String>> result = new HashSet<List<String>>();

		if(str == null){
			return null;
		} else if (str.size() == 0) {
			List<String> blank = new ArrayList<String>();
			blank.add("");
			result.add(blank);
			return result;
		}

		String firstChar = str.remove(0);
		//str contains remaining elements
		Set<List<String>> words = permutation(str);
		for(List<String> newString : words){
			for(int i = 0; i < newString.size(); i++){
				
				result.add(this.CharAdd(new ArrayList<String>(newString), firstChar, i));
			
			}
		}
		return result;
	}

	private List<String> CharAdd(List<String> newString, String firstChar, int j){
		newString.add(j, firstChar);
		return newString;
	}
	
}

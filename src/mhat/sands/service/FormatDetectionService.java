package mhat.sands.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatDetectionService {

	//TODO prevent readahead on EPOCH to avoid overlap with MS
	private static String REGEX_EPOCH = "[0-9]{10}";
	private static String REGEX_EPOCHMS = "[0-9]{10}.[0-9]{3}";
	private static String REGEX_UUID = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
	
	private String value;
	
	private Pattern epoch_pattern;
	private Pattern epochms_pattern;
	private Pattern uuid_pattern; 
	
	public FormatDetectionService(String seed){
		
		this.value = seed;
		
		this.uuid_pattern = Pattern.compile(REGEX_UUID);
		this.epoch_pattern = Pattern.compile(REGEX_EPOCH);
		this.epochms_pattern = Pattern.compile(REGEX_EPOCHMS);
		
		
		
	}
	
	public boolean isEpoch(){
		
		Matcher m = epoch_pattern.matcher(this.value);

		return m.matches();
	}
	
	public boolean isEpochMS(){
		
		Matcher m = epochms_pattern.matcher(this.value);
		
		return m.matches();
		
	}
	
	public boolean isUUID(){
		
		Matcher m = uuid_pattern.matcher(this.value);
		
		return m.matches();
		
	}
	
}

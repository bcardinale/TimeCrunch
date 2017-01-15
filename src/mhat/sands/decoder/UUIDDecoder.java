package mhat.sands.decoder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import mhat.sands.generator.UUIDGenerator;

public class UUIDDecoder {

	public static void Decode(String uuidString, String inputfile){
		if(!inputfile.isEmpty()){
			
			String sCurrentLine; 
			BufferedReader br;
			
			printHeader();
			
			try {
				br = new BufferedReader(new FileReader(inputfile));
				while ((sCurrentLine = br.readLine()) != null) {
					
					decode(sCurrentLine);
					
				}
				
				br.close();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else{
			printHeader();
			decode(uuidString);
		}
			
		
	}
	
	private static void decode(String uuidString){
		UUID uuid = UUIDGenerator.decodeUUID(uuidString);
		/*
		 * 
		 * 1 Time-based UUID
			2 DCE security UUID
			3 Name-based UUID
			4 Randomly generated UUID
		 */
		
		int version = uuid.version();
		int variant = uuid.variant();

		
		
		switch(version){
			case 1:
				
				Date timestamp = new Date(UUIDGenerator.getTimeFromUUID(uuid));
				
				String mac = UUIDGenerator.bytesToHex(UUIDGenerator.longToBytes(uuid.getLeastSignificantBits()));
				//TODO generate MAC address string
				System.out.println(uuid + "\t| 1 (Time-based) | " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(timestamp)  + "[NOT IMPLEMENTED]");
				break;
			case 2:
				System.out.println("Version 2 - DCE security UUID");
				break;
			case 3:
				System.out.println("Version 3 - Name-based UUID");
				break;
			case 4:
				System.out.println("Version 4 - Randomly Generated UUID");
				break;
		}
	}
	
	private static void printHeader(){
		System.out.println("UUID\t\t\t\t\t|VERSION\t|TIMESTAMP\t\t|MACADDRESS");
		
	}
	
	
}

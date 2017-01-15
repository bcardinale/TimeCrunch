package mhat.sands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mhat.sands.decoder.EpochDecoder;
import mhat.sands.decoder.UUIDDecoder;
import mhat.sands.generator.EpochGenerator;
import mhat.sands.generator.EpochMsGenerator;
import mhat.sands.generator.PermutationGenerator;
import mhat.sands.generator.UUIDGenerator;
import mhat.sands.object.MutableInt;
import mhat.sands.service.CollisionDetectionService;
import mhat.sands.service.CrunchService;
import mhat.sands.service.FormatDetectionService;
import mhat.sands.service.PreImageAttackService;

public class Main {

	public static void main(String[] args) {

		boolean UUID = false;
		boolean EPOCH = false;
		boolean EPOCHMS = false;
		boolean PERMUTATE = false;
		boolean DECODE = false;
		boolean COLLISION = false;
		boolean PREIMAGE = false;
		
		String seed = new String();
		String output = new String();
		String sinterval = new String();
		String inputfile = new String();
		String timeunit = new String();
		String squantity = new String();
		String delimiter = new String();
		
		Date starttime = new Date();
		Date endtime = new Date();
		
		long interval = 0;
		long quantity = 0;
		
		List<String> variables = new ArrayList<String>();
		
		FormatDetectionService fds = new FormatDetectionService("");
		
		//UUID + Seed Value + ForwardRange + NegativeRange + Output
		for(int i =0; i < args.length; i++){
			String v = args[i];
			switch(v){
				case "-U":
					UUID = true;
					break;
				case "-E":
					EPOCH = true;
					break;
				case "-M":
					EPOCHMS = true;
					break;
				case "-P":
					PERMUTATE = true;
					break;
				case "-PI":
					PREIMAGE = true;
					break;
				case "-D":
					DECODE = true;
					break;
				case "-C":
					COLLISION = true;
					break;
				case "-f":
					i++;
					inputfile = args[i];
					break;
				case "-v":
					i++;
					for(int y = i; y<args.length; y++){
						variables.add(args[y]);
					}
					break;
				case "-s":
					i++;
					seed = args[i];
					fds = new FormatDetectionService(seed);
					break;
				case "-o":
					i++;
					output = args[i];
					break;
				case "-i":
					i++;
					sinterval = args[i];
					break;
				case "-u":
					i++;
					timeunit = args[i];
					break;
				case "-q":
					i++;
					squantity = args[i];
					break;
				case "-d":
					i++;
					delimiter = args[i];
					break;
				case "-starttime":
					i++;
					starttime = parseDate(args[i]);
					break;
				case "-endtime":
					i++;
					endtime = parseDate(args[i]);
					break;

			}
		}
			
		//test time unit
		//test interval
		//Should auto detect between epoch and epochms
		//Built to be threadable, can divide and run.
		if(PERMUTATE){
			//CrunchService cs = new CrunchService(inputfile, output, variables);
			//cs.run();
			PermutationGenerator pg = new PermutationGenerator(inputfile, output, delimiter, variables);
			pg.run();
			
		} else if (COLLISION){
			
			System.out.println("Checking for collisions..");
			CollisionDetectionService cds = new CollisionDetectionService(inputfile);
			cds.run();
			Map<String,MutableInt> collisions = cds.getDuplicates();
			if(collisions.size() > 0){
				System.out.println("Collision(s detected.");
				System.out.println("Count\t| Value ");
			}
			
			for(String collision : collisions.keySet()){
				MutableInt count = collisions.get(collision);
				
				System.out.println(count.get() + "\t| " + collision);
				
			}			
		} else if (PREIMAGE){
			
			PreImageAttackService pias = new PreImageAttackService(inputfile, starttime, endtime, delimiter, variables);
			pias.run();
			
		} else if(DECODE){ 

			if(fds.isUUID()){
				
				UUIDDecoder.Decode(seed, inputfile);
				
			} else if (fds.isEpoch() || fds.isEpochMS()){
				
				
				Date decode = EpochDecoder.getDateFromString(seed);
				System.out.println("Provided Value: " + seed);
				System.out.println("Timestamp: "  + decode);
				
			} else if (fds.isEpochMS()){
				System.out.println("broken.");
				Date decode = EpochDecoder.getDateFromStringMs(seed);
				System.out.println("Provided Value: " + seed);
				System.out.println("Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(decode));
				
			}
			
		}else if(UUID | EPOCH | EPOCHMS){

			
			if(!squantity.isEmpty()){
				try{
					quantity = Long.valueOf(squantity);
				}catch(Exception e){
					printargs("Invalid quantity value");
					return;
				}
			}else{
				printargs("Quantity value required");
				return;
			}
			
			if(!sinterval.isEmpty()){
				try{
					interval = Long.valueOf(sinterval);
				}catch(Exception e){
					printargs("Invalid interval value");
					return;
				}
			}else{
				printargs("Interval value required.");
				return;
			}
				
			
			if(UUID){

				UUIDGenerator ug = new UUIDGenerator(seed, interval, timeunit, quantity, output);
				ug.run();
				
			}else if(EPOCH){
				
				EpochGenerator eg = new EpochGenerator(seed, interval, timeunit, quantity, output);
				eg.run();
				
			}else if(EPOCHMS){
				
				EpochMsGenerator emg = new EpochMsGenerator(seed, interval, timeunit, quantity, output);
				emg.run();	
			}
		} else {
			printargs("Mode not selected.");
		}
		
		System.out.println("Done");
	}
	
	public static Date parseDate(String dateString){
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {

            Date date = formatter.parse(dateString);
            
            return date;
            
        } catch (Exception ex){
        	System.out.println("Unable to parse date. Expected format: yyyy-MM-dd HH:mm");
        	return null;
        }
		
	}
	
	public static void printargs(String message){
		
		//Consider randomizing least significant bits
		
		/* Args
		 * 
		 * Type
		 * Generate Time 
		 * -U - UUID
		 * -E - EPOCH
		 * -M - EPOCHMS
		 * 	-s - Seed UUID Value
		 *  -i - Interval: A positive or negative number to increment the generation by. Used in combination with time unit
		 * 	-u - Time Unit: Measurement of time to be used with Interval. Values: (ns, ms, s, m, h)
		 *  -q - Quantity: How many candidates to generate
		 *  -o - Output File: file to store results.

		 * -P Create permutations
		 * 	-f - input file
		 * 	-v - variables
		 *  -d - delimiter
		 *  -o - output file
		 * 
		 * -D - Decode Value
		 * 	-s Value to Decode
		 *  -f take values from file
		 *  
		 * -C - Collision Check
		 * 	-f - input file to check for collisions
		 *  
		 * -PI - PreImageAttack
		 * 	- f - Collision File
		 *  - starttime
		 *  - endtime
		 *  - delimiter
		 *  - variables
		 */
		
		System.out.println("TimeCrunch - v0.1");
		System.out.println("Created by Brian Cardinale");
		System.out.println("");
		System.out.println("Generate Time Values");
		System.out.println("\t-U - UUID");
		System.out.println("\t-E - EPOCH");
		System.out.println("\t-M - EPOCHMS");
		System.out.println("\t\t-s - Seed UUID Value");
		System.out.println("\t\t-i - Interval: A positive or negative number used in combination with time unit");
		System.out.println("\t\t-u - Time Unit: Measurement of time to be used with Interval. Values: (ns, ms, s, m, h)");
		System.out.println("\t\t-q - Quantity: How many candidates to generate");
		System.out.println("\t\t-o - Output File: file to store results.");
		System.out.println("");
		System.out.println("\t-P Create permutations");
		System.out.println("\t\t-f - input file");
		System.out.println("\t\t-v - variables");
		System.out.println("\t\t-d - delimiter");
		System.out.println("\t\t-o - output file");
		System.out.println("");
		System.out.println("\t-D - Decode Value");
		System.out.println("\t\t-s Value to Decode");
		System.out.println("\t\t-f take values from file");
		System.out.println("");
		System.out.println("\t-C - Collision Check");
		System.out.println("\t\t-f - input file to check for collisions");
		System.out.println("");
		System.out.println("\t-PI - PreImageAttack");
		System.out.println("\t\t- f - Collision File");
		System.out.println("\t\t- starttime");
		System.out.println("\t\t- endtime");
		System.out.println("\t\t- delimiter");
		System.out.println("\t\t- variables");
		
		return;
		
	}

}

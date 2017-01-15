package mhat.sands.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mhat.sands.generator.EpochMsGenerator;
import mhat.sands.generator.PermutationGenerator;
import mhat.sands.generator.UUIDGenerator;

public class PreImageAttackService implements Runnable {

	//I need previous Hashes - CollisionDetection Service (take in by -f)
	
	//I need seed value -s
	
	//Start Time - Finish Time
	
	//I need to grab a block of time
	
	//I need to permute time
	
	//Check permutations against previous collisions.
	
	private String delimiter;
	private List<String> variables;
	
	private CollisionDetectionService cds;
	
	private Date mintimerange;
	private Date maxtimerange;
	
	private long threads;
	
	
	
	public PreImageAttackService(String collisionfile, Date mintimerange, Date maxtimerange, String delimiter, List<String> variables){
		
		this.cds = new CollisionDetectionService(collisionfile);
		this.mintimerange = mintimerange;
		this.maxtimerange = maxtimerange;
		this.delimiter = delimiter;
		this.variables = variables;
		
		threads = 1;
		
	}

	@Override
	public void run() {
		
		Instant start = Instant.ofEpochMilli(mintimerange.getTime());
		Instant end = Instant.ofEpochMilli(maxtimerange.getTime());
		Duration range = Duration.between(start, end);

		Long seconds = range.getSeconds();
		
		//This is the place to divide the work into multiple threads
		
		long threadChunks = seconds/this.threads;
		
		for(long i =0; i<this.threads; i++){
			
			long offset = threadChunks * i;
			
			Instant tempStart = start.plusSeconds(offset);		
			String seed = String.valueOf(tempStart.toEpochMilli());
			EpochMsGenerator msthread = new EpochMsGenerator(seed, 1,"ms",threadChunks*1000,"");
			msthread.generate();
			List<Long> increments = msthread.getIncrements();
			
		
			PermutationGenerator pg = new PermutationGenerator(increments, this.delimiter, this.variables);
			pg.PermutateIncrements();
			List<String> permutations = pg.getPermutations();
			
			cds.run();
			
			for(String p : permutations){
				byte[] pbytes;
				try {
				
					pbytes = p.getBytes("UTF-8");
					MessageDigest md = MessageDigest.getInstance("MD5");
					byte[] thedigest = md.digest(pbytes);
					String hexdigest = UUIDGenerator.bytesToHex(thedigest);
					
					if(cds.CheckCollision(hexdigest)){
						System.out.println("PreImageFound:\t " + p + " Digest: " + hexdigest);
					}else{
						//System.out.println("Tested:\t " + p + " Digest: " + hexdigest);
					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		
		
		
	}
	
	
}

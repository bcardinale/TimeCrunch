package mhat.sands.generator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EpochGenerator implements Runnable {

	private Long seed;
	private String outputpath;
	private Long timeunit;
	private Long quantity;
	private Long interval;
	
	public EpochGenerator (String seed, long interval, String timeunit, Long quantity, String output){
		
		this.outputpath = output;
		this.quantity = quantity;
		this.interval = interval;
		
		try{
			this.seed = Long.valueOf(seed);
		}catch(Exception e){
			System.out.println("Invalid Epoch Seed format. Value should be numeric. e.g. 1484013669");
			return;
		}
		
		timeunit = timeunit.toLowerCase();
		switch(timeunit){
			case "ns":
				//this.timeunit = NUM_100NS;
				System.out.println("This unit of time is too granular for EpochMs");
				break;
			case "ms":
				System.out.println("This unit of time is too granular for EpochMs");
				break;
			case "s":
				this.timeunit = NUM_1S;
				break;
			case "m":
				this.timeunit = NUM_1M;
				break;
			case "h":
				this.timeunit = NUM_1H;
				break;
			default:
				this.timeunit = NUM_1S;
				break;
		}
	}
	
	//Need to verify this is accurate
	//static final long NUM_100NS = 10000L;
	//static final long NUM_1MS = 100000L;
	static final long NUM_1S = 1L;
	static final long NUM_1M = 60L;
	static final long NUM_1H = 3600L;
	
	
	@Override
	public void run() {
		
		String ls = System.getProperty("line.separator");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.outputpath))) {

			for(long i = 1; i < this.quantity; i++){
	    		
	    		long increment = (this.interval * i) * this.timeunit;
	    		
	    		Long candidate = this.seed + increment;
	    		
	    		bw.write(candidate.toString());
	    		
	    		//System.out.println(candidate);
	    	}

		} catch (IOException e) {

			e.printStackTrace();

		}
		
		
	
		
	}

}

package mhat.sands.generator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

public class UUIDGenerator implements Runnable {

	private String seed;
	private String outputpath;
	private Long timeunit;
	private Long quantity;
	private Long interval;
	
	public UUIDGenerator(String seed, long interval, String timeunit, Long quantity, String output){
		
		this.seed = seed;
		this.outputpath = output;
		this.interval = interval;
		this.quantity = quantity;
	
		timeunit = timeunit.toLowerCase();
		switch(timeunit){
			case "ns":
				this.timeunit = NUM_100NS;
				break;
			case "ms":
				this.timeunit = NUM_1MS;
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
				this.timeunit = NUM_1MS;
				break;
		}
		
	}
	
	//Need to verify this is accurate
	static final long NUM_100NS =     1000L;
	static final long NUM_1MS =      10000L;
	static final long NUM_1S =    10000000L;
	static final long NUM_1M =   600000000L;
	static final long NUM_1H = 36000000000L;
	
	@Override
	public void run() {
		String ls = System.getProperty("line.separator");
		
    	UUID seed_uuid = decodeUUID(seed);
    	
    	System.out.println("SEED: " + seed_uuid.toString());
    	System.out.println("TIMESTAMP: " + new Date(getTimeFromUUID(seed_uuid)));
    	
    	String uuid_suffix = getSuffixFromUUID(seed_uuid);
    	System.out.println("SUFFIX: " + uuid_suffix);

    	String lastuuid = new String();
    	
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.outputpath))) {

		 	for(long i = 1; i < this.quantity; i++){
	    		
	    		long increment = (this.interval * i) * this.timeunit;
	    		
	    		String candidate = encodeUUID(seed_uuid.toString(), increment, uuid_suffix);
	    		
	    		bw.write(candidate + ls);
	    		
	    		lastuuid = candidate;
	    		//System.out.println(candidate);
	    	}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("LAST VALUE: " + lastuuid);
		UUID luuid = UUID.fromString(lastuuid);
    	System.out.println("LAST TIMESTAMP: " + new Date(getTimeFromUUID(luuid)));
		
	}

    private String getSuffixFromUUID(UUID uuid) {
		
    	String suuid = uuid.toString();
    	
    	String suffix = suuid.substring(19);
    	
		return suffix;
	}

	static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;
    
    public static long getTimeFromUUID(UUID uuid) {
	    return (uuid.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000;
	}
    
    public static UUID decodeUUID(String uuid_hexstring){
    	
    	byte[] uuid_bytearray = hexStringToByteArray(uuid_hexstring);
    	
    	ByteBuffer bb = ByteBuffer.wrap(uuid_bytearray);
        long high = bb.getLong();
        long low = bb.getLong();
        UUID uuid = new UUID(high, low);
           	
        return uuid;
    }
    
    
    final protected static String d = "-";
    public static String encodeUUID(String seed_user_id, long increment, String suffix){
    	
    	UUID seed_uuid = decodeUUID(seed_user_id);
    	
    	long seed_date = seed_uuid.timestamp();
    	
    	seed_date = seed_date + increment;
    	
    	byte[] timeBytes = longToBytes(seed_date);
    	
    	String firstOct = bytesToHex(new byte[]{timeBytes[4], timeBytes[5], timeBytes[6], timeBytes[7]});
    	String secondOct = bytesToHex(new byte[]{timeBytes[2], timeBytes[3]});
    	String thirdOct = bytesToHex(new byte[]{timeBytes[0], timeBytes[1]}).replace("0", "1");
    	
    	String s_guid_prefix = (firstOct + d + secondOct + d + thirdOct + d).toLowerCase();
    	
    	String one = s_guid_prefix + suffix;
    	
        return one;
    }
    
    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip 
        return buffer.getLong();
    }
    
    public static byte[] hexStringToByteArray(String s) {
    	s = s.replace("-", "");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
	
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
	
	
}

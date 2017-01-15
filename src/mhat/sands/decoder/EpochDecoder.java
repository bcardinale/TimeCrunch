package mhat.sands.decoder;

import java.util.Date;

public class EpochDecoder {

	public static Date getDateFromString(String dateString){
		
		long dateLong = Long.parseLong(dateString);
				
		return getDate(dateLong * 1000);
				
		
	}
	
	public static Date getDateFromStringMs(String dateString){
			
		long dateLong = Long.parseLong(dateString);
		
		return getDate(dateLong);
	}
	
	public static Date getDate(Long dateLong){
		return new Date(dateLong);
	}
	
}

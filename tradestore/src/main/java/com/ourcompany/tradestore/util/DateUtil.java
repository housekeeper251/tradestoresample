package com.ourcompany.tradestore.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class provides method for date calculation.
 * @author LENOVO
 *
 */
public class DateUtil {

	public  static Date fetchTodaysDateWithoutTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		 
		// Set time fields to zero
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		 
		// Put it back in the Date object
		Date effectiveDateNew=(cal.getTime());
		
		return effectiveDateNew;
	}
	
	public static Date getDateWithoutTime(Date effectiveDate){
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(effectiveDate);
		 
		// Set time fields to zero
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		 
		// Put it back in the Date object
		Date effectiveDateNew=(cal.getTime());
		
		return effectiveDateNew;
	}

	public static Date getCurrentDateWithTime(){
		return new Date();
	}

	
	public static Date getDate(int days){
		
		Calendar cal = Calendar.getInstance();

//		   System.out.println("Today's date is "+dateFormat.format(cal.getTime()));
   		    cal.setTime(new Date());

		    cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

		   cal.add(Calendar.DATE, days);
		
//		   System.out.println("This Week date is "+dateFormat.format(cal.getTime()));  
		   
		   Date effectiveDate=cal.getTime();
		   return effectiveDate;
	}

	
}

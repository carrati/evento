package com.evento.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {
	
	private static Calendar cal = GregorianCalendar.getInstance(new Locale("pt", "BR"));
	private static final SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyyy");
	
	public static Date[] daysOfWeek(Date currDate){
		
		Date[] weekDays = new Date[7];

		if(currDate!=null)
			cal.setTime(currDate);
		
		for(int i=0; i<Calendar.SATURDAY; i++){
			weekDays[i] = cal.getTime();
			cal.add(Calendar.DATE, 1);
		}
  		return weekDays;
	}
	
	public static String formatDate(Date date){
		return DF.format(date);
	}
	
	public static String formatDate(Date date, String pattern){
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public static Date parseDate(String strDate) throws ParseException{
		return DF.parse(strDate);
	}
	
	public static Date parseDate(String strDate, String pattern) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat(pattern); 
		return (Date)formatter.parse(strDate); 
	}
	
	public static Date parseDate(String strDate, String pattern, Locale locale) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale); 
		return (Date)formatter.parse(strDate); 
	}
	
	public static void main(String[] args){
//		daysOfWeek(new Date());
//		System.out.println(getSumDate(-7));
		System.out.println(formatDate(new Date(), "EEE, MMM d, ''yy "));
		System.out.println(formatDate(new Date(), "MMMMM d, yyyy "));
		try {
			System.out.println(parseDate("Feb 15, 2012", "MMM d, yyyy", new Locale("en", "US")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static Date getSumDate(int dias) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_MONTH, dias);
		
		return new Date(gc.getTime().getTime());
	}
	
	public static long dateDiff(Date d1, Date d2){
		return d1.getTime()-d2.getTime();
	}
	public static long dateDiff(long ts1, long ts2){
		return ts1-ts2;
	}
	public static boolean isBefore(Calendar h1, Calendar h2){
		return h1.getTime().getTime()-h2.getTime().getTime()<0;
	}
	
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }
 
    /**
     * Inverte o mês e o dia de uma String.
     * 
     * @param dateStr
     * @return
     */
    public static String invertMonth(String dateStr){
    	String[] fields = null;
    	String separator = "-";
    	
    	if(!dateStr.contains(separator) && dateStr.contains("/")){
    		separator = "/";
    	}
    	
    	fields = dateStr.split(separator);
    	
    	if(fields.length > 1){
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(fields[1]).append(separator).append(fields[0]).append(separator).append(fields[2]); 
	    	return sb.toString();
    	} else {
    		return dateStr;
    	}
    }
 
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
	            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
	            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
	

}

package util.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    public static DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static int WEEK_OF_MONTH = Calendar.WEEK_OF_MONTH;
    public static int WEEKS_FOR_LOAN = 2;
    public static int SATURDAY = Calendar.SATURDAY;
    public static int SUNDAY = Calendar.SUNDAY;
    
    // Eg. DateHelper.addDaysToToday(DateHelper.WEEK_OF_MONTH, DateHelper.WEEKS_FOR_LOAN)
    public static Date addDaysToToday(int context, int duration) {
        Calendar cal = Calendar.getInstance();
        cal.add(context, duration);
        return cal.getTime();
    }
    
    public static Date addDaystoDate(Date startDate, int context, int duration) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(context, duration);
        return cal.getTime();
    }
    
    public static long getNumDaysFromDate(Date date) {
        Date today = Calendar.getInstance().getTime();
        return (today.getTime() - date.getTime()) / (1000 * 60 * 60 * 24);
    }
    
    public static boolean isDateAfterToday(Date date) {
        Date today = Calendar.getInstance().getTime();
        return today.after(date);
    }
    
    public static boolean isBeforeGivenHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY) < hour;
    }
    
    public static boolean isAfterGivenHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY) > hour;
    }
    
    public static boolean isGivenDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == day;
    }
    
    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }
    
    public static String format(Date date) {
        return DATE_FORMATTER.format(date);
    }
    
    public static String formatTimestamp(Date date) {
        return TIMESTAMP_FORMATTER.format(date);
    }
}

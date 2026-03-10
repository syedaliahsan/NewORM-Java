package com.sa.orm.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;

/**
 * <h4>Description</h4>Contains method that can be used for date manipulation.
 * <p>Ranging from parsing and formatting dates, this class has method for date
 * addition, date differences and date comparisons.</p>
 * <p>Most frequently needed methods like <code>getDateToString</code> and
 * <code>getStringToDate</code> are given in this class.</p>
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2004 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>August 07, 2004
 * @author Ali Ahsan
 * @version 1.0
 */
public class DateUtils {

  public final static String yyyy_MM_dd = "yyyy-MM-dd";

  public final static String MMM__dd_yyyy = "MMM dd, yyyy";

  public final static String MMM_yyyy = "MMM, yyyy";

  public final static String MM_dd_yyyy = "MM-dd-yyyy";

  public final static String MMM_dd_yyyy__H_mm = "MMM-dd-yyyy H:mm";

  public final static String MMM_dd_yyyy__h_mm__a = "MMM-dd-yyyy h:mm a";

  public final static String MM_dd_yyyy__H_mm = "MM-dd-yyyy H:mm";

  public final static String MM_dd_yyyy__h_mm__a = "MM-dd-yyyy h:mm a";

  public final static String EEE__MMM__d__H_mm_ss__z__yyyy = "EEE MMM d H:mm:ss z yyyy";

  public final static String EEE_MMM_dd_yyyy_H_mm = "EEE, MMM-dd-yyyy H:mm";

  public final static String EEE_MMM_dd_yyyy_h_mm__a = "EEE, MMM-dd-yyyy h:mm a";

  public final static String H_mm = "H:mm";

  public final static String HH_mm = "HH:mm";

  public final static String h_mm__a = "h:mm a";

  public final static String hh_mm__a = "hh:mm a";

  public final static String EEE_MMM_dd_yyyy = "EEE, MMM-dd-yyyy";

  public final static String[] weekDaysInString = new String[] {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};

  public final static String[] monthInString = new String[] {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

  private static Logger logger = Logger.getLogger(DateUtils.class.getName());
  
  /**
   * This no argument constructor does not do anything.
   */
  public DateUtils() {
    ;
  } // end of no-argument constructor

  /**
   * Returns a <code>java.util.Timestamp</code> object with date
   * incremented by the given <code>incrementDays</code> to the the
   * given <code>sourceDate</code>.
   * @param sourceDate Base date which will be incremented by the
   * given <code>incrementDays</code>.
   * @param incrementDays Increment factor in days.
   * @return Date incremented by given <code>incrementDays</code> in
   * the form of object of <code>java.sql.Timestamp</code>.
   */
  public static java.sql.Timestamp incrementDate(java.util.Date sourceDate,
      int incrementDays) {
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    calendar.setTime(sourceDate);
    calendar.add(Calendar.DATE, incrementDays);
    return new Timestamp(calendar.getTime().getTime());
  } // end of method incrementDate

  /**
   * Returns a <code>java.sql.Timestamp</code> object with date
   * decremented by the given <code>decrementDays</code> to the the given
   * <code>sourceDate</code>.
   * @param sourceDate Base date which will be decremented by the given
   * <code>decrementDays</code>.
   * @param decrementDays Decrement factor in days.
   * @return Date decremented by given <code>decrementDays</code> in the form
   * of an object of <code>java.sql.Timestamp</code>.
   */
  public static java.sql.Timestamp decrementDate(java.util.Date sourceDate,
      int decrementDays) {
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    calendar.setTime(sourceDate);
    decrementDays *= -1;
    calendar.add(Calendar.DATE, decrementDays);
    return new Timestamp(calendar.getTime().getTime());
  } // end of method decrementDate

  /**
   * Returns an object of <code>java.sql.Timestamp</code> having
   * date extracted from <code>datePart</code> and time extracted from
   * <code>timePart</code> objects.
   * @param datePart Date to be included in the returned value.
   * @param timePart Time to be included in the returned value.
   * @return <code>java.sql.Timestamp</code> having the given <code>datePart</code>
   * as date portion and <code>timePart</code> as time portion.
   */
  public static java.sql.Timestamp getDateTime(java.util.Date datePart,
      java.util.Date timePart) {
    if(datePart == null || timePart == null) {
      return null;
    } // end of if
    java.util.Calendar timeCal = java.util.Calendar.getInstance();
    timeCal.setTime(timePart);
    java.util.Calendar dateTime = java.util.Calendar.getInstance();
    dateTime.setTime(datePart);
    dateTime.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
    dateTime.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
    dateTime.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
    dateTime.set(Calendar.MILLISECOND, timeCal.get(Calendar.MILLISECOND));
    return new Timestamp(dateTime.getTime().getTime());
  } // end of method getDateTime

  /**
   * Returns hour value in the given <code>date</code> object.
   * Please note that this hour will be 1 to 12.
   * @param date Date object which will be used to extract hour value.
   * @return Hour of the the given date (1 - 12).
   */
  public static int getHour(java.util.Date date) {

    try {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(Calendar.HOUR);

    } // end of try
    catch (Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return -1;
  } // end of method getHour

  /**
   * Returns hour of the day (0-23) set in the given object.
   * @param date Date object from which Hour of Day is to be extracted.
   * @return Hour of the day value from the given <code>date</code> (0 -23).
   */
  public static int getHourOfDay(java.util.Date date) {

    try {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(Calendar.HOUR_OF_DAY);

    } // end of try
    catch (Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch

    return -1;
  } // end of method getHourOfDay

  /**
   * Returns the minute value in the given <code>date</code> object.
   * @param date Date object from which minute value is to be extracted.
   * @return Minute value from the given date object 0 -59).
   */
  public static int getMinute(java.util.Date date) {

    try {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(Calendar.MINUTE);
    } // end of try
    catch (Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch

    return -1;
  } // end of method getMinute

  /**
   * Returns AM/PM value from the given <code>date</code> object.
   * @param date Date object from which AM/PM value is to be extracted.
   * @return AM/PM value from the given <code>date</code> object.
   */
  public static int getAMPM(java.util.Date date) {

    try {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar.get(Calendar.AM_PM);
    } // end of try
    catch (Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch

    return -1;
  } // end of method getAMPM

  /**
   * Sets the given component of date to the given value in the given
   * <code>sourceDate</code> and returns it.
   * @param sourceDate Date in which the given <code>part</code> is to
   * be set with the given <code>value</code>.
   * @param part Component of date to be set.
   * @param value Value which to be set as the given component of
   * date.
   * @return New date with new value of the given component of time.
   */
  public static Timestamp setComponent(java.util.Date sourceDate, int part,
                                       int value) {

    try {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(sourceDate);
      calendar.set(part, value);
      return new Timestamp(calendar.getTime().getTime());
    } // end of try
    catch (Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return new Timestamp(sourceDate.getTime());
  } // end of method setComponent

  /**
   * Returns the difference between the given two dates. Please note
   * that the first date should be greater than or equal to the second date.
   * @param laterDate Greater date in the two dates.
   * @param earlierDate Smaller date in the two dates.
   * @return Difference in number of days between the two dates.
   */
  public static int getDateDifferenceInDays(java.util.Date laterDate,
      java.util.Date earlierDate) {

    try {
      long difference = laterDate.getTime() - earlierDate.getTime();
      difference = difference/86400000L;
      return (int)difference;
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return -1;
  } // end of getDateDifferenceInDays

  /**
   * Retuns difference between the given two dates in the format
   * number Year(s) number Month(s)
   * If year is zero, the returned string wont have Year part.
   * If month is zero, the returned string wont have Month part.
   * @param laterDate Smaller date in the two dates.
   * @param earlierDate Greater date in the two dates.
   * @return Date Difference in years, months, days as a string.
   */
  public static String getDateDifference(java.util.Date earlierDate,
      java.util.Date laterDate) {
    if(laterDate == null || earlierDate == null) {
      return "";
    } // end of if

    String diff = "";
    int years = getYear(laterDate) - getYear(earlierDate);
    int months = getMonth(laterDate) - getMonth(earlierDate);
    if(months < 0) {
      months += 12;
      years--;
    }

    if(years > 0) {
      diff += years + " Year";
      if(years > 1) {
	diff += "s ";
      } // end of if
    } // end of if

    if(months > 0) {
      diff += months + " Month";
      if(months > 1) {
	diff += "s ";
      } // end of if
    } // end of if

    return diff;
  } // end of method getDateDifference

  /**
   * Returns a {@link String} object having the given <code>date</code> in the
   * given <code>format</code>.
   * @param date Date to be formatted in the desired format.
   * @param format Desired format of the <code>date</code>.
   * @return Formatted <code>date</code> as per the given
   * <code>format</code>.
   */
  public static String getDateToString(java.util.Date date, java.lang.String format) {
    if(date == null || format == null) {
      return null;
    } // end of if
    SimpleDateFormat df = new SimpleDateFormat(format);
    return df.format(date);
  } // end of method getDateToString

  /**
   * Parses the given <code>date</code> string using the given
   * <code>format</code> and creates a <code>java.sql.Date</code>
   * object and returns it. Note that the given <code>format</code>
   * should match the format of given <code>date</code> string.
   * @param date String having a date in it.
   * @param format Format of the <code>date</code>.
   * @return A <code>java.sql.Date</code> object.
   */
  public static java.sql.Date getStringToDate(java.lang.String date,
      java.lang.String format) {

    try {
      SimpleDateFormat format1 = new SimpleDateFormat(format);
      return new java.sql.Date(format1.parse(date).getTime());
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return null;
  } // end of method getStringToDate

  /**
   * Parses the given <code>time</code> string using the given
   * <code>format</code> and creates a <code>java.sql.Time</code>
   * object and returns it. Note that the given <code>format</code>
   * should match the format of given <code>time</code> string.
   * @param time String having a date in it.
   * @param format Format of the <code>time</code>.
   * @return A <code>java.sql.Time</code> object.
   */
  public static java.sql.Time getStringToTime(java.lang.String time,
      java.lang.String format) {

    try {
      SimpleDateFormat format1 = new SimpleDateFormat(format);
      return new Time(format1.parse(time).getTime());
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return null;
  } // end of method getStringToTime

  /**
   * Parses the given <code>timestamp</code> string using the given
   * <code>format</code> and creates a <code>java.sql.Timestamp</code>
   * object and returns it. Note that the given <code>format</code>
   * should match the format of given <code>timestamp</code> string.
   * @param timestamp String having a timestamp in it.
   * @param format Format of the <code>timestamp</code>.
   * @return A <code>java.sql.Timestamp</code> object.
   */
  public static java.sql.Timestamp getStringToTimestamp(java.lang.String timestamp,
      java.lang.String format) {

    try {
      SimpleDateFormat format1 = new SimpleDateFormat(format);
      return new Timestamp(format1.parse(timestamp).getTime());
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return null;
  } // end of method getStringToTimestamp

  /**
   * Returns a <code>String</code> object having a date formatted as
   * yyyy-MM-dd against the given <code>year</code>,
   * <code>month</code> and <code>day</code> strings.
   * It never returns <code>null</code>. It either returns an empty
   * string or a date string in format yyyy-MM-dd
   * It does not check for the correctness of the date.
   * In case any of the given strings is empty or <code>null</code>
   * it returns an empty string.
   * In case length of year is not four, or length of month and day is
   * greater than 2, it returns an empty string.
   * @param year Year part of date (should be 4 digits)
   * @param month Month part of date (1 to 12)
   * @param day day part of date (1 to 31)
   * @return A <code>java.lang.String</code> object having date
   * formatted as yyyy-MM-dd or an empty string.
   */
  public static java.lang.String getDateString(java.lang.String year,
      java.lang.String month, java.lang.String day) {

    if(StringUtils.getNull(year) == null || StringUtils.getNull(month) == null
       || StringUtils.getNull(day) == null) {
      return "";
    } // end of if
    else if(year.length() != 4 || month.length() > 2 || day.length() > 2) {
      return "";
    } // end of if
    StringBuffer date = new StringBuffer(year);
    date.append("-");
    if(month.length() == 1) {
      date.append("0");
    } // end of if
    date.append(month);
    date.append("-");
    if(day.length() == 1) {
      date.append("0");
    } // end of if
    date.append(day);
    try {
      java.sql.Date.valueOf(date.toString());
    } // end of try
    catch(Exception e) {
      return "";
    } // end of catch
    return date.toString();
  } // end of getDateString

  /**
   * Returns the day of month of the given <code>date</code>.
   * @param date From which day is to be extracted.
   * @return Day (of month) of the of given <code>date</code>.
   */
  public static int getDay(java.util.Date date) {
    if(date == null) {
      return 0;
    } // end of if
    try{
      java.util.Calendar cal = java.util.Calendar.getInstance();
      cal.setTime(date);
      return cal.get(Calendar.DATE);
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return 0;
  } // end of method getDay

  /**
   * Returns the day of week of given <code>date</code>.
   * @param date From which day of week is to be extracted.
   * @return Day of week of the given <code>date</code>.
   */
  public static int getDayOfWeek(java.util.Date date) {
    if(date == null) {
      return 0;
    } // end of if
    try{
      java.util.Calendar cal = java.util.Calendar.getInstance();
      cal.setTime(date);
      return cal.get(Calendar.DAY_OF_WEEK);
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return 0;
  } // end of method getDayOfWeek

  /**
   * Returns the month of given <code>date</code>.
   * @param date From which month is to be extracted.
   * @return Month of given <code>date</code> (0-11).
   */
  public static int getMonth(java.util.Date date) {
    if(date == null) {
      return 0;
    } // end of if
    try{
      java.util.Calendar cal = java.util.Calendar.getInstance();
      cal.setTime(date);
      return cal.get(Calendar.MONTH);
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return 0;
  } // end of method getMonth

  /**
   * Returns the year of given <code>date</code>.
   * @param date From which year part is to be extracted.
   * @return Year of given <code>date<code>.
   */
  public static int getYear(java.util.Date date) {
    if(date == null) {
      return 0;
    } // end of if
    try{
      java.util.Calendar cal = java.util.Calendar.getInstance();
      cal.setTime(new java.util.Date(date.getTime()));
      return cal.get(Calendar.YEAR);
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return 0;
  } // end of method getYear

  /**
   * Returns number of days that has passed since Jan 01, 1970
   * till the given <code>date</code>.
   * @param date From which number of days are to be calculated.
   * @return Number of days since Jan 01, 1970 till the given <code>date</code>.
   */
  public static int getNumberOfDays(java.util.Date date){

    long numberOfDays = 0;
    if (date != null){
      numberOfDays = (((date.getTime())/1000)/3600)/24;
    } // end of if
    return (int)numberOfDays;
  } // end of method getNumberOfDays

  /**
   * Returns name of the <code>day</code> whose number is given.
   * @param day Whose textual representation is needed.
   * @return Textual representation of the given <code>day</code> number
   * (e.g. Sunday for 0).
   */
  public static java.lang.String getDayName(int day) {
    String ret_val = null;
    switch (day) {
      case Calendar.SUNDAY:
      	ret_val = "Sunday";
      	break;
      case Calendar.MONDAY:
        ret_val = "Monday";
        break;
      case Calendar.TUESDAY:
        ret_val = "Tuesday";
        break;
      case Calendar.WEDNESDAY:
      	ret_val = "Wednesday";
      	break;
      case Calendar.THURSDAY:
        ret_val = "Thursday";
        break;
      case Calendar.FRIDAY:
        ret_val = "Friday";
        break;
      case Calendar.SATURDAY:
        ret_val = "Saturday";
        break;
    } // end of switch
    return ret_val;
  } // end of method getDayName

  /**
   * Returns <code>true</code> if the given date ranges overlap.
   * Ranges are defined as <p><ul><li>Range 1 : <code>startDate1<code>
   * to <code>endDate1</code></li><li>Range 2 : <code>startDate2<code>
   * to <code>endDate2</code></li></ul></p>
   * Assumptions:<p><ul>
   * <li><code>startDate1<code> and <code>startDate2<code> can never
   * be <code>null</code>.</li>
   * <li>A <code>null</code> <code>endDate</code> means unlimited in
   * future and will be compared.</li>
   * <li>Two equal dates from different ranges will be taken as
   * overlapping i.e. <code>startDate1.equals(endDate2)</code> or
   * <code>startDate2.equals(endDate1)</code> will be considered as
   * overlapping.</li>
   * <li>If any of start dates is <code>null</code>,
   * <code>NullPointerException</code> is be caught and handled.
   * Calling method is responsible for not passing <code>null</code>
   * start dates.</li></ul></p>
   * @param startDate1 Start Date of range 1
   * @param endDate1 End Date of range 1
   * @param startDate2 Start Date of range 2
   * @param endDate2 End Date of range 2
   * @return <code>true</code> if the given date ranges overlap else
   * <code>false</code>.
   */
  public static boolean doDatesOverlap(Timestamp startDate1, Timestamp endDate1,
      Timestamp startDate2, Timestamp endDate2) {

    if(endDate1 != null && endDate1.before(startDate2)) {
      return false;
    } // end of if
    if(endDate1 == null && endDate2 != null && endDate2.before(startDate1)) {
      return false;
    } // end of if
    if(endDate2 != null && endDate2.before(startDate1)) {
      return false;
    } // end of if
    if(endDate2 == null && endDate1 != null && endDate1.before(startDate2)) {
      return false;
    } // end of if

    return true;
  } // end of method doDatesOverlap

  /**
   * Main method to test methods of this class.
   * @param args Command line arguments.
   * @throws java.lang.Exception In case of any errors
   */
  public static void main(String[] args) throws Exception {
    //DateUtils dateUtils1 = new DateUtils();

/*Start doDatesOverlap checked *
    long millis = System.currentTimeMillis();

    // Dates2 are completely inside Dates1
    Timestamp startDate1 = new Timestamp( - 600000);
    Timestamp endDate1 = new Timestamp(millis + 600000);
    Timestamp startDate2 = new Timestamp(millis - 10000);
    Timestamp endDate2 = new Timestamp(millis + 10000);
    boolean overlap = doDatesOverlap(startDate1, endDate1, startDate2, endDate2);
    System.out.println("Overlap : " + overlap);

    // endDate2 is inside Dates1
    startDate1 = new Timestamp(millis - 600000);
    endDate1 = new Timestamp(millis + 600000);
    startDate2 = new Timestamp(millis - 700000);
    endDate2 = new Timestamp(millis + 10000);
    overlap = doDatesOverlap(startDate1, endDate1, startDate2, endDate2);
    System.out.println("Overlap : " + overlap);

    // startDate2 is inside Dates1
    startDate1 = new Timestamp(millis - 600000);
    endDate1 = new Timestamp(millis + 600000);
    startDate2 = new Timestamp(millis - 10000);
    endDate2 = new Timestamp(millis + 700000);
    overlap = doDatesOverlap(startDate1, endDate1, startDate2, endDate2);
    System.out.println("Overlap : " + overlap);

    // Dates1 are completely inside Dates2
    startDate1 = new Timestamp(millis - 600000);
    endDate1 = new Timestamp(millis + 600000);
    startDate2 = new Timestamp(millis - 700000);
    endDate2 = new Timestamp(millis + 700000);
    overlap = doDatesOverlap(startDate1, endDate1, startDate2, endDate2);
    System.out.println("Overlap : " + overlap);

    // endDate1 is inside Dates2
    startDate1 = new Timestamp(millis - 1200000);
    endDate1 = new Timestamp(millis + 600000);
    startDate2 = new Timestamp(millis - 700000);
    endDate2 = new Timestamp(millis + 700000);
    overlap = doDatesOverlap(startDate1, endDate1, startDate2, endDate2);
    System.out.println("Overlap : " + overlap);

    // startDate1 is inside Dates2
    startDate1 = new Timestamp(millis - 600000);
    endDate1 = new Timestamp(millis + 1200000);
    startDate2 = new Timestamp(millis - 700000);
    endDate2 = new Timestamp(millis + 700000);
    overlap = doDatesOverlap(startDate1, endDate1, startDate2, endDate2);
    System.out.println("Overlap : " + overlap);

    // startDate2 is equal to endDate 1
    startDate1 = new Timestamp(millis - 1200000);
    endDate1 = new Timestamp(millis);
    startDate2 = new Timestamp(millis);
    endDate2 = new Timestamp(millis + 1200000);
    overlap = doDatesOverlap(startDate1, endDate1, startDate2, endDate2);
    System.out.println("Overlap : " + overlap);

    // endDate1 and endDate2 both are null
    startDate1 = new Timestamp(millis - 1200000);
    endDate1 = null;
    startDate2 = new Timestamp(millis);
    endDate2 = null;
    overlap = doDatesOverlap(startDate1, endDate1, startDate2, endDate2);
    System.out.println("Overlap : " + overlap);

    // endDate1 is null and startDate 2 is greater than startDate1
    startDate1 = new Timestamp(millis - 1200000);
    endDate1 = null;
    startDate2 = new Timestamp(millis + 100000);
    endDate2 = new Timestamp(millis + 1200000);
    overlap = doDatesOverlap(startDate1, endDate1, startDate2, endDate2);
    System.out.println("Overlap : " + overlap);

    // mutually exclusive date ranges
    startDate1 = new Timestamp(millis - 1200000);
    endDate1 = new Timestamp(millis);
    startDate2 = new Timestamp(millis + 100000);
    endDate2 = new Timestamp(millis + 1200000);
    overlap = doDatesOverlap(startDate1, endDate1, startDate2, endDate2);
    System.out.println("Overlap : " + overlap);
/*End*/
/*Start getDateString checked *
    System.out.println("Date is : " + getDateString("2004", "", "23"));
    System.out.println("Date is : " + getDateString("2004", "12", null));
    System.out.println("Date is : " + getDateString("2004", "12", "23"));
    System.out.println("Date is : " + getDateString("2004", "2", "2"));
    System.out.println("Date is : " + getDateString("2004", "2", "31"));
/*End*/
/*Start getNumberOfDays checked *
    System.out.println(DateUtils.getNumberOfDays(DateUtils.getStringToDate("08-09-2003 2:30 PM",DateUtils.MM_dd_yyyy__h_mm__a)));
    System.out.println(DateUtils.getNumberOfDays(new Date()));
    System.out.println(DateUtils.getNumberOfDays(new Date()) - DateUtils.getNumberOfDays(DateUtils.getStringToDate("08-09-2003 2:30 PM",DateUtils.MM_dd_yyyy__h_mm__a)));
/*End*/

/*Start getStringToDate checked *
    System.out.println(DateUtils.getStringToDate("11-15-2002 7:0 AM",DateUtils.MM_dd_yyyy__h_mm__a));
/*End*/

/*Start misc *
    java.sql.Timestamp ts = java.sql.Timestamp.valueOf("2000-0-1 14:0:0.0");
    System.out.println(ts);
/*End*/

/*Start getLongFormattedDateString checked *
    System.out.println(DateUtils.getLongFormattedDateString(Calendar.getInstance().getTime()));
/*End*/

/*Start getMediumFormattedDateToString checked *
    DateUtils.getMediumFormattedDateToString("Fri Sep 20 09:20:00 PKT 2002");
    System.out.println(DateUtils.getMediumFormattedStringToDate(DateUtils.getMediumFormattedDateToString(Calendar.getInstance().getTime())));
/*End*/

/*Start getShortFormattedDateString checked *
    System.out.println(DateUtils.getShortFormattedDateString("Tue Sep 24 11:20:00 PKT 2002"));
/*End*/

/*Start getShortFormattedDateString checked *
    System.out.println(DateUtils.getShortFormattedDateString("Tue Sep 24 11:20:00 PKT 2002"));
/*End*/

/*Start getHour checked *
    System.out.println(DateUtils.getHour(new java.sql.Timestamp(System.currentTimeMillis())));
/*End*/

/*Start getMinute checked *
     System.out.println(DateUtils.getMinute(new java.sql.Timestamp(System.currentTimeMillis())));
/*End*/

/*Start getDateDifferenceInDays checked *
     System.out.println(DateUtils.getDateDifferenceInDays(DateUtils.getDate("12-17-2002 9:00","MM-dd-yyyy HH:mm"),DateUtils.getDate("12-18-2002 0:00","MM-dd-yyyy HH:mm")));
/*End*/

/*Start getDateDifference checked *
      System.out.println(DateUtils.getDateDifference(DateUtils.getDate("06-16-1951","MM-dd-yyyy"), DateUtils.getDate("05-20-2003","MM-dd-yyyy")));
/*End*/

  } // end of method main

} // end of class DateUtils

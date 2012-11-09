package com.signaturemobile.signaturemobile.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;


/**
 * Tools provide utility methods
 *  
 * @author <a href="mailto:moisesvs@gmail.com">Mooisés Vázquez Sánchez</a>
 */
public class Tools {
    
    private static final String TAG = "Tools";
    
    /**
     * Hexadecimal characters
     */
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();


    private static final String DATE_IN_FORMAT = "yyyyMMdd";

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    
    private static final String MONTH_FORMAT = "MM";
    
    private static final String YEAR_FORMAT = "yyyy";
    
    private static final String MONTH_FILTER_FORMAT = "MMMM yyyy";
    
    private static final Calendar CALENDAR = Calendar.getInstance();

    private static SimpleDateFormat dateInFormatter = null;
    private static SimpleDateFormat dateFormatter = null;
    private static SimpleDateFormat monthFormatter = null;
    private static SimpleDateFormat yearFormatter = null;
    private static SimpleDateFormat monthFilterFormatter = null;
    
    /**
     * Month names
     */
    private static Vector<String> MONTHS;

    /**
     * Calculates the distance between 2 geographic coordinates
     * 
     * @param latitude1 first coordinate latitude
     * @param longitude1 first coordinate longitude
     * @param latitude2 second coordinate latitude
     * @param longitude2 second coordinate latitude
     * @return The distance between the provided geographic coordinates
     */
    public static double calculateDistance(double latitude1, double longitude1,
            double latitude2, double longitude2) {
        float[] results = new float[3];
        Location.distanceBetween(latitude1, longitude1, latitude2, longitude2, results);
        return results[0];
    }

    /**
     * Calculates the euclidean distance, in pixels between 2 graphical ponts
     * 
     * @param point1 first point
     * @param point2 second point
     */
    public static int euclideanDistance(Point point1, Point point2) {
        int x1, y1, x2, y2;
        
        x1 = point1.x;
        y1 = point1.y;
        
        x2 = point2.x;
        y2 = point2.y;
        
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    /**
     * Converts username into a formatted username
     * @param aUsername
     * @return the formatted user name
     */
    public static String formatUsername(String aUsername) {
        String result = null;
        if (aUsername != null) {
            String username = aUsername.toUpperCase();
            int usernameLength = aUsername.length();
            int usernameLast = usernameLength - 1;
            char nifCharacter = username.charAt(usernameLast);
            if ((!Character.isDigit(nifCharacter)) && (Character.isUpperCase(nifCharacter))) {
                if (usernameLength <= 10) {
                    String nif = username.substring(0, usernameLast);
                    int nifValue;
                    try {
                        nifValue = Integer.parseInt(nif);
                        int nifIndex = nifValue % 23;
                        if ((nifIndex >= 0) && (nifIndex < 23)) {
                            String validNIFCharacters = "TRWAGMYFPDXBNJZSQVHLCKE";
                            char validNIFCharacter = validNIFCharacters.charAt(nifIndex);
                            if (validNIFCharacter == nifCharacter) {
                                StringBuffer sb = new StringBuffer("0019-");
                                int zeros = 10 - usernameLength;
                                for (int i = 0; i < zeros; i++) {
                                    sb.append("0");
                                }
                                sb.append(username);
                                result = sb.toString();
                            }
                        }
                    } catch (NumberFormatException e) {
//                        Tools.logThrowable(TAG, e);
                    }
                }
            } else if (usernameLength == 9) {
                try {
                    Integer.parseInt(username);
                    StringBuffer sb = new StringBuffer("404134");
                    sb.append(username);
                    String card = sb.toString();
                    int digits = card.length();
                    int even = 0;
                    int odd = 0;
                    int code = 0;
                    int evenTotal = 0;
                    int oddTotal = 0;
                    int oddSingleTotal = 0;
                    int value;
                    for (int i = 0; i < digits; i++) {
                        value = Integer.parseInt(card.substring(i, i+1));
                        if ((i % 2) != 0) {
                            even = value;
                            evenTotal += even;
                        } else {
                            odd = value * 2;
                            if (odd > 9) {
                                oddTotal += (odd / 10) + (odd % 10);
                            } else {
                                oddSingleTotal += odd;
                            }
                        }
                    }
                    code = ((evenTotal + oddTotal + oddSingleTotal) % 10);
                    if (code > 0) {
                        code = 10 - code;
                    }
                    result = sb.append(code).toString();
                } catch (NumberFormatException e) {
//                    Tools.logThrowable(TAG, e);
                }
            } else if ((usernameLength == 16) || (usernameLength == 7)) {
                result = username;
            }
        }
        return result;
    }

    /**
     * Appends two strings
     * @param text1 first string
     * @param text2 second string
     * @return the result
     */
    public static String append(String text1, String text2) {
        return new StringBuffer((text1 == null) ? "" : text1).append((text2 == null) ? "" : text2).toString();
    }
   
    /**
     * Convert a date into a string format as yyyyMMdd
     * @param date the date object to convert
     * @return a formatted string with the date information
     */
    public static String date2Str(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);       
        StringBuffer result = new StringBuffer();
        appendNumber(result, year, 4);
        appendNumber(result, month, 2);
        appendNumber(result, day, 2);             
        return result.toString();
    }
    
    /**
     * Appends digits with padding for long integer value to buffer.
     * @param result Buffer to contain result.
     * @param num Value to convert.
     * @param minWidth Pads digits with leading zeros to meet this width.
     */
    public static void appendNumber(StringBuffer result, int num, int minWidth) {
        result.append(Integer.toString(num));
        while(result.length()<minWidth) {
            result.insert(0, "0");
        }
    }
    
    /**
     * Convert a date into a medium format
     * @param date the date object to convert
     * @return a formatted string with the date information
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
//        BB
//        SimpleDateFormat formatter = new SimpleDateFormat(SimpleDateFormat.DATE_MEDIUM);
//        return formatter.formatLocal(date.getTime());
        
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return dateFormat.format(date);
    }
    
    /**
     * Convert a date into a medium format
     * @param date the date object to convert
     * @return a formatted string with the date information
     */
    public static boolean isInitDate(Date date) {
    	boolean result = false;
    	Date initDate = new Date(0);
    	if ((date != null) && (date.compareTo(initDate) == 0)) {
    		result = true;
        }
        return result;
    }
    
    /**
     * Convert a date into a medium format
     * @param date the date object to convert
     * @return a formatted string with the date information
     */
    public static String formatWeekDay(Date date) {
        if (date == null) {
            return null;
        }
//        BB
//        SimpleDateFormat formatter = new SimpleDateFormat(SimpleDateFormat.DATE_FULL);
//        String full = formatter.formatLocal(date.getTime());
        
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        String full = dateFormat.format(date);
        
        String[] tokens = splitBy(full, ',', true);
        return tokens[0];
    }
    
    /**
     * Convert a date into a user friendly, displayable string
     * @param date the date object to show
     * @param showDate true if the date information must be shown, false if
     * only the time information
     * @return a string with the date converted, expressed as:
     * hh:mm if showDate is false
     * dd/MM hh:mm if showDate is true
     */
    public static String time2DisplayStr(Date date, boolean showDate) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);       
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);       
        StringBuffer result = new StringBuffer();
        if (showDate) {
            appendNumber(result, day, 2);
            result.append("/");
            appendNumber(result, month, 2);
            result.append(" ");
        }                        
        appendNumber(result, hour, 2);
        result.append(":");
        appendNumber(result, minute, 2);             
        return result.toString();
    }

//    /**
//     * Truncate a text to fit the target width
//     * @param font the used font
//     * @param text the text
//     * @param width the target width
//     * @return the set of fragmented lines of text
//     */
//    public static String[] truncateToFit(Font font, String text, int width) {
//        if (font.getAdvance(text) < width) {
//            return new String[] {text};
//        } else {
//            Vector lines = new Vector();
//            String[] fragments = splitBySpaces(text);
//            String current;
//            String previous = "";
//            StringBuffer sb = new StringBuffer();
//            int last = fragments.length - 1;
//            String fragment;
//            for (int i = 0; i <= last; i++) {
//                fragment = fragments[i];
//                sb.append(fragment);
//                current = sb.toString();
//                if (font.getAdvance(current) > width) {
//                    if (!previous.trim().equals("")) {
//                        lines.addElement(previous);
//                    }
//                    sb.setLength(0);
//                    sb.append(fragment);
//                    previous = fragment;
//                } else {
//                    previous = current;
//                }
//                if (i == last) {
//                    if (!previous.trim().equals("")) {
//                        lines.addElement(previous);
//                    }
//                }
//            }
//            String[] result = new String[lines.size()];
//            lines.copyInto(result);
//            return result;
//        }
//    }

    /**
     * Split a text by spaces
     * @param text the text
     * @return the list of words
     */
    public static String[] splitBySpaces(String text) {
        return splitBy(text, ' ', false);
    }
    
    /**
     * Split a text by spaces
     * @param text the text
     * @param separator 
     * @param remove 
     * @return the list of words
     */
    public static String[] splitBy(String text, char separator, boolean remove) {
        Vector<String> fragments = new Vector<String>();
        int size = text.length();
        int pos = 0;
        int current = 0;
        boolean done = false;
        for (int i = 0; (i < size) && (!done);) {
            pos = text.indexOf(separator, current);
            if (pos >= 0) {
                fragments.addElement(text.substring(current, remove ? pos : pos + 1));
                current = pos + 1;
            } else {
                done = true;
                if (current < (size - 1)) {
                    fragments.addElement(text.substring(current));
                }
            }
        }
        String[] result = new String[fragments.size()];
        fragments.copyInto(result);
        return result;
    }

    /**
     * Remove HTML markup from text
     * @param text
     * @return the text
     */
    public static String removeHTML(String text) {
        StringBuffer sb = new StringBuffer();
        if (text != null) {
            int size = text.length();
            int pos = 0;
            int last = -1;
            boolean finished = false;
            do {
                pos = text.indexOf('<', pos);
                if (pos > last) {
                    sb.append(text.substring(last + 1, pos));
                    last = pos;
                    pos = text.indexOf('>', pos);
                    if (pos > last) {
                        last = pos;
                    } else {
                        finished = true;
                    }
                } else {
                    if (last < (size - 1)) {
                        sb.append(text.substring(last + 1));
                    }
                    finished = true;
                }
            } while (!finished);
        }
        String result = sb.toString();
        result = removeEscapedHTML(result);
        result = replaceText(result, "&quot;", "\"");
        result = replaceText(result, "&nbsp;", " ");
        result = replaceText(result, "&euro;", "�");
        return result;
    }

    /**
     * Convert a date into a medium format
     * @param date the date object to convert
     * @return a formatted string with the date information
     */
    public static String getStringFromDate(Date date) {
        if (date == null) {
            return null;
        }
        String result = null;
        synchronized (DATE_FORMAT) {
            if (dateFormatter == null) {
                dateFormatter = new SimpleDateFormat(DATE_FORMAT);
            }
            //BB
            //result = dateFormatter.formatLocal(date.getTime());
            result = dateFormatter.format(date.getTime());
        }
        return result;
    }
    
    /**
     * Remove HTML markup from text
     * @param text
     * @return the text
     */
    private static String removeEscapedHTML(String text) {
        StringBuffer sb = new StringBuffer();
        if (text != null) {
            int size = text.length();
            int pos = 0;
            int last = -1;
            boolean finished = false;
            do {
                pos = text.indexOf("&lt;", pos);
                if (pos > last) {
                    sb.append(text.substring(last + 1, pos));
                    last = pos;
                    pos = text.indexOf("&gt;", pos);
                    if (pos > last) {
                        last = pos + 3;
                    } else {
                        finished = true;
                    }
                } else {
                    if (last < (size - 1)) {
                        sb.append(text.substring(last + 1));
                    }
                    finished = true;
                }
            } while (!finished);
        }
        String result = sb.toString();
        return result;
    }

    /**
     * Replace text
     * @param source 
     * @param text
     * @param replaced 
     * @return the text
     */
    public static String replaceText(String source, String text, String replaced) {
        String result = source;
        if ((source != null) && (text != null)) {
            int pos = source.indexOf(text);
            if (pos >= 0) {
                int textSize = text.length();
                StringBuffer sb = new StringBuffer();
                int last = -1;
                boolean finished = false;
                do {
                    if (pos <= last) {
                        finished = true;
                        sb.append(source.substring(last + 1));
                    } else {
                        sb.append(source.substring(last + 1, pos));
                        if (replaced != null) {
                            sb.append(replaced);
                        }
                        last = pos + textSize - 1;
                        pos = source.indexOf(text, last);
                    }
                } while (!finished);
                result = sb.toString();
            }
        }
        return result;
    }

    /**
     * Parse an RSS date
     * @param date
     * @return the parsed date
     */
    public static Date parseDate(String date) {
        Date result = null;
        synchronized(CALENDAR) {
            if (date != null) {
                String text = date.trim();
                try {
                    if ((text.length() >= 8) && (text.length() <= 10)) {
                        String[] tokens = Tools.splitBy(text, '-', true);
                        if (tokens != null) {
                            int size = tokens.length;
                            if (size == 3) {
                                CALENDAR.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tokens[0]));
                                CALENDAR.set(Calendar.MONTH, Integer.parseInt(tokens[1]) - 1);
                                int year = Integer.parseInt(tokens[2]);
                                if (year < 2000) {
                                    year += 2000;
                                }
                                CALENDAR.set(Calendar.YEAR, year);
                                result = CALENDAR.getTime();
                            }
                        }
                    } else {
                        if (MONTHS == null) {
                            MONTHS = new Vector<String>();
                            MONTHS.addElement("Jan");
                            MONTHS.addElement("Feb");
                            MONTHS.addElement("Mar");
                            MONTHS.addElement("Apr");
                            MONTHS.addElement("May");
                            MONTHS.addElement("Jun");
                            MONTHS.addElement("Jul");
                            MONTHS.addElement("Aug");
                            MONTHS.addElement("Sep");
                            MONTHS.addElement("Oct");
                            MONTHS.addElement("Nov");
                            MONTHS.addElement("Dec");
                        }
                        boolean large = (text.indexOf(',') > 0);
                        String[] tokens = Tools.splitBy(text, ' ', true);
                        if (tokens != null) {
                            int size = tokens.length;
                            if (large) {
                                if (size > 3) {
                                    CALENDAR.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tokens[1]));
                                    try {
                                        int month = Integer.parseInt(tokens[2]);
                                        CALENDAR.set(Calendar.MONTH, month - 1);
                                    } catch (Exception e) {
                                        CALENDAR.set(Calendar.MONTH, MONTHS.indexOf(tokens[2]));
                                    }
                                    int year = Integer.parseInt(tokens[3]);
                                    if (year < 2000) {
                                        year += 2000;
                                    }
                                    CALENDAR.set(Calendar.YEAR, year);
                                    if (size > 4) {
                                        tokens = Tools.splitBy(tokens[4], ':', true);
                                        CALENDAR.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tokens[0]));
                                        CALENDAR.set(Calendar.MINUTE, Integer.parseInt(tokens[1]));
                                        CALENDAR.set(Calendar.SECOND, Integer.parseInt(tokens[2]));
                                    } else {
                                        CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
                                        CALENDAR.set(Calendar.MINUTE, 0);
                                        CALENDAR.set(Calendar.SECOND, 0);
                                    }
                                    result = CALENDAR.getTime();
                                }
                            } else {
                                if (size > 2) {
                                    CALENDAR.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tokens[0]));
                                    try {
                                        int month = Integer.parseInt(tokens[1]);
                                        CALENDAR.set(Calendar.MONTH, month - 1);
                                    } catch (Exception e) {
                                        CALENDAR.set(Calendar.MONTH, MONTHS.indexOf(tokens[1]));
                                    }
                                    int year = Integer.parseInt(tokens[2]);
                                    if (year < 2000) {
                                        year += 2000;
                                    }
                                    CALENDAR.set(Calendar.YEAR, year);
                                    if (size > 3) {
                                        tokens = Tools.splitBy(tokens[3], ':', true);
                                        CALENDAR.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tokens[0]));
                                        CALENDAR.set(Calendar.MINUTE, Integer.parseInt(tokens[1]));
                                        CALENDAR.set(Calendar.SECOND, Integer.parseInt(tokens[2]));
                                    } else {
                                        CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
                                        CALENDAR.set(Calendar.MINUTE, 0);
                                        CALENDAR.set(Calendar.SECOND, 0);
                                    }
                                    result = CALENDAR.getTime();
                                }
                            }
                        }
                    }
                } catch (NumberFormatException e) {
//                    Tools.logThrowable(TAG, e);
                }
            }
        }
        return result;
    }

    /**
     * Compares two dates
     * @param date1 
     * @param date2 
     * @return the true if same day
     */
    public static boolean isSameDay(Date date1, Date date2) {
        boolean result = false;
        synchronized(CALENDAR) {
            if ((date1 != null) && (date1 != null)) {
                CALENDAR.setTime(date1);
                int year = CALENDAR.get(Calendar.YEAR); 
                int month = CALENDAR.get(Calendar.MONTH); 
                int day = CALENDAR.get(Calendar.DAY_OF_MONTH);
                CALENDAR.setTime(date2);
                result = (year == CALENDAR.get(Calendar.YEAR)) && (month == CALENDAR.get(Calendar.MONTH)) && (day == CALENDAR.get(Calendar.DAY_OF_MONTH));
            }
        }
        return result;
    }

    /**
     * Concat two strings
     * @param text1 first string
     * @param text2 second string
     * @return the result
     */
    public static String concat(String text1, String text2) {
        return new StringBuffer((text1 == null) ? "" : text1).append((text2 == null) ? "" : text2).toString();
    }

    /**
     * Convert a date into a month format
     * @param date the date object to convert
     * @return a formatted string with the date information
     */
    public static String getMonthFromDate(Date date) {
        if (date == null) {
            return null;
        }
        String result = null;
        synchronized (MONTH_FORMAT) {
            if (monthFormatter == null) {
                monthFormatter = new SimpleDateFormat(MONTH_FORMAT);
            }
            result = monthFormatter.format(date.getTime());
        }
        return result;
    }

    /**
     * Convert a date into a year format
     * @param date the date object to convert
     * @return a formatted string with the date information
     */
    public static String getYearFromDate(Date date) {
        if (date == null) {
            return null;
        }
        String result = null;
        synchronized (YEAR_FORMAT) {
            if (yearFormatter == null) {
                yearFormatter = new SimpleDateFormat(YEAR_FORMAT);
            }
            result = yearFormatter.format(date.getTime());
        }
        return result;
    }

    /**
     * Convert a date into a year format
     * @param date the date object to convert
     * @return a formatted string with the date information
     */
    public static String getMonthFilterFromDate(Date date) {
        if (date == null) {
            return null;
        }
        String result = null;
        synchronized (MONTH_FILTER_FORMAT) {
            if (monthFilterFormatter == null) {
                monthFilterFormatter = new SimpleDateFormat(MONTH_FILTER_FORMAT);
            }
            result = monthFilterFormatter.format(date.getTime());
        }
        return result;
    }

    /**
     * Get a date with time 0
     * @param date the source date
     * @return the converted date
     */
    public static Date getDateWidthTimeToZero(Date date) {
        Date result = null;
        synchronized(CALENDAR) {
            if (date != null) {
                CALENDAR.setTime(date);
                CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
                CALENDAR.set(Calendar.MINUTE, 0);
                CALENDAR.set(Calendar.SECOND, 0);
                CALENDAR.set(Calendar.MILLISECOND, 0);
                result = CALENDAR.getTime();
            }
        }
        return result;
    }
    
    /**
     * Get a date with time 0
     * @param date the source date
     * @return the converted date
     */
    public static long getDateMillisWithTimeToZero(Date date) {
        Date result = null;
        synchronized(CALENDAR) {
            if (date != null) {
                CALENDAR.setTime(date);
                CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
                CALENDAR.set(Calendar.MINUTE, 0);
                CALENDAR.set(Calendar.SECOND, 0);
                CALENDAR.set(Calendar.MILLISECOND, 0);
                result = CALENDAR.getTime();
            }
        }
        return result.getTime();
    }

    /**
     * Convert a date into a server format
     * @param date the date object to convert
     * @return a formatted string with the date information
     */
    public static String getServerStringFromDate(Date date) {
        if (date == null) {
            return null;
        }
        String result = null;
        synchronized (DATE_IN_FORMAT) {
            if (dateInFormatter == null) {
                dateInFormatter = new SimpleDateFormat(DATE_IN_FORMAT);
            }
            result = dateInFormatter.format(date.getTime());
        }
        return result;
    }

    /**
     * Get first day of month date
     * @param date the source date
     * @return the converted date
     */
    public static Date getFirstDayOfMonthDate(Date date) {
        Date result = null;
        synchronized(CALENDAR) {
            if (date != null) {
                CALENDAR.setTime(date);
                CALENDAR.set(Calendar.DAY_OF_MONTH, 1);
                CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
                CALENDAR.set(Calendar.MINUTE, 0);
                CALENDAR.set(Calendar.SECOND, 0);
                CALENDAR.set(Calendar.MILLISECOND, 0);
                result = CALENDAR.getTime();
            }
        }
        return result;
    }

    /**
     * Get last day of month date
     * @param date the source date
     * @return the converted date
     */
    public static Date getLastDayOfMonthDate(Date date) {
        Date result = null;
        synchronized(CALENDAR) {
            if (date != null) {
                CALENDAR.setTime(date);
                int year = CALENDAR.get(Calendar.YEAR);
                int month = CALENDAR.get(Calendar.MONTH);
//                BB
//                int last = DateTimeUtilities.getNumberOfDaysInMonth(month, year);
                
                
                int day = CALENDAR.get(Calendar.DAY_OF_MONTH);
                // create a calendar object of the desired month                
                Calendar cal = new GregorianCalendar(year, month, day);
                // get the number of days in that month
                int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

                
                
                CALENDAR.set(Calendar.DAY_OF_MONTH, last);
                CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
                CALENDAR.set(Calendar.MINUTE, 0);
                CALENDAR.set(Calendar.SECOND, 0);
                CALENDAR.set(Calendar.MILLISECOND, 0);
                result = CALENDAR.getTime();
            }
        }
        return result;
    }
    


    /**
     * Formats a phone number to the expected format in server
     * @param mobile the mobile number
     * @return the expected format in server
     */
    public static String formatPhoneNumber(String mobile) {
        String result;
        if (mobile != null) {
            int size = mobile.length();
            StringBuffer sb = new StringBuffer();
            char character;
            for (int i = 0; i < size; i++) {
                character = mobile.charAt(i);
                switch (character) {
                case ' ':
                    break;
                case '(':
                    break;
                case ')':
                    break;
                case '-':
                    break;
                case '+':
                    sb.append(00);
                    break;
                default:
                    sb.append(character);
                    break;
                }
            }
            result = sb.toString();
        } else {
            result = "";
        }
        return result;
    }
    
    
    /**
     * Formats a phone number to the expected format in server for ATMs
     * @param mobile the mobile number
     * @return the expected format in server
     */
    public static String formatPhoneNumberValidForATM(String mobile) {
        String result = formatPhoneNumber(mobile);
        if (result != null) {
            int size = result.length();
            if (size > 9) {
                result = result.substring(size - 9);
            }
        } else {
            result = "";
        }
        return result;
    }
    
    /**
     * Check control digits for an account of a bank and office
     * @param account the account
     * @return true if valid
     */
    public static boolean checkAccount(String account) {
        return ((account != null) && (account.length() == 23)) ? 
                checkAccount(
                        account.substring(13, 23), 
                        account.substring(0, 4), 
                        account.substring(5, 9), 
                        account.substring(10, 12) 
                        ) : false;
    }
    
    /**
     * Check control digits for an account of a bank and office
     * @param anAccountNumber
     * @param aBankNumber
     * @param aOfficeNumber
     * @param aControlNumber
     * @return true if valid
     */
    public static boolean checkAccount(String anAccountNumber, String aBankNumber, String aOfficeNumber, String aControlNumber) {
        
        boolean result = false;
        
        if ((aBankNumber != null) && (aOfficeNumber != null) && (anAccountNumber != null) && (aControlNumber != null)) {
            
            
            int[] bankWeights = new int[]{4, 8, 5, 10};
            int[] officeWeights = new int[]{9, 7, 3, 6};
            int[] accountWeights = new int[]{1, 2, 4, 8, 5, 10, 9, 7, 3, 6};

            int[] bankDigits = new int[4];
            int[] officeDigits = new int[4];
            int[] accountDigits = new int[10];
            int[] controlDigits = new int[2];
            
            Tools.fillFromString(aBankNumber, bankDigits, 4);
            Tools.fillFromString(aOfficeNumber, officeDigits, 4);
            Tools.fillFromString(anAccountNumber, accountDigits, 10);
            Tools.fillFromString(aControlNumber, controlDigits, 2);

            int bankWeight = Tools.calculateWeight(bankDigits, bankWeights);
            int officeWeight = Tools.calculateWeight(officeDigits, officeWeights);
            int accountWeight = Tools.calculateWeight(accountDigits, accountWeights);

            int calculatedControlDigit0 = Tools.calculateControlDigit(bankWeight + officeWeight);
            int calculatedControlDigit1 = Tools.calculateControlDigit(accountWeight);
            
            int controlDigit0 = controlDigits[0];
            int controlDigit1 = controlDigits[1];

            result = ((controlDigit0 == calculatedControlDigit0) && (controlDigit1 == calculatedControlDigit1));
            
        }
        return result;
        
    }
    
    /*
     * Fill the target array with integers from source string
     */
    private static void fillFromString(String aString, int[] anArray, int aSize) {
        
        int delta = aSize - ((aString != null) ? aString.length() : 0);
        int pos;
        String digit;
        for (int i = 0; i < aSize; i++) {
            if (i < delta) {
                anArray[i] = 0;
            } else {
                pos = i - delta;
                digit = aString.substring(pos, pos + 1);
                try {
                    anArray[i] = Integer.parseInt(digit);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

    /*
     * Fill the target array with integers from source string
     */
    private static int calculateWeight(int[] aDigitsArray, int[] aWeightsArray) {
        
        int result = 0;
        int digit;
        int weight;
        int size = aDigitsArray.length;
        for (int i = 0; i < size; i++) {
            digit = aDigitsArray[i];
            weight = aWeightsArray[i];
            result += digit * weight;
        }
        return result;
        
    }

    /*
     * Calculate the control digit
     */
    private static int calculateControlDigit(int aWeight) {
        
        int mod = (aWeight % 11);
        int result = ((mod == 0) ? 11 : ((mod == 1) ? 10 : mod)); 
        return 11 - result;
        
    }
    
    /**
     * Method that validates an email address (is a simple validation)
     * @param address The email address to validate
     * @return true if the mail address is valid; otherwhise false
     */
    public static boolean validateEmailAddress(String address) {
        boolean result = true;
        if ( (address == null) || (address.length() == 0) || (address.trim().equals("")) ) {
            result = false;
        } else {
            // doesn't contains @
//            boolean correct = true;
            if (!address.contains("@")) {
                result = false;
            }                   
            
            // contains more than one @
            int pos = address.indexOf("@");
            if (address.indexOf("@",pos+1) != -1) {
                result = false;
            }
            
            // contains spaces
            if (address.contains(" ")) {
                result = false;
            }
        }
        
        return result;
    }

    /**
     * Create date only date today
     */
    public static Date createDateOnlyToday (){
    	Date dateToday = new Date();
    	dateToday.setHours(0);
    	dateToday.setMinutes(0);
    	dateToday.setSeconds(0);

    	return dateToday;
    }
    
    /**
     * Gets the IMEI
     * @return the IMEI
     */
    private static String getIMEI(Context context) {

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = manager.getDeviceId();

        if (imei == null){
            imei =  Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        }
        
        return imei;
    }
    
    /**
     * Gets the MD5 diggest from data
     * @param data the input data
     * @return the diggest
     */
    public static byte[] getMD5(byte[] data) {
        byte[] md5 = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");  
            digest.update(data);  
            md5 = digest.digest();  
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }
    
    /**
     * Gets the MD5 diggest from data
     * @param data the input data
     * @return the diggest
     */
    public static String getMD5String(String data) {
        String result = null;
        
        byte[] md5 = getMD5(data.getBytes());
        if (md5 != null) {
            StringBuffer sb = new StringBuffer();
            appendHexString(md5, 0, 16, sb);
            result = sb.toString();
        }
        
        return result;
    }
    
    /**
     * Gets the device ID
     * @return the device ID
     */
    public static String getDeviceID(Context context) {
        return getMD5String(getIMEI(context));
    }

    /**
     * Appends the data as hex string to the buffer
     * @param data the input data
     * @param from the start position
     * @param size the size
     * @param sb the buffer
     */
    public static void appendHexString(byte[] data, int from, int size, StringBuffer sb) {
        if (data != null) {
            int max = data.length;
            int to = Math.min(from + size, max);
            int octet;
            int index;
            for (int i = from; i < to; i++) {
                octet = data[i];
                index = (octet & 0xF0) >>> 4;
                sb.append(HEX_CHARS[index]);
                index = octet & 0x0F;
                sb.append(HEX_CHARS[index]);
            }
        }
    }

    /**
     * Capitalize the text
     * @param text the original text
     * @return the capitalized text
     */
    public static String capitalize(String text) {
        String result = null;
        if (text != null) {
            if (text.length() > 0) {
                if (text.length() > 1) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(text.substring(0, 1).toUpperCase());
                    sb.append(text.substring(1).toLowerCase());
                    result = sb.toString();
                } else {
                    result = text;
                }
            } else {
                result = text;
            }
        }
        return result;
    }

    /**
     * Replace all occurrences of a string pattern
     * @param source the original string
     * @param pattern the target pattern
     * @param replacement the replacement
     * @return the modified string
     */
    public static String replaceAll(String source, String pattern, String replacement) {    

        if (source == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int idx = -1;

        String workingSource = source;
        
        while ((idx = workingSource.indexOf(pattern)) != -1) {

            sb.append(workingSource.substring(0, idx));
            sb.append(replacement);
            sb.append(workingSource.substring(idx + pattern.length()));
            workingSource = sb.toString();
            
            sb.delete(0, sb.length());

        }

        return workingSource;
    
    }

    /**
     * Returns true if array contains object. False otherwise
     * @param array
     * @param object
     * @return true if array contains object. False otherwise
     */
    public static boolean contains(Object[] array, Object object) {
        boolean result = false;
        
        if ((array != null) && (object != null)) {
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(object)) {
                    result = true;
                    break;
                }
            }
        }
        
        return result;
    }

    /**
     * Transfor pixel size value to density pixels
     * @param ps pixel size
     * @return density pixels
     */
    public static int densityPixels(int ps) {
        DisplayMetrics metrics = new DisplayMetrics();
        SignatureMobileApplication.getInstance().getToolBox().getApplicationDisplay().getMetrics(metrics);
        
        return (int) (ps * metrics.density);
    }
    
    /**
     * Remove undesired characters from phone number and returns result
     * 
     * @param phoneNumber given phone number
     * @return excaped number
     */
    public static String escapePhoneNumber(String phoneNumber) {
        String result = phoneNumber;
        result.replace(" ", "");
        result.replace("(", "");
        result.replace(")", "");
        result.replace("-", "");
        return result;
    }
    
    /**
     * Reads a byte array from given input stram
     * 
     * @param is Input stream to read from
     * @return readed byte array
     */
    public static byte[] readBytesFromStream(InputStream is) {
        byte[] result;
        byte[] buff = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int readed;
        
        try {
            while ((readed = is.read(buff)) > -1) {
                bos.write(buff, 0, readed);
            }
            result = bos.toByteArray();
        } catch (IOException e) {
            result = null;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                	// Nothing
                }
            }
        }
    
        return result;
    }

    /**
     * Reads a serializable object from given file
     * 
     * @param file file to read from
     * @return readed serializable object
     */
    public static Serializable readFromFile(File file) {
        Serializable result = null;
        
        if (file.exists()) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            
            try {
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                result = (Serializable) ois.readObject();
            } catch (Exception ignored) {
                System.out.println(ignored);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Exception e) {
                    	// Nothing
                    }
                }
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (Exception e) {
                    	// Nothing
                    }
                }
            }
        }
    
        return result;
    }

    /**
     * Writes a Serializable object to given file
     * 
     * @param object Serializable object
     * @param file file to write
     * @throws IOException 
     */
    public static void writeToFile(Serializable object, File file) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (Exception e) {
                	// Nothing
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                	// Nothing
                }
            }
        }
    }

    /**
     * Method that scales the received srcBitmap so it fits both desiredWidth and desiredHeight
     * maintaining aspect ratio
     * @param srcBitmap The source bitmap to be scaled
     * @param desiredWidth The desired destination scaled bitmap width; the resulting width may be smaller due to aspect ratio
     * @param desiredHeight The desired destination scaled bitmap height; the resulting height may be smaller due to aspect ratio
     * @return
     */ 
    public static Bitmap scaleBitmap(Bitmap srcBitmap, int desiredWidth, int desiredHeight) {

        // first calculate the scale factor based on image width
        float factor = srcBitmap.getWidth() / desiredWidth;
        int destinationWidth = desiredWidth;
        int destinationHeight = (int)(srcBitmap.getHeight() / factor);
    
        // if resulting image height is bigger than desired image height, calculate the
        // scale factor based on image height
        if (destinationHeight > desiredHeight) {
            factor = srcBitmap.getHeight() / desiredHeight;
            destinationHeight = desiredHeight;
            destinationWidth = (int)(srcBitmap.getWidth() / factor);
        }
    
        // scale
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(srcBitmap, destinationWidth, destinationHeight, true);
    
        return scaledBitmap;
    }

    /**
     * Adds elemnts in the array into vector
     * @param vector
     * @param array
     */
    public static void addStrings(Vector<String> vector,
            String[] array) {
        if ((array != null) && (vector != null)) {
            for (int i = 0; i < array.length; i++) {
                vector.add(array[i]);
            }
        }
    }

    /**
     * Creates a random file name not used inside a given directory, creates an empty file with that name inside the directory
     * and returns the file name (not including the path)
     *  
     * @param directory The directory to contain the file
     * @param fileExtension The file extension to use
     *  
     * @return The new file name (not including the path)
     */
    public static String findAndCreateNotUsedFileName(File directory,
            String fileExtension) {
        File fileDescriptor;
        String result = null;
        
        do {
            result = String.valueOf(Math.random());
            
            if (result.length() > 0) {
                if ((fileExtension != null) && (fileExtension.length() > 0)) {
                    result += "." + fileExtension;
                }
                
                fileDescriptor = new File(directory, result);
                
                if (!fileDescriptor.exists()) {
                    try {
                        fileDescriptor.createNewFile();
                    } catch (IOException e) {
                        result = null;
                        break;
                    }
                } else {
                    result = null;
                }
            } else {
                result = null;
            }
        } while (result == null);
        
        return result;
    }

    /**
     * Checks whether the provided directory is created. It creates the directory when it is not found
     * 
     * @param directory The directory File object to check
     */
    public static void checkAndCreateDirectory(File cacheDir) {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }
    
    /**
     * Log throwable
     * @param tag the tag
     * @param t the throwable
     */
    public static void logThrowable(String tag, Throwable t) {
        logLine(tag, Log.getStackTraceString(t));
    }

    /**
     * Log a line
     * @param tag the tag
     * @param line the line
     */
    public static void logLine(String tag, String line) {
        if (Constants.TRACE_ALLOWED) {
            Log.d(TAG, line);
        }
    }

}
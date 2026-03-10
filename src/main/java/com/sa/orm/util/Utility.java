package com.sa.orm.util;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.lang.reflect.*;

/**
 * <p>Defines general utility methods that can be used for any
 * project. These methods include {@link #getName(String, String, String)},
 * {@link #getAddressMultiLine(String, String, String, String, String, String)},
 * {@link #getFileContentsBinary(String)}, {@link #search(String[], String)},
 * {@link #printParameters(HttpServletRequest, HttpSession)},
 * {@link #moveFile(String, String, String, String)} and
 * {@link #separateWholeAndDecimal(float, int, boolean, int)}.</p>
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2002 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>August 07, 2004
 * @author Ali Ahsan
 * @version 1.0
 */
public class Utility {

  private static Logger logger = Logger.getLogger(Utility.class.getName());
  
  /**
   * Returns displayable SSN string. As ssn is number in database
   * and can have leading zeros so this method can be used to convert a number
   * smaller than nine digits to nine displayable digits with leading zeros.
   * @param ssnValue SSN which may not have nine digits.
   * @return displayable SSN have nine digits
   * @ Ali Ahsan
   */
  public static String getSSN(int ssnValue) {
    if(ssnValue == 0) {
      return "";
    } // end of if
    String ssn = "" + ssnValue;
    while(ssn.length() < 9) {
      ssn = "0" + ssn;
    } // end of while
    return ssn;
  } // end of method getSSN

  /**
   * Formats the given phone number such that the area code is
   * wrapped in paranthesis, the first three digits are separated by a space
   * and the rest of the four digits are separated by a space. If there is
   * extension then the extension is shown after phone number following text
   * "ext".
   * @param phoneNumber Phone number that is to be formatted. If it is null,
   * this method will return empty string.
   * @return formatted phone number if given phone number is not null.
   * Otherwise empty string.
   */
  public static String formatPhoneNumber(String phoneNumber) {
    if(phoneNumber == null || phoneNumber.length() < 10) {
      return "";
    } // end of if
    StringBuffer newNumber = new StringBuffer("(");
    newNumber.append(phoneNumber.substring(0, 3));
    newNumber.append(") ");
    newNumber.append(phoneNumber.substring(3, 6));
    newNumber.append("-");
    newNumber.append(phoneNumber.substring(6, 10));
    if(phoneNumber.length() > 11) {
      newNumber.append(" Ext ");
      newNumber.append(phoneNumber.substring(11));
    } // end of if
    return newNumber.toString();
  } // end of method formatPhoneNumber

  /**
   * Formats the given pager number such that the phone number
   * portion of the pager number is shown as phone number format
   * i.e. (111) 111-1111 and pager number is appended to phone number with
   * a hyphen as separator between phone number and pager number.
   * @param pagerNumber Pager number that is to be formatted. If it is null,
   * this method will return empty string.
   * @return formatted pager number if given pager number is not null.
   * Otherwise empty string.
   */
  public static String formatPagerNumber(String pagerNumber) {
    if(pagerNumber == null || pagerNumber.length() < 17) {
      return "";
    } // end of if
    StringBuffer newNumber = new StringBuffer(formatPhoneNumber(pagerNumber.substring(0, 10)));
    newNumber.append("-");
    newNumber.append(pagerNumber.substring(10));
    return newNumber.toString();
  } // end of method formatPagerNumber

  /**
   * Parses the given pager number and returns a string array
   * containing four elements. First three are phone number and fourth on is
   * pager number.
   * If given value is null or has less than 17 digits, the returned array
   * contains empty strings.
   * It never returns null.
   * @param pagerNumber Pager number that is to be parsed. If it is invalid,
   * this method will return array of empty strings.
   * @return components of given pager number if given pager number is valid.
   * Otherwise empty strings.
   */
  public static String[] getPagerNumberComponents(String pagerNumber) {
    String[] returnStr = new String[] {"", "", "", ""};
    if(pagerNumber == null || pagerNumber.length() < 17) {
      return returnStr;
    } // end of if
    returnStr[0] = pagerNumber.substring(0, 3);
    returnStr[1] = pagerNumber.substring(3, 6);
    returnStr[2] = pagerNumber.substring(6, 10);
    returnStr[3] = pagerNumber.substring(10);
    return returnStr;
  } // end of method getPagerNumberComponents

  /**
   * Parses the given phone number and returns a string array
   * containing four elements. First three are phone number and fourth one is
   * extension (if exists).
   * If given value is null or has less than 10 digits, the returned array
   * contains empty strings.
   * It never returns null.
   * @param phoneNumber Phone number that is to be parsed. If it is invalid,
   * this method will return array of empty strings.
   * @return components of given phone number if given phone number is valid.
   * Otherwise empty strings.
   */
  public static String[] getPhoneNumberComponents(String phoneNumber) {
    String[] returnStr = new String[] {"", "", "", ""};
    if(phoneNumber == null || phoneNumber.length() < 10) {
      return returnStr;
    } // end of if
    returnStr[0] = phoneNumber.substring(0, 3);
    returnStr[1] = phoneNumber.substring(3, 6);
    returnStr[2] = phoneNumber.substring(6, 10);
    if(phoneNumber.length() > 11) {
      returnStr[3] = phoneNumber.substring(11);
    } // end of if
    return returnStr;
  } // end of method getPhoneNumberComponents

  /**
   * Parses the given SSN and returns a string array containing
   * three elements.
   * If given value is zero the returned array contains empty strings.
   * It never returns null.
   * @param ssn SSN that is to be parsed. If it is zeor, this method returns
   * array of empty strings.
   * @return components of given SSN if given SSN is valid. Otherwise empty strings.
   */
  public static String[] getSSNComponents(int ssn) {
    String[] returnStr = new String[] {"", "", ""};
    if(ssn == 0) {
      return returnStr;
    } // end of if
    String ssnStr = getSSN(ssn);
    returnStr[0] = ssnStr.substring(0, 3);
    returnStr[1] = ssnStr.substring(3, 5);
    returnStr[2] = ssnStr.substring(5);
    return returnStr;
  } // end of method getSSNComponents

  /**
   * Formats a ssn string placing "-" after three and then after
   * 5 digits
   * @param ssn SSN to be formatted
   * @return formatted SSN string like 123-45-6789
   */
  public static String formatSSN(String ssn) {
    if(ssn == null || ssn.length() == 0) {
      return "";
    } // end of if
    while(ssn.length() < 9) {
      ssn = "0" + ssn;
    }
    ssn = ssn.substring(0,3) + "-" + ssn.substring(3,5) + "-" + ssn.substring(5,9);
    return ssn;
  } // end of method formatSSN

  /**
   * Returns on string for the given four strings to make it a
   * presentable address in a single line. This address will look like
   * street1, street2 city state zip country
   * if all the parameters have values. Otherwise it will format it such that it
   * does not have repetition of comma/space and does not start and end with
   * a comma/space
   * @param street1 Street1 address to be included in address string
   * @param street2 Street2 address to be included in address string
   * @param city City name to be included in address string
   * @param state State to be included in address string
   * @param zip Zip code to be included in address string
   * @param country Country name to be included in address string
   * @return String having address formatted like
   * street1, street2 city state zip country
   */
  public static String getAddressOneLine(String street1, String street2,
      String city, String state, String zip, String country) {

    street1 = (street1 == null) ? "" : street1;
    street2 = (street2 == null) ? "" : street2;
    city = (city == null) ? "" : city;
    state = (state == null) ? "" : state;
    zip = (zip == null) ? "" : zip;
    country = (country == null) ? "" : country;

    StringBuffer address = new StringBuffer(street1);
    if(street2.length() > 1) {
      address.append(", ");
      address.append(street2);
    } // end of if
    if(city.length() > 1) {
      address.append(" ");
      address.append(city);
    } // end of if
    if(state.length() > 1) {
      address.append(" ");
      address.append(state);
    } // end of if
    if(zip.length() > 1) {
      address.append(" ");
      address.append(zip);
    } // end of if
    if(country.length() > 1) {
      address.append(" ");
      address.append(country);
    } // end of if
    return address.toString();
  } // end of method getAddressOneLine

  /**
   * Returns on string for the given four strings to make it a
   * presentable address in multiple lines. This address will look like
   * street1
   * street2
   * city state zip
   * country
   * if all the parameters have values. Otherwise it will format it such that it
   * does not have repetition of comma/space and does not start and end with
   * a comma/space
   * @param street1 Street1 address to be included in address string
   * @param street2 Street2 address to be included in address string
   * @param city City name to be included in address string
   * @param state State to be included in address string
   * @param zip Zip code to be included in address string
   * @param country Country name to be included in address string
   * @return String having address formatted like
   * Street1
   * Street2
   * City State Zip
   * Country
   */
  public static String getAddressMultiLine(String street1, String street2,
      String city, String state, String zip, String country) {

    street1 = (street1 == null) ? "" : street1;
    street2 = (street2 == null) ? "" : street2;
    city = (city == null) ? "" : city;
    state = (state == null) ? "" : state;
    zip = (zip == null) ? "" : zip;
    country = (country == null) ? "" : country;

    StringBuffer address = new StringBuffer(street1);
    if(street2.length() > 1) {
      address.append("\n");
      address.append(street2);
    } // end of if
    if(city.length() > 1) {
      address.append("\n");
      address.append(city);
    } // end of if
    if(state.length() > 1) {
      address.append(" ");
      address.append(state);
    } // end of if
    if(zip.length() > 1) {
      address.append(" ");
      address.append(zip);
    } // end of if
    if(country.length() > 1) {
      address.append("\n");
      address.append(country);
    } // end of if
    return address.toString();
  } // end of method getAddress

  /**
   * Does two things. First, it completes the the length of the
   * password if its length is less than six characters.
   * Secondly it encrypts password so that it can be stored in database.
   * @param input raw password.
   * @return atleast six characters and encrypted password.
   */
  public static String generatePassword(String input) {
    if(input == null) {
      return input;
    } // end of if
    while(input.length() < 6) {
      input += "0";
    } // end of while
    return input;
  } // end of method generatePassword

  /**
   * Formats the given three components of name (i.e. first, middle
   * and last) such that it places a space between first name and middle initial
   * and puts a dot after middle initial if it exists. Then it puts another
   * space between the first string and last name if last name is given.
   * So if all the three components are given, the name would look like
   * First M. Last
   * @param firstName First name component of name to be formatted
   * @param middleName Middle name component of name to be formatted
   * @param lastName Last name component of name to be formatted
   * @return Formatted name like First M. Last
   */
  public static String getName(String firstName, String middleName, String lastName) {
    firstName = (firstName == null) ? "" : firstName;
    middleName = (middleName == null) ? "" : middleName;
    lastName = (lastName == null) ? "" : lastName;

    StringBuffer name = new StringBuffer(firstName);
    if(middleName.length() > 0) {
      if(name.length() > 0) {
        name.append(" ");
      } // end of if
      name.append(middleName);
      name.append(".");
    } // end of if
    if(lastName.length() > 0) {
      if(name.length() > 0) {
        name.append(" ");
      } // end of if
      name.append(lastName);
    } // end of if
    return name.toString();
  } // end of method getName

  /**
   * Searches the given value in the given array and returns the
   * index of array at which the given value is found. Otherwise, it returns -1
   * @param src Array in which to look for the given value
   * @param value that is to be searched in the given array.
   * @return index of the value in src or -1
   */
  public static int search(int[] src, int value) {
    if(src == null) {
      return -1;
    }
    for(int i = 0; i < src.length; i++) {
      if(src[i] == value){
        return i;
      } // end of if
    }
    return -1;
  } // end of method search

  /**
   * Searches the given value in the given array and returns the
   * index of array at which the given value is found. Otherwise, it returns -1
   * @param src Array in which to look for the given value
   * @param value that is to be searched in the given array.
   * @return index of the value in src or -1
   */
  public static int search(String[] src, String value) {
    if(src == null) {
      return -1;
    }
    for(int i = 0; i < src.length; i++) {
      if(src[i] != null && src[i].equals(value)) {
        return i;
      } // end of if
    }
    return -1;
  } // end of method search

  /**
   * Compares the given string with the value returned by a method whose name
   * is given on each of the objects in the given array. If the value matches
   * the value returned by called method, index of the object in the array is
   * returned.
   * Please note that no exception will be thrown in case one occurs. -1 will
   * be returned in case of any error or if the value was not found.
   * @param src Array of objects on which the given method is to be called to
   * get the value for comparison with the given <code>value</code>.
   * @param value String to be compared with the returned value of called method
   * on each object in the array.
   * @param compareMethodName Name of the method to be called on the given
   * array to get a value for comparison
   * @return Index of the object in the array whose method return value was
   * equivalent to given <code>value</code>.
   */
  public static int search(Object[] src, int value, String compareMethodName) {
    if(src == null) {
      return -1;
    } // end of if
    try {
      for (int i = 0; i < src.length; i++) {
          if (((Integer)src[i].getClass().getMethod(compareMethodName, new Class[0]).invoke(src[i], new Object[0])).intValue() ==
              value) {
            return i;
          } // end of if
      } // end of for
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return -1;
  } // end of method search

  /**
   * Returns the name of a US state if an abreviation of that state
   * is passed to it.
   * @param abv Abreviation of a US state whose complete name is to be returned.
   * @return Complete name of a US state whose abreviated string is given to it.
   */
  public static String getState(String abv){
    if(abv == null || abv.length() < 2){
      return "";
    } // end of if
    String state = "";
    abv = abv.toLowerCase();
    switch(abv.charAt(0)){
      case 'a':
        switch(abv.charAt(1)){
          case 'a':		state = "Federal Services";	break;
          case 'e':		state = "Federal Services";	break;
          case 'k':		state = "Alaska";		break;
          case 'l':		state = "Alabama";		break;
          case 'p':		state = "Federal Services";	break;
          case 'r':		state = "Arkansas";		break;
          case 's':		state = "American Samoa";	break;
          case 'z':		state = "Arizona";		break;
          default:		state = "";			break;
        } // end of inner switch
        break;

      case 'c':
        switch(abv.charAt(1)){
          case 'a':		state = "California";	break;
          case 'o':		state = "Colorado";	break;
          case 't':		state = "Connecticut";	break;
          default:		state = "";		break;
        } // end of inner switch
        break;

      case 'd':
        switch(abv.charAt(1)){
          case 'c':		state = "District Of Columbia";	break;
          case 'e':		state = "Delaware";		break;
          default:		state = "";			break;
        } // end of inner switch
        break;

      case 'f':
        switch(abv.charAt(1)){
          case 'l':		state = "Florida";			break;
          case 'm':	state = "Federal States of Micronesia";	break;
          default:		state = "";				break;
        } // end of inner switch
        break;

      case 'g':
        switch(abv.charAt(1)){
          case 'a':		state = "Georgia";	break;
          case 'u':		state = "Guam";		break;
          default:		state = "";		break;
        } // end of inner switch
        break;

      case 'h':		state = "Hawaii";	break;

      case 'i':
        switch(abv.charAt(1)){
          case 'a':		state = "Iowa";		break;
          case 'd':		state = "Idaho";		break;
          case 'l':		state = "Illinois";		break;
          case 'n':		state = "Indiana";	break;
          default:		state = "";		break;
        } // end of inner switch
        break;

      case 'k':
        switch(abv.charAt(1)){
          case 's':		state = "Kansas";	break;
          case 'y':		state = "Kentucky";	break;
          default:		state = "";		break;
        } // end of inner switch
        break;

      case 'l':		state = "Louisiana";	break;

      case 'm':
        switch(abv.charAt(1)){
          case 'a':		state = "Massachusetts";		break;
          case 'd':		state = "Maryland";			break;
          case 'e':		state = "Maine";				break;
          case 'h':		state = "Marshall Islands";		break;
          case 'i':		state = "Michigan";			break;
          case 'n':		state = "Minnesota";			break;
          case 'o':		state = "Missouri";			break;
          case 'p':		state = "Northern Mariana Islands";	break;
          case 's':		state = "Mississippi";			break;
          case 't':		state = "Montana";			break;
          default:		state = "";				break;
        } // end of inner switch
        break;

      case 'n':
        switch(abv.charAt(1)){
          case 'c':		state = "North Carolina";		break;
          case 'd':		state = "North Dakota";		break;
          case 'e':		state = "Nebraska";		break;
          case 'h':		state = "New Hampshire";	break;
          case 'j':		state = "New Jersey";		break;
          case 'm':	state = "New Mexico";		break;
          case 'v':		state = "Nevada";		break;
          case 'y':		state = "New York";		break;
          default:		state = "";			break;
        } // end of inner switch
        break;

      case 'o':
        switch(abv.charAt(1)){
          case 'h':		state = "Ohio";		break;
          case 'k':		state = "Oklahoma";	break;
          case 'r':		state = "Oregon";	break;
          default:		state = "";		break;
        } // end of inner switch
        break;

      case 'p':
        switch(abv.charAt(1)){
          case 'a':		state = "Pennsylvania";	break;
          case 'r':		state = "Puerto Rico";	break;
          case 'w':	state = "Palau";		break;
          default:		state = "";		break;
        } // end of inner switch
        break;

      case 'r':		state = "Rhode Island";		break;

      case 's':
        switch(abv.charAt(1)){
          case 'c':		state = "South Carolina";	break;
          case 'd':		state = "South Dakota";		break;
          default:		state = "";			break;
        } // end of inner switch
        break;

      case 't':
        switch(abv.charAt(1)){
          case 'n':		state = "Tennessee";	break;
          case 'x':		state = "Texas";		break;
          default:		state = "";		break;
        } // end of inner switch
        break;

      case 'u':		state = "Utah";		break;

      case 'v':
        switch(abv.charAt(1)){
          case 'a':		state = "Virginia";		break;
          case 'i':		state = "Virgin Islands";		break;
          case 't':		state = "Vermont";		break;
          default:		state = "";			break;
        } // end of inner switch
        break;

      case 'w':
        switch(abv.charAt(1)){
          case 'a':		state = "Washington";		break;
          case 'i':		state = "Wisconsin";		break;
          case 'v':		state = "West Virginia";		break;
          case 'y':		state = "Wyoming";		break;
          default:		state = "";			break;
        } // end of inner switch
        break;


      default:		state = "";	break;
    } // end of switch
    return state;
  } // end of getState method

  /**
   *
   * @param state
   * @return
   *
 public static String getAvb(String state){
  return "";
 } // end of getAbv method
 */

  /**
   * Creates a directory / folder on the hard disk at the given path.
   * @param path Path of the directory / folder to be created.
   */
  public static void makeDirectory(String path) {
    java.io.File f = new java.io.File(path);

    if (!f.exists()) {
      logger.fine("file.mkdirs(" + path + ") - " + f.mkdirs());
    } // end of if
  } // end of method makeDirectory

  /**
   * Returns the contents of a File in form of a String
   * if the file exists. Otherwise it returns null.
   * @param file Name and full path of the file whose contents are to be
   * returned.
   * @return Contents of a file whose path is given.
   * @throws IOException Incase of any troubles in IO operation.
   */
  public static String getFileContents(String file)
      throws IOException {

    File f = new File(file);
    String ret_val = null;
    if (f.exists()) {
      DataInputStream dis = new DataInputStream(new BufferedInputStream(new
          FileInputStream(f)));
      ret_val = dis.readUTF();

      dis.close();
    }
    return ret_val;
  } // end of method getFileContents

  /**
   * Returns the contents of a File in the form of byte array
   * if the file exists. Otherwise it returns null.
   * @param fileName Name and full path of the file whose contents are to be
   * returned.
   * @return Contents of a file whose path is given.
   * @throws IOException Incase of any troubles in IO operation.
   */
  public static byte[] getFileContentsBinary(String fileName)
      throws IOException {

    File file = new File(fileName);
    byte[] ret_val = null;
    if (file.exists()) {
      ret_val = new byte[(int)file.length()];
      DataInputStream dis = new DataInputStream(new BufferedInputStream(
          new FileInputStream(file)));
      dis.readFully(ret_val);
      dis.close();

    } // end of if
    return ret_val;
  } // end of method getFileContents

  public static byte[] readFromStream(InputStream stream, int length)
      throws IOException {

    BufferedInputStream bis = new BufferedInputStream(stream);
    byte[] allBytes = new byte[length];
    bis.read(allBytes);
    return allBytes;
  } // end of method readFromStream

  /**
   * Returns all public static final Strings in the Class given
   * @param obj object of any class whose constants' names are to be returned.
   * @return Name of all the public static final String constants in the given
   * class of object.
   */
public static String[] getConstants(Object obj) {
    Field[] fields = obj.getClass().getFields();
//    logger.finer(Arrays.asList(fields));

    Vector params = new Vector();
    for (int i = 0; i < fields.length; i++) {
      int modifiers = fields[i].getModifiers();
      if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) &&
          Modifier.isPublic(modifiers) &&
          "java.lang.String".equals(fields[i].getType().getName())) {
        try {
          params.add(fields[i].get(fields[i].getName()));
//          logger.fine(fields[i].getName() + " : " + fields[i].get(fields[i].getName()));
        }
        catch (IllegalAccessException iae) {
          throw new RuntimeException(iae.getMessage());
        }
      }
    }
    String[] parameters = (String[]) params.toArray(new String[0]);
    return parameters;
  } // end of method getConstants

  /**
   * Remove the duplicate entries from the given Array of String
   * values and returns the distinct values as a String array.
   * THIS METHOD DOES NOT KEEP THE ORIGINAL ORDER OF THE GIVEN ARRAY.
   * @param array From which duplicate values are to be removed
   * @return array of String values having no duplicate values in the array.
   */
  public static String[] removeDuplicates(String[] array) {
    Map<String, String> tempMap = new HashMap<String, String>();
    for(int i = 0; i < array.length; i++) {
      tempMap.put(array[i], "");
    } // end of for
    array = (String[])tempMap.keySet().toArray(new String[0]);
    return array;
  } // end of method removeDuplicates

  /**
   * Remove the duplicate entries from the given Array of int values
   * and returns the distinct values as an int array.
   * THIS METHOD DOES NOT KEEP THE ORIGINAL ORDER OF THE GIVEN ARRAY.
   * @param array From which duplicate values are to be removed
   * @return array of int values having no duplicate values in the array.
   */
  public static int[] removeDuplicates(int[] array) {
    if(array == null) {
      return null;
    } // end of if
    if(array.length < 1) {
      return new int[0];
    } // end of if
    Map<Integer, String> tempMap = new HashMap<Integer, String>();
    for(int i = 0; i < array.length; i++) {
      tempMap.put((array[i]), "");
    } // end of for
    return integerArrayToIntArray((Integer[])tempMap.keySet().toArray(new Integer[0]));
  } // end of method removeDuplicates

  /**
   * Remove the null elements from the given Array of String
   * values and returns rest of the values as a String array.
   * THIS METHOD DOES NOT KEEP THE ORIGINAL ORDER OF THE GIVEN ARRAY.
   * @param array From which null values are to be removed
   * @return array of String values having no null values in the array.
   */
  public static String[] removeDuplicatesAndNulls(String[] array) {
    if(array == null) {
      return null;
    } // end of if
    if(array.length < 1) {
      return new String[0];
    } // end of if
    Map<String, String> tempMap = new HashMap<String, String>();
    for(int i=0; i < array.length; i++) {
      if(array[i] != null) {
        tempMap.put(array[i], "");
      } // end of if
    } // end of for
    array = (String[])tempMap.keySet().toArray(new String[0]);
    return array;
  } // end of method removeDuplicatesAndNulls

  /**
   * Remove the null elements from the given Array of String
   * values and returns rest of the values as a String array.
   * THIS METHOD KEEPS THE ORIGINAL ORDER OF THE GIVEN STRING.
   * @param array From which null values are to be removed
   * @return array of String values having no null values in the array.
   */
  public static String[] removeNulls(String[] array) {
    Vector<String> tempVector = new Vector<String>();
    for(int i = 0; i < array.length; i++) {
      if(array[i] != null) {
        tempVector.add(array[i]);
      } // end of if
    } // end of for
    array = (String[])tempVector.toArray(new String[0]);
    return array;
  } // end of method removeNulls

  /**
   * Remove those elements from the given array whose value is zero.
   * THIS METHOD KEEPS THE ORIGINAL ORDER OF THE GIVEN STRING.
   * @param array From which zero value elements are to be removed.
   * @return array of int values having no element with zero value.
   */
  public static int[] removeZeros(int[] array) {
    if(array == null) {
      return null;
    } // end of if
    if(array.length < 1) {
      return new int[0];
    } // end of if
    Vector<Integer> tempVec = new Vector<Integer>();
    for(int i = 0; i < array.length; i++) {
      if(array[i] > 0) {
        tempVec.add((array[i]));
      } // end of if
    } // end of for
    array = integerArrayToIntArray((Integer[])tempVec.toArray(new Integer[0]));
    return array;
  } // end of method removeZeros

  /**
   * Moves the given file <code>fileName</code> from <code>srcDir</code>
   * to <code>descDir</code> and renames it as the given <code>newFileName</code>.
   * @param srcDir Path of source directory.
   * @param destDir Path of destination directory.
   * @param fileName Name of file name to be moved.
   * @param newFileName New name of the file to be used in destination directory.
   * @return true if file moved and renamed successfully. Else false
   */
  public static boolean moveFile(String srcDir, String destDir, String fileName,
                                 String newFileName) {
    if(srcDir == null || destDir == null || fileName == null) {
      return false;
    }
    boolean returnValue = false;
    String osName = System.getProperty("os.name");
    Runtime runTime = Runtime.getRuntime();

    if(osName.toLowerCase().startsWith("win")) {
      logger.fine(srcDir + fileName + " has been successfully moved to "
                         + destDir + fileName);
      returnValue = true;
    } // end of if
    else {
      java.io.File src = new java.io.File(srcDir);
      if(!src.exists()) {
        logger.warning("Source Directory Does not exist");
        return false;
      } // end of if
      java.io.File dest = new java.io.File(destDir);
      if(!dest.exists()) {
        logger.warning("Destination Directory Does not exist");
        if(!dest.mkdirs()) {
          logger.warning("Cannot creat non existant destination directory");
          return false;
        } // end of if
      } // end of if
      StringBuffer shellCommands = new StringBuffer("mv ");
      shellCommands.append(srcDir);
      shellCommands.append(fileName);
      shellCommands.append(" ");
      shellCommands.append(destDir);
      if(newFileName != null) {
        shellCommands.append(newFileName);
      } else {
        shellCommands.append(fileName);
      } // end of else
      try {
        Process process = runTime.exec(new String[] {shellCommands.toString()});
        process.waitFor();
        int exitCode = process.exitValue();
        if(exitCode == 0) {
          logger.fine("File moved Successfully with Command : " + shellCommands.toString());
          returnValue = true;
        } // end of if
        else {
          logger.warning("File cannot be Moved");
        }
      } // end of try
      catch(Exception e){
        logger.warning(Utility.getStackTrace(e));
      } // end of catch
    } // end of else
    return returnValue;
  } // end of method moveFile

  /**
   * Returns an int array having int values taken from the
   * respective elements of the given Integer array.
   * @param src Array to be converted into an int array.
   * @return int array having values of the respective elements of given array.
   */
  public static int[] integerArrayToIntArray(Integer[] src){
    int[] returnValue = null;

    if (src != null) {
      returnValue = new int[src.length];
      for (int i = 0; i < src.length; i++) {
        returnValue [i] = src[i].intValue();
      } // end for

    } // end if
    return returnValue;
  } //  end of method integerArrayToIntArray

  /**
   * Returns a two strings in the form of a string array separating
   * the whole and decimal part of the given double.
   * @param srcValue Double value whose whole and decimal parts are to be
   * returned.
   * @param minimumWholeLength Minimum length of whole part of double value.
   * e.g. if the given value is 10 and users want to have it as 010, then
   * passing 3 for this arugment would do this.
   * @param decimalRequired Whether or not decimal portion of the value required?
   * @param precision Precision of the decimal portion.
   * Please note that if the precision is zero and decimalRequired is true, this
   * method will throw a RuntimeException.
   * @return Two Strings having whole and decimal part of the given value as a
   * string array.
   */
  public static String[] separateWholeAndDecimal(double srcValue,
      int minimumWholeLength, boolean decimalRequired, int precision) {

    if(decimalRequired && precision < 1) {
      throw new RuntimeException("Invalid arguments");
    } // end of if

    String src = "" + srcValue;
    String wholePart = null;
    String decimalPart = "";
    if(src.indexOf(".") > -1) {
      if(decimalRequired) {
        decimalPart = src.substring(src.indexOf(".") + 1);
      } // end of if
      wholePart = src.substring(0, src.indexOf("."));
    } // end of if
    if(decimalRequired) {
      while(decimalPart.length() < precision) {
        decimalPart += "0";
      } // end of if
    } // end of if

    while(wholePart.length() < minimumWholeLength) {
      wholePart = "0" + wholePart;
    } // end of if

    return new String[] {wholePart, decimalPart};
  } // end of method separateWholeAndDecimal

  /**
   * Returns a two strings in the form of a string array separating
   * the whole and decimal part of the given double.
   * @param srcValue Double value whose whole and decimal parts are to be
   * returned.
   * @param minimumWholeLength Minimum length of whole part of double value.
   * e.g. if the given value is 10 and users want to have it as 010, then
   * passing 3 for this arugment would do this.
   * @param decimalRequired Whether or not decimal portion of the value required?
   * @param precision Precision of the decimal portion.
   * Please note that if the precision is zero and decimalRequired is true, this
   * method will throw a RuntimeException.
   * @return Two Strings having whole and decimal part of the given value as a
   * string array.
   */
  public static String[] separateWholeAndDecimal(float srcValue,
      int minimumWholeLength, boolean decimalRequired, int precision) {

    if(decimalRequired && precision < 1) {
      throw new RuntimeException("Invalid arguments");
    } // end of if

    String src = "" + srcValue;
    String wholePart = null;
    String decimalPart = "";
    if(src.indexOf(".") > -1) {
      if(decimalRequired) {
        decimalPart = src.substring(src.indexOf(".") + 1);
      } // end of if
      wholePart = src.substring(0, src.indexOf("."));
    } // end of if
    if(decimalRequired) {
      while(decimalPart.length() < precision) {
        decimalPart += "0";
      } // end of if
    } // end of if

    while(wholePart.length() < minimumWholeLength) {
      wholePart = "0" + wholePart;
    } // end of if

    return new String[] {wholePart, decimalPart};
  } // end of method separateWholeAndDecimal

  /**
   * Used to save an array against the given key. It first checks
   * if the given map contains any object agaist the given key.If there is no
   * object it creates a <code>java.util.Vector</code> object and add the given
   * object in it and then saves the newly created <code>java.util.Vector</code>
   * in the given map against the given key.
   * If there exists an object against the given key in the given map, then this
   * method type casts that object (retrieved from map) into <code>java.util.Vector</code>
   * and adds the given method in the vector and saves the <code>java.util.Vector</code>
   * in the given map against the given key.
   * @param map Map containing <code>java.util.Vector</code> objects against keys
   * @param object Object to be saved in the map
   * @param key key to be saved in the map
   */
  public static void addToMap(Map map, Object object, Object key) {
    if(object  == null || key == null || map == null) {
      return;
    } // end of if
    Object alreadyThere = map.get(key);
    if(alreadyThere != null) {
      ((Vector)alreadyThere).addElement(object);
    } // end of if
    else {
      Vector vec = new Vector();
      vec.addElement(object);
      map.put(key, vec);
    } // end of else
  } // end of method addToMap

  /**
   * Executes the given <code>methodName</code> on each object of
   * the given array. It expects that the method invoked would return an integer
   * value. Hence it returns array of <code>int</code> having the numbers
   * returned by the invoked methods.
   * @param objs Array of objects on which given method is to be invoked.
   * @param methodName Name of the method to be invoked on the objects in the
   * given array.
   * @return Array of numbers returned by invocation of given method on the
   * given array. Null if the given array is null. Zero length array if length
   * of the given array is zero.
   */
  public static int[] getIds(Object[] objs, String methodName) {
    if(objs == null || methodName == null || methodName.trim().length() < 1) {
      return null;
    } // end of if
    if(objs.length < 1) {
      return new int[0];
    } // end of if
    try {
      Method method = objs[0].getClass().getMethod(methodName, new Class[0]);
      return getIds(objs, method);
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return null;
  } // end of method getIds

  /**
   * Executes the given <code>method</code> on each object of the given array.
   * <p>It expects that the method invoked would return an {@link Integer}
   * value. Hence it returns array of <code>int</code> having the numbers
   * returned by the invoked methods.
   * 
   * @param objs Array of objects on which given method is to be invoked.
   * @param method Method to be invoked on the objects in the given array.
   * 
   * @return Array of numbers returned by invocation of given method on the
   * given array. <code>null</code> if the given array is <code>null</code>.
   * Zero length array if length of the given array is zero.
   */
  public static int[] getIds(Object[] objs, Method method) {
    if(objs == null || method == null) {
      return null;
    } // end of if
    if(objs.length < 1) {
      return new int[0];
    } // end of if
    int[] ids = new int[objs.length];
    try {
      
      for(int i = 0; i < objs.length; i++) {
        ids[i] = ((Integer)method.invoke(objs[i], new Object[0])).intValue();
      } // end of for
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return ids;
  } // end of method getIds

  /**
   * Executes the given <code>methodName</code> on each object of the
   * given array. It expects that the method invoked would return a
   * {@link String} value. Hence it returns an array of
   * {@link String} having the values returned by the
   * invoked methods.
   * @param objs Array of objects on which given method is to be
   * invoked.
   * @param methodName Name of the method to be invoked on the objects
   * in the given array.
   * @return Array of numbers returned by invocation of given method
   * on the given array. Null if the given array is null. Zero length
   * array if length of the given array is zero.
   */
  public static String[] getFieldValues(Object[] objs, String methodName) {
    if(objs == null) {
      return null;
    } // end of if
    if(objs.length < 1) {
      return new String[0];
    } // end of if
    String[] ids = new String[objs.length];
    try {
      for(int i = 0; i < objs.length; i++) {
        ids[i] = (String)objs[i].getClass().getMethod(methodName, new Class[0]).invoke(objs[i], new Object[0]);
      } // end of for
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return ids;
  } // end of method getFieldValues

  /**
   * Invokes the given <code>method</code> on each object of the given array and
   * returns the values in a {@link List} object.
   * 
   * @param objs Array of objects on which given method is to be invoked.
   * @param method Method to be invoked on the objects in the given array.
   * 
   * @return Array of values returned by invocation of given method on the given
   * array. <code>null</code> if the given array is <code>null</code>. Zero
   * length list if length of the given array is zero.
   */
  public static List getFieldValues(Object[] objs, Method method) {
    if(objs == null || method == null) {
      return null;
    } // end of if
    if(objs.length < 1) {
      return new ArrayList();
    } // end of if
    List list = new ArrayList();
    try {
      for(int i = 0; i < objs.length; i++) {
        list.add(method.invoke(objs[i], new Object[0]));
      } // end of for
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return list;
  } // end of method getFieldValues

  /**
   * Invokes the given <code>method</code> on the given <code>target</code> and
   * returns the value returned by the method.
   * <p>It is assumed that the <code>method</code> has no input arguments.</p>
   * <p><code>null</code> is returned if either of <code>target</code> or
   * <code>method</code> is <code>null</code>.</p>
   * <p><code>null</code> is returned if <code>method</code> could not be
   * invoked on <code>target</code> due to any reason.</p>
   * 
   * @param target Object on which given method is to be invoked.
   * @param method Method to be invoked on the objects in the given array.
   * 
   * @return Return value of the <code>method</code> for the given object
   * <code>target</code>.
   */
  public static Object invokeMethod(Object target, Method method) {
    return invokeMethod(target, method, new Object[0]);
  } // end of method invokeMethod

  /**
   * Invokes the given <code>method</code> on the given <code>target</code> with
   * given <code>inputArguments</code> as input arguments and returns the value
   * returned by the method.
   * <p>It is assumed that the <code>method</code> has no input arguments.</p>
   * <p><code>null</code> is returned if either of <code>target</code> or
   * <code>method</code> is <code>null</code>.</p>
   * <p><code>null</code> is returned if <code>method</code> could not be
   * invoked on <code>target</code> due to any reason.</p>
   * 
   * @param target Object on which given method is to be invoked.
   * @param method Method to be invoked on the objects in the given array.
   * @param inputArguments Array of input arguments to be passed to
   * <code>method</code> when invoking it.
   * <p>In case the method has no input arguments, the array still shall be zero
   * length object. Passing <code>null</code> may result in error.</p>
   * 
   * @return Return value of the <code>method</code> for the given object
   * <code>target</code>.
   */
  public static Object invokeMethod(Object target, Method method, Object[] inputArguments) {
    if (target == null || method == null) {
        return null;
    }
    
    Object result = null;
    try {
      // Handle null input arguments for primitive parameters
      if (inputArguments != null) {
        Class<?>[] parameterTypes = method.getParameterTypes();
          
          // Create a new array to hold processed arguments
        Object[] processedArguments = new Object[inputArguments.length];
          
        for (int i = 0; i < inputArguments.length; i++) {
          if (i < parameterTypes.length) {
            processedArguments[i] = handlePrimitiveParameter(inputArguments[i], parameterTypes[i]);
          }
          else {
            // If more arguments than parameters, keep as is
            processedArguments[i] = inputArguments[i];
          }
        }
          
        result = method.invoke(target, processedArguments);
      }
      else {
        result = method.invoke(target, (Object[]) null);
      }
    }
    catch (Exception e) {
      logger.warning("Could not execute " + method.getName() + " on " + target.getClass().getName() + " with value " + inputArguments + "");
      logger.warning(Utility.getStackTrace(e));
    }
    
    return result;
  }

  /**
   * Handles primitive parameter cases by providing default values for null arguments
   * 
   * @param arg The actual argument value
   * @param paramType The expected parameter type
   * @return Processed argument with default values for null primitive parameters
   */
  private static Object handlePrimitiveParameter(Object arg, Class<?> paramType) {
    // If argument is not null, return as is
    if (arg != null) {
        return arg;
    }
    
    // Handle null argument for primitive types by providing default values
    if (paramType.isPrimitive()) {
        if (paramType == boolean.class) {
            return Boolean.FALSE;
        } else if (paramType == byte.class) {
            return Byte.valueOf((byte) 0);
        } else if (paramType == short.class) {
            return Short.valueOf((short) 0);
        } else if (paramType == int.class) {
            return Integer.valueOf(0);
        } else if (paramType == long.class) {
            return Long.valueOf(0L);
        } else if (paramType == float.class) {
            return Float.valueOf(0.0f);
        } else if (paramType == double.class) {
            return Double.valueOf(0.0d);
        } else if (paramType == char.class) {
            return Character.valueOf('\u0000');
        }
    }
    
    // For non-primitive types, null is acceptable
    return null;
  }
  
  public static Object invokeMethod_old(Object target, Method method, Object[] inputArguments) {
    if(target == null || method == null) {
      return null;
    } // end of if
    Object result = null;
    try {
      result = method.invoke(target, inputArguments);
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
    return result;
  } // end of method invokeMethod

  /**
   * Writes the given string to a file whose name is also given.
   * @param fileName File to be created in which the given contents are to be
   * written.
   * @param contents Contents to be written in the given file.
   */
  public static void writeFile(String fileName, String contents) {
    try{
      FileOutputStream fos = new FileOutputStream(fileName);
      BufferedOutputStream bos = new BufferedOutputStream(fos);
      bos.write(contents.getBytes());
      bos.flush();
      bos.close();
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
  } // end of method writeFile

  /**
   * Writes the given byte array to a file whose name is also given.
   * @param fileName File to be created in which the given contents are to be
   * written.
   * @param contents Contents to be written in the given file.
   */
  public static void writeFile(String fileName, byte[] contents) {
    try{
      FileOutputStream fos = new FileOutputStream(fileName);
      BufferedOutputStream bos = new BufferedOutputStream(fos);
      bos.write(contents);
      bos.flush();
      bos.close();
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
  } // end of method writeFile

  /**
   * Returns a string having <code>length</code> number of characters. Included
   * characters are digits 0 to 9, a to z and A to Z in random order.
   * @param length Required number of characters in the returned random string.
   * @return A string consisting of randomly picked numbers and characters.
   */
  public static String getRandomString(int length) {
    StringBuffer randomString = new StringBuffer();
    int randomInt = 0;
    for (int i = 0; i < length; i++) {
      randomInt = (int)(Math.random() * 122);
      if(randomInt < 48 || randomInt > 122 || (randomInt > 57 && randomInt < 65) || (randomInt > 90 && randomInt < 97)) {
        i--;
        continue;
      } // end of for
//      logger.finest(randomInt + " : " + (char)randomInt);
      randomString.append((char)randomInt);
    } // end of for
    return randomString.toString();
  } // end of method getRandomString

  /**
   * Returns a string having <code>length</code> number of characters. Included
   * characters any between the given range.
   * @param startChar ASCII code of a character starting the range to choose
   * characters from.
   * @param endChar ASCII code of a character ending the range to choose
   * characters from.
   * @param length Required number of characters in the returned random string.
   * @return A string consisting of randomly picked numbers and characters.
   */
  public static String getRandomString(int startChar, int endChar, int length) {
    StringBuffer randomString = new StringBuffer();
    int randomInt = 0;
    for (int i = 0; i < length; i++) {
      randomInt = (int)(Math.random() * endChar);
      if(randomInt < startChar || randomInt > endChar) {
        i--;
        continue;
      } // end of for
      randomString.append((char)randomInt);
    } // end of for
    return randomString.toString();
  } // end of method getRandomString

  /**
   * Returns the stack trace of the given exception as string so that
   * it can be logged properly
   * @param e Throwable object whose stack trace is to be returned.
   * @return Stack trace of the given exception object.
   */
  public static String getStackTrace(Throwable e) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(bos);
    e.printStackTrace(ps);
    return bos.toString();
  } // end of method getStackTrace
  
  /**
   * Returns a map contaning given <code>keys</code> and array of
   * objects from the given <code>objects</code>.
   * @param keys Values to be searched and filled as key in the
   * returned map. Note that the returned map contains 
   * <code>java.lang.Integer</code> class objects as keys. 
   * @param objects Array of object on which a method is to be called
   * to match the given keys and populate a map.
   * @param methodName Name of the method to be called to compare key
   * value.
   * @return Map containing a subset of <code>objects</code> against a
   * key given in <code>keys</code>.
   */
  public static Map<Integer, Object> getMap(int[] keys, Object[] objects, String methodName) {
    if (keys == null || keys.length < 1) {
      return null;
    } // end of if
    Map<Integer, Object> map = new TreeMap<Integer, Object>();
    if (objects == null || objects.length < 1) {
      for (int i = 0; i < keys.length; i++) {
        map.put(keys[i], null);
      } // end of for;
      return map;
    } // end of if

    Integer value = null;
    Vector<Object> objectVec = new Vector<Object>(Arrays.asList(objects));
    Vector<Object> matchedVec = null;
    for (int i = 0; i < keys.length; i++) {
      matchedVec = new Vector<Object>();
      Object obj = map.get(keys[i]) ; 
      if ( obj != null ) continue ;
      for (int j = 0; j < objectVec.size(); j++) {
        try {
          value = (Integer) objectVec.elementAt(j).getClass().getMethod(methodName, new Class[0]).invoke(objectVec.elementAt(j), new Object[0]);
          //logger.finest("->" + objectVec.elementAt(j) ) ;
          if (value.intValue() == keys[i]) {
            matchedVec.add(objectVec.elementAt(j));
            objectVec.remove(j);
            j--;
          } // end of if
        } // end of try
        catch (Exception ex) {
          logger.warning(Utility.getStackTrace(ex));
        }
      } // end of for j
      
      if (matchedVec != null && matchedVec.size() > 0) {
        map.put(keys[i], matchedVec.toArray());
      } // end of if
      else {
        map.put(keys[i], null);
      } // end of else
    } // end of for i
    return map;
  } // end of method getMap

  /**
   * Returns a map containing given <code>keys</code> of values 
   * returned by invoking <code>valueMethodName</code> (if 
   * <code>valueMethodName</code> is not <code>null</code>), and array
   * of objects from the given <code>objects</code> matching a key's
   * value.
   * @param keys Objects to be used as keys or those containing
   * object on which <code>valueMethodName</code> is to be called to
   * get values to be put as key in the returned map.
   * @param objects Array of object on which a method is to be called
   * to match the given (or extracted) keys and populate a map.
   * @param matchMethodName Name of the method to be called to compare
   * key value.
   * @param valueMethodName Name of method to be called on 
   * <code>keys</code> to get value to be put in the map as keys. If
   * <code>null</code>, <code>keys</code> are used as keys in the map.
   * @return Map containing a subset of <code>objects</code> against a
   * key found in <code>keys</code> or value or its method.
   */
  public static Map getMap(Object[] keys, Object[] objects, String matchMethodName, String valueMethodName) {
    if (keys == null || keys.length < 1) {
      return null;
    } // end of if
    Map map = new TreeMap();
    if (objects == null || objects.length < 1) {
      for (int i = 0; i < keys.length; i++) {
        map.put(keys[i], null);
      } // end of for;
      return map;
    } // end of if

    Object value = null;
    Vector objectVec = new Vector(Arrays.asList(objects));
    Vector matchedVec = null;
    for (int i = 0; i < keys.length; i++) {
      if(keys[i] == null) {
        continue;
      } // end of if
      matchedVec = new Vector();
      for (int j = 0; j < objectVec.size(); j++) {
        try {
          value = (Object) objectVec.elementAt(j).getClass().getMethod(matchMethodName, new Class[0]).invoke(objectVec.elementAt(j), new Object[0]);
          if (keys[i].equals(value) == true) {
            if(valueMethodName != null) {
              matchedVec.add(objectVec.elementAt(j).getClass().getMethod(valueMethodName, new Class[0]).invoke(objectVec.elementAt(j), new Object[0]));
            } // end of if
            else {
              matchedVec.add(objectVec.elementAt(j));
            } // end of else
            objectVec.remove(j);
            j--;
          } // end of if
        } // end of try
        catch (Exception ex) {
        }
      } // end of for j
      if (matchedVec != null && matchedVec.size() > 0) {
        map.put(keys[i], matchedVec.toArray());
      } // end of if
      else {
        map.put(keys[i], null);
      } // end of else
    } // end of for i
    return map;
  } // end of method getMap

  /**
   * Returns a map containing keys (values returned by given 
   * <code>keyMethodName</code> when invoked on each object of the
   * given <code>objects</code> and their counts (occurrences in the
   * given <code>objects</code>).
   * Type of key (String or Integer etc) shall be same as returned by
   * the method.
   * It throws any of <code>RuntimeException</code> as thrown by 
   * methods called. For example, if <code>keyMethodName</code>, when
   * invoked on an object, returns <code>null</code>, 
   * <code>NullPointerException</code> is thrown.
   * @param objects Array of object on which a method is to be called
   * to get key and count.
   * @param keyMethodName Name of the method to be called to get key
   * value.
   * @return Map containing keys and their counts.
   */
  public static Map getCountMap(Object[] objects, String keyMethodName) {
    Map map = new TreeMap();
    if (objects == null || objects.length < 1) {
      return map;
    } // end of if

    Object value = null;
    Integer count = null;
    for (int i = 0; i < objects.length; i++) {
      try {
        value = objects[i].getClass().getMethod(keyMethodName, new Class[0]).invoke(objects[i], new Object[0]);
        if(value != null) {
          count = (Integer)map.get(value);
          if(count == null) {
            map.put(value, 1);
          } // end of if
          else {
            map.put(value, count.intValue() + 1);
          } // end of else
        } // end of if
      } // end of try
      catch (Exception ex) {
        throw new RuntimeException(ex.getMessage(), ex);
      } // end of catch
    } // end of for
    return map;
  } // end of method getCountMap

  /**
   * Returns <code>true</code> if the given <code>childClass</code> is a child
   * class of <code>parentClass</code>.
   * <p>In case either or both of <code>childClass</code> and
   * <code>parentClass</code> is <code>null</code>, <code>false</code> is
   * returned.</p>
   * 
   * @param childClass Object of a child class to the given
   * <code>parentClass</code> to be checked if true.
   * @param parentClass Object of parent class of the given
   * <code>childClass</code> to be checked if true.
   * 
   * @return <code>true</code> if the given <code>childClass</code> is a child
   * class of <code>parentClass</code>. <code>false</code> otherwise.
   */
  public static boolean isChildClass(Class childClass, Class parentClass) {
    if(childClass == null || parentClass == null) {
      return false;
    }
    return parentClass.isAssignableFrom(childClass);
  } // end of method isChildClass
  
  /**
   * Main method to test methods of this class
   * @param args command line arguments
   * @throws Exception in case of any method throws exception
   */
  public static void main(String[] args) throws Exception {
    System.out.println("ArrayList is assignable from Collection : " + java.util.ArrayList.class.isAssignableFrom(Collection.class));
    System.out.println("Collection is assignable from List : " + java.util.Collection.class.isAssignableFrom(java.util.List.class));
    System.out.println("Collection is assignable from ArrayList : " + java.util.Collection.class.isAssignableFrom(ArrayList.class));
    
    //String src = "C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\ant\\AntBuildGenerator.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\ant\\CocoonTask.CocoonFactory.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\bean\\BeanListener.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\bean\\CocoonWrapper.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\bean\\helpers\\OutputStreamListener.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\caching\\validity\\Event.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\classloader\\ClassLoaderManager.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\classloader\\ClassLoaderManagerImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\crawler\\CocoonCrawler.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\elementprocessor\\types\\BooleanResult.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\jms\\JMSConnection.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\jsp\\JSPEngine.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\jsp\\JSPEngineServletConfig.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\persistence\\ConverterException.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\pipeline\\OutputComponentSelector.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\profiler\\EnvironmentInfo.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\renderer\\ExtendableRendererFactory.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\sax\\XMLByteStreamFragment.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\sax\\XMLByteStreamInterpreter.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\sax\\XMLDeserializer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\sax\\XMLSerializer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\sax\\XMLTeePipe.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\search\\SimpleLuceneCocoonSearcherImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\slide\\impl\\ContextTxXMLFileDescriptorsStore.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\slide\\impl\\JMSContentInterceptor.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\slide\\impl\\SlideConfigurationAdapter.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\slide\\impl\\SlideLoggerAdapter.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\slide\\impl\\SlideRepositoryImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\components\\xpointer\\ElementPathPart.PathInclusionPipe.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\convertor\\ConversionResult.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\Datatype.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\DatatypeBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\DatatypeManager.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\DefaultSelectionListBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\DynamicSelectionList.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\FlowJXPathSelectionList.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\FlowJXPathSelectionListBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\typeimpl\\BooleanType.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\typeimpl\\DateType.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\typeimpl\\EnumType.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\typeimpl\\EnumTypeBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\typeimpl\\FloatType.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\typeimpl\\FloatTypeBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\datatype\\validationruleimpl\\AbstractValidationRuleBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\transformation\\EffectPipe.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\forms\\transformation\\EffectWidgetReplacingPipe.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\generation\\asciiart\\AsciiArtPad.AsciiArtString.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\mail\\ContentTypePreference.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\mail\\datasource\\FilePartDataSource.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\mail\\MailContentHandlerDelegate.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\mail\\MailCtPref.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\mail\\MailSender.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\matching\\CookieMatcher.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\matching\\Matcher.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\matching\\RequestParamMatcher.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\ojb\\components\\ConnectionFactoryAvalonDataSource.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\acting\\helpers\\Mapping.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\aspect\\AspectDataStore.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\aspect\\impl\\DefaultAspectDataHandler.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\aspect\\impl\\SessionAspectDataStore.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\Constants.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\coplet\\adapter\\impl\\ApplicationCopletAdapter.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\coplet\\CopletBaseData.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\coplet\\CopletDescription.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\coplets\\basket\\AddToBasketAspect.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\coplets\\basket\\BasketManager.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\coplets\\basket\\BasketManagerImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\coplets\\basket\\CleanBasketEvent.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\coplets\\basket\\ContentItem.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\event\\aspect\\EventAspect.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\event\\aspect\\EventAspectContext.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\event\\aspect\\impl\\LinkEventAspect.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\event\\CopletInstanceEvent.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\event\\Event.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\event\\impl\\ChangeCopletInstanceAspectDataEvent.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\event\\impl\\CopletJXPathEvent.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\event\\impl\\CopletStatusEvent.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\event\\impl\\DefaultEventConverter.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\event\\impl\\EventAspectChain.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\event\\impl\\MaximizeEvent.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\impl\\DefaultPortalComponentManager.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\impl\\PortalServiceImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\impl\\PortalServiceInfo.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\layout\\CompositeLayout.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\layout\\impl\\CompositeLayoutImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\layout\\Item.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\layout\\renderer\\aspect\\impl\\AbstractCompositeAspect.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\layout\\renderer\\aspect\\impl\\CIncludeCopletAspect.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\layout\\renderer\\aspect\\impl\\CompositeContentAspect.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\layout\\renderer\\aspect\\impl\\ParameterAspect.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\DynamicInformationProviderImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\DynamicTitleServiceImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\factory\\ControllerFactoryImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\InformationProviderServiceImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\om\\common\\ContentTypeImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\om\\common\\ContentTypeSetImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\om\\common\\ParameterImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\om\\common\\PreferenceUtil.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\om\\PortletApplicationDefinitionImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\om\\PortletApplicationEntityImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\services\\factory\\FactoryManagerServiceImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\servlet\\ServletRequestImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\pluto\\servlet\\ServletResponseImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\PortalService.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\source\\CopletSource.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\util\\AbstractFieldHandler.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\util\\ConfigurationFieldHandler.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\util\\CopletBaseDataReferenceFieldHandler.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\util\\CopletDataFieldHandler.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\util\\CopletInstanceDataReferenceFieldHandler.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\portal\\util\\ParameterFieldHandler.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\reading\\AxisRPCReader.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\reading\\ComposerReader.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\samples\\flow\\java\\CalculatorFlow.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\selection\\BrowserSelector.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\serialization\\ElementProcessorSerializer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\serialization\\iTextSerializer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\serialization\\RTFSerializer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\sitemap\\ContentAggregator.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\sitemap\\LinkGatherer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\slide\\util\\AdminHelper.Group.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\slide\\util\\AdminHelper.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\taglib\\IterationTag.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\taglib\\jxpath\\core\\OutTag.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\taglib\\test\\IterationTestTag.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\constrained\\ContainerElementEndEvent.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\constrained\\ElementEventAdapter.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\constrained\\ElementEventListener.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\DASLTransformer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\EncodeURLTransformer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\ExtendedParserTransformer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\FragmentExtractorTransformer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\helpers\\DefaultIncludeCacheManager.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\helpers\\IncludeCacheManager.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\helpers\\IncludeCacheManagerSession.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\helpers\\VariableConfiguration.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\IncludeTransformer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\pagination\\ItemGroup.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\pagination\\PageRules.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\pagination\\Pagesheet.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\pagination\\Paginator.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\PatternTransformer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\transformation\\RoleFilterTransformer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\util\\ByteRange.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\util\\FileFormatException.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\util\\IOUtils.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\util\\log\\ExtensiblePatternFormatter.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\util\\log\\ExtensiblePatternFormatter.PatternRun.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\webapps\\authentication\\AuthenticationManager.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\webapps\\authentication\\components\\Authenticator.AuthenticationResult.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\webapps\\authentication\\components\\Authenticator.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\webapps\\authentication\\generation\\ConfigurationGenerator.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\webapps\\portal\\components\\PortalManager.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\webapps\\portal\\components\\PortalManagerImpl.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\webapps\\portal\\generation\\ConfigurationGenerator.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\webapps\\portal\\PortalConstants.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\webapps\\session\\components\\DefaultSessionManager.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\webservices\\instrument\\InstrumentationService.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\binding\\AggregateJXPathBindingBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\binding\\BindingException.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\binding\\CaseJXPathBindingBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\binding\\ComposedJXPathBindingBase.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\binding\\ContextJXPathBindingBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\binding\\DeleteNodeJXPathBindingBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\binding\\InsertBeanJXPathBindingBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\binding\\InsertNodeJXPathBindingBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\binding\\JavaScriptJXPathBindingBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\Constants.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\datatype\\DefaultDatatypeManager.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\datatype\\DynamicSelectionList.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\datatype\\FlowJXPathSelectionList.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\datatype\\FlowJXPathSelectionListBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\datatype\\typeimpl\\DateType.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\datatype\\typeimpl\\EnumType.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\datatype\\typeimpl\\EnumTypeBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\datatype\\typeimpl\\FloatType.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\datatype\\typeimpl\\FloatTypeBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\datatype\\typeimpl\\IntegerTypeBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\datatype\\validationruleimpl\\EmailValidationRule.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\event\\ActionEvent.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\event\\ActionListener.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\event\\DeferredValueChangedEvent.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\event\\impl\\JavaClassWidgetListenerBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\expression\\IsNullFunction.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\AbstractContainerWidget.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\AbstractWidget.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\ActionDefinition.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\AddRowActionDefinition.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\AggregateField.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\AggregateFieldDefinition.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\AggregateFieldDefinition.SplitMapping.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\ContainerDefinition.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\ContainerDefinitionDelegate.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\ContainerDelegate.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\ContainerWidget.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\DeleteRowsActionDefinition.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\DuplicateIdException.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\Field.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\FieldDefinition.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\FieldDefinitionBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\OutputDefinition.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\OutputDefinitionBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\formmodel\\RowActionDefinition.MoveDownDefinition.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\samples\\Contact.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\transformation\\EffectPipe.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\transformation\\EffectWidgetReplacingPipe.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\util\\JavaScriptHelper.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\validation\\impl\\JavaScriptValidator.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\woody\\validation\\impl\\JavaScriptValidatorBuilder.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\xml\\ContentHandlerWrapper.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\xml\\IncludeXMLConsumer.html.GetRight      C:\\Software\\Java\\Apache\\Cocoon\\AllDocs\\2.1\\apidocs\\org\\apache\\cocoon\\xml\\xlink\\ExtendedXLinkPipe.html.GetRight      ";
    //System.out.println(StringUtils.replaceString(src, "      ", "\n"));
    String lineSep = System.getProperty("line.separator");
    System.out.println(((int)lineSep.charAt(0)));
    System.out.println(((int)lineSep.charAt(1)));
/*Start getIds checked /
    PaperVO[] papers = new PaperVO[3];
    papers[0] = new PaperVO();
    papers[0].setPaperId(12);
    papers[1] = new PaperVO();
    papers[1].setPaperId(7);
    papers[2] = new PaperVO();
    papers[2].setPaperId(45);
    int[] ids = getIds(papers, "getPaperId");
    System.out.println(StringUtils.intArrayToString(ids, ','));
/*End*/

/*Start getFileContentsBinary checked /
    System.out.println(new String(getFileContentsBinary("C:/projects/FINS/defaultroot/papers/3/Telephone Index.txt")));
/*End*/

/*Start removeNulls checked /
    String[] array = {"123", "456", "789", "012", null, "234", null, "456"};
    System.out.println(StringUtils.stringArrayToString(array, ","));
    array = removeNulls(array);
    System.out.println(StringUtils.stringArrayToString(array, ","));
/*End*/

/*Start removeDuplicatesAndNulls checked /
    String[] array = {"123", "456", "789", "012", null, "234", null, "456"};
    System.out.println(StringUtils.stringArrayToString(array, ","));
    array = removeDuplicatesAndNulls(array);
    System.out.println(StringUtils.stringArrayToString(array, ","));
/*End*/

/*Start removeDuplicates checked /
    String[] array = {"123", "456", "789", "012", null, "234", null, "456"};
    System.out.println(StringUtils.stringArrayToString(array, ","));
    array = removeDuplicates(array);
    System.out.println(StringUtils.stringArrayToString(array, ","));
/*End*/

/*Start removeDuplicates checked /
    int[] array = {123, 456, 789, 012, 0, 234, 0, 456};
    System.out.println(StringUtils.intArrayToString(array, ','));
    array = removeDuplicates(array);
    System.out.println(StringUtils.intArrayToString(array, ','));
/*End*/

/*Start removeZeros checked /
    int[] array = {123, 456, 789, 012, 0, 234, 0, 456};
    System.out.println(StringUtils.intArrayToString(array, ','));
    array = removeZeros(array);
    System.out.println(StringUtils.intArrayToString(array, ','));
/*End*/

/*Start getPagerNumberComponents checked /
    System.out.println(StringUtils.stringArrayToString(getPagerNumberComponents("850654789621853"), ","));
    System.out.println(StringUtils.stringArrayToString(getPagerNumberComponents(null), ","));
    System.out.println(StringUtils.stringArrayToString(getPagerNumberComponents(""), ","));
    System.out.println(StringUtils.stringArrayToString(getPagerNumberComponents("85065478962147853"), "\t"));
/*End*/

/*Start formatPagerNumber checked /
    System.out.println(formatPagerNumber("850654789621853"));
    System.out.println(formatPagerNumber(""));
    System.out.println(formatPagerNumber(null));
    System.out.println(formatPagerNumber("85065478962147853"));
/*End*/

/*Start MoveFile checked /
    moveFile("C:\\test\\", "C:\\", "someone12.txt", null);
/*End*/


/*Start separateWholeAndDecimal checked *
    System.out.println(StringUtils.stringArrayToString(separateWholeAndDecimal(777.21f, 1, true, 2), "."));
    System.out.println(StringUtils.stringArrayToString(separateWholeAndDecimal(777, 4, true, 2), "."));
    System.out.println(StringUtils.stringArrayToString(separateWholeAndDecimal(777, 3, true, 2), "."));
    System.out.println(StringUtils.stringArrayToString(separateWholeAndDecimal(777, 3, false, 0), "."));
    System.out.println(StringUtils.stringArrayToString(separateWholeAndDecimal(777, 3, true, 0), "."));
/*End*/

//    System.out.println(formatPhoneNumber("8508777"));

//    System.out.println(generatePassword("Abcdef"));

//    System.out.println(getName("Ali", "A", "Jameel"));

/*
    test integerArrayToIntArray()

    Integer[] test= new Integer [5];
    test [0]=new Integer(5);
    test [1]=new Integer(23);
    test [2]=new Integer(11);
    test [3]=new Integer(2323);
    test [4]=new Integer(454);

    int result []= Utility.integerArrayToIntArray(test);
    for (int i = 0; i < result.length; i++) {
      System.out.println("  int []  " +i+ " : "+ result[i]);
    }
    end  integerArrayToIntArray()
*/

/*
    java.util.Calendar cal = java.util.Calendar.getInstance();
    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("EEE");
    for(int i = 0; i < 7; i++) {
      System.out.println(df.format(cal.getTime()) + " : " +
                         cal.get(cal.DAY_OF_WEEK));
      cal.add(cal.DATE, 1);
    } // end of for
*/
/*
    java.io.File file = new java.io.File(Constants.CODE_BASE);
    java.io.File[] files = file.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].getName().indexOf("jsp") != -1) {
        System.out.println(files[i].getName());
      }
    }
*/
//    System.out.println("E0015".compareTo("A0010"));
  } // end of method main
} // end of class Utility

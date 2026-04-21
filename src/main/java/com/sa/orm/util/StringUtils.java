package com.sa.orm.util;

import java.util.Arrays;
import java.util.stream.Collectors;

import java.text.StringCharacterIterator;
import java.io.UnsupportedEncodingException;

import java.util.Vector;

/**
 * <h4>Description</h4>Contains methods to be used to get strings in the desired
 * formats.
 * <p>These methods are utility methods to be used in any project. Some of the
 * method are {@link #getNull(String)}, {@link #getEmpty(int)},
 * {@link #getEmpty(String)} {@link #replaceString(String, String, String)} and
 * {@link #stringArrayToString(String[], String, String, String)}.</p>
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2002 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>November 11, 2008
 * @author Ali Ahsan
 * @version 1.0
 */
public class StringUtils {

  public final static String ddd_ddd__dd = "###,##0.00";
  public final static String ddd_ddd__dddd = "###,##0.0000";
  public final static String ddddd__dd = "#####0.00";

  /**
   * Removes the given <code>substring</code> from <code>source</code>
   * and returns the new string.
   * It omits all the occurrences of <code>substring</code> in
   * <code>source</code>.
   * @param source String from which the given <code>substring</code> is to
   * be removed.
   * @param substring String to remove from <code>source</code>.
   * @return String not having <code>substring</code> and having rest of the
   * contents of <code>source</code>.
   */
  public static String removeString(String source, String substring) {
    if(getNull(source) == null || getNull(substring) == null) {
      return source;
    } // end of if
    int index = -1;
    StringBuffer newStr = new StringBuffer(source);
    while(true) {
      index = newStr.toString().indexOf(substring);
      if(index > -1) {
        newStr.replace(index, index + substring.length(), "");
      } // end of if
      else {
        break;
      } // end of else
    } // end of while
    return newStr.toString();
  } // end of method removeString

  /**
   * Encodes the given string to be transported over http protocol.
   * @param src string to be encoded.
   * @returns Http encoded string
   */
  public static String httpEncode(String src) {
    try {
      return httpEncode(src, "UTF-8");
    } // end of try
    catch (Exception ex) {}
    return src;
  } // end of method httpEncode

  /**
   * Encodes the given string to be transported over http protocol.
   * @param src string to be encoded.
   * @param encoding Encoding to be used to encode <code>src</code>
   * @throws UnsupportedEncodingException In case given encoding is not
   * recognized.
   * @returns Http encoded string
   */
  public static String httpEncode(String src, String encoding)
      throws UnsupportedEncodingException {

    if (src == null) {
      return "";
    } // end of if
    return java.net.URLEncoder.encode(src, encoding);
  } // end of method httpEncode

  /**
   * Returns <code>null</code> if the given <code>src</code> is
   * either empty or <code>null</code>. Otherwise it returns the original
   * string passed to it. Purpose of this method is to have a string which is
   * not empty string.
   * @param src String to be checked for empty or <code>null</code>.
   * @return empty string or original string.
   */
  public static String getNull(String src) {
    if ("".equals(src)) {
      return null;
    } // end of if
    return src;
  } // end of method getNull

  /**
   * Returns 'N / A' if the given <code>src</code> is either
   * <code>null</code> or an empty string. Otherwise it returns the given string.
   * @param src String to be checked for <code>null</code> or empty.
   * @return 'N / A' if given string is <code>null</code> or empty OR the
   * given string.
   */
  public static String getNA(String src) {
    if (src == null || src.length() == 0) {
      return "N / A";
    } // end of if
    return src;
  } // end of method stringNA

  /**
   * Truncates the given string <code>src</code> to makes its length
   * equal to <code>len</code>.
   * If the length of <code>src<code> is less than or equal to <code>len</code>,
   * then it returns <code>src</code>.
   * It never returns null.
   * @param src String to be truncated to the given lesser length.
   * @param len Required length of the string.
   * @return a string whose length is equal to <code>len</code> if <code>len</code>
   * is less than length of <code>src</code>. Otherwise <code>src</code>.
   */
  public static String setLength(String src, int len) {
    if (src == null) {
      return "";
    } // end of if
    if(len < src.length()) {
      return src.substring(0, len);
    } // end of if
    return src;
  } // end of method setLength

  /**
   * Converts a <code>boolean</code> array into to a single
   * <code>String</code> separated by given character.
   * @param src <code>boolean</code> array that is to be converted into a single
   * <code>String</code>
   * @param separator To be used to separate the elements of the given array in
   * the resulting string
   * @return String having array elements separated by given character.
   */
  public static String booleanArrayToString(boolean [] src, char separator){
    if(src == null){
      return null;
    } // end of else
    return booleanArrayToString(src, separator, src.length);
  } // end of method intArrayToString

  /**
   * Converts a <code>boolean</code> array into to a single
   * <code>String</code> separated by given character
   * <code>separator</code>.
   * @param src <code>boolean</code> array that is to be converted into a
   * single <code>String</code>.
   * @param separator To be used to separate the elements of array in
   * the resulting string.
   * @param noOfElements Number of elements from start to be taken
   * from <code>src</code> array.
   * @return String having given number of array elements separated by
   * given character.
   */
  public static String booleanArrayToString(boolean [] src, char separator, int noOfElements){
    StringBuffer sb = null;
    if(src != null && src.length > 0){
      sb = new StringBuffer("" + src[0]);
    } // end of if
    else{
      return null;
    } // end of else
    for(int i = 1; i < noOfElements; i++){
      sb.append(separator + "" + src[i]);
    } // end of for
    return sb.toString();
  } // end of method intArrayToString

  /**
   * Converts a {@link Boolean} array into to a single {@link String} separated
   * by given <code>separator</code>.
   * 
   * @param src {@link Boolean} array that is to be converted into a single
   *  {@link String}.
   * @param separator To be used to separate the elements of the given array in
   * the returned string.
   * 
   * @return String having array elements separated by given character.
   */
  public static String booleanArrayToString(Boolean [] src, char separator){
    if(src == null){
      return null;
    } // end of else
    return booleanArrayToString(src, separator, src.length);
  } // end of method integerArrayToString


  /**
   * Converts a {@link Boolean} array into to a single {@link String} separated
   * by given character <code>separator</code>.
   * 
   * @param src {@link Boolean} array that is to be converted into a single
   * {@link String}.
   * @param separator To be used to separate the elements of array in the
   * returned string.
   * @param noOfElements Number of elements from start to be taken from
   * <code>src</code> array.
   * 
   * @return String having given number of array elements separated by
   * given character.
   */
  public static String booleanArrayToString(Boolean [] src, char separator, int noOfElements){
    StringBuilder sb = null;
    if(src != null && src.length > 0){
      sb = new StringBuilder("" + src[0].booleanValue());
    } // end of if
    else{
      return null;
    } // end of else
    for(int i = 1; i < noOfElements; i++){
      sb.append(separator + "" + src[i].booleanValue());
    } // end of for
    return sb.toString();
  } // end of method integerArrayToString

  /**
   * Converts a <code>int</code> array into to a single
   * <code>String</code> separated by given character.
   * @param src <code>int</code> array that is to be converted into a single
   * <code>String</code>
   * @param separator To be used to separate the elements of the given array in
   * the resulting string
   * @return String having array elements separated by given character.
   */
  public static String intArrayToString(int [] src, char separator){
    if(src == null){
      return null;
    } // end of else
    return intArrayToString(src, separator, src.length);
  } // end of method intArrayToString

  /**
   * Converts a <code>int</code> array into to a single
   * <code>String</code> separated by given character
   * <code>separator</code>.
   * @param src <code>int</code> array that is to be converted into a
   * single <code>String</code>.
   * @param separator To be used to separate the elements of array in
   * the resulting string.
   * @param noOfElements Number of elements from start to be taken
   * from <code>src</code> array.
   * @return String having given number of array elements separated by
   * given character.
   */
  public static String intArrayToString(int [] src, char separator, int noOfElements){
    StringBuffer sb = null;
    if(src != null && src.length > 0){
      sb = new StringBuffer("" + src[0]);
    } // end of if
    else{
      return null;
    } // end of else
    for(int i = 1; i < noOfElements; i++){
      sb.append(separator + "" + src[i]);
    } // end of for
    return sb.toString();
  } // end of method intArrayToString

  /**
   * Converts a {@link Integer} array into to a single {@link String} separated
   * by given <code>separator</code>.
   * 
   * @param src {@link Integer} array that is to be converted into a single
   *  {@link String}.
   * @param separator To be used to separate the elements of the given array in
   * the returned string.
   * 
   * @return String having array elements separated by given character.
   */
  public static String integerArrayToString(Integer [] src, char separator){
    if(src == null){
      return null;
    } // end of else
    return integerArrayToString(src, separator, src.length);
  } // end of method integerArrayToString


  /**
   * Converts a {@link Integer} array into to a single {@link String} separated
   * by given character <code>separator</code>.
   * 
   * @param src {@link Integer} array that is to be converted into a single
   * {@link String}.
   * @param separator To be used to separate the elements of array in the
   * returned string.
   * @param noOfElements Number of elements from start to be taken from
   * <code>src</code> array.
   * 
   * @return String having given number of array elements separated by
   * given character.
   */
  public static String integerArrayToString(Integer [] src, char separator, int noOfElements){
    StringBuilder sb = null;
    if(src != null && src.length > 0){
      sb = new StringBuilder("" + src[0].intValue());
    } // end of if
    else{
      return null;
    } // end of else
    for(int i = 1; i < noOfElements; i++){
      sb.append(separator + "" + src[i].intValue());
    } // end of for
    return sb.toString();
  } // end of method integerArrayToString

  /**
   * Converts a <code>long</code> array into to a single
   * <code>String</code> separated by given character.
   * @param src <code>long</code> array that is to be converted into a single
   * <code>String</code>
   * @param separator To be used to separate the elements of the given array in
   * the resulting string
   * @return String having array elements separated by given character.
   */
  public static String longArrayToString(long [] src, char separator){
    if(src == null){
      return null;
    } // end of else
    return longArrayToString(src, separator, src.length);
  } // end of method longArrayToString

  /**
   * Converts a <code>long</code> array into to a single
   * <code>String</code> separated by given character
   * <code>separator</code>.
   * @param src <code>long</code> array that is to be converted into a
   * single <code>String</code>.
   * @param separator To be used to separate the elements of array in
   * the resulting string.
   * @param noOfElements Number of elements from start to be taken
   * from <code>src</code> array.
   * @return String having given number of array elements separated by
   * given character.
   */
  public static String longArrayToString(long [] src, char separator, int noOfElements){
    StringBuffer sb = null;
    if(src != null && src.length > 0){
      sb = new StringBuffer("" + src[0]);
    } // end of if
    else{
      return null;
    } // end of else
    for(int i = 1; i < noOfElements; i++){
      sb.append(separator + "" + src[i]);
    } // end of for
    return sb.toString();
  } // end of method longArrayToString

  /**
   * Converts a {@link Long} array into to a single {@link String} separated
   * by given <code>separator</code>.
   * 
   * @param src {@link Long} array that is to be converted into a single
   *  {@link String}.
   * @param separator To be used to separate the elements of the given array in
   * the returned string.
   * 
   * @return String having array elements separated by given character.
   */
  public static String longArrayToString(Long [] src, char separator){
    if(src == null){
      return null;
    } // end of else
    return longArrayToString(src, separator, src.length);
  } // end of method longArrayToString


  /**
   * Converts a {@link Long} array into to a single {@link String} separated
   * by given character <code>separator</code>.
   * 
   * @param src {@link Long} array that is to be converted into a single
   * {@link String}.
   * @param separator To be used to separate the elements of array in the
   * returned string.
   * @param noOfElements Number of elements from start to be taken from
   * <code>src</code> array.
   * 
   * @return String having given number of array elements separated by
   * given character.
   */
  public static String longArrayToString(Long [] src, char separator, int noOfElements){
    StringBuilder sb = null;
    if(src != null && src.length > 0){
      sb = new StringBuilder("" + src[0].longValue());
    } // end of if
    else{
      return null;
    } // end of else
    for(int i = 1; i < noOfElements; i++){
      sb.append(separator + "" + src[i].longValue());
    } // end of for
    return sb.toString();
  } // end of method longArrayToString
  
  /**
   * Converts a <code>int</code> array into to a single
   * <code>String</code> separated by given character.
   * @param src <code>int</code> array that is to be converted into a
   * single <code>String</code>
   * @param separator To be used to separate the elements of the given
   * array in the resulting string
   * @return String having array elements separated by given
   * character.
   */
  public static String floatArrayToString(float [] src, char separator){
    if(src == null){
      return null;
    } // end of else
    return floatArrayToString(src, separator, src.length);
  } // end of method floatArrayToString

  /**
   * Converts a <code>float</code> array into to a single
   * <code>String</code> separated by given character
   * <code>separator</code>.
   * @param src <code>float</code> array that is to be converted into
   * a single <code>String</code>.
   * @param separator To be used to separate the elements of array in
   * the resulting string.
   * @param noOfElements Number of elements from start to be taken
   * from <code>src</code> array.
   * @return String having given number of array elements separated by
   * given character.
   */
  public static String floatArrayToString(float [] src, char separator, int noOfElements){
    StringBuffer sb = null;
    if(src != null && src.length > 0){
      sb = new StringBuffer("" + src[0]);
    } // end of if
    else{
      return null;
    } // end of else
    for(int i = 1; i < noOfElements; i++){
      sb.append(separator + "" + src[i]);
    } // end of for
    return sb.toString();
  } // end of method floatArrayToString

  /**
   * Converts a {@link Float} array into to a single {@link String} separated
   * by given character.
   * 
   * @param src {@link Float} array that is to be converted into a single
   * {@link String}.
   * @param separator To be used to separate the elements of the given array in
   * the returned string.
   * 
   * @return String having array elements separated by given character.
   */
  public static String floatArrayToString(Float [] src, char separator){
    if(src == null){
      return null;
    } // end of else
    return floatArrayToString(src, separator, src.length);
  } // end of method floatArrayToString

  /**
   * Converts a {@link Float} array into to a single {@link String} separated
   * by given character <code>separator</code>.
   * 
   * @param src {@link Float} array that is to be converted into a single
   * {@link String}.
   * @param separator To be used to separate the elements of array in the
   * returned string.
   * @param noOfElements Number of elements from start to be taken from
   * <code>src</code> array.
   * 
   * @return String having given number of array elements separated by given
   * character.
   */
  public static String floatArrayToString(Float [] src, char separator, int noOfElements){
    StringBuffer sb = null;
    if(src != null && src.length > 0){
      sb = new StringBuffer("" + src[0].floatValue());
    } // end of if
    else{
      return null;
    } // end of else
    for(int i = 1; i < noOfElements; i++){
      sb.append(separator + "" + src[i].floatValue());
    } // end of for
    return sb.toString();
  } // end of method floatArrayToString

  /**
   * Converts a <code>int</code> array into to a single
   * <code>String</code> separated by given character.
   * @param src <code>int</code> array that is to be converted into a
   * single <code>String</code>
   * @param separator To be used to separate the elements of the given
   * array in the resulting string
   * @return String having array elements separated by given
   * character.
   */
  public static String doubleArrayToString(double [] src, char separator){
    if(src == null){
      return null;
    } // end of else
    return doubleArrayToString(src, separator, src.length);
  } // end of method floatArrayToString

  /**
   * Converts a <code>float</code> array into to a single
   * <code>String</code> separated by given character
   * <code>separator</code>.
   * @param src <code>float</code> array that is to be converted into
   * a single <code>String</code>.
   * @param separator To be used to separate the elements of array in
   * the resulting string.
   * @param noOfElements Number of elements from start to be taken
   * from <code>src</code> array.
   * @return String having given number of array elements separated by
   * given character.
   */
  public static String doubleArrayToString(double [] src, char separator, int noOfElements){
    StringBuffer sb = null;
    if(src != null && src.length > 0){
      sb = new StringBuffer("" + src[0]);
    } // end of if
    else{
      return null;
    } // end of else
    for(int i = 1; i < noOfElements; i++){
      sb.append(separator + "" + src[i]);
    } // end of for
    return sb.toString();
  } // end of method floatArrayToString

  /**
   * Converts a {@link Float} array into to a single {@link String} separated
   * by given character.
   * 
   * @param src {@link Float} array that is to be converted into a single
   * {@link String}.
   * @param separator To be used to separate the elements of the given array in
   * the returned string.
   * 
   * @return String having array elements separated by given character.
   */
  public static String doubleArrayToString(Double [] src, char separator){
    if(src == null){
      return null;
    } // end of else
    return doubleArrayToString(src, separator, src.length);
  } // end of method floatArrayToString

  /**
   * Converts a {@link Float} array into to a single {@link String} separated
   * by given character <code>separator</code>.
   * 
   * @param src {@link Float} array that is to be converted into a single
   * {@link String}.
   * @param separator To be used to separate the elements of array in the
   * returned string.
   * @param noOfElements Number of elements from start to be taken from
   * <code>src</code> array.
   * 
   * @return String having given number of array elements separated by given
   * character.
   */
  public static String doubleArrayToString(Double [] src, char separator, int noOfElements){
    StringBuffer sb = null;
    if(src != null && src.length > 0){
      sb = new StringBuffer("" + src[0].doubleValue());
    } // end of if
    else{
      return null;
    } // end of else
    for(int i = 1; i < noOfElements; i++){
      sb.append(separator + "" + src[i].doubleValue());
    } // end of for
    return sb.toString();
  } // end of method floatArrayToString

  /**
   * Converts the <code>String</code> array <code>src</code> to
   * <code>int</code> array.
   * It is assumed that all the elements in <code>String</code> array
   * are whole numbers.
   * @param src <code>String</code> array whose elements contain
   * integers in string data type.
   * @return <code>int</code> array containing <code>int</code> data
   * type for the provided strings.
   * @throws NumberFormatException if any of the <code>src</code>
   * elements is not an <code>int</code>.
   */
  public static int[] stringArrayToIntArray(String src[])
      throws NumberFormatException {
    return stringArrayToIntArray(src, src.length);
  } // end of stringArrayToIntArray(String src[])

  /**
   * Converts a <code>String</code> array to <code>int</code> array
   * It is assumed that all the elements till the given
   * <code>noOfElements</code> index location in <code>src</code> are
   * integers.
   * @param src <code>String</code> array whose elements contain
   * integers in string data type.
   * @param noOfElements Number of elements from start that are to be
   * included in the <code>int</code> array.
   * @return <code>int</code> array containing <code>int</code> data
   * type for the provided strings.
   * @throws NumberFormatException if any of the <code>src</code>
   * elements is not an <code>int</code>
   */
  public static int[] stringArrayToIntArray(String src[], int noOfElements)
      throws NumberFormatException {
    if(src == null) {
      return null;
    } // end of if src is null
    int []result = new int[noOfElements];
    for(int i = 0; i < noOfElements; i++) {
      result[i] = Integer.parseInt(src[i].trim());
    } // end of for
    return result;
  } // end of method stringArrayToIntArray

  /**
   * Converts the <code>String</code> array <code>src</code> to
   * <code>float</code> array.
   * It is assumed that all the elements in <code>String</code> array
   * are whole numbers.
   * @param src <code>String</code> array whose elements contain
   * integers in string data type.
   * @return <code>float</code> array containing <code>float</code>
   * data type for the provided strings.
   * @throws NumberFormatException if any of the <code>src</code>
   * elements is not an <code>float</code>.
   */
  public static float[] stringArrayToFloatArray(String src[])
      throws NumberFormatException {
    return stringArrayToFloatArray(src, src.length);
  } // end of stringArrayToFloatArray(String src[])

  /**
   * Converts a <code>String</code> array to <code>float</code> array
   * It is assumed that all the elements till the given
   * <code>noOfElements</code> index location in <code>src</code> are
   * integers.
   * @param src <code>String</code> array whose elements contain
   * integers in string data type.
   * @param noOfElements Number of elements from start that are to be
   * included in the <code>float</code> array.
   * @return <code>float</code> array containing <code>float</code>
   * data type for the provided strings.
   * @throws NumberFormatException if any of the <code>src</code>
   * elements is not an <code>float</code>
   */
  public static float[] stringArrayToFloatArray(String src[], int noOfElements)
      throws NumberFormatException {
    if(src == null) {
      return null;
    } // end of if src is null
    float []result = new float[noOfElements];
    for(int i = 0; i < noOfElements; i++) {
      result[i] = Float.parseFloat(src[i].trim());
    } // end of for
    return result;
  } // end of method stringArrayToFloatArray

  /**
   * Parses the elements of the given <code>src</code> into
   * <code>int</code> and makes an equivalent <code>Integer</code> array to be
   * returned.
   * If the given <code>src</code> is null, this method returns null.
   * @param src Array containing whole numbers in its elements.
   * @param noOfElements Number of elements of <code>src</code> to be converted
   * into <code>java.lang.Integer</code> array.
   * @return <code>java.lang.Integer</code> array.
   * @throws NumberFormatException If any element of the given <code>src</code>
   * does not contain a valid whole number.
   */
  public static Integer[] stringArrayToIntegerArray(String src[], int noOfElements)
      throws NumberFormatException {
    if(src == null) {
      return null;
    } // end of if src is null
    Integer []result = new Integer[noOfElements];
    for(int i = 0; i < noOfElements; i++) {
      result[i] = Integer.parseInt(src[i].trim());
    } // end of for
    return result;
  } // end of method stringArrayToIntegerArray

  /**
   * Returns an array of <code>String</code> having elements that
   * are separated by the given <code>separator</code> in the <code>src</code>
   * string.
   * @param src <code>String</code> from which elements are to be extracted
   * @param separator <code>String</code> which is the <code>separator</code>
   * of elements in <code>src</code> string.
   * @return Array of <code>String</code> having elements extracted from
   * <code>src</code>.
   */
  public static String[] splitString(String src, String separator) {
    if(src == null) {
      return new String[0];
    } // end of if
    Vector<String> vec = new Vector<String>();
    int index = -1;
    int prevIndex = 0;
    while(true) {
      index = src.indexOf(separator, prevIndex);
      if(index < 0) {
        vec.addElement(src.substring(prevIndex));
        break;
      } // end of if
      vec.addElement(src.substring(prevIndex, index));
      prevIndex = index + separator.length();
    } // end of while
    return (String[]) vec.toArray(new String[0]);
  } // end of method splitString

  /**
   * Returns a <code>String</code> having elements in the
   * <code>src</code> array separated by the given <code>separator</code>.
   * @param src <code>String</code> array from which elements are to be
   * extracted.
   * @param separator <code>String</code> which is the separator of elements
   * in <code>src</code> array.
   * @return Array of <code>String</code> having elements of the given
   * <code>src</code> array.
   */
  public static String stringArrayToString(String src[], String separator) {
    if(src == null || src.length == 0) {
      return "";
    } // end of if
    StringBuffer returnString = new StringBuffer(src[0]);
    for(int i = 1; i < src.length; i ++) {
      returnString.append(separator);
      returnString.append(src[i]);
    } // end of for
    return returnString.toString();
  } // end of method stringArrayToString

  /**
   * Returns a <code>String</code> having elements in the
   * <code>src</code> array separated by the given <code>separator</code>.
   * @param src Array from which elements are to be extracted.
   * @param separator <code>String</code> which will be the separator of
   * elements in <code>src</code> array.
   * @param prefix <code>String</code> to be prepended to each element of the
   * <code>src</code> array.
   * @param suffix <code>String</code> to be apended to each element of the
   * <code>src</code> array.
   * @return <code>String</code> object having elements of the given
   * <code>src</code> separated by <code>separator</code>.
   */
  public static String stringArrayToString(String src[], String separator,
      String prefix, String suffix) {
    if(src == null || src.length == 0) {
      return "";
    } // end of if
    StringBuffer returnString = new StringBuffer();
    if(prefix != null) {
      returnString.append(prefix);
    } // end of if
    returnString.append(src[0]);
    if(suffix != null) {
      returnString.append(suffix);
    } // end of if
    for(int i = 1; i < src.length; i ++) {
      returnString.append(separator);
      if(prefix != null) {
        returnString.append(prefix);
      } // end of if
      returnString.append(src[i]);
      if(suffix != null) {
        returnString.append(suffix);
      } // end of if
    } // end of for
    return returnString.toString();
  } // end of method stringArrayToString

  /**
   * Replaces all the occurances of <code>target</code> character
   * in <code>source</code> String with <code>replacement</code> character.
   * @param source String whose characters are to be replaced.
   * @param target character which is to be replaced.
   * @param replacement character with which <code>target</code> character is
   * to be replaced.
   * @return String having all the <code>target</code> occurances replaced.
   */
  public static String replaceCharacter(String source, char target, char replacement) {
    return replaceString(source, "" + target, "" + replacement);
  } // end of method replaceCharacter

  /**
   * Replaces all the occurances of <code>target</code> string
   * in <code>source</code> string with <code>replacement</code> string.
   * @param source String whose <code>target</code> strings are to be replaced.
   * @param target Substring which is to be replaced.
   * @param replacement string with which <code>target</code> substring is
   * to be replaced.
   * @return String having all the <code>target</code> occurances replaced.
   */
  public static String replaceString(String source, String target, String replacement) {
    if(source == null) {
      return source;
    } // end of if
    String retvalue = source;
    int index = 0;
    int previousIndex = 0;
    if(source.indexOf(target) > -1) {
      StringBuffer hold = new StringBuffer();
      while(true) {
        index = source.indexOf(target, previousIndex);
        if(index < 0) {
          hold.append(source.substring(previousIndex));
          break;
        } // end of if
        hold.append(source.substring(previousIndex, index));
        hold.append(replacement);
        previousIndex = index + target.length();
      } // end of while
      retvalue = hold.toString();
    } // end of if
    return retvalue;
  } // end of method replaceString

  /**
   * Returns an empty string if the given <code>src</code> is either
   * empty or <code>null</code> or contains 'null'. Otherwise it returns the
   * original string passed to it.
   * Purpose of this method is to have a string which is never <code>null</code>
   * and does not contain 'null'.
   * @param src String to be checked for empty or <code>null</code>.
   * @return empty string or original string.
   */
  public static String getEmpty(String src) {
    if (src == null || src.equalsIgnoreCase("null")) {
      src = "";
    } // end of if
    return (src);
  } // end of method getEmpty

  /**
   * Returns a string representation of the given number. It returns
   * an empty string if the given number is zero.
   * @param num Number to be checked for 0.
   * @return empty String if given <code>num</code> is zero. Otherwise string
   * representation of the given number.
   */
  public static String getEmpty(int num) {
    String str = "";
    if (num == 0) {
      str = "";
    } // end of if
    else {
      str += num;
    } // end of else
    return (str);
  } // end of method getEmpty

  /**
   * Formats a double into the desired format. It returns
   * <code>null</code> if <code>format</code> string contains invalid characters.
   * @param value Number to be formatted.
   * @param format Format required for the given number.
   * @return String containing the given number in the given format.
   */
  public static String getDecimalFormattedString(double value, String format) {
    try{
      java.text.DecimalFormat decFormat = new java.text.DecimalFormat(format);
      String s = decFormat.format(value);
      return s;
    }
    catch(Exception e){
      return null;
    } // end of catch
  } // end of getDecimalFormattedString

  /**
   * Formats a float into the desired format. It returns
   * <code>null</code> if <code>format</code> string contains invalid characters.
   * @param value Number to be formatted.
   * @param format Format required for the given number.
   * @return String containing the given number in the given format.
   */
  public static String getDecimalFormattedString(float value, String format) {
    return getDecimalFormattedString(value,format);
  } // end of method getDecimalFormattedString

  /**
   * Returns all the tokens between <code>startToken</code> and
   * <code>endToken</code> token in the given <code>text</code>
   * @param text String to extract tokens from.
   * @param startToken Starting token of the string to start extracting tokens.
   * @param endToken Ending token to stop extracting tokens.
   * @return Array of strings containing extracted tokens.
   */
  public static String[] getTokens(String text, char startToken, char endToken) {
    Vector indexes = new Vector();

    if (text != null && text.length() > 0 && text.indexOf(startToken) != -1) {

      for (int i = text.indexOf(startToken); i < text.length(); i++) {
        if (text.charAt(i) == startToken) {
          int[] ids = new int[2];
          ids[0] = i + 1;
          ids[1] = text.indexOf(endToken, i);
          if (ids[1] == -1)
            throw new IllegalArgumentException("No corresponding Ending element " +
                startToken + " for Starting element " + endToken);
          i = ids[1] + 1;
          indexes.add(ids);
        } // end of if
      } // end of for
    } // end of if

    String[] tokens = new String[indexes.size()];

    for (int i = 0; i < indexes.size(); i++) {
      int[] ids = (int[]) indexes.get(i);
      tokens[i] = text.substring(ids[0], ids[1]);
    } // end of for

    return tokens;
  } // end of method getTokens

  /**
   * Replace characters having special meaning inside HTML tags with their
   * escaped equivalents, using character entities such as <tt>'&amp;'</tt>.
   *
   * <P>The escaped characters are :
   * <ul><li> < </li><li> > </li><li> " </li><li> ' </li>
   * <li> \ </li><li> & </li></ul>
   *
   *<P> Use cases for this method include :
   *<ul>
   * <li>render ineffective all HTML present in arbitrary text input
   * by a user (in a message board, for example)
   * <li>ensure that arbitrary text appearing inside a tag does not "confuse"
   * the tag. For example, <tt>HREF='Blah.do?Page=1&Sort=ASC'</tt>
   * does not comply with strict HTML because of the ampersand, and should be changed to
   * <tt>HREF='Blah.do?Page=1&amp;Sort=ASC'</tt>. This is commonly seen in building
   * query strings. (In JSTL, the c:url tag performs this task automatically.)
   *</ul>
   * @param aTagFragment Text to be escaped
   * @return Escaped text for html reserved characters
   */
  public static String escapeHTMLTags(String aTagFragment){
    final StringBuffer result = new StringBuffer();

    final StringCharacterIterator iterator = new StringCharacterIterator(aTagFragment);
    char character =  iterator.current();
    while (character != StringCharacterIterator.DONE ){
      switch(character) {
        case '<':
          result.append("&lt;");
          break;
        case '>':
          result.append("&gt;");
          break;
        case '\"':
          result.append("&quot;");
          break;
        case '\'':
          result.append("&#039;");
          break;
        case '\\':
          result.append("&#092;");
          break;
        case '&':
          result.append("&amp;");
          break;
        default :
          //the char is not a special one
          //add it to the result as is
          result.append(character);
      } // end of switch
      character = iterator.next();
    } // end of while
    return result.toString();
  } // end of method escapeHTMLTags

  /**
   * Replace characters having special meaning in regular expressions
   * with their escaped equivalents.
   *
   * <P>The escaped characters are:
   *<ul><li>.</li><li>\</li><li>?</li><li>*</li><li>+</li>
   *<li>&</li><li>:</li><li>{</li><li>}</li><li>[</li><li>]</li>
   *<li>(</li><li>)</li><li>^</li><li>$</li></ul>
   * @param aRegexFragment Text to be escaped
   * @return Escaped text for regex sequences
   */
  public static String escapeRegexSequences(String aRegexFragment){
    final StringBuffer result = new StringBuffer();

    final StringCharacterIterator iterator = new StringCharacterIterator(aRegexFragment);
    char character =  iterator.current();
    while (character != StringCharacterIterator.DONE ){
      /*
      * All literals need to have backslashes doubled.
      */
      switch(character) {
        case '.':
        case '\\':
        case '?':
        case '*':
        case '+':
        case '&':
        case ':':
        case '{':
        case '}':
        case '[':
        case ']':
        case '(':
        case ')':
        case '^':
        case '$':
          result.append("\\");
        default:
          result.append(character);
      } // end of switch
      character = iterator.next();
    } // end of while
    return result.toString();
  } // end of method escapeRegexSequences

  /**
   * Formats the given <code>src</code> string by spliting it using
   * the given <code>splitSeparator</code> and then taking number of
   * <code>letterCount</code> or available in that word (if availble is less
   * than <code>letterCount</code> from start of end of word, decided by
   * <code>fromStart</code> boolean value and putting a <code>newSeparator</code>
   * between then.
   * For example if you call this method with following parameters
   * formatString("Community Hospital Private Limited", true, 3, 3, " ", ".");
   * it will return "Com.Hos.Pri."
   * @param src String to be formatted
   * @param fromStart whether the chuck of letters to be taken from start of
   * the word or end. <code>true</code> means start while <code>false</code>
   * means end
   * @param letterCount How many letters to be taken from a word.
   * @param chunks How many words need to be taken
   * @param splitSeparator Which string to be used to split the given string.
   * @param newSeparator Which string to be used to separate the newly formated
   * chunks of letter(s).
   * @return formatted string based upon above rules.
   */
  public static String formatString(String src, boolean fromStart,
                      int letterCount, int chunks, String splitSeparator,
                      String newSeparator) {
    if(src == null) {
      return "";
    } // end of if
    StringBuffer returnString = new StringBuffer();
    String[] words = StringUtils.splitString(src, splitSeparator);
    int counter = 0;
    for(int i = 0; i < words.length; i++) {
      if(counter == chunks) {
        break;
      } // end of if
      if(words[i].length() > letterCount) {
        if(fromStart) {
          returnString.append(words[i].substring(0, letterCount));
        } // end of if
        else {
          returnString.append(words[i].substring(words[i].length() - letterCount));
        } // end of else
      } // end of if
      else {
        returnString.append(words[i]);
      } // end of if words length is equal to or less than letterCount
      returnString.append(newSeparator);
      counter++;
    } // end of for
    return returnString.toString();
  } // end of method formatString

  /**
   * Returns the reverse character sequence in the given string.
   * @param src String whose character sequence is to be reversed.
   * @return String containing reversed character sequence of the
   * given string.
   */
  public static String reverse(String src) {
    return (new StringBuffer(src).reverse().toString());
  } // end of method reverse

  /**
   * Returns the given <code>src</code> string wrapped in given
   * <code>prefix</code> and <code>suffix</code>.
   * If any of <code>prefix</code> and <code>suffix</code> is
   * <code>null</code>, that is replaced by empty string.
   * @param src String to be wrapped.
   * @param prefix Wrapper string to be used in the start of
   * <code>src</code> string.
   * @param suffix Wrapper string to be used at the end of
   * <code>src</code> string.
   * @return Wrapped string.
   */
  public static String wrap(String src, String prefix, String suffix) {
    return getEmpty(prefix) + src + getEmpty(suffix);
  } // end of method wrap
  
  /**
   * Returns the given <code>src</code> string wrapped in given
   * <code>wrapper</code>.
   * If <code>wrapper</code> is <code>null</code>, nothing is
   * prepended or appended to <code>src</code>.
   * @param src String to be wrapped.
   * @param wrapper Wrapper string.
   * @return Wrapped string.
   */
  public static String wrap(String src, String wrapper) {
    return wrap(src, wrapper, wrapper);
  } // end of method wrap
  
  /**
   * Returns the given <code>fieldName</code> string wrapped in the given
   * <code>wrapper</code>.
   * @param fieldName String to be wrapped.
   * @param wrapper Wrapper string.
   * @return Wrapped string.
   */
  public static String wrapDBField(String fieldName, String wrapper) {
    return wrapDBField(null, fieldName, wrapper, "");
  } // end of method wrap
  
  /**
   * Returns the given <code>fieldName</code> string and optionally
   * <code>tableName</code> wrapped in the given <code>wrapper</code>.
   * @param tableName String to be wrapped separately and prepended to returned
   * wrapped <code>fieldName</code>.
   * @param fieldName String to be wrapped.
   * @param wrapper Wrapper string.
   * @param qualifierSeparator String to be used between th
   * e<code>tableName</code> and the <code>fieldName</code> when given.
   * @return Wrapped string e.g. "email" or "User"."email".
   */
  public static String wrapDBField(String tableName, String fieldName, String wrapper, String qualifierSeparator) {
    String wrappedTableName = "";
    if(tableName != null && tableName.trim().length() > 0) {
      wrappedTableName = wrap(tableName, wrapper, wrapper) + qualifierSeparator;
    }
    return wrappedTableName + wrap(fieldName, wrapper, wrapper);
  } // end of method wrap
  
  /**
   * Returns the given <code>src</code> string wrapped in given
   * <code>wrapper</code>.
   * If <code>wrapper</code> is <code>null</code>, nothing is
   * prepended or appended to <code>src</code>.
   * @param tableName String to be wrapped separately and prepended to returned
   * wrapped <code>fieldName</code>.
   * @param fieldName String to be wrapped.
   * @param wrapperPrefix Wrapper prefix string.
   * @param wrapperSuffix Wrapper suffix string.
   * @param qualifierSeparator String to be used between th
   * @return Wrapped string.
   */
  public static String wrapDBField(String tableName, String fieldName,
      String wrapperPrefix, String wrapperSuffix, String qualifierSeparator) {
    String wrappedTableName = "";
    if(tableName != null && tableName.trim().length() > 0) {
      wrappedTableName = wrapFields(tableName, wrapperPrefix, wrapperSuffix) + qualifierSeparator;
    }
    return wrappedTableName + wrapFields(fieldName, wrapperPrefix, wrapperSuffix);
  } // end of method wrap
  
  /**
   * Returns the given <code>fields</code> string after ensuring that each of the
   * fields, if there are multiple separated by a comma character and optionally
   * space characters around the comma character, are wrapped in the given
   * <code>prefix</code> and <code>suffix</code>.
   * It does not re-wraps any of the fields that is already wrapped.
   * It takes into consideration that any of the fields may already be qualified
   * by its respective table name/alias. In such cases, both the table
   * name/alias and the field name would be wrapped if not already wrapped.
   * It also considers that the given <code>fields</code> string might have
   * multiple fields separated by a comma and optionally space characters.
   * If any of <code>prefix</code> and <code>suffix</code> is
   * <code>null</code>, that is replaced by empty string.
   * @param fields Fields, in given string, to be wrapped.
   * @param prefix Wrapper string to be used in the start of each of the
   * <code>fields</code>.
   * @param suffix Wrapper string to be used at the end of each of the
   * <code>fields</code>.
   * @return Wrapped fields as a single string.
   */
  public static String wrapFields(String fields, String prefix, String suffix) {
    if(fields == null) return fields;
    return String.join(", ", wrapFields(removeEmpty(fields.split("\s*,\s*")), prefix, suffix));
  } // end of method wrap
  
  public static String[] removeEmpty(String[] input) {
    return (String[]) Arrays.stream(input)
        .filter(s -> !s.trim().isEmpty())
        .collect(Collectors.toList()).toArray(new String[] {});
  }

  /**
   * Returns the given <code>fields</code> array after ensuring that each of the
   * fields are wrapped around with the given <code>prefix</code> and
   * <code>suffix</code>.
   * It does not re-wraps any of the fields that are already wrapped.
   * It takes into consideration that any of the fields may already be qualified
   * by its respective table name/alias e.g. User.firstName.
   * If any of <code>prefix</code> and <code>suffix</code> is
   * <code>null</code>, that is replaced by empty string.
   * @param src String to be wrapped.
   * @param prefix Wrapper string to be used in the start of
   * <code>src</code> string.
   * @param suffix Wrapper string to be used at the end of
   * <code>src</code> string.
   * @return Wrapped string.
   */
  public static String[] wrapFields(String[] fields, String prefix, String suffix) {
    if (fields == null || fields.length < 1) {
      return new String[] {};
    }
  
    // Normalize prefix and suffix: replace null with empty string
    final String p = (prefix == null) ? "" : prefix;
    final String s = (suffix == null) ? "" : suffix;
  
    String[] result = new String[fields.length];
  
    for (int i = 0; i < fields.length; i++) {
      String field = fields[i];
      if (field == null) {
        result[i] = null;
        continue;
      }

      field = field.trim();
      if (field.length() < 1) {
        result[i] = null;
        continue;
      }

      String aliasToken = " AS ";
      int asIndex = field.toUpperCase().lastIndexOf(aliasToken);
      
      String expr = "";
      String alias = "";
      if (asIndex > -1) {
        expr = field.substring(0, asIndex).trim();
        alias = field.substring(asIndex + aliasToken.length()).trim();
      }
      else {
        aliasToken = " ";
        asIndex = field.lastIndexOf(aliasToken);
        if (asIndex > -1) {
          expr = field.substring(0, asIndex).trim();
          alias = field.substring(asIndex + aliasToken.length()).trim();
        }
        else {
          expr = field;
        }
      }

      // Split by dot to handle qualification (Table.Column or Alias.Column)
      // Using -1 limit to keep empty trailing parts if they exist
      String[] parts = expr.split("\\.", -1);
      StringBuilder wrappedField = new StringBuilder();

      for (int j = 0; j < parts.length; j++) {
        String part = parts[j];
        if(p.length() > 0) {
          part = part.startsWith(p) ? part.substring(p.length()) : part;
        }
        if(s.length() > 0) {
          part = part.endsWith(s) ? part.substring(0, part.length() - 1) : part;
        }

        if (!part.isEmpty()) {
          wrappedField.append(p).append(part).append(s);
        }
        else {
          wrappedField.append(part);
        }

        // Re-add the dot if there are more parts
        if (j < parts.length - 1) {
          wrappedField.append(".");
        }
      }
      
      if (alias.length() > 0) {
        if(p.length() > 0) {
          wrappedField.append(aliasToken);
          if(alias.startsWith(p)) {
            wrappedField.append(alias);
          }
          else {
            wrappedField.append(p).append(alias).append(s);
          }
        }
    }
      result[i] = wrappedField.toString();
    }
  
    return result;
  } // end of method wrap
  
  /**
   * Returns a string having first letter capital with all other
   * letters untouched.
   * @param src String whose first letter is to be capitalized.
   * @return String having first letter capital if the length of the
   * given string after trim is greater than zero.
   */
  public static String firstLetterCapital(String src) {
    if(src == null || src.trim().length() < 1) {
      return src;
    } // end of if
    src = src.trim();
    String firstLetter = src.charAt(0) + "";
    String rest = src.substring(1);
    return firstLetter.toUpperCase() + rest;
  } // end of method firstLetterCapital
  
  /**
   * Returns a string having first letter in lower case with all other
   * letters untouched.
   * @param src String whose first letter is to be in lower case.
   * @return String having first letter in lower case if the length of
   * the given string after trim is greater than zero.
   */
  public static String firstLetterLower(String src) {
    if(src == null || src.trim().length() < 1) {
      return src;
    } // end of if
    src = src.trim();
    String firstLetter = src.charAt(0) + "";
    String rest = src.substring(1);
    return firstLetter.toLowerCase() + rest;
  } // end of method firstLetterLower
  
  /**
   * Main method to test this class.
   * @param args Command line arguments.
   * @throws Exception in case of any errors.
   */
  public static void main(String[]args) throws Exception {
    //System.out.println(removeString("Dam Dam Ali Ali Ali Ali Kar", "am"));
    //System.out.println(java.util.Arrays.asList(StringUtils.getTokens("asdasdasdas[a ]das[d]sd[asda]", '[', ']')));

    //System.out.println(StringUtils.removeString("PIPPH","P"));
    //float tempFloat = new Float("15982.92").floatValue();
    //System.out.println("temp Float "+tempFloat);
    //System.out.println(StringUtils.getDecimalFormattedValue(15982.92f, StringUtils.ddd_ddd__dd));
//    String src = "abc\ndef\nghi\njkl\nmno";
/*
    String[] elements = StringUtils.splitString(src, "@");
    System.out.println(elements.length + " elements found.");
    for(int i = 0; i < elements.length; i++) {
      System.out.println(elements[i]);
    } // end of for
    System.out.println(StringUtils.stringArrayToString(elements, "@"));
*/
//    System.out.println("client.name clientName \t>\t" + wrapFields("client.name clientName", "\"", "\""));
//    System.out.println("client.name as clientName \t>\t" + wrapFields("client.name as clientName", "\"", "\""));
//    System.out.println("name as clientName \t>\t" + wrapFields("name as clientName", "\"", "\""));
//    System.out.println("\"client\".name as clientName \t>\t" + wrapFields("\"client\".name as clientName", "\"", "\""));
//    System.out.println("client.\"name\" as clientName \t>\t" + wrapFields("client.\"name\" as clientName", "\"", "\""));

//    System.out.println("\"name\" as \"clientName\" \t>\t" + wrapFields("\"name\" as \"clientName\"", "\"", "\""));
//    System.out.println("\"client\".\"name\" as \"clientName\" \t>\t" + wrapFields("\"client\".\"name\" as \"clientName\"", "\"", "\""));

  } // end of method main

} // end of class StringUtils

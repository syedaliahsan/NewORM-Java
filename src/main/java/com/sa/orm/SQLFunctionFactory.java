package com.sa.orm;

/**
 * <h4>Description</h4>Represents delegate factory class to
 * instantiate function objects to be used in queries.
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2009 Soft Assets.
 * All Rights Reserved.
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>January 01, 2009
 * @author Ali Ahsan
 * @version 1.0
 */
public abstract class SQLFunctionFactory implements SQLFunctionFactoryInterface {

  protected final static String FUNC_COUNT = "Count";

  protected final static String FUNC_MAX = "Max";

  protected final static String FUNC_MIN = "Min";

  protected final static String FUNC_SUM = "Sum";

  protected final static String FUNC_AVERAGE = "Avg";

  protected final static String FUNC_ST_DEV = "StDev";

  protected final static String FUNC_VARIANCE = "Var";

  /**
   * Does not do anything.
   */
  protected SQLFunctionFactory() {
    ;
  } // end of no-argument constructor

  /**
   * Returns an object which represents an SQL count function.
   * @param fieldName Name of the field whose count is to be
   * calculated in database.
   * @return Object representing an SQL count function.
   */
  public SQLFunction createCount(String fieldName) {
    return new SQLFunction(FUNC_COUNT, new String[] {fieldName}, true);
  } // end of method createCount
  
  /**
   * Returns an object which represents an SQL count function.
   * @param fieldName Name of the field whose count is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL count function.
   */
  public SQLFunction createCount(String fieldName, String alias) {
    return new SQLFunction(FUNC_COUNT, new String[] {fieldName}, alias, true);
  } // end of method createCount
  
  /**
   * Returns an object which represents an SQL max function.
   * @param fieldName Name of the field whose max value is to be
   * calculated in database.
   * @return Object representing an SQL max function.
   */
  public SQLFunction createMax(String fieldName) {
    return new SQLFunction(FUNC_MAX, new String[] {fieldName}, true);
  } // end of method createMax
  
  /**
   * Returns an object which represents an SQL max function.
   * @param fieldName Name of the field whose max value is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL max function.
   */
  public SQLFunction createMax(String fieldName, String alias) {
    return new SQLFunction(FUNC_MAX, new String[] {fieldName}, alias, true);
  } // end of method createMax
  
  /**
   * Returns an object which represents an SQL min function.
   * @param fieldName Name of the field whose min value is to be
   * calculated in database.
   * @return Object representing an SQL min function.
   */
  public SQLFunction createMin(String fieldName) {
    return new SQLFunction(FUNC_MIN, new String[] {fieldName}, true);
  } // end of method createMin
  
  /**
   * Returns an object which represents an SQL min function.
   * @param fieldName Name of the field whose min value is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL min function.
   */
  public SQLFunction createMin(String fieldName, String alias) {
    return new SQLFunction(FUNC_MIN, new String[] {fieldName}, alias, true);
  } // end of method createMin
  
  /**
   * Returns an object which represents an SQL average function.
   * @param fieldName Name of the field whose average is to be
   * calculated in database.
   * @return Object representing an SQL average function.
   */
  public SQLFunction createAverage(String fieldName) {
    return new SQLFunction(FUNC_AVERAGE, new String[] {fieldName}, true);
  } // end of method createAverage
  
  /**
   * Returns an object which represents an SQL average function.
   * @param fieldName Name of the field whose average is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL average function.
   */
  public SQLFunction createAverage(String fieldName, String alias) {
    return new SQLFunction(FUNC_AVERAGE, new String[] {fieldName}, alias, true);
  } // end of method createAverage
  
  /**
   * Returns an object which represents an SQL standard deviation
   * function.
   * @param fieldName Name of the field whose standard deviation is to
   * be calculated in database.
   * @return Object representing an SQL standard deviation function.
   */
  public SQLFunction createStDev(String fieldName) {
    return new SQLFunction(FUNC_ST_DEV, new String[] {fieldName}, true);
  } // end of method createStDev
  
  /**
   * Returns an object which represents an SQL standard deviation
   * function.
   * @param fieldName Name of the field whose standard deviation is to
   * be calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL standard deviation function.
   */
  public SQLFunction createStDev(String fieldName, String alias) {
    return new SQLFunction(FUNC_ST_DEV, new String[] {fieldName}, alias, true);
  } // end of method createStDev
  
  /**
   * Returns an object which represents an SQL variance function.
   * @param fieldName Name of the field whose variance is to be
   * calculated in database.
   * @return Object representing an SQL variance function.
   */
  public SQLFunction createVariance(String fieldName) {
    return new SQLFunction(FUNC_VARIANCE, new String[] {fieldName}, true);
  } // end of method createVariance
  
  /**
   * Returns an object which represents an SQL variance function.
   * @param fieldName Name of the field whose variance is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL variance function.
   */
  public SQLFunction createVariance(String fieldName, String alias) {
    return new SQLFunction(FUNC_VARIANCE, new String[] {fieldName}, alias, true);
  } // end of method createVariance
  
  /**
   * Returns an object which represents an SQL sum function.
   * @param fieldName Name of the field whose sum is to be
   * calculated in database.
   * @return Object representing an SQL sum function.
   */
  public SQLFunction createSum(String fieldName) {
    return new SQLFunction(FUNC_SUM, new String[] {fieldName}, true);
  } // end of method createSum
  
  /**
   * Returns an object which represents an SQL sum function.
   * @param fieldName Name of the field whose sum is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL sum function.
   */
  public SQLFunction createSum(String fieldName, String alias) {
    return new SQLFunction(FUNC_SUM, new String[] {fieldName}, alias, true);
  } // end of method createSum
  
  /**
   * Returns a string that can be used in an SQL query representing
   * the given functions.
   * @param functions Object whose SQL is to be returned.
   * @return String to be used as part of an SQL query.
   */
  public String getFunctionString(SQLFunction[] functions, boolean includeAlias) {
    if(functions == null || functions.length < 1) {
      return "";
    } // end of if
    StringBuffer sb = new StringBuffer(functions[0].getFunctionString(includeAlias));
    for (int i = 1; i < functions.length; i++) {
      sb.append(", ");
      sb.append(functions[i].getFunctionString(includeAlias));
    } // end of for
    return sb.toString();
  } // end of method getFunctionString
  
} // end of class SQLFunctionFactory

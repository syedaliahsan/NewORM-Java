package com.sa.orm;

/**
 * <h4>Description</h4>Represents template for factory classes for
 * database specific function object generation.
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2009 Soft Assets.
 * All Rights Reserved.
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>January 01, 2009
 * @author Ali Ahsan
 * @version 1.0
 */
public interface SQLFunctionFactoryInterface {

  /**
   * Returns an object which represents an SQL count function.
   * @param fieldName Name of the field whose count is to be
   * calculated in database.
   * @return Object representing an SQL count function.
   */
  public SQLFunction createCount(String fieldName);
  
  /**
   * Returns an object which represents an SQL count function.
   * @param fieldName Name of the field whose count is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL count function.
   */
  public SQLFunction createCount(String fieldName, String alias);
  
  /**
   * Returns an object which represents an SQL max function.
   * @param fieldName Name of the field whose max value is to be
   * calculated in database.
   * @return Object representing an SQL max function.
   */
  public SQLFunction createMax(String fieldName);
  
  /**
   * Returns an object which represents an SQL max function.
   * @param fieldName Name of the field whose max value is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL max function.
   */
  public SQLFunction createMax(String fieldName, String alias);
  
  /**
   * Returns an object which represents an SQL min function.
   * @param fieldName Name of the field whose min value is to be
   * calculated in database.
   * @return Object representing an SQL min function.
   */
  public SQLFunction createMin(String fieldName);
  
  /**
   * Returns an object which represents an SQL min function.
   * @param fieldName Name of the field whose min value is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL min function.
   */
  public SQLFunction createMin(String fieldName, String alias);
  
  /**
   * Returns an object which represents an SQL average function.
   * @param fieldName Name of the field whose average is to be
   * calculated in database.
   * @return Object representing an SQL average function.
   */
  public SQLFunction createAverage(String fieldName);
  
  /**
   * Returns an object which represents an SQL average function.
   * @param fieldName Name of the field whose average is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL average function.
   */
  public SQLFunction createAverage(String fieldName, String alias);
  
  /**
   * Returns an object which represents an SQL standard deviation
   * function.
   * @param fieldName Name of the field whose standard deviation is to
   * be calculated in database.
   * @return Object representing an SQL standard deviation function.
   */
  public SQLFunction createStDev(String fieldName);
  
  /**
   * Returns an object which represents an SQL standard deviation
   * function.
   * @param fieldName Name of the field whose standard deviation is to
   * be calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL standard deviation function.
   */
  public SQLFunction createStDev(String fieldName, String alias);
  
  /**
   * Returns an object which represents an SQL variance function.
   * @param fieldName Name of the field whose variance is to be
   * calculated in database.
   * @return Object representing an SQL variance function.
   */
  public SQLFunction createVariance(String fieldName);
  
  /**
   * Returns an object which represents an SQL variance function.
   * @param fieldName Name of the field whose variance is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL variance function.
   */
  public SQLFunction createVariance(String fieldName, String alias);
  
  /**
   * Returns an object which represents an SQL sum function.
   * @param fieldName Name of the field whose sum is to be
   * calculated in database.
   * @return Object representing an SQL sum function.
   */
  public SQLFunction createSum(String fieldName);
  
  /**
   * Returns an object which represents an SQL sum function.
   * @param fieldName Name of the field whose sum is to be
   * calculated in database.
   * @param alias Alias of the commulative functionf field.
   * @return Object representing an SQL sum function.
   */
  public SQLFunction createSum(String fieldName, String alias);
  
  /**
   * Returns a string that can be used in an SQL query representing
   * the given functions.
   * @param functions Object whose SQL is to be returned.
   * @return String to be used as part of an SQL query.
   */
  public String getFunctionString(SQLFunction[] functions, boolean includeAlias);
  
} // end of interface SQLFunction

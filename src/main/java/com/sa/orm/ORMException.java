package com.sa.orm;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * <h4>Description</h4>The only exception class for the ORM.
 * <p>Type of exceptions are identified by the error code defined as
 * <code>enum</code> in this class.</p>
 * <p>To make this class sufficient for the entire API, an extra method
 * {@link #getStandardizedMessage()} has been defined which returns a localized
 * string which can be understood by the end user as well.</p>
 * <p>The file containing the user friendly messages is assumed to be available
 * in class path and named 'ExceptionMessages.properties'.</p>
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2010
 * @author Ali Ahsan
 * @version 1.1
 */
@SuppressWarnings("serial")
public class ORMException extends Exception {
  
  public static enum ERROR_CODE {
    
    /**
     * Error code for the situation when the driver class to get database
     * connection is not found.
     * <p>One parameter is required for standardized message i.e. fully
     * qualified name of the driver class.</p>
     */
    DATABASE_DRIVER_CLASS_NOT_FOUND,
    
    /**
     * Error code for the situation when the driver manager could not connect 
     * to database server due to unknown host name or invalid host port.
     * <p>No parameters are required for standardized message.</p>
     */
    DB_HOST_NOT_FOUND,
    
    /**
     * Error code for the situation when the driver manager could not connect 
     * to database server due to invalid port number or database overload.
     * <p>No parameters are required for standardized message.</p>
     */
    DB_COULD_NOT_CONNECT,
    
    /**
     * Error code for the situation when the database was not found in the
     * database server.
     * <p>One parameter is required for standardized message: i.e. database
     * name.</p>
     */
    DB_NOT_FOUND,
    
    /**
     * Error code for the situation when the specified user name for database
     * connection, does not exist in the database.
     * <p>Two parameters are required for standardized message: i.e. database
     * user name and database host name.</p>
     */
    INVALID_DB_USER,
    
    /**
     * Error code for the situation when the specified database user password is
     * not correct.
     * <p>Two parameters are required for standardized message: i.e. database
     * user name and database host name.</p>
     */
    INVALID_DB_USER_PASSWORD,
    
    /**
     * Error code for the situation when the syntax of the SQL statement is not
     * valid.
     * <p>One parameter is required for standardized message: i.e. the string
     * from server mentioning the place where syntax was incorrect.</p>
     */
    INVALID_QUERY_SYNTAX,
    
    /**
     * Error code for the situation when the database server did not allow a
     * particular database operation to be performed. In other words, logged in
     * database user did not have the privilege to perform the operation in
     * question.
     */
    DB_OPERATION_NOT_AUTHORIZED,
    
    /**
     * Error code for the situation, while inserting or updating a row, when a
     * field's value is shorter or longer than the allowed length.
     * <p>XXX parameters are required for standardized message: i.e. entity
     * (table) name and attribute (column) name which refer to the primary key
     * of the row being deleted.</p>
     */
    INVALID_FIELD_LENGTH,
    
    /**
     * Error code for the situation when a duplicate value had been specified
     * for a column, marked as primary key or unique, in insert or update query.
     * <p>Three parameters are required i.e. entity (table) name, attribute
     * (column) name and value which was duplicate.</p>
     */
    DUPLICATE_KEY_NOT_ALLOWED,
    
    /**
     * Error code for the situation when a required column's value was not
     * provided while inserting or updating a row.
     */
    REQUIRED_FIELD_NOT_SPECIFIED,
    
    /**
     * Error code for the situation, while inserting or updating a row, when a
     * specified foreign key value did not exist in the respective parent table.
     */
    INVAILD_PARENT_KEY,
    
    /**
     * Error code for the situation, while deleting a row, when one or more
     * rows are found, in any table(s), having the primary key, of the row being
     * deleted, as foreign key.
     * <p>Two parameters are required for standardized message: i.e. entity
     * (table) name and attribute (column) name which refer to the primary key
     * of the row being deleted.</p>
     */
    DELETE_INTEGRITY_CONSTRAINT,
    
    /**
     * 
     */
    DATA_TYPE_MISMATCH
  };
  
  private static final String resourceName = "ExceptionMessages";

  /**
   * List of parameters to be inserted in the returned user friendly message.
   */
  protected List<String> params = null;
  
  protected ERROR_CODE errorCode = null;
  
  /**
   * Instantiates an object of this class with default instance member values.
   * <p>NOTE: {@link #getMessage()}, {@link #getUserFriendlyMessage()} and
   * {@link #getCause()} will all be <code>null</code>. </p>
   */
  public ORMException() {
    this(null, null);
  } // end of no-argument constructor
  
  /**
   * Instantiates an object of this class with given and default instance member
   * values.
   * 
   * @param message Internal (technical) message of the exception.
   */
  public ORMException(String message) {
    this(message, null);
  } // end of constructor;
  
  /**
   * Instantiates an object of this class with given and default instance member
   * values.
   * 
   * @param cause {@link Throwable} which caused this exception.
   */
  public ORMException(Throwable cause) {
    this(null, cause);
  } // end of constructor;
  
  /**
   * Instantiates an object of this class with given and default instance member
   * values.
   * 
   * @param message Internal (technical) message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   */
  public ORMException(String message, Throwable cause) {
    this(message, cause, null);
  } // end of constructor;
  
  /**
   * Instantiates an object of this class with given and default instance member
   * values.
   * 
   * @param message Internal (technical) message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * @param errorCode {@link ERROR_CODE} of this exception.
   */
  public ORMException(String message, Throwable cause, ERROR_CODE errorCode) {
    this(message, cause, errorCode, null);
  } // end of constructor;
  
  /**
   * Instantiates an object of this class with given instance member values.
   * 
   * @param message Internal (technical) message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * @param errorCode {@link ERROR_CODE} of this exception.
   * @param params Parameters to replace place holders in user friendly message.
   * The order of the objects in <code>params</code> shall be according to the
   * place holders specified in the respective message in the properties file.
   */
  public ORMException(String message, Throwable cause, ERROR_CODE errorCode, List params) {
    super(message, cause);
    this.errorCode = errorCode;
    this.params = params;
  } // end of constructor;
  
  /**
   * Returns the given <code>message</code> String back after replacing the
   * placeholders with {@link #params}.
   * <p>If the given <code>message</code> is null or empty or blank, it is
   * returned as is.</p>
   * <p>If {@link #params} have no values, <code>message</code> is returned as
   * is.</p>
   * 
   * @param message Message having place holders to be replaced with params.
   * 
   * @return String having place holders replaced with params.
   */
  public String insertParams(String message) {
    if(message == null || message.trim().length() < 1) {
      return message;
    } // end of if
    if(params == null || params.size() < 1) {
      return message;
    } // end of if
    return MessageFormat.format(message, (Object[])params.toArray(new Object[0]));
  } // end of method insertParams
  
  /**
   * Returns localized user friendly message using the current locale, if
   * available, for translation.
   * 
   * @return User friendly localized message.
   */
  public String getStandardizedMessage() {
    return getStandardizedMessage(Locale.getDefault());
  } // end of method getStandardizedMessage
  
  /**
   * Returns localized user friendly message using the current locale, if
   * available, for translation.
   * 
   * @param {@link Locale} to be used to translated the returned message.
   * 
   * @return User friendly localized message.
   */
  public String getStandardizedMessage(Locale locale) {
    if(errorCode == null) {
      return "";
    } // end of if
    PropertyResourceBundle resourceBundle = (PropertyResourceBundle) ResourceBundle.getBundle(resourceName, locale);
    Object msg = resourceBundle.handleGetObject(errorCode.toString());
    String message = null;
    if(msg != null) {
      message = msg.toString();
    } // end of if
    return insertParams(message);
  } // end of method getStandardizedMessage
  
} // end of class ORMException

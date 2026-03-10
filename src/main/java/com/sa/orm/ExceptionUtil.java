package com.sa.orm;

import java.sql.ResultSet;

/**
 * <h4>Description</h4>Checks if a particular type of exception has occurred and
 * returns an {@link ORMException} object containing appropriate values.
 * 
   * TODO methods for the following cases are yet to be added.
   * 1. When the package specified for underlying ORM implementation does not contain required classes.
   * 2. When server rejects connection request due to over load.
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2010
 * @author Ali Ahsan
 * @version 1.1
 */
public abstract class ExceptionUtil {

  /**
   * Returns an object of {@link ORMException} containing the appropriate
   * instance member values to represent the failure in making a connection to
   * database server.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * making a connection to database server.
   */
  public ORMException getConnectionException(String message, Throwable cause) {
    return getConnectionException(message, cause, true);
  }

  /**
   * Returns an object of {@link ORMException} containing the appropriate
   * instance member values to represent the failure in making a connection to
   * database server.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * @param alwaysReturn If <code>true</code>, an object of {@link ORMException}
   * is returned even if appropriate error code and parameters are not set.
   * Methods of this class, when call this method, pass <code>false</code>.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * making a connection to database server.
   * 
   * FIXME Include the case when server rejects connection due to over load.
   */
  public ORMException getConnectionException(String message, Throwable cause, boolean alwaysReturn) {
    throw new RuntimeException("Must be overridden by the child class");
  }

  /**
   * Returns an object of {@link ORMException} containing the appropriate
   * instance member values to represent the exception caught due to errors in
   * data retrieval operation in database server.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * data retrieval operation in database server.
   * 
   * FIXME Complete this method.
   */
  public ORMException getSelectException(String message, Throwable cause) {
    throw new RuntimeException("Must be overridden by the child class");
  } // end of method getSelectException

  /**
   * Returns an object of {@link ORMException} containing the appropriate
   * instance member values to represent the exception caught while filling an
   * object from {@link ResultSet} object..
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * object filling.
   * 
   * FIXME Complete this method.
   */
  public static ORMException getFillObjectException(String message, Throwable cause) {
    throw new RuntimeException("Must be overridden by the child class");
  } // end of method getFillObjectException

  /**
   * Returns an object of {@link ORMException} containing the appropriate
   * instance member values to represent the failure in executing an update
   * query in database server.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * executing an udpate query in database server.
   * 
   * FIXME Complete this method.
   */
  public ORMException getUpdateQueryExecutionException(String message, Throwable cause) {
    throw new RuntimeException("Must be overridden by the child class");
  } // end of method getUpdateQueryExecutionException

  /**
   * Returns an object of {@link ORMException} containing the appropriate
   * instance member values to represent the exception caught due to errors in
   * insertion operation in database server.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * insertion operation in database server.
   * 
   * FIXME Complete this method.
   */
  public ORMException getInsertException(String message, Throwable cause) {
    throw new RuntimeException("Must be overridden by the child class");
  } // end of method getInsertException

  /**
   * Returns an object of {@link ORMException} containing the appropriate
   * instance member values to represent the exception caught due to errors in
   * update operation in database server.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * update operation in database server.
   * 
   * FIXME Complete this method.
   */
  public ORMException getUpdateException(String message, Throwable cause) {
    throw new RuntimeException("Must be overridden by the child class");
  } // end of method getUpdateException

  /**
   * Returns an object of {@link ORMException} containing the appropriate
   * instance member values to represent the exception caught due to errors in
   * deletion operation in database server.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * delete operation in database server.
   */
  public ORMException getDeleteException(String message, Throwable cause) {
    throw new RuntimeException("Must be overridden by the child class");
  } // end of method getDeleteException

} // end of class ExceptionUtil

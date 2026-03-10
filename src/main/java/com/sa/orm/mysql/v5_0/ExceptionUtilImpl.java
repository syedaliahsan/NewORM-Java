package com.sa.orm.mysql.v5_0;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sa.orm.ExceptionUtil;
import com.sa.orm.ORMException;
import com.sa.orm.ORMException.ERROR_CODE;

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
public class ExceptionUtilImpl extends ExceptionUtil {

  private static final String DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN1 = "Cannot delete or update a parent row: a foreign key constraint fails";
  private static final String DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN2 = "`.`";
  private static final String DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN3 = "`, CONSTRAINT";
  private static final String DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN4 = "FOREIGN KEY (`";
  private static final String DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN5 = "`) REFERENCES";
  
  private static final String DRIVER_CLASS_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN1 = "Cannot load JDBC driver class";
  private static final String DRIVER_CLASS_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN2 = "'";
  
  private static final String DB_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN1 = "Syntax error or access violation,  message from server: \"Unknown database";
  private static final String DB_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN2 = "'";
  
  private static final String DB_HOST_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN1 = "Network is unreachable: connect";
  
  private static final String DB_COULD_NOT_CONNECT_EXCEPTION_MESSAGE_TOKEN1 = "Connection refused: connect";
  
  private static final String INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN1 = "Invalid authorization specification,  message from server: \"Access denied for user";
  private static final String INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN2 = "(using password: NO)";
  private static final String INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN3 = "'";
  private static final String INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN4 = "'@'";
  
  private static final String INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN1 = "Invalid authorization specification,  message from server: \"Access denied for user";
  private static final String INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN2 = "(using password: YES)";
  private static final String INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN3 = "'";
  private static final String INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN4 = "'@'";
  
  private static final String INVALID_QUERY_SYNTAX_EXCEPTION_MESSAGE_TOKEN1 = "Syntax error or access violation,  message from server: \"You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use";
  private static final String INVALID_QUERY_SYNTAX_EXCEPTION_MESSAGE_TOKEN2 = "'";
  
  /**
   * Returns an object of {@link ORMException} which represents the error caused
   * by the non-existence of the driver class to be used to connect to database.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * finding driver class to get database connection.
   */
  private static ORMException getDriverClassNotFoundException(String message, Throwable cause) {
    if(message != null) {
      int index = message.indexOf(DRIVER_CLASS_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN1);
      if(index > -1) {
        String driverClassName = null;
        index += DRIVER_CLASS_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN1.length();
        driverClassName = message.substring(message.indexOf(DRIVER_CLASS_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN2, index) + DRIVER_CLASS_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN2.length(), message.lastIndexOf(DRIVER_CLASS_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN2));

        List params = new ArrayList();
        params.add(driverClassName);
        return new ORMException(message, cause, ERROR_CODE.DATABASE_DRIVER_CLASS_NOT_FOUND, params);
      } // end of if
    }
    return null;
  } // end of method getDriverClassNotFoundException
  
  /**
   * Returns an object of {@link ORMException} which represents the error caused
   * by the non-existence of the driver class to be used to connect to database.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * finding driver class to get database connection.
   */
  private static ORMException getDatabaseHostNotFoundException(String message, Throwable cause) {
    if(message != null) {
      int index = message.indexOf(DB_HOST_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN1);
      if(index > -1) {
        return new ORMException(message, cause, ERROR_CODE.DB_HOST_NOT_FOUND, null);
      } // end of if
    }
    return null;
  } // end of method getDatabaseHostNotFoundException
  
  /**
   * Returns an object of {@link ORMException} which represents the error caused
   * by the non-existence of the driver class to be used to connect to database.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * finding driver class to get database connection.
   */
  private static ORMException getDatabaseCouldNotConnectException(String message, Throwable cause) {
    if(message != null) {
      int index = message.indexOf(DB_COULD_NOT_CONNECT_EXCEPTION_MESSAGE_TOKEN1);
      if(index > -1) {
        return new ORMException(message, cause, ERROR_CODE.DB_COULD_NOT_CONNECT, null);
      } // end of if
    }
    return null;
  } // end of method getDatabaseCouldNotConnectException
  
  /**
   * Returns an object of {@link ORMException} which represents the error caused
   * by the non-existence of the driver class to be used to connect to database.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * finding driver class to get database connection.
   */
  private static ORMException getDatabaseNotFoundException(String message, Throwable cause) {
    if(message != null) {
      int index = message.indexOf(DB_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN1);
      if(index > -1) {
        String databaseName = null;
        index += DB_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN1.length();
        int index1 = message.indexOf(DB_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN2, index);
        index1 += DB_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN2.length();
        int index2 = message.indexOf(DB_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN2, index1);
        databaseName = message.substring(index1, index2);

        List params = new ArrayList();
        params.add(databaseName);
        return new ORMException(message, cause, ERROR_CODE.DB_NOT_FOUND, params);
      } // end of if
    }
    return null;
  } // end of method getDatabaseNotFoundException
  
  /**
   * Returns an object of {@link ORMException} which represents the error caused
   * due to the invalid database user specified to login to database.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * connecting to database due to the invalid user name.
   */
  private static ORMException getInvalidDatabaseUserException(String message, Throwable cause) {
    String msgClone = message;
    if(message != null) {
      int index1 = message.indexOf(INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN1);
      int index2 = message.indexOf(INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN2);
      if(index1 > -1 && index2 > -1) {
        message = message.substring(index1 + INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN1.length());
        message = message.substring(0, index2);
        index1 = message.indexOf(INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN3);
        index1 += INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN3.length();
        index2 = message.indexOf(INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN4, index1);
        String userName = message.substring(index1, index2);
        
        index1 = index2 + INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN4.length();
        index2 = message.indexOf(INVALID_DB_USER_EXCEPTION_MESSAGE_TOKEN3, index1);
        String databaseHostName = message.substring(index1, index2);

        List params = new ArrayList();
        params.add(userName);
        params.add(databaseHostName);
        return new ORMException(msgClone, cause, ERROR_CODE.INVALID_DB_USER, params);
      } // end of if
    }
    return null;
  } // end of method getInvalidDatabaseUserException
  
  /**
   * Returns an object of {@link ORMException} which represents the error caused
   * due to the invalid database user password specified to login to database.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * connecting to database due to the invalid user password.
   */
  private static ORMException getInvalidDatabaseUserPasswordException(String message, Throwable cause) {
    String msgClone = message;
    if(message != null) {
      int index1 = message.indexOf(INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN1);
      int index2 = message.indexOf(INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN2);
      if(index1 > -1 && index2 > -1) {
        message = message.substring(index1 + INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN1.length());
        message = message.substring(0, index2);
        index1 = message.indexOf(INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN3);
        index1 += INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN3.length();
        index2 = message.indexOf(INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN4, index1);
        String userName = message.substring(index1, index2);
        
        index1 = index2 + INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN4.length();
        index2 = message.indexOf(INVALID_DB_USER_PASSWORD_EXCEPTION_MESSAGE_TOKEN3, index1);
        String databaseHostName = message.substring(index1, index2);

        List params = new ArrayList();
        params.add(userName);
        params.add(databaseHostName);
        return new ORMException(msgClone, cause, ERROR_CODE.INVALID_DB_USER_PASSWORD, params);
      } // end of if
    }
    return null;
  } // end of method getInvalidDatabaseUserPasswordException
  
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
  } // end of method getConnectionException

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
    ORMException ormException = getDriverClassNotFoundException(message, cause);
    
    if(ormException == null) {
      ormException = getDatabaseHostNotFoundException(message, cause);
    } // end of if
    if(ormException == null) {
      ormException = getDatabaseCouldNotConnectException(message, cause);
    } // end of if
    if(ormException == null) {
      ormException = getInvalidDatabaseUserException(message, cause);
    } // end of if
    if(ormException == null) {
      ormException = getInvalidDatabaseUserPasswordException(message, cause);
    } // end of if
    if(ormException == null) {
      ormException = getDatabaseNotFoundException(message, cause);
    } // end of if
    if(ormException == null && alwaysReturn) {
      ormException = new ORMException(message, cause);
    } // end of if
    return ormException;
  } // end of method getConnectionException

  /**
   * Returns an object of {@link ORMException} which represents the syntax error
   * in the SQL statement being executed in database server.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * executing a database query due to syntax error.
   */
  private static ORMException getQuerySyntaxException(String message, Throwable cause) {
    if(message != null) {
      int index = message.indexOf(INVALID_QUERY_SYNTAX_EXCEPTION_MESSAGE_TOKEN1);
      if(index > -1) {
        index += INVALID_QUERY_SYNTAX_EXCEPTION_MESSAGE_TOKEN1.length();
        int index1 = message.indexOf(INVALID_QUERY_SYNTAX_EXCEPTION_MESSAGE_TOKEN2, index);
        index1 += INVALID_QUERY_SYNTAX_EXCEPTION_MESSAGE_TOKEN2.length();
        int index2 = message.lastIndexOf(INVALID_QUERY_SYNTAX_EXCEPTION_MESSAGE_TOKEN2);
        String query = message.substring(index1, index2);
        
        List params = new ArrayList();
        params.add(query);
        return new ORMException(message, cause, ERROR_CODE.INVALID_QUERY_SYNTAX, params);
      } // end of if
    }
    return null;
  } // end of method getQuerySyntaxException
  
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
    ORMException ormException = getConnectionException(message, cause, false);
    if(ormException == null) {
      ormException = getQuerySyntaxException(message, cause);
    } // end of if
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    } // end of if
    return ormException;
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
    ORMException ormException = null;
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    } // end of if
    return ormException;
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
    ORMException ormException = getConnectionException(message, cause, false);
    if(ormException == null) {
      ormException = getQuerySyntaxException(message, cause);
    } // end of if
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    } // end of if
    return ormException;
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
    ORMException ormException = getConnectionException(message, cause, false);
    if(ormException == null) {
      ormException = getQuerySyntaxException(message, cause);
    } // end of if
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    } // end of if
    return ormException;
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
    ORMException ormException = getConnectionException(message, cause, false);
    if(ormException == null) {
      ormException = getQuerySyntaxException(message, cause);
    } // end of if
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    } // end of if
    return ormException;
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
    ORMException ormException = getConnectionException(message, cause, false);
    if(ormException == null) {
      ormException = getQuerySyntaxException(message, cause);
    } // end of if
    if(ormException == null) {
      ormException = getDeleteIntegrityConstraintException(message, cause);
    } // end of if
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    } // end of if
    return ormException;
  } // end of method getDeleteException

  /**
   * Returns an object of {@link ORMException} containing the appropriate
   * instance member values to represent the exception caught due to child rows
   * found against the row being deleted in the database server.
   * 
   * @param message Internal message of the exception.
   * @param cause {@link Throwable} which caused this exception.
   * 
   * @return An object of {@link ORMException} representing the failure in
   * delete operation in database server due to the existence of child rows of
   * the row being deleted.
   */
  private static ORMException getDeleteIntegrityConstraintException(String message, Throwable cause) {
    List params = new ArrayList();
    ERROR_CODE errorCode = null;
    if(message != null) {
      int index = message.indexOf(DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN1);
      if(index > -1) {
        index += DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN1.length();
        errorCode = ERROR_CODE.DELETE_INTEGRITY_CONSTRAINT;
        int index1 = message.indexOf(DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN2, index);
        index1 += DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN2.length();
        int index2 = message.indexOf(DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN3, index1);
        
        String entityName = message.substring(index1, index2);
        params.add(entityName);
        
        index1 = message.indexOf(DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN4, index);
        index1 += DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN4.length();
        index2 = message.indexOf(DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN5, index1);
        String attributeName = message.substring(index1, index2);
        params.add(attributeName);
        return new ORMException(message, cause, errorCode, params);
      } // end of if
    }
    return null;
  } // end of method getDeleteKeyConstraintException
  
} // end of class ExceptionUtil

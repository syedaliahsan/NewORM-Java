package com.sa.orm.pgsql.v4;

import java.util.ArrayList;
import java.util.List;

import com.sa.orm.ExceptionUtil;
import com.sa.orm.ORMException;
import com.sa.orm.ORMException.ERROR_CODE;

/**
 * <h4>Description</h4>Checks if a particular type of exception has occurred and
 * returns an {@link ORMException} object containing appropriate values.
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
  private static final String DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN2 = "\".\"";
  private static final String DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN3 = "\", CONSTRAINT";
  private static final String DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN4 = "FOREIGN KEY (\"";
  private static final String DELETE_INTEGRITY_CONSTRAINT_EXCEPTION_MESSAGE_TOKEN5 = "\") REFERENCES";
  
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
      }
    }
    return null;
  }
  
  private static ORMException getDatabaseHostNotFoundException(String message, Throwable cause) {
    if(message != null) {
      int index = message.indexOf(DB_HOST_NOT_FOUND_EXCEPTION_MESSAGE_TOKEN1);
      if(index > -1) {
        return new ORMException(message, cause, ERROR_CODE.DB_HOST_NOT_FOUND, null);
      }
    }
    return null;
  }
  
  private static ORMException getDatabaseCouldNotConnectException(String message, Throwable cause) {
    if(message != null) {
      int index = message.indexOf(DB_COULD_NOT_CONNECT_EXCEPTION_MESSAGE_TOKEN1);
      if(index > -1) {
        return new ORMException(message, cause, ERROR_CODE.DB_COULD_NOT_CONNECT, null);
      }
    }
    return null;
  }
  
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
      }
    }
    return null;
  }
  
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
      }
    }
    return null;
  }
  
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
      }
    }
    return null;
  }
  
  @Override
  public ORMException getConnectionException(String message, Throwable cause) {
    return getConnectionException(message, cause, true);
  }

  @Override
  public ORMException getConnectionException(String message, Throwable cause, boolean alwaysReturn) {
    ORMException ormException = getDriverClassNotFoundException(message, cause);
    
    if(ormException == null) {
      ormException = getDatabaseHostNotFoundException(message, cause);
    }
    if(ormException == null) {
      ormException = getDatabaseCouldNotConnectException(message, cause);
    }
    if(ormException == null) {
      ormException = getInvalidDatabaseUserException(message, cause);
    }
    if(ormException == null) {
      ormException = getInvalidDatabaseUserPasswordException(message, cause);
    }
    if(ormException == null) {
      ormException = getDatabaseNotFoundException(message, cause);
    }
    if(ormException == null && alwaysReturn) {
      ormException = new ORMException(message, cause);
    }
    return ormException;
  }

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
      }
    }
    return null;
  }
  
  @Override
  public ORMException getSelectException(String message, Throwable cause) {
    ORMException ormException = getConnectionException(message, cause, false);
    if(ormException == null) {
      ormException = getQuerySyntaxException(message, cause);
    }
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    }
    return ormException;
  }

  public static ORMException getFillObjectException(String message, Throwable cause) {
    ORMException ormException = null;
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    }
    return ormException;
  }

  @Override
  public ORMException getUpdateQueryExecutionException(String message, Throwable cause) {
    ORMException ormException = getConnectionException(message, cause, false);
    if(ormException == null) {
      ormException = getQuerySyntaxException(message, cause);
    }
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    }
    return ormException;
  }

  @Override
  public ORMException getInsertException(String message, Throwable cause) {
    ORMException ormException = getConnectionException(message, cause, false);
    if(ormException == null) {
      ormException = getQuerySyntaxException(message, cause);
    }
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    }
    return ormException;
  }

  @Override
  public ORMException getUpdateException(String message, Throwable cause) {
    ORMException ormException = getConnectionException(message, cause, false);
    if(ormException == null) {
      ormException = getQuerySyntaxException(message, cause);
    }
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    }
    return ormException;
  }

  @Override
  public ORMException getDeleteException(String message, Throwable cause) {
    ORMException ormException = getConnectionException(message, cause, false);
    if(ormException == null) {
      ormException = getQuerySyntaxException(message, cause);
    }
    if(ormException == null) {
      ormException = getDeleteIntegrityConstraintException(message, cause);
    }
    if(ormException == null) {
      ormException = new ORMException(message, cause);
    }
    return ormException;
  }

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
      }
    }
    return null;
  }
}

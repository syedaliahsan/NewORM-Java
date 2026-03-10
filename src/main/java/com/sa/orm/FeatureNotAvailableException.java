package com.sa.orm;

/**
 * <h4>Description</h4>Exception which is thrown when a feature is not supported
 * in the underlying database and the API does not support a work-around built
 * into the API.
 * <p>NOTE: It has not been defined as a checked exception and the developer
 * must ensure that cases throwing this exception are handled.</p>
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
public class FeatureNotAvailableException extends RuntimeException {
  public FeatureNotAvailableException() {
    super();
  } // end of no-argument constructor
  
  public FeatureNotAvailableException(String msg) {
    super(msg);
  } // end of constructor
  
  public FeatureNotAvailableException(Throwable throwable) {
    super(throwable);
  } // end of constructor
  
  public FeatureNotAvailableException(String msg, Throwable throwable) {
    super(msg, throwable);
  } // end of constructor
  
} // end of class FeatureNotAvailableException

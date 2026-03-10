package com.sa.orm.util;

/**
 * <h4>Description</h4>Standard exception class to be thrown when there are some
 * reflection related errors in this tool.
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
public class ReflectionException extends RuntimeException {
  public ReflectionException() {
    super();
  }
  
  public ReflectionException(String message) {
    super(message);
  }
  
  public ReflectionException(Throwable rootCause) {
    super(rootCause);
  }
  
  public ReflectionException(String message, Throwable rootCause) {
    super(message, rootCause);
  }
}

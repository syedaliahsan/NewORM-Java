package com.sa.orm.util;

import java.util.Comparator;
import java.lang.reflect.Method;

/**
 * <h4>Description</h4>General purpose comparator class which compares objects
 * of any types through reflection.
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>April 23, 2009
 * @author Ali Ahsan
 * @version 1.0
 */
public class GeneralComparator
    implements Comparator {

  private Method method = null;
  private boolean ascending = true;
  
  /**
   * Sets the instance members to be used later.
   * @param classType Object to get
   * <code>java.lang.reflect.Method</code> object to be used by
   * <code>compare</code> method later.
   * @param methodName Name of the method which will be invoked on
   * given objects in <code>compare</code> method.
   * @param ascending Whether or not comparison shall be in ascending
   * order.
   */
  public GeneralComparator(Object classType, String methodName, boolean ascending)
    throws NoSuchMethodException {
    method = classType.getClass().getMethod(methodName, new Class[0]);
    this.ascending = ascending;
  } // end of constructor
  
  /**
   * Compares its two arguments for order by calling the method, given
   * in constructor, to get the two values. Returns a negative
   * integer, zero, or a positive integer as the first argument is
   * less than, equal to, or greater than the second.
   * @param obj1 The first object to be compared.
   * @param obj2 The second object to be compared.
   */
  public int compare(Object obj1, Object obj2) {
    try {
      Object val1 = method.invoke(obj1, new Object[0]);
      Object val2 = method.invoke(obj2, new Object[0]);
      if(val1 == null && val2 == null) {
        return 0;
      } // end of if
      if(val1 == null) {
        if(ascending) {
          return 1;
        } // end of if
        return -1;
      } // end of if
      if(val2 == null) {
        if(ascending) {
          return -1;
        } // end of if
        return 1;
      } // end of if
      if(ascending) {
        return val1.toString().compareTo(val2.toString());
      } // end of if
      return val2.toString().compareTo(val1.toString());
    } // end of try
    catch(Exception ee) {
      throw new RuntimeException(ee.getMessage(), ee);
    } // end of catch
  } // end of method compare

} // end of class GeneralComparator

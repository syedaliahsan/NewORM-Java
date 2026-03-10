package com.sa.orm.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * <h4>Description</h4>Data structure to hold object and properties of an
 * instance member that may not be a database attribute.
 * <p>Business logic classes use object of this class to call the setter
 * function to populate its value if it is found in a ResultSet.</p>
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2026 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>Feb 07, 2026
 * @author Ali Ahsan
 * @version 1.1
 */
public class InstanceMember {

  private static Logger logger = Logger.getLogger(InstanceMember.class.getName());
  
  /**
   * Object whose instance member and mapped database field names are stored in
   * this object.
   */
  protected Object pojo;

  /**
   * {@link Field} object this data structure represents.
   */
  protected Field field;
  
  /**
   * Local (internal) identifier to hold setter method object.
   * 
   * @see #getSetterMethod()
   */
  protected Method setterMethod;
  
  /**
   * Local (internal) identifier to hold getter method object.
   * 
   * @see #getGetterMethod()
   */
  protected Method getterMethod;
  
  /**
   * Initializes this object with given values.
   * 
   * @param pojo Object containing this field.
   * @param field {@link Field} object representing this field.
   */
  public InstanceMember(Object pojo, Field field) {
    this.pojo = pojo;
    this.field = field;
  } // end of constructor
  
  /**
   * Returns the object containing this field.
   * 
   * @return Object containing this field.
   */
  public Object getPojo() {
    return pojo;
  }

  /**
   * Returns the {@link Field} object representing this field in the containing
   * POJO.
   * 
   * @return The {@link Field} object representing this field in the containing
   * POJO.
   */
  public Field getField() {
    return field;
  }

  /**
   * Returns name of this field in the containing POJO.
   * 
   * @return Name of this field in the containing POJO.
   */
  public String getInstanceMemberName() {
    if(this.field == null) {
      return null;
    }
    return this.field.getName();
  }

  /**
   * Returns the fully qualified name of the POJO containing this field.
   * 
   * @return Fully qualified name of the POJO containing this field.
   */
  public String getClassName() {
    if(this.pojo == null) {
      return null;
    }
    return this.pojo.getClass().getName();
  }

  /**
   * Setter {@link Method} of this field.
   * 
   * @return An object of {@link Method} which sets the value of this field.
   */
  public Method getSetterMethod() {
    if(this.pojo == null || this.field == null) {
      setterMethod = null;
      return null;
    }
    if(setterMethod != null) {
      return setterMethod;
    }
    String setterName = "set" + StringUtils.firstLetterCapital(this.getInstanceMemberName());
    try {
      setterMethod = pojo.getClass().getMethod(setterName, new Class[] {field.getType()});
    } catch(NoSuchMethodException nsme) {
      ;
    }
    return setterMethod;
  }
  
  /**
   * Getter {@link Method} of this field.
   * 
   * @return An object of {@link Method} which returns the value of this field.
   */
  public Method getGetterMethod() {
    if(this.pojo == null || this.field == null) {
      getterMethod = null;
      System.out.println("Returning null for field [" + this.getField() + "]");
      return null;
    }
    if(getterMethod != null) {
      return getterMethod;
    }
    String getterName = "get" + StringUtils.firstLetterCapital(this.getInstanceMemberName());
    try {
      getterMethod = pojo.getClass().getMethod(getterName, new Class[0]);
    } catch(NoSuchMethodException nsme) {
      logger.warning("Getter method not found with name [" + getterName + "]");
      getterName = "is" + StringUtils.firstLetterCapital(this.getInstanceMemberName());
      try {
        getterMethod = pojo.getClass().getMethod(getterName, new Class[0]);
      } catch(NoSuchMethodException nsme1) {
        logger.warning("Getter method not found with name [" + getterName + "]");
      }
    }
    return getterMethod;
  } // end of method getGetterMethod

  /**
   * Returns the instance member values formatted in a string.
   * 
   * @return Instance member values separated by tab and new line character.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("====================DBField=======================\n");
    sb.append("\tinstanceMemberName : ");
    sb.append(getInstanceMemberName());
    sb.append("\npojoClassName : ");
    sb.append(getClassName());
    sb.append("\tgetterName : ");
    sb.append(getGetterMethod().getName());
    sb.append("\n");
    return sb.toString();
  } // end of toString
} // end of class DBField

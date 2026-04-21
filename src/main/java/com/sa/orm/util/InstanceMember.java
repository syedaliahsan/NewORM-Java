package com.sa.orm.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
   * Handles standard "setFieldName" and boolean "setActive" for field "isActive".
   * * @return An object of {@link Method} which sets the value of this field.
   */
  public Method getSetterMethod() {
    if (this.pojo == null || this.field == null) {
      setterMethod = null;
      return null;
    }
    if (setterMethod != null) {
      return setterMethod;
    }

    String fieldName = this.getInstanceMemberName();
    String capitalizedName = StringUtils.firstLetterCapital(fieldName);
    
    // Default strategy: setFieldName
    String setterName = "set" + capitalizedName;

    // Boolean optimization: If field is "isActive", Lombok/JavaBeans uses "setActive"
    if (field.getType() == boolean.class || field.getType() == Boolean.class) {
      if (fieldName.startsWith("is") && fieldName.length() > 2 && Character.isUpperCase(fieldName.charAt(2))) {
        setterName = "set" + fieldName.substring(2);
      }
    }

    try {
      setterMethod = pojo.getClass().getMethod(setterName, new Class[] { field.getType() });
      logger.finer("Setter " + setterName + " found for field " + fieldName + " in " + pojo.getClass().getName());
    }
    catch (NoSuchMethodException nsme) {
      // Fallback: If "setActive" failed, try the literal "setIsActive" 
      if (setterName.startsWith("set") && !setterName.equals("set" + capitalizedName)) {
        try {
          setterName = "set" + capitalizedName;
          setterMethod = pojo.getClass().getMethod(setterName, new Class[] { field.getType() });
          logger.finer("Setter " + setterName + " found for field " + fieldName + " in " + pojo.getClass().getName());
        }
        catch (NoSuchMethodException e) {
          logger.severe("Setter not found for: " + fieldName);
        }
      }
      else {
        logger.severe(nsme.getMessage());
      }
    }
    return setterMethod;
  }

  /**
   * Getter {@link Method} of this field.
   * Handles "getFieldName", "isFieldName", and "isActive" for field "isActive".
   * * @return An object of {@link Method} which returns the value of this field.
   */
  public Method getGetterMethod() {
    if (this.pojo == null || this.field == null) {
      getterMethod = null;
      return null;
    }
    if (getterMethod != null) {
      return getterMethod;
    }

    String fieldName = this.getInstanceMemberName();
    String capitalizedName = StringUtils.firstLetterCapital(fieldName);
    Class<?> type = field.getType();

    // List of potential getter names to try in order
    List<String> candidates = new ArrayList<>();
    
    if (type == boolean.class || type == Boolean.class) {
      // 1. Try "isActive" (if field is "isActive") or "isSubscribed" (if field is "subscribed")
      if (fieldName.startsWith("is") && fieldName.length() > 2 && Character.isUpperCase(fieldName.charAt(2))) {
        candidates.add(fieldName); 
      }
      else {
        candidates.add("is" + capitalizedName);
      }
    }
    
    // 2. Try standard "getFieldName"
    candidates.add("get" + capitalizedName);

    for (String name : candidates) {
      try {
        getterMethod = pojo.getClass().getMethod(name, new Class[0]);
        if (getterMethod != null) break;
      } catch (NoSuchMethodException ignored) {
        // Continue to next candidate
      }
    }

    if (getterMethod == null) {
      logger.warning("Getter method not found for field [" + fieldName + "]");
    }
    
    return getterMethod;
  }
  
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

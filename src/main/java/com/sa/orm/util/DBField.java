package com.sa.orm.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * <h4>Description</h4>Data structure to hold object and relational world
 * properties of a field/instance member.
 * <p>Business logic classes use object of this class do all the database
 * interaction including inserting, updating, deleting and retrieving data from
 * the database.</p>
 * 
 * TODO Remove all the setter methods so that DAO classes can not make changes
 * in the objects.
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2010
 * @author Ali Ahsan
 * @version 1.1
 */
public class DBField extends InstanceMember {

  private static Logger logger = Logger.getLogger(DBField.class.getName());
  
  /**
   * Name of the column in database table for this field.
   */
  protected String dbFieldName;
  
  /**
   * Name of the table containing this field.
   */
  protected String entityName;
  
  /**
   * Name of the constraint, if specified by the developer.
   */
  protected String constraintName;

  /**
   * Initializes this object with given values.
   * 
   * @param pojo Object containing this field.
   * @param field {@link Field} object representing this field.
   * @param entityName Database name (table) of <code>pojo</code>.
   * @param dbFieldName Name of database table column of this field.
   */
  public DBField(Object pojo, Field field, String entityName, String dbFieldName) {
    this(pojo, field, entityName, dbFieldName, null);
  } // end of constructor
  
  /**
   * Initializes this object with given values.
   * 
   * @param pojo Object containing this field.
   * @param field {@link Field} object representing this field.
   * @param entityName Database name (table) of <code>pojo</code>.
   * @param dbFieldName Name of database table column of this field.
   * @param constraintName Name of the constraint in the database.
   */
  public DBField(Object pojo, Field field, String entityName, String dbFieldName, String constraintName) {
    super(pojo, field);
    this.entityName = entityName;
    this.dbFieldName = dbFieldName;
    this.constraintName = constraintName;
  } // end of constructor
  
  /**
   * Returns the name of this field in database.
   * 
   * @return Name of this field in database.
   */
  public String getDbFieldName() {
    return dbFieldName;
  }

  /**
   * Returns the name of the entity (table) containing this field (column) in
   * database.
   * 
   * @return Name of the database entity (table) contained this field (column).
   */
  public String getEntityName() {
    return entityName;
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
   * Returns name of the constraint on this field in the database.
   * <p>This is not type of constraint e.g. Foreign Key, rather the name of a
   * foreign key in the database.</p>
   * 
   * @return Name of the constraint on this field in the database.
   */
  public String getConstraintName() {
    return constraintName;
  }

  /**
   * Sets name of the constraint on this field in the database.
   * <p>This is not type of constraint e.g. Foreign Key, rather the name of a
   * foreign key in the database.</p>
   * 
   * @param constraintName Name of the constraint on this field in the database.
   */
  public void setConstraintName(String constraintName) {
    this.constraintName = constraintName;
  }
  
  /**
   * Getter {@link Method} of this field.
   * 
   * @return An object of {@link Method} which returns the value of this field.
   */
  public Method getGetterMethod() {
    if(this.pojo == null || this.field == null) {
      getterMethod = null;
      System.out.println("Returning null for entity [" + this.getEntityName() + "] and field [" + this.getField() + "]");
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
    sb.append("entityName : ");
    sb.append(entityName);
    sb.append("\tdbFieldName : ");
    sb.append(dbFieldName);
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

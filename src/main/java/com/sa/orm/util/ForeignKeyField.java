package com.sa.orm.util;

import java.lang.reflect.Field;

import com.sa.orm.reflect.annotation.AnnotationConstants;

/**
 * <h4>Description</h4>Data structure to hold object and relational world
 * properties of a foreign key field.
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2010
 * @author Ali Ahsan
 * @version 1.1
 */
public class ForeignKeyField extends DBField {

  /**
   * Name of the parent (or child) table, this (corresponding contained) object
   * is linked to.
   */
  protected String referencedEntity;
  
  /**
   * Name of the referenced field (in child or parent table).
   */
  protected String referencedField;
  
  protected int joinType = AnnotationConstants.JOIN_TYPE_INNER;
  
  protected String customJoinType;
  
  public ForeignKeyField(Object pojo, Field field, String entityName,
      String dbFieldName, String referencedEntity, String referencedField) {
    
    this(pojo, field, entityName, dbFieldName, referencedEntity, 
        referencedField, 0, null);
  } // end of constructor
  
  public ForeignKeyField(Object pojo, Field field, String entityName,
      String dbFieldName, String referencedEntity, String referencedField,
      int joinType, String customJoinType) {
    
    super(pojo, field, entityName, dbFieldName);
    this.referencedEntity = referencedEntity;
    this.referencedField = referencedField;
    this.customJoinType = customJoinType;
    this.joinType = joinType;
    if(this.joinType != AnnotationConstants.JOIN_TYPE_CUSTOM) {
      this.joinType = AnnotationConstants.JOIN_TYPE_INNER;
    }
  } // end of constructor

  public String getReferencedEntity() {
    return referencedEntity;
  }

  public void setReferencedEntity(String referencedEntity) {
    this.referencedEntity = referencedEntity;
  }

  public String getReferencedField() {
    return referencedField;
  }

  public void setReferencedField(String referencedField) {
    this.referencedField = referencedField;
  }

  public int getJoinType() {
    return joinType;
  }

  public void setJoinType(int joinType) {
    this.joinType = joinType;
  }

  public String getCustomJoinType() {
    return customJoinType;
  }

  public void setCustomJoinType(String customJoinType) {
    this.customJoinType = customJoinType;
  }
  
  /**
   * Returns the instance member values formatted in a string.
   * 
   * @return Instance member values separated by tab and new line character.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("====================ForeignKeyField=======================\n");
    sb.append(super.toString());
    sb.append("referencedEntity : ");
    sb.append(referencedEntity);
    sb.append("\treferencedField : ");
    sb.append(referencedField);
    sb.append("\tjoinType : ");
    sb.append(joinType);
    sb.append("\ncustomJoinType : ");
    sb.append(customJoinType);
    sb.append("\n");
    return sb.toString();
  } // end of toString
} // end of class ForeignKeyField

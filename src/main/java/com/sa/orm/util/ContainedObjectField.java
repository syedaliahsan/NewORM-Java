package com.sa.orm.util;

import java.lang.reflect.Field;

import com.sa.orm.ORMInfoManager;
import com.sa.orm.reflect.annotation.AnnotationConstants;

/**
 * <h4>Description</h4>Data structure to hold object and relational world
 * properties of a contained object.
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2010
 * @author Ali Ahsan
 * @version 1.1
 */
public class ContainedObjectField extends DBField {

  /**
   * Name of the parent (or child) table, this (corresponding contained) object
   * is linked to.
   */
  protected String referencedEntity;
  
  /**
   * Name of the referenced field (in child or parent table).
   */
  protected String referencedField;
  
  /**
   * Type of the contained object.
   */
  protected String containedObjectType;
  
  /**
   * Name of the instance member in the contained object which is linked to
   * <code>entity</code> and <code>referencedField</code>.
   */
  protected String relatedInstanceMemberName;

  /**
   * Relationship of of the container object with contained object.
   * <p>Valid values are {@link AnnotationConstants#RELATIONSHIP_CHILD} and
   * {@link AnnotationConstants#RELATIONSHIP_PARENT}.</p>
   */
  protected int relationshipWithContainedObject = AnnotationConstants.RELATIONSHIP_CHILD;
  
  protected int joinType = AnnotationConstants.JOIN_TYPE_INNER;
  
  protected String customJoinType;
  
  private ContainedObjectField reciprocalContainedObjectField = null;
  
  private boolean reciprocalContainedObjectFieldSearched = false;
  
  public ContainedObjectField(Object pojo, Field field, String entityName,
      String dbFieldName, String referencedEntity, String referencedField,
      String containedObjectType, String relatedInstanceMemberName,
      int relationshipWithContainedObject) {
    
    this(pojo, field, entityName, dbFieldName, referencedEntity, 
        referencedField, containedObjectType, relatedInstanceMemberName, relationshipWithContainedObject, 0, null);
  } // end of constructor
  
  public ContainedObjectField(Object pojo, Field field, String entityName,
      String dbFieldName, String referencedEntity, String referencedField,
      String containedObjectType, String relatedInstanceMemberName,
      int relationshipWithContainedObject, int joinType, String customJoinType) {
    
    super(pojo, field, entityName, dbFieldName);
    this.referencedEntity = referencedEntity;
    this.referencedField = referencedField;
    this.containedObjectType = containedObjectType;
    this.relatedInstanceMemberName = relatedInstanceMemberName;
    this.relationshipWithContainedObject = relationshipWithContainedObject;
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

  public String getContainedObjectType() {
    return containedObjectType;
  }

  public void setContainedObjectType(String containedObjectType) {
    this.containedObjectType = containedObjectType;
  }

  public String getRelatedInstanceMemberName() {
    return relatedInstanceMemberName;
  }

  public void setRelatedInstanceMemberName(String relatedInstanceMemberName) {
    this.relatedInstanceMemberName = relatedInstanceMemberName;
  }

  public int getRelationshipWithContainedObject() {
    return relationshipWithContainedObject;
  }

  public void setRelationshipWithContainedObject(
      int relationshipWithContainedObject) {
    this.relationshipWithContainedObject = relationshipWithContainedObject;
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
   * Returns reciprocal {@link ContainedObjectField} of this object.
   * 
   * @return Reciprocal {@link ContainedObjectField} of this object.
   * 
   * @see ORMInfoManager#getReciprocalContainedObjectField(ContainedObjectField)
   */
  public ContainedObjectField getReciprocalContainedObjectField() {
    if(reciprocalContainedObjectFieldSearched == false) {
      reciprocalContainedObjectField = ORMInfoManager.getReciprocalContainedObjectField(this);
      reciprocalContainedObjectFieldSearched = true;
    } // end of if
    return reciprocalContainedObjectField;
  } // end of method getReciprocalContainedObjectField
  
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
    sb.append("\trelatedInstanceMemberName : ");
    sb.append(relatedInstanceMemberName);
    sb.append("\ncontainedObjectType : ");
    sb.append(containedObjectType);
    sb.append("\trelationshipWithContainedObject : ");
    sb.append(relationshipWithContainedObject);
    sb.append("\tjoinType : ");
    sb.append(joinType);
    sb.append("\ncustomJoinType : ");
    sb.append(customJoinType);
    sb.append("\n");
    return sb.toString();
  } // end of toString
} // end of class ContainedObjectField

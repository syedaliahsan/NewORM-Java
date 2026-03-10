package com.sa.orm.reflect.annotation;

/**
 * Contains constant values to be used in conjunction with annotations.
 * 
 * 
 * <p>Copyright (c) 2002 - 2011 Soft Assets. All Rights Reserved.</p>
 * <p><h4>Company</h4></p><p>Soft Assets</p>
 * <p><h4>Date Created</h4></p><p>May 25, 2010</p>
 * 
 * @author Ali Ahsan
 * @version 0.1
 */
public class AnnotationConstants {

  /**
   * Specifies that both the current and referred fields shall have same value
   * to be included in the selection.
   */
  public static final int JOIN_TYPE_INNER = 1;
  
  /**
   * Specifies that rows matching the values in current and referred fields or
   * rows having current field value to be not <code>null</code> and referred
   * field value to be <code>null</code> are to be included in the selection.
   */
  @Deprecated
  public static final int JOIN_TYPE_LEFT_OUTER = 2;
  
  /**
   * Specifies that rows matching the values in current and referred fields or
   * rows having current field value to be <code>null</code> and referred field
   * value to be not <code>null</code> are to be included in the selection.
   */
  @Deprecated
  public static final int JOIN_TYPE_RIGHT_OUTER = 3;
  
  /**
   * Specifies that rows matching the values in current and referred fields or
   * rows having <code>null</code> value in either current or referred fields
   * are to be included in the selection.
   */
  @Deprecated
  public static final int JOIN_TYPE_FULL_OUTER = 4;
  
  /**
   * Specifies that join condition has been separately provided and that ORM
   * shall not use default join type.
   */
  public static final int JOIN_TYPE_CUSTOM = 5;
  
  public static final int RELATIONSHIP_CHILD = 1;
  
  public static final int RELATIONSHIP_PARENT = 2;
  
  
} // end of class AnnotationConstants

package com.sa.orm.reflect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used with an instance member of a POJO to specify that the
 * instance member is the unique identifier of the objects of the class
 * containing the instance member.
 * 
 * <p>Copyright (c) 2002 - 2011 Soft Assets. All Rights Reserved</p>
 * <p><h4>Company</h4></p><p>Soft Assets</p>
 * <p><h4>Date Created</h4></p><p>May 25, 2010</p>
 * 
 * @author Ali Ahsan
 * @version 1.0
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
	
  /**
   * Returns name of the constraint.
   * 
   * @return Name of the constraint.
   */
  public String constraintName() default "";
  
} // end of annotation PrimaryKey

package com.sa.orm.reflect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used to specify a unique database constraint. It has no
 * effect on query generation and execution. It is used in DDL specification
 * only.
 * <p>For a composite unique key, this annotation must be used with one of the
 * fields in composite unique fields.</p>
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
public @interface Unique {
	
  /**
   * Returns name of the constraint name.
   * 
   * @return Name of the constraint name.
   */
  public String constraintName() default "";
  
	/**
	 * Returns comma separated names of other fields which are compositely
	 * unique with this field.
	 * 
	 * @return Name of the other fields to form a unique tuple.
	 */
	public String otherFields() default "";
	
} // end of annotation Unique

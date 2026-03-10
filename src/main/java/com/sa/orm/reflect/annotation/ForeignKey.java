package com.sa.orm.reflect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used to specify a foreign key.
 * 
 * <p>Copyright (c) 2002 - 2012 Soft Assets. All Rights Reserved</p>
 * <p><h4>Company</h4></p><p>Soft Assets</p>
 * <p><h4>Date Created</h4></p><p>October 27, 2010</p>
 * 
 * @author Ali Ahsan
 * @version 1.1
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKey {
	
	/**
	 * Returns name of the underlying (parent) table whose field is the source of
	 * this foreign key relationship.
	 * 
	 * @return Name of the underlying parent table.
	 */
	public String referenceEntity();
	
	/**
	 * Returns name of the (source) field of the (parent) table.
	 * 
	 * @return Name of the (source) field of the (parent) table.
	 */
	public String referencedField();
	
  /**
   * Returns name of the foreign key constraint name.
   * 
   * @return Name of the foreign key constraint name.
   */
  public String constraintName() default "";
  
} // end of annotation Field

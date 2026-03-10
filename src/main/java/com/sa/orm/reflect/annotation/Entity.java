package com.sa.orm.reflect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used along with a POJO declaration which can be persisted.
 * 
 * <p>Copyright (c) 2002 - 2011 Soft Assets. All Rights Reserved</p>
 * <p><h4>Company</h4></p><p>Soft Assets</p>
 * <p><h4>Date Created</h4></p><p>May 25, 2010</p>
 * 
 * @author Ali Ahsan
 * @version 1.0
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {

	/**
	 * Returns Name of the corresponding entity in relational world.
	 * 
	 * @return Name of the corresponding entity in relational world.
	 */
	public String name() default "";
	
	/**
	 * Returns Name of the parent entity in relational world. This is to mimic
	 * object inheritance in relational world.
	 * 
	 * @return Name of the parent entity in relational world.
	 */
	public String inherits() default "";
	
} // end of Annotation Entity

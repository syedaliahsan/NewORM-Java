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
	 * Returns the parent entity class in relational world. This is to mimic
	 * object inheritance in relational world.
	 *
	 * @return The parent entity class in relational world.
	 */
	public Class<?> inherits() default Object.class;

	/**
	 * Returns the names of attributes from the parent entity that should be
	 * included in this entity. These are attributes that exist in the parent
	 * entity but need to be explicitly included in the child entity as well
	 * (e.g., tenantId).
	 *
	 * @return Array of attribute names to inherit from the parent entity.
	 */
	public String[] inheritedAttributes() default {};

	/**
	 * Returns whether the primary key from the parent entity should be
	 * automatically included in this entity. When true, the child entity
	 * will not declare its own primary key field; instead, it inherits
	 * the primary key from the parent (which also serves as a foreign key).
	 *
	 * @return true if the parent's primary key should be inherited, false otherwise.
	 */
	public boolean inheritPK() default false;

} // end of Annotation Entity

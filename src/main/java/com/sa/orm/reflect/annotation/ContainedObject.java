package com.sa.orm.reflect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Annotation to be used to specify an object representing an entity on either
 * side (i.e. parent or child) of a foreign key relationship.
 * 
 * <p>Copyright (c) 2002 - 2011 Soft Assets. All Rights Reserved</p>
 * <p><h4>Company</h4></p><p>Soft Assets</p>
 * <p><h4>Date Created</h4></p><p>May 25, 2010</p>
 * 
 * @author Ali Ahsan
 * @version 0.1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContainedObject {
	
	/**
	 * Returns name of the underlying (parent) table whose field is the source of
	 * this foreign key relationship.
	 * 
	 * @return Name of the underlying parent table.
	 */
	public String referencedEntity();
	
	/**
	 * Returns name of the (source) field of the (parent) table.
	 * 
	 * @return Name of the (source) field of the (parent) table.
	 */
	public String referencedField();
	
	/**
	 * Fully qualified name of the contained (parent or child) class.
	 * <p>In case the contained class represents parent or single child
	 * (one-to-one relationship), this attribute is optional, as the name can be
	 * inferred from the type of the instance member.</p>
	 * <p>In case of one-to-many relationship, it is required as the type of the
	 * instance member may be array or any {@link Collection} object.</p>
	 * 
	 * @return Fully qualified name of the contained (parent or child) class.
	 */
	public String type() default "";
	
	/**
	 * Name of the instance member (attribute) in the container object, which is
	 * associated with the field in foreign key relationship.
	 * 
	 * @return Name of the instance member in the container object.
	 */
	public String localInstanceMember() default "";

	/**
	 * Relationship of the container object with contained object.
	 * <p>Required in case the container is child of the contained object.</p>
	 * 
	 * @return Relationship of the container object with contained object.
	 */
	public int relationshipWithContainedObject() default AnnotationConstants.RELATIONSHIP_PARENT;
	
	/**
	 * Condition of the relationship between this and referenced entity. Used to
	 * infer the <code>JOIN</code> type.
	 * <p>Valid values are 1 or 5. Please see constants prefixed with
	 * <code>JOIN_TYPE</code> in {@link AnnotationConstants}</p>.
	 * 
	 * @return Type of join to be used to connect current field with referenced
	 * field.
	 * 
   * @see AnnotationConstants#JOIN_TYPE_INNER
	 * @see AnnotationConstants#JOIN_TYPE_CUSTOM
	 */
	public int joinType() default AnnotationConstants.JOIN_TYPE_INNER;

	/**
	 * Join condition if not standard.
	 * 
	 * @return Custom join condition.
	 */
	public String customJoinType() default "";
	
} // end of annotation Field

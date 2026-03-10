package com.sa.orm.reflect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used along with an instance member of a POJO to specify the
 * underlying database field.
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
public @interface Field {
	
	/**
	 * Valid values for @link {@link Field#type()}.
	 */
	public enum Type {
		FIXED_LENGTH_TEXT, VARIABLE_LENGTH_TEXT, UNLIMITED_TEXT;
	} // end of enum Type
	
	/**
	 * Returns name of the underlying database field.
	 * <p>If not given, the POJO field name is assumed to be the underlying
	 * database field name.</p>
	 * 
	 * @return Name of the underlying database field.
	 */
	public String name() default "";
	
	/**
	 * Returns <code>true</code> if the value for this field is required,
	 * <code>false</code> otherwise.
	 * <p>Required means that the field value is neither <code>null</code> nor
	 * empty (in case of string or time stamp).</p>
	 * 
	 * @return Whether or not the value for this field is required?
	 */
	public boolean required() default false;
	
	/**
	 * Returns minimum allowed number of characters in this {@link String} type
	 * field.
	 * 
	 * @return Minimum allowed number of characters in this {@link String} type
	 * field.
	 */
	public int minLength() default 0;
	
	/**
	 * Returns maximum allowed number of characters in this {@link String} type
	 * field.
	 * <p>Recommended to be provided for {@link String} type fields. In case it
	 * is not provided, unlimited size data type shall be used in the database.
	 * </p>
	 * <p>In case the {@link #type} is {@link Type#FIXED_LENGTH_TEXT}, this
	 * field is used to specify field size and a positive number is required.
	 * </p>
	 * 
	 * @return Maximum allowed number of characters in this {@link String} type
	 * field.
	 */
	public int maxLength() default 0;
	
	/**
	 * Returns minimum allowed value in this field, it is numeric or date type.
	 * 
	 * @return Minimum allowed value of this field.
	 */
	public int minValue() default 1;
	
	/**
	 * Returns maximum allowed value in this field, it is numeric or date type.
	 * 
	 * @return Maximum allowed value of this field.
	 */
	public int maxValue() default Integer.MAX_VALUE;
	
	/**
	 * Returns field type for this field if it is a {@link String}.
	 * <p>In case its value is {@link Type#FIXED_LENGTH_TEXT},
	 * {@link #maxLength()} is used to specify field size.</p>
	 * 
	 * @return Type for this field if it is a {@link String}.
	 * 
	 * @see Type
	 */
	public Type type() default Type.VARIABLE_LENGTH_TEXT;
	
	/**
	 * Returns default value of this field.
	 * 
	 * @return Default value of this field.
	 */
	public String defaultValue() default "";
	
} // end of annotation Field

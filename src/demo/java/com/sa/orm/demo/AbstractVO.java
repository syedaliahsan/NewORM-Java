package com.sa.orm.demo;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.sa.orm.util.DateUtils;
import com.sa.orm.util.StringUtils;
import com.sa.orm.util.Utility;

/**
 * Base class of all the Value Objects (VOs).
 * 
 * @author Ali Ahsan
 * @version 0.1
 */
public abstract class AbstractVO implements Serializable {

	/**
	 * Unique serial (for serialization and de-serialization) for this class.
	 */
	private static final long serialVersionUID = 20110427000000001L;
	
  /**
   * Default format to convert <code>java.sql.Timestamp</code> values
   * to String.
   */
  public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
  
  /**
   * Default format to convert <code>java.sql.Date</code> values to
   * String.
   */
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  
  /**
   * Default format to convert <code>java.sql.Time</code> values to
   * String.
   */
  public static final String TIME_FORMAT = "HH:mm:ss";

  /**
   * Line separator character to be used in <code>toString</code> and
   * <code>toXML</code> methods.
   */
  public static final String LINE_SEPARATOR = "\r\n";

  /**
   * Size of indent space to be used to indent XML tags.
   */
  public static final String INDENT_UNIT = "\t";
  
  public AbstractVO() {
    ;
  }
  
  /**
   * Returns the string containing the instance members and their
   * values separated by tab character. Every four instance members
   * are separated by a new line.
   * @return String containing instance member names and their values.
   */
  public String toString() {
    return toString(new TreeMap<String, String>());
  } // end of method toString
  
  /**
   * Returns the string containing the instance members and their
   * values separated by tab character. Every four instance members
   * are separated by a new line.
   * @param toStringMap Map object to contain name of classes that
   * have been traversed. It is used to detect and stop circular
   * execution of this method.
   * @return String containing instance member names and their values.
   */
  public String toString(Map<String, String> toStringMap) {
    if(toStringMap.containsKey(this.getClass().getName())) {
      return "";
    } // end of if
    toStringMap.put(this.getClass().getName(), "true");
    Method[] getters = this.getClass().getMethods();
    StringBuffer sb1 = new StringBuffer("");
    StringBuffer sb = new StringBuffer(LINE_SEPARATOR);
    sb.append(super.toString());
    sb.append(LINE_SEPARATOR);
    sb.append("------------------------------------------------------");
    sb.append(LINE_SEPARATOR);
    String fieldName = null;
    String value = null;
    Class returnType = null;
    Object[] array = null;
    Method method = null;
    Object obj = null;
    Collection colObj = null;
    boolean primitiveField = false;

    for (int count = -1, i = 0; i < getters.length; i++) {
      if(getters[i].getName().startsWith("get") == false && getters[i].getName().startsWith("is") == false) {
        continue;
      } // end of if
      if(getters[i].getDeclaringClass().getName().endsWith("AbstractVO")) {
        continue;
      } // end of if
      if(Modifier.isPublic(getters[i].getModifiers()) == false) {
        continue;
      } // end of if
      primitiveField = false;
      fieldName = getters[i].getName();
      if(fieldName.startsWith("get")) {
        fieldName = fieldName.substring(3);
      } // end of if
      else if(fieldName.startsWith("is")) {
        fieldName = fieldName.substring(2);
      } // end of if
      try {
        fieldName = getters[i].getName();
        if(fieldName.startsWith("get")) {
          fieldName = fieldName.substring(3);
        } // end of if
        else if(fieldName.startsWith("is")) {
          fieldName = fieldName.substring(2);
        } // end of if
        value = null;
        returnType = getters[i].getReturnType();
        if(returnType.isArray()) {
          array = (Object[])getters[i].invoke(this, new Object[0]);
          if(array != null && array.length > 0) {
            method = array[0].getClass().getMethod("toString", new Class[] {Map.class});
            for (int j = 0; j < array.length; j++) {
              value = (String)method.invoke(array[j], new Object[] {toStringMap});
            } // end of for
          } // end of if
        } // end of if
        else if(Utility.isChildClass(returnType, Collection.class)) {
          colObj = (Collection)getters[i].invoke(this, new Object[0]);
          if(colObj != null && colObj.size() > 0) {
            array = (Object[])colObj.toArray();
            method = array[0].getClass().getMethod("toString", new Class[] {Map.class});
            for (int j = 0; j < array.length; j++) {
              value = (String)method.invoke(array[j], new Object[] {toStringMap});
            } // end of for
          } // end of if
        } // end of if
        else {
          obj = getters[i].invoke(this, new Object[0]);
          if(obj != null) {
            if(AbstractVO.class.isAssignableFrom(obj.getClass())) {
              method = obj.getClass().getMethod("toString", new Class[] {Map.class});
              value = (String)method.invoke(obj, new Object[] {toStringMap});
            }
            else {
              value = format(obj.toString());
              primitiveField = true;
            }
          } // end of if
          count++;
        } // end of else

        value = StringUtils.getEmpty(value);

        if(primitiveField) {
          if(count > 0 && count % 4 == 0) {
            sb.append(LINE_SEPARATOR);
          } // end of if
          else if(count > 0) {
            sb.append("\t");
          } // end of if
          sb.append(fieldName);
          sb.append(" : ");
          sb.append(value);
        } // end of if
        else {
          sb1.append(value);
        } // end of else
      } catch(Exception ee) {}
    } // end of for
    return sb.toString() + sb1.toString();
  } // end of method toString
  
  /**
   * Returns the instance members as XML. Identifier of each instance
   * member is considered as tag in the returned XML. Name of the
   * entity represented by this class is used as root element of the
   * returned XML.
   * Each contained object's XML is indented two spaces in addition to
   * the given <code>indent</code>.
   * @param indent Spaces to be prepended before each line.
   * @param omitEmpty Whether or not to omit the empty instance
   * members from the returned XML.
   * @return XML containing name, as tags, and values, as text in
   * tags, of the instance members of this object.
   */
  public String toXML(String indent, Boolean omitEmpty) {
    return toXML(indent, omitEmpty, new TreeMap<String, String>());
  } // end of method toXML
  
  /**
   * Returns the instance members as XML. Identifier of each instance
   * member is considered as tag in the returned XML. Name of the
   * entity represented by this class is used as root element of the
   * returned XML.
   * Each contained object's XML is indented two spaces in addition to
   * the given <code>indent</code>.
   * @param indent Spaces to be prepended before each line.
   * @param omitEmpty Whether or not to omit the empty instance
   * members from the returned XML.
   * @param toXMLMap Map object to contain name of classes that have
   * been traversed. It is used to detect and stop circular execution
   * of this method.
   * @return XML containing name, as tags, and values, as text in
   * tags, of the instance members of this object.
   */
  public String toXML(String indent, Boolean omitEmpty, Map<String, String> toXMLMap) {
    if(toXMLMap.containsKey(this.getClass().getName())) {
      return "";
    } // end of if
    toXMLMap.put(this.getClass().getName(), "true");
    String entityName = this.getClass().getSimpleName();
    if(indent == null) {
      indent = "";
    } // end of if
    if(omitEmpty == null) {
      omitEmpty = false;
    } // end of if
    Method[] getters = this.getClass().getMethods();
    StringBuffer sb = new StringBuffer();
    StringBuffer sb1 = new StringBuffer("");
    StringBuffer innerSB = null;
    String fieldName = null;
    String value = null;
    Class returnType = null;
    Object obj = null;
    Object[] array = null;
    Collection colObj = null;
    Method method = null;
    boolean fieldNameFlag = true;
    boolean primitiveField = false;
    sb.append(indent);
    sb.append("<");
    sb.append(entityName);
    sb.append(">");
    sb.append(LINE_SEPARATOR);
    for (int i = 0; i < getters.length; i++) {
      if(getters[i].getName().startsWith("get") == false && getters[i].getName().startsWith("is") == false) {
        continue;
      } // end of if
      if(getters[i].getDeclaringClass().getName().equals("java.lang.Object")) {
        continue;
      } // end of if
      if(Modifier.isPublic(getters[i].getModifiers()) == false) {
        continue;
      } // end of if
      fieldNameFlag = true;
      primitiveField = false;
      fieldName = getters[i].getName();
      if(fieldName.startsWith("get")) {
        fieldName = fieldName.substring(3);
      } // end of if
      else if(fieldName.startsWith("is")) {
        fieldName = fieldName.substring(2);
      } // end of if
      value = null;
      try {
        returnType = getters[i].getReturnType();
        if(returnType.isArray()) {
          array = (Object[])getters[i].invoke(this, new Object[0]);
          if(array != null && array.length > 0) {
            innerSB = new StringBuffer();
            method = array[0].getClass().getMethod("toXML", new Class[] {String.class, Boolean.class, Map.class});
            for (int j = 0; j < array.length; j++) {
              value = (String)method.invoke(array[j], new Object[] {indent + INDENT_UNIT + INDENT_UNIT, omitEmpty, toXMLMap});
              innerSB.append(value);
            } // end of for
            value = innerSB.toString();
          } // end of if
        } // end of if
        else if(Utility.isChildClass(returnType, Collection.class)) {
          colObj = (Collection)getters[i].invoke(this, new Object[0]);
          if(colObj != null && colObj.size() > 0) {
            array = (Object[])colObj.toArray();
            innerSB = new StringBuffer();
            method = array[0].getClass().getMethod("toXML", new Class[] {String.class, Boolean.class, Map.class});
            for (int j = 0; j < array.length; j++) {
              value = (String)method.invoke(array[j], new Object[] {indent + INDENT_UNIT + INDENT_UNIT, omitEmpty, toXMLMap});
              innerSB.append(value);
            } // end of for
            value = innerSB.toString();
          } // end of if
        } // end of if
        else {
          obj = getters[i].invoke(this, new Object[0]);

          if(obj != null) {
            if(AbstractVO.class.isAssignableFrom(obj.getClass())) {
              method = obj.getClass().getMethod("toXML", new Class[] {String.class, Boolean.class, Map.class});
              value = (String)method.invoke(obj, new Object[] {indent + INDENT_UNIT + INDENT_UNIT, omitEmpty, toXMLMap});
            }
            else {
              value = format(obj);
              primitiveField = true;
            }
          } // end of if
        } // end of else
      } catch(Exception ee) {
        ee.printStackTrace();
      }

      value = StringUtils.getEmpty(value);
      if(StringUtils.getNull(value) != null || omitEmpty.booleanValue() == false) {
        if(primitiveField) {
          sb.append(indent);
          sb.append(INDENT_UNIT);
          sb.append("<");
          sb.append(fieldName);
          sb.append(">");
          if(value.endsWith(LINE_SEPARATOR)) {
            sb.append(LINE_SEPARATOR);
          } // end of if
          sb.append(StringUtils.getEmpty(value));
          if(value.endsWith(LINE_SEPARATOR)) {
            sb.append(indent);
            sb.append(INDENT_UNIT);
          } // end of if
          sb.append("</");
          sb.append(fieldName);
          sb.append(">");
          sb.append(LINE_SEPARATOR);
        } // end of if
        else {
          if(fieldNameFlag) {
            sb1.append(indent);
            sb1.append(INDENT_UNIT);
            sb1.append("<");
            sb1.append(fieldName);
            sb1.append(">");
            if(value.endsWith(LINE_SEPARATOR)) {
              sb1.append(LINE_SEPARATOR);
            } // end of if
          } // end of if
          sb1.append(value);
          if(fieldNameFlag) {
            if(value.endsWith(LINE_SEPARATOR)) {
              sb1.append(indent);
              sb1.append(INDENT_UNIT);
            } // end of if
            sb1.append("</");
            sb1.append(fieldName);
            sb1.append(">");
            sb1.append(LINE_SEPARATOR);
          } // end of if
        } // end of if
      } // end of if
    } // end of for
    sb.append(sb1.toString());
    sb.append(indent);
    sb.append("</");
    sb.append(entityName);
    sb.append(">");
    sb.append(LINE_SEPARATOR);
    return sb.toString();
  } // end of method toXML
  
  /**
   * Returns the formatted value of an instance member. For example value of a
   * {@link Timestamp} object is formatted as {@link #TIMESTAMP_FORMAT}.
   * {@link String}, {@link Integer}, {@link Short}, {@link Byte}, {@link Long},
   * {@link Float} and {@link Double} object values are not formatted.
   * 
   * @param obj Object representing value of an instance member that is to be
   * formatted.
   * 
   * @return Formatted value of the given object as string.
   */
  private String format(Object obj) {
    if(obj == null) {
      return null;
    } // end of if
    if(obj instanceof String || obj instanceof Integer || obj instanceof Short || 
        obj instanceof Byte || obj instanceof Long || obj instanceof Float || 
        obj instanceof Double) {
      return obj.toString();
    } // end of if
    if(obj instanceof Timestamp) {
      return DateUtils.getDateToString((Timestamp)obj, TIMESTAMP_FORMAT);
    } // end of if
    if(obj instanceof java.sql.Date || obj instanceof java.util.Date) {
      return DateUtils.getDateToString((java.util.Date)obj, DATE_FORMAT);
    } // end of if
    if(obj instanceof java.sql.Time) {
      return DateUtils.getDateToString((java.sql.Time)obj, TIME_FORMAT);
    } // end of if
    return obj.toString();
  } // end of method format
  
} // end of class AbstractVO

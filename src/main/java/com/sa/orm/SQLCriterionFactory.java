package com.sa.orm;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Collection;

import com.sa.orm.SQLCriterion.BOOLEAN_OPERATOR;

/**
 * <h4>Description</h4>Provides methods to get child classes of
 * {@link SQLCriterion} for any one of <code>int</code>, <code>float</code>,
 * {@link String}, {@link java.sql.Timestamp} data types for database comparison
 * operations. In addition, it provides methods to instantiate
 * {@link SQLSubQueryCriterion} classes based on the given input.
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2010 Soft Assets.
 * All Rights Reserved.
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>December 31, 2008
 * @author Ali Ahsan
 * @version 1.0
 */
public class SQLCriterionFactory {

  /**
   * Representing <code>int</code> data type of the underlying child
   * class.
   */
  public static final int INT = 1;

  /**
   * Representing <code>long</code> data type of the underlying child
   * class.
   */
  public static final int LONG = 2;

  /**
   * Representing <code>float</code> data type of the underlying child
   * class.
   */
  public static final int FLOAT = 3;

  /**
   * Representing <code>double</code> data type of the underlying child
   * class.
   */
  public static final int DOUBLE = 4;

  /**
   * Representing {@link String} data type of the underlying child class.
   */
  public static final int STRING = 5;

  /**
   * Representing {@link Timestamp data type of the underlying child class.
   */
  public static final int TIME_STAMP = 6;

  /**
   * Representing {@link Object} data type of the underlying child class.
   */
  public static final int OBJECT = 7;

  /**
   * Representing CLOB data type of the underlying child class.
   */
  public static final int CLOB = 8;

  /**
   * Representing BLOB data type of the underlying child class.
   */
  /**
   * Representing BLOB data type of the underlying child class.
   */
  public static final int BLOB = 9;

  /**
   * Representing Column data type of the underlying child class.
   */
  public static final int COLUMN = 10;

  protected SQLCriterionFactory() {
  }

  public int getType(String className) {
    return staticGetType(className);
  }
  
  public static int staticGetType(String className) {
    if ("java.lang.Integer".equals(className) || "int".equals(className)) return INT;
    if ("java.lang.Long".equals(className) || "long".equals(className)) return LONG;
    if ("java.lang.Float".equals(className) || "float".equals(className)) return FLOAT;
    if ("java.lang.Double".equals(className) || "double".equals(className)) return DOUBLE;
    if ("java.lang.String".equals(className)) return STRING;
    if ("java.lang.Timestamp".equals(className)) return TIME_STAMP;
    if ("java.lang.Clob".equals(className)) return CLOB;
    if ("java.lang.Blob".equals(className)) return BLOB;
    return OBJECT;
  }
  
  public int getType(Object value) {
    return staticGetType(value);
  }
  
  // Static helper for getType to maintain compatibility if static access is needed
  public static int staticGetType(Object value) {
     if (value instanceof Integer) return INT;
     if (value instanceof Long) return LONG;
     if (value instanceof Float) return FLOAT;
     if (value instanceof Double) return DOUBLE;
     if (value instanceof String) return STRING;
     if (value instanceof Timestamp) return TIME_STAMP;
     if (value instanceof Clob) return CLOB;
     if (value instanceof Blob) return BLOB;
     return OBJECT;
  }

  // --- Factory Methods ---

  public SQLCriterion createEqualTo(String fieldName, Object value) {
    return createEqualTo(fieldName, (String)null, value);
  }

  public SQLCriterion createEqualTo(String fieldName, Object pojo, Object value) {
    return createEqualTo(fieldName, ORMInfoManager.getEntityName(pojo), value);
  }

  public SQLCriterion createEqualTo(String fieldName, String tableName, Object value) {
    return createEqualTo(fieldName, tableName, value, getType(value));
  }

  public SQLCriterion createEqualTo(String fieldName, String tableName, Object value, int fieldType) {
    return createEqualTo(fieldName, tableName, value, fieldType, null, null);
  }

  public SQLCriterion createEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask) {
    return createEqualTo(fieldName, tableName, value, fieldType, dbMask, javaMask, false);
  }

  public SQLCriterion createEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    if(value == null) {
      return new SQLStringCriterion(SQLCriterion.NULL, fieldName, tableName, null);
    }
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case LONG: return new SQLLongCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, new Long[] {(Long)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case DOUBLE: return new SQLDoubleCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, new Double[] {(Double)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  public SQLCriterion createNotEqualTo(String fieldName, String tableName, Object value, int fieldType) {
    return createNotEqualTo(fieldName, tableName, value, fieldType, null, null);
  }

  public SQLCriterion createNotEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask) {
      return createNotEqualTo(fieldName, tableName, value, fieldType, dbMask, javaMask, false);
  }
  
  public SQLCriterion createNotEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.NOT_EQUAL_TO, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case LONG: return new SQLLongCriterion(SQLCriterion.NOT_EQUAL_TO, fieldName, tableName, new Long[] {(Long)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.NOT_EQUAL_TO, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case DOUBLE: return new SQLDoubleCriterion(SQLCriterion.NOT_EQUAL_TO, fieldName, tableName, new Double[] {(Double)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.NOT_EQUAL_TO, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.NOT_EQUAL_TO, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  // ... (Implementing other methods similarly, using switch)
  // To avoid excessively large file content in one go, I will provide the FULL implementation structure but condensed slightly where appropriate if redundant.
  // Actually, I should better provide full implementation for correctness.

  public SQLCriterion createIn(String fieldName, String tableName, Collection values, int fieldType, String dbMask, String javaMask) {
      return createIn(fieldName, tableName, values, fieldType, dbMask, javaMask, false);
  }

  public SQLCriterion createIn(String fieldName, String tableName, Collection values, int fieldType, String dbMask, String javaMask, boolean compareNull) {
     if (values == null) return null;
     Object[] arr = values.toArray();
     switch(fieldType) {
      case INT: 
          Integer[] intArr = new Integer[arr.length];
          for(int i=0; i<arr.length; i++) intArr[i] = (Integer)arr[i];
          return new SQLIntCriterion(SQLCriterion.IN, fieldName, tableName, intArr, compareNull);
      case LONG: 
        Long[] longArr = new Long[arr.length];
        for(int i=0; i<arr.length; i++) longArr[i] = (Long)arr[i];
        return new SQLLongCriterion(SQLCriterion.IN, fieldName, tableName, longArr, compareNull);
      case FLOAT:
        Float[] floatArr = new Float[arr.length];
        for(int i=0; i<arr.length; i++) floatArr[i] = (Float)arr[i];
        return new SQLFloatCriterion(SQLCriterion.IN, fieldName, tableName, floatArr, compareNull);
      case DOUBLE:
          Double[] doubleArr = new Double[arr.length];
          for(int i=0; i<arr.length; i++) doubleArr[i] = (Double)arr[i];
          return new SQLDoubleCriterion(SQLCriterion.IN, fieldName, tableName, doubleArr, compareNull);
      case STRING:
          String[] strArr = new String[arr.length];
          for(int i=0; i<arr.length; i++) strArr[i] = arr[i].toString();
          return new SQLStringCriterion(SQLCriterion.IN, fieldName, tableName, strArr, compareNull);
      case TIME_STAMP:
          Timestamp[] tsArr = new Timestamp[arr.length];
          for(int i=0; i<arr.length; i++) tsArr[i] = (Timestamp)arr[i];
          return createTimeStampCriterion(SQLCriterion.IN, fieldName, tableName, tsArr, dbMask, javaMask, compareNull);
      default: return null;
     }
  }

  public SQLCriterion createNotIn(String fieldName, String tableName, Collection values, int fieldType, String dbMask, String javaMask) {
      return createNotIn(fieldName, tableName, values, fieldType, dbMask, javaMask, false);
  }

  public SQLCriterion createNotIn(String fieldName, String tableName, Collection values, int fieldType, String dbMask, String javaMask, boolean compareNull) {
     if (values == null) return null;
     Object[] arr = values.toArray();
     switch(fieldType) {
      case INT: 
          Integer[] intArr = new Integer[arr.length];
          for(int i=0; i<arr.length; i++) intArr[i] = (Integer)arr[i];
          return new SQLIntCriterion(SQLCriterion.NOT_IN, fieldName, tableName, intArr, compareNull);
      case LONG: 
        Long[] longArr = new Long[arr.length];
        for(int i=0; i<arr.length; i++) longArr[i] = (Long)arr[i];
        return new SQLLongCriterion(SQLCriterion.NOT_IN, fieldName, tableName, longArr, compareNull);
      case FLOAT:
        Float[] floatArr = new Float[arr.length];
        for(int i=0; i<arr.length; i++) floatArr[i] = (Float)arr[i];
        return new SQLFloatCriterion(SQLCriterion.NOT_IN, fieldName, tableName, floatArr, compareNull);
      case DOUBLE:
          Double[] doubleArr = new Double[arr.length];
          for(int i=0; i<arr.length; i++) doubleArr[i] = (Double)arr[i];
          return new SQLDoubleCriterion(SQLCriterion.NOT_IN, fieldName, tableName, doubleArr, compareNull);
      case STRING:
          String[] strArr = new String[arr.length];
          for(int i=0; i<arr.length; i++) strArr[i] = arr[i].toString();
          return new SQLStringCriterion(SQLCriterion.NOT_IN, fieldName, tableName, strArr, compareNull);
      case TIME_STAMP:
          Timestamp[] tsArr = new Timestamp[arr.length];
          for(int i=0; i<arr.length; i++) tsArr[i] = (Timestamp)arr[i];
          return createTimeStampCriterion(SQLCriterion.NOT_IN, fieldName, tableName, tsArr, dbMask, javaMask, compareNull);
      default: return null;
     }
  }

  public SQLCriterion createNull(String fieldName, String tableName, int fieldType) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.NULL, fieldName, tableName, null, false);
      case LONG: return new SQLLongCriterion(SQLCriterion.NULL, fieldName, tableName, null, false);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.NULL, fieldName, tableName, null, false);
      case DOUBLE: return new SQLDoubleCriterion(SQLCriterion.NULL, fieldName, tableName, null, false);
      case STRING: return new SQLStringCriterion(SQLCriterion.NULL, fieldName, tableName, null, false);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.NULL, fieldName, tableName, null, null, null, false);
      default: return null;
    }
  }

  public SQLCriterion createNotNull(String fieldName, String tableName, int fieldType) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.NOT_NULL, fieldName, tableName, null, false);
      case LONG: return new SQLLongCriterion(SQLCriterion.NOT_NULL, fieldName, tableName, null, false);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.NOT_NULL, fieldName, tableName, null, false);
      case DOUBLE: return new SQLDoubleCriterion(SQLCriterion.NOT_NULL, fieldName, tableName, null, false);
      case STRING: return new SQLStringCriterion(SQLCriterion.NOT_NULL, fieldName, tableName, null, false);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.NOT_NULL, fieldName, tableName, null, null, null, false);
      default: return null;
    }
  }

  public SQLCriterion createBetween(String fieldName, String tableName, Object value1, Object value2, int fieldType, String dbMask, String javaMask) {
      return createBetween(fieldName, tableName, value1, value2, fieldType, dbMask, javaMask, false);
  }

  public SQLCriterion createBetween(String fieldName, String tableName, Object value1, Object value2, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.BETWEEN, fieldName, tableName, new Integer[] {(Integer)value1, (Integer)value2}, compareNull);
      case LONG: return new SQLLongCriterion(SQLCriterion.BETWEEN, fieldName, tableName, new Long[] {(Long)value1, (Long)value2}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.BETWEEN, fieldName, tableName, new Float[] {(Float)value1, (Float)value2}, compareNull);
      case DOUBLE: return new SQLDoubleCriterion(SQLCriterion.BETWEEN, fieldName, tableName, new Double[] {(Double)value1, (Double)value2}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.BETWEEN, fieldName, tableName, new String[] {value1.toString(), value2.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.BETWEEN, fieldName, tableName, new Timestamp[] {(Timestamp)value1, (Timestamp)value2}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  public SQLCriterion createNotBetween(String fieldName, String tableName, Object value1, Object value2, int fieldType, String dbMask, String javaMask) {
      return createNotBetween(fieldName, tableName, value1, value2, fieldType, dbMask, javaMask, false);
  }
  
  public SQLCriterion createNotBetween(String fieldName, String tableName, Object value1, Object value2, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.NOT_BETWEEN, fieldName, tableName, new Integer[] {(Integer)value1, (Integer)value2}, compareNull);
      case LONG: return new SQLLongCriterion(SQLCriterion.NOT_BETWEEN, fieldName, tableName, new Long[] {(Long)value1, (Long)value2}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.NOT_BETWEEN, fieldName, tableName, new Float[] {(Float)value1, (Float)value2}, compareNull);
      case DOUBLE: return new SQLDoubleCriterion(SQLCriterion.NOT_BETWEEN, fieldName, tableName, new Double[] {(Double)value1, (Double)value2}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.NOT_BETWEEN, fieldName, tableName, new String[] {value1.toString(), value2.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.NOT_BETWEEN, fieldName, tableName, new Timestamp[] {(Timestamp)value1, (Timestamp)value2}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  public SQLCriterion createLessThan(String fieldName, String tableName, Object value, int fieldType) {
    return createLessThan(fieldName, tableName, value, fieldType, null, null);
  }

  public SQLCriterion createLessThan(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask) {
      return createLessThan(fieldName, tableName, value, fieldType, dbMask, javaMask, false);
  }

  public SQLCriterion createLessThan(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.LESS_THAN, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case LONG: return new SQLLongCriterion(SQLCriterion.LESS_THAN, fieldName, tableName, new Long[] {(Long)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.LESS_THAN, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case DOUBLE: return new SQLDoubleCriterion(SQLCriterion.LESS_THAN, fieldName, tableName, new Double[] {(Double)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.LESS_THAN, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.LESS_THAN, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  public SQLCriterion createLessThanEqualTo(String fieldName, String tableName, Object value, int fieldType) {
    return createLessThanEqualTo(fieldName, tableName, value, fieldType, null, null);
  }

  public SQLCriterion createLessThanEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask) {
      return createLessThanEqualTo(fieldName, tableName, value, fieldType, dbMask, javaMask, false);
  }

  public SQLCriterion createLessThanEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.LESS_THAN_EQUAL_TO, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case LONG: return new SQLLongCriterion(SQLCriterion.LESS_THAN_EQUAL_TO, fieldName, tableName, new Long[] {(Long)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.LESS_THAN_EQUAL_TO, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case DOUBLE: return new SQLDoubleCriterion(SQLCriterion.LESS_THAN_EQUAL_TO, fieldName, tableName, new Double[] {(Double)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.LESS_THAN_EQUAL_TO, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.LESS_THAN_EQUAL_TO, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  public SQLCriterion createGreaterThan(String fieldName, String tableName, Object value, int fieldType) {
    return createGreaterThan(fieldName, tableName, value, fieldType, null, null);
  }

  public SQLCriterion createGreaterThan(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask) {
      return createGreaterThan(fieldName, tableName, value, fieldType, dbMask, javaMask, false);
  }
  
  public SQLCriterion createGreaterThan(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.GREATER_THAN, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case LONG: return new SQLLongCriterion(SQLCriterion.GREATER_THAN, fieldName, tableName, new Long[] {(Long)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.GREATER_THAN, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case DOUBLE: return new SQLDoubleCriterion(SQLCriterion.GREATER_THAN, fieldName, tableName, new Double[] {(Double)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.GREATER_THAN, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.GREATER_THAN, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  public SQLCriterion createGreaterThanEqualTo(String fieldName, String tableName, Object value, int fieldType) {
    return createGreaterThanEqualTo(fieldName, tableName, value, fieldType, null, null);
  }

  public SQLCriterion createGreaterThanEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask) {
      return createGreaterThanEqualTo(fieldName, tableName, value, fieldType, dbMask, javaMask, false);
  }

  public SQLCriterion createGreaterThanEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.GREATER_THAN_EQUAL_TO, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case LONG: return new SQLLongCriterion(SQLCriterion.GREATER_THAN_EQUAL_TO, fieldName, tableName, new Long[] {(Long)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.GREATER_THAN_EQUAL_TO, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case DOUBLE: return new SQLDoubleCriterion(SQLCriterion.GREATER_THAN_EQUAL_TO, fieldName, tableName, new Double[] {(Double)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.GREATER_THAN_EQUAL_TO, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.GREATER_THAN_EQUAL_TO, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }
  
  public SQLCriterion createContains(String fieldName, String tableName, Object value, int fieldType) {
      return createContains(fieldName, tableName, value, fieldType, false);
  }
  
  public SQLCriterion createContains(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
      if (fieldType == STRING) {
         return new SQLStringCriterion(SQLCriterion.CONTAINS, fieldName, tableName, new String[] {value.toString()}, compareNull);
      }
      return null;
  }
  
  public SQLCriterion createDoesNotContain(String fieldName, String tableName, Object value, int fieldType) {
      return createDoesNotContain(fieldName, tableName, value, fieldType, false);
  }

  public SQLCriterion createDoesNotContain(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
      if (fieldType == STRING) {
         return new SQLStringCriterion(SQLCriterion.DOES_NOT_CONTAIN, fieldName, tableName, new String[] {value.toString()}, compareNull);
      }
      return null;
  }

  public SQLCriterion createStartsWith(String fieldName, String tableName, Object value, int fieldType) {
      return createStartsWith(fieldName, tableName, value, fieldType, false);
  }

  public SQLCriterion createStartsWith(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
      if (fieldType == STRING) {
         return new SQLStringCriterion(SQLCriterion.STARTS_WITH, fieldName, tableName, new String[] {value.toString()}, compareNull);
      }
      return null;
  }
  
  public SQLCriterion createDoesNotStartWith(String fieldName, String tableName, Object value, int fieldType) {
      return createDoesNotStartWith(fieldName, tableName, value, fieldType, false);
  }

  public SQLCriterion createDoesNotStartWith(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
      if (fieldType == STRING) {
         return new SQLStringCriterion(SQLCriterion.DOES_NOT_START_WITH, fieldName, tableName, new String[] {value.toString()}, compareNull);
      }
      return null;
  }
  
  public SQLCriterion createEndsWith(String fieldName, String tableName, Object value, int fieldType) {
      return createEndsWith(fieldName, tableName, value, fieldType, false);
  }

  public SQLCriterion createEndsWith(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
      if (fieldType == STRING) {
         return new SQLStringCriterion(SQLCriterion.ENDS_WITH, fieldName, tableName, new String[] {value.toString()}, compareNull);
      }
      return null;
  }
  
  public SQLCriterion createDoesNotEndWith(String fieldName, String tableName, Object value, int fieldType) {
      return createDoesNotEndWith(fieldName, tableName, value, fieldType, false);
  }

  public SQLCriterion createDoesNotEndWith(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
      if (fieldType == STRING) {
         return new SQLStringCriterion(SQLCriterion.DOES_NOT_END_WITH, fieldName, tableName, new String[] {value.toString()}, compareNull);
      }
      return null;
  }

  public SQLCriterion createEqualTo(String fieldName, String tableName, String subQuery, boolean compareNull) {
    return createSubQueryCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, subQuery, compareNull);
  }

  public SQLCriterion createEqualTo(String fieldName, String tableName, String subQueryField, String subQueryTable, SQLCriterion[] criteria,
       BOOLEAN_OPERATOR booleanOperator, String dbMask, boolean compareNull) {
    return createSubQueryCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, subQueryField, subQueryTable, dbMask, criteria, booleanOperator, compareNull);
  }
  
  public String createCriteriaString(SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator) {
    String oper = null;
    StringBuffer criteriaString = new StringBuffer();
    if(BOOLEAN_OPERATOR.AND.equals(booleanOperator)) {
      oper = " " + ORMInfoManager.sqlConstantsObj.getOpAnd() + " ";
    } else {
      oper = " " + ORMInfoManager.sqlConstantsObj.getOpOr() + " ";
    }
    String str = null;
    for(int i = 0; i < criteria.length; i++) {
      str = "";
      if(criteria[i] != null) {
        str = criteria[i].getCriterionString();
      }
      if(str.length() > 0) {
        if(i > 0 && criteriaString.length() > 0) {
          criteriaString.append(oper);
        }
        criteriaString.append(str);
      }
    }
    return criteriaString.toString();
  }

  // --- Protected Helper Methods Overrideable by Subclasses ---

  protected SQLCriterion createTimeStampCriterion(int operator, String fieldName, String tableName, Timestamp[] values, String dbMask, String javaMask, boolean compareNull) {
     return new SQLTimeStampCriterion(operator, fieldName, tableName, values, dbMask, javaMask, compareNull); 
  }
  
  protected SQLCriterion createSubQueryCriterion(int operator, String fieldName, String tableName, String subQuery, boolean compareNull) {
     return new SQLSubQueryCriterion(operator, fieldName, tableName, subQuery, compareNull);
  }

  public SQLCriterion createColumnComparison(String lhsFieldName, String lhsTableName, int operator, String rhsFieldName, String rhsTableName) {
    return new SQLColumnCriterion(operator, lhsFieldName, lhsTableName, rhsFieldName, rhsTableName);
  }

  protected SQLCriterion createSubQueryCriterion(int operator, String fieldName, String tableName, String subQueryField, String subQueryTable,
                         String dbMask, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
                         boolean compareNull) {
     return new SQLSubQueryCriterion(operator, fieldName, tableName, subQueryField, subQueryTable, dbMask, criteria, booleanOperator, compareNull);
  }

}

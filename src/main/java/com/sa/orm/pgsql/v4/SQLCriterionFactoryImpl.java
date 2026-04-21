package com.sa.orm.pgsql.v4;

import java.sql.Timestamp;
import java.util.Collection;

import com.sa.orm.SQLCriterion;
import com.sa.orm.SQLCriterion.BOOLEAN_OPERATOR;
import com.sa.orm.SQLCriterionFactory;

/**
 * <h4>Description</h4>Provides methods to get child classes of
 * {@link SQLCriterion} for PostGreSQL database.
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2008
 * @author Ali Ahsan
 * @version 1.0
 */
public class SQLCriterionFactoryImpl extends SQLCriterionFactory {

  /**
   * Does not do anything.
   */
  public SQLCriterionFactoryImpl() {
    super();
  } // end of no-argument constructor

  @Override
  public SQLCriterion createEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case BOOLEAN: return new SQLBooleanCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, new Boolean[] {(Boolean)value}, compareNull);
      case INT: return new SQLIntCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.EQUAL_TO, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  @Override
  public SQLCriterion createNotEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case BOOLEAN: return new SQLBooleanCriterion(SQLCriterion.NOT_EQUAL_TO, fieldName, tableName, new Boolean[] {(Boolean)value}, compareNull);
      case INT: return new SQLIntCriterion(SQLCriterion.NOT_EQUAL_TO, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.NOT_EQUAL_TO, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.NOT_EQUAL_TO, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.NOT_EQUAL_TO, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  @Override
  public SQLCriterion createIn(String fieldName, String tableName, Collection values, int fieldType, String dbMask, String javaMask, boolean compareNull) {
     if (values == null) return null;
     Object[] arr = values.toArray();
     switch(fieldType) {
       case BOOLEAN: 
         Boolean[] booleanArr = new Boolean[arr.length];
         for(int i=0; i<arr.length; i++) booleanArr[i] = (Boolean)arr[i];
         return new SQLBooleanCriterion(SQLCriterion.IN, fieldName, tableName, booleanArr, compareNull);
      case INT: 
          Integer[] intArr = new Integer[arr.length];
          for(int i=0; i<arr.length; i++) intArr[i] = (Integer)arr[i];
          return new SQLIntCriterion(SQLCriterion.IN, fieldName, tableName, intArr, compareNull);
      case FLOAT:
          Float[] floatArr = new Float[arr.length];
          for(int i=0; i<arr.length; i++) floatArr[i] = (Float)arr[i];
          return new SQLFloatCriterion(SQLCriterion.IN, fieldName, tableName, floatArr, compareNull);
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

  @Override
  public SQLCriterion createNotIn(String fieldName, String tableName, Collection values, int fieldType, String dbMask, String javaMask, boolean compareNull) {
     if (values == null) return null;
     Object[] arr = values.toArray();
     switch(fieldType) {
       case BOOLEAN: 
         Boolean[] booleanArr = new Boolean[arr.length];
         for(int i=0; i<arr.length; i++) booleanArr[i] = (Boolean)arr[i];
         return new SQLBooleanCriterion(SQLCriterion.NOT_IN, fieldName, tableName, booleanArr, compareNull);
      case INT: 
          Integer[] intArr = new Integer[arr.length];
          for(int i=0; i<arr.length; i++) intArr[i] = (Integer)arr[i];
          return new SQLIntCriterion(SQLCriterion.NOT_IN, fieldName, tableName, intArr, compareNull);
      case FLOAT:
          Float[] floatArr = new Float[arr.length];
          for(int i=0; i<arr.length; i++) floatArr[i] = (Float)arr[i];
          return new SQLFloatCriterion(SQLCriterion.NOT_IN, fieldName, tableName, floatArr, compareNull);
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
      case BOOLEAN: return new SQLBooleanCriterion(SQLCriterion.NULL, fieldName, tableName, null, false);
      case INT: return new SQLIntCriterion(SQLCriterion.NULL, fieldName, tableName, null, false);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.NULL, fieldName, tableName, null, false);
      case STRING: return new SQLStringCriterion(SQLCriterion.NULL, fieldName, tableName, null, false);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.NULL, fieldName, tableName, null, null, null, false);
      default: return null;
    }
  }

  @Override
  public SQLCriterion createNotNull(String fieldName, String tableName, int fieldType) {
    switch(fieldType) {
      case BOOLEAN: return new SQLBooleanCriterion(SQLCriterion.NOT_NULL, fieldName, tableName, null, false);
      case INT: return new SQLIntCriterion(SQLCriterion.NOT_NULL, fieldName, tableName, null, false);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.NOT_NULL, fieldName, tableName, null, false);
      case STRING: return new SQLStringCriterion(SQLCriterion.NOT_NULL, fieldName, tableName, null, false);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.NOT_NULL, fieldName, tableName, null, null, null, false);
      default: return null;
    }
  }

  @Override
  public SQLCriterion createBetween(String fieldName, String tableName, Object value1, Object value2, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.BETWEEN, fieldName, tableName, new Integer[] {(Integer)value1, (Integer)value2}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.BETWEEN, fieldName, tableName, new Float[] {(Float)value1, (Float)value2}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.BETWEEN, fieldName, tableName, new String[] {value1.toString(), value2.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.BETWEEN, fieldName, tableName, new Timestamp[] {(Timestamp)value1, (Timestamp)value2}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  @Override
  public SQLCriterion createNotBetween(String fieldName, String tableName, Object value1, Object value2, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.NOT_BETWEEN, fieldName, tableName, new Integer[] {(Integer)value1, (Integer)value2}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.NOT_BETWEEN, fieldName, tableName, new Float[] {(Float)value1, (Float)value2}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.NOT_BETWEEN, fieldName, tableName, new String[] {value1.toString(), value2.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.NOT_BETWEEN, fieldName, tableName, new Timestamp[] {(Timestamp)value1, (Timestamp)value2}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  @Override
  public SQLCriterion createLessThan(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.LESS_THAN, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.LESS_THAN, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.LESS_THAN, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.LESS_THAN, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  @Override
  public SQLCriterion createLessThanEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.LESS_THAN_EQUAL_TO, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.LESS_THAN_EQUAL_TO, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.LESS_THAN_EQUAL_TO, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.LESS_THAN_EQUAL_TO, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  @Override
  public SQLCriterion createGreaterThan(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.GREATER_THAN, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.GREATER_THAN, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.GREATER_THAN, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.GREATER_THAN, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }

  @Override
  public SQLCriterion createGreaterThanEqualTo(String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask, boolean compareNull) {
    switch(fieldType) {
      case INT: return new SQLIntCriterion(SQLCriterion.GREATER_THAN_EQUAL_TO, fieldName, tableName, new Integer[] {(Integer)value}, compareNull);
      case FLOAT: return new SQLFloatCriterion(SQLCriterion.GREATER_THAN_EQUAL_TO, fieldName, tableName, new Float[] {(Float)value}, compareNull);
      case STRING: return new SQLStringCriterion(SQLCriterion.GREATER_THAN_EQUAL_TO, fieldName, tableName, new String[] {value.toString()}, compareNull);
      case TIME_STAMP: return createTimeStampCriterion(SQLCriterion.GREATER_THAN_EQUAL_TO, fieldName, tableName, new Timestamp[] {(Timestamp)value}, dbMask, javaMask, compareNull);
      default: return null;
    }
  }
  
  @Override
  public SQLCriterion createContains(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
    if (fieldType == STRING) {
     return new SQLStringCriterion(SQLCriterion.CONTAINS, fieldName, tableName, new String[] {value.toString()}, compareNull);
    }
    return null;
  }
  
  @Override
  public SQLCriterion createDoesNotContain(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
    if (fieldType == STRING) {
     return new SQLStringCriterion(SQLCriterion.DOES_NOT_CONTAIN, fieldName, tableName, new String[] {value.toString()}, compareNull);
    }
    return null;
  }

  @Override
  public SQLCriterion createStartsWith(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
    if (fieldType == STRING) {
     return new SQLStringCriterion(SQLCriterion.STARTS_WITH, fieldName, tableName, new String[] {value.toString()}, compareNull);
    }
    return null;
  }
  
  @Override
  public SQLCriterion createDoesNotStartWith(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
    if (fieldType == STRING) {
     return new SQLStringCriterion(SQLCriterion.DOES_NOT_START_WITH, fieldName, tableName, new String[] {value.toString()}, compareNull);
    }
    return null;
  }
  
  @Override
  public SQLCriterion createEndsWith(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
    if (fieldType == STRING) {
     return new SQLStringCriterion(SQLCriterion.ENDS_WITH, fieldName, tableName, new String[] {value.toString()}, compareNull);
    }
    return null;
  }
  
  @Override
  public SQLCriterion createDoesNotEndWith(String fieldName, String tableName, Object value, int fieldType, boolean compareNull) {
    if (fieldType == STRING) {
     return new SQLStringCriterion(SQLCriterion.DOES_NOT_END_WITH, fieldName, tableName, new String[] {value.toString()}, compareNull);
    }
    return null;
  }

  @Override
  protected SQLCriterion createTimeStampCriterion(int operator, String fieldName,
      String tableName, Timestamp[] values, String dbMask, String javaMask,
      boolean compareNull) {
     return new SQLTimeStampCriterion(operator, fieldName, tableName, values, dbMask, javaMask, compareNull); 
  }
  
  @Override
  protected SQLCriterion createSubQueryCriterion(int operator, String fieldName,
      String tableName, String subQuery, boolean compareNull) {
     return new SQLSubQueryCriterion(operator, fieldName, tableName, subQuery, compareNull);
  }

  @Override
  protected SQLCriterion createSubQueryCriterion(int operator, String fieldName,
      String tableName, String subQueryField, String subQueryTable,
      String dbMask, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      boolean compareNull) {
    
     return new SQLSubQueryCriterion(operator, fieldName, tableName,
         subQueryField, subQueryTable, dbMask, criteria, booleanOperator,
         compareNull);
  }

  @Override
  public SQLCriterion createColumnComparison(String lhsFieldName, String lhsTableName, int operator, String rhsFieldName, String rhsTableName) {
    return new SQLColumnCriterion(operator, lhsFieldName, lhsTableName, rhsFieldName, rhsTableName);
  }

} // end of class SQLCriterionFactoryImpl

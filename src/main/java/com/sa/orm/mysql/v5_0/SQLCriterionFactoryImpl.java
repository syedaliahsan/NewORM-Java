package com.sa.orm.mysql.v5_0;

import java.sql.Timestamp;
import com.sa.orm.SQLCriterion;
import com.sa.orm.SQLCriterion.BOOLEAN_OPERATOR;
import com.sa.orm.SQLCriterionFactory;

/**
 * <h4>Description</h4>Provides methods to get child classes of
 * {@link SQLCriterion} for any one of
 * <code>int</code>, <code>float</code>, {@link String},
 * {@link Timestamp} data types for database comparison
 * operations.
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2010 Soft Assets.
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
  protected SQLCriterion createTimeStampCriterion(int operator, String fieldName, String tableName, Timestamp[] values, String dbMask, String javaMask, boolean compareNull) {
     return new SQLTimeStampCriterion(operator, fieldName, tableName, values, dbMask, javaMask, compareNull); 
  }
  
  @Override
  protected SQLCriterion createSubQueryCriterion(int operator, String fieldName, String tableName, String subQuery, boolean compareNull) {
     return new SQLSubQueryCriterion(operator, fieldName, tableName, subQuery, compareNull);
  }

  @Override
  protected SQLCriterion createSubQueryCriterion(int operator, String fieldName, String tableName, String subQueryField, String subQueryTable,
                         String dbMask, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
                         boolean compareNull) {
     return new SQLSubQueryCriterion(operator, fieldName, tableName, subQueryField, subQueryTable, dbMask, criteria, booleanOperator, compareNull);
  }

} // end of class SQLCriterionFactoryImpl

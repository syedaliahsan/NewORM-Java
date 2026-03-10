package com.sa.orm.mysql.v5_0;

import java.sql.Timestamp;

/**
 * <h4>Description</h4>Represents int data type field criterion.
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27,2008
 * @author Ali Ahsan
 * @version 1.0
 */
public class SQLTimeStampCriterion extends com.sa.orm.SQLTimeStampCriterion {

  /**
   * Initializes the data members with the given values.
   * @param type of SQL comparison operator to be used for comparing
   * the value for the given field.
   * @param fieldName Name of table column that is to be compared 
   * against the given value.
   * @param tableName Name of table having the given field.
   * @param values Array of {@link Timestamp} objects containing values to be 
   * compared.
   * @param dbMask Mask for the date value to be used in SQL query.
   * @param javaMask Mask for the date value to be used in java.
   */
  public SQLTimeStampCriterion(int type, String fieldName, String tableName,
      Timestamp values[], String dbMask, String javaMask) {

    super(type, fieldName, tableName, values, dbMask, javaMask);
  } // end of constructor

  /**
   * Initializes the data members with the given values.
   * @param type of SQL comparison operator to be used for comparing the value
   * for the given field.
   * @param fieldName Name of table column that is to be compared against the
   * given value.
   * @param tableName Name of table having the given field.
   * @param values Array of {@link Timestamp} objects containing values to be
   * compared.
   * @param dbMask Mask for the date value to be used in SQL query.
   * @param javaMask Mask for the date value to be used in java.
   * @param compareNull Whether the SQL where clause for the given field should
   * also contain the comparison of the field with <code>null</code> values.
   */
  public SQLTimeStampCriterion(int type, String fieldName, String tableName,
      Timestamp values[], String dbMask, String javaMask, boolean compareNull) {

    super(type, fieldName, tableName, values, dbMask, javaMask, compareNull);
  } // end of constructor

} // end of class SQLTimeStampCriterion

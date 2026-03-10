package com.sa.orm.pgsql.v4;

import java.sql.Timestamp;
import com.sa.orm.util.DateUtils;
import com.sa.orm.util.StringUtils;

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
   * @param operator Comparison operator to compare the value against the given
   * field.
   * @param fieldName Name of table column that is to be compared 
   * against the given value.
   * @param tableName Name of table having the given field.
   * @param values Array of {@link Timestamp} objects containing values to be 
   * compared.
   * @param dbMask Mask for the date value to be used in SQL query.
   * @param javaMask Mask for the date value to be used in java.
   */
  public SQLTimeStampCriterion(int operator, String fieldName, String tableName,
                               Timestamp values[], String dbMask, String javaMask) {

    super(operator, fieldName, tableName, values, dbMask, javaMask);
  } // end of constructor

  /**
   * Initializes the data members with the given values.
   * @param operator Comparison operator to compare the value against the given
   * field.
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
  public SQLTimeStampCriterion(int operator, String fieldName, String tableName,
                               Timestamp values[], String dbMask, String javaMask,
                               boolean compareNull) {

    super(operator, fieldName, tableName, values, dbMask, javaMask, compareNull);
  } // end of constructor

  @Override
  protected String wrapFieldInMasks(String fieldName, String tableName, String dbMask) {
    StringBuffer str = new StringBuffer("");
    if(StringUtils.getNull(dbMask) != null) {
      str.append("(to_timestamp(to_char(");
      appendField(str);
      str.append(", '");
      str.append(dbMask);
      str.append("')");
      str.append(", '");
      str.append(dbMask);
      str.append("')");
      str.append(")"); 
    } // end of if
    else {
      appendField(str);
    } // end of else
    return str.toString();
  } // end of method wrapFieldInMasks
  
  @Override
  protected String wrapValueInMasks(Timestamp value, String dbMask, String javaMask) {
    StringBuffer str = new StringBuffer();
    String timestampValue = DateUtils.getDateToString(value, javaMask);
    if(StringUtils.getNull(dbMask) != null) {
      str.append("to_timestamp('");
      str.append(timestampValue);
      str.append("', '");
      str.append(dbMask);
      str.append("')");
    } // end of if
    else {
      str.append("'");
      str.append(timestampValue);
      str.append("'");
    } // end of else
    return str.toString();
  } // end of method wrapValueInMasks

  protected void appendField(StringBuffer criterionString) {
    criterionString.append(StringUtils.wrapDBField(tableName, fieldName, SQLConstants.FIELD_PREFIX, SQLConstants.FIELD_SUFFIX, SQLConstants.QUALIFIER_SEPARATOR));
  }
} // end of class SQLTimeStampCriterion

package com.sa.orm;

/**
 * <h4>Description</h4>Represents a criterion for comparing two database columns (fields).
 * This allows for criteria like "TableA.Column1 = TableB.Column2".
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>Feb 07, 2026
 * @author Ali Ahsan
 * @version 1.0
 */
public class SQLColumnCriterion implements SQLCriterion {

  protected int operator = 0;
  protected String lhsFieldName = null;
  protected String lhsTableName = null;
  protected String rhsFieldName = null;
  protected String rhsTableName = null;

  /**
   * Initializes the data members with the given values.
   * 
   * @param operator Comparison operator.
   * @param lhsFieldName Name of the Left Hand Side field.
   * @param lhsTableName Name of the Left Hand Side table.
   * @param rhsFieldName Name of the Right Hand Side field.
   * @param rhsTableName Name of the Right Hand Side table.
   */
  public SQLColumnCriterion(int operator, String lhsFieldName, String lhsTableName, String rhsFieldName, String rhsTableName) {
    this.operator = operator;
    this.lhsFieldName = lhsFieldName;
    this.lhsTableName = lhsTableName;
    this.rhsFieldName = rhsFieldName;
    this.rhsTableName = rhsTableName;
  }

  public String getCriterionString() {
    StringBuffer criterionString = new StringBuffer("(");
    
    appendField(criterionString, lhsTableName, lhsFieldName);
    
    criterionString.append(" ");
    switch(operator) {
      case EQUAL_TO:
        criterionString.append("=");
        break;
      case NOT_EQUAL_TO:
        criterionString.append("<>");
        break;
      case LESS_THAN:
        criterionString.append("<");
        break;
      case LESS_THAN_EQUAL_TO:
        criterionString.append("<=");
        break;
      case GREATER_THAN:
        criterionString.append(">");
        break;
      case GREATER_THAN_EQUAL_TO:
        criterionString.append(">=");
        break;
      default:
        criterionString.append("=");
        break;
    }
    criterionString.append(" ");
    
    appendField(criterionString, rhsTableName, rhsFieldName);
    
    criterionString.append(")");
    return criterionString.toString();
  }

  protected void appendField(StringBuffer buffer, String tableName, String fieldName) {
    if(tableName != null && tableName.length() > 0) {
      buffer.append(tableName);
      buffer.append(".");
    }
    buffer.append(fieldName);
  }
}

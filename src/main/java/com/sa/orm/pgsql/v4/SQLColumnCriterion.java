package com.sa.orm.pgsql.v4;

import com.sa.orm.util.StringUtils;

/**
 * <h4>Description</h4>Represents a criterion for comparing two database columns (fields) for PostgreSQL.
 * This allows for criteria like "TableA.Column1 = TableB.Column2" with proper field wrapping.
 * 
 * @author User
 */
public class SQLColumnCriterion extends com.sa.orm.SQLColumnCriterion {

  public SQLColumnCriterion(int operator, String lhsFieldName, String lhsTableName, String rhsFieldName, String rhsTableName) {
    super(operator, lhsFieldName, lhsTableName, rhsFieldName, rhsTableName);
  }

  @Override
  protected void appendField(StringBuffer buffer, String tableName, String fieldName) {
    buffer.append(StringUtils.wrapDBField(tableName, fieldName, SQLConstants.FIELD_PREFIX, SQLConstants.FIELD_SUFFIX, SQLConstants.QUALIFIER_SEPARATOR));
  }
}

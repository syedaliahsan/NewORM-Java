package com.sa.orm.pgsql.v4;

import java.text.MessageFormat;

import com.sa.orm.FeatureNotAvailableException;

/**
 * <p>Contains all the SQL related constants.</p>
 * <p>Copyright (c) 2004 Soft Assets All Rights Reserved.</p>
 * <p><h4>Company</h4></p><p>Soft Assets</p>
 * @author Ali Ahsan
 * @created August 07, 2004
 * @version 1.0
 */
public class SQLConstants implements com.sa.orm.SQLConstants {

  public static final String FMT_TIME_HH_MM_SS = "%H:%i:%s";
	  
  public static final String FMT_DATE_YYYY_MM_DD = "%Y-%m-%d";
  
  public static final String FMT_DATE_YYYY_MM_DD_HH_MM = "%Y-%m-%d %H:%i";
  
  public static final String FMT_DATE_YYYY_MM_DD_HH_MM_SS = "%Y-%m-%d %H:%i:%s";
  
  public static final String FIELD_PREFIX = "\"";

  public static final String FIELD_SUFFIX = "\"";

  public static final String FIELDS_SEPARATOR = ", ";

  public static final String ALIAS_SEPARATOR = " ";

  public static final String QUALIFIER_SEPARATOR = ".";

  public static final String QUALIFIER_SEPARATOR_REPLACED = "_";
  
  public static final String STRING_WRAPPER = "'";

  public static final String DATE_WRAPPER_PREFIX = "'";

  public static final String DATE_WRAPPER_SUFFIX = "'";

  public static final String NUMBER_WRAPPER = "";

  /**
   * SQL reserved words.
   */
  public final String DISTINCT = " Distinct ";
  
  /**
   * Constant to be used for sort by fields while calling search methods.
   */
  public static final String DESC = " Desc";

  public static final String OP_ALL = "*";

  public static final String OP_AND = "And";

  public static final String OP_OR = "Or";

  /**
   * SQL Clauses
   */
  public static final String CLAUSE_SELECT = "Select ";

  /**
   * Select clause containing just the Select keyword.
   */
  public static final String CLAUSE_SELECT_BARE = "Select ";

  public static final String CLAUSE_FROM = "From ";

  public static final String CLAUSE_WHERE = "Where ";

  public static final String CLAUSE_GROUPBY = "Group By ";

  public static final String CLAUSE_HAVING = "Having ";

  public static final String CLAUSE_INSERT_INTO = "Insert Into ";

  public static final String CLAUSE_UPDATE = "Update ";

  public static final String CLAUSE_DELETE = "Delete ";

  public static final String CLAUSE_SORTBY = "Order By ";

  public static final String CLAUSE_LIMIT = "Limit ";

  public static final String CLAUSE_LIMIT_OFFSET = " Offset ";

  public static final String SUB_CLAUSE_VALUES = "Values";

  public static final String SUB_CLAUSE_SET = "Set";

  public static final String SUB_CLAUSE_ON = "On";

  public static final String SUB_CLAUSE_JOIN = "Join";

  public static final String SUB_CLAUSE_INNER_JOIN = "Inner Join";

  public static final String SUB_CLAUSE_LEFT_OUTER_JOIN = "Left Outer Join";

  public static final String SUB_CLAUSE_RIGHT_OUTER_JOIN = "Right Outer Join";

  /**
   * Not Supported in MySQL as of now.
   */
  public static final String SUB_CLAUSE_OUTER_JOIN = "Outer Join";

  public static final String OP_EQUAL = "=";

  public static final String OP_NOT_EQUAL = "<>";

  public static final String OP_LESS_THAN = "<";

  public static final String OP_LESS_THAN_EQUAL = "<=";

  public static final String OP_GREATER_THAN = ">";

  public static final String OP_GREATER_THAN_EQUAL = ">=";

  public static final String OP_NULL = "Null";

  public static final String OP_NOT_NULL = "Not Null";

  public static final String OP_BETWEEN = "Between";

  public static final String OP_IN = "In ";

  public static final MessageFormat QRY_SELECT_QUERY = new MessageFormat("{0} {1} {2} {3} {4}");

  public static final MessageFormat QRY_GROUPBY_QUERY = new MessageFormat("{0} {1} {2} {3} {4} {5}");

  public static final MessageFormat QRY_DELETE_QUERY = new MessageFormat("{0} {1}");

  public static final MessageFormat QRY_DELETE_QUERY_WITH_RETURNING = new MessageFormat("{0} {1} RETURNING *");

  public static final MessageFormat QRY_INSERT_QUERY = new MessageFormat(CLAUSE_INSERT_INTO + "{0} " + "({1}) " + SUB_CLAUSE_VALUES + " ({2});");

  public static final MessageFormat QRY_INSERT_QUERY_WITH_RETURNING = new MessageFormat(CLAUSE_INSERT_INTO + "{0} " + "({1}) " + SUB_CLAUSE_VALUES + " ({2}) RETURNING *;");

  public static final MessageFormat QRY_UPDATE_QUERY = new MessageFormat(CLAUSE_UPDATE + "{0} " + SUB_CLAUSE_SET + " {1} {2}");

  public static final MessageFormat QRY_UPDATE_QUERY_WITH_RETURNING = new MessageFormat(CLAUSE_UPDATE + "{0} " + SUB_CLAUSE_SET + " {1} {2} RETURNING *");

  public static final MessageFormat QRY_SELECT = new MessageFormat(CLAUSE_SELECT + "{0}");

  public static final MessageFormat QRY_GROUPBY = new MessageFormat(CLAUSE_GROUPBY + "{0}");

  public static final MessageFormat QRY_FROM = new MessageFormat(CLAUSE_FROM + "{0}");

  public static final MessageFormat QRY_WHERE = new MessageFormat(CLAUSE_WHERE + "{0}");

  public static final MessageFormat QRY_DELETE = new MessageFormat(CLAUSE_DELETE + CLAUSE_FROM + "{0}");

  public static final MessageFormat QRY_SINGLE_WHERE = new MessageFormat(CLAUSE_WHERE + "{0} {1} {2}");

  public static final MessageFormat QRY_JOIN = new MessageFormat("({0} {1} {2} " + SUB_CLAUSE_ON + " {3})");

  public static final MessageFormat QRY_INNER_JOIN = new MessageFormat(CLAUSE_FROM + "({0} " + SUB_CLAUSE_INNER_JOIN + " {1} " + SUB_CLAUSE_ON + " {2} = {3})");

  public static final MessageFormat QRY_SINGLE_SELECT = new MessageFormat(CLAUSE_SELECT + "{0} " + CLAUSE_FROM + "{1} " + CLAUSE_WHERE + "{2}");

  public static final MessageFormat QRY_SORTBY = new MessageFormat(CLAUSE_SORTBY + "{0}");

  public static final MessageFormat QRY_LIMIT1 = new MessageFormat(CLAUSE_LIMIT + "{0}");

  public static final MessageFormat QRY_LIMIT2 = new MessageFormat(CLAUSE_LIMIT + "{0}" + CLAUSE_LIMIT_OFFSET + "{1}");

  public static final MessageFormat SPACES = new MessageFormat("{0} {1} {2} {3} {4} {5} {6} {7} {8} {9}");

  /**
   * @return Returns the ALIAS_SEPARATOR.
   */
  public String getAliasSeparator() {
    return ALIAS_SEPARATOR;
  }

  /**
   * @return Returns the CLAUSE_DELETE.
   */
  public String getClauseDelete() {
    return CLAUSE_DELETE;
  }
  
  /**
   * @return Returns the CLAUSE_FROM.
   */
  public String getClauseFrom() {
    return CLAUSE_FROM;
  }
  
  /**
   * @return Returns the CLAUSE_GROUPBY.
   */
  public String getClauseGroupBy() {
    return CLAUSE_GROUPBY;
  }
  
  /**
   * @return Returns the CLAUSE_HAVING.
   */
  public String getClauseHaving() {
    return CLAUSE_HAVING;
  }
  
  /**
   * @return Returns the CLAUSE_INSERT_INTO.
   */
  public String getClauseInsertInto() {
    return CLAUSE_INSERT_INTO;
  }
  
  /**
   * @return Returns the CLAUSE_LIMIT.
   */
  public String getClauseLimit() {
    return CLAUSE_LIMIT;
  }
  
  /**
   * @return Returns the CLAUSE_SELECT.
   */
  public String getClauseSelect() {
    return CLAUSE_SELECT;
  }
  
  /**
   * @return Returns the CLAUSE_SORTBY.
   */
  public String getClauseSortBy() {
    return CLAUSE_SORTBY;
  }
  
  /**
   * @return Returns the CLAUSE_UPDATE.
   */
  public String getClauseUpdate() {
    return CLAUSE_UPDATE;
  }
  
  /**
   * @return Returns the CLAUSE_WHERE.
   */
  public String getClauseWhere() {
    return CLAUSE_WHERE;
  }
  
  /**
   * @return Returns the DATE_WRAPPER_PREFIX.
   */
  public String getDateWrapperPrefix() {
    return DATE_WRAPPER_PREFIX;
  }

  /**
   * @return Returns the DATE_WRAPPER_SUFFIX.
   */
  public String getDateWrapperSuffix() {
    return DATE_WRAPPER_SUFFIX;
  }
  
  /**
   * @return Returns the DESC.
   */
  public String getDesc() {
    return DESC;
  }
  
  /**
   * @return Returns the FIELD_PREFIX.
   */
  public String getFieldPrefix() {
    return FIELD_PREFIX;
  }
  
  /**
   * @return Returns the FIELD_SUFFIX.
   */
  public String getFieldSuffix() {
    return FIELD_SUFFIX;
  }
  
  /**
   * @return Returns the FIELDS_SEPARATOR.
   */
  public String getFieldsSeparator() {
    return FIELDS_SEPARATOR;
  }
  
  /**
   * @return Returns the FMT_TIME_HH_MM_SS.
   */
  public String getFmt_Time_HH_MM_SS() {
	  return FMT_TIME_HH_MM_SS;
  }

  /**
   * @return Returns the FMT_DATE_YYYY_MM_DD.
   */
  public String getFmt_Date_YYYY_MM_DD() {
    return FMT_DATE_YYYY_MM_DD;
  }

  /**
   * @return Returns the FMT_DATE_YYYY_MM_DD_HH_MM.
   */
  public String getFmt_Date_YYYY_MM_DD_HH_MM() {
    return FMT_DATE_YYYY_MM_DD_HH_MM;
  }
  
  /**
   * @return Returns the FMT_DATE_YYYY_MM_DD_HH_MM_SS.
   */
  public String getFmt_Date_YYYY_MM_DD_HH_MM_SS() {
    return FMT_DATE_YYYY_MM_DD_HH_MM_SS;
  }
  
  /**
   * @return Returns the NUMBER_WRAPPER.
   */
  public String getNumberWrapper() {
    return NUMBER_WRAPPER;
  }
  
  /**
   * @return Returns the DISTINCT.
   */
  public String getDistinct() {
    return DISTINCT;
  } // end of method getDistinct
  
  /**
   * @return Returns the OP_ALL.
   */
  public String getOpAll() {
    return OP_ALL;
  }
  
  /**
   * @return Returns the OP_AND.
   */
  public String getOpAnd() {
    return OP_AND;
  }
  
  /**
   * @return Returns the OP_BETWEEN.
   */
  public String getOpBetween() {
    return OP_BETWEEN;
  }
  
  /**
   * @return Returns the OP_EQUAL.
   */
  public String getOpEqual() {
    return OP_EQUAL;
  }
  
  /**
   * @return Returns the OP_GREATER_THAN.
   */
  public String getOpGreaterThan() {
    return OP_GREATER_THAN;
  }

  /**
   * @return Returns the OP_GREATER_THAN_EQUAL.
   */
  public String getOpGreaterThanEqual() {
    return OP_GREATER_THAN_EQUAL;
  }
  
  /**
   * @return Returns the OP_IN.
   */
  public String getOpIn() {
    return OP_IN;
  }
  
  /**
   * @return Returns the OP_LESS_THAN.
   */
  public String getOpLessThan() {
    return OP_LESS_THAN;
  }
  
  /**
   * @return Returns the OP_LESS_THAN_EQUAL.
   */
  public String getOpLessThanEqual() {
    return OP_LESS_THAN_EQUAL;
  }
  
  /**
   * @return Returns the OP_NOT_EQUAL.
   */
  public String getOpNotEqual() {
    return OP_NOT_EQUAL;
  }
  
  /**
   * @return Returns the OP_NOT_NULL.
   */
  public String getOpNotNull() {
    return OP_NOT_NULL;
  }
  
  /**
   * @return Returns the OP_NULL.
   */
  public String getOpNull() {
    return OP_NULL;
  }
  
  /**
   * @return Returns the OP_OR.
   */
  public String getOpOr() {
    return OP_OR;
  }
  
  /**
   * @return Returns the QRY_DELETE.
   */
  public MessageFormat getQryDelete() {
    return QRY_DELETE;
  }
  
  /**
   * @return Returns the QRY_DELETE_QUERY.
   */
  public MessageFormat getQryDeleteQuery() {
    return QRY_DELETE_QUERY;
  }
  
  /**
   * @return Returns the QRY_FROM.
   */
  public MessageFormat getQryFrom() {
    return QRY_FROM;
  }
  
  /**
   * @return Returns the QRY_GROUPBY.
   */
  public MessageFormat getQryGroupBy() {
    return QRY_GROUPBY;
  }
  
  /**
   * @return Returns the QRY_GROUPBY_QUERY.
   */
  public MessageFormat getQryGroupByQuery() {
    return QRY_GROUPBY_QUERY;
  }
  
  /**
   * @return Returns the QRY_INNER_JOIN.
   */
  public MessageFormat getQryInnerJoin() {
    return QRY_INNER_JOIN;
  }
  
  /**
   * @return Returns the QRY_INSERT_QUERY.
   */
  public MessageFormat getQryInsertQuery() {
    return QRY_INSERT_QUERY;
  }
  
  /**
   * @return Returns the QRY_LIMIT1.
   */
  public MessageFormat getQryLimit1() {
    return QRY_LIMIT1;
  }
  
  /**
   * @return Returns the QRY_LIMIT2.
   */
  public MessageFormat getQryLimit2() {
    return QRY_LIMIT2;
  }
  
  /**
   * @return Returns the QRY_SELECT.
   */
  public MessageFormat getQrySelect() {
    return QRY_SELECT;
  }
  
  /**
   * @return Returns the QRY_SELECT_QUERY.
   */
  public MessageFormat getQrySelectQuery() {
    return QRY_SELECT_QUERY;
  }
  
  /**
   * @return Returns the QRY_SINGLE_SELECT.
   */
  public MessageFormat getQrySingleSelect() {
    return QRY_SINGLE_SELECT;
  }
  
  /**
   * @return Returns the QRY_SINGLE_WHERE.
   */
  public MessageFormat getQrySingleWhere() {
    return QRY_SINGLE_WHERE;
  }
  
  /**
   * @return Returns the QRY_SORTBY.
   */
  public MessageFormat getQrySortBy() {
    return QRY_SORTBY;
  }
  
  /**
   * @return Returns the QRY_UPDATE_QUERY.
   */
  public MessageFormat getQryUpdateQuery() {
    return QRY_UPDATE_QUERY;
  }
  
  /**
   * @return Returns the QRY_WHERE.
   */
  public MessageFormat getQryWhere() {
    return QRY_WHERE;
  }
  
  /**
   * @return Returns the QUALIFIER_SEPARATOR.
   */
  public String getQualifierSeparator() {
    return QUALIFIER_SEPARATOR;
  }
  
  /**
   * @return Returns the QUALIFIER_SEPARATOR_REPLACED.
   */
  public String getQualifierSeparatorReplaced() {
    return QUALIFIER_SEPARATOR_REPLACED;
  }
  
  /**
   * @return Returns the SPACES.
   */
  public MessageFormat getSpaces() {
    return SPACES;
  }
  
  /**
   * @return Returns the STRING_WRAPPER.
   */
  public String getStringWrapper() {
    return STRING_WRAPPER;
  }
  
  /**
   * @return Returns the SUB_CLAUSE_INNER_JOIN.
   */
  public String getSubClauseInnerJoin() {
    return SUB_CLAUSE_INNER_JOIN;
  }
  
  /**
   * @return Returns the SUB_CLAUSE_ON.
   */
  public String getSubClauseOn() {
    return SUB_CLAUSE_ON;
  }
  
  /**
   * @return Returns the SUB_CLAUSE_SET.
   */
  public String getSubClauseSet() {
    return SUB_CLAUSE_SET;
  }
  
  /**
   * @return Returns the SUB_CLAUSE_VALUES.
   */
  public String getSubClauseValues() {
    return SUB_CLAUSE_VALUES;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MessageFormat getQryJoin() {
    return QRY_JOIN;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSubClauseLeftOuterJoin() {
    return SUB_CLAUSE_LEFT_OUTER_JOIN;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSubClauseOuterJoin() {
    throw new FeatureNotAvailableException();
    //return SUB_CLAUSE_OUTER_JOIN;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSubClauseRightOuterJoin() {
    return SUB_CLAUSE_RIGHT_OUTER_JOIN;
  }

} // end of class SQLConstants

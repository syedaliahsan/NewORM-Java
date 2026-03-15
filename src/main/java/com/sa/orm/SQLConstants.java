package com.sa.orm;

import java.text.MessageFormat;

/**
 * <h4>Description</h4>Defines getter methods for SQL related
 * constants.
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2009 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>January 01, 2009
 * @author Ali Ahsan
 * @version 1.0
 */
public interface SQLConstants {

  /**
   * @return Returns the ALIAS_SEPARATOR.
   */
  public String getAliasSeparator();

  /**
   * @return Returns the CLAUSE_DELETE.
   */
  public String getClauseDelete();

  /**
   * @return Returns the CLAUSE_FROM.
   */
  public String getClauseFrom();

  /**
   * @return Returns the CLAUSE_GROUPBY.
   */
  public String getClauseGroupBy();

  /**
   * @return Returns the CLAUSE_HAVING.
   */
  public String getClauseHaving();

  /**
   * @return Returns the CLAUSE_INSERT_INTO.
   */
  public String getClauseInsertInto();

  /**
   * @return Returns the CLAUSE_LIMIT.
   */
  public String getClauseLimit();

  /**
   * @return Returns the CLAUSE_SELECT.
   */
  public String getClauseSelect();

  /**
   * @return Returns the CLAUSE_SORTBY.
   */
  public String getClauseSortBy();

  /**
   * @return Returns the CLAUSE_UPDATE.
   */
  public String getClauseUpdate();

  /**
   * @return Returns the CLAUSE_WHERE.
   */
  public String getClauseWhere();

  /**
   * @return Returns the DATE_WRAPPER_PREFIX.
   */
  public String getDateWrapperPrefix();

  /**
   * @return Returns the DATE_WRAPPER_SUFFIX.
   */
  public String getDateWrapperSuffix();

  /**
   * @return Returns the DESC.
   */
  public String getDesc();

  /**
   * @return Returns the FIELD_PREFIX.
   */
  public String getFieldPrefix();

  /**
   * @return Returns the FIELD_SUFFIX.
   */
  public String getFieldSuffix();

  /**
   * @return Returns the FIELDS_SEPARATOR.
   */
  public String getFieldsSeparator();

  /**
   * @return Returns the FMT_TIME_YYYY_MM_DD_HH_MM_SS.
   */
  public String getFmt_Time_HH_MM_SS();

  /**
   * @return Returns the FMT_DATE_YYYY_MM_DD.
   */
  public String getFmt_Date_YYYY_MM_DD();

  /**
   * @return Returns the FMT_DATE_YYYY_MM_DD_HH_MM.
   */
  public String getFmt_Date_YYYY_MM_DD_HH_MM();

  /**
   * @return Returns the FMT_DATE_YYYY_MM_DD_HH_MM_SS.
   */
  public String getFmt_Date_YYYY_MM_DD_HH_MM_SS();

  /**
   * @return Returns the NUMBER_WRAPPER.
   */
  public String getNumberWrapper();

  /**
   * @return Returns the DISTINCT.
   */
  public String getDistinct();

  /**
   * @return Returns the OP_ALL.
   */
  public String getOpAll();

  /**
   * @return Returns the OP_AND.
   */
  public String getOpAnd();

  /**
   * @return Returns the OP_BETWEEN.
   */
  public String getOpBetween();

  /**
   * @return Returns the OP_EQUAL.
   */
  public String getOpEqual();

  /**
   * @return Returns the OP_GREATER_THAN.
   */
  public String getOpGreaterThan();

  /**
   * @return Returns the OP_GREATER_THAN_EQUAL.
   */
  public String getOpGreaterThanEqual();

  /**
   * @return Returns the OP_IN.
   */
  public String getOpIn();

  /**
   * @return Returns the OP_LESS_THAN.
   */
  public String getOpLessThan();

  /**
   * @return Returns the OP_LESS_THAN_EQUAL.
   */
  public String getOpLessThanEqual();

  /**
   * @return Returns the OP_NOT_EQUAL.
   */
  public String getOpNotEqual();

  /**
   * @return Returns the OP_NOT_NULL.
   */
  public String getOpNotNull();

  /**
   * @return Returns the OP_NULL.
   */
  public String getOpNull();

  /**
   * @return Returns the OP_OR.
   */
  public String getOpOr();

  /**
   * @return Returns the QRY_DELETE.
   */
  public MessageFormat getQryDelete();

  /**
   * @return Returns the QRY_DELETE_QUERY.
   */
  public MessageFormat getQryDeleteQuery();

  /**
   * @return Returns the QRY_FROM.
   */
  public MessageFormat getQryFrom();

  /**
   * @return Returns the QRY_GROUPBY.
   */
  public MessageFormat getQryGroupBy();

  /**
   * @return Returns the QRY_GROUPBY_QUERY.
   */
  public MessageFormat getQryGroupByQuery();

  /**
   * @return Returns the QRY_INNER_JOIN.
   */
  public MessageFormat getQryInnerJoin();

  /**
   * Returns format of <code>Join</code>. It requires following values in the
   * specified order:
   * <ul><li>Left side of the join</li>
   * <li>Join Type (either of {@link #getSubClauseInnerJoin()},
   * {@link #getSubClauseOuterJoin()}, {@link #getSubClauseLeftOuterJoin()} or
   * {@link #getSubClauseRightOuterJoin()}.</li>
   * <li>Right side of the join.</li>
   * <li>Condition to be specified to join the two sides.</li>
   * </ul>
   * 
   * @return Returns format of <code>Join</code>.
   */
  public MessageFormat getQryJoin();
  
  /**
   * @return Returns the QRY_INSERT_QUERY.
   */
  public MessageFormat getQryInsertQuery();

  /**
   * @return Returns the QRY_LIMIT1.
   */
  public MessageFormat getQryLimit1();

  /**
   * @return Returns the QRY_LIMIT2.
   */
  public MessageFormat getQryLimit2();

  /**
   * @return Returns the QRY_SELECT.
   */
  public MessageFormat getQrySelect();

  /**
   * @return Returns the QRY_SELECT_QUERY.
   */
  public MessageFormat getQrySelectQuery();

  /**
   * @return Returns the QRY_SINGLE_SELECT.
   */
  public MessageFormat getQrySingleSelect();

  /**
   * @return Returns the QRY_SINGLE_WHERE.
   */
  public MessageFormat getQrySingleWhere();

  /**
   * @return Returns the QRY_SORTBY.
   */
  public MessageFormat getQrySortBy();

  /**
   * @return Returns the QRY_UPDATE_QUERY.
   */
  public MessageFormat getQryUpdateQuery();

  /**
   * @return Returns the QRY_WHERE.
   */
  public MessageFormat getQryWhere();

  /**
   * @return Returns the QUALIFIER_SEPARATOR.
   */
  public String getQualifierSeparator();

  /**
   * @return Returns the QUALIFIER_SEPARATOR_REPLACED.
   */
  public String getQualifierSeparatorReplaced();

  /**
   * @return Returns the SPACES.
   */
  public MessageFormat getSpaces();

  /**
   * @return Returns the STRING_WRAPPER.
   */
  public String getStringWrapper();

  /**
   * @return Returns the SUB_CLAUSE_INNER_JOIN.
   */
  public String getSubClauseInnerJoin();

  /**
   * @return Returns the SUB_CLAUSE_OUTER_JOIN.
   */
  public String getSubClauseOuterJoin();

  /**
   * @return Returns the SUB_CLAUSE_LEFT_OUTER_JOIN.
   */
  public String getSubClauseLeftOuterJoin();

  /**
   * @return Returns the SUB_CLAUSE_RIGHTOUTER_JOIN.
   */
  public String getSubClauseRightOuterJoin();

  /**
   * @return Returns the SUB_CLAUSE_ON.
   */
  public String getSubClauseOn();

  /**
   * @return Returns the SUB_CLAUSE_SET.
   */
  public String getSubClauseSet();

  /**
   * @return Returns the SUB_CLAUSE_VALUES.
   */
  public String getSubClauseValues();

  /**
   * @return Returns the CLAUSE_UNION.
   */
  public String getClauseUnion();

} // end of interface SQLConstants

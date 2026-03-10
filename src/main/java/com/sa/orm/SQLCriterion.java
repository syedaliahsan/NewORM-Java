package com.sa.orm;

/**
 * <h4>Description</h4>Defines skeleton for SQL Statement Criteria classes as
 * well as constants.
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2011 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2008
 * @author Ali Ahsan
 * @version 1.0
 */
public interface SQLCriterion {

  /**
   * Constants value for database operator 'equal to'.
   */
  public static final int EQUAL_TO = 1;

  /**
   * Constants value for database operator 'not equal to'.
   */
  public static final int NOT_EQUAL_TO = 2;

  /**
   * Constants value for database operator 'between'.
   */
  public static final int BETWEEN = 3;

  /**
   * Constants value for database operator 'not between'.
   */
  public static final int NOT_BETWEEN = 4;

  /**
   * Constants value for database operator 'less than'.
   */
  public static final int LESS_THAN = 5;

  /**
   * Constants value for database operator 'less than equal to'.
   */
  public static final int LESS_THAN_EQUAL_TO = 6;

  /**
   * Constants value for database operator 'greater than'.
   */
  public static final int GREATER_THAN = 7;

  /**
   * Constants value for database operator 'greater than equal to'.
   */
  public static final int GREATER_THAN_EQUAL_TO = 8;

  /**
   * Constants value for database operator 'is null'.
   */
  public static final int NULL = 9;

  /**
   * Constants value for database operator 'is not null'.
   */
  public static final int NOT_NULL = 10;

  /**
   * Constants value for database operator 'in given list'.
   */
  public static final int IN = 11;

  /**
   * Constants value for database operator 'not in given list'.
   */
  public static final int NOT_IN = 12;

  /**
   * Constants value for database operator 'contains'.
   */
  public static final int CONTAINS = 13;

  /**
   * Constants value for database operator 'does not contain'.
   */
  public static final int DOES_NOT_CONTAIN = 14;

  /**
   * Constants value for database operator 'starts with'.
   */
  public static final int STARTS_WITH = 15;

  /**
   * Constants value for database operator 'does not start with'.
   */
  public static final int DOES_NOT_START_WITH = 16;

  /**
   * Constants value for database operator 'ends with'.
   */
  public static final int ENDS_WITH = 17;

  /**
   * Constants value for database operator 'does not end with'.
   */
  public static final int DOES_NOT_END_WITH = 18;

  /**
   * Constants value for boolean operator 'And'.
   */
  public static enum BOOLEAN_OPERATOR {
    AND, OR;
  };

  /**
   * Returns string that can be used as criteria in <code>WHERE</code> clause of
   * an SQL statement.
   * 
   * @return criteria String to be used in <code>WHERE</code> clause
   * of an SQL statement.
   */
  public String getCriterionString();

} // end of interface SQLCriterion

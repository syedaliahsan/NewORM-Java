package com.sa.orm;

import com.sa.orm.util.StringUtils;

/**
 * <h4>Description</h4>Represents functions to be used in queries.
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2009 Soft Assets.
 * All Rights Reserved.
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>January 01, 2009
 * @author Ali Ahsan
 * @version 1.0
 */
public class SQLFunction implements SQLCriterion {

  protected String function = null;
  
  protected String[] params;
  
  protected String alias = null;
  
  protected boolean cummulative = false;
  
  /**
   * Sets the given parameter values as instance member values.
   * @param function SQL fuction this object is representing.
   * @param params Parameters of function e.g. fieldName or fieldName
   * and a literal value etc.
   * @param cummulative Whether the function is cummulative or not.
   * Cummulative functions can only be used in Group By queries where
   * as others can be used in any type of query.
   */
  public SQLFunction(String function, String[] params, boolean cummulative) {
    this.function = function;
    this.params = params;
    this.cummulative = cummulative;
  } // end of no-argument constructor
  
  /**
   * Sets the given parameter values as instance member values.
   * @param function SQL fuction this object is representing.
   * @param params Parameters of function e.g. fieldName or fieldName
   * and a literal value etc.
   * @param alias Alias of the function column.
   * @param cummulative Whether the function is cummulative or not.
   * Cummulative functions can only be used in Group By queries where
   * as others can be used in any type of query.
   */
  public SQLFunction(String function, String[] params, String alias, boolean cummulative) {
    this.function = function;
    this.params = params;
    this.alias = alias;
    this.cummulative = cummulative;
  } // end of no-argument constructor
  
  /**
   * Returns <code>true</code> if this object represents a cummulative
   * function. Otherwise it returns <code>false</code>.
   * @return <code>true</code> if cummulative function, otherwise
   * <code>false</code>.
   */
  public boolean isCummulative() {
    return this.cummulative;
  } // end of method isCummulative

  /**
   * Returns a string that can be used in an SQL query representing a
   * function in <code>Select</code> clause of query.
   * @return String to be used as part of an SQL query.
   */
  public String getCriterionString() {
    return getCriterionString(false);
  } // end of method getCriterionString
  
  /**
   * Returns a string that can be used in an SQL query representing a
   * function in <code>Select</code> clause of query.
   * @param includeAlias Whether or not to include alias, if given, in
   * the returned string.
   * @return String to be used as part of an SQL query.
   */
  public String getCriterionString(boolean includeAlias) {
    String sql = function + "(" + StringUtils.stringArrayToString(params, ", ") + ")";
    if(includeAlias && alias != null && alias.trim().length() > 0) {
      sql += " " + alias;
    } // end of if
    return sql;
  } // end of method getCriterionString
  
} // end of interface SQLFunction

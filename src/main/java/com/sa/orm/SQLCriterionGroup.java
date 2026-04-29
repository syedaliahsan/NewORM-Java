package com.sa.orm;

import java.util.Vector;

/**
 * <h4>Description</h4>Represents a criteria group. Enables the user
 * to defined different operators for one <code>Where</code> SQL
 * statement.
 * <h4>Copyrights</h4>Copyrights &copy; 2008 Soft Assets.
 * All Rights Reserved.
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2008
 * @author Ali Ahsan
 * @version 1.0
 */
public class SQLCriterionGroup implements SQLCriterion {

  /**
   * Object that holds added <code>SQLCriterion</code> child classes.
   */
  private Vector criteria = null;

  /**
   * Operator to be used between the criteria found in the the vector
   * object. It is not appended to last criterion.
   */
  private Enum operator = BOOLEAN_OPERATOR.AND;
  
  /**
   * Initializes vector object for later use.
   */
  private SQLCriterionGroup() {
    criteria = new Vector();
  } // end of no-argument constructor
  
  /**
   * Initializes vector object for later use and sets the given 
   * operator as operator to be used after the generated SQL.
   * @param operator Operator between criteria.
   */
  public SQLCriterionGroup(Enum operator) {
    this();
    this.operator = operator;
  } // end of constructor
  
  /**
   * Initializes vector object for later use and sets the given 
   * operator as operator to be used after the generated SQL.
   * @param criterion Criterion
   * @param operator Operator between criteria.
   */
  public SQLCriterionGroup(SQLCriterion criterion, Enum operator) {
    this();
    this.operator = operator;
    addCriterion(criterion);
    //criteria.add(new SQLCriterionGroup(criterion, operator));
  } // end of constructor
  
  /**
   * Initializes vector object for later use and sets the given 
   * operator as operator to be used after the generated SQL.
   * @param criteria Criteria
   * @param operator Operator between criteria.
   */
  public SQLCriterionGroup(SQLCriterion[] criteria, Enum operator) {
    this();
    this.operator = operator;
    addCriteria(criteria);
  } // end of constructor
  
  /**
   * Adds the given <code>criterion</code> in the vector.
   * @param criterion Criterion to be added to this group.
   */
  public void addCriterion(SQLCriterion criterion) {
    this.criteria.add(criterion);
  } // end of method addCriterion
  
  /**
   * Adds each member of the given <code>criteria</code> in the
   * vector.
   * @param criteria Criteria to be added to this group.
   */
  public void addCriteria(SQLCriterion[] criteria) {
    if(criteria == null || criteria.length < 1) {
      return;
    } // end of if

    for (int i = 0; i < criteria.length; i++) {
      addCriterion(criteria[i]);
    } // end of for
  } // end of method addCriteria
  
  /**
   * Returns a string that can be used as criteria in 
   * <code>Where</code> clause of an SQL Statement.
   * @return criteria String to be used in <code>Where</code> clause
   * of SQL statement.
   */
  public String getCriterionString() {
    StringBuffer sql = new StringBuffer();
    SQLCriterion criterion = null;
    String oper = null;
    if(this.criteria == null || this.criteria.size() < 1) {
      return "";
    } // end of if
    
    if(BOOLEAN_OPERATOR.AND.equals(operator)) {
      oper = " And ";
    } // end of if
    else {
      oper = " Or ";
    } // end of else

    sql.append("(");
    SQLCriterion[] criterionArray = (SQLCriterion[])this.criteria.toArray(new SQLCriterion[0]);
    for (int i = 0; i < criterionArray.length; i++) {
      criterion = criterionArray[i];
      sql.append(criterion.getCriterionString());
      if(i < (criterionArray.length - 1)) {
        sql.append(oper);
      } // end of if
    } // end of for
    sql.append(")");
    return sql.toString();
  } // end of method getCriterionString

} // end of class SQLCriterionGroup

package com.sa.orm;

import java.sql.SQLSyntaxErrorException;
import com.sa.orm.SQLCriterion.BOOLEAN_OPERATOR;

/**
 * <h4>Description</h4>Represents a <code>JOIN</code> in the <code>FROM</code>
 * clause of an SQL statement.
 * <p>It can be used independently in an SQL statement or can be added to
 * {@link FromClause} class, which in turn can be used in an SQL statement.</p>
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2011 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>May 05, 2010
 * @author Ali Ahsan
 * @version 1.0
 */
public class Join implements From {
  
  public static final int JOIN_TYPE_INNER = 1;
  
  public static final int JOIN_TYPE_LEFT_OUTER = 2;
  
  public static final int JOIN_TYPE_RIGHT_OUTER = 3;
  
  public static final int JOIN_TYPE_OUTER = 4;
  
  /**
   * Name of the table to be used in <code>FROM</code> clause.
   */
  protected FromElement leftSide;
  
  /**
   * Alias of the table to be used in <code>FROM</code> clause.
   * <p>It is ignored if empty</p>
   */
  protected FromElement rightSide;
  
  protected int joinType;
  
  /**
   * Key attributed to be used to define the <code>JOIN</code> between the two
   * tables in the <code>FROM</code> clause.
   * <p>It is used in the <code>JION</code> only if <code>linkedTable</code> is
   * also given with all the instance members.</p>
   */
  String key;
  
  /**
   * Object containing information to be used to specify <code>JOIN</code>
   * between <code>tableName</code> and the <code>tableName</code> specified in
   * <code>linkedTable</code> object.
   * <p>Note: <code>tableName</code> in <code>this</code> object is the left
   * side of the <code>JOIN</code></p>
   */
  Join linkedTable;
  
  protected SQLCriterion[] onCriteria;
  
  protected BOOLEAN_OPERATOR onCriteriaOperator;

  public Join() {
  }

  public Join(FromElement leftSide, FromElement rightSide, int joinType) {
    this.leftSide = leftSide;
    this.rightSide = rightSide;
    this.joinType = joinType;
  }

  public Join(FromElement leftSide, FromElement rightSide, int joinType, SQLCriterion[] onCriteria, BOOLEAN_OPERATOR onCriteriaOperator) {
    this.leftSide = leftSide;
    this.rightSide = rightSide;
    this.joinType = joinType;
    this.onCriteria = onCriteria;
    this.onCriteriaOperator = onCriteriaOperator;
  }
  
  public FromElement getLeftSide() {
    return leftSide;
  }

  public void setLeftSide(FromElement leftSide) {
    this.leftSide = leftSide;
  }

  public FromElement getRightSide() {
    return rightSide;
  }

  public void setRightSide(FromElement rightSide) {
    this.rightSide = rightSide;
  }

  public int getJoinType() {
    return joinType;
  }

  public void setJoinType(int joinType) {
    this.joinType = joinType;
  }

  public SQLCriterion[] getOnCriteria() {
    return onCriteria;
  }

  public void setOnCriteria(SQLCriterion[] onCriteria) {
    this.onCriteria = onCriteria;
  }

  public BOOLEAN_OPERATOR getOnCriteriaOperator() {
    return onCriteriaOperator;
  }

  public void setOnCriteriaOperator(BOOLEAN_OPERATOR onCriteriaOperator) {
    this.onCriteriaOperator = onCriteriaOperator;
  }

  /**
   * Returns the string that can be used as <code>FROM</code> clause, except
   * <code>FROM</code> keyword, of an SQL statement.
   * 
   * @return String to be used in <code>FROM</code> clause of an SQL statement.
   * 
   * @throws SQLSyntaxErrorException In case it can not generate a structurally
   * valid <code>FROM</code> clause code.
   * 
   * TODO Complete this method.
   */
  public String getFromString() throws SQLSyntaxErrorException {
     throw new UnsupportedOperationException("Use getJoinClause(SQLCriterionFactory) instead.");
  }
  
  public String getJoinClause(SQLCriterionFactory criterionFactory) throws SQLSyntaxErrorException {
    if(this.rightSide == null) {
      throw new SQLSyntaxErrorException("Right side of the join must be specifed.");
    }
    
    StringBuilder joinClause = new StringBuilder();
    switch (joinType) {
      case JOIN_TYPE_INNER:
        joinClause.append(" INNER JOIN ");
        break;
      case JOIN_TYPE_LEFT_OUTER:
        joinClause.append(" LEFT OUTER JOIN ");
        break;
      case JOIN_TYPE_RIGHT_OUTER:
        joinClause.append(" RIGHT OUTER JOIN ");
        break;
      case JOIN_TYPE_OUTER:
        joinClause.append(" FULL OUTER JOIN ");
        break;
      default:
        joinClause.append(" JOIN ");
        break;
    }
    
    joinClause.append(rightSide.getFromString());
    
    if (onCriteria != null && onCriteria.length > 0) {
      joinClause.append(" ON ");
      joinClause.append(criterionFactory.createCriteriaString(onCriteria, onCriteriaOperator));
    }
    return joinClause.toString();
  }
  
} // end of class Join
